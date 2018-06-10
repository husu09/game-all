package com.su.excel.co;

import com.su.common.obj.Item;

public class BagCo {
	private int id;
	/**
	 * 类型
	 * */
	private int type;
	/**
	 * 品质
	 * */
	private int quality;
	/**
	 * 使用类型
	 * */
	private int useType;
	/**
	 * 使用效果
	 * */
	private Item item;
	/**
	 * 有效期/毫秒
	 * */
	private int effTime;
	/**
	 * 叠加上限
	 * */
	private int limit;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getQuality() {
		return quality;
	}
	public void setQuality(int quality) {
		this.quality = quality;
	}
	public int getUseType() {
		return useType;
	}
	public void setUseType(int useType) {
		this.useType = useType;
	}
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	public int getEffTime() {
		return effTime;
	}
	public void setEffTime(int effTime) {
		this.effTime = effTime;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	
	
}
