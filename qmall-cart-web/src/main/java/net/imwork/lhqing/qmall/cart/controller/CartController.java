package net.imwork.lhqing.qmall.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import net.imwork.lhqing.qmall.cart.service.CartService;
import net.imwork.lhqing.qmall.common.pojo.QmallResult;
import net.imwork.lhqing.qmall.common.utils.CookieUtils;
import net.imwork.lhqing.qmall.common.utils.JsonUtils;
import net.imwork.lhqing.qmall.pojo.Item;
import net.imwork.lhqing.qmall.pojo.User;
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
	@Autowired
	private CartService cartService;

	@Value("${COOKIES_KEY}")
	private String COOKIES_KEY;
	@Value("${COOKIE_CART_EXPIRE}")
	private int COOKIE_CART_EXPIRE;

	@RequestMapping("/add/{itemId}.html")
	public String addCart(@PathVariable long itemId, @RequestParam(defaultValue = "1") Integer num,
			HttpServletRequest request, HttpServletResponse response) {
		User user = (User) request.getAttribute("user");
		//判断用户是否登录
		if(user != null){
			//如果是登录状态，把购物车写入redis
			cartService.addCart(user.getId(), itemId, num);
			
		} else{
			//如果未登录状态使用cookie
			
			// 从cookie中取购物车列表
			List<Item> itemList = this.getCartItemListByCookie(request);
			// 判断商品在商品列表中是否存在
			boolean itemIsExist = false;
			for (Item item : itemList) {
				if (item.getId() == itemId) {
					// 如果存在,数量相加
					item.setNum(item.getNum() + num);
					itemIsExist = true;
					// 跳出循环
					break;
				}
			}
			
			// 如果购物车中不存在该商品
			if (!itemIsExist) {
				// 根据商品Id查询商品信息，得到一个Item对象
				Item item = itemService.getItemById(itemId);
				// 设置商品数量
				item.setNum(num);
				// 取一张图片
				String images = item.getImage();
				if (StringUtils.isNoneBlank(images)) {
					item.setImage(images.split(",")[0]);
				}
				// 把商品添加到商品列表
				itemList.add(item);
			}
			// 写入cookie
			String itemListJson = JsonUtils.objectToJson(itemList);
			
			CookieUtils.setCookie(request, response, COOKIES_KEY, itemListJson, COOKIE_CART_EXPIRE, true);	
		}

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

	/**
	 * 展示购物车列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/cart")
	public String showCartList(HttpServletRequest request, HttpServletResponse response) {
		// 从cookie中取购物车列表
		List<Item> cartList = this.getCartItemListByCookie(request);
		
		//判断用户是否为登录状态
		User user = (User) request.getAttribute("user");
		if(user != null){
			//如果为登录状态
			
			//把cookie中的购物车商品和服务端的购物车商品合并
			cartService.mergeCart(user.getId(), cartList);
			//把cookie中的购物车删除
			CookieUtils.deleteCookie(request, response, "cart");
			//从服务端取购物车列表
			cartList = cartService.getCartList(user.getId());
		}
		
		// 把列表传递给页面
		request.setAttribute("cartList", cartList);
		// 返回逻辑视图
		return "cart";
	}

	/**
	 * 更新购物车商品数量
	 * 
	 * @param itemId
	 * @param num
	 * @return
	 */
	@RequestMapping("/update/num/{itemId}/{num}")
	@ResponseBody
	public QmallResult updateCartNum(@PathVariable Long itemId, @PathVariable Integer num, 
			HttpServletRequest request, HttpServletResponse response) {
		//判断用户是否为登录状态
		User user = (User) request.getAttribute("user");
		if(user != null){
			//登录状态下，更新redis中的购物车列表
			cartService.updateCartNum(user.getId(), itemId, num);
		} else{
			//未登录状态下
			
			// 从cookie中取购物车列表
			List<Item> itemList = this.getCartItemListByCookie(request);
			// 遍历商品列表找到对应的商品
			for (Item item : itemList) {
				if (item.getId().longValue() == itemId) {
					// 更新数量
					item.setNum(num);
					// 跳出循环
					break;
				}
			}
			// 写入cookie
			String itemListJson = JsonUtils.objectToJson(itemList);
			CookieUtils.setCookie(request, response, COOKIES_KEY, itemListJson, COOKIE_CART_EXPIRE, true);
		}
		
		//返回成功
		return QmallResult.ok();
	}
	
	/**
	 * 删除购物车商品
	 */
	@RequestMapping("/delete/{itemId}")  
	public String deleteCartItem(@PathVariable Long itemId,
			HttpServletRequest request,HttpServletResponse response){
		//判断用户是否为登录状态
		User user = (User) request.getAttribute("user");
		if(user != null){
			//登录状态下，删除redis中的购物车列表
			cartService.deleteCartItem(user.getId(), itemId);
		} else{
			//登录状态下
			
			// 从cookie中取购物车列表
			List<Item> itemList = this.getCartItemListByCookie(request);
			// 遍历商品列表找到对应的商品
			for (Item item : itemList) {
				if (item.getId().longValue() == itemId) {
					//删除商品
					itemList.remove(item);
					//跳出循环,否则在循环中修改了遍历的List集合会抛出异常。
					break;
				}
			}
			// 写入cookie
			String itemListJson = JsonUtils.objectToJson(itemList);
			CookieUtils.setCookie(request, response, COOKIES_KEY, itemListJson, COOKIE_CART_EXPIRE, true);
		}
		
		
		// |--返回逻辑视图,如果redirect:
			// |--后面不加/，就是相对于当前路径，
			// |--加/就是绝对路径
		return "redirect:/cart/cart.html";
	}

}
