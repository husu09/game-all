package com.su.core.akka;

import com.google.protobuf.MessageLite;
import com.su.core.action.ActionContext;
import com.su.core.action.ActionMeta;
import com.su.core.util.SpringContextUtil;

import akka.actor.TypedActor;
import io.netty.channel.ChannelHandlerContext;

public class ProcessorActorImpl implements ProcessorActor {

	private AkkaContext akkaContext = SpringContextUtil.getSpringContext().getBean(AkkaContext.class);
	private ActionContext actionContext = SpringContextUtil.getSpringContext().getBean(ActionContext.class);

	@Override
	public void process(ChannelHandlerContext ctx, MessageLite messageLite) {
		ActionMeta actionMeta = actionContext.get(messageLite.getClass().getSimpleName());
		if (actionMeta == null) {
			// 没有找到对应的协议处理类
			return;
		}
		
		
	}

	@Override
	public void poisonPill() {
		TypedActor.get(akkaContext.getActorSystem()).poisonPill(this);

	}

	@Override
	public void stop() {
		TypedActor.get(akkaContext.getActorSystem()).stop(this);
	}

}
