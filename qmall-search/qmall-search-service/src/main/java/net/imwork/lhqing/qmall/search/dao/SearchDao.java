package net.imwork.lhqing.qmall.search.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import net.imwork.lhqing.qmall.common.pojo.SearchItem;
import net.imwork.lhqing.qmall.common.pojo.SearchResult;

/**
 * 商品搜索Dao
 * @author lhq_i
 *
 */
@Repository
public class SearchDao {
	
	
	@Autowired
	private SolrServer solrServer;
	
	/**
	 * 根据查询条件查询索引库
	 * @param query
	 * @return
	 * @throws SolrServerException 
	 */
	public SearchResult search(SolrQuery query) throws SolrServerException{
		SearchResult searchResult = new SearchResult();
		//根据query查询数据库
		QueryResponse queryResponse = solrServer.query(query);
		//取查询结果
		SolrDocumentList solrDocumentList = queryResponse.getResults();
		Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
		//取查询结果总记录数
		long numFound = solrDocumentList.getNumFound();
		searchResult.setRecourdCount(numFound);
		//取商品列表，需要取高亮显示
		List<SearchItem> itemList = new ArrayList<>();
		for (SolrDocument solrDocument : solrDocumentList) {
			//取高亮显示
			String itemTitle;
			List<String> itemTitleList = highlighting.get(solrDocument.get("id")).get("item_title");
			if (itemTitleList != null && itemTitleList.size() > 0) {
				itemTitle = itemTitleList.get(0);
			} else {
				itemTitle = solrDocument.get("item_title").toString();
			}
			
			SearchItem searchItem = new SearchItem();
			searchItem.setId(solrDocument.get("id").toString());
			searchItem.setTitle(itemTitle);
			searchItem.setSellPoint(solrDocument.get("item_sell_point").toString());
			searchItem.setPrice((Long) solrDocument.get("item_price"));
			searchItem.setCategoryName(solrDocument.get("item_category_name").toString());
			searchItem.setImage(solrDocument.get("item_image").toString());
			//添加到商品列表
			itemList.add(searchItem);
		}
		searchResult.setItemList(itemList);
		//返回结果
		return searchResult;
		
	}
}
