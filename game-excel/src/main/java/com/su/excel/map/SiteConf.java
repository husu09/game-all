package com.su.excel.map;

import org.springframework.stereotype.Component;

import com.su.excel.config.SiteConfig;
import com.su.excel.core.AbstractExcelMapper;
import com.su.excel.core.RowData;

@Component
public class SiteConf extends AbstractExcelMapper<SiteConfig> {

	@Override
	public String getName() {
		return "游戏分类";
	}

	@Override
	public SiteConfig map(RowData rowData) {
		SiteConfig t = new SiteConfig();
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
