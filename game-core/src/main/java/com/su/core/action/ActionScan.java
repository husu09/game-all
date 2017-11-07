package com.su.core.action;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import com.su.core.proto.ProtoContext;

@Component
public class ActionScan implements BeanPostProcessor {

	@Autowired
	private ActionContext actionContext;
	@Autowired
	private ProtoContext protoContext;

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if (bean.getClass().isAnnotationPresent(Controller.class)) {
			Method[] methods = bean.getClass().getMethods();
			for (Method method : methods) {
				if (method.isAnnotationPresent(Action.class)) {
					// 是否需要登录
					boolean mustLogin = method.getAnnotation(Action.class).mustLogin();
					Parameter parameter = method.getParameters()[1];
					String messageName = parameter.getType().getSimpleName();

					if (!protoContext.contains(messageName)) {
						System.out.println("协议不存在：" + messageName);
						return bean;
					}
					if (messageName.endsWith("Req")) {
						System.out.println("不是请求协议：" + messageName);
						return bean;
					}
					if (actionContext.contains(messageName)) {
						System.out.println("重复的消息处理对象：" + messageName);
						return bean;
					}

					actionContext.add(messageName, new ActionMeta(mustLogin, bean));
				}
			}
		}
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

}
