package net.imwork.lhqing.qmall.service;

import java.util.List;

import net.imwork.lhqing.qmall.common.pojo.EasyUITreeNode;

/**
 * 商品分类
 * @author lhq_i
 *
 */
public interface ItemCatService {
	/**
	 * Q:根据父节点获得商品分类信息
	 * @param parentId 父节点
	 * @return List<EasyUITreeNode> 商品分类信息集合
	 */
	List<EasyUITreeNode> getItemCat(long parentId);
}
