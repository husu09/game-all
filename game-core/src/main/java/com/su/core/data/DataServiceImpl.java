package com.su.core.data;

import java.util.Collection;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.su.common.rmi.DataRmiService;
import com.su.core.cache.Cache;

@Repository
public class DataServiceImpl implements DataService {
	
	@Autowired
	private DataRmiService dataRmiService;
	@Autowired
	private MQService mqService;
	@Autowired
	private RedisService redisService;
	@Autowired
	private MemoryService memoryService;
	
	
	private <T> boolean isRedisCache(T t) {
		if (t == null)
			return false;
		Cache ac = t.getClass().getAnnotation(Cache.class);
		if (ac != null) {
			return ac.redisCache();
		}
		return false;
	}
	
	private <T> boolean isRedisCache(Collection<T> ts) {
		if (ts == null || ts.size() == 0)
				return false;
		T t = ts.iterator().next();
		Cache ac = t.getClass().getAnnotation(Cache.class);
		if (ac != null) {
			return ac.redisCache();
		}
		return false;
	}
	
	private <T> boolean isRedisCache(T[] ts) {
		if (ts == null || ts.length == 0)
			return false;
		T t = ts[0];
		Cache ac = t.getClass().getAnnotation(Cache.class);
		if (ac != null) {
			return ac.redisCache();
		}
		return false;
	}
	
	private <T> boolean isMemoryCache(T t) {
		if (t == null)
			return false;
		Cache ac = t.getClass().getAnnotation(Cache.class);
		if (ac != null) {
			return ac.redisCache();
		}
		return false;
	}
	
	private <T> boolean isMemoryCache(Collection<T> ts) {
		if (ts == null || ts.size() == 0)
				return false;
		T t = ts.iterator().next();
		Cache ac = t.getClass().getAnnotation(Cache.class);
		if (ac != null) {
			return ac.redisCache();
		}
		return false;
	}
	
	private <T> boolean isMemoryCache(T[] ts) {
		if (ts == null || ts.length == 0)
			return false;
		T t = ts[0];
		Cache ac = t.getClass().getAnnotation(Cache.class);
		if (ac != null) {
			return ac.redisCache();
		}
		return false;
	}
	
	@Override
	public <T> void save(T t) {
		mqService.sendSave(t);
		if (isRedisCache(t))
			redisService.saveOrUpdate(t);
	}

	@Override
	public <T> void save(Collection<T> ts) {
		mqService.sendSave(ts);
		if (isRedisCache(ts))
			redisService.saveOrUpdate(ts);
		
	}

	@Override
	public <T> void save(T[] ts) {
		mqService.sendSave(ts);
		if (isRedisCache(ts))
			redisService.saveOrUpdate(ts);
	}

	@Override
	public <T> int saveNow(T t) {
		if (isRedisCache(t))
			redisService.saveOrUpdate(t);
		return dataRmiService.save(t);
	}

	@Override
	public <T> int[] saveNow(Collection<T> ts) {
		if (isRedisCache(ts))
			redisService.saveOrUpdate(ts);
		return dataRmiService.save(ts);
	}

	@Override
	public <T> int[] saveNow(T[] ts) {
		if (isRedisCache(ts))
			redisService.saveOrUpdate(ts);
		return dataRmiService.save(ts);
	}

	@Override
	public <T> void update(T t) {
		mqService.sendUpdate(t);
		if (isRedisCache(t))
			redisService.saveOrUpdate(t);
		
	}

	@Override
	public <T> void update(Collection<T> ts) {
		mqService.sendUpdate(ts);
		if (isRedisCache(ts))
			redisService.saveOrUpdate(ts);
	}

	@Override
	public <T> void update(T[] ts) {
		mqService.sendUpdate(ts);
		if (isRedisCache(ts))
			redisService.saveOrUpdate(ts);
	}

	@Override
	@Deprecated
	public <T> int update(T t, String sql) {
		int i = dataRmiService.update(sql);
		// 缓存失效
		if (isRedisCache(t))
			redisService.delete(t);
		return i;
	}

	@Override
	public <T> void delete(T t) {
		mqService.delete(t);
		if (isRedisCache(t))
			redisService.delete(t);
		
	}

	@Override
	public <T> void delete(Collection<T> ts) {
		mqService.delete(ts);
		if (isRedisCache(ts))
			redisService.delete(ts);
		
	}

	@Override
	public <T> void delete(T[] ts) {
		mqService.delete(ts);
		if (isRedisCache(ts))
			redisService.delete(ts);
		
	}

	@Override
	@Deprecated
	public <T> int delete(T t, String sql) {
		int i = dataRmiService.delete(sql);
		// 缓存失效
		if (isRedisCache(t))
			redisService.delete(t);
		return i;
	}

	@Override
	public <T> T get(Class<T> c, Integer id) {
		T t = null;
		boolean ismc = false;
		boolean isrc = false;
		if (ismc = isMemoryCache(c))
			t = memoryService.get(c, id);
		if (t == null && (isrc = isRedisCache(t)))
			t = redisService.get(c, id);
		if (t == null) {
			t = dataRmiService.get(c, id);
			if (ismc)
				memoryService.save(t);
			if (isrc)
				redisService.saveOrUpdate(t);
		}
		return t;
	}

	@Override
	public <T> T get(DetachedCriteria detachedCriteria) {
		T t = dataRmiService.get(detachedCriteria);
		if (isMemoryCache(t))
				memoryService.save(t);
		if (isRedisCache(t))
			redisService.saveOrUpdate(t);
		return t;
	}

	@Override
	public <T> List<T> list(Class<T> c) {
		T t = null;
		boolean ismc = false;
		boolean isrc = false;
		if (ismc = isMemoryCache(c))
			t = memoryService.get(c);
		if (t == null && (isrc = isRedisCache(t)))
			t = redisService.get(c);
		if (t == null) {
			t = dataRmiService.get(c);
			if (ismc)
				memoryService.save(t);
			if (isrc)
				redisService.saveOrUpdate(t);
		}
		return t;
	}

	@Override
	public <T> List<T> list(DetachedCriteria detachedCriteria) {
		T t = dataRmiService.get(detachedCriteria);
		if (isMemoryCache(t))
				memoryService.save(t);
		if (isRedisCache(t))
			redisService.saveOrUpdate(t);
		return t;
	}

	@Override
	public <T> List<T> list(Class<T> c, int first, int max) {
		T t = null;
		boolean ismc = false;
		boolean isrc = false;
		if (ismc = isMemoryCache(c))
			t = memoryService.get(c);
		if (t == null && (isrc = isRedisCache(t)))
			t = redisService.get(c);
		if (t == null) {
			t = dataRmiService.get(c);
			if (ismc)
				memoryService.save(t);
			if (isrc)
				redisService.saveOrUpdate(t);
		}
		return t;
	}

	@Override
	public <T> List<T> list(DetachedCriteria detachedCriteria, int first, int max) {
		T t = dataRmiService.get(detachedCriteria);
		if (isMemoryCache(t))
				memoryService.save(t);
		if (isRedisCache(t))
			redisService.saveOrUpdate(t);
		return t;
	}

	@Override
	public long get(Class<?> c, Projection projection) {
		dataRmiService.get(c, projection);
		return 0;
	}
	

}
