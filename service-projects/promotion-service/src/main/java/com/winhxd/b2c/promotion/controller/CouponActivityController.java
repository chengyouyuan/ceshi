package com.winhxd.b2c.promotion.controller;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.CouponActivityAddCondition;
import com.winhxd.b2c.common.domain.promotion.condition.CouponActivityCondition;
import com.winhxd.b2c.common.domain.promotion.enums.CouponActivityEnum;
import com.winhxd.b2c.common.domain.promotion.util.ExcelUtil;
import com.winhxd.b2c.common.domain.promotion.util.ExcelVerifyResult;
import com.winhxd.b2c.common.domain.promotion.util.ImportResult;
import com.winhxd.b2c.common.domain.promotion.vo.CouponActivityImportStoreVO;
import com.winhxd.b2c.common.domain.promotion.vo.CouponActivityStoreVO;
import com.winhxd.b2c.common.domain.promotion.vo.CouponActivityVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.promotion.CouponActivityServiceClient;
import com.winhxd.b2c.promotion.service.CouponActivityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author sjx
 * @date 2018/8/6
 * @Description 优惠券活动相关入口
 */
@Api(tags = "CouponActivity")
@RestController
public class CouponActivityController implements CouponActivityServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(CouponActivityController.class);
    @Autowired
    private CouponActivityService couponActivityService;

    @Override
    @ApiOperation(value = "领券推券活动列表接口", notes = "领券推券活动列表接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")})
    public ResponseResult<PagedList<CouponActivityVO>> queryCouponActivity(@RequestBody CouponActivityCondition condition) {
        logger.info("/promotion/v1/queryCouponActivity/ 领券推券活动列表查询开始");
        ResponseResult<PagedList<CouponActivityVO>> result = new ResponseResult<PagedList<CouponActivityVO>>();
        result = couponActivityService.findCouponActivity(condition);
        logger.info("/promotion/v1/queryCouponActivity/ 领券推券活动列表查询结束");
        return result;
    }

    @ApiOperation(value = "优惠券活动导入小店信息", notes = "优惠券活动导入小店信息")
    @Override
    public ResponseResult<List<CouponActivityImportStoreVO>> couponActivityStoreImportExcel(@RequestBody MultipartFile inputfile) {
        ResponseResult<List<CouponActivityImportStoreVO>> result = new ResponseResult<List<CouponActivityImportStoreVO>>();
        if (!inputfile.getOriginalFilename().endsWith(".xls")
                && !inputfile.getOriginalFilename().endsWith(".xlsx")){
            result.setMessage("文件扩展名错误！");
            result.setCode(BusinessCode.CODE_1001);
            return result;
        }
        ImportResult<CouponActivityImportStoreVO> importResult = this.parseExcel(inputfile);
        List<ExcelVerifyResult> invalidList = importResult.getInvalidList(1);
        if (invalidList.isEmpty()) {
            List<CouponActivityImportStoreVO> list = importResult.getList();
            result.setData(list);
        }
        result.setCode(BusinessCode.CODE_OK);
        result.setMessage("返回小店导入信息成功");
        return result;
    }

    private ImportResult<CouponActivityImportStoreVO> parseExcel(MultipartFile excel) {
        ImportResult<CouponActivityImportStoreVO> importResult = null;
        try {
            importResult = ExcelUtil.importExcelVerify(excel.getInputStream(), CouponActivityImportStoreVO.class);
        } catch (Exception e) {
            throw new BusinessException(BusinessCode.CODE_1001, e);
        }
        return importResult;
    }

    /**
     *
     *@Deccription 添加优惠券活动
     *@Params  condition
     *@Return  ResponseResult
     *@User  sjx
     *@Date   2018/8/6
     */
    @Override
    @ApiOperation(value = "添加优惠券活动", notes = "添加优惠券活动")
    public ResponseResult<Integer> addCouponActivity(@RequestBody CouponActivityAddCondition condition) {
        //判断必填参数
        if(null == condition){
            throw new BusinessException(BusinessCode.CODE_1007);
        }
        if(null == condition.getType()){
            throw new BusinessException(BusinessCode.CODE_1007);
        }
        //领券
        if(condition.getType() == CouponActivityEnum.PULL_COUPON.getCode()){
            if (StringUtils.isBlank(condition.getName()) && condition.getCouponActivityTemplateList() == null
                    && StringUtils.isBlank(condition.getExolian()) && StringUtils.isBlank(condition.getRemarks())
                    && condition.getActivityStart()==null && condition.getActivityEnd() == null) {
                throw new BusinessException(BusinessCode.CODE_1007);
            }
            if(condition.getCouponActivityTemplateList().get(0).getCouponNumType() == null
                    && condition.getCouponActivityTemplateList().get(0).getCouponNum() == null
                    && condition.getCouponActivityTemplateList().get(0).getCustomerVoucherLimitType() == null
                    && condition.getCouponActivityTemplateList().get(0).getStartTime() == null
                    && condition.getCouponActivityTemplateList().get(0).getEndTime() == null
                    && condition.getCouponActivityTemplateList().get(0).getTemplateId() == null ){
                throw new BusinessException(BusinessCode.CODE_1007);
            }
            if(condition.getCouponActivityTemplateList().get(0).getCustomerVoucherLimitType() == CouponActivityEnum.STORE_LIMITED.getCode()
                    && condition.getCouponActivityTemplateList().get(0).getCustomerVoucherLimitNum() == null){
                throw new BusinessException(BusinessCode.CODE_1007);
            }
            if(condition.getCouponActivityTemplateList().get(0).getCouponActivityStoreCustomerList().get(0).getStoreId() == null){
                throw new BusinessException(BusinessCode.CODE_1007);
            }
        }
        //推券
        if(condition.getType() == CouponActivityEnum.PUSH_COUPON.getCode()){
            if (StringUtils.isBlank(condition.getName()) && condition.getCouponActivityTemplateList() == null
                    && StringUtils.isBlank(condition.getExolian()) && StringUtils.isBlank(condition.getRemarks())
                    && condition.getCouponType() == null && condition.getActivityStart() == null
                    && condition.getActivityEnd() == null) {
                throw new BusinessException(BusinessCode.CODE_1007);
            }
            if(condition.getCouponActivityTemplateList().get(0).getTemplateId() == null
                    && condition.getCouponActivityTemplateList().get(0).getStartTime() == null
                    && condition.getCouponActivityTemplateList().get(0).getEndTime() == null
                    && condition.getCouponActivityTemplateList().get(0).getEffectiveDays() == null
                    && condition.getCouponActivityTemplateList().get(0).getSendNum() == null){
                throw new BusinessException(BusinessCode.CODE_1007);
            }
        }
        //区域信息验证
        //导入没有区域信息
        //if(condition.getCouponActivityAreaList() == null){
        //    throw new BusinessException(BusinessCode.CODE_1007);
        //}
        //if(condition.getCouponActivityAreaList().get(0).getRegionCode() == null
        //        && condition.getCouponActivityAreaList().get(0).getRegionName() == null){
        //    throw new BusinessException(BusinessCode.CODE_1007);
        //}

        ResponseResult responseResult = new ResponseResult();
        couponActivityService.saveCouponActivity(condition);
        responseResult.setCode(BusinessCode.CODE_OK);
        responseResult.setMessage("添加成功！");
        return responseResult;
    }

    /**
     *
     *@Deccription 查看优惠券活动
     *@Params  condition
     *@Return  ResponseResult
     *@User  sjx
     *@Date   2018/8/8
     */
    @ApiOperation(value = "优惠券活动查看&回显编辑页", notes = "优惠券活动查看&回显编辑页")
    @Override
    public ResponseResult<CouponActivityVO> getCouponActivityById(String id) {
        if(StringUtils.isBlank(id)){
            throw new BusinessException(BusinessCode.CODE_1007);
        }
        logger.info("优惠券活动查看&回显编辑页入参id: " +id);
        ResponseResult responseResult = new ResponseResult();
        CouponActivityVO couponActivityVO = couponActivityService.getCouponActivityById(id);
        if(couponActivityVO!=null){
            responseResult.setData(couponActivityVO);
            responseResult.setCode(BusinessCode.CODE_OK);
        }
        return responseResult;
    }



    /**
     *
     *@Deccription 编辑优惠券活动
     *@Params  condition
     *@Return  ResponseResult
     *@User  sjx
     *@Date   2018/8/7
     */
    @Override
    @ApiOperation(value = "编辑优惠券活动", notes = "编辑优惠券活动")
    public ResponseResult<Integer> updateCouponActivity(@RequestBody CouponActivityAddCondition condition) {
        //判断必填参数
        //活动有效期内不允许修改活动！！
        Date activityStart = condition.getActivityStart();
        Date activityEnd = condition.getActivityEnd();
        Date now =  new Date();
        if(now.before(activityStart) && now.after(activityEnd)){
            throw new BusinessException(BusinessCode.CODE_1003);
        }
        if(null == condition){
            throw new BusinessException(BusinessCode.CODE_1007);
        }
        if(null == condition.getType()){
            throw new BusinessException(BusinessCode.CODE_1007);
        }
        //领券
        if(condition.getType() == CouponActivityEnum.PULL_COUPON.getCode()){
            if (StringUtils.isBlank(condition.getName()) && condition.getCouponActivityTemplateList() == null
                    && StringUtils.isBlank(condition.getExolian()) && StringUtils.isBlank(condition.getRemarks())
                    && condition.getActivityStart()==null && condition.getActivityEnd() == null) {
                throw new BusinessException(BusinessCode.CODE_1007);
            }
            if(condition.getCouponActivityTemplateList().get(0).getCouponNumType() == null
                    && condition.getCouponActivityTemplateList().get(0).getCouponNum() == null
                    && condition.getCouponActivityTemplateList().get(0).getCustomerVoucherLimitType() == null
                    && condition.getCouponActivityTemplateList().get(0).getStartTime() == null
                    && condition.getCouponActivityTemplateList().get(0).getEndTime() == null
                    && condition.getCouponActivityTemplateList().get(0).getTemplateId() == null ){
                throw new BusinessException(BusinessCode.CODE_1007);
            }
            if(condition.getCouponActivityTemplateList().get(0).getCustomerVoucherLimitType() == CouponActivityEnum.STORE_LIMITED.getCode()
                    && condition.getCouponActivityTemplateList().get(0).getCustomerVoucherLimitNum() == null){
                throw new BusinessException(BusinessCode.CODE_1007);
            }
            if(condition.getCouponActivityTemplateList().get(0).getCouponActivityStoreCustomerList().get(0).getStoreId() == null){
                throw new BusinessException(BusinessCode.CODE_1007);
            }
        }
        //推券
        if(condition.getType() == CouponActivityEnum.PUSH_COUPON.getCode()){
            if (StringUtils.isBlank(condition.getName()) && condition.getCouponActivityTemplateList() == null
                    && StringUtils.isBlank(condition.getExolian()) && StringUtils.isBlank(condition.getRemarks())
                    && condition.getCouponType() == null && condition.getActivityStart() == null
                    && condition.getActivityEnd() == null) {
                throw new BusinessException(BusinessCode.CODE_1007);
            }
            if(condition.getCouponActivityTemplateList().get(0).getTemplateId() == null
                    && condition.getCouponActivityTemplateList().get(0).getStartTime() == null
                    && condition.getCouponActivityTemplateList().get(0).getEndTime() == null
                    && condition.getCouponActivityTemplateList().get(0).getEffectiveDays() == null
                    && condition.getCouponActivityTemplateList().get(0).getCustomerVoucherLimitNum() == null){
                throw new BusinessException(BusinessCode.CODE_1007);
            }
        }

        ResponseResult responseResult = new ResponseResult();
        couponActivityService.updateCouponActivity(condition);
        responseResult.setCode(BusinessCode.CODE_OK);
        return responseResult;
    }

    /**
     *
     *@Deccription 删除优惠券活动（设为无效）
     *@Params  id
     *@Return  ResponseResult
     *@User  sjx
     *@Date   2018/8/8
     */
    @ApiOperation(value = "删除优惠券活动", notes = "删除优惠券活动")
    @Override
    public ResponseResult<Integer> deleteCouponActivity(CouponActivityCondition condition) {
        if(condition.getId() == null){
            throw new BusinessException(BusinessCode.CODE_1007);
        }
        logger.info("deleteCouponActivity--Id:" + condition.getId());
        ResponseResult responseResult = new ResponseResult();
        couponActivityService.deleteCouponActivity(condition);
        responseResult.setCode(BusinessCode.CODE_OK);
        responseResult.setMessage("删除成功");

        return responseResult;
    }

    /**
     *
     *@Deccription 撤回活动优惠券
     *@Params  id
     *@Return  ResponseResult
     *@User  sjx
     *@Date   2018/8/9
     */
    @ApiOperation(value = "撤回活动优惠券", notes = "撤回活动优惠券")
    @Override
    public ResponseResult<Integer> revocationActivityCoupon(CouponActivityCondition condition) {
        if(condition.getId() == null){
            throw new BusinessException(BusinessCode.CODE_1007);
        }
        logger.info("revocationActivityCoupon--Id:" + condition.getId());
        ResponseResult responseResult = new ResponseResult();
        couponActivityService.revocationActivityCoupon(condition);
        responseResult.setCode(BusinessCode.CODE_OK);
        responseResult.setMessage("撤销成功");

        return responseResult;
    }

    /**
     *
     *@Deccription 停用/开启活动
     *@Params  condition
     *@Return  ResponseResult
     *@User  sjx
     *@Date   2018/8/9
     */
    @Override
    public ResponseResult<Integer> updateCouponActivityStatus(CouponActivityAddCondition condition) {
        //判断必填参数
        if(condition == null){
            throw new BusinessException(BusinessCode.CODE_1007);
        }
        if(condition.getId() == null && condition.getActivityStatus() == null){
            throw new BusinessException(BusinessCode.CODE_1007);
        }
        ResponseResult responseResult = new ResponseResult();
        couponActivityService.updateCouponActivityStatus(condition);
        responseResult.setCode(BusinessCode.CODE_OK);
        return responseResult;
    }

    /**
     *
     *@Deccription 根据活动获取优惠券列表
     *@Params  condition
     *@Return  ResponseResult
     *@User  sjx
     *@Date   2018/8/9
     */
    @ApiOperation(value = "根据活动获取优惠券列表", notes = "根据活动获取优惠券列表")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")})
    @Override
    public ResponseResult<PagedList<CouponActivityStoreVO>> queryCouponByActivity(CouponActivityCondition condition) {
        if(condition == null){
            throw new BusinessException(BusinessCode.CODE_1007);
        }
        if(condition.getId() == null){
            throw new BusinessException(BusinessCode.CODE_1007);
        }
        logger.info("queryCouponByActivity--Id:" + condition.getId());
        ResponseResult<PagedList<CouponActivityStoreVO>> result = new ResponseResult<PagedList<CouponActivityStoreVO>>();
        result = couponActivityService.findCouponByActivity(condition);
        return result;
    }

    /**
     *
     *@Deccription 根据活动获取小店信息
     *@Params  condition
     *@Return  ResponseResult
     *@User  sjx
     *@Date   2018/8/9
     */
    @ApiOperation(value = "根据活动获取小店信息", notes = "根据活动获取小店信息")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")})
    @Override
    public ResponseResult<PagedList<CouponActivityStoreVO>> queryStoreByActivity(CouponActivityCondition condition) {
        if(condition == null){
            throw new BusinessException(BusinessCode.CODE_1007);
        }
        if(condition.getId() == null){
            throw new BusinessException(BusinessCode.CODE_1007);
        }
        logger.info("queryStoreByActivity--Id:" + condition.getId());
        ResponseResult<PagedList<CouponActivityStoreVO>> result = new ResponseResult<PagedList<CouponActivityStoreVO>>();
        result = couponActivityService.findStoreByActivity(condition);
        return result;
    }
}
