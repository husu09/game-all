package com.su.core.data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class CacheContext {
	
	private Map<Integer, Cache> cacheMap = new ConcurrentHashMap<>();
	
	
	
}
