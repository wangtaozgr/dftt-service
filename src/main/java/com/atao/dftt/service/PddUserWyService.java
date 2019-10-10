package com.atao.dftt.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
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
import com.atao.dftt.util.FileUtils;
import com.atao.dftt.util.FreemarkerUtils;
import com.atao.util.DateUtils;

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

	@Value("${pdd.pj.img.path}")
	public String pddImgPath;// 评多多图片路径

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

	/**
	 * 
	 * @param username
	 * @param goodsId
	 * @param price
	 * @param group
	 *            是否和别人拼团
	 * @return
	 */
	public DataVo createOrder(String username, String goodsId, float price, boolean group, boolean ddtz) {
		DataVo result = new DataVo();
		try {
			logger.info("taodan-{}:开始pdd下单,goodsId={},price={},group={}", username, goodsId, price, group);
			PddUser user = queryUserByUsername(username);
			PddHttp http = PddHttp.getInstance(user);
			JSONObject productDetail = http.productDetail(goodsId);
			JSONObject goods = productDetail.getJSONObject("goods");
			String groupId = goods.getJSONArray("group").getJSONObject(1).getString("group_id");
			JSONArray skus = productDetail.getJSONArray("sku");
			JSONObject activity = productDetail.getJSONObject("activity_collection").getJSONObject("activity");
			String activityId = "";
			if (activity != null) {
				activityId = activity.getString("activity_id");
			}
			String groupOrderId = "";
			if (group) {
				JSONArray localGroups = http.groups(goodsId);
				if (localGroups != null && localGroups.size() > 0)
					groupOrderId = localGroups.getJSONObject(0).getString("group_order_id");
			}
			if (goods.getIntValue("status") == 2) {
				result = new DataVo(0, "生成订单失败,商品已下架!", null);
				return result;
			}
			for (int j = 0; j < skus.size(); j++) {
				JSONObject sku = skus.getJSONObject(j);
				if (sku.getIntValue("group_price") / 100f == price && sku.getIntValue("quantity") > 0) {
					String skuId = sku.getString("sku_id");
					JSONObject order = http.submitAppOrder(activityId, groupId, skuId, goodsId, groupOrderId, ddtz);
					if (order == null) {
						result = new DataVo(0, "生成订单失败,cookie已失效!", null);
						return result;
					}
					if(order.getIntValue("order_amount")/ 100f != price) {
						result = new DataVo(0, "生成订单成功,但是价格不符合!", null);
						return result;
					}
					logger.info("taodan-{}:pdd下单成功,goodsId={},order={}", username, goodsId, order);
					result = new DataVo(1, "生成订单成功", order);
					return result;
				}
			}
			logger.info("taodan-{}:pdd下单失败,goodsId={},error={}", username, goodsId, "生成订单失败,没有找到相符的商品");
			result = new DataVo(0, "生成订单失败,没有找到相符的商品!", null);
			return result;
		} catch (Exception e) {
			logger.error("taodan-{}:pdd下单失败,goodsId={},error={}", username, goodsId, "生成订单失败,解析商品时发生异常");
			result = new DataVo(0, "生成订单失败,解析商品时发生异常!", null);
			return result;
		}

	}

	public String confirmReciveGoods(String username, String orderSn, String goodsId, String comment,
			String[] imageUrls) {
		PddUser user = queryUserByUsername(username);
		PddHttp http = PddHttp.getInstance(user);
		JSONObject order = http.queryOrderDetail(orderSn);
		if (order == null) {
			logger.error("pdd-{}:{}|查询拼多多订单状态失败，请更新cookie!", user.getUsername(), orderSn);
			return "查询拼多多订单状态异常，请更新cookie!";
		}
		if ("待收货".equals(order.getString("chatStatusPrompt"))) {
			if (http.isSign(order)) {
				JSONObject confirm = http.confirmReciveGoods(orderSn);
				if (confirm != null) {
					JSONObject submitComment = http.submitComment(goodsId, orderSn, comment, imageUrls);
					if (submitComment != null) {
						String downloadMsg = downloadCommentImage(http, orderSn, pddImgPath, 1);
						return downloadMsg;
					} else {
						return "评价异常!";
					}
				} else {
					return "确认收货异常!";
				}
			} else {
				return "货物还未签收,不能确认收货!";
			}
		} else if ("待评价".equals(order.getString("chatStatusPrompt"))) {
			logger.info("pdd-{}:开始评价.orderSn={}", user.getUsername(), orderSn);
			JSONObject submitComment = http.submitComment(goodsId, orderSn, comment, imageUrls);
			if (submitComment != null) {

				String downloadMsg = downloadCommentImage(http, orderSn, pddImgPath, 1);
				return downloadMsg;
			} else {
				return "评价异常!";
			}
		} else if ("已评价".equals(order.getString("chatStatusPrompt"))) {
			String downloadMsg = downloadCommentImage(http, orderSn, pddImgPath, 1);
			return downloadMsg;
		}
		return "订单状态不对,确认收货失败!";
	}

	/**
	 * 不需要确认是否已签字
	 * 
	 * @param username
	 * @param orderSn
	 * @param goodsId
	 * @param comment
	 * @param imageUrls
	 * @return
	 */
	public String confirmReciveGoodsNew(String username, String orderSn, String goodsId, String comment,
			String[] imageUrls) {
		PddUser user = queryUserByUsername(username);
		PddHttp http = PddHttp.getInstance(user);
		JSONObject order = http.queryOrderDetail(orderSn);
		if (order == null) {
			logger.error("pdd-{}:{}|查询拼多多订单状态失败，请更新cookie!", user.getUsername(), orderSn);
			return "查询拼多多订单状态异常，请更新cookie!";
		}
		if ("待收货".equals(order.getString("chatStatusPrompt"))) {
			JSONObject confirm = http.confirmReciveGoods(orderSn);
			if (confirm != null) {
				JSONObject submitComment = http.submitComment(goodsId, orderSn, comment, imageUrls);
				if (submitComment != null) {
					/*try {
						Thread.sleep(60000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					String downloadMsg = downloadCommentImage(http, orderSn, pddImgPath, 1);
					return downloadMsg;*/
					return "";
				} else {
					return "评价异常!";
				}
			} else {
				return "确认收货异常!";
			}
		} else if ("待评价".equals(order.getString("chatStatusPrompt"))) {
			logger.info("pdd-{}:开始评价.orderSn={}", user.getUsername(), orderSn);
			JSONObject submitComment = http.submitComment(goodsId, orderSn, comment, imageUrls);
			if (submitComment != null) {
				/*try {
					Thread.sleep(60000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				String downloadMsg = downloadCommentImage(http, orderSn, pddImgPath, 1);
				return downloadMsg;*/
				return "";
			} else {
				return "评价异常!";
			}
		} else if ("已评价".equals(order.getString("chatStatusPrompt"))) {
			/*String downloadMsg = downloadCommentImage(http, orderSn, pddImgPath, 1);
			return downloadMsg;*/
			return "";
		}
		return order.getString("chatStatusPrompt");
	}

	public String downloadCommentImage(PddHttp http, String orderSn, String pddImgPath, int count) {
		if (count > 10) {
			return "超时未下载评论图片,发生异常!";
		}
		String downloadMsg = http.downloadCommentImage(orderSn, pddImgPath);
		if ("没有找到相关评论，无法下载评论图片!".equals(downloadMsg)) {
			count++;
			try {
				Thread.sleep(20000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return downloadCommentImage(http, orderSn, pddImgPath, count);
		} else {
			return downloadMsg;
		}
	}
	

	/**
	 * 不下载评论图片
	 * 
	 * @param username
	 * @param orderSn
	 * @param goodsId
	 * @param comment
	 * @param imageUrls
	 * @return
	 */
	public String confirmReciveGoodsPkd(String username, String orderSn, String goodsId, String comment,
			String[] imageUrls) {
		PddUser user = queryUserByUsername(username);
		PddHttp http = PddHttp.getInstance(user);
		JSONObject order = http.queryOrderDetail(orderSn);
		if (order == null) {
			logger.error("pdd-{}:{}|查询拼多多订单状态失败，请更新cookie!", user.getUsername(), orderSn);
			return "查询拼多多订单状态异常，请更新cookie!";
		}
		if ("待收货".equals(order.getString("chatStatusPrompt"))) {
			if (http.isSign(order)) {
				JSONObject confirm = http.confirmReciveGoods(orderSn);
				if (confirm != null) {
					JSONObject submitComment = http.submitComment(goodsId, orderSn, comment, imageUrls);
					if (submitComment != null) {
						return "";
					} else {
						return "评价异常!";
					}
				} else {
					return "确认收货异常!";
				}
			} else {
				return "货物还未签收,不能确认收货!";
			}
		} else if ("待评价".equals(order.getString("chatStatusPrompt"))) {
			logger.info("pdd-{}:开始评价.orderSn={}", user.getUsername(), orderSn);
			JSONObject submitComment = http.submitComment(goodsId, orderSn, comment, imageUrls);
			if (submitComment != null) {
				return "";
			} else {
				return "评价异常!";
			}
		}
		return "订单状态不对,确认收货失败!";
	}

	public String downloadCommentImage(String username, String orderSn) {
		PddUser user = queryUserByUsername(username);
		PddHttp http = PddHttp.getInstance(user);
		return http.downloadCommentImage(orderSn, pddImgPath);
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
