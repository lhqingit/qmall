package net.imwork.lhqing.qmall.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import net.imwork.lhqing.qmall.common.utils.CookieUtils;
import net.imwork.lhqing.qmall.common.utils.JsonUtils;
import net.imwork.lhqing.qmall.pojo.Item;
import net.imwork.lhqing.qmall.service.ItemService;

/**
 * 购物车处理
 * 
 * @author lhq_i
 *
 */
@Controller
@RequestMapping("cart")
public class CartController {

	@Autowired
	private ItemService itemService;

	@Value("${COOKIES_KEY}")
	private String COOKIES_KEY;
	
	@Value("${COOKIE_CART_EXPIRE}")
	private int COOKIE_CART_EXPIRE;

	@RequestMapping("/add/{itemId}.html")
	public String addCart(@PathVariable long itemId, @RequestParam(defaultValue = "1") Integer num,
			HttpServletRequest request, HttpServletResponse response) {

		// 从cookie中取购物车列表
		List<Item> itemList = this.getCartItemListByCookie(request);
		// 判断商品在商品列表中是否存在
		boolean itemIsExist = false;
		for (Item item : itemList) {
			if (item.getId() == itemId) {
				// 如果存在,数量相加
				item.setNum(item.getNum() + num);
				itemIsExist = true;
				//跳出循环
				break;
			}
		}

		//如果购物车中不存在该商品
		if (!itemIsExist) {
			//根据商品Id查询商品信息，得到一个Item对象
			Item item = itemService.getItemById(itemId);
			//设置商品数量
			item.setNum(num);
			//取一张图片
			String images = item.getImage();
			if(StringUtils.isNoneBlank(images)){
				item.setImage(images.split(",")[0]);
			}
			//把商品添加到商品列表
			itemList.add(item);
		}
		//写入cookie
		String itemListJson = JsonUtils.objectToJson(itemList);

		CookieUtils.setCookie(request, response, COOKIES_KEY, itemListJson, COOKIE_CART_EXPIRE, true);

		return "cartSuccess";
	}

	/**
	 * 从cookie中取购物车列表
	 * 
	 * @param request
	 * @return
	 */
	private List<Item> getCartItemListByCookie(HttpServletRequest request) {
		String itemListJson = CookieUtils.getCookieValue(request, COOKIES_KEY, true);
		// 判断json是否为空
		if (StringUtils.isBlank(itemListJson)) {
			// 为空时返回空集合
			return new ArrayList<>();
		}
		// 把json转换成商品列表
		List<Item> itemList = JsonUtils.jsonToList(itemListJson, Item.class);
		return itemList;
	}
	
	@RequestMapping("/cart")
	public String showCartList(HttpServletRequest request){
		//从cookie中取购物车列表
		List<Item> cartList = this.getCartItemListByCookie(request);	
		//把列表传递给页面
		request.setAttribute("cartList", cartList);
		//返回逻辑视图
		return "cart";
	}
}
