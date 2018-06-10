package com.su.server.service;

import java.util.List;

import com.su.common.obj.Grid;
import com.su.common.obj.Item;
import com.su.server.context.PlayerContext;

public class BagService {
	
	/**
	 * 添加物品
	 * */
	public boolean addItem(PlayerContext playerContext,Item item, int reason) {
		List<Grid> bagGrid = playerContext.getPlayerDetail().getBagGrid();
		
		return false;
	}
}
