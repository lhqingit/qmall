package net.imwork.lhqing.qmall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * 页面跳转
 * @author lhq_i
 *
 */
@Controller
public class PageController {

	/**
	 * 跳转到首页
	 * @return 逻辑视图（首页）
	 */
	@RequestMapping("/")
	public String showIndex(){
		return "index";
	}
	
	/**
	 * 解析路径，跳转视图
	 * @param page
	 * @return 逻辑视图
	 */
	@RequestMapping("/{page}")
	public String showPage(@PathVariable String page){
		return page;
	}
	
	
	
}
