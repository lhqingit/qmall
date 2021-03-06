package net.imwork.lhqing.qmall.freemarker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FreeMarkerTest {

	@Test
	public void testFreeMarker() throws IOException, TemplateException{
		//1.创建一个模板文件
		//2.创建一个Configuration对象
		Configuration configuration = new Configuration(Configuration.getVersion());
		//3.设置模板文件保存的目录
		configuration.setDirectoryForTemplateLoading(new File("E:/Qing_java_software/workspaces"
				+ "/qmall_work/eclipse_qmall_workspace/qmall-item-web/src/main/webapp/WEB-INF/ftl"));
		//4.模板文件的编码格式，一般为UTF-8
		configuration.setDefaultEncoding("UTF-8");
		//5.加载一个模板文件，创建一个模板对象
		Template template = configuration.getTemplate("hello.ftl");
		//6.创建一个数据集。可以是pojo也可以是map.推荐使用map
		Map<String, String> data = new HashMap<>();
		data.put("hello", "hello freemarker!");
		//7.创建一个Writer对象，指定输出文件的路径及文件名
		Writer out = new FileWriter(new File("E:/Qing_java_software/workspaces/qmall_work/"
				+ "eclipse_qmall_workspace/qmall-item-web/src/main/webapp/WEB-INF/ftl/hello.txt"));
		//8.生成静态页面
		template.process(data, out);
		//9.关闭流
		out.close();
		
	}
	
	
	@Test
	public void testFreeMarkerByStudent() throws IOException, TemplateException{
		//1.创建一个模板文件
		//2.创建一个Configuration对象
		Configuration configuration = new Configuration(Configuration.getVersion());
		//3.设置模板文件保存的目录
		configuration.setDirectoryForTemplateLoading(new File("E:/Qing_java_software/workspaces"
				+ "/qmall_work/eclipse_qmall_workspace/qmall-item-web/src/main/webapp/WEB-INF/ftl"));
		//4.模板文件的编码格式，一般为UTF-8
		configuration.setDefaultEncoding("UTF-8");
		//5.加载一个模板文件，创建一个模板对象
		Template template = configuration.getTemplate("student.ftl");
		//6.创建一个数据集。可以是pojo也可以是map.推荐使用map
		Map<String, Object> data = new HashMap<>();
		//创建一个POJO对象
		Student student = new Student(1417070214,"啦啦啦",22,"辽宁沈阳");
		data.put("student", student);
		//添加一个List
		List<Student> stuList = new ArrayList<>();
		stuList.add(new Student(2, "多", 20, "浑南"));
		stuList.add(new Student(3, "巴", 21, "北京"));
		stuList.add(new Student(4, "胺", 22, "上海"));
		stuList.add(new Student(5, "啊", 23, "成都"));
		data.put("stuList", stuList);
		//添加日期类型
		data.put("dateQ", new Date());
		//NULL值的测试
//		data.put("isANullVal", "我是一个有内容的值啊!");
//		data.put("isANullVal", null);
		//Q:测试模板引用需要的值
		data.put("hello", "hello freemarker!");
		//7.创建一个Writer对象，指定输出文件的路径及文件名
		Writer out = new FileWriter(new File("E:/Qing_java_software/workspaces/qmall_work/"
				+ "eclipse_qmall_workspace/qmall-item-web/src/main/webapp/WEB-INF/ftl/student.html"));
		//8.生成静态页面
		template.process(data, out);
		//9.关闭流
		out.close();
		
	}
	
	
	
}
