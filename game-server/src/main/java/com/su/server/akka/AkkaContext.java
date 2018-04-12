package com.su.server.akka;

import org.springframework.stereotype.Component;

import akka.actor.ActorSystem;
import akka.actor.TypedActor;
import akka.actor.TypedProps;

@Component
public class AkkaContext {
	
	
	private ActorSystem system = ActorSystem.create("GAME");
	
	/**
	 * 创建Actor
	 * */
	public ProcessorActor createActor() {
		return TypedActor.get(system).typedActorOf(new TypedProps<ProcessorActorImpl>(ProcessorActor.class, ProcessorActorImpl.class));
	}
	
	/**
	 * 获取 ActorSystem
	 * */
	public ActorSystem getActorSystem() {
		return system;
	}
	
	public void close() {
		system.terminate();
	}
    
	
}
