package com.su.core.data;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.su.core.config.AppConfig;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisClient {
	
	
	private String host;
	private int port;
	private String password;
	
	private JedisPoolConfig config;
	private JedisPool pool;

	@PostConstruct
	private void init() {
		config = new JedisPoolConfig();
		pool = new JedisPool(config, host, port, 60000, password);
	}

	public Jedis getJedis() {
		return pool.getResource();
	}
	
	public String get(String key) {
		Jedis jedis = getJedis();
		return jedis.get(key);
	}
	
	/**
	 * 删除key
	 * */
	public void delete(String key) {
		Jedis jedis = getJedis();
		jedis.del(key);
	}
	
	public void set(String key, String value) {
		Jedis jedis = getJedis();
		jedis.set(key, value);
	}
	
	public void set(String key, String value, int seconds) {
		Jedis jedis = getJedis();
		jedis.setex(key, seconds, value);
	}
	
	
	
	/**
	 * 获取hash表中的值
	 * */
	public String getForMap(String key, String field) {
		Jedis jedis = getJedis();
		return jedis.hget(key, field);
	}
	/**
	 * 获取hash表中所有值
	 * */
	public List<String> getForMap(String key) {
		Jedis jedis = getJedis();
		return jedis.hvals(key);
	}
	/**
	 * 设置hash表中的值
	 * */
	public void setForMap(String key, String field, String value) {
		Jedis jedis = getJedis();
		jedis.hset(key, field, value);
	}
	
	/**
	 * 删除hash表中的值
	 * */
	public void deleteForMap(String key, String field) {
		Jedis jedis = getJedis();
		jedis.hdel(key, field);
	}
	
	
}
