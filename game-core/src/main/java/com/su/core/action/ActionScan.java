package com.su.core.action;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import com.su.proto.core.ProtoContext;

/**
 * 加载所有的协议处理者
 * */
@Component
public class ActionScan implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private ActionContext actionContext;
	@Autowired
	private ProtoContext protoContext;


	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		Map<String, Object> beans = event.getApplicationContext().getBeansWithAnnotation(Controller.class);
		
		for (Object bean : beans.values()) {
			Method[] methods = bean.getClass().getMethods();
			for (Method method : methods) {
				if (method.isAnnotationPresent(Action.class)) {
					// 是否需要登录
					boolean mustLogin = method.getAnnotation(Action.class).mustLogin();
					Parameter parameter = method.getParameters()[1];
					String messageName = parameter.getType().getSimpleName();
					if (!protoContext.contains(messageName)) {
						System.out.println("协议不存在：" + messageName);
					}
					if (!messageName.endsWith("Req")) {
						System.out.println("不是请求协议：" + messageName);
					}
					if (actionContext.contains(messageName)) {
						System.out.println("重复的消息处理对象：" + messageName);
					}

					actionContext.add(messageName, new ActionMeta(mustLogin, bean, method));
				}
			}
		}
		
	}
	


}
