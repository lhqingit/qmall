package net.imwork.lhqing.qmall.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import net.imwork.lhqing.qmall.item.pojo.ItemData;
import net.imwork.lhqing.qmall.pojo.Item;
import net.imwork.lhqing.qmall.pojo.ItemDesc;
import net.imwork.lhqing.qmall.service.ItemService;

/**
 * 商品详情页面展示Controller
 * @author lhq_i
 *
 */
@Controller
@RequestMapping("item")
public class ItemDetailController {
	
	@Autowired
	private ItemService itemService;
	
	@RequestMapping("{itemId}")
	public String showItem(@PathVariable() Long itemId, Model model) {
		//调用服务取商品基本信息
		Item item = itemService.getItemById(itemId);
		ItemData itemData = new ItemData(item);
		//取商品描述信息
		ItemDesc itemDesc = itemService.getItemDescById(itemId);
		//把信息传递给页面
		model.addAttribute("item", itemData);
		//返回逻辑视图
		model.addAttribute("itemDesc", itemDesc);
		return "item";
	}
}
