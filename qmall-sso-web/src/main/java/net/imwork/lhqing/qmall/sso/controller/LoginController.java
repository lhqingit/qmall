package net.imwork.lhqing.qmall.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;



import net.imwork.lhqing.qmall.common.pojo.QmallResult;
import net.imwork.lhqing.qmall.common.utils.CookieUtils;
import net.imwork.lhqing.qmall.sso.service.LoginService;

/**
 * 用户登录处理Controller
 * 
 * @author lhq_i
 *
 */
@Controller
public class LoginController {

	@Autowired
	private LoginService loginService;

	@Value("${COOKIES_KEY}")
	private String COOKIES_KEY;

	/**
	 * 跳转到登录界面
	 * 
	 * @return
	 */
	@RequestMapping("/page/login")
	public String showLogin(String redirect, Model model) {
		model.addAttribute("redirect",redirect);
		return "login";
	}

	/**
	 * 用户登录
	 * 
	 * @param username
	 * @param password
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/user/login", method = RequestMethod.POST)
	public QmallResult login(HttpServletRequest request, HttpServletResponse response, 
			String username, String password) {
		String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
		QmallResult qmallResult = loginService.userLogin(username, md5Password);
		// 判断是否登录成功
		if (qmallResult.getStatus() == 200) {
			String token = qmallResult.getData().toString();
			//如果登录成功需要把token写入cookie
			CookieUtils.setCookie(request, response, COOKIES_KEY, token);
		}
		return qmallResult;
	}

}
