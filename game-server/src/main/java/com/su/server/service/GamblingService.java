package com.su.server.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.su.config.FieldCo;
import com.su.core.event.GameEventAdapter;
import com.su.core.gambling.Site;
import com.su.excel.mapper.FieldMapper;

@Service
public class GamblingService extends GameEventAdapter {

	/**
	 * 所有场
	 */
	private Map<Integer, Site> siteMap = new HashMap<>();

	@Autowired
	private FieldMapper siteConf;

	@Override
	public void serverStart() {
		/**
		 * 初始化所有场
		 */
		for (FieldCo siteCo : siteConf.all()) {
			if (siteCo.isOpen()) {
				Site site = new Site(siteCo);
				siteMap.put(siteCo.getId(), site);
			}
		}
	}

	public Map<Integer, Site> getSiteMap() {
		return siteMap;
	}

}
