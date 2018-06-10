package com.su.excel.map;

import org.springframework.stereotype.Component;

import com.su.excel.co.BagCo;
import com.su.excel.core.ExcelMapAdapter;
import com.su.excel.core.RowData;

@Component
public class BagConf extends ExcelMapAdapter<BagCo> {

	@Override
	public String getName() {
		return "道具";
	}

	@Override
	public BagCo map(RowData rowData) {
		
		return null;
	}

}
