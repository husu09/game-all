package com.su.server;

import java.util.Scanner;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.su.core.action.ActionScan;
import com.su.core.context.GameContext;
import com.su.core.data.IDGenerator;
import com.su.core.data.MQClient;
import com.su.core.data.RedisClient;
import com.su.core.netty.NettyServer;
import com.su.excel.core.ExcelProcessor;
import com.su.server.config.ServerConfig;

public class ServerStart {

	public static void main(String[] args) throws Exception {

		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ServerConfig.class);
		// 加载配置
		context.getBean(ExcelProcessor.class).reload();
		context.getBean(IDGenerator.class).init();
		context.getBean(ActionScan.class).scan();
		MQClient mqClient = context.getBean(MQClient.class);
		mqClient.init();
		RedisClient redisClient = context.getBean(RedisClient.class);
		redisClient.init();
		NettyServer nettyServer = context.getBean(NettyServer.class);
		nettyServer.init();
		GameContext gameContext = context.getBean(GameContext.class);

		System.out.println("输入stop关闭服务器：");
		Scanner sc = new Scanner(System.in);
		while (true) {
			String command = sc.nextLine();
			if (command.equals("stop")) {
				gameContext.setStopping(true);
				mqClient.destroy();
				redisClient.destroy();
				nettyServer.destroy();
				context.close();
				break;
			}
		}
		sc.close();
	}

}
