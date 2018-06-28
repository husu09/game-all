package com.su.excel.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.su.common.constant.EffectConst;
import com.su.common.util.ParseUtil;
import com.su.common.util.TimeUtil;
import com.su.config.PropesCo;
import com.su.excel.core.AbstractExcelMapper;
import com.su.excel.core.RowData;

@Component
public class PropesMapper extends AbstractExcelMapper<PropesCo> {

	@Autowired
	private ParameterMapper parameterMapper;

	@Override
	public String getName() {
		return "D道具";
	}

	@Override
	public PropesCo map(RowData rowData) {
		PropesCo temp = new PropesCo();
		temp.setId(rowData.getInt("id"));
		temp.setType(rowData.getInt("lx"));
		temp.setQuality(rowData.getInt("pz"));
		temp.setUseType(rowData.getInt("sylx"));
		if (temp.getUseType() == EffectConst.VALUE) {
			temp.setEffectNum(rowData.getInt("syxg"));
		} else if (temp.getUseType() == EffectConst.RESOURCES) {
			temp.setEffectItem(rowData.getGoods("syxg"));
		}
		temp.setExpirationTime(rowData.getInt("yxq") * TimeUtil.ONE_DAY);
		int limit = rowData.getInt("djsx");
		limit = limit == 0 ? ParseUtil.getInt(parameterMapper.get(1).getValue()) : limit;
		temp.setLimit(limit);
		return temp;
	}

}
