package com.su.core.data;

import java.lang.reflect.Field;
import java.util.Collection;

import javax.persistence.Id;

import org.springframework.stereotype.Component;

@Component
public class CacheUtil {
	/**
	 * 生成 key
	 */
	public static String getKey(Object o) {
		String name = o.getClass().getSimpleName();
		Object id = null;
		Field[] fields = o.getClass().getFields();
		for (Field f : fields) {
			if (f.isAnnotationPresent(Id.class)) {
				try {
					id = f.get(o);
					break;
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
	

	public static String getKey(Class<?> c, int id) {
		return c.getSimpleName() + id;
	}
	
	/**
	 * 生成 parent key
	 */
	public static String getParentKey(Object o) {
		return o.getClass().getSimpleName();
	}

	/**
	 * 生成 parent key
	 */
	public static String getParentKey(Class<?> c) {
		return c.getSimpleName();
	}

	/**
	 * 检测对象是否是持久化状态
	 */
	public static boolean isPersistent(Object o) {
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

	public static boolean isRedisCache(Class<?> c) {
		Cache ac = c.getAnnotation(Cache.class);
		if (ac != null) {
			return ac.redisCache();
		}
		return false;
	}

	public static boolean isRedisCache(Object o) {
		Cache ac = o.getClass().getAnnotation(Cache.class);
		if (ac != null) {
			return ac.redisCache();
		}
		return false;
	}

	public static boolean isRedisCache(Collection<Object> os) {
		if (os.size() == 0)
			return false;
		Object t = os.iterator().next();
		Cache ac = t.getClass().getAnnotation(Cache.class);
		if (ac != null) {
			return ac.redisCache();
		}
		return false;
	}

	public static boolean isRedisCache(Object[] os) {
		if (os.length == 0)
			return false;
		Object t = os[0];
		Cache ac = t.getClass().getAnnotation(Cache.class);
		if (ac != null) {
			return ac.redisCache();
		}
		return false;
	}

	public static boolean isMemoryCache(Class<?> c) {
		Cache ac = c.getAnnotation(Cache.class);
		if (ac != null) {
			return ac.memoryCache();
		}
		return false;
	}

	public static boolean isMemoryCache(Object o) {
		Cache ac = o.getClass().getAnnotation(Cache.class);
		if (ac != null) {
			return ac.memoryCache();
		}
		return false;
	}

	public static boolean isMemoryCache(Collection<Object> os) {
		if (os.size() == 0)
			return false;
		Object t = os.iterator().next();
		Cache ac = t.getClass().getAnnotation(Cache.class);
		if (ac != null) {
			return ac.memoryCache();
		}
		return false;
	}

	public static <T> boolean isMemoryCache(Object[] os) {
		if (os.length == 0)
			return false;
		Object t = os[0];
		Cache ac = t.getClass().getAnnotation(Cache.class);
		if (ac != null) {
			return ac.memoryCache();
		}
		return false;
	}

}
