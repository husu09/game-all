package com.su.server.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.su.common.constant.SiteConst;
import com.su.common.constant.SysAttr;
import com.su.common.po.Player;
import com.su.common.po.PlayerDetail;
import com.su.core.akka.BridgeService;
import com.su.core.context.PlayerContext;
import com.su.core.game.Site;
import com.su.core.game.TableResult;
import com.su.msg.GamblingMsg._GamePlayerResult;

@Service
public class BridgeServiceImpl implements BridgeService {

	private Logger logger = LoggerFactory.getLogger(BridgeServiceImpl.class);

	@Autowired
	private PlayerService playerService;
	@Autowired
	private ResourceService resourceService;
	@Autowired
	private SiteService siteService;

	@Override
	public _GamePlayerResult doTableResult(PlayerContext playerContext, TableResult tableResult) {
		Player player = playerService.getPlayerById(tableResult.getPlayerContext().getPlayerId());
		PlayerDetail playerDetail = playerService.getPlayerDetail(player.getId());
		_GamePlayerResult.Builder builder = _GamePlayerResult.newBuilder();
		if (tableResult.isWin()) {
			doClassicWin(playerContext, tableResult, player, playerDetail, builder);

		} else {
			doClassicLose(playerContext, tableResult, player, playerDetail, builder);
		}
		return builder.build();
	}

	private void doClassicWin(PlayerContext playerContext, TableResult tableResult, Player player,
			PlayerDetail playerDetail, _GamePlayerResult.Builder builder) {
		// 胜利
		playerDetail.setContinueWinCount(playerDetail.getContinueWinCount() + 1);
		// 获得的花生
		int addPeanut = tableResult.getMultiple() * tableResult.getSiteCo().getBaseScore();
		addPeanut = addPeanut + addPeanut * (tableResult.getSiteCo().getRankingAddition() / 100);
		resourceService.add(playerContext, SysAttr.PEANUT, 0, addPeanut, 1002);

		builder.setMultiple(tableResult.getMultiple());
		builder.setPeanut(addPeanut);
	}

	private void doClassicLose(PlayerContext playerContext, TableResult tableResult, Player player,
			PlayerDetail playerDetail, _GamePlayerResult.Builder builder) {
		// 失败
		playerDetail.setContinueWinCount(0);
		// 扣除的花生
		int eddPeanut = tableResult.getMultiple() * tableResult.getSiteCo().getBaseScore();
		resourceService.add(playerContext, SysAttr.PEANUT, 0, eddPeanut, 2002);

		builder.setMultiple(tableResult.getMultiple());
		builder.setPeanut(eddPeanut);
	}

	@Override
	public Map<Integer, Site> getSiteMap() {
		return siteService.getSiteMap();
	}

}
