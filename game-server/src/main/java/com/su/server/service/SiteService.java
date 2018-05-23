package com.su.server.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.su.core.event.GameEventAdapter;
import com.su.excel.mapper.SiteMapper;
import com.su.server.obj.play.Site;

@Service
public class SiteService extends GameEventAdapter {

	/**
	 * 所有场
	 */
	private Map<Integer, Site> siteMap = new HashMap<>();

	@Autowired
	private SiteMapper siteConf;

	@Override
	public void serverStart() {
		/**
		 * 初始化所有场
		 */
		/*for (SiteCo siteCo : siteConf.all()) {
			if (siteCo.isOpen()) {
				Site site = new Site(siteCo);
				siteMap.put(siteCo.getId(), site);
			}
		}*/
	}

}
