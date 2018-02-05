package com.su.play;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecreationMode implements Mode {
	
	/**
	 * 场所
	 * */
	private Map<Integer, Site> sites;

	public RecreationMode() {
		sites = new HashMap<>();
		
		
	}
	
	/**
	 * 获取场所
	 * */
	public Site getRoom(int siteId) {
		return sites.get(siteId);
	}
	

}
