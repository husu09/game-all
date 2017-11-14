package com.su.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = {"com.su"})
@PropertySource("config.properties")
public class AppConfig {
	@Value("${server.port}")
	private int port;
	@Value("${proto.packName}")
	private String packName;

	public int getPort() {
		return port;
	}

	public String getPackName() {
		return packName;
	}
	
	
}
