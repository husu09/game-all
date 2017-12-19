package com.su.excel.manager;

import org.springframework.stereotype.Component;

import com.su.excel.core.ExcelMappingAdapter;
import com.su.excel.core.RowData;

@Component
public class TestManager extends ExcelMappingAdapter<TestManager> {

	@Override
	public TestManager mapping(RowData rowData) {
		
		return null;
	}

	@Override
	public String name() {
		return "C翅膀";
	}

}
