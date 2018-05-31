package com.su.core.akka;

import org.springframework.stereotype.Component;

import akka.actor.ActorSystem;
import akka.actor.TypedActor;
import akka.actor.TypedProps;
import akka.japi.Creator;

@Component
public class AkkaContext {

	private static final ActorSystem system = ActorSystem.create("GAME");

	/**
	 * 创建actor
	 */
	public <T> T createActor(Class<T> interfaceCls, Class<T> implementCls) {
		return TypedActor.get(system).typedActorOf(new TypedProps<T>(interfaceCls, implementCls));
	}

	/**
	 * 创建actor（带参数的）
	 */
	public <T> T createActor(Class<T> interfaceCls, Class implementCls, Object... objs) {
		return TypedActor.get(system).typedActorOf(new TypedProps<T>(interfaceCls, new Creator<T>() {
			@Override
			public T create() throws Exception {
				Class[] classzs = new Class[objs.length];
				for (int i = 0; i < objs.length; i++) {
					classzs[i] = objs[i].getClass();
				}
				return (T) implementCls.getConstructor(classzs).newInstance(objs);
			}
		}));
	}

	/**
	 * 关闭actorySystem
	 * */
	public void close() {
		system.terminate();
	}
	
	/**
	 * 关闭 acotr
	 * */
	public void stop(Object obj) {
		TypedActor.get(system).stop(obj);
	}
	
	/**
	 * 关闭 acotr（等待所有调用完成后关闭）
	 * */
	public void poisonPill(Object obj) {
		TypedActor.get(system).poisonPill(obj);
	}
	

}
