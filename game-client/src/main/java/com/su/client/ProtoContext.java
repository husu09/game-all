package com.su.client;

import java.util.HashMap;
import java.util.Map;

public class ProtoContext {
	
	private static ProtoContext instance;
	
	private ProtoContext() {
		
	}
	
	public static ProtoContext getInstance() {
		if (instance == null) {
			instance = new ProtoContext();
		}
		return instance;
	}
	
	/**
	 * <协议名称，Class类>
	 * */
	private Map<String, Class<?>> protoClassMap = new HashMap<>();
	/**
	 * <协议名，<属性名，set 方法名称>>
	 * */
	private Map<String, Map<String, String>> protoMetaMap = new HashMap<>();

	
	
	
	public Map<String, Class<?>> getClassMap() {
		return protoClassMap;
	}

	/**
	 * @param name 协议名称
	 * @param c	Class
	 */
	public void addClass(String name, Class<?> c) {
		protoClassMap.put(name, c);
	}
	
	/**
	 * @param name 协议名称
	 * @return
	 */
	public Class<?> getClass(String name) {
		return protoClassMap.get(name);
	}
	
	/**
	 * @param name 协议名称
	 * @return
	 */
	public boolean containsClass(String name) {
		return protoClassMap.containsKey(name);
	}
	
	/**
	 * @param name 协议名称
	 * @return
	 */
	public Map<String, String> getMeta(String name) {
		if (protoMetaMap.get(name) == null) {
			protoMetaMap.put(name, new HashMap<>());
		}
		return protoMetaMap.get(name);
	}
	
	/**
	 * @param name 协议名称
	 */
	public boolean containsMeta(String name) {
		return protoMetaMap.containsKey(name);
	}
	
}
