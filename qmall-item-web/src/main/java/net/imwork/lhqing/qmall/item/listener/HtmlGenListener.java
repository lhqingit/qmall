package net.imwork.lhqing.qmall.item.listener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import net.imwork.lhqing.qmall.item.pojo.ItemData;
import net.imwork.lhqing.qmall.pojo.Item;
import net.imwork.lhqing.qmall.pojo.ItemDesc;
import net.imwork.lhqing.qmall.service.ItemService;

/**
 * 监听商品添加消息，生成对应的静态界面
 * 
 * @author lhq_i
 *
 */
public class HtmlGenListener implements MessageListener {

	@Autowired
	private ItemService itemService;
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;

	@Value("${HTML_GEN_PATH}")
	private String HTML_GEN_PATH;

	@Override
	public void onMessage(Message message) {
		// 创建一个模板，参考jsp
		try {
			// 从消息中取商品id
			TextMessage textMessage = (TextMessage) message;
			String itemIdStr = textMessage.getText();
			long itemId = Long.parseLong(itemIdStr);
			//等待事务提交，等待一秒
			Thread.sleep(1000);
			// 根据商品id查询商品信息，商品基本信息和商品描述
			Item item = itemService.getItemById(itemId);
			ItemData itemData = new ItemData(item);
			//取商品描述
			ItemDesc itemDesc = itemService.getItemDescById(itemId);
			// 创建一个数据集，把商品数据封装
			Map<String, Object> data = new HashMap<>();
			data.put("item", itemData);
			data.put("itemDesc", itemDesc);
			// 加载模板对象
			Configuration configuration = freeMarkerConfigurer.getConfiguration();
			Template template = configuration.getTemplate("item.ftl");
			// 创建一个输出流，指定输出的目录及文件名
			Writer out = new FileWriter(new File(HTML_GEN_PATH + itemId + ".html"));
			// 生成静态页面
			template.process(data, out);
			// 关闭流
			out.close();
			
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (TemplateNotFoundException e) {
			e.printStackTrace();
		} catch (MalformedTemplateNameException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
