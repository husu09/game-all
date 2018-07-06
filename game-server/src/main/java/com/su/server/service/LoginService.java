package com.su.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.su.common.po.Player;
import com.su.core.data.RedisClient;
import com.su.server.constant.RedisKey;
import com.su.server.obj.PlayerCoreData;

@Service
public class LoginService{
	@Autowired
	private RedisClient redisClient;

	/**
	 * 添加名字缓存
	 */
	public void addIdCacheByName(String name, long id) {
		redisClient.hset(RedisKey.PLAYER_NAME_ID, name, String.valueOf(id));
	}

	/**
	 * 删除名字缓存
	 */
	public void removeIdCacheByName(String name) {
		redisClient.hdel(RedisKey.PLAYER_NAME_ID, name);
	}

	/**
	 * 通过名字获取id
	 */
	public long getIdCacheByName(String name) {
		return Long.parseLong(redisClient.hget(RedisKey.PLAYER_NAME_ID, name));
	}

	/**
	 * 添加玩家核心数据到缓存中
	 */
	public void addPlayerCache(Player player) {
		PlayerCoreData playerCoreData = new PlayerCoreData(player);
		String value = JSON.toJSONString(playerCoreData);
		redisClient.hset(RedisKey.PLAYER_CORE_DATA, String.valueOf(playerCoreData.getId()), value);
	}

	/**
	 * 从缓存中删除玩家核心数据
	 */
	public void removePlayerCache(long id) {
		redisClient.hdel(RedisKey.PLAYER_CORE_DATA, String.valueOf(id));
	}

	/**
	 * 从缓存中获取玩家核心数据
	 */
	public PlayerCoreData getPlayerCache(long id) {
		String value = redisClient.hget(RedisKey.PLAYER_CORE_DATA, String.valueOf(id));
		return JSON.parseObject(value, PlayerCoreData.class);
	}

}
