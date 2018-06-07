package com.su.core.akka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.MessageLite;
import com.su.common.util.SpringUtil;
import com.su.core.action.ActionContext;
import com.su.core.action.ActionMeta;
import com.su.core.context.PlayerContext;
import com.su.core.event.GameEventDispatcher;
import com.su.core.gambling.CardResult;
import com.su.core.netty.NettyServerHandler;
import com.su.msg.LoginMsg.Login_;

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
				// 没有找到对应的协议处理类
				logger.error("not find action-meta {}", messageLite.getClass().getSimpleName());
				PlayerContext.sendError(ctx, 10001);
				return;
			}
			Attribute<PlayerContext> attr = ctx.channel().attr(NettyServerHandler.PLAYER_CONTEXT_KEY);
			if (actionMeta.isMustLogin()) {
				PlayerContext playerContext = attr.get();
				if (playerContext == null) {
					// 没有找到对应的PlayerContext
					logger.error("not find player context");
					PlayerContext.sendError(ctx, 10001);
					return;
				}
				actionMeta.getMethod().invoke(actionMeta.getExecutor(), playerContext, messageLite);
			} else {
				PlayerContext playerContext = new PlayerContext();
				playerContext.setCtx(ctx);
				playerContext.setActor(this);
				actionMeta.getMethod().invoke(actionMeta.getExecutor(), playerContext, messageLite);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 系统错误
			PlayerContext.sendError(ctx, 10001);
		}

	}

	@Override
	public void logout(PlayerContext playerContext) {
		gameEventDispatcher.logout(playerContext);

	}

	@Override
	public void checkRefresh(PlayerContext playerContext) {

	}

	@Override
	public void login(PlayerContext playerContext, Login_.Builder builder) {
		gameEventDispatcher.login(playerContext, builder);
	}

	@Override
	public void doCardResult(CardResult cardResult) {
		
	}

}
