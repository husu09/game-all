package com.su.excel.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = { "com.su" })
@PropertySource(value = "excelConfig.properties",encoding = "UTF-8")
public class ExcelConfig {

}
