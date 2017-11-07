package com.su.core.akka;

import com.google.protobuf.MessageLite;
import com.su.common.util.SpringUtil;
import com.su.core.action.ActionContext;
import com.su.core.action.ActionMeta;
import com.su.core.context.PlayerContext;

import akka.actor.TypedActor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

public class ProcessorActorImpl implements ProcessorActor {

	private AkkaContext akkaContext = SpringUtil.getContext().getBean(AkkaContext.class);
	private ActionContext actionContext = SpringUtil.getContext().getBean(ActionContext.class);
	public static final AttributeKey<PlayerContext> PLAYER_CONTEXT_KEY = AttributeKey.valueOf("PLAYER_CONTEXT_KEY");

	@Override
	public void process(ChannelHandlerContext ctx, MessageLite messageLite) throws Exception {
		ActionMeta actionMeta = actionContext.get(messageLite.getClass().getSimpleName());
		if (actionMeta == null) {
			throw new RuntimeException("没有找到对应的协议处理类");
		}
		if (actionMeta.isMustLogin()) {
			PlayerContext playerContext = ctx.channel().attr(PLAYER_CONTEXT_KEY).get();
			if (playerContext == null) {
				throw new RuntimeException("没有找到对应的PlayerConte");
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
