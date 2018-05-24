package com.su.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.su.common.po.PlayerDetail;

public class JsonTest {
	
	@Test
	public void fastJsonTest() {
		/**
		 * FastJson 序列化对象时：
		 * 1. 默认不包含为 Null 的字段
		 * 2. 默认不包含没有 get 方法的字段
		 * */
		PlayerDetail playerDetail = new PlayerDetail(0);
		//playerDetail.setGridList( new ArrayList<>());
		//playerDetail.setBagData("bagData");
		String str = JSON.toJSONString(playerDetail);
		System.out.println(str);
		
		Map<String, Object> map = new HashMap<>();
		map.put("bagData", "bagData");
		map.put("gridList",  new ArrayList<>());
		String str2 = JSON.toJSONString(playerDetail);
		System.out.println(str2);
		
	}
	
	
}
