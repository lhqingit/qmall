package net.imwork.lhqing.qmall.order.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.imwork.lhqing.qmall.cart.service.CartService;
import net.imwork.lhqing.qmall.common.pojo.QmallResult;
import net.imwork.lhqing.qmall.order.pojo.OrderInfo;
import net.imwork.lhqing.qmall.order.service.OrderService;
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
	@Autowired
	private OrderService orderService;
	
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
	
	
	@RequestMapping(value="create", method=RequestMethod.POST)
	public String createOrder(OrderInfo orderInfo,HttpServletRequest request){
		//取用户信息
		User user = (User) request.getAttribute("user");
		//把用户信息添加到orderInfo中
		orderInfo.setUserId(user.getId());
		orderInfo.setBuyerNick(user.getUsername());
		//调用服务生成订单
		QmallResult qmallResult = orderService.createOrder(orderInfo);
		//如果订单生成成功，需要删除购物车
		if(qmallResult.getStatus() == 200){
			//清空购物车
			cartService.clearCartItem(user.getId());			
		}
		//把订单号传递给页面
		request.setAttribute("orderId", qmallResult.getData());
		request.setAttribute("payment", orderInfo.getPayment());
		//返回逻辑视图
		return "success";
	}
	
}
