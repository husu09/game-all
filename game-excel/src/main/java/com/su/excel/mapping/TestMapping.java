package com.su.excel.mapping;

import org.springframework.stereotype.Component;

import com.su.excel.conf.GenConf;
import com.su.excel.core.ExcelMappingAdapter;
import com.su.excel.core.RowData;

@Component
public class TestMapping extends ExcelMappingAdapter<GenConf> {

	@Override
	public GenConf mapping(RowData rowData) {
		GenConf po = new GenConf();
		po.setId(rowData.getInt("id"));
		po.setName(rowData.getString("mz"));
		return po;
	}

	@Override
	public String name() {
		return "J将领表";
	}
}
