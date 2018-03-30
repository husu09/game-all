package com.su.core.data;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.criterion.Projections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.su.common.rmi.DataRmiService;
import com.su.common.util.SpringUtil;

/**
 * id 生成器
 */
@Component
public class IDGenerator {

	private final static Logger logger = LoggerFactory.getLogger(IDGenerator.class);

	@Autowired
	private DataRmiService dataRmiService;

	private Map<String, AtomicLong> idMap = new HashMap<>();

	public long next(Object o) {
		String parentKey = CacheUtil.getParentKey(o);
		AtomicLong atomicLong = idMap.get(parentKey);
		long id = atomicLong.incrementAndGet();
		setId(o, id);
		return id;
	}

	private long getMaxId(Object o) {
		return dataRmiService.get(o.getClass(), Projections.max("id"));
	}

	/**
	 * 设置 id
	 */
	public void setId(Object o, Object id) {
		Field[] fields = o.getClass().getDeclaredFields();
		boolean flag = false;
		for (Field f : fields) {
			if (f.isAnnotationPresent(Id.class)) {
				try {
					flag = true;
					f.setAccessible(true);
					f.set(o, id);
					break;
				} catch (Exception e) {
					e.printStackTrace();
					logger.info("设置对象id失败{} {}", o, id);
				}
			}
		}
		if (!flag)
			logger.info("对象没有id属性{}", o);
	}

	public void init() {
		Map<String, Object> beans = SpringUtil.getContext().getBeansWithAnnotation(Entity.class);
		for (Object o : beans.values()) {
			String parentKey = CacheUtil.getParentKey(o);
			AtomicLong atomicLong = idMap.get(parentKey);
			if (atomicLong == null) {
				synchronized (o) {
					atomicLong = idMap.get(parentKey);
					if (atomicLong == null) {
						atomicLong = new AtomicLong(getMaxId(o));
						idMap.put(parentKey, atomicLong);
					}
				}
			}
		}
	}

}
