package com.su.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.su.common.po.Player;
import com.su.core.data.DataService;
import com.su.proto.PlayerProto.PlayerDataPro;

@Service
public class PlayerService {
	@Autowired
	private DataService dataService;

	/**
	 * 创建用户
	 */
	public long createPlayer(String name) {
		Player player = new Player();
		player.setName(name);
		return dataService.save(player);
	}

	public Player getPlayerById(long id) {
		return dataService.get(Player.class, id);
	}
	
	public PlayerDataPro toPlayerDataPro(Player player) {
		PlayerDataPro.Builder builder = PlayerDataPro.newBuilder();
		builder.setId(player.getId());
		builder.setName(player.getName());
		return builder.build();
	}
}
