package com.su.server.obj.play;

import java.util.concurrent.ConcurrentLinkedDeque;

import com.su.server.context.PlayerContext;

/**
 * 匹配器
 * */
public class Matcher {
	volatile
	/**
	 * 玩家队列
	 * */
	private  ConcurrentLinkedDeque<GamePlayer> playerDeque = new ConcurrentLinkedDeque<>();
	/**
	 * 场所
	 * */
	private Site site;


	
	public Matcher(Site site) {
		this.site = site;
	}
	
	/**
	 * 处理玩家加入
	 * */
	public void doPlayerJoin(PlayerContext playerContext) {
		playerDeque.offerLast(new GamePlayer());
		// 尝试从队列中获取4个玩家
		GamePlayer[] gamePlayers = new GamePlayer[4];
		for (int i = 0 ; i < 4; i++) {
			gamePlayers[i] = playerDeque.poll();
			if (gamePlayers[i] == null) {
				// 不足4人时重新排队
				for (GamePlayer gamePlayer : gamePlayers) {
					if (gamePlayer == null)
						break;
					playerDeque.offerFirst(gamePlayer);
				}
				return;
			}
		}
		// 人数足够时开始游戏
		Table table = site.getTableQueue().poll();
		if (table == null) {
			table = new Table();
		}
		table.start();
		
	}
	
	
	
		
}
