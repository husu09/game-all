package com.su.excel.mapper;

import org.springframework.stereotype.Component;

import com.su.excel.config.KVCo;
import com.su.excel.core.AbstractExcelMapper;
import com.su.excel.core.RowData;

@Component
public class ParameterMapper extends AbstractExcelMapper<KVCo> {

	@Override
	public String getName() {
		return "1参数";
	}

	@Override
	public KVCo map(RowData rowData) {
		KVCo temp = new KVCo();
		temp.setId(rowData.getInt("id"));
		temp.setValue(rowData.getString("cs"));
		return temp;
	}
	
	/**
	 * 初始化常量
	 * */
	public void initConst() {
		
	}

}
