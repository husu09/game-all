package com.su.client;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.su.core.config.AppConfig;

public class ClientStart {
	
	public static void main(String[] args) throws Exception {
		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		ClientUI clientUI = context.getBean(ClientUI.class);
		clientUI.show();
	}
}
