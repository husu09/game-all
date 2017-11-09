package com.su.excel.core;

public interface ExcelMapping<T> {
	
	public String name();
	
	public T mapping(RowData rowData);
	
	public void complete();
	
	public void completeAll();
	
	public T get(int id);
}
