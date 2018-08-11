package com.winhxd.b2c.store.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.UUID;

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
 * @ClassName: ImageUploadUtil 
 * @Description: TODO
 * @author: wuyuanbao
 * @date: 2018年8月11日 上午9:51:00
 */
@Component
public class ImageUploadUtil{
	private static final Logger LOGGER = LoggerFactory.getLogger(ImageUploadUtil.class);
	private static final int SUCCESS_CODE=200;

	@Value("${picture.basehost}")
	private String baseHost="http://cdn.winhxd.com";

	
	public ResponseResult<ProductImageVO> uploadImage(MultipartFile imageFile) {
		ResponseResult<ProductImageVO> result=new ResponseResult<>();
	
		String postFix;
		
		String fileName=imageFile.getOriginalFilename();
		
		int postFixIndex;
		
		ProductImageVO imageVo=null;
		
		postFixIndex = fileName.lastIndexOf(".");
		postFix = fileName.substring(postFixIndex);
		if(postFix.equalsIgnoreCase(".jpg")||postFix.equalsIgnoreCase(".JPG")
				||postFix.equalsIgnoreCase(".jpeg")||postFix.equalsIgnoreCase(".JPEG")
				||postFix.equalsIgnoreCase(".png")||postFix.equalsIgnoreCase(".PNG")){
			//上传文件
			imageVo=httpClientUploadFile(imageFile);
			if(imageVo==null){
				result.setCode(BusinessCode.CODE_200015);
				result.setMessage("图片上传失败");
				return result;
			}
			
		}else{
			result.setCode(BusinessCode.CODE_200014);
			result.setMessage("上传图片格式不正确");
			return result;
		}

		
		result.setData(imageVo);
		return result;
	}
	
	   /**
     * 中转文件
     * 
     * @param file
     *            上传的文件
     * @return 响应结果
     */
    private ProductImageVO  httpClientUploadFile(MultipartFile file) {
        //final String remote_url = "http://upload.winhxd.com:8100/crm/uploadResource2CDNAction.do?method=addRecordNewJson";// 第三方服务器请求地址
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String result = "";
        ProductImageVO imageVo=null;
        try {
            String fileName = file.getOriginalFilename();
            //去重
            String uuidName=generateUUID()+fileName;
            
            HttpPost httpPost = new HttpPost(baseHost);
            
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            // 文件流
            builder.addBinaryBody("file", file.getInputStream(), ContentType.MULTIPART_FORM_DATA, uuidName);
            // 类似浏览器表单提交，对应input的name和value
            builder.addTextBody("filename", uuidName);
            HttpEntity entity = builder.build();
            
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);// 执行提交
            //查看是否请求成
            StatusLine statusLine=response.getStatusLine();
            int stateCode =statusLine.getStatusCode();
            //请求成功
            if(SUCCESS_CODE==stateCode){
            	 HttpEntity responseEntity = response.getEntity();
                 if (responseEntity != null) {
                     // 将响应内容转换为字符串
                     result = EntityUtils.toString(responseEntity, Charset.forName("UTF-8"));
                     if(result!=null){
                     	Map<String,Object> json=JsonUtil.parseJSONObject(result);
                     	String path=(String)json.get("path");
                     	imageVo=new ProductImageVO();
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
