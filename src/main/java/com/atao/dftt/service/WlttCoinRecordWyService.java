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
import com.atao.dftt.http.WlttHttp;
import com.atao.dftt.mapper.WlttCoinRecordMapper;
import com.atao.dftt.model.Wltt;
import com.atao.dftt.model.WlttCoinRecord;
import com.atao.util.DateUtils;

import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

/**
 *
 * @author twang
 */
@Service
public class WlttCoinRecordWyService extends BaseService<WlttCoinRecord> {

	@Resource
	private WlttCoinRecordMapper wlttCoinRecordMapper;
	@Resource
	private WlttWyService wlttWyService;

	@Override
	public BaseMapper<WlttCoinRecord> getMapper() {
		return wlttCoinRecordMapper;
	}

	public void updateCoin(Wltt user) {
		WlttHttp http = WlttHttp.getInstance(user);
		String coinDay = DateUtils.formatDate(new Date(), "yyyyMMdd");
		WlttCoinRecord record = new WlttCoinRecord();
		record.setCoinDay(coinDay);
		record.setUsername(user.getUsername());
		JSONObject object = http.queryMyCoin();
		if (object != null) {
			record.setTodayCoin(object.getIntValue("today_gold"));
			record.setBalance(object.getDoubleValue("money_balance"));
			record.setTotalIncome(object.getDoubleValue("money_balance"));
			record.setUpdateTime(new Date());
			super.save(record);
		}
	}

	public void updateAllCoin() {
		List<Wltt> users = wlttWyService.getUsedUser();
		for (Wltt user : users) {
			updateCoin(user);
		}
	}

	public MyPage<WlttCoinRecord> queryMyCoinRecord(String username) {
		WlttCoinRecord t = new WlttCoinRecord();
		t.setRows(3);
		t.setUsername(username);
		String coinDay = DateUtils.formatDate(new Date(), "yyyyMMdd");
		t.setCoinDay(coinDay);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sorts", "updateTime=1");
		return super.queryPage(t, params);
	}

	@Override
	public Weekend<WlttCoinRecord> genSqlExample(WlttCoinRecord t) {
		Weekend<WlttCoinRecord> w = super.genSqlExample(t);
		WeekendCriteria<WlttCoinRecord, Object> c = w.weekendCriteria();
		if (StringUtils.isNotBlank(t.getUsername())) {
			c.andEqualTo(WlttCoinRecord::getUsername, t.getUsername());
		}
		if (StringUtils.isNotBlank(t.getCoinDay())) {
			c.andEqualTo(WlttCoinRecord::getCoinDay, t.getCoinDay());
		}
		w.and(c);
		return w;
	}
}
