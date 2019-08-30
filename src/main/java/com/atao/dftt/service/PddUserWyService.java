package com.atao.dftt.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.atao.base.mapper.BaseMapper;
import com.atao.base.service.BaseService;
import com.atao.base.util.StringUtils;
import com.atao.dftt.api.vo.DataVo;
import com.atao.dftt.http.PddHttp;
import com.atao.dftt.mapper.PddUserMapper;
import com.atao.dftt.model.PddUser;

import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

/**
 *
 * @author twang
 */
@Service
public class PddUserWyService extends BaseService<PddUser> {

	@Resource
	private PddUserMapper pddUserMapper;

	@Override
	public BaseMapper<PddUser> getMapper() {
		return pddUserMapper;
	}

	public List<PddUser> getUsedUser() {
		PddUser dftt = new PddUser();
		dftt.setUsed(true);
		List<PddUser> users = super.queryList(dftt, null);
		return users;
	}

	public PddUser queryUserByUsername(String username) {
		PddUser p = new PddUser();
		p.setUsername(username);
		return super.queryOne(p, null);
	}
	
	public DataVo createOrder(String username, String goodsId, float price) {
		DataVo result = new DataVo();
		PddUser user = queryUserByUsername(username);
		logger.info("pdd-{}:开始查询商品。", user.getUsername());
		PddHttp http = PddHttp.getInstance(user);
		JSONObject productDetail = http.productDetail(Long.valueOf(goodsId));
		JSONObject goods = productDetail.getJSONObject("goods");
		String groupId = goods.getJSONArray("group").getJSONObject(1).getString("group_id");
		JSONArray skus = productDetail.getJSONArray("sku");
		String activityId = productDetail.getJSONObject("activity_collection").getJSONObject("activity").getString("activity_id");
		for (int j = 0; j < skus.size(); j++) {
			JSONObject sku = skus.getJSONObject(j);
			if (sku.getIntValue("group_price") == price*100) {
				String skuId = sku.getString("sku_id");
				JSONObject order = http.submitAppOrder(activityId, groupId, skuId, goodsId);
				result = new DataVo(1, "生成订单成功", order);
				return result;
			}
		}		
		result = new DataVo(0, "生成订单失败", null);
		return result;
	}
	
	public JSONObject queryOrderDetail(String username, String orderSn) {
		PddUser user = queryUserByUsername(username);
		logger.info("pdd-{}:开始查询商品。", user.getUsername());
		PddHttp http = PddHttp.getInstance(user);
		JSONObject order = http.queryOrderDetail(orderSn);
		return order;
		
	}

	@Override
	public Weekend<PddUser> genSqlExample(PddUser t) {
		Weekend<PddUser> w = super.genSqlExample(t);
		WeekendCriteria<PddUser, Object> c = w.weekendCriteria();
		if (StringUtils.isNotBlank(t.getUsername())) {
			c.andEqualTo(PddUser::getUsername, t.getUsername());
		}
		if (t.getUsed() != null) {
			c.andEqualTo(PddUser::getUsed, t.getUsed());
		}
		w.and(c);
		return w;
	}

}
