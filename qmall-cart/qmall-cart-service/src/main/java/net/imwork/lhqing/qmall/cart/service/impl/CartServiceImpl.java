package net.imwork.lhqing.qmall.cart.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import net.imwork.lhqing.qmall.cart.service.CartService;
import net.imwork.lhqing.qmall.common.jedis.JedisClient;
import net.imwork.lhqing.qmall.common.pojo.QmallResult;
import net.imwork.lhqing.qmall.common.utils.JsonUtils;
import net.imwork.lhqing.qmall.mapper.ItemMapper;
import net.imwork.lhqing.qmall.pojo.Item;

/**
 * 购物车处理服务
 * 
 * @author lhq_i
 *
 */
@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private JedisClient jedisClient;
	@Autowired
	private ItemMapper itemMappper;

	@Value("${REDIS_CART_PRE}")
	private String REDIS_CART_PRE;

	@Override
	public QmallResult addCart(long userId, long itemId, int num) {
		// 向redis中添加购物车
		// 数据类型是hash key:用户Id
		// field:商品Id value:商品信息

		String redisCartKey = REDIS_CART_PRE + ":" + userId;

		// 判断商品类型是否存在
		Boolean hexists = jedisClient.hexists(redisCartKey, itemId + "");
		if (hexists) {
			// 如果存在数量相加
			String itemJson = jedisClient.hget(redisCartKey, itemId + "");
			// 把json转换为Item
			Item item = JsonUtils.jsonToPojo(itemJson, Item.class);
			item.setNum(item.getNum() + num);
			// 写回redis
			jedisClient.hset(redisCartKey, itemId + "", JsonUtils.objectToJson(item));

		} else {
			// 如果不存在，根据商品Id取商品信息，调用Dao层
			// 不调用ItemService，是因为服务端尽量减少相互调用
			Item item = itemMappper.selectByPrimaryKey(itemId);
			// 设置购物车数据量
			item.setNum(num);
			// 取一张图片
			String images = item.getImage();
			if (StringUtils.isNotBlank(images)) {
				item.setImage(images.split(",")[0]);
			}
			// 添加到购物车列表
			jedisClient.hset(redisCartKey, itemId + "", JsonUtils.objectToJson(item));
		}
		return QmallResult.ok();
	}

	@Override
	public QmallResult mergeCart(long userId, List<Item> itemList) {
		for (Item item : itemList) {
			this.addCart(userId, item.getId(), item.getNum());
		}
		return QmallResult.ok();
	}

	@Override
	public List<Item> getCartList(long userId) {
		// 根据用户Id查询购物车列表
		List<String> itemStrList = jedisClient.hvals(REDIS_CART_PRE + ":" + userId);

		List<Item> itemList = new ArrayList<>();
		for (String itemStr : itemStrList) {
			// 创建一个Item对象
			Item item = JsonUtils.jsonToPojo(itemStr, Item.class);
			// 添加到列表
			itemList.add(item);
		}
		return itemList;
	}

	@Override
	public QmallResult updateCartNum(long userId, long itemId, int num) {
		
		String redisCartKey = REDIS_CART_PRE + ":" + userId;
		//从redis中取商品信息
		String itemJson = jedisClient.hget(redisCartKey, itemId + "");
		//更新商品数量
		Item item = JsonUtils.jsonToPojo(itemJson, Item.class);
		item.setNum(num);
		//写入redis
		jedisClient.hset(redisCartKey, itemId + "", JsonUtils.objectToJson(item));
		return QmallResult.ok();
	}

	@Override
	public QmallResult deleteCartItem(long userId, long itemId) {
		//删除购物车商品
		jedisClient.hdel(REDIS_CART_PRE + ":" + userId, itemId + "");
		return QmallResult.ok();
	}

	@Override
	public QmallResult clearCartItem(long userId) {
		//清空购物车商品
		jedisClient.del(REDIS_CART_PRE + ":" + userId);
		return QmallResult.ok();
	}

}
