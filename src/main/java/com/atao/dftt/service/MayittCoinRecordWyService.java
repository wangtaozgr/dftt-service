package com.atao.dftt.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.atao.base.mapper.BaseMapper;
import com.atao.base.service.BaseService;
import com.atao.base.support.MyPage;
import com.atao.base.util.StringUtils;
import com.atao.dftt.http.MayittHttp;
import com.atao.dftt.mapper.MayittCoinRecordMapper;
import com.atao.dftt.model.MayittCoinRecord;
import com.atao.dftt.model.MayittUser;
import com.atao.util.DateUtils;

import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

/**
 *
 * @author twang
 */
@Service
public class MayittCoinRecordWyService extends BaseService<MayittCoinRecord> {

	@Resource
	private MayittCoinRecordMapper mayittCoinRecordMapper;
	@Resource
	private MayittUserWyService mayittUserWyService;

	@Override
	public BaseMapper<MayittCoinRecord> getMapper() {
		return mayittCoinRecordMapper;
	}

	public void updateCoin(MayittUser user) {
		MayittHttp http = MayittHttp.getInstance(user);
		String coinDay = DateUtils.formatDate(new Date(), "yyyyMMdd");
		MayittCoinRecord record = new MayittCoinRecord();
		record.setCoinDay(coinDay);
		record.setUsername(user.getUsername());
		JSONObject object = http.queryMyCoin();
		logger.info("mayitt-{}:查询今日获取的金币信息成功.", user.getUsername());
		record.setTodayCoin(object.getIntValue("today_score"));
		record.setBalance(object.getDoubleValue("money"));
		record.setTotalIncome(object.getDoubleValue("total_money"));
		record.setUpdateTime(new Date());
		super.save(record);
	}

	public void updateAllCoin() {
		List<MayittUser> users = mayittUserWyService.getUsedUser();
		for (MayittUser user : users) {
			updateCoin(user);
		}
	}

	public MyPage<MayittCoinRecord> queryMyCoinRecord(String username) {
		MayittCoinRecord t = new MayittCoinRecord();
		t.setRows(3);
		t.setUsername(username);
		String coinDay = DateUtils.formatDate(new Date(), "yyyyMMdd");
		t.setCoinDay(coinDay);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sorts", "updateTime=1");
		return super.queryPage(t, params);
	}

	public MayittCoinRecord queryTodayMyCoin(String username) {
		MayittCoinRecord t = new MayittCoinRecord();
		t.setUsername(username);
		String coinDay = DateUtils.formatDate(new Date(), "yyyyMMdd");
		t.setCoinDay(coinDay);
		return super.queryOne(t, null);
	}

	@Override
	public Weekend<MayittCoinRecord> genSqlExample(MayittCoinRecord t) {
		Weekend<MayittCoinRecord> w = super.genSqlExample(t);
		WeekendCriteria<MayittCoinRecord, Object> c = w.weekendCriteria();
		if (StringUtils.isNotBlank(t.getUsername())) {
			c.andEqualTo(MayittCoinRecord::getUsername, t.getUsername());
		}
		if (StringUtils.isNotBlank(t.getCoinDay())) {
			c.andEqualTo(MayittCoinRecord::getCoinDay, t.getCoinDay());
		}
		w.and(c);
		return w;
	}
}
