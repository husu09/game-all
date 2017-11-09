package com.su.excel.po;

import org.springframework.stereotype.Component;

import com.su.excel.core.ExcelMappingAdapter;
import com.su.excel.core.RowData;

@Component
public class GenEp extends ExcelMappingAdapter<GenEp> {

	@Override
	public GenEp mapping(RowData rowData) {
		
		return null;
	}

	@Override
	public String name() {
		return "C翅膀";
	}

	private int id;
	private String name;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	

}
