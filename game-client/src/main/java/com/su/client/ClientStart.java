package com.su.client;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.su.core.config.AppConfig;

public class ClientStart {
	
	public static void main(String[] args) throws Exception {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		ClientUI clientUI = context.getBean(ClientUI.class);
		// 显示UI
		clientUI.show();
	}
}
