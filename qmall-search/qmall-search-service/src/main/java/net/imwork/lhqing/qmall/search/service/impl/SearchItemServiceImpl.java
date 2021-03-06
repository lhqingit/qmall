package net.imwork.lhqing.qmall.search.service.impl;

import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.imwork.lhqing.qmall.common.pojo.QmallResult;
import net.imwork.lhqing.qmall.common.pojo.SearchItem;
import net.imwork.lhqing.qmall.search.mapper.SearchItemMapper;
import net.imwork.lhqing.qmall.search.server.SearchItemService;

/**
 * 索引库维护Service
 * @author lhq_i
 *
 */
@Service
public class SearchItemServiceImpl implements SearchItemService{
	
	@Autowired
	private SearchItemMapper searchItemMapper;
	@Autowired
	private SolrServer solrServer;
	
	@Override
	public QmallResult importAllItems() {
		
		
		try {
			//查询商品列表
			List<SearchItem> itemList = searchItemMapper.getItemList();
			//遍历商品列表
			for (SearchItem searchItem : itemList) {
				//创建文档对象
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
			}
			//提交
			solrServer.commit();
			//返回导入成功
			return QmallResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return QmallResult.build(500, "数据导入失败");
		}
		
	}

	
}
