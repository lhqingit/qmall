package net.imwork.lhqing.qmall.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 注册功能Controller
 * @author lhq_i
 *
 */
@Controller
public class RegisterController {
	@RequestMapping("/page/register")
	public String showRegister() {
		return "register";
	}
}
