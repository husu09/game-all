package com.su.excel.mapper;

import org.springframework.stereotype.Component;

import com.su.config.FieldCo;
import com.su.excel.core.AbstractExcelMapper;
import com.su.excel.core.RowData;

@Component
public class FieldMapper extends AbstractExcelMapper<FieldCo> {

	@Override
	public String getName() {
		return "Y游戏场";
	}

	@Override
	public FieldCo map(RowData rowData) {
		FieldCo t = new FieldCo();
		t.setId(rowData.getInt("id"));
		t.setSiteType(rowData.getInt("lx"));
		t.setOpen(rowData.getBoolean("sfkf"));
		t.setBaseScore(rowData.getInt("df"));
		t.setMiniBean(rowData.getInt("rczdxz"));
		t.setMaxBean(rowData.getInt("rczgxz"));
		t.setInitTableNum(rowData.getInt("cszs"));
		t.setRankingAddition(rowData.getInt("bwdfjc"));
		return t;
	}

}
