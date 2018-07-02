package com.su.server.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.su.core.akka.BridgeService;
import com.su.core.game.Site;
import com.su.core.game.TableResult;
import com.su.msg.TableMsg._GamePlayerResult;

@Service
public class BridgeServiceImpl implements BridgeService {

	@Autowired
	private SiteService siteService;
	@Autowired
	private PlayerGameService playerGameService;


	@Override
	public Map<Integer, Site> getSiteMap() {
		return siteService.getSiteMap();
	}


	@Override
	public _GamePlayerResult doTableResult(TableResult tableResult) {
		return playerGameService.doTableResult(tableResult);
	}


	@Override
	public void doContestClose(int ranking) {
		playerGameService.doContestClose(ranking);
	}

}
