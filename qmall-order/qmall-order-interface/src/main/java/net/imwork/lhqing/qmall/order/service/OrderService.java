package net.imwork.lhqing.qmall.order.service;

import net.imwork.lhqing.qmall.common.pojo.QmallResult;
import net.imwork.lhqing.qmall.order.pojo.OrderInfo;

public interface OrderService {
	
	/**
	 * 创建订单
	 * @param orderInfo
	 * @return
	 */
	QmallResult createOrder(OrderInfo orderInfo);
}
