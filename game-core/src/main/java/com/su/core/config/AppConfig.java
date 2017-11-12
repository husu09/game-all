package com.su.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

import com.su.common.rmi.DataRmiService;

import redis.clients.jedis.Jedis;

@Configuration
@ComponentScan(basePackages = { "com.su" })
@PropertySource("config.properties")
public class AppConfig {
	
	@Bean
	public RmiProxyFactoryBean dataRmiService() {
		RmiProxyFactoryBean rmiProxy = new RmiProxyFactoryBean();
		rmiProxy.setServiceUrl("rmi://localhost/DataRmiService");
		rmiProxy.setServiceInterface(DataRmiService.class);
		return rmiProxy;
	}

}
