package com.su.core.schedule;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

@Component
public class ScheduleManager {
	private ScheduledExecutorService pool = Executors
			.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

	public void start() {
		pool.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {

			}
		}, 0, 1, TimeUnit.SECONDS);
	}
	
	public void stop() {
		pool.shutdown();
	}
}
