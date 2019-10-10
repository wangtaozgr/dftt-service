package com.atao.dftt.service;

import com.atao.dftt.model.PddMall;
import com.atao.dftt.model.PddMall;

import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import com.atao.dftt.mapper.PddMallMapper;
import com.atao.base.mapper.BaseMapper;
import com.atao.base.service.BaseService;
import com.atao.base.util.StringUtils;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *
 * @author twang
 */
@Service
public class PddMallWyService extends BaseService<PddMall> {

	@Resource
	private PddMallMapper pddMallMapper;

	@Override
	public BaseMapper<PddMall> getMapper() {
		return pddMallMapper;
	}

	public PddMall queryPddMallByLogoKey(String logoKey) {
		PddMall t = new PddMall();
		t.setLogoKey(logoKey);
		return super.queryOne(t, null);
	}

	public void savePddMall(String mallId, String mallName, String mallImg) {
		String logoKey = getImageName(mallImg);
		PddMall pddMall = this.queryPddMallByLogoKey(logoKey);
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
			c.andEqualTo(PddMall::getMallName, t.getMallName());
		}
		if (StringUtils.isNotBlank(t.getMallLogo())) {
			c.andEqualTo(PddMall::getMallLogo, t.getMallLogo());
		}
		if (StringUtils.isNotBlank(t.getLogoKey())) {
			c.andEqualTo(PddMall::getLogoKey, t.getLogoKey());
		}
		w.and(c);
		return w;
	}
}
