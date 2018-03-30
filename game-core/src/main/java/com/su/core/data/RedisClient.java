package com.su.core.data;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * redis 客户端
 * */
@Component
public class RedisClient {
	
	@Value("${redis.host}")
	private String host;
	@Value("${redis.port}")
	private int port;
	@Value("${redis.password}")
	private String password;
	
	private JedisPool pool;
	
	private static Logger logger = LoggerFactory.getLogger(RedisClient.class);  
	

	public void init() {
		JedisPoolConfig config = new JedisPoolConfig();
		pool = new JedisPool(config, host, port, 60000, password);
	}
	
	public void destroy() {
		pool.close();
	}
	
	/**
	 * 获取资源
	 * */
	public Jedis getResource() {
		return pool.getResource();
	}
	
	/**
	 * 释放资源
	 * */
	public static void returnResource(Jedis jedis) {
		if (jedis != null) jedis.close();
	}
	
	/**
	 * 获取值
	 * */
	public String get(String key) {
		Jedis jedis = null;
		String value = null;
		try {
			jedis = getResource();
			value = jedis.get(key);
		} catch (Exception e) {
			logger.warn("get {} = {}", key, value, e);
		} finally {
			returnResource(jedis);
		}
		return value;
	}
	
	/**
	 * 删除key
	 * */
	public long delete(String key) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			result = jedis.del(key);
		} catch (Exception e) {
			logger.warn("del {}", key, e);
		} finally {
			returnResource(jedis);
		}
		return result;
	}
	
	public String set(String key, String value) {
		String result = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			result = jedis.set(key, value);
		} catch (Exception e) {
			logger.warn("set {} = {}", key, value, e);
		} finally {
			returnResource(jedis);
		}
		return result;
	}
	
	public String set(String key, String value, int seconds) {
		String result = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			result = jedis.setex(key,seconds, value);
		} catch (Exception e) {
			logger.warn("set {} = {}", key, value, e);
		} finally {
			returnResource(jedis);
		}
		return result;
	}
	
	/**
	 * 获取hash表中的值
	 * */
	public String getMap(String key, String field) {
		String value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			value = jedis.hget(key, field);
		} catch (Exception e) {
			logger.warn("get {} = {}", key, value, e);
		} finally {
			returnResource(jedis);
		}
		return value;
	}
	
	/**
	 * 获取hash表中所有值
	 * */
	public List<String> getMap(String key) {
		List<String> value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			value = jedis.hvals(key);
		} catch (Exception e) {
			logger.warn("getList {} = {}", key, value, e);
		} finally {
			returnResource(jedis);
		}
		return value;
	}
	/**
	 * 设置hash表中的值
	 * */
	public long setMap(String key, String field, String value) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			result = jedis.hset(key, field, value);
		} catch (Exception e) {
			logger.warn("setMap {} = {}", key, value, e);
		} finally {
			returnResource(jedis);
		}
		return result;
	}
	
	/**
	 * 删除hash表中的值
	 * */
	public long deleteMap(String key, String field) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			result = jedis.hdel(key, field);
		} catch (Exception e) {
			logger.warn("del {}", key, e);
		} finally {
			returnResource(jedis);
		}
		return result;
	}
	
	
}
