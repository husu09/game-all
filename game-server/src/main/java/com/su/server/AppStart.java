package com.su.server;

import java.util.Scanner;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.su.core.netty.NettyServer;
import com.su.core.proto.ProtoScan;

public class AppStart {

	public static void main(String[] args) throws Exception {
		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		// 配置
		AppConfig appConfig = context.getBean(AppConfig.class);
		// 扫描proto协议
		ProtoScan protoScan = context.getBean(ProtoScan.class);
		protoScan.scan(appConfig.getPackName());
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
				break;
			}
		}
		sc.close();
	}
}
