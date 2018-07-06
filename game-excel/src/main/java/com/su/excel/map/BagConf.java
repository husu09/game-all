package com.su.excel.map;

import org.springframework.stereotype.Component;

import com.su.common.constant.EffectConst;
import com.su.common.util.TimeUtil;
import com.su.excel.config.BagConfig;
import com.su.excel.core.AbstractExcelMapper;
import com.su.excel.core.RowData;

@Component
public class BagConf extends AbstractExcelMapper<BagConfig> {

	@Override
	public String getName() {
		return "D道具";
	}

	@Override
	public BagConfig map(RowData rowData) {
		BagConfig temp = new BagConfig();
		temp.setId(rowData.getInt("id"));
		temp.setType(rowData.getInt("lx"));
		temp.setQuality(rowData.getInt("pz"));
		temp.setUseType(rowData.getInt("sylx"));
		if (temp.getUseType() == EffectConst.VALUE) {
			temp.setEffectNum(rowData.getInt("syxg"));
		} else if (temp.getUseType() == EffectConst.RESOURCES) {
			temp.setEffectItem(rowData.getItem("syxg"));
		}
		temp.setExpirationTime(rowData.getInt("yxq") * TimeUtil.ONE_DAY);
		temp.setLimit(rowData.getInt("djsx"));
		return temp;
	}

}
