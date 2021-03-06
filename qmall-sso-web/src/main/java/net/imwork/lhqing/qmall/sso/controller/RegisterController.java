package net.imwork.lhqing.qmall.sso.controller;

import java.lang.reflect.Method;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.imwork.lhqing.qmall.common.pojo.QmallResult;
import net.imwork.lhqing.qmall.pojo.User;
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

	/**
	 * Q:跳转到用户注册界面
	 * 
	 * @return
	 */
	@RequestMapping("/page/register")
	public String showRegister() {
		return "register";
	}

	/**
	 * Q:数据一致性校验
	 * 
	 * @param param
	 * @param type
	 *            1:用户名 2:手机号 3:邮箱
	 * @return
	 */
	@RequestMapping("/user/check/{param}/{type}")
	@ResponseBody
	public QmallResult checkData(@PathVariable String param, @PathVariable int type) {
		QmallResult qmallResult = registerService.checkData(param, type);
		return qmallResult;
	}

	/**
	 * Q:用户注册
	 */
	@RequestMapping(value = "/user/register", method = RequestMethod.POST)
	@ResponseBody
	public QmallResult register(User user) {
		QmallResult qmallResult = registerService.register(user);
		return qmallResult;
	}

}
