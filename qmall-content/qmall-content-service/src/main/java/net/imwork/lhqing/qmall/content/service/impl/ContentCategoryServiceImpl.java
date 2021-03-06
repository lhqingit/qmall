package net.imwork.lhqing.qmall.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.imwork.lhqing.qmall.common.pojo.EasyUITreeNode;
import net.imwork.lhqing.qmall.common.pojo.QmallResult;
import net.imwork.lhqing.qmall.content.service.ContentCategoryService;
import net.imwork.lhqing.qmall.mapper.ContentCategoryMapper;
import net.imwork.lhqing.qmall.pojo.ContentCategory;
import net.imwork.lhqing.qmall.pojo.ContentCategoryExample;
import net.imwork.lhqing.qmall.pojo.ContentCategoryExample.Criteria;

/**
 * 内容分类管理
 * 
 * @author lhq_i
 *
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

	@Autowired
	private ContentCategoryMapper contentCategoryMapper;

	@Override
	public List<EasyUITreeNode> getContentCategoryList(long parentId) {
		// 根据parentId查询子节点列表
		ContentCategoryExample example = new ContentCategoryExample();
		Criteria criteria = example.createCriteria();
		// 设置查询条件
		criteria.andParentIdEqualTo(parentId);
		// 执行查询
		List<ContentCategory> categoryList = contentCategoryMapper.selectByExample(example);
		// 转换成EasyUITreeNode的列表
		List<EasyUITreeNode> nodeList = new ArrayList<>();
		for (ContentCategory contentCategory : categoryList) {
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(contentCategory.getId());
			node.setText(contentCategory.getName());
			node.setState(contentCategory.getIsParent() ? "closed" : "open");
			//添加到列表
			nodeList.add(node);
		}
		return nodeList;
	}

	@Override
	public QmallResult addContentCategory(long parentId, String name) {
		//创建一个content_category表对应的POJO对象
		ContentCategory contentCategory = new ContentCategory();
		//设置POJO的属性
		contentCategory.setParentId(parentId);
		contentCategory.setName(name);
		//Q:默认状态为1(正常)，默认排序为1,不需要再手动插入数据
		//Q:新添加的节点一定是叶子节点，默认值为0，也就是，不是父类目
		
		
		contentCategory.setCreated(new Date());
		//插入到数据库
		contentCategoryMapper.insertSelective(contentCategory);
		//判断父节点的isParent的属性，如果是false,改为true
		  //Q:数据库中存的是0/1，1为true，0为false MySQL中没有boolean类型，所以会用到tinyint类型来表示
			//根据parentId查询父节点
		ContentCategory parent = contentCategoryMapper.selectByPrimaryKey(parentId);
		//Q:前面的判断是防止空指针异常
		if(parent != null && !parent.getIsParent()){
			//Q:更新父节点isParent为是父节点
			ContentCategory updateParent = new ContentCategory();
			updateParent.setId(parentId);
			updateParent.setIsParent(true);
			//更新到数据库
			contentCategoryMapper.updateByPrimaryKeySelective(updateParent);
		}
		//返回结果，QmallResult，包含POJO
		return QmallResult.ok(contentCategory);
	}

	@Override
	public QmallResult updateContentCategory(Long id, String name) {
		//Q:更新内容分类数据
		ContentCategory contentCategory = new ContentCategory();
		contentCategory.setId(id);
		contentCategory.setName(name);
		//Q:严格条件下还需要在这个地方根据上面返回的更新条数是否为1来进行判断是否更新成功，
			//不成功则抛出异常，以前在公司我就是这么写的，但是在这个地方就不进行这么严格的判断了
		contentCategoryMapper.updateByPrimaryKeySelective(contentCategory);
		
		//Q:返回结果
		return QmallResult.ok();
	}

	@Override//Q:没有进行严格的判断
	public QmallResult deleteContentCategory(Long id) {
		//Q:查询该节点是否是父节点
		ContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
			//如果是，则不进行删除，并返回相应信息
		if(contentCategory == null || contentCategory.getIsParent()){
			return QmallResult.build(206, "该节点包含子节点，不可以删除哟~");
		}
		//Q:获得父节点的Id
		Long parentId = contentCategory.getParentId();
		
		//Q:删除节点
		contentCategoryMapper.deleteByPrimaryKey(id);
		//Q:查询该节点的父节点是否还有子节点
		ContentCategoryExample example = new ContentCategoryExample();
		example.createCriteria().andParentIdEqualTo(parentId);
		int  childNodeCount = contentCategoryMapper.countByExample(example);
			//如果没有，则修改父节点的isParent属性为false
		if(childNodeCount == 0){
			 ContentCategory parentContentCategory = new ContentCategory();
			 parentContentCategory.setId(parentId);
			 parentContentCategory.setIsParent(false);
			 contentCategoryMapper.updateByPrimaryKeySelective(parentContentCategory);
		}
		//Q:返回结果
		return QmallResult.ok();
	}

}
