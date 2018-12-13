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
import com.atao.dftt.http.TaottHttp;
import com.atao.dftt.mapper.TaottCoinRecordMapper;
import com.atao.dftt.model.TaoToutiaoUser;
import com.atao.dftt.model.TaottCoinRecord;
import com.atao.util.DateUtils;

import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

/**
 *
 * @author twang
 */
@Service
public class TaottCoinRecordWyService extends BaseService<TaottCoinRecord> {

	@Resource
	private TaottCoinRecordMapper taottCoinRecordMapper;

	@Resource
	private TaoToutiaoUserWyService taoToutiaoUserWyService;

	@Override
	public BaseMapper<TaottCoinRecord> getMapper() {
		return taottCoinRecordMapper;
	}

	public void updateCoin(TaoToutiaoUser user) {
		TaottHttp http = TaottHttp.getInstance(user);
		String coinDay = DateUtils.formatDate(new Date(), "yyyyMMdd");
		TaottCoinRecord record = new TaottCoinRecord();
		record.setCoinDay(coinDay);
		record.setUsername(user.getUsername());
		JSONObject object = http.queryMyCoin();
		if (object != null) {
			record.setTodayCoin(object.getIntValue("todayGold"));
			record.setBalance(object.getDoubleValue("balance") / 100);
			record.setTotalIncome(object.getDoubleValue("totalIncome") / 100);
			record.setUpdateTime(new Date());
			super.save(record);
		}
	}

	public void updateAllCoin() {
		List<TaoToutiaoUser> users = taoToutiaoUserWyService.getUsedUser();
		for (TaoToutiaoUser user : users) {
			updateCoin(user);
		}
	}

	public MyPage<TaottCoinRecord> queryMyCoinRecord(String username) {
		TaottCoinRecord t = new TaottCoinRecord();
		t.setRows(3);
		t.setUsername(username);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sorts", "updateTime=1");
		return super.queryPage(t, params);
	}

	@Override
	public Weekend<TaottCoinRecord> genSqlExample(TaottCoinRecord t) {
		Weekend<TaottCoinRecord> w = super.genSqlExample(t);
		WeekendCriteria<TaottCoinRecord, Object> c = w.weekendCriteria();
		if (StringUtils.isNotBlank(t.getUsername())) {
			c.andEqualTo(TaottCoinRecord::getUsername, t.getUsername());
		}
		if (StringUtils.isNotBlank(t.getCoinDay())) {
			c.andEqualTo(TaottCoinRecord::getCoinDay, t.getCoinDay());
		}
		w.and(c);
		return w;
	}

}
