package com.su.common.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Transient;

import com.alibaba.fastjson.JSON;

public class CustomConvert {
	
	public static String toJSONString(Object obj) {
		Map<String, Object> map = null;
		Field[] declaredFields = obj.getClass().getDeclaredFields();
		for (Field field : declaredFields) {
			if (field.isAnnotationPresent(Transient.class))
				continue;
			if (field.getType().isPrimitive() || field.getType().isAssignableFrom(String.class)) {
				if (!field.isAccessible())
					field.setAccessible(true);
				if (map == null)
					map = new HashMap<>();
				try {
					map.put(field.getName(), field.get(obj));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return JSON.toJSONString(map);
	}
	
	public static <T> T parseObject(String text, Class<T> clazz) {
		return null;
	}
	

}
