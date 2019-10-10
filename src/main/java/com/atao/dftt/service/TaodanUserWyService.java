package com.atao.dftt.service;

import com.atao.dftt.model.TaodanUser;
import com.atao.dftt.model.TaodanUser;
import com.atao.util.StringUtils;

import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import com.atao.dftt.mapper.TaodanUserMapper;
import com.atao.base.mapper.BaseMapper;
import com.atao.base.service.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;

import javax.annotation.Resource;

/**
 *
 * @author twang
 */
@Service
public class TaodanUserWyService extends BaseService<TaodanUser> {

	@Resource
	private TaodanUserMapper taodanUserMapper;

	@Override
	public BaseMapper<TaodanUser> getMapper() {
		return taodanUserMapper;
	}

	public List<TaodanUser> getUsedUser() {
		TaodanUser t = new TaodanUser();
		t.setUsed(true);
		List<TaodanUser> users = super.queryList(t, null);
		return users;
	}

	public TaodanUser queryByUsername(String username) {
		TaodanUser t = new TaodanUser();
		t.setUsed(true);
		t.setUsername(username);
		return super.queryOne(t, null);
	}

	@Override
	public Weekend<TaodanUser> genSqlExample(TaodanUser t) {
		Weekend<TaodanUser> w = super.genSqlExample(t);
		WeekendCriteria<TaodanUser, Object> c = w.weekendCriteria();
		if (StringUtils.isNotBlank(t.getUsername())) {
			c.andEqualTo(TaodanUser::getUsername, t.getUsername());
		}
		if (t.getUsed() != null) {
			c.andEqualTo(TaodanUser::getUsed, t.getUsed());
		}
		w.and(c);
		return w;
	}
}
