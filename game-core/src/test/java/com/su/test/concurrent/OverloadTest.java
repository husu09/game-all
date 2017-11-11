package com.su.test.concurrent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class OverloadTest {
	
	public void save(Object o) {
		System.out.println("save(Object o)");
	}
	
	public void save(Collection<Object> o) {
		System.out.println("save(Collection<Object> o)");
	}
	
	public void save(Object[] o) {
		System.out.println("save(Object[] o)");
	}
	
	public static void main(String[] args) {
		OverloadTest t = new OverloadTest();
		Object o = new Object();
		List list = new ArrayList<>();
		Integer[] arr = new Integer[]{1};
		t.save(list);
	}
}
