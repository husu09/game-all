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
	 * 账号
	 */
	private String account;
	/**
	 * 名称
	 * */
	private String name;
	/**
	 * 花生
	 * */
	private int peanut;
	/**
	 * 段位
	 * */
	private int rankStep;
	
	public void setId(long id) {
		this.id = id;
	}
	public long getId() {
		return id;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPeanut() {
		return peanut;
	}
	public void setPeanut(int peanut) {
		this.peanut = peanut;
	}
	public int getRankStep() {
		return rankStep;
	}
	public void setRankStep(int rankStep) {
		this.rankStep = rankStep;
	}
	
}
