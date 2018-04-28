package net.imwork.lhqing.qmall.order.pojo;

import java.io.Serializable;
import java.util.List;

import net.imwork.lhqing.qmall.pojo.Order;
import net.imwork.lhqing.qmall.pojo.OrderItem;
import net.imwork.lhqing.qmall.pojo.OrderShipping;

public class OrderInfo extends Order implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<OrderItem> orderItems;
	
	private OrderShipping orderShipping;

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public OrderShipping getOrderShipping() {
		return orderShipping;
	}

	public void setOrderShipping(OrderShipping orderShipping) {
		this.orderShipping = orderShipping;
	}

}
