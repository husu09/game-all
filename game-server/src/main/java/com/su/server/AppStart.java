package com.su.server;

import java.util.Scanner;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.su.core.config.AppConfig;
import com.su.core.netty.NettyServer;

public class AppStart {

	public static void main(String[] args) throws Exception {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		// 启动服务
		NettyServer server = context.getBean(NettyServer.class);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					server.start();
				} catch (Exception e) {
					System.out.println("启动netty服务失败！");
					e.printStackTrace();
				}
			}
		}, "netty-server").start();
		
		

		System.out.println("输入stop关闭服务器：");
		Scanner sc = new Scanner(System.in);
		while (true) {
			String command = sc.nextLine();
			if (command.equals("stop")) {
				// 关闭服务器
				server.stop();
				context.stop();
				break;
			}
		}
		sc.close();
	}
}
