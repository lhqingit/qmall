package net.imwork.lhqing.qmall.content.service;

import java.util.List;

import net.imwork.lhqing.qmall.common.pojo.EasyUIDataGridResult;
import net.imwork.lhqing.qmall.common.pojo.QmallResult;
import net.imwork.lhqing.qmall.pojo.Content;

public interface ContentService {

	/**
	 * 添加一条内容
	 * @param content
	 * @return
	 */
	QmallResult addContent(Content content);

	/**
	 * 根据内容分类Id查询内容分类列表
	 * @param categoryId 内容分类Id
	 * @return
	 */
	List<Content> getContentListByCid(Long categoryId);

	/**
	 * Q:根据内容分类ID分页获取内容分类列表
	 * @param page
	 * @param rows
	 * @param categoryId 内容分类ID
	 * @return
	 */
	EasyUIDataGridResult getContentList(Integer page, Integer rows, Long categoryId);

	/**
	 * Q:删除内容
	 * @param ids 要删除的内容ID集合
	 * @return
	 */
	QmallResult deleteContent(Long[] ids);

	/**
	 * Q:编辑内容
	 * @param content
	 * @return
	 */
	QmallResult updateContent(Content content);
}
