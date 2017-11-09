package com.su.excel.core.type;

public class NumParser implements TypeParser<Integer>{

	@Override
	public Integer parser(String value) {
		return Integer.parseInt(value);
	}

	@Override
	public String name() {
		return "num";
	}

}
