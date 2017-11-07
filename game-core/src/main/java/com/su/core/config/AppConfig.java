package com.su.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

import com.su.common.rmi.DataRmiService;

@Configuration
@ComponentScan(basePackages = { "com.su" })
@PropertySource("config.properties")
public class AppConfig {

	@Value("${rmi.url}")
	private String rmiUrl;

	@Bean
	public RmiProxyFactoryBean dataRmiService() {
		RmiProxyFactoryBean rmiProxy = new RmiProxyFactoryBean();
		rmiProxy.setServiceUrl(rmiUrl);
		rmiProxy.setServiceInterface(DataRmiService.class);
		return rmiProxy;
	}

}
