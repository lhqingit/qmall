package net.imwork.lhqing.qmall.search.message;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import net.imwork.lhqing.qmall.common.pojo.SearchItem;
import net.imwork.lhqing.qmall.search.mapper.SearchItemMapper;

/**
 * 监听商品添加（Q:和编辑）消息，将对应的商品信息同步到索引库
 * @author lhq_i
 *
 */
public class ItemAddMessageListener implements MessageListener {

	@Autowired
	SearchItemMapper searchItemMapper;
	@Autowired
	private SolrServer solrServer;
	
	@Override
	public void onMessage(Message message) {
		try {
			//从消息中取商品Id
			TextMessage textMessage = (TextMessage) message;
			String itemIdStr = textMessage.getText();
			//等待发送方，提交事务，将添加或修改的商品数据更新到数据库中
			Thread.sleep(500);//等待一秒
			//根据商品Id查询商品信息
			SearchItem searchItem = searchItemMapper.getItemById(new Long(itemIdStr));
			//创建一个文档对象
			SolrInputDocument document = new SolrInputDocument();
			//向文档对象中添加域
			document.addField("id", searchItem.getId());
			document.addField("item_title", searchItem.getTitle());
			document.addField("item_sell_point", searchItem.getSellPoint());
			document.addField("item_price", searchItem.getPrice());
			document.addField("item_category_name", searchItem.getCategoryName());
			document.addField("item_image", searchItem.getImage());
			//把文档对象写入索引库
			solrServer.add(document);
			//提交
			solrServer.commit();
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

}
