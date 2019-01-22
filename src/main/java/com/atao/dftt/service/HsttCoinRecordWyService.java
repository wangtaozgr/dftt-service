package com.atao.dftt.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.atao.base.mapper.BaseMapper;
import com.atao.base.service.BaseService;
import com.atao.base.support.MyPage;
import com.atao.base.util.StringUtils;
import com.atao.dftt.http.HsttHttp;
import com.atao.dftt.mapper.HsttCoinRecordMapper;
import com.atao.dftt.model.HsttCoinRecord;
import com.atao.dftt.model.HsttUser;
import com.atao.util.DateUtils;

import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

/**
 *
 * @author twang
 */
@Service
public class HsttCoinRecordWyService extends BaseService<HsttCoinRecord> {

	@Resource
	private HsttCoinRecordMapper hsttCoinRecordMapper;
	@Resource
	private HsttUserWyService hsttUserWyService;

	@Override
	public BaseMapper<HsttCoinRecord> getMapper() {
		return hsttCoinRecordMapper;
	}

	public void updateCoin(HsttUser user) {
		HsttHttp http = HsttHttp.getInstance(user);
		String coinDay = DateUtils.formatDate(new Date(), "yyyyMMdd");
		HsttCoinRecord record = new HsttCoinRecord();
		record.setCoinDay(coinDay);
		record.setUsername(user.getUsername());
		JSONObject object = http.queryMyCoin();
		if (object.getIntValue("status")==100) {
			logger.info("hstt-{}:查询今日获取的金币信息成功.", user.getUsername());
			record.setTodayCoin(object.getIntValue("todayG"));
			record.setBalance(object.getDoubleValue("balanceR"));
			record.setTotalIncome(object.getDoubleValue("todayG"));
			record.setUpdateTime(new Date());
			super.save(record);
		}
	}

	public void updateAllCoin() {
		List<HsttUser> users = hsttUserWyService.getUsedUser();
		for (HsttUser user : users) {
			updateCoin(user);
		}
	}

	public MyPage<HsttCoinRecord> queryMyCoinRecord(String username) {
		HsttCoinRecord t = new HsttCoinRecord();
		t.setRows(3);
		t.setUsername(username);
		String coinDay = DateUtils.formatDate(new Date(), "yyyyMMdd");
		t.setCoinDay(coinDay);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sorts", "updateTime=1");
		return super.queryPage(t, params);
	}

	@Override
	public Weekend<HsttCoinRecord> genSqlExample(HsttCoinRecord t) {
		Weekend<HsttCoinRecord> w = super.genSqlExample(t);
		WeekendCriteria<HsttCoinRecord, Object> c = w.weekendCriteria();
		if (StringUtils.isNotBlank(t.getUsername())) {
			c.andEqualTo(HsttCoinRecord::getUsername, t.getUsername());
		}
		if (StringUtils.isNotBlank(t.getCoinDay())) {
			c.andEqualTo(HsttCoinRecord::getCoinDay, t.getCoinDay());
		}
		w.and(c);
		return w;
	}

}
