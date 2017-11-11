package com.su.core.data;

import java.util.Collection;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projection;

public interface DataService {
	
	public <T> void save(T t);

	public <T> void save(Collection<T> ts);

	public <T> void save(T[] ts);
	
	public <T> int saveNow(T t);

	public <T> int[] saveNow(Collection<T> ts);

	public <T> int[] saveNow(T[] ts);

	public <T> void update(T t);

	public <T> void update(Collection<T> ts);

	public <T> void update(T[] ts);

	public int update(String sql);

	public <T> void delete(T t);

	public <T> void delete(Collection<T> ts);

	public <T> void delete(T[] ts);

	public int delete(String sql);

	public <T> T get(Class<T> c, Integer id);

	public <T> T get(DetachedCriteria detachedCriteria);

	public <T> List<T> list(Class<T> c);

	public <T> List<T> list(DetachedCriteria detachedCriteria);

	public <T> List<T> list(Class<T> c, int first, int max);

	public <T> List<T> list(DetachedCriteria detachedCriteria, int first, int max);

	public long get(Class<?> c, Projection projection);
}
