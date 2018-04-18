package com.su.core.akka;

import org.springframework.stereotype.Component;

import akka.actor.ActorSystem;
import akka.actor.TypedActor;
import akka.actor.TypedProps;

@Component
public class AkkaContext {

	private ActorSystem system = ActorSystem.create("GAME");

	/**
	 * 创建Actor
	 */
	public <T> T createActor(Class<T> interfaceCls, Class implementationCls) {
		return TypedActor.get(system).typedActorOf(new TypedProps<T>(interfaceCls, implementationCls));
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
