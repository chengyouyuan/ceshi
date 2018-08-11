package com.winhxd.b2c.store.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

/**
 * 上传图片工具类
 * @ClassName: ImageUploadUtil 
 * @Description: TODO
 * @author: wuyuanbao
 * @date: 2018年8月11日 上午9:51:00
 */
public class ImageUploadUtil{
	private static final Logger LOGGER = LoggerFactory.getLogger(ImageUploadUtil.class);

	@Value("${picture.savepath}")
	private String savePath;

	@Value("${picture.baseimgpath}")
	private String baseImgPath;

	@Value("${picture.baseimgurl}")
	private String baseImgUrl;

	@Value("${picture.baseimgport}")
	private String baseImgPort;

	@Value("${picture.basehost}")
	private String baseHost;

	
	public List<Map<String, String>> updateUserHead(String ip, int port, Map<String, byte[]> files) {

		Set<Map.Entry<String, byte[]>> set = files.entrySet();
		String postFix;
		String picFileName;
		String fileName;
		int postFixIndex;
		String temPicId;
		String temPicUrl;
		String uuid = generateUUID();
		String destFolder = getPicPath(uuid);
		List<Map<String, String>> retList = new ArrayList<>();

		Iterator<Map.Entry<String, byte[]>> it = set.iterator();
		Map<String, String> tempMap;

		while (it.hasNext()) {
			tempMap = new HashMap<>();
			Map.Entry<String, byte[]> entry;
			entry = it.next();
			byte[] datas = entry.getValue();
			fileName = entry.getKey();
			postFixIndex = fileName.lastIndexOf(".");
			postFix = fileName.substring(postFixIndex);
			picFileName = generateUUID() + postFix;
			File newFile = new File(destFolder, picFileName);
			try {
				writeFile(newFile.toString(), datas);
			} catch (IOException e) {
				LOGGER.info("错误", e);
			}
			temPicId = generateUUID();
			tempMap.put("pid", fileName);
			temPicUrl = getPicUrl(ip, port, uuid, picFileName);
			tempMap.put("purl", temPicUrl);
			retList.add(tempMap);
			try {
				Thread.sleep(500);
			} catch (Exception e1) {
				LOGGER.info("错误", e1);
			}
		}
		return retList;
	}

	private void writeFile(String file, byte[] data) throws IOException {
		FileUtils.writeByteArrayToFile(new File(file), data);
	}
	/**
	 * 生成UUID
	 *
	 * @return
	 */
	private String generateUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	/**
	 * 项目本地图片统一存放在配置文件中的IMG_PATH下
	 *
	 * @return
	 */
	public String getPicPath(String uuid) {
		// 确保最后以文件分隔符结束
		if (!baseImgPath.endsWith(File.separator)) {
			baseImgPath += File.separator;
		}
		return baseImgPath.concat(File.separator).concat(savePath).concat(File.separator).concat(uuid);
	}

	/**
	 * 根据配置返回文件的url
	 *
	 * @param ip
	 * @param port
	 * @param uuid
	 * @return
	 */
	private String getPicUrl(String ip, int port, String uuid, String picFileName) {
		String URL_SEPARATOR = "/";
		// 确保最后以文件分隔符结束
		if (!baseImgPath.endsWith(URL_SEPARATOR)) {
			baseImgPath += URL_SEPARATOR;
		}
		// 确保以/开始
		if (!baseImgPath.startsWith(URL_SEPARATOR)) {
			baseImgPath = URL_SEPARATOR + baseImgPath;
		}
		String imgPort = getBaseImgPort();
		StringBuilder builder = new StringBuilder();
		builder.append(baseHost).append(baseImgUrl).append(uuid).append(URL_SEPARATOR).append(picFileName);
		return builder.toString();
	}

	/**
	 * 防止nginx下获取不到端口
	 *
	 * @return
	 */
	private String getBaseImgPort() {
		return baseImgPort;
	}
}
