package com.su.core.akka;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import akka.actor.ActorSystem;
import akka.actor.TypedActor;
import akka.actor.TypedProps;

@Component
public class AkkaContext {
	
	/**
	 * <channelId, actor>
	 * */
	private Map<String, ProcessorActor> actorMap = new ConcurrentHashMap<>();
	
	private ActorSystem system = ActorSystem.create("GAME");
	
	/**
	 * 创建Actor
	 * */
	public ProcessorActor createActor() {
		return TypedActor.get(system).typedActorOf(new TypedProps<ProcessorActorImpl>(ProcessorActor.class, ProcessorActorImpl.class));
	}
	
	/**
	 * 根据 channelId 获取 Actor
	 * */
	public ProcessorActor getActor(String channelId) {
		return actorMap.get(channelId);
	}
	
	/**
	 * 添加 Actor
	 * */
	public void addActor(String channelId, ProcessorActor processorActor) {
		actorMap.put(channelId, processorActor);
	}
	
	/**
	 * 移除 Actor
	 * */
	public void removeActor(String channelId) {
		actorMap.remove(channelId);
	}
	
	/**
	 * 判断 Actor 是否存在
	 * */
	public boolean containsActor(String channelId) {
		return actorMap.containsKey(channelId);
	}
	
	/**
	 * 获取 ActorSystem
	 * */
	public ActorSystem getActorSystem() {
		return system;
	}
	
}
