package com.su.core.data;

import java.util.Collection;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.su.common.rmi.DataRmiService;
import com.su.core.action.Action;
import com.su.core.cache.Cache;

@Repository
public class DataServiceImpl {

	@Autowired
	private DataRmiService dataRmiService;
	@Autowired
	private MQService mqService;
	@Autowired
	private RedisService redisService;
	@Autowired
	private MemoryService memoryService;

	private boolean isRedisCache(Object o) {
		if (o == null)
			throw new NullPointerException();
		Cache ac = o.getClass().getAnnotation(Cache.class);
		if (ac != null) {
			return ac.redisCache();
		}
		return false;
	}

	private boolean isRedisCache(Collection<Object> os) {
		if (os == null)
			throw new NullPointerException();
		if (os.size() == 0)
			return false;
		Object t = os.iterator().next();
		Cache ac = t.getClass().getAnnotation(Cache.class);
		if (ac != null) {
			return ac.redisCache();
		}
		return false;
	}

	private boolean isRedisCache(Object[] ts) {
		if (ts == null)
			throw new NullPointerException();
		if (ts.length == 0)
			return false;
		Object t = ts[0];
		Cache ac = t.getClass().getAnnotation(Cache.class);
		if (ac != null) {
			return ac.redisCache();
		}
		return false;
	}

	private boolean isMemoryCache(Object o) {
		if (o == null)
			throw new NullPointerException();
		Cache ac = o.getClass().getAnnotation(Cache.class);
		if (ac != null) {
			return ac.memoryCache();
		}
		return false;
	}

	private boolean isMemoryCache(Collection<Object> os) {
		if (os == null || os.size() == 0)
			return false;
		Object t = os.iterator().next();
		Cache ac = t.getClass().getAnnotation(Cache.class);
		if (ac != null) {
			return ac.memoryCache();
		}
		return false;
	}

	private <T> boolean isMemoryCache(Object[] os) {
		if (os == null || os.length == 0)
			return false;
		Object t = os[0];
		Cache ac = t.getClass().getAnnotation(Cache.class);
		if (ac != null) {
			return ac.memoryCache();
		}
		return false;
	}

	@Action
	public void save(Object o) {
		mqService.sendSave(o);
		if (isRedisCache(o))
			redisService.saveOrUpdate(o);
	}

	@Action
	public <T> void save(Collection<T> ts) {
		mqService.sendSave(ts);
		if (isRedisCache(ts))
			redisService.saveOrUpdate(ts);

	}

	@Action
	public <T> void save(T[] ts) {
		mqService.sendSave(ts);
		if (isRedisCache(ts))
			redisService.saveOrUpdate(ts);
	}

	@Action
	public <T> int saveNow(T t) {
		if (isRedisCache(t))
			redisService.saveOrUpdate(t);
		return dataRmiService.save(t);
	}

	@Action
	public <T> int[] saveNow(Collection<T> ts) {
		if (isRedisCache(ts))
			redisService.saveOrUpdate(ts);
		return dataRmiService.save(ts);
	}

	@Action
	public <T> int[] saveNow(T[] ts) {
		if (isRedisCache(ts))
			redisService.saveOrUpdate(ts);
		return dataRmiService.save(ts);
	}

	@Action
	public <T> void update(T t) {
		mqService.sendUpdate(t);
		if (isRedisCache(t))
			redisService.saveOrUpdate(t);

	}

	@Action
	public <T> void update(Collection<T> ts) {
		mqService.sendUpdate(ts);
		if (isRedisCache(ts))
			redisService.saveOrUpdate(ts);
	}

	@Action
	public <T> void update(T[] ts) {
		mqService.sendUpdate(ts);
		if (isRedisCache(ts))
			redisService.saveOrUpdate(ts);
	}

	@Action
	@Deprecated
	public <T> int update(T t, String sql) {
		int i = dataRmiService.update(sql);
		// 缓存失效
		if (isRedisCache(t))
			redisService.delete(t);
		return i;
	}

	@Action
	public <T> void delete(T t) {
		mqService.delete(t);
		if (isRedisCache(t))
			redisService.delete(t);

	}

	@Action
	public <T> void delete(Collection<T> ts) {
		mqService.delete(ts);
		if (isRedisCache(ts))
			redisService.delete(ts);

	}

	@Action
	public <T> void delete(T[] ts) {
		mqService.delete(ts);
		if (isRedisCache(ts))
			redisService.delete(ts);

	}

	@Action
	@Deprecated
	public <T> int delete(T t, String sql) {
		int i = dataRmiService.delete(sql);
		// 缓存失效
		if (isRedisCache(t))
			redisService.delete(t);
		return i;
	}

	@Action
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

	@Action
	public <T> T get(DetachedCriteria detachedCriteria) {
		T t = dataRmiService.get(detachedCriteria);
		if (isMemoryCache(t))
			memoryService.save(t);
		if (isRedisCache(t))
			redisService.saveOrUpdate(t);
		return t;
	}

	@Action
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

	@Action
	public <T> List<T> list(DetachedCriteria detachedCriteria) {
		T t = dataRmiService.get(detachedCriteria);
		if (isMemoryCache(t))
			memoryService.save(t);
		if (isRedisCache(t))
			redisService.saveOrUpdate(t);
		return t;
	}

	@Action
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

	@Action
	public <T> List<T> list(DetachedCriteria detachedCriteria, int first, int max) {
		T t = dataRmiService.get(detachedCriteria);
		if (isMemoryCache(t))
			memoryService.save(t);
		if (isRedisCache(t))
			redisService.saveOrUpdate(t);
		return t;
	}

	@Action
	public long get(Class<?> c, Projection projection) {
		dataRmiService.get(c, projection);
		return 0;
	}

}
