package net.imwork.lhqing.qmall.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import net.imwork.lhqing.qmall.common.utils.FastDFSClient;
import net.imwork.lhqing.qmall.common.utils.JsonUtils;

/**
 * 图片上传处理
 * @author lhq_i
 *
 */
@Controller
public class PictureController {

	@Value("${IMAGE_SERVER_URL}")
	String IMAGE_SERVER_URL;

	@RequestMapping(value="/pic/upload",produces=MediaType.TEXT_PLAIN_VALUE+";charset=UTF-8")
	@ResponseBody
	// Q:uploadFile的名字必须与/js/common.js文件中，配置的kingEditorParams中filePostName的值保持一致
	// Q:解决KindEditor兼容问题，需要将响应的Content-Type由application/json变为text/plain
	// Q:将返回值类型换成String，否则会报406错误 Not Acceptable，Content-Type自动就变为text/plain
	public String fileUpload(MultipartFile uploadFile) {

		try {
			// 1.取文件的扩展名
			String originalFilename = uploadFile.getOriginalFilename();
			String extName = originalFilename.substring(originalFilename.indexOf(".") + 1);
			// 2.创建一个FastDFS的客户端
			FastDFSClient fastDFSClient = new FastDFSClient("classpath:conf/client.conf");
			// 3.执行上传操作
			String path = fastDFSClient.uploadFile(uploadFile.getBytes(), extName);
			// 4.将返回的path和ip地址拼装成完整的url，用于访问上传的文件
			String url = IMAGE_SERVER_URL + path;
			// 5.返回map
			Map<String, Object> result = new HashMap<>();
			result.put("error", 0);
			result.put("url", url);
//			return result;
			return JsonUtils.objectToJson(result);

		} catch (Exception e) {
			e.printStackTrace();
			// 5.返回错误信息
			Map<String, Object> result = new HashMap<>();
			result.put("error", 1);
			result.put("message", "图片上传失败：" + e);
//			return result;
			return JsonUtils.objectToJson(result);
		}

	}

}
