package com.su.core.data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

@Component
public class MQClient {

	@Value("${mq.queueName}")
	private String queueName;
	private Channel channel = null;
	private Connection connection = null;

	
	public void init() {
		// 创建连接工厂
		ConnectionFactory factory = new ConnectionFactory();
		// 设置RabbitMQ相关信息
		factory.setHost("localhost");
		// factory.setUsername();
		// factory.setPassword();
		// factory.setPort();
		// 创建一个新的连接
		try {
			connection = factory.newConnection();
			// 创建一个通道
			channel = connection.createChannel();
			// 声明一个队列
			channel.queueDeclare(queueName, true, false, false, null);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void send(String s) {
		// 发送消息到队列中
		try {
			channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, s.getBytes("UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("发送消息到时 mq 失败 " + s);
		}
	}


	public void destroy() {
		// 关闭通道和连接
		if (channel != null)
			try {
				channel.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		if (connection != null)
			try {
				connection.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
