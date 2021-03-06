package net.imwork.lhqing.qmall.search.service.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.imwork.lhqing.qmall.common.pojo.SearchResult;
import net.imwork.lhqing.qmall.search.dao.SearchDao;
import net.imwork.lhqing.qmall.search.server.SearchService;

/**
 * 商品搜索Service
 * @author lhq_i
 *
 */
@Service
public class SearchServiceImpl implements SearchService{
	
	@Autowired
	private SearchDao searchDao;
	
	
	public SearchResult search(String keywords, int page , int rows) throws SolrServerException{
		//创建一个solrQuery查询对象
		SolrQuery query = new SolrQuery();
		//设置查询条件
		query.setQuery(keywords);
		
		//设置分页条件
		if (page <= 0) page = 1;
		query.setStart((page - 1) / rows);
		query.setRows(rows);
		//设置默认搜索域
		query.set("df", "item_keywords");
		query.setHighlight(true);
		query.setHighlightSimplePre("<em style='color:red'>");
		query.setHighlightSimplePost("</em>");
		query.addHighlightField("item_title");
		//调用Dao执行查询
		SearchResult searchResult = searchDao.search(query);
		//计算总页数
		long recourdCount = searchResult.getRecourdCount();
		int totalPages = (int) Math.ceil(recourdCount * 1.0 / rows);
		//添加到返回结果
		searchResult.setTotalPages(totalPages);
		//返回结果
		return searchResult;
	}
	
}
