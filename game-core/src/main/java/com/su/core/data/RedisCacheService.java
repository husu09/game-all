package com.su.core.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;

import redis.clients.jedis.Jedis;

public class RedisCacheService {
	
	private RedisClient client;
	
	private CacheUtil cacheUtil;
	
	public void saveOrUpdate(Object o) {
		if (!cacheUtil.isPersistent(o)) {
			return;
		} 
		client.setForMap(cacheUtil.getParentKey(o), cacheUtil.getKey(o) , JSON.toJSONString(o));
	}
	
	public void saveOrUpdate(Collection<Object> os) {
		for (Object o : os) {
			saveOrUpdate(o);
		}
		
	}
	
	public void saveOrUpdate(Object[] os) {
		for (Object o : os) {
			saveOrUpdate(o);
		}
		
	}

	public void delete(Class<?> c) {
		client.delete(cacheUtil.getParentKey(c));
	}
	
	public void delete(Object o) {
		client.deleteForMap(cacheUtil.getParentKey(o), cacheUtil.getKey(o));
	}
	
	public void delete(Collection<Object> os) {
		for (Object o : os) {
			delete(o);
		}
	}
	
	public void delete(Object[] os) {
		for (Object o : os) {
			delete(o);
		}
	}

	public <T> T get(Class<T> c, int id) {
		String value = client.getForMap(cacheUtil.getParentKey(c), cacheUtil.getKey(c, id));
		return JSON.parseObject(value, c);
	}

	public <T> List<T> list(Class<T> c) {
		List<String> list = client.getForMap(cacheUtil.getParentKey(c));
		List<T> ts = new ArrayList<>(list.size());
		for (String s : list) {
			T t = JSON.parseObject(s, c);
			ts.add(t);
		}
		return ts;
	}

	public <T> List<T> list(Class<T> c, int first, int max) {
		List<T> resultList = null;
		int i = 0;
		for (T t : list(c)) {
			if (i >= first) {
				if (resultList == null)
					resultList = new ArrayList<>(max);
				resultList.add(t);
			}
			if (i+1 == max)
				break;
			i++;
		}
		return resultList;
	}

}

