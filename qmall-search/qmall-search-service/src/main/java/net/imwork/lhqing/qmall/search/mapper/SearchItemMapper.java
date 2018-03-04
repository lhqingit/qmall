package net.imwork.lhqing.qmall.search.mapper;

import java.util.List;

import net.imwork.lhqing.qmall.common.pojo.SearchItem;

public interface SearchItemMapper {
	/**
	 * 查询所有商品列表
	 */
	List<SearchItem> getItemList();
	
	/**
	 * 根据Id获取搜索商品
	 */
	SearchItem getItemById(long itemId);
	
}
