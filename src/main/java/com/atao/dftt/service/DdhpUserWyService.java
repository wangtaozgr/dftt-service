package com.atao.dftt.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.atao.base.mapper.BaseMapper;
import com.atao.base.service.BaseService;
import com.atao.dftt.mapper.DdhpUserMapper;
import com.atao.dftt.model.DdhpUser;
import com.atao.util.StringUtils;

import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

/**
 *
 * @author twang
 */
@Service
public class DdhpUserWyService extends BaseService<DdhpUser> {

	@Resource
	private DdhpUserMapper ddhpUserMapper;

	@Override
	public BaseMapper<DdhpUser> getMapper() {
		return ddhpUserMapper;
	}

	public List<DdhpUser> getUsedUser() {
		DdhpUser t = new DdhpUser();
		t.setUsed(true);
		List<DdhpUser> users = super.queryList(t, null);
		return users;
	}

	public DdhpUser queryByUsername(String username) {
		DdhpUser t = new DdhpUser();
		t.setUsed(true);
		t.setUsername(username);
		return super.queryOne(t, null);
	}

	@Override
	public Weekend<DdhpUser> genSqlExample(DdhpUser t) {
		Weekend<DdhpUser> w = super.genSqlExample(t);
		WeekendCriteria<DdhpUser, Object> c = w.weekendCriteria();
		if (StringUtils.isNotBlank(t.getUsername())) {
			c.andEqualTo(DdhpUser::getUsername, t.getUsername());
		}
		if (t.getUsed() != null) {
			c.andEqualTo(DdhpUser::getUsed, t.getUsed());
		}
		
		return w;
	}

}
