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
import com.atao.dftt.http.JkdttHttp;
import com.atao.dftt.mapper.JkdttCoinRecordMapper;
import com.atao.dftt.model.JkdttCoinRecord;
import com.atao.dftt.model.JkdttUser;
import com.atao.util.DateUtils;

import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

/**
 *
 * @author twang
 */
@Service
public class JkdttCoinRecordWyService extends BaseService<JkdttCoinRecord> {

	@Resource
	private JkdttCoinRecordMapper jkdttCoinRecordMapper;
	@Resource
	private JkdttUserWyService jkdttUserWyService;

	@Override
	public BaseMapper<JkdttCoinRecord> getMapper() {
		return jkdttCoinRecordMapper;
	}

	public void updateCoin(JkdttUser user) {
		JkdttHttp http = JkdttHttp.getInstance(user);
		String coinDay = DateUtils.formatDate(new Date(), "yyyyMMdd");
		JkdttCoinRecord record = new JkdttCoinRecord();
		record.setCoinDay(coinDay);
		record.setUsername(user.getUsername());
		JSONObject object = http.queryMyCoin();
		if ("ok".equals(object.getString("ret"))) {
			logger.info("jkdtt-{}:查询今日获取的金币信息成功.", user.getUsername());
			JSONObject result = object.getJSONObject("datas").getJSONObject("userInfo").getJSONObject("account");
			record.setTodayCoin(result.getIntValue("gold"));
			record.setBalance(result.getDoubleValue("money"));
			record.setTotalIncome(result.getDoubleValue("money"));
			record.setUpdateTime(new Date());
			super.save(record);
		}
	}

	public void updateAllCoin() {
		List<JkdttUser> users = jkdttUserWyService.getUsedUser();
		for (JkdttUser user : users) {
			updateCoin(user);
		}
	}

	public MyPage<JkdttCoinRecord> queryMyCoinRecord(String username) {
		JkdttCoinRecord t = new JkdttCoinRecord();
		t.setRows(3);
		t.setUsername(username);
		String coinDay = DateUtils.formatDate(new Date(), "yyyyMMdd");
		t.setCoinDay(coinDay);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sorts", "updateTime=1");
		return super.queryPage(t, params);
	}

	@Override
	public Weekend<JkdttCoinRecord> genSqlExample(JkdttCoinRecord t) {
		Weekend<JkdttCoinRecord> w = super.genSqlExample(t);
		WeekendCriteria<JkdttCoinRecord, Object> c = w.weekendCriteria();
		if (StringUtils.isNotBlank(t.getUsername())) {
			c.andEqualTo(JkdttCoinRecord::getUsername, t.getUsername());
		}
		if (StringUtils.isNotBlank(t.getCoinDay())) {
			c.andEqualTo(JkdttCoinRecord::getCoinDay, t.getCoinDay());
		}
		
		return w;
	}
}
