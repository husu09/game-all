package com.su.core.action;

public class ActionMeta {
	/**
	 * 是否需要登录
	 * */
	private boolean mustLogin;
	/**
	 * 方法执行者
	 * */
	private Object executor;
	
	public ActionMeta(boolean mustLogin, Object executor) {
		this.mustLogin = mustLogin;
		this.executor = executor;
	}
	public boolean isMustLogin() {
		return mustLogin;
	}
	public void setMustLogin(boolean mustLogin) {
		this.mustLogin = mustLogin;
	}
	public Object getExecutor() {
		return executor;
	}
	public void setExecutor(Object executor) {
		this.executor = executor;
	}
	
	
}
