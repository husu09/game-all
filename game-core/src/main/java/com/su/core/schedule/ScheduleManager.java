package com.su.core.schedule;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ScheduleManager {
	private Logger logger = LoggerFactory.getLogger(ScheduleManager.class);
	
	private ScheduledExecutorService pool = Executors
			.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

	public void start() {
		pool.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {

			}
		}, 0, 1, TimeUnit.SECONDS);
		logger.info("初始化定时管理器成功");
	}
	
	public void stop() {
		pool.shutdown();
		logger.info("销毁定时管理器");
	}
}
