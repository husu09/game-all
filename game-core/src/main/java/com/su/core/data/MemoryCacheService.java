package com.su.core.data;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.Id;

import org.springframework.beans.factory.annotation.Autowired;

public class MemoryCacheService {
	@Autowired
	private MemoryCache cache;
	@Autowired
	private CacheUtil cacheUtil;

	public void saveOrUpdate(Object o) {
		if (!cacheUtil.isPersistent(o)) {
			return;
		}
		if (!cache.contains(o)) {
			cache.add(o);
		}
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
		cache.remove(c);

	}

	public void delete(Object o) {
		cache.remove(o);
	}

	public void delete(Collection<Object> os) {
		for (Object o : os)
			cache.remove(o);
	}

	public void delete(Object[] os) {
		for (Object o : os)
			cache.remove(o);
	}

	public <T> T get(Class<T> c, int id) {
		return cache.get(c, id);
	}

	public <T> List<T> list(Class<T> c) {
		return cache.list(c);
	}

	public <T> List<T> list(Class<T> c, int first, int max) {
		return cache.list(c, first, max);
	}

}
