package com.su.core.proto;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.google.protobuf.MessageLite;
import com.su.core.config.AppConfig;
import com.su.core.util.CoreUtils;

/**
 * 扫描指定包下的所有proto协议
 */
@Component
public class ProtoScan implements ApplicationListener<ContextRefreshedEvent>  {

	@Autowired
	private ProtoContext protoContext;
	@Autowired
	private AppConfig appConfig;

	public void scan(String packName) throws Exception {
		String packPath = packName.replace(".", "/");
		String realPath = Thread.currentThread().getContextClassLoader().getResource(packPath).getPath();
		File dir = new File(realPath);
		for (File chiled : dir.listFiles()) {
			if (chiled.isDirectory()) {
				scan(packName + "." + chiled.getName());
			} else {
				try {
					int lastIndex = chiled.getName().indexOf(".");
					String name = chiled.getName().substring(0, lastIndex);
					String[] arr = name.split("\\$");
					if (arr.length == 2 && !arr[1].endsWith("OrBuilder") && !CoreUtils.isInteger(arr[1])) {
						Class<?> c = Class.forName(packName + "." + arr[0] + "$" + arr[1]);
						MessageLite messageLite = (MessageLite) c.getMethod("getDefaultInstance").invoke(null);
						protoContext.add(c.getSimpleName(), messageLite);
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		try {
			scan(appConfig.getPackName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}