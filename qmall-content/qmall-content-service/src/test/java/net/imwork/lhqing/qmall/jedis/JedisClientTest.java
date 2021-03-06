package net.imwork.lhqing.qmall.jedis;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import net.imwork.lhqing.qmall.common.jedis.JedisClient;


public class JedisClientTest {

	@Test
	public void testJedisClient(){
		//初始化spring容器
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
		//从容器中获得JedisClient对象
		JedisClient jedisClient = applicationContext.getBean(JedisClient.class);
		jedisClient.set("myTestKey", "666666~");
		String myTestValue = jedisClient.get("myTestKey");
		System.out.println(myTestValue);
	}
}
