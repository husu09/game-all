package com.su.server;

import java.util.Scanner;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.su.core.action.ActionScan;
import com.su.core.akka.AkkaContext;
import com.su.core.data.IDGenerator;
import com.su.core.data.MQProducer;
import com.su.core.data.RedisClient;
import com.su.core.schedule.ScheduleManager;
import com.su.excel.core.ExcelProcessor;
import com.su.proto.core.ProtoScan;
import com.su.server.config.ServerConfig;
import com.su.server.context.GameContext;
import com.su.server.event.GameEventDispatcher;
import com.su.server.netty.NettyServer;

public class ServerStart {

	public static void main(String[] args) throws Exception {

		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ServerConfig.class);
		System.out.println("==============================启动服务==============================");
		// 加载配置
		context.getBean(ExcelProcessor.class).reload();
		context.getBean(IDGenerator.class).init();
		context.getBean(ActionScan.class).scan();
		RedisClient redisClient = context.getBean(RedisClient.class);
		redisClient.init();
		MQProducer mqProducer = context.getBean(MQProducer.class);
		mqProducer.start();
		ScheduleManager scheduleManager = context.getBean(ScheduleManager.class);
		scheduleManager.start();
		NettyServer nettyServer = context.getBean(NettyServer.class);
		nettyServer.start();
		GameContext gameContext = context.getBean(GameContext.class);
		AkkaContext akkaContext = context.getBean(AkkaContext.class);
		ProtoScan protoScan = context.getBean(ProtoScan.class);
		protoScan.init();
		GameEventDispatcher gameEventDispatcher = context.getBean(GameEventDispatcher.class);
		gameEventDispatcher.serverStart();
		System.out.println("输入stop关闭服务器：");
		Scanner sc = new Scanner(System.in);
		while (true) {
			String command = sc.nextLine();
			if (command.equals("stop")) {
				gameContext.setStopping(true);
				nettyServer.stop();
				scheduleManager.stop();
				mqProducer.stop();
				redisClient.destroy();
				akkaContext.close();
				gameEventDispatcher.serverStop();
				context.close();
				break;
			}
		}
		sc.close();
		System.out.println("==============================关闭服务==============================");
	}

}
