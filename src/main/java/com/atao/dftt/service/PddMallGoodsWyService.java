package com.atao.dftt.service;

import com.atao.dftt.model.PddMallGoods;
import com.atao.dftt.model.PddMallGoods;
import com.atao.util.StringUtils;

import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import com.atao.dftt.mapper.PddMallGoodsMapper;
import com.atao.base.mapper.BaseMapper;
import com.atao.base.service.BaseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *
 * @author twang
 */
@Service
public class PddMallGoodsWyService extends BaseService<PddMallGoods> {

	@Resource
	private PddMallGoodsMapper pddMallGoodsMapper;

	@Override
	public BaseMapper<PddMallGoods> getMapper() {
		return pddMallGoodsMapper;
	}

	public PddMallGoods queryPddMallGoodsByLogoKey(String mallId, String logoKey) {
		PddMallGoods t = new PddMallGoods();
		t.setMallId(mallId);
		t.setLogoKey(logoKey);
		return super.queryOne(t, null);
	}

	public void savePddMallGoods(String mallId, String goodsImg, String goodsId) {
		String logoKey = getImageName(goodsImg);
		PddMallGoods pddMallGoods = this.queryPddMallGoodsByLogoKey(mallId, logoKey);
		if (pddMallGoods == null) {
			pddMallGoods = new PddMallGoods();
		}
		if (StringUtils.isNotBlank(goodsId))
			pddMallGoods.setGoodsId(goodsId);
		pddMallGoods.setGoodsLogo(goodsImg);
		pddMallGoods.setMallId(mallId);
		pddMallGoods.setLogoKey(logoKey);
		super.save(pddMallGoods);
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
	public Weekend<PddMallGoods> genSqlExample(PddMallGoods t) {
		Weekend<PddMallGoods> w = super.genSqlExample(t);
		WeekendCriteria<PddMallGoods, Object> c = w.weekendCriteria();
		if (StringUtils.isNotBlank(t.getMallId())) {
			c.andEqualTo(PddMallGoods::getMallId, t.getMallId());
		}
		if (StringUtils.isNotBlank(t.getGoodsId())) {
			c.andEqualTo(PddMallGoods::getGoodsId, t.getGoodsId());
		}
		if (StringUtils.isNotBlank(t.getGoodsLogo())) {
			c.andEqualTo(PddMallGoods::getGoodsLogo, t.getGoodsLogo());
		}
		if (StringUtils.isNotBlank(t.getLogoKey())) {
			c.andEqualTo(PddMallGoods::getLogoKey, t.getLogoKey());
		}
		w.and(c);
		return w;
	}
}
