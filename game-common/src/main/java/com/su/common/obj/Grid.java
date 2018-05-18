package com.su.common.obj;

/**
 * 物品格子
 * */
public class Grid {
	/**
	 * 类型
	 * */
	private int type;
	/**
	 * 物品id
	 * */
	private int sysId;
	/**
	 * 数量
	 * */
	private int count;
	/**
	 * 有效时间
	 * */
	private long endTime;
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getSysId() {
		return sysId;
	}
	public void setSysId(int sysId) {
		this.sysId = sysId;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}	
}
