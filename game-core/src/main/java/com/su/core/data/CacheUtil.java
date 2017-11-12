package com.su.core.data;

import java.lang.reflect.Field;

import javax.persistence.Id;

public class CacheUtil {
	/**
	 * 生成 key
	 * */
	public String getKey(Object o) {
		String name = o.getClass().getSimpleName();
		Object id = null;
		Field[] fields = o.getClass().getFields();
		for (Field f : fields) {
			if (f.isAnnotationPresent(Id.class)) {
				try {
					id = f.get(o);
				} catch (Exception e) {
					throw new RuntimeException("无法生成该对象的 key " + o);
				}
			}
		}
		if (id == null) {
			throw new RuntimeException("无法生成该对象的 key " + o);
		}
		return name + id;
	}
	
	public String getKey(Class<?> c, int id) {
		return c.getSimpleName() + id;
	}
	
	/**
	 * 生成 parent key
	 * */
	public String getParentKey(Object o) {
		return o.getClass().getSimpleName();
	}
	
	/**
	 * 生成 parent key
	 * */
	public String getParentKey(Class<?> c) {
		return c.getSimpleName();
	}
	
	/**
	 * 检测对象是否是持久化状态
	 * */
	public boolean isPersistent(Object o) {
		Field[] fields = o.getClass().getFields();
		for (Field f : fields) {
			if (f.isAnnotationPresent(Id.class)) {
				try {
					Object id = f.get(o);
					if (id != null)
						return true;
				} catch (Exception e) {
					return false;
				}
			}
		}
		return false;
	}

}
