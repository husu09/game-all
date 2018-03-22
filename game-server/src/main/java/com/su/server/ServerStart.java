package com.su.server;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.su.excel.core.ExcelProcessor;
import com.su.server.config.ServerConfig;

@Component
public class ServerStart  implements ApplicationListener<ContextRefreshedEvent>  {
	
	@Autowired
	private ExcelProcessor preDataProcess;

	public static void main(String[] args) throws Exception {
		
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ServerConfig.class);
		System.out.println("输入stop关闭服务器：");
		Scanner sc = new Scanner(System.in);
		while (true) {
			String command = sc.nextLine();
			if (command.equals("stop")) {
				context.close();
				break;
			}
		}
		sc.close();
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		// 预处理配置
		preDataProcess.refresh();
	}
}
