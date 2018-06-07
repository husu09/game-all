package com.su.core.schedule;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ScheduleManager {
	private Logger logger = LoggerFactory.getLogger(ScheduleManager.class);
	
	private ScheduledExecutorService schedulePool = Executors
			.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
	
	private ExecutorService pool = Executors.newCachedThreadPool();
	
	public void start() {
		// 定时任务
		schedulePool.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {

			}
		}, 0, 1, TimeUnit.SECONDS);
		
		// 任务
		pool.execute(new Runnable() {
			
			@Override
			public void run() {
				
			}
		});
		logger.info("初始化定时管理器成功");
	}
	
	public void stop() {
		schedulePool.shutdown();
		logger.info("销毁定时管理器");
	}
}
