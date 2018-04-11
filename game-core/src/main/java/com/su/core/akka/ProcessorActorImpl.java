package com.su.core.akka;

import com.google.protobuf.MessageLite;
import com.su.common.util.SpringUtil;
import com.su.core.action.ActionContext;
import com.su.core.action.ActionMeta;
import com.su.core.cons.ErrCode;
import com.su.core.context.PlayerContext;
import com.su.core.event.GameEventDispatcher;
import com.su.core.netty.NettyServerHandler;

import akka.actor.TypedActor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;

public class ProcessorActorImpl implements ProcessorActor {

	private AkkaContext akkaContext = SpringUtil.getContext().getBean(AkkaContext.class);
	private ActionContext actionContext = SpringUtil.getContext().getBean(ActionContext.class);
	private GameEventDispatcher gameEventDispatcher = SpringUtil.getContext().getBean(GameEventDispatcher.class);
	

	@Override
	public void process(ChannelHandlerContext ctx, MessageLite messageLite) {
		try {
			ActionMeta actionMeta = actionContext.get(messageLite.getClass().getSimpleName());
			if (actionMeta == null) {
				//没有找到对应的协议处理类
				PlayerContext.sendError(ctx, ErrCode.NOT_FIND_ACTION);
				return;
			}
			Attribute<PlayerContext> attr = ctx.channel().attr(NettyServerHandler.PLAYER_CONTEXT_KEY);
			if (actionMeta.isMustLogin()) {
				PlayerContext playerContext = attr.get();
				if (playerContext == null) {
					//没有找到对应的PlayerContext
					PlayerContext.sendError(ctx, ErrCode.NOT_FIND_PLAYER_CONTEXT);
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
			//系统错误
			PlayerContext.sendError(ctx, ErrCode.SYSTEM_ERROR);
		}

	}

	@Override
	public void stop() {
		TypedActor.get(akkaContext.getActorSystem()).stop(this);
	}

	@Override
	public void logout(PlayerContext playerContext) {
		gameEventDispatcher.logout(playerContext);
		
	}

	@Override
	public void checkRefresh(PlayerContext playerContext) {
		// TODO Auto-generated method stub
		
	}

}
