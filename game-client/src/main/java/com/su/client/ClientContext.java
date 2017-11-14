package com.su.client;

import java.util.HashMap;
import java.util.Map;

import com.google.protobuf.MessageLite;

public class ClientContext {

	private static ClientContext instance = new ClientContext();

	private ClientContext() {

	}

	public static ClientContext getInstance() {
		return instance;
	}

	/**
	 * 当前选中的协议
	 */
	private volatile MessageLite selectMessageLite;

	public MessageLite getSelectMessageLite() {
		return selectMessageLite;
	}

	public void setSelectMessageLite(MessageLite selectMessageLite) {
		this.selectMessageLite = selectMessageLite;
	}
	
	/**
	 * <协议名称,<属性名，类型>>
	 * */
	private Map<String, Map<String, Integer>> map = new HashMap<>();
	
	public void addProperty(String messageName, String propertyName, int propertyType) {
		if (map.get(messageName) == null) {
			map.put(messageName, new HashMap<>(5));
		}
		map.get(messageName).put(propertyName, propertyType);
	}
	
	public int getProperty(String messageName, String propertyName) {
		return map.get(messageName).get(propertyName);
	}

}
