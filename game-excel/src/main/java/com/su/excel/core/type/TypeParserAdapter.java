package com.su.excel.core.type;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class TypeParserAdapter<T> implements TypeParser<T> {
	
	@Autowired
	private TypeManager typeManager;
	
	@PostConstruct
	public void init() {
		typeManager.put(name(), this);
	}

}
