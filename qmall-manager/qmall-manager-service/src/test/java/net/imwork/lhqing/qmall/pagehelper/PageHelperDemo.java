package net.imwork.lhqing.qmall.pagehelper;

import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import net.imwork.lhqing.qmall.mapper.ItemMapper;
import net.imwork.lhqing.qmall.pojo.Item;
import net.imwork.lhqing.qmall.pojo.ItemExample;

public class PageHelperDemo {

	/**
	 * 学习PageHrlper的使用
	 */
	@Test
	public void testPageHelper() {
		// 初始化spring容器
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"classpath:spring/applicationContext-dao.xml");
		// 从容器中获得Mapper代理对象
		ItemMapper itemMapper = applicationContext.getBean(ItemMapper.class);
		// 执行sql语句之前设置分页信息，使用PageHelper的startPage()静态方法
		PageHelper.startPage(1, 10);// Q:第一页，显示十条数据
		//紧跟着的第一个select方法会被分页
		// 执行查询
		ItemExample example = new ItemExample();
		List<Item> itemList = itemMapper.selectByExample(example);
		// 用PageInfo取分页信息，1.总记录数 2.总页数3.当前页码4.页面条数5.
		PageInfo<Item> pageInfo = new PageInfo<>(itemList);
		System.out.println("1.总记录数,total:" + pageInfo.getTotal());
		System.out.println("2.总页数,pages:" + pageInfo.getPages());
		System.out.println("3.当前页码,pageNum:" + pageInfo.getPageNum());
		System.out.println("4.页面条数,pageSize:" + pageInfo.getPageSize());
		System.out.println("4.页面条数,list.size():" + itemList.size());
	}

}
