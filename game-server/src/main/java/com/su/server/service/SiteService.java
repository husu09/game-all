package com.su.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.su.core.event.GameEventAdapter;
import com.su.excel.co.SiteCo;
import com.su.excel.map.SiteConf;

@Service
public class SiteService extends GameEventAdapter {
	
	
	@Autowired
	private SiteConf siteConf;
	
	@Override
	public void serverStart() {
		for (SiteCo siteCo : siteConf.all()) {
			
		}
	}
	
	
}
