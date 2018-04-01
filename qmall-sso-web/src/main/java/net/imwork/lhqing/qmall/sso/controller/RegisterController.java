package net.imwork.lhqing.qmall.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.imwork.lhqing.qmall.common.pojo.QmallResult;
import net.imwork.lhqing.qmall.sso.service.RegisterService;

/**
 * 注册功能Controller
 * 
 * @author lhq_i
 *
 */
@Controller
public class RegisterController {
	@Autowired
	private RegisterService registerService;
	
	@RequestMapping("/page/register")
	public String showRegister() {
		return "register";
	}

	@RequestMapping("/user/check/{param}/{type}")
	@ResponseBody
	public QmallResult checkData(@PathVariable String param, @PathVariable int type) {
		QmallResult qmallResult = registerService.checkData(param, type);
		return qmallResult;
	}
}
