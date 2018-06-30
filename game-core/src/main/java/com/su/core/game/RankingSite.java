package com.su.core.game;

import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

import com.su.common.constant.GameConst;
import com.su.common.po.Player;
import com.su.common.util.ArrayUtil;
import com.su.common.util.SpringUtil;
import com.su.config.SiteCo;
import com.su.core.akka.BridgeService;
import com.su.core.context.PlayerContext;
import com.su.core.game.enums.PlayerState;

public class RankingSite extends MatchSite {


	/**
	 * 匹配中的玩家队列，段位小的排前面
	 */
	private TreeSet<GamePlayer> playerSet = new TreeSet<>(new Comparator<GamePlayer>() {

		@Override
		public int compare(GamePlayer playerOne, GamePlayer playerTwo) {
			
			Player one = bridgeService.getPlayerById(playerOne.getId());
			Player two = bridgeService.getPlayerById(playerTwo.getId());
			return two.getRankStep() - one.getRankStep();
		}

	});

	private SiteCo siteCo;

	public RankingSite(SiteCo siteCo) {
		this.siteCo = siteCo;
		// 初始化牌桌
		for (int i = 0; i < siteCo.getInitTableNum(); i++) {
			Table table = new RankingTable(this);
			getIdleTableQueue().offer(table);
		}
	}
	
	@Override
	public synchronized void addPlayerToMatch(PlayerContext playerContext, boolean isFirst) {
		// 验证是否可以加入匹配队列
		if (playerContext.getGamePlayer() == null)
			new GamePlayer(playerContext);
		else if (playerSet.contains(playerContext.getGamePlayer()))
			return;
		else if (playerContext.getGamePlayer().getState() != null)
			return;
		playerSet.add(playerContext.getGamePlayer());
		getPlayerNum().incrementAndGet();

		GamePlayer[] players = new GamePlayer[GameConst.PLAYER_COUNT];
		for (Iterator<GamePlayer> it = playerSet.iterator(); it.hasNext();) {
			GamePlayer player = it.next();
			GamePlayer last = ArrayUtil.getLast(players);
			if (last != null && bridgeService.getPlayerById(last.getId()).getRankStep()
					/ 10 != bridgeService.getPlayerById(player.getId()).getRankStep() / 10)
				break;
		}
		if (ArrayUtil.getCount(players) >= GameConst.PLAYER_COUNT) {
			for (int i = 0; i < players.length; i++)
				playerSet.remove(players[i]);
			return;
		}
		// 人数足够时开始游戏
		Table table = getIdleTableQueue().poll();
		if (table == null) {
			table = new RankingTable(this);
		}
		// 设置玩家状态
		for (GamePlayer otherPlayers : players)
			otherPlayers.setState(PlayerState.WAIT);
		table.getActor().setPlayers(players);
		table.getActor().deal();
	}

	@Override
	public synchronized void removePlayerFromMatch(GamePlayer gamePlayer) {
		if (playerSet.remove(gamePlayer))
			getPlayerNum().decrementAndGet();
	}

	/**
	 * 获取配置对象
	 */
	@Override
	public SiteCo getSiteCo() {
		return siteCo;
	}

}
