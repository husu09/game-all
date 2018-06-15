package com.su.core.akka;

import java.util.Map;

import com.su.core.context.PlayerContext;
import com.su.core.gambling.Site;
import com.su.core.gambling.TableResult;
import com.su.msg.GamblingMsg._GamePlayerResult;

/**
 * GameServer向外提供服务的接口
 * */
public interface BridgeService {
	/**
	 * 牌局结算
	 * */
	_GamePlayerResult doTableResult(PlayerContext playerContext, TableResult tableResult);
	
	/**
	 * 获取所有场
	 * */
	Map<Integer, Site> getSiteMap();
}
