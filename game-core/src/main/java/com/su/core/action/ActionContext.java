package com.su.core.action;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

/**
 * 存储协议处理对象
 * */
@Component
public class ActionContext {
	
	private Map<String, ActionMeta> map = new HashMap<>();
	
	public void add(String name, ActionMeta actionMeta) {
		map.put(name, actionMeta);
	}
	
	public ActionMeta get(String name) {
		return map.get(name);
	}
	
	public boolean contains(String name) {
		return map.containsKey(name);
	}
}
