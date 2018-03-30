package com.su.excel.map;

import org.springframework.stereotype.Component;

import com.su.excel.co.GenCo;
import com.su.excel.core.ExcelMapAdapter;
import com.su.excel.core.RowData;

@Component
public class TestMap extends ExcelMapAdapter<GenCo> {

	@Override
	public GenCo map(RowData rowData) {
		GenCo po = new GenCo();
		po.setId(rowData.getInt("id"));
		po.setName(rowData.getString("mz"));
		return po;
	}

	@Override
	public String getName() {
		return "J将领表";
	}
}
