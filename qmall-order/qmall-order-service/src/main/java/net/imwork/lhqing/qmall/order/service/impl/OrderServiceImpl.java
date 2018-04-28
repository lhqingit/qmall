package net.imwork.lhqing.qmall.order.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import net.imwork.lhqing.qmall.common.jedis.JedisClient;
import net.imwork.lhqing.qmall.common.pojo.QmallResult;
import net.imwork.lhqing.qmall.mapper.OrderItemMapper;
import net.imwork.lhqing.qmall.mapper.OrderMapper;
import net.imwork.lhqing.qmall.mapper.OrderShippingMapper;
import net.imwork.lhqing.qmall.order.pojo.OrderInfo;
import net.imwork.lhqing.qmall.order.service.OrderService;
import net.imwork.lhqing.qmall.pojo.OrderItem;
import net.imwork.lhqing.qmall.pojo.OrderShipping;

/**
 * 订单处理服务
 * @author lhq_i
 *
 */
@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private OrderItemMapper orderItemMapper;
	@Autowired
	private OrderShippingMapper orderShippingMapper;
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${ORDER_ID_GEN_KEY}")
	private String ORDER_ID_GEN_KEY;
	@Value("${ORDER_ID_START}")
	private String ORDER_ID_START;
	@Value("${ORDER_DETAIL_ID_GEN_KEY}")
	private String ORDER_DETAIL_ID_GEN_KEY;
	
	@Override
	public QmallResult createOrder(OrderInfo orderInfo) {
		//生成订单号，使用redis的incr生成
		if(!jedisClient.exists(ORDER_ID_GEN_KEY)){
			jedisClient.set(ORDER_ID_GEN_KEY, ORDER_ID_START);
		}
		String orderId = jedisClient.incr(ORDER_ID_GEN_KEY).toString();
		//补全orderInfo的属性
		orderInfo.setOrderId(orderId);
		orderInfo.setCreateTime(new Date());
		orderInfo.setUpdateTime(new Date());
		//插入订单表
		orderMapper.insertSelective(orderInfo);
		//向订单明细表插入数据
		List<OrderItem> orderItems = orderInfo.getOrderItems();
		for (OrderItem orderItem : orderItems) {
			//生成明细Id
			String orderDetailId = jedisClient.incr(ORDER_DETAIL_ID_GEN_KEY).toString();
			//补全pojo属性
			orderItem.setId(orderDetailId);
			orderItem.setOrderId(orderId);
			//向明细表插入数据
			orderItemMapper.insert(orderItem);
		}
		//向订单物流表插入数据
		OrderShipping orderShipping = orderInfo.getOrderShipping();
		orderShipping.setOrderId(orderId);
		orderShipping.setCreated(new Date());
		orderShipping.setUpdated(new Date());
		orderShippingMapper.insert(orderShipping);
		//返回QmallResult,包含订单号
		return QmallResult.ok(orderId);
	}

}
