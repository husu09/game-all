package com.su.data.mq;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;

@Component
public class MQClient implements ApplicationListener<ContextRefreshedEvent> {

	@Value("${mq.queueName}")
	private String queueName;
	private Channel channel = null;
	private Connection connection = null;

	public void init() {
		try {
			// 创建连接工厂
			ConnectionFactory factory = new ConnectionFactory();
			// 设置RabbitMQ相关信息
			factory.setHost("localhost");
			// factory.setUsername();
			// factory.setPassword();
			// factory.setPort();
			// 创建一个新的连接
			connection = factory.newConnection();
			// 创建一个通道
			channel = connection.createChannel();
			// 声明一个队列
			// 告诉服务器我们需要那个频道的消息，如果频道中有消息，就会执行回调函数handleDelivery
			channel.queueDeclare(queueName, true, false, false, null);
			Consumer consumer = new MQCustomer(channel);
			// 自动回复队列应答 -- RabbitMQ中的消息确认机制
			channel.basicConsume(queueName, true, consumer);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@PreDestroy
	public void destroy() throws Exception {
		// 关闭通道和连接
		if (channel != null)
			channel.close();
		if (connection != null)
			connection.close();
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		init();
	}

}
