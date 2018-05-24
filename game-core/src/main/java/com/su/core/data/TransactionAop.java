package com.su.core.data;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TransactionAop {
	@Autowired
	private TransactionManager transactionManager;
	
	@Around("execution (* com.su.server.service.*.*(..))")
	public Object transactionProcess(ProceedingJoinPoint pj) throws Throwable {
		Object result = null;
		try {
			// 执行目标方法
			result = pj.proceed();
			transactionManager.commit();
			System.out.println("commint...........");
		} catch (Throwable e) {
			transactionManager.rollblack();
			System.out.println("rollblack...........");
			throw e;
		}
		return result;
	}
}
