package net.imwork.lhqing.qmall.content.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import net.imwork.lhqing.qmall.common.jedis.JedisClient;
import net.imwork.lhqing.qmall.common.pojo.EasyUIDataGridResult;
import net.imwork.lhqing.qmall.common.pojo.QmallResult;
import net.imwork.lhqing.qmall.common.utils.JsonUtils;
import net.imwork.lhqing.qmall.content.service.ContentService;
import net.imwork.lhqing.qmall.mapper.ContentMapper;
import net.imwork.lhqing.qmall.pojo.Content;
import net.imwork.lhqing.qmall.pojo.ContentExample;

/**
 * 内容管理service
 * 
 * @author lhq_i
 *
 */
@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	ContentMapper contentMapper;
	@Autowired
	JedisClient jedisClient;

	@Value("${CONTENT_LIST}")
	private String CONTENT_LIST;

	@Override
	public QmallResult addContent(Content content) {
		// 将内容数据插入到内容表
		content.setCreated(new Date());
		contentMapper.insert(content);

		// 缓存同步，删除缓存中对应的数据
		jedisClient.hdel(CONTENT_LIST, content.getCategoryId().toString());

		return QmallResult.ok();
	}

	@Override
	public List<Content> getContentListByCid(Long categoryId) {
		// 查询缓存
		try {
			// 如果缓存中有 直接响应结果
			String contentListStr = jedisClient.hget(CONTENT_LIST, categoryId + "");
			if (StringUtils.isNotBlank(contentListStr)) {
				return JsonUtils.jsonToList(contentListStr, Content.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 如果缓存中没有 查询数据库
		ContentExample example = new ContentExample();
		example.createCriteria().andCategoryIdEqualTo(categoryId);
		List<Content> contentList = contentMapper.selectByExampleWithBLOBs(example);

		// 把结果添加到缓存
		try {
			jedisClient.hset(CONTENT_LIST, categoryId + "", JsonUtils.objectToJson(contentList));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return contentList;
	}

	@Override
	public EasyUIDataGridResult getContentList(Integer page, Integer rows, Long categoryId) {
		// 设置分页信息
		PageHelper.startPage(page, rows);
		// 执行查询
		ContentExample example = new ContentExample();
		example.createCriteria().andCategoryIdEqualTo(categoryId);
		List<Content> contentList = contentMapper.selectByExample(example);
		// 创建一个返回值对象
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setRows(contentList);
		// 取分页相关信息
		PageInfo pageInfo = new PageInfo(contentList);
		// 取总记录数
		long total = pageInfo.getTotal();
		result.setTotal(total);
		return result;
	}

	@Override
	public QmallResult deleteContent(Long[] ids) {
		// Q:为了缓存同步需要获取到对应的内容分类Id，而前端传过来的内容ids都是对应一个内容分类，所以查谁都可以
		Content content = contentMapper.selectByPrimaryKey(ids[0]);

		// Q:删除内容
		ContentExample contentExample = new ContentExample();
		contentExample.createCriteria().andIdIn(Arrays.asList(ids));
		contentMapper.deleteByExample(contentExample);

		//Q:防止空指针异常,更新和添加在这个地方也有可能发生空指针，但是概率几乎为0，这个地方在多个管理员同时操作后台时有可能发生
		if(content != null){
			// Q:缓存同步，删除缓存中对应的数据
			jedisClient.hdel(CONTENT_LIST, content.getCategoryId().toString());
		} else{
			System.out.println("在删除时，同步缓存失败了，请重新点击-编辑（不进行任何更改），再保存，来解决缓存不同步的情况");
		}

		// 返回成功信息
		return QmallResult.ok();
	}

	@Override
	public QmallResult updateContent(Content content) {
		contentMapper.updateByPrimaryKeySelective(content);

		// Q:缓存同步，删除缓存中对应的数据
		jedisClient.hdel(CONTENT_LIST, content.getCategoryId().toString());

		return QmallResult.ok();
	}

}
