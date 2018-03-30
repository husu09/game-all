package com.su.data.mq;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

@Component
public class MQServer {

	@Value("${mq.queueName}")
	private String queueName;
	@Value("${mq.host}")
	private String host;
	private Connection connection;
	@Autowired
	private CustomerWorker customerWorker;
	//Runtime.getRuntime().availableProcessors()
	private Customer[] customers = new Customer[1];

	public void start() {
		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(host);
			connection = factory.newConnection();
			for (int i = 0; i < customers.length; i++) {
				customers[0] = new Customer(connection.createChannel(), queueName, customerWorker);
				customers[0].start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		try {
			for (int i = 0; i < customers.length; i++) {
				if (customers[0] != null) {
					customers[0].stop();
				}
			}
			connection.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
