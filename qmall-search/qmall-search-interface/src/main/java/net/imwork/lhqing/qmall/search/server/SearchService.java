package net.imwork.lhqing.qmall.search.server;

import net.imwork.lhqing.qmall.common.pojo.SearchResult;

public interface SearchService {
	/**
	 * 搜索商品数据
	 * @param keywords 搜索关键字
	 * @param page 第几页
	 * @param rows 一页包含的条数
	 * @return
	 * @throws Exception
	 */
	SearchResult search(String keywords, int page , int rows) throws Exception;
}
