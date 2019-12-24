package com.atao.dftt.service;

import com.atao.dftt.model.CoinTxRecord;
import com.atao.dftt.model.CoinTxRecord;

import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import com.atao.dftt.mapper.CoinTxRecordMapper;
import com.atao.base.mapper.BaseMapper;
import com.atao.base.service.BaseService;
import com.atao.base.util.StringUtils;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *
 * @author twang
 */
@Service
public class CoinTxRecordWyService extends BaseService<CoinTxRecord> {

	@Resource
	private CoinTxRecordMapper coinTxRecordMapper;

	@Override
	public BaseMapper<CoinTxRecord> getMapper() {
		return coinTxRecordMapper;
	}
	
	@Override
	public Weekend<CoinTxRecord> genSqlExample(CoinTxRecord t) {
		Weekend<CoinTxRecord> w = super.genSqlExample(t);
		WeekendCriteria<CoinTxRecord, Object> c = w.weekendCriteria();
		if (StringUtils.isNotBlank(t.getUsername())) {
			c.andEqualTo(CoinTxRecord::getUsername, t.getUsername());
		}
		if (StringUtils.isNotBlank(t.getType())) {
			c.andEqualTo(CoinTxRecord::getType, t.getType());
		}
		
		return w;
	}
}
