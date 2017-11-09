package com.su.excel;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.su.excel.config.ExcelConfig;

public class ExcelStart {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ExcelConfig.class);
		context.close();
	}
}
