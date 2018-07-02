package com.su.core.akka;

import java.util.Map;

import com.su.common.po.Player;
import com.su.core.game.Site;
import com.su.core.game.TableResult;
import com.su.msg.TableMsg._GamePlayerResult;

/**
 * GameServer向外提供服务的接口
 * */
public interface BridgeService {
	/**
	 * 获取所有场
	 * */
	public Map<Integer, Site> getSiteMap();
	/**
	 * 处理牌局结束
	 * */
	public _GamePlayerResult doTableResult(TableResult tableResult);
	/**
	 * 处理比赛结束
	 * */
	public void doContestClose(int ranking);
	
	public Player getPlayerById(long id);
	
}
