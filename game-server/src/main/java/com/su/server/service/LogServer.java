package com.su.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.su.common.po.ResouceLog;
import com.su.core.data.DataService;

@Service
public class LogServer {
	@Autowired
	private DataService dataService;

	/**
	 * @param reason	来源
	 * @param useCount	使用数量
	 * @param leftCount	剩余数量（-1 表示没统计）
	 */
	public void addResourceLog(long playerId, int reason, int useCount, int leftCount) {
		ResouceLog resouceLog = new ResouceLog();
		resouceLog.setId(playerId);
		resouceLog.setReason(reason);
		resouceLog.setUseCount(useCount);
		resouceLog.setLeftCount(leftCount);
		dataService.save(resouceLog);
	}
}
