package net.imwork.lhqing.qmall.publish;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Q:测试不使用Tomcat，自己发布服务，使用Tomcat是为了打成war包，便于部署
 * @author lhq_i
 *
 */
public class TestPublish {
	
	@Test
	public void publishService() throws Exception {
		ApplicationContext appicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
		
		System.out.println("服务已启动。。。");
		System.in.read();
		System.out.println("服务已关闭");
			
	}
	
	
}
