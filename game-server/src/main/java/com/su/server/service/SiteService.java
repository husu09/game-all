package com.su.server.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.su.excel.co.SiteCo;
import com.su.excel.map.SiteConf;
import com.su.server.event.GameEventAdapter;
import com.su.server.obj.play.Site;

@Service
public class SiteService extends GameEventAdapter {

	/**
	 * 所有场
	 */
	private Map<Integer, Site> siteMap = new HashMap<>();

	@Autowired
	private SiteConf siteConf;

	@Override
	public void serverStart() {
		/**
		 * 初始化所有场
		 */
		for (SiteCo siteCo : siteConf.all()) {
			if (siteCo.isOpen()) {
				Site site = new Site(siteCo);
				siteMap.put(siteCo.getId(), site);
			}
		}
	}

}
