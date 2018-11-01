package com.su.server.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.su.core.action.Action;
import com.su.core.context.PlayerContext;
import com.su.core.game.MatchSite;
import com.su.core.game.enums.SiteType;
import com.su.msg.MatchSiteMsg.CancelMatch;
import com.su.msg.MatchSiteMsg.CancelMatch_;
import com.su.msg.MatchSiteMsg.GetMatchSite;
import com.su.msg.MatchSiteMsg.GetMatchSite_;
import com.su.msg.MatchSiteMsg.Match;
import com.su.msg.MatchSiteMsg.Match_;
import com.su.msg.MatchSiteMsg._MatchSite;
import com.su.server.service.MatchSiteService;

@Controller
public class MatchSiteControl {

	@Autowired
	private MatchSiteService matchSiteService;

	/**
	 * 匹配
	 */
	@Action
	public void match(PlayerContext playerContext, Match req) {
		if (playerContext.getGamePlayer() != null && playerContext.getGamePlayer().getState() != null) {
			playerContext.sendError(3002);
			return;
		}
		MatchSite matchSite = matchSiteService.getMatchSiteMap().get(req.getSiteId());
		if (matchSite == null) {
			playerContext.sendError(1002);
			return;
		}
		// 加入到匹配队列
		matchSite.addPlayerToMatch(playerContext, false);
		playerContext.write(Match_.getDefaultInstance());
	}

	/**
	 * 取消匹配
	 */
	@Action
	public void mcancelMatch(PlayerContext playerContext, CancelMatch req) {
		if (playerContext.getGamePlayer() != null && playerContext.getGamePlayer().getState() != null) {
			playerContext.sendError(3002);
			return;
		}
		// 从匹配队列中移除
		MatchSite matchSite = matchSiteService.getMatchSiteMap().get(req.getSiteId());
		if (matchSite == null) {
			playerContext.sendError(1002);
			return;
		}
		matchSite.removePlayerFromMatch(playerContext.getGamePlayer());
		playerContext.write(CancelMatch_.getDefaultInstance());

	}

	/**
	 * 获取信息
	 */
	@Action
	public void getMatchSite(PlayerContext playerContext, GetMatchSite req) {
		if (req.getSiteType() != SiteType.CLASSIC.getValue() && req.getSiteType() != SiteType.RANKING.getValue()) {
			playerContext.sendError(1002);
			return;
		}
		GetMatchSite_.Builder resp = GetMatchSite_.newBuilder();
		_MatchSite.Builder _matchSite = _MatchSite.newBuilder();
		// 获取指定类型的游戏场
		for (MatchSite matchSite : matchSiteService.getMatchSiteMap().values()) {
			if (matchSite.getSiteType().getValue() == req.getSiteType()) {
				_matchSite.setSiteId(matchSite.getSiteCo().getId());
				_matchSite.setPlayerNum(matchSite.getPlayerNum());
				resp.addMatchSite(_matchSite);
				_matchSite.clear();
			}
		}
		playerContext.write(resp);
	}
	
	
}
