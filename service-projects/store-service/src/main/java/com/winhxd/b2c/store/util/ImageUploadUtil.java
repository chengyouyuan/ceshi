package com.winhxd.b2c.store.util;

import java.io.IOException;
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
import com.winhxd.b2c.common.util.JsonUtil;

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
	private String baseHost = "http://cdn.winhxd.com";
	/**
	 * 300kb
	 */
	private static final long KB_300 = 300 * 1024;

	/**
	 * 上传图片
	 * 
	 * @Title: uploadImage
	 * @Description: TODO
	 * @param imageFile
	 * @return
	 * @throws IOException
	 *             ResponseResult<ProductImageVO>
	 * @author wuyuanbao
	 * @date 2018年8月13日上午10:56:25
	 */
	public ResponseResult<ProductImageVO> uploadImage(MultipartFile imageFile) throws IOException {
		ResponseResult<ProductImageVO> result = new ResponseResult<>();

		String postFix;

		String fileName = imageFile.getOriginalFilename();

		long fileSize = imageFile.getSize();
		// 判断图片大小是否超过300KB
		if (KB_300 < fileSize) {
			logger.error("上传图片："+fileName+",size:"+fileSize+",图片大小超过300KB!");
			result.setCode(BusinessCode.CODE_200016);
			result.setMessage("图片大小超过300KB!");
			return result;
		}
		int postFixIndex;

		ProductImageVO imageVo = null;
		if (StringUtils.isNotEmpty(fileName)) {
			postFixIndex = fileName.lastIndexOf(".");
			postFix = fileName.substring(postFixIndex);
			if (postFix.equalsIgnoreCase(".jpg") || postFix.equalsIgnoreCase(".JPG")
					|| postFix.equalsIgnoreCase(".jpeg") || postFix.equalsIgnoreCase(".JPEG")
					|| postFix.equalsIgnoreCase(".png") || postFix.equalsIgnoreCase(".PNG")) {
				// 上传文件
				imageVo = httpClientUploadFile(imageFile);
				if (imageVo == null) {
					logger.error("上传图片："+fileName+",size:"+fileSize+",图片上传失败!");
					result.setCode(BusinessCode.CODE_200015);
					result.setMessage("图片上传失败");
					return result;
				}

			} else {
				logger.error("上传图片："+fileName+",size:"+fileSize+",图片格式不正确!");
				result.setCode(BusinessCode.CODE_200014);
				result.setMessage("上传图片格式不正确");
				return result;
			}

			result.setData(imageVo);
		} else {
			result.setCode(BusinessCode.CODE_1001);
		}

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
	private ProductImageVO httpClientUploadFile(MultipartFile file) {
		// 第三方服务器请求地址
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String result = "";
		ProductImageVO imageVo = null;
		try {
			String fileName = file.getOriginalFilename();
			// 去重
			String uuidName = generateUUID() + fileName;

			HttpPost httpPost = new HttpPost(baseHost);

			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			// 文件流
			builder.addBinaryBody("file", file.getInputStream(), ContentType.MULTIPART_FORM_DATA, uuidName);
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
