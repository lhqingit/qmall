package net.imwork.lhqing.qmall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import net.imwork.lhqing.qmall.common.pojo.EasyUITreeNode;
import net.imwork.lhqing.qmall.common.pojo.QmallResult;
import net.imwork.lhqing.qmall.content.service.ContentCategoryService;

/**
 * 内容分类管理
 * @author lhq_i
 *
 */
@Controller
@RequestMapping("/content/category")
public class ContentCategoryController {
	@Autowired
	private ContentCategoryService contentCategoryService;
	
	@RequestMapping("/list")
	@ResponseBody
	public List<EasyUITreeNode> getContentCategoryList(//Q:当传过来的Id是null时parentId默认为0
			@RequestParam(name="id", defaultValue="0")Long parentId){
		return contentCategoryService.getContentCategoryList(parentId);
	}
	
	/**
	 * 添加分类节点
	 */
	@RequestMapping(value="/create", method=RequestMethod.POST)
	@ResponseBody//Q:不要忘记写这个注解了,要不然就会经过视图解析器，就不能正确返回信息了
	public QmallResult createContentCategory(Long parentId,String name){
		//调用服务来添加节点
		return contentCategoryService.addContentCategory(parentId, name);
	}
	
	/**
	 * Q:编辑分类节点
	 */
	@RequestMapping(value="/update", method=RequestMethod.POST)
	@ResponseBody
	public QmallResult updateContentCategory(Long id,String name){
		return contentCategoryService.updateContentCategory(id, name);
	}
	
	/**
	 * Q:删除分类节点
	 */
	@RequestMapping(value="delete", method=RequestMethod.POST)
	@ResponseBody
	public QmallResult deleteContentCategory(Long id){
		return contentCategoryService.deleteContentCategory(id);
	}
	
	
}
