package net.imwork.lhqing.qmall.sso.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;



import net.imwork.lhqing.qmall.common.pojo.QmallResult;
import net.imwork.lhqing.qmall.common.utils.JsonUtils;
import net.imwork.lhqing.qmall.sso.service.TokenService;

/**
 * Token处理
 * 
 * @author lhq_i
 *
 */
@Controller
public class TokenController {

	@Autowired
	private TokenService tokenService;

	/**
	 * 根据Token查询用户信息
	 * @param tokenContent
	 * @return
	 */
//	@RequestMapping(value="/user/token/{token}",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
//	@ResponseBody
//	public String getUserByToken(@PathVariable String token,String callback){
//		QmallResult qmallResult = tokenService.getUserByToken(token);
//		String qmallResultJsonStr = JsonUtils.objectToJson(qmallResult);
//		//响应结果之前，判断是否为jsonp请求
//		if(StringUtils.isNotBlank(callback)){
//			//把结果封装成一个js语句响应
//			return callback+"("+qmallResultJsonStr+");";
//		}
//		return qmallResultJsonStr;
//	}
	
	/**
	 * 根据Token查询用户信息(Spring4.1版本以上支持下列写法)
	 * @param token
	 * @param callback
	 * @return
	 */
	@RequestMapping(value="/user/token/{token}")
	@ResponseBody
	public Object getUserByToken(@PathVariable String token,String callback){
		QmallResult qmallResult = tokenService.getUserByToken(token);
		//响应结果之前，判断是否为jsonp请求
		if(StringUtils.isNotBlank(callback)){
			//把结果封装成一个js语句响应
			MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(qmallResult);
			mappingJacksonValue.setJsonpFunction(callback);
			return mappingJacksonValue;
		}
		return qmallResult;
	}

}
