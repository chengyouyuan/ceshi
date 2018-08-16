package com.winhxd.b2c.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.store.vo.ProductImageVO;

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

	@Value("${picture.basehost}")
	private String baseHost="";
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
	* @throws IOException ResponseResult<ProductImageVO>
	* @author wuyuanbao
	* @date 2018年8月16日上午10:09:57
	 */
	public ResponseResult<ProductImageVO> uploadImage(MultipartFile imageFile,Long size) throws IOException {
		ResponseResult<ProductImageVO> result = new ResponseResult<>();
		ProductImageVO imageVo = new ProductImageVO();

		String postFix;
		if (imageFile == null) {
			logger.error("上传图片：图片不能为空！");
			result = new ResponseResult<>(BusinessCode.CODE_1019);
			return result;
		}
		String fileName=imageFile.getOriginalFilename();
		if (StringUtils.isEmpty(fileName)) {
			logger.error("上传图片：图片名称不能为空！");
			result = new ResponseResult<>(BusinessCode.CODE_1020);
			result.setData(imageVo);
			return result;
		}
		
		long fileSize = imageFile.getSize();
		if (size != null && size > 0) {
			// 判断图片大小是否超过限制大小，单位KB
			if (size * 1024 < fileSize) {
				logger.error("上传图片：" + fileName + ",size:" + fileSize + ",图片大小超过" + size + "KB!");
				result = new ResponseResult<>(BusinessCode.CODE_1018);
				return result;
			}
		} else {
			// 判断图片大小是否超过300KB
			if (KB_300 < fileSize) {
				logger.error("上传图片：" + fileName + ",size:" + fileSize + ",图片大小超过300KB!");
				result = new ResponseResult<>(BusinessCode.CODE_1018);
				return result;
			}
		}

		int postFixIndex;

		postFixIndex = fileName.lastIndexOf(".");
		postFix = fileName.substring(postFixIndex);
		if (postFix.equalsIgnoreCase(".jpg") || postFix.equalsIgnoreCase(".JPG") || postFix.equalsIgnoreCase(".jpeg")
				|| postFix.equalsIgnoreCase(".JPEG") || postFix.equalsIgnoreCase(".png")
				|| postFix.equalsIgnoreCase(".PNG")) {
			// 上传文件
			imageVo = httpClientUploadFile(fileName, imageFile.getInputStream());
			if (imageVo == null) {
				logger.error("上传图片：" + fileName + ",size:" + fileSize + ",图片上传失败!");
				result = new ResponseResult<>(BusinessCode.CODE_1017);
				return result;
			}
		} else {
			logger.error("上传图片：" + fileName + ",size:" + fileSize + ",图片格式不正确!");
			result = new ResponseResult<>(BusinessCode.CODE_1016);
			result.setData(imageVo);
			return result;
		}

		result.setData(imageVo);

		return result;
	}
	/**
	 * 上传图片
	* @Title: uploadImage 
	* @Description: TODO 
	* @param fileName 图片名称
	* @param inputStream 流
	* @param size 上传图片限制大小 单位是KB（不传默认限制图片大小300KB）
	* @return
	* @throws IOException ResponseResult<ProductImageVO>
	* @author wuyuanbao
	* @date 2018年8月16日上午10:11:17
	 */
	public ResponseResult<ProductImageVO> uploadImage(String fileName,InputStream inputStream,Long size) throws IOException {
		ResponseResult<ProductImageVO> result = new ResponseResult<>();
		ProductImageVO imageVo = new ProductImageVO();

		String postFix;
		if (StringUtils.isEmpty(fileName)) {
			logger.error("上传图片：图片名称不能为空！");
			result = new ResponseResult<>(BusinessCode.CODE_1019);
			result.setData(imageVo);
			return result;
		}
		if (inputStream == null) {
			logger.error("上传图片：图片不能为空！");
			result = new ResponseResult<>(BusinessCode.CODE_1020);
			result.setData(imageVo);
			return result;
		}
		long fileSize = inputStream.available();
		if (size != null && size > 0) {
			// 判断图片大小是否超过限制大小，单位KB
			if (size * 1024 < fileSize) {
				logger.error("上传图片：" + fileName + ",size:" + fileSize + ",图片大小超过" + size + "KB!");
				result = new ResponseResult<>(BusinessCode.CODE_1018);
				return result;
			}
		} else {
			// 判断图片大小是否超过300KB
			if (KB_300 < fileSize) {
				logger.error("上传图片：" + fileName + ",size:" + fileSize + ",图片大小超过300KB!");
				result = new ResponseResult<>(BusinessCode.CODE_1018);
				return result;
			}
		}

		int postFixIndex;

		postFixIndex = fileName.lastIndexOf(".");
		postFix = fileName.substring(postFixIndex);
		if (postFix.equalsIgnoreCase(".jpg") || postFix.equalsIgnoreCase(".JPG") || postFix.equalsIgnoreCase(".jpeg")
				|| postFix.equalsIgnoreCase(".JPEG") || postFix.equalsIgnoreCase(".png")
				|| postFix.equalsIgnoreCase(".PNG")) {
			// 上传文件
			imageVo = httpClientUploadFile(fileName, inputStream);
			if (imageVo == null) {
				logger.error("上传图片：" + fileName + ",size:" + fileSize + ",图片上传失败!");
				result = new ResponseResult<>(BusinessCode.CODE_1017);
				return result;
			}

		} else {
			logger.error("上传图片：" + fileName + ",size:" + fileSize + ",图片格式不正确!");
			result = new ResponseResult<>(BusinessCode.CODE_1016);

			return result;
		}

		result.setData(imageVo);

		return result;
	}

	/**
	 * 中转文件
	* @Title: httpClientUploadFile 
	* @Description: TODO 
	* @param file
	* @return ProductImageVO
	* @author wuyuanbao
	* @date 2018年8月13日上午10:56:56
	 */
	private ProductImageVO httpClientUploadFile(String fileName,InputStream inputStream) {
		// 第三方服务器请求地址
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String result = "";
		ProductImageVO imageVo = null;
		try {
			
			int postFixIndex = fileName.lastIndexOf(".");
			//获取后缀
			String postFix = fileName.substring(postFixIndex);
			// 去重
			String uuidName = generateUUID() + postFix;

			HttpPost httpPost = new HttpPost(baseHost);

			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			// 文件流
			builder.addBinaryBody("file", inputStream, ContentType.MULTIPART_FORM_DATA, uuidName);
			// 类似浏览器表单提交，对应input的name和value
			builder.addTextBody("filename", uuidName);
			HttpEntity entity = builder.build();

			httpPost.setEntity(entity);
			HttpResponse response = httpClient.execute(httpPost);// 执行提交
			// 查看是否请求成
			StatusLine statusLine = response.getStatusLine();
			int stateCode = statusLine.getStatusCode();
			// 请求成功
			if (SUCCESS_CODE == stateCode) {
				HttpEntity responseEntity = response.getEntity();
				if (responseEntity != null) {
					// 将响应内容转换为字符串
					result = EntityUtils.toString(responseEntity, Charset.forName("UTF-8"));
					if (result != null) {
						Map<String, Object> json = JsonUtil.parseJSONObject(result);
						String path = (String) json.get("path");
						imageVo = new ProductImageVO();
						imageVo.setImageName(fileName);
						imageVo.setImageUrl(path);
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return imageVo;
	}

	/**
	 * 生成UUID
	 *
	 * @return
	 */
	private String generateUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}

}
