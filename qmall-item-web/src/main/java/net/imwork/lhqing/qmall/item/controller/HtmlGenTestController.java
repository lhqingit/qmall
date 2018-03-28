package net.imwork.lhqing.qmall.item.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

/**
 * Q:测试Freemarker与springMVC整合
 * 生成静态页面测试
 * @author lhq_i
 *
 */
@Controller
public class HtmlGenTestController {
	
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	
	
	@RequestMapping(value = "/genHtml")
	@ResponseBody
	public String genHtml() throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException{
		Configuration configuration = freeMarkerConfigurer.getConfiguration();
		//加载模板对象
		Template template = configuration.getTemplate("hello.ftl");
		//创建一个数据集
		Map<String,String> data = new HashMap<>();
		data.put("hello", "123456");
		//指定文件输出的路径及文件名
		Writer out = new FileWriter(new File("E:/Qing_java_software/workspaces/qmall_work/"
				+ "eclipse_qmall_workspace/qmall-item-web/src/main/webapp/WEB-INF/ftl/hello.html"));
		//输出文件
		template.process(data, out);
		//关闭流
		out.close();
		return "OK";
	}
}
