package com.su.core.akka;

import java.util.Map;

import com.su.core.context.PlayerContext;
import com.su.core.game.Site;
import com.su.core.game.TableResult;
import com.su.msg.GamblingMsg._GamePlayerResult;

/**
 * GameServer向外提供服务的接口
 * */
public interface BridgeService {
	/**
	 * 牌局结算
	 * */
	public _GamePlayerResult doTableResult(PlayerContext playerContext, TableResult tableResult);
	
	/**
	 * 获取所有场
	 * */
	public Map<Integer, Site> getSiteMap();
	
	/**
	 * 获取玩家对象
	 * */
}
