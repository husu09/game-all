package com.su.common.po;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.alibaba.fastjson.JSON;
import com.su.common.data.Cache;
import com.su.common.obj.Grid;

@Cache
@Entity
public class PlayerDetail {
	@Id
	private long id;
	/**
	 * 背包
	 * */
	private String bagData;
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	public List<Grid> getBagGrid() {
		List<Grid> gridList = JSON.parseArray(bagData, Grid.class);
		if (gridList == null)
			gridList = new ArrayList<>();
		return gridList;
	}
	
	public void updateBagData() {
		bagData = JSON.toJSONString(bagData);
	}
	
}
