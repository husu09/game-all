package com.su.server.service;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.su.common.po.Player;
import com.su.core.data.DataService;

@Service
public class AccountService {
	@Autowired
	private DataService dataService;
	
	/**
	 * 根据用户名查找用户
	 * */
	public Player queryPlayerByName(String name) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Player.class);
		detachedCriteria.add(Restrictions.eq("name", name));
		return dataService.get(detachedCriteria );
	}
	
	/**
	 * 创建用户
	 * */
	public Player createPlayer(String name) {
		Player player = new Player();
		player.setName(name);
		long id = dataService.save(player);
		player.setId(id);
		return player;
	}
}
