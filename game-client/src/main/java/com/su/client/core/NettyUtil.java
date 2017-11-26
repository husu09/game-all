package com.su.client.core;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.su.common.util.SpringUtil;

public class NettyUtil {
	public static void start(String host, int port) {
		ClientContext ctx = ClientContext.getInstance();
		AnnotationConfigApplicationContext context = (AnnotationConfigApplicationContext) SpringUtil
				.getContext();

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


}
