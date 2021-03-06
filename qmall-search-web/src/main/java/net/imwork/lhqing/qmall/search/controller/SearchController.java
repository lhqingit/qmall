package net.imwork.lhqing.qmall.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import net.imwork.lhqing.qmall.common.pojo.SearchResult;
import net.imwork.lhqing.qmall.search.server.SearchService;

/**
 * 商品搜索Controller
 * 
 * @author lhq_i
 *
 */
@Controller
public class SearchController {

	@Autowired
	private SearchService searchService;
	
	@Value("${SEARCH_RESULT_ROWS}")
	private Integer SEARCH_RESULT_ROWS;
	
	@RequestMapping("/search")
	public String searchItemList(String keyword, 
			@RequestParam(defaultValue = "1") Integer page, Model model) throws Exception {
		
		//解决get请求乱码
		keyword = new String(keyword.getBytes("ISO-8859-1"), "UTF-8");
		
		//查询商品列表
		SearchResult searchResult = searchService.search(keyword, page, SEARCH_RESULT_ROWS);
		//把结果传递给页面
		model.addAttribute("query", keyword);
		model.addAttribute("page", page);
		model.addAttribute("totalPages", searchResult.getTotalPages());
		model.addAttribute("recourdCount", searchResult.getRecourdCount());
		model.addAttribute("itemList", searchResult.getItemList());
		
		//测试错误
//		int test = 1/0;
		
		//返回逻辑视图
		return "search";
	}

}
