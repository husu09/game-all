package com.su.excel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.su.excel.config.ExcelConfig;
import com.su.excel.core.ExcelManager;
import com.su.excel.core.PreDataProcess;


@Component
public class ExcelStart implements ApplicationListener<ContextRefreshedEvent> {
	
	@Autowired
	private PreDataProcess preDataProcess;
	
	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ExcelConfig.class);
		context.close();
	}

	@Override 
	public void onApplicationEvent(ContextRefreshedEvent event) {
		//preDataProcess.process();
		preDataProcess.refresh();
	}
}
