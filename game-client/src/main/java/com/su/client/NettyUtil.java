package com.su.client;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.su.core.util.SpringContextUtil;

public class NettyUtil {
	public static void start(String host, int port) {
		ClientContext ctx = ClientContext.getInstance();
		AnnotationConfigApplicationContext context = (AnnotationConfigApplicationContext) SpringContextUtil
				.getSpringContext();

		// 启动服务
		NettyClient client = context.getBean(NettyClient.class);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					client.start(host, port);
				} catch (Exception e) {
					System.out.println("启动netty客户端失败！");
					e.printStackTrace();
				}
			}
		}, "netty-client").start();

		// 等待连接完成
		try {
			ctx.getCdl().await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public static void stop() {

	}
}
