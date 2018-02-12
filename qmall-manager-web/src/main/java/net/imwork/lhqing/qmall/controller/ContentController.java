package net.imwork.lhqing.qmall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.imwork.lhqing.qmall.common.pojo.EasyUIDataGridResult;
import net.imwork.lhqing.qmall.common.pojo.QmallResult;
import net.imwork.lhqing.qmall.content.service.ContentService;
import net.imwork.lhqing.qmall.pojo.Content;

/**
 * 内容管理
 * @author lhq_i
 *
 */
@Controller
@RequestMapping("/content")
public class ContentController {

	@Autowired
	private ContentService contentService;
	
	/**
	 * 新增内容
	 * @param content
	 * @return
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public QmallResult saveContent(Content content){
		//调用服务把内容数据保存到数据库
		return contentService.addContent(content);
	}
	
	/**
	 * Q:根据内容分类ID分页获取内容分类列表
	 * @param page 
	 * @param rows
	 * @param categoryId
	 * @return
	 */
	@RequestMapping("/query/list")
	@ResponseBody
	public EasyUIDataGridResult getItemList(Integer page,Integer rows,Long categoryId){
		//调用服务，查询商品列表
		EasyUIDataGridResult result = contentService.getContentList(page, rows, categoryId);
		return result;
	}
	
	/**
	 * Q:删除内容
	 * @param ids 要删除的内容Id集合
	 * @return
	 */
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	@ResponseBody
	public QmallResult deleteContent(Long[] ids){
		QmallResult result = contentService.deleteContent(ids);
		return result;
	}
	
	
	@RequestMapping(value="/edit", method=RequestMethod.POST)
	@ResponseBody
	public QmallResult editContent(Content content){
		return contentService.updateContent(content);
	}
}
