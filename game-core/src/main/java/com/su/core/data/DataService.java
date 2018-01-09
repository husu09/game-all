package com.su.core.data;

import java.util.Collection;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.su.common.mq.DataOperator;
import com.su.common.rmi.DataRmiService;

@Repository
public class DataService {
	@Autowired
	private DataRmiService dataRmiService;
	@Autowired
	private RedisCacheService redisService;
	@Autowired
	private MemoryCacheService memoryService;	
	@Autowired
	private IDGenerator idGenerator;
	@Autowired
	private TransactionManager transactionManager;

	public long save(Object o) {
		long id = idGenerator.next(o);
		if (CacheUtil.isMemoryCache(o))
			memoryService.saveOrUpdate(o);
		if (CacheUtil.isRedisCache(o))
			redisService.saveOrUpdate(o);
		transactionManager.addCache(o);
		transactionManager.addDataOperator(DataOperator.SAVE, o);
		return id;
	}

	public long[] save(Collection<Object> os) {
		long[] arr = new long[os.size()];
		int i = 0;
		for (Object o : os) {
			long id = idGenerator.next(o);
			arr[i++] = id;
		}
		if (CacheUtil.isMemoryCache(os))
			memoryService.saveOrUpdate(os);
		if (CacheUtil.isRedisCache(os))
			redisService.saveOrUpdate(os);
		transactionManager.addCache(os);
		transactionManager.addDataOperator(DataOperator.SAVE, os);
		return arr;
	}

	public long[] save(Object[] os) {
		long[] arr = new long[os.length];
		int i = 0;
		for (Object o : os) {
			long id = idGenerator.next(o);
			arr[i++] = id;
		}
		if (CacheUtil.isMemoryCache(os))
			memoryService.saveOrUpdate(os);
		if (CacheUtil.isRedisCache(os))
			redisService.saveOrUpdate(os);
		transactionManager.addCache(os);
		transactionManager.addDataOperator(DataOperator.SAVE, os);
		return arr;
	}

	public void update(Object o) {
		if (CacheUtil.isMemoryCache(o))
			memoryService.saveOrUpdate(o);
		if (CacheUtil.isRedisCache(o))
			redisService.saveOrUpdate(o);
		transactionManager.addCache(o);
		transactionManager.addDataOperator(DataOperator.UPDATE, o);
	}

	public void update(Collection<Object> os) {
		if (CacheUtil.isMemoryCache(os))
			memoryService.saveOrUpdate(os);
		if (CacheUtil.isRedisCache(os))
			redisService.saveOrUpdate(os);
		transactionManager.addCache(os);
		transactionManager.addDataOperator(DataOperator.UPDATE, os);
	}

	public void update(Object[] os) {
		if (CacheUtil.isMemoryCache(os))
			memoryService.saveOrUpdate(os);
		if (CacheUtil.isRedisCache(os))
			redisService.saveOrUpdate(os);
		transactionManager.addCache(os);
		transactionManager.addDataOperator(DataOperator.UPDATE, os);
	}

	@Deprecated
	public int update(Class<?> c, String sql) {
		int i = dataRmiService.update(sql);
		// 缓存失效
		if (CacheUtil.isMemoryCache(c))
			memoryService.delete(c);
		if (CacheUtil.isRedisCache(c))
			redisService.delete(c);
		return i;
	}

	public void delete(Object o) {
		if (CacheUtil.isMemoryCache(o))
			memoryService.delete(o);
		if (CacheUtil.isRedisCache(o))
			redisService.delete(o);
		transactionManager.addCache(o);
		transactionManager.addDataOperator(DataOperator.DELETE, o);
	}

	public void delete(Collection<Object> os) {
		if (CacheUtil.isMemoryCache(os))
			memoryService.delete(os);
		if (CacheUtil.isRedisCache(os))
			redisService.delete(os);
		transactionManager.addCache(os);
		transactionManager.addDataOperator(DataOperator.DELETE, os);
	}

	public void delete(Object[] os) {
		if (CacheUtil.isMemoryCache(os))
			memoryService.delete(os);
		if (CacheUtil.isRedisCache(os))
			redisService.delete(os);
		transactionManager.addCache(os);
		transactionManager.addDataOperator(DataOperator.DELETE, os);
	}

	@Deprecated
	public int delete(Class<?> c, String sql) {
		int i = dataRmiService.delete(sql);
		// 缓存失效
		if (CacheUtil.isMemoryCache(c))
			memoryService.delete(c);
		if (CacheUtil.isRedisCache(c))
			redisService.delete(c);
		return i;
	}

	public <T> T get(Class<T> c, int id) {
		T t = null;
		boolean ismc = false;
		boolean isrc = false;
		if (ismc = CacheUtil.isMemoryCache(c))
			t = memoryService.get(c, id);
		if (t == null && (isrc = CacheUtil.isRedisCache(t)))
			t = redisService.get(c, id);
		if (t == null) {
			t = dataRmiService.get(c, id);
			if (ismc)
				memoryService.saveOrUpdate(t);
			if (isrc)
				redisService.saveOrUpdate(t);
		}
		transactionManager.addCache(t);
		return t;
	}

	public <T> T get(DetachedCriteria detachedCriteria) {
		T t = dataRmiService.get(detachedCriteria);
		if (CacheUtil.isMemoryCache(t))
			memoryService.saveOrUpdate(t);
		if (CacheUtil.isRedisCache(t))
			redisService.saveOrUpdate(t);
		transactionManager.addCache(t);
		return t;
	}

	public <T> List<T> listByCache(Class<T> c) {
		List<T> ts = null;
		boolean ismc = false;
		boolean isrc = false;
		if (ismc = CacheUtil.isMemoryCache(c))
			ts = memoryService.list(c);
		if (ts == null && (isrc = CacheUtil.isRedisCache(ts)))
			ts = redisService.list(c);
		if (ts == null) {
			ts = dataRmiService.list(c);
			if (ismc)
				memoryService.saveOrUpdate(ts);
			if (isrc)
				redisService.saveOrUpdate(ts);
		}
		transactionManager.addCache(ts);
		return ts;
	}
	
	public <T> List<T> list(Class<T> c) {
		List<T> ts = dataRmiService.list(c);
		if (CacheUtil.isMemoryCache(ts))
			memoryService.saveOrUpdate(ts);
		if (CacheUtil.isRedisCache(ts))
			redisService.saveOrUpdate(ts);
		transactionManager.addCache(ts);
		return ts;
	}

	public <T> List<T> list(DetachedCriteria detachedCriteria) {
		List<T> ts = dataRmiService.list(detachedCriteria);
		if (CacheUtil.isMemoryCache(ts))
			memoryService.saveOrUpdate(ts);
		if (CacheUtil.isRedisCache(ts))
			redisService.saveOrUpdate(ts);
		transactionManager.addCache(ts);
		return ts;
	}

	public <T> List<T> listByCache(Class<T> c, int first, int max) {
		List<T> ts = null;
		boolean ismc = false;
		boolean isrc = false;
		if (ismc = CacheUtil.isMemoryCache(c))
			ts = memoryService.list(c, first, max);
		if (ts == null && (isrc = CacheUtil.isRedisCache(ts)))
			ts = redisService.list(c, first, max);
		if (ts == null) {
			ts = dataRmiService.list(c, first, max);
			if (ismc)
				memoryService.saveOrUpdate(ts);
			if (isrc)
				redisService.saveOrUpdate(ts);
		}
		transactionManager.addCache(ts);
		return ts;
	}
	
	public <T> List<T> list(Class<T> c, int first, int max) {
		List<T> ts = dataRmiService.list(c, first, max);
		if (CacheUtil.isMemoryCache(ts))
			memoryService.saveOrUpdate(ts);
		if (CacheUtil.isRedisCache(ts))
			redisService.saveOrUpdate(ts);
		transactionManager.addCache(ts);
		return ts;
	}

	public <T> List<T> list(DetachedCriteria detachedCriteria, int first, int max) {
		List<T> ts = dataRmiService.get(detachedCriteria);
		if (CacheUtil.isMemoryCache(ts))
			memoryService.saveOrUpdate(ts);
		if (CacheUtil.isRedisCache(ts))
			redisService.saveOrUpdate(ts);
		transactionManager.addCache(ts);
		return ts;
	}

	public long get(Class<?> c, Projection projection) {
		long l = dataRmiService.get(c, projection);
		return l;
	}

}
