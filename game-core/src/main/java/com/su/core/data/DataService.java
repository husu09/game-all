package com.su.core.data;

import java.util.Collection;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.su.common.rmi.DataRmiService;
import com.su.core.action.Action;

@Repository
public class DataService {

	@Autowired
	private DataRmiService dataRmiService;
	@Autowired
	private MQService mqService;
	@Autowired
	private RedisCacheService redisService;
	@Autowired
	private MemoryCacheService memoryService;	
	
	private boolean isRedisCache(Class<?> c) {
		Cache ac = c.getAnnotation(Cache.class);
		if (ac != null) {
			return ac.redisCache();
		}
		return false;
	}
	
	private boolean isRedisCache(Object o) {
		Cache ac = o.getClass().getAnnotation(Cache.class);
		if (ac != null) {
			return ac.redisCache();
		}
		return false;
	}

	private boolean isRedisCache(Collection<Object> os) {
		if (os.size() == 0)
			return false;
		Object t = os.iterator().next();
		Cache ac = t.getClass().getAnnotation(Cache.class);
		if (ac != null) {
			return ac.redisCache();
		}
		return false;
	}

	private boolean isRedisCache(Object[] os) {
		if (os.length == 0)
			return false;
		Object t = os[0];
		Cache ac = t.getClass().getAnnotation(Cache.class);
		if (ac != null) {
			return ac.redisCache();
		}
		return false;
	}
	
	private boolean isMemoryCache(Class<?> c) {
		Cache ac = c.getAnnotation(Cache.class);
		if (ac != null) {
			return ac.memoryCache();
		}
		return false;
	}
	
	private boolean isMemoryCache(Object o) {
		Cache ac = o.getClass().getAnnotation(Cache.class);
		if (ac != null) {
			return ac.memoryCache();
		}
		return false;
	}

	private boolean isMemoryCache(Collection<Object> os) {
		if (os.size() == 0)
			return false;
		Object t = os.iterator().next();
		Cache ac = t.getClass().getAnnotation(Cache.class);
		if (ac != null) {
			return ac.memoryCache();
		}
		return false;
	}

	private <T> boolean isMemoryCache(Object[] os) {
		if (os.length == 0)
			return false;
		Object t = os[0];
		Cache ac = t.getClass().getAnnotation(Cache.class);
		if (ac != null) {
			return ac.memoryCache();
		}
		return false;
	}

	public void save(Object o) {
		if (isMemoryCache(o))
			memoryService.saveOrUpdate(o);
		if (isRedisCache(o))
			redisService.saveOrUpdate(o);
		mqService.sendSave(o);
	}

	public void save(Collection<Object> os) {
		if (isMemoryCache(os))
			memoryService.saveOrUpdate(os);
		if (isRedisCache(os))
			redisService.saveOrUpdate(os);
		mqService.sendSave(os);
	}

	public void save(Object[] os) {
		if (isMemoryCache(os))
			memoryService.saveOrUpdate(os);
		if (isRedisCache(os))
			redisService.saveOrUpdate(os);
		mqService.sendSave(os);
	}

	public int saveNow(Object o) {
		int i = dataRmiService.save(o);
		if (isMemoryCache(o))
			memoryService.saveOrUpdate(o);
		if (isRedisCache(o))
			redisService.saveOrUpdate(o);
		return i;
	}

	public int[] saveNow(Collection<Object> os) {
		int[] arr = dataRmiService.save(os);
		if (isMemoryCache(os))
			memoryService.saveOrUpdate(os);
		if (isRedisCache(os))
			redisService.saveOrUpdate(os);
		return arr;
	}

	public int[] saveNow(Object[] os) {
		int[] arr = dataRmiService.save(os);
		if (isMemoryCache(os))
			memoryService.saveOrUpdate(os);
		if (isRedisCache(os))
			redisService.saveOrUpdate(os);
		return arr;
	}

	public void update(Object o) {
		if (isMemoryCache(o))
			memoryService.saveOrUpdate(o);
		if (isRedisCache(o))
			redisService.saveOrUpdate(o);
		mqService.sendUpdate(o);

	}

	public void update(Collection<Object> os) {
		if (isMemoryCache(os))
			memoryService.saveOrUpdate(os);
		if (isRedisCache(os))
			redisService.saveOrUpdate(os);
		mqService.sendUpdate(os);
	}

	public void update(Object[] os) {
		if (isMemoryCache(os))
			memoryService.saveOrUpdate(os);
		if (isRedisCache(os))
			redisService.saveOrUpdate(os);
		mqService.sendUpdate(os);
	}

	@Deprecated
	public int update(Class<?> c, String sql) {
		int i = dataRmiService.update(sql);
		// 缓存失效
		if (isMemoryCache(c))
			memoryService.delete(c);
		if (isRedisCache(c))
			redisService.delete(c);
		return i;
	}

	public void delete(Object o) {
		if (isMemoryCache(o))
			memoryService.delete(o);
		if (isRedisCache(o))
			redisService.delete(o);
		mqService.delete(o);
	}

	public void delete(Collection<Object> os) {
		if (isMemoryCache(os))
			memoryService.delete(os);
		if (isRedisCache(os))
			redisService.delete(os);
		mqService.delete(os);
	}

	public void delete(Object[] os) {
		if (isMemoryCache(os))
			memoryService.delete(os);
		if (isRedisCache(os))
			redisService.delete(os);
		mqService.delete(os);
	}

	@Deprecated
	public int delete(Class<?> c, String sql) {
		int i = dataRmiService.delete(sql);
		// 缓存失效
		if (isMemoryCache(c))
			memoryService.delete(c);
		if (isRedisCache(c))
			redisService.delete(c);
		return i;
	}

	public <T> T get(Class<T> c, int id) {
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
				memoryService.saveOrUpdate(t);
			if (isrc)
				redisService.saveOrUpdate(t);
		}
		return t;
	}

	public <T> T get(DetachedCriteria detachedCriteria) {
		T t = dataRmiService.get(detachedCriteria);
		if (isMemoryCache(t))
			memoryService.saveOrUpdate(t);
		if (isRedisCache(t))
			redisService.saveOrUpdate(t);
		return t;
	}

	public <T> List<T> listByCache(Class<T> c) {
		List<T> ts = null;
		boolean ismc = false;
		boolean isrc = false;
		if (ismc = isMemoryCache(c))
			ts = memoryService.list(c);
		if (ts == null && (isrc = isRedisCache(ts)))
			ts = redisService.list(c);
		if (ts == null) {
			ts = dataRmiService.list(c);
			if (ismc)
				memoryService.saveOrUpdate(ts);
			if (isrc)
				redisService.saveOrUpdate(ts);
		}
		return ts;
	}
	
	public <T> List<T> list(Class<T> c) {
		List<T> ts = dataRmiService.list(c);
		if (isMemoryCache(ts))
			memoryService.saveOrUpdate(ts);
		if (isRedisCache(ts))
			redisService.saveOrUpdate(ts);
		return ts;
	}

	public <T> List<T> list(DetachedCriteria detachedCriteria) {
		List<T> ts = dataRmiService.list(detachedCriteria);
		if (isMemoryCache(ts))
			memoryService.saveOrUpdate(ts);
		if (isRedisCache(ts))
			redisService.saveOrUpdate(ts);
		return ts;
	}

	public <T> List<T> listByCache(Class<T> c, int first, int max) {
		List<T> ts = null;
		boolean ismc = false;
		boolean isrc = false;
		if (ismc = isMemoryCache(c))
			ts = memoryService.list(c, first, max);
		if (ts == null && (isrc = isRedisCache(ts)))
			ts = redisService.list(c, first, max);
		if (ts == null) {
			ts = dataRmiService.list(c, first, max);
			if (ismc)
				memoryService.saveOrUpdate(ts);
			if (isrc)
				redisService.saveOrUpdate(ts);
		}
		return ts;
	}
	
	public <T> List<T> list(Class<T> c, int first, int max) {
		List<T> ts = dataRmiService.list(c, first, max);
		if (isMemoryCache(ts))
			memoryService.saveOrUpdate(ts);
		if (isRedisCache(ts))
			redisService.saveOrUpdate(ts);
		return ts;
	}

	public <T> List<T> list(DetachedCriteria detachedCriteria, int first, int max) {
		List<T> ts = dataRmiService.get(detachedCriteria);
		if (isMemoryCache(ts))
			memoryService.saveOrUpdate(ts);
		if (isRedisCache(ts))
			redisService.saveOrUpdate(ts);
		return ts;
	}

	public long get(Class<?> c, Projection projection) {
		long l = dataRmiService.get(c, projection);
		return l;
	}

}
