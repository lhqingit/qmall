package net.imwork.lhqing.qmall.cart.service;

import java.util.List;

import net.imwork.lhqing.qmall.common.pojo.QmallResult;
import net.imwork.lhqing.qmall.pojo.Item;

public interface CartService {
	/**
	 * 添加购物车保存到redis中
	 * 
	 * @param userId
	 * @param itemId
	 * @param num
	 * @return
	 */
	QmallResult addCart(long userId, long itemId, int num);
	
	/**
	 * 合并登录状态和未登录状态下的购物车商品列表
	 */
	QmallResult mergeCart(long userId, List<Item> itemList);
	
	/**
	 * 获取购物车列表（redis中的）
	 */
	List<Item> getCartList(long userId);
	
	/**
	 * 更新购物车商品（redis中的）
	 */
	QmallResult updateCartNum(long userId, long itemId, int num);
	
	/**
	 * 删除购物车商品（redis中的）
	 */
	QmallResult deleteCartItem(long userId, long itemId);

	/**
	 * 清空购物车商品（redis中的）
	 */
	QmallResult clearCartItem(long userId);
	
}
