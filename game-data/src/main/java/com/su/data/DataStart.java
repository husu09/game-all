package com.su.data;

import java.util.Scanner;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.su.data.config.DataConfig;
import com.su.data.mq.MQServer;

public class DataStart {
	public static void main(String[] args) {

		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DataConfig.class);
		MQServer mqServer = context.getBean(MQServer.class);
		mqServer.start();
		System.out.println("输入stop关闭服务器：");
		Scanner sc = new Scanner(System.in);
		while (true) {
			String command = sc.nextLine();
			if (command.equals("stop")) {
				// 关闭服务器
				mqServer.stop();
				context.close();
				break;
			}
		}
		sc.close();
	}
}
