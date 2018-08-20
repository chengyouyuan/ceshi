package com.winhxd.b2c.common.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.common.model.BaseFile;
import com.winhxd.b2c.common.domain.common.model.BaseImageFile;
import com.winhxd.b2c.common.exception.BusinessException;

/**
 * 上传图片工具类
 * 
 * @ClassName: ImageUploadUtil
 * @Description: TODO
 * @author: wuyuanbao
 * @date: 2018年8月11日 上午9:51:00
 */
@Component
public class ImageUploadUtil {
	private static final Logger logger = LoggerFactory.getLogger(ImageUploadUtil.class);
	private static final int SUCCESS_CODE = 200;
	private static final int NUMBER_1024=1024;
	private static final String JPG="jpg";
	private static final String JPEG="jpeg";
    private static final String PNG="png";

	@Value("${cdn.picture}")
	private String baseHost="";

	@Autowired
	private  UploadUtil uploadUtil;

	/**
	 * 300kb
	 */
	private static final long KB_300 = 300 * 1024;

	/**
	 * 上传图片
	* @Title: uploadImage 
	* @Description: TODO 
	* @param imageFile MultipartFile（form提交）
	* @param size 上传图片限制大小 单位是KB（不传默认限制图片大小300KB）
	* @return
	* @throws Exception
	* @author wuyuanbao
	* @date 2018年8月16日上午10:09:57
	 */
	public BaseImageFile uploadImage(MultipartFile imageFile, Long size) throws Exception {
		//ResponseResult<ProductImageVO> result = new ResponseResult<>();
		BaseImageFile baseImageFile = null;

		String postFix;
		if (imageFile == null) {
			logger.error("上传图片：图片不能为空！");
			throw   new BusinessException(BusinessCode.CODE_1020);
			//result = new ResponseResult<>();
			//return result;
		}
	   return 	uploadImage(imageFile.getOriginalFilename(),imageFile.getInputStream(),null);
//		String fileName=imageFile.getOriginalFilename();
//		if (StringUtils.isEmpty(fileName)) {
//			logger.error("上传图片：图片名称不能为空！");
//			//result = new ResponseResult<>(BusinessCode.CODE_1020);
//			throw   new BusinessException(BusinessCode.CODE_1019);
//			//result.setData(imageVo);
//			//return result;
//		}
//
//		long fileSize = imageFile.getSize();
//		if (size != null && size > 0) {
//			// 判断图片大小是否超过限制大小，单位KB
//			if (size * 1024 < fileSize) {
//				logger.error("上传图片：" + fileName + ",size:" + fileSize + ",图片大小超过" + size + "KB!");
//				throw   new BusinessException(BusinessCode.CODE_1018);
//				//result = new ResponseResult<>(BusinessCode.CODE_1018);
//				//return result;
//			}
//		} else {
//			// 判断图片大小是否超过300KB
//			if (KB_300 < fileSize) {
//				logger.error("上传图片：" + fileName + ",size:" + fileSize + ",图片大小超过300KB!");
//				throw   new BusinessException(BusinessCode.CODE_1018);
//				//result = new ResponseResult<>(BusinessCode.CODE_1018);
//				//return result;
//			}
//		}
//
//		int postFixIndex;
//
//		postFixIndex = fileName.lastIndexOf(".");
//		postFix = fileName.substring(postFixIndex);
//		if (postFix.equalsIgnoreCase(".jpg") || postFix.equalsIgnoreCase(".JPG") || postFix.equalsIgnoreCase(".jpeg")
//				|| postFix.equalsIgnoreCase(".JPEG") || postFix.equalsIgnoreCase(".png")
//				|| postFix.equalsIgnoreCase(".PNG")) {
//			// 上传文件
//			BaseFile baseFile = uploadUtil.httpClientUploadFile(fileName, imageFile.getInputStream());
//			if (baseFile == null) {
//				logger.error("上传图片：" + fileName + ",size:" + fileSize + ",图片上传失败!");
//				throw  new BusinessException(BusinessCode.CODE_1018);
//				//result = new ResponseResult<>(BusinessCode.CODE_1017);
//				//return result;
//			}else{
//				baseImageFile = new BaseImageFile();
//				baseImageFile.setName(baseFile.getName());
//				baseImageFile.setUrl(baseFile.getUrl());
//			}
//		} else {
//			logger.error("上传图片：" + fileName + ",size:" + fileSize + ",图片格式不正确!");
//			throw  new BusinessException(BusinessCode.CODE_1016);
//			//result = new ResponseResult<>(BusinessCode.CODE_1016);
//			//result.setData(imageVo);
//			//return result;
//		}
//		return baseImageFile;
	}

	/**
	 * 上传图片
	* @Title: uploadImage 
	* @Description: TODO 
	* @param fileName 图片名称
	* @param inputStream 流
	* @param size 上传图片限制大小 单位是KB（不传默认限制图片大小300KB）
	* @return
	* @throws IOException
	* @author wuyuanbao
	* @date 2018年8月16日上午10:11:17
	 */
	public BaseImageFile uploadImage(String fileName,InputStream inputStream,Long size) throws Exception {
//		ResponseResult<ProductImageVO> result = new ResponseResult<>();
//		ProductImageVO imageVo = new ProductImageVO();
		BaseImageFile baseImageFile = null;
		String postFix;

		if (StringUtils.isEmpty(fileName)) {
			logger.error("上传图片：图片名称不能为空！");
			throw new BusinessException(BusinessCode.CODE_1019);
			//logger.error("上传图片：图片名称不能为空！");
			//result = new ResponseResult<>(BusinessCode.CODE_1019);
			//result.setData(imageVo);
			//return result;
		}
		if (inputStream == null) {
			logger.error("上传图片：图片不能为空！");
			throw new BusinessException(BusinessCode.CODE_1020);
			//result.setData(imageVo);
			//return result;
		}
		long fileSize = inputStream.available();
		if (size != null && size > 0) {
			// 判断图片大小是否超过限制大小，单位KB
			if (size * NUMBER_1024 < fileSize) {
				logger.error("上传图片：" + fileName + ",size:" + fileSize + ",图片大小超过" + size + "KB!");
				throw new BusinessException(BusinessCode.CODE_1018);
				//result = new ResponseResult<>(BusinessCode.CODE_1018);
				//return result;
			}
		} else {
			// 判断图片大小是否超过300KB
			if (KB_300 < fileSize) {
				logger.error("上传图片：" + fileName + ",size:" + fileSize + ",图片大小超过300KB!");
				throw new BusinessException(BusinessCode.CODE_1018);
				//result = new ResponseResult<>(BusinessCode.CODE_1018);
				//return result;
			}
		}
		int postFixIndex;

		postFixIndex = fileName.lastIndexOf(".");
		postFix = fileName.substring(postFixIndex);
		if (JPG.equalsIgnoreCase(postFix) || JPEG.equalsIgnoreCase(postFix) 
		        || PNG.equalsIgnoreCase(postFix)) {
			// 上传文件
			//BaseFile baseFile = uploadUtil.UploadFile("ceshi",fileName,inputStream,true);
			BaseFile baseFile = uploadUtil.httpClientUploadFile(baseHost,fileName, inputStream);
			if (baseFile == null) {
				logger.error("上传图片：" + fileName + ",size:" + fileSize + ",图片上传失败!");
				throw new BusinessException(BusinessCode.CODE_1017);
				//result = new ResponseResult<>(BusinessCode.CODE_1017);
				//return result;
			}else{
				baseImageFile=new BaseImageFile();
				baseImageFile.setName(baseFile.getName());
				baseImageFile.setUrl(baseFile.getUrl());
			}

		} else {
			logger.error("上传图片：" + fileName + ",size:" + fileSize + ",图片格式不正确!");
			throw new BusinessException(BusinessCode.CODE_1016);
			//result = new ResponseResult<>(BusinessCode.CODE_1016);
			//return result;
		}
		return baseImageFile;
	}

}
