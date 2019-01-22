package com.atao.dftt.service;

import com.atao.dftt.model.ZqttCoinRecord;
import com.atao.dftt.model.ZqttUser;
import com.atao.dftt.model.ZqttCoinRecord;
import com.atao.util.DateUtils;

import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import com.atao.dftt.http.ZqttHttp;
import com.atao.dftt.mapper.ZqttCoinRecordMapper;
import com.alibaba.fastjson.JSONObject;
import com.atao.base.mapper.BaseMapper;
import com.atao.base.service.BaseService;
import com.atao.base.support.MyPage;
import com.atao.base.util.StringUtils;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

/**
 *
 * @author twang
 */
@Service
public class ZqttCoinRecordWyService extends BaseService<ZqttCoinRecord> {

	@Resource
	private ZqttCoinRecordMapper zqttCoinRecordMapper;
	@Resource
	private ZqttUserWyService zqttUserWyService;

	@Override
	public BaseMapper<ZqttCoinRecord> getMapper() {
		return zqttCoinRecordMapper;
	}

	public void updateCoin(ZqttUser user) {
		ZqttHttp http = ZqttHttp.getInstance(user);
		String coinDay = DateUtils.formatDate(new Date(), "yyyyMMdd");
		ZqttCoinRecord record = new ZqttCoinRecord();
		record.setCoinDay(coinDay);
		record.setUsername(user.getUsername());
		JSONObject object = http.queryMyCoin();
		logger.info("zqtt-{}:查询今日获取的金币信息成功.", user.getUsername());
		record.setTodayCoin(object.getIntValue("today_score"));
		record.setBalance(object.getDoubleValue("money"));
		record.setTotalIncome(object.getDoubleValue("score")/10000);
		record.setUpdateTime(new Date());
		super.save(record);
	}

	public void updateAllCoin() {
		List<ZqttUser> users = zqttUserWyService.getUsedUser();
		for (ZqttUser user : users) {
			updateCoin(user);
		}
	}

	public MyPage<ZqttCoinRecord> queryMyCoinRecord(String username) {
		ZqttCoinRecord t = new ZqttCoinRecord();
		t.setRows(3);
		t.setUsername(username);
		String coinDay = DateUtils.formatDate(new Date(), "yyyyMMdd");
		t.setCoinDay(coinDay);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sorts", "updateTime=1");
		return super.queryPage(t, params);
	}

	public ZqttCoinRecord queryTodayMyCoin(String username) {
		ZqttCoinRecord t = new ZqttCoinRecord();
		t.setUsername(username);
		String coinDay = DateUtils.formatDate(new Date(), "yyyyMMdd");
		t.setCoinDay(coinDay);
		return super.queryOne(t, null);
	}

	@Override
	public Weekend<ZqttCoinRecord> genSqlExample(ZqttCoinRecord t) {
		Weekend<ZqttCoinRecord> w = super.genSqlExample(t);
		WeekendCriteria<ZqttCoinRecord, Object> c = w.weekendCriteria();
		if (StringUtils.isNotBlank(t.getUsername())) {
			c.andEqualTo(ZqttCoinRecord::getUsername, t.getUsername());
		}
		if (StringUtils.isNotBlank(t.getCoinDay())) {
			c.andEqualTo(ZqttCoinRecord::getCoinDay, t.getCoinDay());
		}
		w.and(c);
		return w;
	}
}
