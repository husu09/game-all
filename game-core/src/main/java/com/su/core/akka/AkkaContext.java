package com.su.core.akka;

import org.springframework.stereotype.Component;

import akka.actor.ActorSystem;
import akka.actor.TypedActor;
import akka.actor.TypedProps;
import akka.japi.Creator;

@Component
public class AkkaContext {

	private ActorSystem system = ActorSystem.create("GAME");

	/**
	 * 创建Actor
	 */
	public <T> T createActor(Class<T> interfaceCls, Class implementCls) {
		return TypedActor.get(system).typedActorOf(new TypedProps<T>(interfaceCls, implementCls));
	}
	
	public <T> T createActor(Class<T> interfaceCls, Class implementCls, Object obj) {
		return TypedActor.get(system).typedActorOf(new TypedProps<T>(interfaceCls, new Creator<T>() {
			@Override
			public T create() throws Exception {
				return (T) implementCls.getConstructor(obj.getClass()).newInstance(obj);
			}
		}));
	}

	/**
	 * 获取 ActorSystem
	 */
	public ActorSystem getActorSystem() {
		return system;
	}

	public void close() {
		system.terminate();
	}

}
