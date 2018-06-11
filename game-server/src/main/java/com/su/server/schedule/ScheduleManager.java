package com.su.server.schedule;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.su.core.gambling.Site;
import com.su.server.service.GamblingService;

@Component
public class ScheduleManager {

	@Autowired
	private GamblingService gamblingService;

	private Logger logger = LoggerFactory.getLogger(ScheduleManager.class);

	private ScheduledExecutorService schedulePool = Executors
			.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

	private ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

	public void start() {
		// 定时任务
		schedulePool.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {

			}
		}, 0, 1, TimeUnit.SECONDS);

		// 任务
		for (Site site : gamblingService.getSiteMap().values()) {
			pool.execute(new Runnable() {

				@Override
				public void run() {
					site.doWaitTable();
				}
			});
			pool.execute(new Runnable() {

				@Override
				public void run() {
					site.doWaitGamePlayer();
				}
			});
		}
		logger.info("初始化定时管理器成功");
	}

	public void stop() {
		schedulePool.shutdown();
		logger.info("销毁定时管理器");
	}
}