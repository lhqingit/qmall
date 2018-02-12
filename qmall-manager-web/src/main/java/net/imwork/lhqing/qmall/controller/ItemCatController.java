package net.imwork.lhqing.qmall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import net.imwork.lhqing.qmall.common.pojo.EasyUITreeNode;
import net.imwork.lhqing.qmall.service.ItemCatService;

/**
 * 商品类别管理Controller
 * @author lhq_i
 *
 */
@Controller
@RequestMapping("/item/cat")
public class ItemCatController {
	@Autowired
	ItemCatService itemCatService;
	
	@RequestMapping("/list")
	@ResponseBody
	public List<EasyUITreeNode> getItemCatList(@RequestParam(name="id",defaultValue="0")Long parentId){
		//调用服务查询节点列表
		List<EasyUITreeNode> resultList = itemCatService.getItemCat(parentId);
		return resultList;
	}
}
