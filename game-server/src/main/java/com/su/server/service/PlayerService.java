package com.su.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.su.common.po.Player;
import com.su.common.po.PlayerDetail;
import com.su.core.data.DataService;
import com.su.msg.PlayerMsg._Player;

@Service
public class PlayerService {
	@Autowired
	private DataService dataService;

	/**
	 * 创建用户
	 */
	public long createPlayer(Player player) {
		return dataService.save(player);
	}

	public Player getPlayerById(long id) {
		return dataService.get(Player.class, id);
	}
	
	/**
	 * 获取 playerDetail 对象
	 * */
	public PlayerDetail getPlayerDetail(long id) {
		PlayerDetail playerDetail = dataService.get(PlayerDetail.class, id);
		if (playerDetail == null) {
			playerDetail = new PlayerDetail();
			playerDetail.setId(id);
			dataService.save(playerDetail);
		}
		return playerDetail;
	}

	public _Player serializePlayer(Player player) {
		_Player.Builder builder = _Player.newBuilder();
		builder.setId(player.getId());
		builder.setName(player.getName());
		return builder.build();
	}
}
