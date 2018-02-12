package net.imwork.lhqing.qmall.service;

import net.imwork.lhqing.qmall.common.pojo.EasyUIDataGridResult;
import net.imwork.lhqing.qmall.common.pojo.QmallResult;
import net.imwork.lhqing.qmall.pojo.Item;

/**
 * 商品管理
 * @author lhq_i
 *
 */
public interface ItemService {
	/**
	 * 根据商品ID查询商品信息
	 * @param itemId
	 * @return Item (MyBatis逆向工程生成的POJO类)
	 */
	Item getItemById(long itemId);
	
	/**
	 * Q:得到商品分页数据
	 * @param page
	 * @param rows
	 * @return
	 */
	EasyUIDataGridResult getItemList(int page,int rows);
	
	/**
	 * 添加商品
	 * @param item 商品信息
	 * @param desc 商品描述
	 * @return
	 */
	QmallResult addItem(Item item,String desc);
	
	/**
	 * Q:根据商品Id获取商品描述信息
	 * @param itemId 商品Id
	 * @return
	 */
	QmallResult queryItemDescById(Long itemId);

	/**
	 * Q:根据商品Id获取商品信息
	 * @param itemId 商品Id
	 * @return
	 */
	QmallResult queryItemById(Long itemId);

	/**
	 * Q:更新商品信息
	 * @param item 商品Id
	 * @param desc 商品描述
	 * @return
	 */
	QmallResult updateItem(Item item, String desc);

	/**
	 * Q:删除商品
	 * @param ids 要删除的商品ID集合
	 * @return
	 */
	QmallResult deleteItem(Long[] ids);

	/**
	 * Q:下架商品
	 * @param ids 要下架的商品ID集合
	 * @return
	 */
	QmallResult instockItem(Long[] ids);

	/**
	 * Q:上架商品
	 * @param ids 要上架的商品ID集合
	 * @return
	 */
	QmallResult reshelfItem(Long[] ids);

}
