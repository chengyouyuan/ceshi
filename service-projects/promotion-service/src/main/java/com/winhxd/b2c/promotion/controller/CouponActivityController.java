package com.winhxd.b2c.promotion.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.customer.vo.CustomerUserInfoExportVO;
import com.winhxd.b2c.common.domain.customer.vo.CustomerUserInfoVO;
import com.winhxd.b2c.common.domain.promotion.condition.CouponActivityAddCondition;
import com.winhxd.b2c.common.domain.promotion.condition.CouponActivityCondition;
import com.winhxd.b2c.common.domain.promotion.enums.CouponActivityEnum;
import com.winhxd.b2c.common.domain.promotion.vo.CouponActivityImportCustomerVO;
import com.winhxd.b2c.common.domain.promotion.vo.CouponActivityImportStoreVO;
import com.winhxd.b2c.common.domain.promotion.vo.CouponActivityStoreVO;
import com.winhxd.b2c.common.domain.promotion.vo.CouponActivityVO;
import com.winhxd.b2c.common.domain.store.condition.StoreListByKeywordsCondition;
import com.winhxd.b2c.common.domain.store.vo.StoreUserInfoVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.customer.CustomerServiceClient;
import com.winhxd.b2c.common.feign.promotion.CouponActivityServiceClient;
import com.winhxd.b2c.common.feign.store.StoreServiceClient;
import com.winhxd.b2c.promotion.service.CouponActivityService;
import com.winhxd.b2c.promotion.service.CouponPushService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author sjx.
 * @date 2018/8/6
 * @Description 优惠券活动相关入口
 */
@RestController
public class CouponActivityController implements CouponActivityServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(CouponActivityController.class);
    @Autowired
    private CouponActivityService couponActivityService;
    @Autowired
    private StoreServiceClient storeServiceClient;
    @Autowired
    private CustomerServiceClient customerServiceClient;
    @Autowired
    private CouponPushService couponPushService;

    /**
     *
     *@Deccription 查询优惠券活动
     *@Params  CouponActivityCondition
     *@Return  ResponseResult
     *@User  sjx
     *@Date   2018/8/7
     */
    @Override
    public ResponseResult<PagedList<CouponActivityVO>> queryCouponActivity(@RequestBody CouponActivityCondition condition) {
        logger.info("/promotion/v1/queryCouponActivity/ 领券推券活动列表查询开始");
        ResponseResult<PagedList<CouponActivityVO>> result = new ResponseResult<PagedList<CouponActivityVO>>();
        PagedList<CouponActivityVO> pagedList = new PagedList<>();
        PageHelper.startPage(condition.getPageNo(),condition.getPageSize());
        List<CouponActivityVO> activity = couponActivityService.findCouponActivity(condition);
        PageInfo<CouponActivityVO> pageInfo = new PageInfo<>(activity);
        pagedList.setData(pageInfo.getList());
        pagedList.setPageNo(pageInfo.getPageNum());
        pagedList.setPageSize(pageInfo.getPageSize());
        pagedList.setTotalRows(pageInfo.getTotal());
        result.setData(pagedList);
        logger.info("/promotion/v1/queryCouponActivity/ 领券推券活动列表查询结束");
        return result;
    }
    /**
     *
     *@Deccription 导入excel，返回小店信息
     *@Params  List<CouponActivityImportStoreVO>
     *@Return  ResponseResult
     *@User  sjx
     *@Date   2018/8/19
     */
    @Override
    public ResponseResult<List<StoreUserInfoVO>> couponActivityStoreImportExcel(@RequestBody List<CouponActivityImportStoreVO> list) {
        if(CollectionUtils.isEmpty(list)){
            throw new BusinessException(BusinessCode.CODE_1007);
        }
        ResponseResult<List<StoreUserInfoVO>> result = new ResponseResult<List<StoreUserInfoVO>>();

        List<Long> storeIdList = list.stream().map(caiv -> Long.valueOf(caiv.getStoreId())).distinct().collect(Collectors.toList());

        //调接口判断数据有效性
        ResponseResult<List<StoreUserInfoVO>> responseResult = new ResponseResult<List<StoreUserInfoVO>>();
        StoreListByKeywordsCondition storeListByKeywordsCondition = new StoreListByKeywordsCondition();
        storeListByKeywordsCondition.setStoreIds(storeIdList);
        responseResult =  storeServiceClient.getStoreListByKeywords(storeListByKeywordsCondition);
        List<StoreUserInfoVO> storeUserInfoVOList= responseResult.getData();
        result.setData(storeUserInfoVOList);
        result.setCode(BusinessCode.CODE_OK);
        result.setMessage("返回小店导入信息成功");
        return result;
    }


    @Override
    public ResponseResult<List<CustomerUserInfoVO>> couponActivityCustomerUserImportExcel(@RequestBody List<CouponActivityImportCustomerVO> list) {
        if(CollectionUtils.isEmpty(list)){
            throw new BusinessException(BusinessCode.CODE_1007);
        }
        ResponseResult<List<CustomerUserInfoVO>> result = new ResponseResult<List<CustomerUserInfoVO>>();

        List<String> userPhoneList = list.stream().map(caic -> caic.getPhone()).distinct().collect(Collectors.toList());

        //调接口判断数据有效性
        ResponseResult<List<CustomerUserInfoVO>> responseResult = new ResponseResult<List<CustomerUserInfoVO>>();
        responseResult = customerServiceClient.findCustomerUserByPhones(userPhoneList);
        List<CustomerUserInfoVO> customerUserInfoVOList= responseResult.getData();
        result.setData(customerUserInfoVOList);
        result.setCode(BusinessCode.CODE_OK);
        result.setMessage("返回用户导入信息成功");
        return result;
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
    public ResponseResult<Integer> addCouponActivity(@RequestBody CouponActivityAddCondition condition) {
        //判断必填参数
        addOrUpdateVerifyParam(condition);
        ResponseResult<Integer> responseResult = new ResponseResult<Integer>();
        //判断活动时间是否冲突(推券&新人注册)
        if(condition.getType() == CouponActivityEnum.PUSH_COUPON.getCode()
                && condition.getCouponType() == CouponActivityEnum.NEW_USER.getCode()){
            Boolean flag = couponActivityService.getActivityDateClash(condition);
            if(flag){
                responseResult.setCode(BusinessCode.CODE_503002);
                responseResult.setMessage("活动时间冲突");
                return responseResult;
            }
        }

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
    @Override
    public ResponseResult<CouponActivityVO> getCouponActivityById(String id) {
        if(StringUtils.isBlank(id)){
            throw new BusinessException(BusinessCode.CODE_1007);
        }
        logger.info("优惠券活动查看&回显编辑页入参id: " +id);
        ResponseResult<CouponActivityVO> responseResult = new ResponseResult<>();
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
    public ResponseResult<Integer> updateCouponActivity(@RequestBody CouponActivityAddCondition condition) {
        //判断必填参数
        addOrUpdateVerifyParam(condition);

        ResponseResult<Integer> responseResult = new ResponseResult<Integer>();
        //判断活动时间是否冲突(推券&新人注册)
        if(condition.getType() == CouponActivityEnum.PUSH_COUPON.getCode()
                && condition.getCouponType() == CouponActivityEnum.NEW_USER.getCode()){
            Boolean flag = couponActivityService.getActivityDateClash(condition);
            if(flag){
                responseResult.setCode(BusinessCode.CODE_503002);
                responseResult.setMessage("活动时间冲突");
                return responseResult;
            }
        }

        //活动有效期内不允许修改活动！！
        Date activityStart = condition.getActivityStart();
        Date activityEnd = condition.getActivityEnd();
        Date now =  new Date();
        if(now.before(activityStart) && now.after(activityEnd)){
            throw new BusinessException(BusinessCode.CODE_1003);
        }

        couponActivityService.updateCouponActivity(condition);
        responseResult.setCode(BusinessCode.CODE_OK);
        return responseResult;
    }

    /**
     * 新增和修改方法验证参数
     * @param condition
     * @return
     */
    public void addOrUpdateVerifyParam (CouponActivityAddCondition condition){
        //判断必填参数
        if(null == condition){
            throw new BusinessException(BusinessCode.CODE_1007);
        }
        if(null == condition.getType()){
            throw new BusinessException(BusinessCode.CODE_1007);
        }
        if(!CollectionUtils.isEmpty(condition.getCouponActivityCustomerList())
                && !CollectionUtils.isEmpty(condition.getCouponActivityTemplateList().get(0).getCouponActivityStoreCustomerList())){
            throw new BusinessException(BusinessCode.CODE_503701);
        }
        //判断推券和领券活动的每类券，最多给门店推送100张或让门店可领100张。
        Integer maxNum = 100;
        //领券
        if(condition.getType() == CouponActivityEnum.PULL_COUPON.getCode()){
            if (StringUtils.isBlank(condition.getName()) || condition.getCouponActivityTemplateList() == null
                    || StringUtils.isBlank(condition.getExolian()) || StringUtils.isBlank(condition.getRemarks())
                    || condition.getActivityStart()==null || condition.getActivityEnd() == null) {
                throw new BusinessException(BusinessCode.CODE_1007);
            }
            if(condition.getCouponActivityTemplateList().get(0).getCouponNumType() == null
                    || condition.getCouponActivityTemplateList().get(0).getCouponNum() == null
                    || condition.getCouponActivityTemplateList().get(0).getCustomerVoucherLimitType() == null
                    || condition.getCouponActivityTemplateList().get(0).getStartTime() == null
                    || condition.getCouponActivityTemplateList().get(0).getEndTime() == null
                    || condition.getCouponActivityTemplateList().get(0).getTemplateId() == null ){
                throw new BusinessException(BusinessCode.CODE_1007);
            }
            if(condition.getActivityEnd().after(condition.getCouponActivityTemplateList().get(0).getEndTime())){
                throw new BusinessException(BusinessCode.CODE_503003);
            }
            if(condition.getCouponActivityTemplateList().get(0).getCustomerVoucherLimitType() == CouponActivityEnum.STORE_LIMITED.getCode()
                    && condition.getCouponActivityTemplateList().get(0).getCustomerVoucherLimitNum() == null){
                throw new BusinessException(BusinessCode.CODE_1007);
            }
            if(condition.getCouponActivityTemplateList().get(0).getCustomerVoucherLimitType() == CouponActivityEnum.STORE_LIMITED.getCode()) {
                if (condition.getCouponActivityTemplateList().get(0).getCustomerVoucherLimitNum()>maxNum){
                    throw new BusinessException(BusinessCode.CODE_503004);
                }
            }
            if (!CollectionUtils.isEmpty(condition.getCouponActivityTemplateList().get(0).getCouponActivityStoreCustomerList())){
                if(condition.getCouponActivityTemplateList().get(0).getCouponActivityStoreCustomerList().get(0).getStoreId() == null){
                    throw new BusinessException(BusinessCode.CODE_1007);
                }
            }
        }
        //推券
        if(condition.getType() == CouponActivityEnum.PUSH_COUPON.getCode()){
            if (StringUtils.isBlank(condition.getName()) || condition.getCouponActivityTemplateList() == null
                    || StringUtils.isBlank(condition.getExolian()) || StringUtils.isBlank(condition.getRemarks())
                    || condition.getCouponType() == null || condition.getActivityStart() == null
                    || condition.getActivityEnd() == null) {
                throw new BusinessException(BusinessCode.CODE_1007);
            }
            if(condition.getCouponActivityTemplateList().get(0).getTemplateId() == null
                    || (condition.getCouponActivityTemplateList().get(0).getEffectiveDays() == null && condition.getCouponActivityTemplateList().get(0).getStartTime() == null)
                    || condition.getCouponActivityTemplateList().get(0).getSendNum() == null){
                throw new BusinessException(BusinessCode.CODE_1007);
            }
            if (condition.getCouponActivityTemplateList().get(0).getSendNum()>maxNum){
                throw new BusinessException(BusinessCode.CODE_503004);
            }
            Integer sendNum = condition.getCouponActivityTemplateList().get(0).getSendNum();
            Integer couponNum = condition.getCouponActivityTemplateList().get(0).getCouponNum();
            if(sendNum<=0 || couponNum<=0 || (couponNum%sendNum != 0) || couponNum<sendNum){
                throw new BusinessException(BusinessCode.CODE_503601);
            }
        }

    }


    /**
     *
     *@Deccription 删除优惠券活动（设为无效）
     *@Params  id
     *@Return  ResponseResult
     *@User  sjx
     *@Date   2018/8/8
     */
    @Override
    public ResponseResult<Integer> deleteCouponActivity(CouponActivityCondition condition) {
        if(condition.getId() == null){
            throw new BusinessException(BusinessCode.CODE_1007);
        }
        logger.info("deleteCouponActivity--Id:" + condition.getId());
        ResponseResult<Integer> responseResult = new ResponseResult<>();
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
    @Override
    public ResponseResult<Integer> revocationActivityCoupon(@RequestBody CouponActivityCondition condition) {
        if(condition.getId() == null){
            throw new BusinessException(BusinessCode.CODE_1007);
        }
        logger.info("revocationActivityCoupon--Id:" + condition.getId());
        ResponseResult<Integer> responseResult = new ResponseResult<>();
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
    public ResponseResult<Integer> updateCouponActivityStatus(@RequestBody CouponActivityAddCondition condition) {
        //判断必填参数
        if(condition == null){
            throw new BusinessException(BusinessCode.CODE_1007);
        }
        if(condition.getId() == null && condition.getActivityStatus() == null){
            throw new BusinessException(BusinessCode.CODE_1007);
        }
        //一期只有停止活动
        if(condition.getActivityStatus()==CouponActivityEnum.ACTIVITY_OPEN.getCode()){
            condition.setActivityStatus(CouponActivityEnum.ACTIVITY_STOP.getCode());
        }
        ResponseResult<Integer> responseResult = new ResponseResult<>();
        couponActivityService.updateCouponActivityStatus(condition);
        responseResult.setCode(BusinessCode.CODE_OK);
        responseResult.setMessage("停止活动成功！");
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
    @Override
    public ResponseResult<PagedList<CouponActivityStoreVO>> queryCouponByActivity(@RequestBody CouponActivityCondition condition) {
        if(condition == null){
            throw new BusinessException(BusinessCode.CODE_1007);
        }
        if(condition.getId() == null){
            throw new BusinessException(BusinessCode.CODE_1007);
        }
        logger.info("queryCouponByActivity--Id:" + condition.getId());
        ResponseResult<PagedList<CouponActivityStoreVO>> result = new ResponseResult<PagedList<CouponActivityStoreVO>>();
        PagedList<CouponActivityStoreVO> pagedList = new PagedList<>();
        PageHelper.startPage(condition.getPageNo(),condition.getPageSize());
        List<CouponActivityStoreVO> coupon = couponActivityService.findCouponByActivity(condition);
        PageInfo<CouponActivityStoreVO> pageInfo = new PageInfo<>(coupon);
        pagedList.setData(pageInfo.getList());
        pagedList.setPageNo(pageInfo.getPageNum());
        pagedList.setPageSize(pageInfo.getPageSize());
        pagedList.setTotalRows(pageInfo.getTotal());
        result.setData(pagedList);
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
    @Override
    public ResponseResult<PagedList<CouponActivityStoreVO>> queryStoreByActivity(@RequestBody CouponActivityCondition condition) {
        if(condition == null){
            throw new BusinessException(BusinessCode.CODE_1007);
        }
        if(condition.getId() == null){
            throw new BusinessException(BusinessCode.CODE_1007);
        }
        logger.info("queryStoreByActivity--Id:" + condition.getId());
        ResponseResult<PagedList<CouponActivityStoreVO>> result = new ResponseResult<PagedList<CouponActivityStoreVO>>();
        PagedList<CouponActivityStoreVO> pagedList = new PagedList<>();
        PageHelper.startPage(condition.getPageNo(),condition.getPageSize());
        List<CouponActivityStoreVO> store = couponActivityService.findStoreByActivity(condition);
        PageInfo<CouponActivityStoreVO> pageInfo = new PageInfo<>(store);
        pagedList.setData(pageInfo.getList());
        pagedList.setPageNo(pageInfo.getPageNum());
        pagedList.setPageSize(pageInfo.getPageSize());
        pagedList.setTotalRows(pageInfo.getTotal());
        result.setData(pagedList);
        return result;
    }

    @Override
    public ResponseResult<List<CustomerUserInfoExportVO>> queryCustomerByActivity(@RequestBody CouponActivityCondition condition) {
        if(condition == null){
            throw new BusinessException(BusinessCode.CODE_1007);
        }
        if(condition.getId() == null){
            throw new BusinessException(BusinessCode.CODE_1007);
        }
        logger.info("queryCustomerByActivity--Id:" + condition.getId());
        ResponseResult<List<CustomerUserInfoExportVO>> result = new ResponseResult<List<CustomerUserInfoExportVO>>();
        List<CustomerUserInfoExportVO> list = new ArrayList<>();

        List<Long> customerIds = couponPushService.getCustomerIdByActiveId(condition.getId());
        ResponseResult<List<CustomerUserInfoVO>> customerUserResult = customerServiceClient.findCustomerUserByIds(customerIds);
        List<CustomerUserInfoVO> datas = customerUserResult.getData();
        if(!CollectionUtils.isEmpty(datas)){
            for(CustomerUserInfoVO customerUserInfoVO : datas){
                CustomerUserInfoExportVO cuie = new CustomerUserInfoExportVO();
                BeanUtils.copyProperties(customerUserInfoVO,cuie);
                list.add(cuie);
            }
        } else {
            throw new BusinessException(BusinessCode.CODE_500014);
        }
        result.setData(list);
        return result;
    }
}
