package com.su.client.core;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.su.client.config.ClientConfig;

public class ClientStart {
	
	public static void main(String[] args) throws Exception {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ClientConfig.class);
		ClientUI clientUI = context.getBean(ClientUI.class);
		// 显示UI
		clientUI.show();
	}
}
