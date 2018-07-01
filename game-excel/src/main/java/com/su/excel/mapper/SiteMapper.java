package com.su.excel.mapper;

import org.springframework.stereotype.Component;

import com.su.excel.config.SiteCo;
import com.su.excel.core.AbstractExcelMapper;
import com.su.excel.core.RowData;

@Component
public class SiteMapper extends AbstractExcelMapper<SiteCo> {

	@Override
	public String getName() {
		return "游戏分类";
	}

	@Override
	public SiteCo map(RowData rowData) {
		SiteCo t = new SiteCo();
		t.setId(rowData.getInt("id"));
		t.setSiteType(rowData.getInt("lx"));
		t.setSiteMode(rowData.getInt("ms"));
		t.setOpen(rowData.getBoolean("sfkf"));
		t.setBaseScore(rowData.getInt("df"));
		t.setMiniBean(rowData.getInt("rczdxz"));
		t.setMaxBean(rowData.getInt("rczgxz"));
		t.setInitTableNum(rowData.getInt("cszs"));
		t.setRankingAddition(rowData.getInt("bwdfjc"));
		return t;
	}

}