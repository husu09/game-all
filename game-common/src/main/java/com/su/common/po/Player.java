package com.su.common.po;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.su.common.data.Cache;

@Cache
@Entity
public class Player implements Serializable {
	@Id
	private long id;
	/**
	 * 名字
	 */
	private String name;
	/**
	 * 等级
	 */
	private int level;
	/**
	 * 头像
	 */
	private String picture;
	/**
	 * 金币
	 */
	private int gold;
	/**
	 * 钻石
	 */
	private int diamond;
	/**
	 * 创建时间
	 */
	private long createTime;
	/**
	 * 最后登录时间
	 */
	private long lastLoginTime;
	/**
	 * 最后登出时间
	 */
	private long lastLogoutTime;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public int getGold() {
		return gold;
	}
	public void setGold(int gold) {
		this.gold = gold;
	}
	public int getDiamond() {
		return diamond;
	}
	public void setDiamond(int diamond) {
		this.diamond = diamond;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public long getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(long lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public long getLastLogoutTime() {
		return lastLogoutTime;
	}
	public void setLastLogoutTime(long lastLogoutTime) {
		this.lastLogoutTime = lastLogoutTime;
	}
	
	

}
