package com.su.excel.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.su.common.constant.EffectConst;
import com.su.common.util.ParseUtil;
import com.su.common.util.TimeUtil;
import com.su.config.BagCo;
import com.su.excel.core.AbstractExcelMapper;
import com.su.excel.core.RowData;

@Component
public class BagMapper extends AbstractExcelMapper<BagCo> {

	@Autowired
	private ParameterMapper parameterMapper;

	@Override
	public String getName() {
		return "D道具";
	}

	@Override
	public BagCo map(RowData rowData) {
		BagCo temp = new BagCo();
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
		int limit = rowData.getInt("djsx");
		limit = limit == 0 ? ParseUtil.getInt(parameterMapper.get(1).getValue()) : limit;
		temp.setLimit(limit);
		return temp;
	}

}
