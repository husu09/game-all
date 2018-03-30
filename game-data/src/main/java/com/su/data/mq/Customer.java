package com.su.data.mq;

import java.io.IOException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class Customer {

	private String queueName;
	private CustomerWorker worker;
	private Channel channel;

	public Customer(Channel channel, String queueName, CustomerWorker worker) {
		this.channel = channel;
		this.queueName = queueName;
		this.worker = worker;
	}

	public void start() {
		try {
			channel.queueDeclare(queueName, true, false, false, null);
			channel.basicQos(1);

			Consumer consumer = new DefaultConsumer(channel) {

				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties,
						byte[] body) throws IOException {
					String message = new String(body, "UTF-8");
					try {
						worker.doWork(message);
					} finally {
						channel.basicAck(envelope.getDeliveryTag(), false);
					}
				}
			};
			boolean autoAck = false;
			channel.basicConsume(queueName, autoAck, consumer);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void stop() {
		// 关闭通道和连接
		if (channel != null)
			try {
				channel.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

}
