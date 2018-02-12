package net.imwork.lhqing.qmall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import net.imwork.lhqing.qmall.common.pojo.EasyUIDataGridResult;
import net.imwork.lhqing.qmall.common.pojo.QmallResult;
import net.imwork.lhqing.qmall.pojo.Item;
import net.imwork.lhqing.qmall.service.ItemService;

/**
 * 商品管理Controller
 * @author lhq_i
 *
 */
@Controller
@RequestMapping("/item")
public class ItemController {
	//自动注入服务
	@Autowired
	ItemService itemService;


	@RequestMapping("/{itemId}")
	@ResponseBody//下面返回值写Item,会自动把返回值转化为POJO,
	//前提是要引入三个jackson-*.jar包，否则会报406错误·406 - 客户端浏览器不接受所请求页面的 MIME 类型。 
	public Item getItemById(@PathVariable long itemId) {
		Item item = itemService.getItemById(itemId);
		return item;
	}
	
	@RequestMapping("/list")
	@ResponseBody
	public EasyUIDataGridResult getItemList(Integer page,Integer rows){
		//调用服务，查询商品列表
		EasyUIDataGridResult result = itemService.getItemList(page, rows);
		return result;
	}
	
	/**
	 * 商品添加功能
	 * @param item 商品信息
	 * @param desc 商品描述信息
	 * @return
	 */
	@RequestMapping(value="/save", method=RequestMethod.POST)
	@ResponseBody
	public QmallResult addItem(Item item,String desc){
		QmallResult result = itemService.addItem(item, desc);
		return result;
	}
	
	/**
	 * Q:根据商品Id获取商品描述信息
	 * @param itemId
	 * @return
	 */
	@RequestMapping(value="/query/desc/{itemId}")
	@ResponseBody
	public QmallResult queryItemDescById(@PathVariable("itemId")Long itemId){
		QmallResult result = itemService.queryItemDescById(itemId);
		return result;
	}
	
	/**
	 * Q:根据商品Id获取商品信息
	 * @param itemId 商品Id
	 * @return
	 */
	@RequestMapping(value="/query/item/{itemId}")
	@ResponseBody
	public QmallResult queryItemById(@PathVariable("itemId")Long itemId){
		QmallResult result = itemService.queryItemById(itemId);
		return result;
	}
	
	/**
	 * Q:商品编辑功能
	 * @param item 商品信息
	 * @param desc 商品描述信息
	 * @return
	 */
	@RequestMapping(value="/update", method=RequestMethod.POST)
	@ResponseBody
	public QmallResult editItem(Item item,String desc){
		QmallResult result = itemService.updateItem(item, desc);
		return result;
	}
	
	/**
	 * Q:删除商品
	 * @param ids 要删除的商品Id集合
	 * @return
	 */
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	@ResponseBody
	public QmallResult deleteItem(Long[] ids){
		QmallResult result = itemService.deleteItem(ids);
		return result;
	}
	
	
	/**
	 * Q:下架商品
	 * @param ids
	 * @return
	 */
	@RequestMapping(value="/instock", method=RequestMethod.POST)
	@ResponseBody
	public QmallResult instockItem(Long[] ids){
		QmallResult result = itemService.instockItem(ids);
		return result;
	}
	
	
	/**
	 * Q:上架商品
	 * @param ids
	 * @return
	 */
	@RequestMapping(value="/reshelf", method=RequestMethod.POST)
	@ResponseBody
	public QmallResult reshelfItem(Long[] ids){
		QmallResult result = itemService.reshelfItem(ids);
		return result;
	}
	
}
