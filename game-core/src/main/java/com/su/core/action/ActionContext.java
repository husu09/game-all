package com.su.core.action;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

/**
 * 存储协议处理对象
 * */
@Component
public class ActionContext {
	
	private Map<String, ActionMeta> actionMetaMap = new HashMap<>();
	
	public void add(String name, ActionMeta actionMeta) {
		actionMetaMap.put(name, actionMeta);
	}
	
	public ActionMeta get(String name) {
		return actionMetaMap.get(name);
	}
	
	public boolean contains(String name) {
		return actionMetaMap.containsKey(name);
	}
}
