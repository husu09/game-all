package com.su.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.su.common.po.Player;
import com.su.common.po.PlayerDetail;
import com.su.core.context.GameContext;
import com.su.core.context.PlayerContext;
import com.su.core.data.DataService;
import com.su.core.event.GameEventAdapter;
import com.su.msg.PlayerMsg._Player;

@Service
public class PlayerService extends GameEventAdapter{
	@Autowired
	private DataService dataService;
	@Autowired
	private GameContext gameContext;

	/**
	 * 创建用户
	 */
	public long createPlayer(Player player) {
		return dataService.save(player);
	}
	
	/**
	 * 获取用户
	 * */
	public Player getPlayerById(long id) {
		 Player player = dataService.get(Player.class, id);
		 if (player == null)
			throw new RuntimeException("player is null " + id);
		 return player;
	}
	
	/**
	 * 获取 playerDetail 对象
	 * */
	public PlayerDetail getPlayerDetail(long id) {
		PlayerDetail playerDetail = dataService.get(PlayerDetail.class, id);
		if (playerDetail == null) {
			playerDetail = new PlayerDetail(id);
			dataService.save(playerDetail);
		}
		return playerDetail;
	}
	
	public _Player serializePlayer(Player player) {
		_Player.Builder builder = _Player.newBuilder();
		builder.setId(player.getId());
		builder.setName(player.getName());
		builder.setPeanut(player.getPeanut());
		builder.setRankingStep(player.getRankingStep());
		builder.setRankingScore(player.getRankingScore());
		return builder.build();
	}

	@Override
	public void logout(PlayerContext playerContext) {
		gameContext.getPlayerContextMap().remove(playerContext.getPlayerId());
	}
	
}
