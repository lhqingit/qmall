package net.imwork.lhqing.qmall.search.message;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;

import net.imwork.lhqing.qmall.common.utils.JsonUtils;

/**
 * Q:监听商品删除信息
 * @author lhq_i
 *
 */
public class ItemDeleteMessageListener implements MessageListener {

	@Autowired
	private SolrServer solrServer;
	
	@Override
	public void onMessage(Message message) {
		try {
			TextMessage textMessage = (TextMessage) message;
			String itemIdStr = textMessage.getText();
			solrServer.deleteById((JsonUtils.jsonToList(itemIdStr, String.class)));
			solrServer.commit();
			
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
