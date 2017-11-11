package com.su.core.akka;

import java.lang.reflect.InvocationTargetException;

import javax.swing.ActionMap;

import com.google.protobuf.MessageLite;
import com.su.core.action.ActionContext;
import com.su.core.action.ActionMeta;
import com.su.core.context.GameContext;
import com.su.core.context.PlayerContext;
import com.su.core.util.SpringContextUtil;

import akka.actor.TypedActor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

public class ProcessorActorImpl implements ProcessorActor {

	private AkkaContext akkaContext = SpringContextUtil.getSpringContext().getBean(AkkaContext.class);
	private ActionContext actionContext = SpringContextUtil.getSpringContext().getBean(ActionContext.class);
	private GameContext gameContext = SpringContextUtil.getSpringContext().getBean(GameContext.class);
	public static final AttributeKey<PlayerContext> PLAYER_CONTEXT_KEY = AttributeKey.valueOf("PLAYER_CONTEXT_KEY");  

	@Override
	public void process(ChannelHandlerContext ctx, MessageLite messageLite) throws Exception {
		ActionMeta actionMeta = actionContext.get(messageLite.getClass().getSimpleName());
		if (actionMeta == null) {
			// 没有找到对应的协议处理类
			return;
		}
		if (actionMeta.isMustLogin()) {
			PlayerContext playerContext = ctx.channel().attr(PLAYER_CONTEXT_KEY).get();
			if (playerContext == null) {
				//没有找到对应的PlayerContext
				return;
			}
		}
		PlayerContext playerContext = new PlayerContext();
		playerContext.setCtx(ctx);
		actionMeta.getMethod().invoke(actionMeta.getExecutor(), playerContext, messageLite);
		
		
		
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
