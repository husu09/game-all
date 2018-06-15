package com.su.server.service;

import com.su.common.obj.Item;
import com.su.core.context.PlayerContext;

public class ResouceService {
	
	/**
	 * 添加资源
	 * */
	public void add(PlayerContext playerContext, Item item, int reson) {
		
	}
	
	/**
	 * 扣除资源
	 * */
	public boolean edd(PlayerContext playerContext, Item item, int reson) {
		return true;
	}
	
	public boolean edd(PlayerContext playerContext, int type, int sysId, int count , int reson) {
		return true;
	}
}
