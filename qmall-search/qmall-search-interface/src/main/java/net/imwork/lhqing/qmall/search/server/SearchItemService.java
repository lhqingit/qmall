package net.imwork.lhqing.qmall.search.server;

import net.imwork.lhqing.qmall.common.pojo.QmallResult;

public interface SearchItemService {

	/**
	 * Q:从数据库中导入所有商品数据到solr索引库
	 * @return 自定义响应结构bean
	 */
	QmallResult importAllItems();
}
