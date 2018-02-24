package net.imwork.lhqing.qmall.activemq;

import java.io.IOException;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

public class ActiveMQTest {

	/**
	 * 点到点形式发送消息
	 * @throws JMSException 
	 */
	@Test
	public void testQueueProducer() throws JMSException {
		//1.创建一个连接工厂对象，需要指定服务的ip及端口
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.132:61616");
		//2.使用工厂对象创建一个Connection对象
		Connection connection = connectionFactory.createConnection();
		//3.开启连接，调用Connection对象的start方法
		connection.start();
		//4.创建一个Session对象
			//第一个参数：是否开启事务（Q:与数据库事务没有关系，是ActiveMQ的事务，发送失败时重发）
					//一般不开启事务，如果为true（开启事务），则第二个参数失效无意义
			//第二个参数：应答模式，自动应答或者手动应答（如果第一个参数为true，则该参数无意义）
					//一般为自动应答
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//5.使用Session对象创建一个Destination对象。两种形式queue、topic，现在使用queue
		Queue queue = session.createQueue("qmall-test-queue");
		//6.使用Session对象创建一个Producer对象
		MessageProducer producer = session.createProducer(queue);
		//7.创建一个Message对象，可以使用TextMessage。
		//方法一
		//		TextMessage textMessage = new ActiveMQTextMessage();
		//		textMessage.setText("我是一条测试Queue形式发送的一条消息");
		//方法二
		TextMessage textMessage = session.createTextMessage("我是一条测试Queue形式发送的一条消息");
		//8.发送消息
		producer.send(textMessage);
		//9.关闭资源
		producer.close();
		session.close();
		connection.close();
	}
	
	
	
	/**
	 * 点到点形式接收消息
	 * @throws JMSException
	 * @throws IOException
	 */
	@Test
	public void testQueueConsumer() throws JMSException, IOException {
		//创建一个ConnectionFactory对象连接MQ服务器
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.132:61616");
		//创建一个连接对象
		Connection connection = connectionFactory.createConnection();
		//开启连接
		connection.start();
		//使用Connection对象创建一个Session对象
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//创建一个Destination对象的queue对象
		Queue queue = session.createQueue("qmall-test-queue");
		//使用Session对象创建一个消费者对象
		MessageConsumer consumer = session.createConsumer(queue);
		//接收消息
		consumer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				//打印结果
				TextMessage textMessage = (TextMessage) message;
				try {
					System.out.println(textMessage.getText());
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
		//等待接收消息
		System.in.read();
		//关闭资源
		consumer.close();
		session.close();
		connection.close();
	}
	
	

	/**
	 * 广播形式发送消息
	 * @throws JMSException 
	 */
	@Test
	public void testTopicProducer() throws JMSException {
		//1.创建一个连接工厂对象，需要指定服务的ip及端口
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.132:61616");
		//2.使用工厂对象创建一个Connection对象
		Connection connection = connectionFactory.createConnection();
		//3.开启连接，调用Connection对象的start方法
		connection.start();
		//4.创建一个Session对象
			//第一个参数：是否开启事务（Q:与数据库事务没有关系，是ActiveMQ的事务，发送失败时重发）
					//一般不开启事务，如果为true（开启事务），则第二个参数失效无意义
			//第二个参数：应答模式，自动应答或者手动应答（如果第一个参数为true，则该参数无意义）
					//一般为自动应答
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//5.使用Session对象创建一个Destination对象。两种形式queue、topic，现在使用topic
		Topic topic = session.createTopic("qmall-test-topic");
		//6.使用Session对象创建一个Producer对象
		MessageProducer producer = session.createProducer(topic);
		//7.创建一个Message对象，可以使用TextMessage。
		//方法一
		//		TextMessage textMessage = new ActiveMQTextMessage();
		//		textMessage.setText("我是一条测试Queue形式发送的一条消息");
		//方法二
		TextMessage textMessage = session.createTextMessage("我是一条测试Topic形式发送的一条消息");
		//8.发送消息
		producer.send(textMessage);
		//9.关闭资源
		producer.close();
		session.close();
		connection.close();
	}
	
	

	/**
	 * 广播形式接收消息
	 * @throws JMSException
	 * @throws IOException
	 */
	@Test
	public void testTopicConsumer() throws JMSException, IOException {
		//创建一个ConnectionFactory对象连接MQ服务器
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.132:61616");
		//创建一个连接对象
		Connection connection = connectionFactory.createConnection();
		//开启连接
		connection.start();
		//使用Connection对象创建一个Session对象
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//创建一个Destination对象的topic对象
		Topic topic = session.createTopic("qmall-test-topic");
		//使用Session对象创建一个消费者对象
		MessageConsumer consumer = session.createConsumer(topic);
		//接收消息
		consumer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				//打印结果
				TextMessage textMessage = (TextMessage) message;
				try {
					System.out.println(textMessage.getText());
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
		System.out.println("topic消费者03已经启动。。。");
		//等待接收消息
		System.in.read();
		//关闭资源
		consumer.close();
		session.close();
		connection.close();
	}
	
	
	
}
