package net.imwork.lhqing.qmall.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import net.imwork.lhqing.qmall.common.jedis.JedisClient;
import net.imwork.lhqing.qmall.common.pojo.EasyUIDataGridResult;
import net.imwork.lhqing.qmall.common.pojo.QmallResult;
import net.imwork.lhqing.qmall.common.utils.IDUtils;
import net.imwork.lhqing.qmall.common.utils.JsonUtils;
import net.imwork.lhqing.qmall.mapper.ItemDescMapper;
import net.imwork.lhqing.qmall.mapper.ItemMapper;
import net.imwork.lhqing.qmall.pojo.Item;
import net.imwork.lhqing.qmall.pojo.ItemDesc;
import net.imwork.lhqing.qmall.pojo.ItemDescExample;
import net.imwork.lhqing.qmall.pojo.ItemExample;
import net.imwork.lhqing.qmall.service.ItemService;

/**
 * 商品管理Service
 * 
 * @author lhq_i
 *
 */
@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private ItemDescMapper itemDescMapper;
	@Autowired
	private JmsTemplate jmsTemplate;
	@Resource // 先根据id进行匹配，如果没有再按照类型进行匹配
	private Destination topicDestination;
	@Resource // Q:删除商品用到的topic
	private Destination topicDestinationByDelete;

	@Autowired
	private JedisClient jedisClient;

	//Q:商品redis缓存数据key的前缀
	@Value("${REDIS_ITEM_PRE}")
	private String REDIS_ITEM_PRE;
	//Q:商品详情数据redis缓存时间
	@Value("${ITEM_CACHE_EXPIRE}")
	private Integer ITEM_CACHE_EXPIRE;

	/**
	 * 根据商品ID获取商品信息
	 */
	@Override
	public Item getItemById(long itemId) {
		//查询缓存
		String key = REDIS_ITEM_PRE + ":" + itemId + ":BASE";
		try {
			String itemJsonStr = jedisClient.get(key);
			if(StringUtils.isNotBlank(itemJsonStr)){
				return JsonUtils.jsonToPojo(itemJsonStr, Item.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//缓存中没有，查询数据库
		
		// 根据主键查询
		Item item = itemMapper.selectByPrimaryKey(itemId);
		
		//把结果添加到缓存
		try {
			if(item != null){
				jedisClient.set(key, JsonUtils.objectToJson(item));
				//设置过期时间
				jedisClient.expire(key, ITEM_CACHE_EXPIRE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return item;
	}

	/**
	 * Q:得到商品分页数据
	 */
	@Override
	public EasyUIDataGridResult getItemList(int page, int rows) {
		// 设置分页信息
		PageHelper.startPage(page, rows);
		// 执行查询
		ItemExample example = new ItemExample();
		List<Item> itemList = itemMapper.selectByExample(example);
		// 创建一个返回值对象
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setRows(itemList);
		// 取分页相关信息
		PageInfo pageInfo = new PageInfo(itemList);
		// 取总记录数
		long total = pageInfo.getTotal();
		result.setTotal(total);
		return result;
	}

	/**
	 * 添加商品
	 */
	@Override
	public QmallResult addItem(Item item, String desc) {
		// 利用工具类生成商品id
		final long itemId = IDUtils.getItemId();
		// 补全item的属性
		item.setId(itemId);
		// 商品状态，1-正常，2-下架，3-删除
		item.setStatus((byte) 1);
		item.setCreated(new Date());
		// Q:由于将更新时间字段改为`updated` TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
		// Q:因此不需要再手动进行维护，Mysql自动更改updated字段
		// 向商品表插入数据
		itemMapper.insert(item);

		// 创建一个商品描述表对应的pojo对象
		ItemDesc itemDesc = new ItemDesc();
		// 补全属性
		itemDesc.setItemId(itemId);
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(new Date());
		// 向商品描述表插入数据
		itemDescMapper.insert(itemDesc);

		// 发送商品添加消息
		jmsTemplate.send(topicDestination, new MessageCreator() {

			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage textMessage = session.createTextMessage(itemId + "");
				return textMessage;
			}
		});
		// 返回成功信息
		return QmallResult.ok();
	}

	/**
	 * 根据商品Id获取商品描述信息
	 */
	@Override
	public QmallResult queryItemDescById(Long itemId) {
		ItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);
		return QmallResult.ok(itemDesc);
	}

	/**
	 * Q:根据商品Id查询商品信息
	 */
	@Override
	public QmallResult queryItemById(Long itemId) {
		Item item = itemMapper.selectByPrimaryKey(itemId);
		return QmallResult.ok(item);
	}

	/**
	 * Q:更新商品信息
	 */
	@Override
	public QmallResult updateItem(final Item item, String desc) {
		// 更新商品信息
		itemMapper.updateByPrimaryKeySelective(item);

		// 创建一个商品描述表对应的pojo对象
		ItemDesc itemDesc = new ItemDesc();
		// 补全属性
		itemDesc.setItemId(item.getId());
		itemDesc.setItemDesc(desc);
		// 更新相关的商品描述信息
		itemDescMapper.updateByPrimaryKeySelective(itemDesc);

		// Q:发送商品添加(更新)消息
		jmsTemplate.send(topicDestination, new MessageCreator() {

			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage textMessage = session.createTextMessage(item.getId() + "");
				return textMessage;
			}
		});

		// Q:删除redis中的商品缓存数据
		this.deleteItemDetailCacheByRedis(item.getId());
		
		
		// 返回成功信息
		return QmallResult.ok();
	}



	/**
	 * Q:根据商品Id集合删除商品
	 */
	@Override
	public QmallResult deleteItem(final Long[] ids) {
		// Q:删除商品信息
		ItemExample itemExample = new ItemExample();
		itemExample.createCriteria().andIdIn(Arrays.asList(ids));
		itemMapper.deleteByExample(itemExample);
		// Q:删除对应的商品描述信息
		ItemDescExample itemDescExample = new ItemDescExample();
		itemDescExample.createCriteria().andItemIdIn(Arrays.asList(ids));
		itemDescMapper.deleteByExample(itemDescExample);

		// Q:发送商品删除消息
		jmsTemplate.send(topicDestinationByDelete, new MessageCreator() {

			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage textMessage = session.createTextMessage(JsonUtils.objectToJson(ids));
				return textMessage;
			}
		});

		// Q:删除redis中的对应的商品缓存数据
		for (Long itemId : ids) {
			this.deleteItemDetailCacheByRedis(itemId);
		}
		
		// 返回成功信息
		return QmallResult.ok();
	}

	/**
	 * Q:根据商品Id集合下架商品
	 */
	@Override
	public QmallResult instockItem(final Long[] ids) {
		// Q:匹配在对应Id集合中的商品
		ItemExample itemExample = new ItemExample();
		itemExample.createCriteria().andIdIn(Arrays.asList(ids));
		// Q:设置为商品状态为2-下架
		Item item = new Item();
		// 商品状态，1-正常，2-下架(，3-删除)
		item.setStatus((byte) 2);
		itemMapper.updateByExampleSelective(item, itemExample);
		// Q:发送商品删除消息
		jmsTemplate.send(topicDestinationByDelete, new MessageCreator() {

			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage textMessage = session.createTextMessage(JsonUtils.objectToJson(ids));
				return textMessage;
			}
		});
		
		// Q:删除redis中的对应的商品缓存数据
		for (Long itemId : ids) {
			this.deleteItemDetailCacheByRedis(itemId);
		}
		
		return QmallResult.ok();
	}

	/**
	 * Q:根据商品Id集合上架商品
	 */
	@Override
	public QmallResult reshelfItem(Long[] ids) {
		// Q:匹配在对应Id集合中的商品
		ItemExample itemExample = new ItemExample();
		itemExample.createCriteria().andIdIn(Arrays.asList(ids));
		// Q:设置为商品状态为1-正常
		Item item = new Item();
		// 商品状态，1-正常，2-下架(，3-删除)
		item.setStatus((byte) 1);
		itemMapper.updateByExampleSelective(item, itemExample);

		for (final Long itemId : ids) {
			// Q:发送商品添加(更新)消息
			jmsTemplate.send(topicDestination, new MessageCreator() {

				@Override
				public Message createMessage(Session session) throws JMSException {
					TextMessage textMessage = session.createTextMessage(itemId.toString());
					return textMessage;
				}
			});

		}

		return QmallResult.ok();
	}

	@Override
	public ItemDesc getItemDescById(long itemId) {
		//查询缓存
		String key = REDIS_ITEM_PRE + ":" + itemId + ":DESC";
		try {
			String itemDescJsonStr = jedisClient.get(key);
			if(StringUtils.isNotBlank(itemDescJsonStr)){
				return JsonUtils.jsonToPojo(itemDescJsonStr, ItemDesc.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//缓存中没有，查询数据库
		
		ItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);
		
		//把结果添加到缓存
		try {
			if(itemDesc != null){
				jedisClient.set(key, JsonUtils.objectToJson(itemDesc));
				//设置过期时间
				jedisClient.expire(key, ITEM_CACHE_EXPIRE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemDesc;
	}
	
	
	/**
	 * Q:删除redis缓存的商品详情数据
	 * @param itemId 
	 */
	private void deleteItemDetailCacheByRedis(Long itemId) {
		try {
			String baseKey = REDIS_ITEM_PRE + ":" + itemId + ":BASE";
			String descKey = REDIS_ITEM_PRE + ":" + itemId + ":DESC";
			//Q:商品信息
			jedisClient.expire(baseKey, 0);
			//Q:商品描述信息
			jedisClient.expire(descKey, 0);
		} catch (Exception e) {
			//Q:redis失效也不能影响之前正常的逻辑
			e.printStackTrace();
		}
		
	}

}
