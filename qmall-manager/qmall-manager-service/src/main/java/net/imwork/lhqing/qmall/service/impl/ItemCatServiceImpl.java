package net.imwork.lhqing.qmall.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.imwork.lhqing.qmall.common.pojo.EasyUITreeNode;
import net.imwork.lhqing.qmall.mapper.ItemCatMapper;
import net.imwork.lhqing.qmall.pojo.ItemCat;
import net.imwork.lhqing.qmall.pojo.ItemCatExample;
import net.imwork.lhqing.qmall.pojo.ItemCatExample.Criteria;
import net.imwork.lhqing.qmall.service.ItemCatService;

/**
 * 商品分类
 * 
 * @author lhq_i
 *
 */
@Service
public class ItemCatServiceImpl implements ItemCatService {

	// Q:商品分类Dao
	@Autowired
	ItemCatMapper itemCatMapper;

	/**
	 * Q:根据父节点获得商品分类信息
	 */
	@Override
	public List<EasyUITreeNode> getItemCat(long parentId) {
		// 根据parentId查询子节点列表
		ItemCatExample example = new ItemCatExample();
		// 设置查询条件
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		// 执行查询
		List<ItemCat> itemCatList = itemCatMapper.selectByExample(example);
		// 创建返回结果List
		List<EasyUITreeNode> resultList = new ArrayList<>();
		// 把列表准换成EasyUITreeNode列表
		for (ItemCat itemCat : itemCatList) {
			EasyUITreeNode node = new EasyUITreeNode();
			// 设置属性
			node.setId(itemCat.getId());
			node.setText(itemCat.getName());
			//Q:逆向工程将数据库中tinyint的is_parent自动转换为boolean型的isParent
				//通过判断是否为父节点来获取节点状态
			node.setState(itemCat.getIsParent() ? "closed" : "open");
			resultList.add(node);
		}
		// 返回结果
		return resultList;
	}

}
