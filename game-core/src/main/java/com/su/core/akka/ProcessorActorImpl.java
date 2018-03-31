package com.su.core.akka;

import com.google.protobuf.MessageLite;
import com.su.common.util.SpringUtil;
import com.su.core.action.ActionContext;
import com.su.core.action.ActionMeta;
import com.su.core.context.PlayerContext;
import com.su.core.netty.NettyServerHandler;

import akka.actor.TypedActor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;

public class ProcessorActorImpl implements ProcessorActor {

	private AkkaContext akkaContext = SpringUtil.getContext().getBean(AkkaContext.class);
	private ActionContext actionContext = SpringUtil.getContext().getBean(ActionContext.class);

	@Override
	public void process(ChannelHandlerContext ctx, MessageLite messageLite) {
		try {
			ActionMeta actionMeta = actionContext.get(messageLite.getClass().getSimpleName());
			if (actionMeta == null) {
				PlayerContext.sendError(ctx, -1, "没有找到对应的协议处理类");
				return;
			}
			Attribute<PlayerContext> attr = ctx.channel().attr(NettyServerHandler.PLAYER_CONTEXT_KEY);
			if (actionMeta.isMustLogin()) {
				PlayerContext playerContext = attr.get();
				if (playerContext == null) {
					PlayerContext.sendError(ctx, -1, "没有找到对应的PlayerContext");
					return;
				}
				actionMeta.getMethod().invoke(actionMeta.getExecutor(), playerContext, messageLite);
			}
			PlayerContext playerContext = new PlayerContext();
			playerContext.setCtx(ctx);
			playerContext.setActor(this);
			actionMeta.getMethod().invoke(actionMeta.getExecutor(), playerContext, messageLite);
		} catch (Exception e) {
			e.printStackTrace();
			PlayerContext.sendError(ctx, -1, "系统错误");
		}

	}

	@Override
	public void stop() {
		TypedActor.get(akkaContext.getActorSystem()).stop(this);
	}

}
