package com.su.data.dao;

import java.util.List;

import javax.persistence.EntityGraph;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class BaseDao {

	@Autowired
	private SessionFactory sessionFactory;

	public <T> int save(T t) {
		Session session = sessionFactory.getCurrentSession();
		return (int) session.save(t);
	}

	public <T> void update(T t) {
		Session session = sessionFactory.getCurrentSession();
		session.update(t);
	}

	public <T> void delete(T t) {
		Session session = sessionFactory.getCurrentSession();
		session.delete(t);
	}

	public <T> void get(Class<T> c, Integer id) {
		Session session = sessionFactory.getCurrentSession();
		session.get(c, id);
	}

	public <T> List<T> list(Class<T> c) {
		Session session = sessionFactory.getCurrentSession();
		EntityGraph<T> graph = session.createEntityGraph(c);
		Criteria criteria = session.createCriteria(c);
		return criteria.list();
	}

	public <T> List<T> list(Class<T> c, int first, int max) {
		Session session = sessionFactory.getCurrentSession();
		EntityGraph<T> graph = session.createEntityGraph(c);
		Criteria criteria = session.createCriteria(c);
		criteria.setFirstResult(first);
		criteria.setMaxResults(max);
		return criteria.list();
	}
}
