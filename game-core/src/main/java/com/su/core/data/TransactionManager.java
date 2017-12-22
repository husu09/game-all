package com.su.core.data;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.su.common.mq.MQMessage;

/**
 * 处理事务
 */
@Component
public class TransactionManager {
	
	/**
	 * 延时数据
	 * */
	private ThreadLocal<List<MQMessage>> lazyData = new ThreadLocal<>();
	/**
	 * 即时添加操作
	 * */
	private ThreadLocal<List<Integer>> addData = new ThreadLocal<>();
	/**
	 * 通过 sql 的即时修改和删除操作，不可还原
	 * */
	private ThreadLocal<List<String>> addOrUpdateSql = new ThreadLocal<>();
	/**
	 * 查询出的数据
	 * */
	private ThreadLocal<Map<Integer, Object>> queryData = new ThreadLocal<>();
	
	/**
	 * 提交
	 * */
	public void commit() {
		
	}
	
	/**
	 * 回滚
	 * */
	public void rollblack() {
		
	}

}
