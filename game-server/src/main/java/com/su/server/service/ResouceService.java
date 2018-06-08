package com.su.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.su.common.constant.SysAttr;
import com.su.common.obj.Item;
import com.su.core.context.PlayerContext;

@Component
public class ResouceService {
	
	@Autowired
	private BagService bagService;
	@Autowired
	private LogService logService; 
	
	/**
	 * 添加资源
	 * */
	public void add(PlayerContext playerContext, Item item, int reson) {
		if (item.getType() == SysAttr.PEANUT) {
			
		} else if (item.getType() == SysAttr.ITEM) {
			
		}
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
	
	/**
	 * 添加花生
	 * */
	private void addPeanut(PlayerContext playerContext, int addCount) {
		if (addCount <= 0)
			return;
	}
	
	/**
	 * 扣除花生
	 * */
	private boolean eddPeanut(PlayerContext playerContext, int eddCount) {
		if (eddCount <= 0)
			return true;
		return true;
	}
}
