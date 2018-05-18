package com.su.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.su.common.po.ResouceLog;
import com.su.core.data.DataService;

@Component
public class LogServer {
	@Autowired
	private DataService dataService;

	/**
	 * @param reason	来源
	 * @param useCount	使用数量
	 * @param leftCount	剩余数量（-1 表示没统计）
	 */
	public void addResourceLog(int reason, int useCount, int leftCount) {
		ResouceLog resouceLog = new ResouceLog();
		resouceLog.setReason(reason);
		resouceLog.setUseCount(useCount);
		resouceLog.setLeftCount(leftCount);
		dataService.save(resouceLog);
	}
}
