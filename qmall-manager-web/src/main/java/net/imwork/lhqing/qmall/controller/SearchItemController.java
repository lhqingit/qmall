package net.imwork.lhqing.qmall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.imwork.lhqing.qmall.common.pojo.QmallResult;
import net.imwork.lhqing.qmall.search.server.SearchItemService;

/**
 * 导入商品数据到索引库
 * @author lhq_i
 *
 */
@Controller
public class SearchItemController {
	
	@Autowired
	private SearchItemService searchItemService;
	
	@RequestMapping("/index/item/import")
	@ResponseBody
	public QmallResult importItemList(){
		return searchItemService.importAllItems();
	}
	
	
}
