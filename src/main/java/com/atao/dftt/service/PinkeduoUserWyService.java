package com.atao.dftt.service;

import com.atao.dftt.model.PinkeduoUser;
import com.atao.dftt.model.PinkeduoUser;
import com.atao.util.StringUtils;

import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import com.atao.dftt.mapper.PinkeduoUserMapper;
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
public class PinkeduoUserWyService extends BaseService<PinkeduoUser> {

	@Resource
	private PinkeduoUserMapper pinkeduoUserMapper;

	@Override
	public BaseMapper<PinkeduoUser> getMapper() {
		return pinkeduoUserMapper;
	}

	public List<PinkeduoUser> getUsedUser() {
		PinkeduoUser t = new PinkeduoUser();
		t.setUsed(true);
		List<PinkeduoUser> users = super.queryList(t, null);
		return users;
	}

	public PinkeduoUser queryByUsername(String username) {
		PinkeduoUser t = new PinkeduoUser();
		t.setUsed(true);
		t.setUsername(username);
		return super.queryOne(t, null);
	}

	@Override
	public Weekend<PinkeduoUser> genSqlExample(PinkeduoUser t) {
		Weekend<PinkeduoUser> w = super.genSqlExample(t);
		WeekendCriteria<PinkeduoUser, Object> c = w.weekendCriteria();
		if (StringUtils.isNotBlank(t.getUsername())) {
			c.andEqualTo(PinkeduoUser::getUsername, t.getUsername());
		}
		if (t.getUsed() != null) {
			c.andEqualTo(PinkeduoUser::getUsed, t.getUsed());
		}
		
		return w;
	}
}
