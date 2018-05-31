package com.su.server.akka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.MessageLite;
import com.su.common.util.SpringUtil;
import com.su.core.action.ActionContext;
import com.su.core.action.ActionMeta;
import com.su.server.context.PlayerContext;
import com.su.server.event.GameEventDispatcher;
import com.su.server.netty.NettyServerHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;

public class PLayerActorImpl implements PlayerActor {
	
	
	private Logger logger = LoggerFactory.getLogger(PlayerActor.class);

	private ActionContext actionContext = SpringUtil.getContext().getBean(ActionContext.class);
	private GameEventDispatcher gameEventDispatcher = SpringUtil.getContext().getBean(GameEventDispatcher.class);
	

	@Override
	public void process(ChannelHandlerContext ctx, MessageLite messageLite) {
		try {
			ActionMeta actionMeta = actionContext.getActionMetaMap().get(messageLite.getClass().getSimpleName());
			if (actionMeta == null) {
				//没有找到对应的协议处理类
				logger.error("not find action-meta {}", messageLite.getClass().getSimpleName());
				PlayerContext.sendError(ctx, 00001);
				return;
			}
			Attribute<PlayerContext> attr = ctx.channel().attr(NettyServerHandler.PLAYER_CONTEXT_KEY);
			if (actionMeta.isMustLogin()) {
				PlayerContext playerContext = attr.get();
				if (playerContext == null) {
					//没有找到对应的PlayerContext
					logger.error("not find player context");
					PlayerContext.sendError(ctx, 00001);
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
			PlayerContext.sendError(ctx, 00001);
		}

	}


	@Override
	public void logout(PlayerContext playerContext) {
		gameEventDispatcher.logout(playerContext);
		
	}

	@Override
	public void checkRefresh(PlayerContext playerContext) {
		
		
	}


}
