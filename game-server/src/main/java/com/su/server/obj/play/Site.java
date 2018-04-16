package com.su.server.obj.play;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import com.su.common.util.SpringUtil;
import com.su.core.akka.AkkaContext;
import com.su.excel.co.SiteCo;
import com.su.server.context.PlayerContext;

/**
 * 场所
 */
public class Site {

	/**
	 * 玩家数
	 */
	private AtomicInteger playerNum = new AtomicInteger();
	/**
	 * 牌桌队列
	 */
	private ConcurrentLinkedQueue<Table> tableQueue = new ConcurrentLinkedQueue<>();
	/**
	 * 玩家队列
	 */
	private ConcurrentLinkedDeque<GamePlayer> playerDeque = new ConcurrentLinkedDeque<>();
	/**
	 * 配置
	 */
	private SiteCo siteCo;

	private AkkaContext akkaContext = SpringUtil.getContext().getBean(AkkaContext.class);

	public Site(SiteCo siteCo) {
		this.siteCo = siteCo;
		init();
	}

	/**
	 * 处理玩家加入
	 */
	public void startMatch(PlayerContext playerContext) {
		playerNum.incrementAndGet();
		playerDeque.offerLast(new GamePlayer(playerContext));
		// 尝试从队列中获取4个玩家
		GamePlayer[] gamePlayers = new GamePlayer[4];
		for (int i = 0; i < 4; i++) {
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
		Table table = tableQueue.poll();
		if (table == null) {
			TableActor tableActor = akkaContext.createActor(TableActor.class, TableActorImpl.class);
			table = new Table(tableActor);
			tableActor.init(table);
		}
		table.getActor().start(gamePlayers);

	}

	/**
	 * 初始化
	 */
	public void init() {
		for (int i = 0; i < siteCo.getInitTableNum(); i++) {
			TableActor tableActor = akkaContext.createActor(TableActor.class, TableActorImpl.class);
			Table table = new Table(tableActor);
			tableQueue.offer(table);
		}
	}

}
