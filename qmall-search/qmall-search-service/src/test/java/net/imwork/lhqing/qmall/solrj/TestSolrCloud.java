package net.imwork.lhqing.qmall.solrj;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

/**
 * Q:测试使用SolrJ操作SolrCloud
 * @author lhq_i
 *
 */
public class TestSolrCloud {

	/**
	 * 添加
	 * @throws SolrServerException
	 * @throws IOException
	 */
	@Test
	public void testAddDocument() throws SolrServerException, IOException{
		//创建一个集群的连接，应该使用CloudSolrServer创建
		//zkHost：zookeeper的地址列表
		CloudSolrServer cloudSolrServer = new CloudSolrServer("192.168.25.132:2181,192.168.25.132:2182,192.168.25.132:2183");
		//设置一个defaultCollection属性
		cloudSolrServer.setDefaultCollection("collection2");
		//创建一个文档对象
		SolrInputDocument document = new SolrInputDocument();
		//向文档中添加域
		document.addField("id", "solrCloudTest01");
		document.addField("item_title", "solr集群测试商品01");
		document.addField("item_price", 2499);
		//把文件写入索引库
		cloudSolrServer.add(document);
		//提交
		cloudSolrServer.commit();
	}
	
	/**
	 * 查询
	 * @throws SolrServerException
	 */
	@Test
	public void testQueryDocument() throws SolrServerException{
		//创建一个CloudSolrServer对象
		CloudSolrServer cloudSolrServer = new CloudSolrServer("192.168.25.132:2181,192.168.25.132:2182,192.168.25.132:2183");
		//设置默认的Collection
		cloudSolrServer.setDefaultCollection("collection2");
		
		//创建一个查询对象
		SolrQuery query = new SolrQuery();
		//设置查询条件
		query.setQuery("*:*");
		//执行查询
		QueryResponse queryResponse = cloudSolrServer.query(query);
		//取查询结果
		SolrDocumentList solrDocumentList = queryResponse.getResults();
		System.out.println("查询结果总记录数：" + solrDocumentList.getNumFound());
		for (SolrDocument solrDocument : solrDocumentList) {
			System.out.println("id：" + solrDocument.get("id"));
			System.out.println("item_title：" + solrDocument.get("item_title"));
			System.out.println("item_price：" + solrDocument.get("item_price"));
			System.out.println("_version_：" + solrDocument.get("_version_"));
			System.out.println("title：" + solrDocument.get("title"));
		}
		
	}
	
}
