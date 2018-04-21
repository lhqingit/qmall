package net.imwork.lhqing.qmall.order.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import net.imwork.lhqing.qmall.cart.service.CartService;
import net.imwork.lhqing.qmall.pojo.Item;
import net.imwork.lhqing.qmall.pojo.User;

/**
 * 订单管理Controller
 * @author lhq_i
 *
 */
@Controller
@RequestMapping("order")
public class OrderController {

	@Autowired
	
	private CartService cartService;
	
	@RequestMapping("order-cart")
	public String showOrderCart(HttpServletRequest request){
		//取用户Id
		User user = (User) request.getAttribute("user");
		//根据用户id取购物车列表
		List<Item> cartList = cartService.getCartList(user.getId());
		request.setAttribute("cartList", cartList);
		//根据用户Id取收货地址列表和取支付方式现，在使用静态数据展示的方式
		//返回页面
		return "order-cart";
	}
}
