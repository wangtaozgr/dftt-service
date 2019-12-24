package com.atao.dftt.service;

import com.atao.dftt.model.PddMall;
import com.atao.dftt.model.PddUser;
import com.atao.dftt.model.PddMall;

import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import com.atao.dftt.api.vo.DataVo;
import com.atao.dftt.http.PddHttp;
import com.atao.dftt.mapper.PddMallMapper;
import com.alibaba.fastjson.JSONObject;
import com.atao.base.mapper.BaseMapper;
import com.atao.base.service.BaseService;
import com.atao.base.util.StringUtils;

import org.springframework.stereotype.Service;

import java.util.List;

import javax.annotation.Resource;

/**
 *
 * @author twang
 */
@Service
public class PddMallWyService extends BaseService<PddMall> {

	@Resource
	private PddMallMapper pddMallMapper;
	@Resource
	private PddUserWyService pddUserWyService;

	@Override
	public BaseMapper<PddMall> getMapper() {
		return pddMallMapper;
	}

	/**
	 * 查询商家
	 * 
	 * @param logoKey
	 *            主键
	 * @param mallName
	 *            多个时模糊查询
	 * @return
	 */
	public PddMall queryPddMallByLogoKey(String logoKey, String mallName) {
		if (StringUtils.isBlank(logoKey))
			return null;
		PddMall t = new PddMall();
		t.setLogoKey(logoKey);
		List<PddMall> malls = super.queryList(t, null);
		if (malls != null && malls.size() == 1)
			return malls.get(0);
		for (PddMall mall : malls) {
			if (mall.getMallName().contains(mallName))
				return mall;
		}
		if (malls != null && malls.size() > 0)
			return malls.get(0);
		else
			return null;
	}

	public PddMall queryPddMallByName(String mallName) {
		PddMall t = new PddMall();
		t.setMallName(mallName);
		List<PddMall> malls = super.queryList(t, null);
		if (malls != null && malls.size() > 0) {
			for (PddMall mall : malls) {
				if (mall.getMallName().length() == mallName.length())
					return mall;
			}
			return malls.get(0);
		}
		return null;
	}

	public void savePddMall(String mallId, String mallName, String mallImg) {
		String logoKey = getImageName(mallImg);
		PddMall pddMall = this.queryPddMallByLogoKey(logoKey, mallName);
		if (pddMall == null) {
			pddMall = new PddMall();
		}
		if (StringUtils.isNotBlank(mallId))
			pddMall.setMallId(mallId);
		pddMall.setMallLogo(mallImg);
		pddMall.setMallName(mallName);
		pddMall.setLogoKey(logoKey);
		super.save(pddMall);
	}

	public boolean saveMallName(String mallName) {
		PddMall pddMall = this.queryPddMallByName(mallName);
		if (pddMall == null) {
			PddUser mUser = pddUserWyService.queryUserByUsername(PddHttp.searchUsername);
			PddHttp mHttp = new PddHttp(mUser);
			JSONObject mallObject = mHttp.searchMallName(mallName);
			if (mallObject == null) {
				logger.error("没有找到店铺,mallName={}", mallName);
				return false;
			} else {
				this.savePddMall(mallObject.getString("mallId"), mallName, mallObject.getString("mallImg"));
			}
		}
		return true;
	}

	private String getImageName(String img) {
		if (img.indexOf(".jp") > -1) {
			img = img.substring(0, img.indexOf(".jp"));
		}
		if (img.indexOf(".pn") > -1) {
			img = img.substring(0, img.indexOf(".pn"));
		}
		img = img.replace("\\", "/");
		img = img.substring(img.lastIndexOf("/") + 1, img.length());
		return img;
	}

	@Override
	public Weekend<PddMall> genSqlExample(PddMall t) {
		Weekend<PddMall> w = super.genSqlExample(t);
		WeekendCriteria<PddMall, Object> c = w.weekendCriteria();
		if (StringUtils.isNotBlank(t.getMallId())) {
			c.andEqualTo(PddMall::getMallId, t.getMallId());
		}
		if (StringUtils.isNotBlank(t.getMallName())) {
			if (t.getMallName().endsWith("%")) {
				c.andLike(PddMall::getMallName, t.getMallName());
			} else {
				c.andEqualTo(PddMall::getMallName, t.getMallName());
			}
		}
		if (StringUtils.isNotBlank(t.getMallLogo())) {
			c.andEqualTo(PddMall::getMallLogo, t.getMallLogo());
		}
		if (StringUtils.isNotBlank(t.getLogoKey())) {
			c.andEqualTo(PddMall::getLogoKey, t.getLogoKey());
		}

		return w;
	}
}
