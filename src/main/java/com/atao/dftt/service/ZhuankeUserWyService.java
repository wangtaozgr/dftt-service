package com.atao.dftt.service;

import com.atao.dftt.model.ZhuankeUser;
import com.atao.dftt.model.ZhuankeUser;
import com.atao.util.StringUtils;

import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import com.atao.dftt.mapper.ZhuankeUserMapper;
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
public class ZhuankeUserWyService extends BaseService<ZhuankeUser> {

	@Resource
	private ZhuankeUserMapper zhuankeUserMapper;

	@Override
	public BaseMapper<ZhuankeUser> getMapper() {
		return zhuankeUserMapper;
	}

	public List<ZhuankeUser> getUsedUser() {
		ZhuankeUser t = new ZhuankeUser();
		t.setUsed(true);
		List<ZhuankeUser> users = super.queryList(t, null);
		return users;
	}

	public ZhuankeUser queryByUsername(String username) {
		ZhuankeUser t = new ZhuankeUser();
		t.setUsed(true);
		t.setUsername(username);
		return super.queryOne(t, null);
	}

	@Override
	public Weekend<ZhuankeUser> genSqlExample(ZhuankeUser t) {
		Weekend<ZhuankeUser> w = super.genSqlExample(t);
		WeekendCriteria<ZhuankeUser, Object> c = w.weekendCriteria();
		if (StringUtils.isNotBlank(t.getUsername())) {
			c.andEqualTo(ZhuankeUser::getUsername, t.getUsername());
		}
		if (t.getUsed() != null) {
			c.andEqualTo(ZhuankeUser::getUsed, t.getUsed());
		}
		return w;
	}
}
