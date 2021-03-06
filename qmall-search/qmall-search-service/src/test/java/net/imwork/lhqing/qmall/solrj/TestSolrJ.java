package net.imwork.lhqing.qmall.solrj;

import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class TestSolrJ {

	/**
	 * 更新或添加商品数据到索引库
	 * @throws Exception
	 */
	@Test
	public void addDocument() throws Exception {
		//创建一个SolrServer对象，创建一个连接。参数solr服务的url
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.132:8080/solr/collection1");
		//创建一个文档对象SolrInputDocument
		SolrInputDocument document = new SolrInputDocument();
		//向文档对象中添加域。文档中必须包含一个id域，所有的域的名称必须在schema.xml中定义。
		document.addField("id", "docQ01");
		document.addField("item_title", "测试标题Q");
		document.addField("item_price", 1000);
		//把文档写入文档库
		solrServer.add(document);
		//提交
		solrServer.commit();
	}
	
	/**
	 * 删除商品数据到索引库
	 */
	@Test
	public void deleteDocument() throws Exception {
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.132:8080/solr/collection1");
		//删除文档
		solrServer.deleteById("docQ01");
		//提交
		solrServer.commit();
	}
	
	/**
	 * solrJ搜索-简单查询
	 * @throws Exception
	 */
	@Test
	public void queryIndex() throws Exception {
		//创建一个SolrServer对象
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.132:8080/solr/collection1");
		//创建一个SolrQuery对象
		SolrQuery query = new SolrQuery();
		//设置查询条件
		query.set("q", "*:*");//Q:查询所有
		//Q:默认取第一页的10条数据
		//执行查询,QueryResponse对象
		QueryResponse queryResponse = solrServer.query(query);
		//取文档对象
		SolrDocumentList solrDocumentList = queryResponse.getResults();
		System.out.println("总记录数："+solrDocumentList.getNumFound());
		System.out.println("---------------");
		//遍历文档列表，中文档中取域的内容
		for (SolrDocument solrDocument : solrDocumentList) {
			System.out.println("id:\t" + solrDocument.get("id"));
			System.out.println("item_title:\t" + solrDocument.get("item_title"));
			System.out.println("item_sell_point:\t" + solrDocument.get("item_sell_point"));
			System.out.println("item_price:\t" + solrDocument.get("item_price"));
			System.out.println("item_category_name:\t" + solrDocument.get("item_category_name"));
			System.out.println("item_image:\t" + solrDocument.get("item_image"));
			System.out.println("_version_:\t" + solrDocument.get("_version_"));
			System.out.println();
		}
	}
	
	/**
	 * 复杂查询，高亮显示
	 * @throws Exception
	 */
	@Test
	public void queryIndexComplex() throws Exception{
		//创建一个SolrServer对象
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.132:8080/solr/collection1");
		//创建一个SolrQuery查询对象
		SolrQuery query = new SolrQuery();
		//查询条件
		query.set("q", "手机");
		query.set("df", "item_title");
		
		query.setStart(0);
		query.setRows(20);
		
		query.setHighlight(true);
		query.setHighlightSimplePre("<em>");
		query.setHighlightSimplePost("</em>");
		query.addHighlightField("item_title");
		
		//执行查询,QueryResponse对象
		QueryResponse queryResponse = solrServer.query(query);
		
		//取高亮显示
		Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
		
		//取文档对象
		SolrDocumentList solrDocumentList = queryResponse.getResults();
		System.out.println("总记录数："+solrDocumentList.getNumFound());
		System.out.println("---------------");
		//遍历文档列表，中文档中取域的内容
		for (SolrDocument solrDocument : solrDocumentList) {
			List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
			
			String itemTitle;
			if(list != null && list.size() > 0){
				itemTitle = list.get(0);
			} else {
				itemTitle = solrDocument.get("item_title").toString();
			}
			
			System.out.println("id:\t" + solrDocument.get("id"));
			System.out.println("item_title:\t" + itemTitle);
			System.out.println("item_sell_point:\t" + solrDocument.get("item_sell_point"));
			System.out.println("item_price:\t" + solrDocument.get("item_price"));
			System.out.println("item_category_name:\t" + solrDocument.get("item_category_name"));
			System.out.println("item_image:\t" + solrDocument.get("item_image"));
			System.out.println("_version_:\t" + solrDocument.get("_version_"));
			System.out.println();
		}
	}
	
	
}
