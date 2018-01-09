package com.su.core.data;

import java.util.Collection;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.su.common.mq.DataOperator;

/**
 * 处理事务
 */
@Component
public class TransactionManager {

	private ThreadLocal<TransactionData> threadLocal = new ThreadLocal<>();

	@Autowired
	private MQService mqService;
	@Autowired
	private MemoryCacheService memoryService;
	@Autowired
	private RedisCacheService redisService;

	private TransactionData getTransactionData() {
		TransactionData transactionData = threadLocal.get();
		if (transactionData == null) {
			transactionData = new TransactionData();
			threadLocal.set(transactionData);
		}
		return transactionData;

	}

	public void addDataOperator(DataOperator dataOperator, Collection<Object> os) {
		if (os == null)
			return;
		TransactionData transactionData = getTransactionData();
		for (Object o : os)
			transactionData.getLazyDataOperator().put(dataOperator, o);
	}

	public void addDataOperator(DataOperator dataOperator, Object o) {
		if (o == null)
			return;
		TransactionData transactionData = getTransactionData();
		transactionData.getLazyDataOperator().put(dataOperator, o);
	}

	public void addCache(Collection<Object> os) {
		if (os == null)
			return;
		TransactionData transactionData = getTransactionData();
		for (Object o : os)
			transactionData.getCacheData().add(o);
	}

	public void addCache(Object o) {
		if (o == null)
			return;
		TransactionData transactionData = getTransactionData();
		transactionData.getCacheData().add(o);
	}

	/**
	 * 提交
	 */
	public void commit() {
		// 延时操作
		TransactionData transactionData = getTransactionData();
		for (Entry<DataOperator, Object> e : transactionData.getLazyDataOperator().entrySet()) {
			mqService.common(e.getKey(), e.getValue());
		}
		transactionData.getLazyDataOperator().clear();
		// 缓存
		transactionData.getCacheData().clear();

	}

	/**
	 * 回滚
	 */
	public void rollblack() {
		// 延时操作
		TransactionData transactionData = getTransactionData();
		transactionData.getLazyDataOperator().clear();
		// 缓存
		for (Object o : transactionData.getCacheData()) {
			if (CacheUtil.isMemoryCache(o))
				memoryService.delete(o);
			if (CacheUtil.isRedisCache(o))
				redisService.delete(o);
		}
		transactionData.getCacheData().clear();
	}

}
