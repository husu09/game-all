package com.su.excel.config;

import com.su.common.obj.Item;

public class BagConfig {
	private int id;
	/**
	 * 类型
	 */
	private int type;
	/**
	 * 品质
	 */
	private int quality;
	/**
	 * 使用类型
	 */
	private int useType;
	/**
	 * 使用效果
	 */
	private Item effectItem;
	private int effectNum;
	/**
	 * 有效期/毫秒
	 */
	private int expirationTime;
	/**
	 * 叠加上限
	 */
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
	public Item getEffectItem() {
		return effectItem;
	}
	public void setEffectItem(Item effectItem) {
		this.effectItem = effectItem;
	}
	public int getEffectNum() {
		return effectNum;
	}
	public void setEffectNum(int effectNum) {
		this.effectNum = effectNum;
	}
	public int getExpirationTime() {
		return expirationTime;
	}
	public void setExpirationTime(int expirationTime) {
		this.expirationTime = expirationTime;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
}
