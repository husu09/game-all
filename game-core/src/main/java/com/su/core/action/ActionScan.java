package com.su.core.action;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import com.su.common.util.SpringUtil;
import com.su.proto.core.ProtoContext;

/**
 * 加载所有的协议处理者
 */
@Component
public class ActionScan {

	@Autowired
	private ActionContext actionContext;
	@Autowired
	private ProtoContext protoContext;

	public void scan() {

		Map<String, Object> beans = SpringUtil.getContext().getBeansWithAnnotation(Controller.class);

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
