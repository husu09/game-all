package com.su.server.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.su.config.SiteCo;
import com.su.core.event.GameEventAdapter;
import com.su.core.game.ClassicSite;
import com.su.core.game.Site;
import com.su.excel.mapper.SiteConf;

@Service
public class SiteService extends GameEventAdapter {

	private Map<Integer, Site> siteMap = new HashMap<>();
	

	@Autowired
	private SiteConf siteConf;

	@Override
	public void serverStart() {
		// 匹配场
		/*for (SiteCo siteCo : siteConf.all()) {
			if (siteCo.isOpen()) {
				ClassicSite matchSite = new ClassicSite(siteCo);
				siteMap.put(siteCo.getId(), matchSite);
			}
		}*/
		// 比赛场
	}

	public Map<Integer, Site> getSiteMap() {
		return siteMap;
	}

}
