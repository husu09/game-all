package com.su.core.schedule;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ScheduleManager {
	private ScheduledExecutorService pool = Executors.newSingleThreadScheduledExecutor();	
	
	@PostConstruct
	public void init() {
		ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);
		pool.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				
			}
		}, 0, 1, TimeUnit.SECONDS);
	}
}