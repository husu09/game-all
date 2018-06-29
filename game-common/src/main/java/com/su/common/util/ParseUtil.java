package com.su.common.util;

import com.su.common.obj.Goods;
import com.su.common.obj.KV;

public class ParseUtil {

	public static int getInt(String value) {
		if (StringUtil.isNone(value))
			return 0;
		return Integer.parseInt(value);
	}

	public static boolean getBoolean(String value) {
		if (ParseUtil.getInt(value) > 0)
			return true;
		return false;
	}

	public static Goods getGoods(String value) {
		String[] splits = value.split("_");
		Goods goods = new Goods();
		goods.setType(Integer.parseInt(splits[0]));
		goods.setSysId(Integer.parseInt(splits[1]));
		goods.setCount(Integer.parseInt(splits[2]));
		return goods;
	}

	public static KV<Integer> getKVI(String value) {
		String[] splits = value.split("_");
		KV<Integer> kv = new KV<>();
		kv.setKey(Integer.parseInt(splits[0]));
		kv.setValue(Integer.parseInt(splits[1]));
		return kv;
	}

	public static KV<String> getKVS(String value) {
		String[] splits = value.split("_");
		KV<String> kv = new KV<>();
		kv.setKey(Integer.parseInt(splits[0]));
		kv.setValue(splits[1]);
		return kv;
	}

	public static Integer[] getIntArr(String value) {
		String[] splits = value.split("_");
		Integer[] arr = null;
		for (int i = 0; i < splits.length; i++) {
			if (arr == null)
				arr = new Integer[splits.length];
			arr[i] = Integer.parseInt(splits[i]);
		}
		return arr;
	}
}
