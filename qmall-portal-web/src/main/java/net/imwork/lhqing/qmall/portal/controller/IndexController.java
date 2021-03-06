package net.imwork.lhqing.qmall.portal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import net.imwork.lhqing.qmall.content.service.ContentService;
import net.imwork.lhqing.qmall.pojo.Content;

/**
 * Q商城首页展示
 * @author lhq_i
 *
 */
@Controller
public class IndexController {
	
	@Autowired
	private ContentService contentService;
	
	@Value("${CONTENT_CAROUSEL_ID}")
	private Long CONTENT_CAROUSEL_ID;
	
	/**
	 * Q:当直接访问localhost:8082，而不是localhost:8082/index.html时
	 * 由于web.xml文件中配置了默认访问的欢迎页index.html，
	 * 而在web.xml同级目录，也就是WEB-INF目录中没有index.html文件，
	 * 此时下一步localhost:8082/index.html去找servlet满足*.html被springMVC拦截
	 * 与此handle(也就是下面的方法)成功匹配
	 * @return
	 */
	@RequestMapping("/index")
	public String showIndex(Model model) {
		//查询内容分类列表
		List<Content> adList = contentService.getContentListByCid(CONTENT_CAROUSEL_ID);
		//把结果数据传递给界面
		model.addAttribute("ad1List",adList);
		
		//Q:跳转到商城首页逻辑视图
		return "index";
	}
}
