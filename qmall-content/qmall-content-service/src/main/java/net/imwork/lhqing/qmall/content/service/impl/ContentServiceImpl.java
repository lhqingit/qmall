package net.imwork.lhqing.qmall.content.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import net.imwork.lhqing.qmall.common.pojo.EasyUIDataGridResult;
import net.imwork.lhqing.qmall.common.pojo.QmallResult;
import net.imwork.lhqing.qmall.content.service.ContentService;
import net.imwork.lhqing.qmall.mapper.ContentMapper;
import net.imwork.lhqing.qmall.pojo.Content;
import net.imwork.lhqing.qmall.pojo.ContentExample;
import net.imwork.lhqing.qmall.pojo.Item;
import net.imwork.lhqing.qmall.pojo.ItemDescExample;
import net.imwork.lhqing.qmall.pojo.ItemExample;

/**
 * 内容管理service
 * @author lhq_i
 *
 */
@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	ContentMapper contentMapper;
	
	@Override
	public QmallResult addContent(Content content) {
		//将内容数据插入到内容表
		content.setCreated(new Date());
		contentMapper.insert(content);
		return QmallResult.ok();
	}

	@Override
	public List<Content> getContentListByCid(Long categoryId) {
		ContentExample example = new ContentExample();
		example.createCriteria().andCategoryIdEqualTo(categoryId);
		return contentMapper.selectByExampleWithBLOBs(example);
	}

	@Override
	public EasyUIDataGridResult getContentList(Integer page, Integer rows, Long categoryId) {
		//设置分页信息
		PageHelper.startPage(page, rows);
		//执行查询
		ContentExample example = new ContentExample();
		example.createCriteria().andCategoryIdEqualTo(categoryId);
		List<Content> contentList = contentMapper.selectByExample(example);
		//创建一个返回值对象
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setRows(contentList);
		//取分页相关信息
		PageInfo pageInfo = new PageInfo(contentList);
		//取总记录数
		long total = pageInfo.getTotal();
		result.setTotal(total);
		return result;
	}

	@Override
	public QmallResult deleteContent(Long[] ids) {
		//Q:删除内容
		ContentExample contentExample = new ContentExample();
		contentExample.createCriteria().andIdIn(Arrays.asList(ids));
		contentMapper.deleteByExample(contentExample);
		//返回成功信息
		return QmallResult.ok();
	}

	@Override
	public QmallResult updateContent(Content content) {
		contentMapper.updateByPrimaryKeySelective(content);
		return QmallResult.ok();
	}

}
