package net.imwork.lhqing.qmall.content.service;

import java.util.List;

import net.imwork.lhqing.qmall.common.pojo.EasyUITreeNode;
import net.imwork.lhqing.qmall.common.pojo.QmallResult;

public interface ContentCategoryService {
	/**
	 * Q:获取内容分类列表
	 * @param parentId
	 * @return
	 */
	List<EasyUITreeNode> getContentCategoryList(long parentId);
	
	/**
	 * Q:添加内容分类
	 * @param parentId 父类目Id
	 * @param name 分类名称
	 * @return
	 */
	QmallResult addContentCategory(long parentId,String name);

	/**
	 * Q:编辑内容分类
	 * @param id 要更新的内容分类ID
	 * @param name 要修改成的内容分类名称
	 * @return
	 */
	QmallResult updateContentCategory(Long id, String name);

	/**
	 * Q:删除内容分类
	 * @param id 要删除的内容分类ID
	 * @return
	 */
	QmallResult deleteContentCategory(Long id);
}
