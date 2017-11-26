package com.su.proto.core;

import java.io.File;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.protobuf.MessageLite;
import com.su.common.util.CoreUtils;

/**
 * 扫描指定包下的所有proto协议
 */
@Component
public class ProtoScan {

	@Autowired
	private ProtoContext protoContext;
	
	@Value("${proto.packName}")
	private String protoPackName;

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
	
	@PostConstruct
	public void init() throws Exception {
		scan(protoPackName);
	}
}
