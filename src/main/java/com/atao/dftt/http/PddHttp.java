package com.atao.dftt.http;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.atao.base.util.StringUtils;
import com.atao.dftt.model.PddUser;
import com.atao.dftt.util.FileUtils;
import com.atao.dftt.util.MobileHttpUrlConnectUtils;

import ch.qos.logback.core.util.FileUtil;

public class PddHttp {
	private static Logger logger = LoggerFactory.getLogger(PddHttp.class);
	private static Map<String, PddHttp> userMap = new HashMap<String, PddHttp>();
	public PddUser user;
	public String anti_content = "0alAfandFyhYq9duvXi6MC9Z7uUPkCjk__lgNHfdJ_jX-jT2p_BSBiwdqOn12lMiu5zQ_2i07iQMq0mDk9i7AMAJ8QlQDJ59P0y2qxqw0EBP8mXG95BJlMhYTnI-urdWuirueate3z_NKI7Z3ddCLDMcMfYtj_DqCHb7i1VOJcidcrzzSHcIEm1Uj8UpIIFUfEZm5B5PWe0bCvHH-5yYBx39WEESSTgL1_vcks1igt-7wjb4GMiuUr1zm-358T53qpI3r-u5lgCxQXwPZb5UYJQLvokQLxcKEITbQeK8cS1oOOUnkNUugmCeXNGOp8m-njSKxJVtg99QGsdEpGbeDPKogcIF4ro0ZUxC-EKMxBWhI-02CZs446ox4Sxkop9t3O_U9-w4mUNs64WqIXbwawbUFP1TYarLSl6y-SizKOxqdybDsjOac0yrN6E3cdnIYzI_ogpWvY1awyZPLVqzOD0ypJrcfrajL7aQUoiT0t0VYlKVQXf6RhHe2NA7cq4C3ewrIhXJIf8ZXuH5rtuilwImIS6vD-dr65quLho-jxLdOAlDa_-VdFXlSpexXeVE9Tr0flqHrnz78kxukZnJuW0moWbhSN4EjbqBW4mbLoORjM9KZ5_ARhKa7NxqqQrj3QHy50y7PMqQB9rTf3b4DIftxnCqyK8lLnBIBuS1-Hx7_-Yxs_EFHUwpFCmhhuosxWaqNoFmzDTT8iQQCK-qdtIwhxE3pw-33_FyihVkIvHhrVgiaiMMRr4zt8VC9";

	public PddHttp(PddUser user) {
		this.user = user;
	}

	public static synchronized PddHttp getInstance(PddUser user) {
		PddHttp http = (PddHttp) userMap.get(user.getUsername());
		if (http == null) {
			http = new PddHttp(user);
			userMap.put(user.getUsername(), http);
		}
		return http;
	}

	public static void main(String[] args) throws URISyntaxException, IOException {
		PddUser user = new PddUser();
		// user.setUid("1852788720");
		// user.setAddressId("4169994514");
		// user.setToken("M2TAWPOSR2ETG2IIK7YQUFEJTY6K6YQLU3RJ4KX4WAWAPUPAC62Q103f010");
		// user.setApiUid("rBUGzVwUht4AJXpZhdyiAg==");
		/*
		 * user.setUid("7887501021770"); user.setAddressId("4526882634");
		 * user.setToken("EL5PTDOEG4TQANDZ7YXEJXQFSLKECCHFPYTZXD5QXYLU4SYRTEWA1024a60");
		 * user.setApiUid("rBUGzVwUht4AJXpZhdyiAg==");
		 */
		user.setUid("1852788720");
		user.setAddressId("4169994514");
		user.setToken("5SP2FH37RBJ6XC2OVAPUDCJ3C3JB67L4TVRN7VQTQX4VBPY5YSKA103f010");
		user.setApiUid("rBUGzVwUht4AJXpZhdyiAg==");
		user.setUserAgent(
				"android Mozilla/5.0 (Linux; Android 7.1.2; MI 5X Build/N2G47H; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/64.0.3282.137 Mobile Safari/537.36  phh_android_version/4.33.0 phh_android_build/a2d7218bda21579f6f922dd29885b0d04b040338_pdd_patch phh_android_channel/xm");
		PddHttp http = PddHttp.getInstance(user);
		//http.uploadPic("https://t10.baidu.com/it/u=4232360919,2367790879&fm=76");
		//String[] imageUrls = {"http://www.ddhaoping.com/static/index/images/common/icon-2.png","http://www.ddhaoping.com/static/index/images/common/icon-6.png"};
		//http.submitComment("24305851613", "190810-632542986914080", "给个好评，给个好评，给个好评，给个好评", imageUrls);
		
		http.myComments();
		// http.isPayed("190314-620544656134080");
		// http.submitMsOrder("10087319573190314-620544656134080", "157991525691",
		// "6061113295",
		// "690324767909011685");
		// JSONObject object = http.luckyProductList();
		// http.submitMsOrder("9666483403", "150128201486",
		// "5824829372","682053681193931685");
		// System.out.println(object.toJSONString());
		// http.luckyShareList();
		// http.luckyProductDetail("201902200178");
		// http.productList();
		// JSONObject json = http.productDetail(5817302466l);
		// System.out.println(json.toJSONString());
		// http.submitOrder("4700564513", "66182739494", "3065965844");
		// http.submitAppOrder("", "9704190075", "150817102377", "5846576717");// 45034
		// 42005
		// http.getActId("9704780241", "150828594322", "5847050673");
		// http.submitWxOrder("", "9548387727", "148424487314", "5757554411");
		// http.coinsCollect();
	}

	/**
	 * 下期秒杀商品列表
	 * 
	 * @return
	 */
	public JSONObject productList() {
		String url = "https://mobile.yangkeduo.com/proxy/api/api/spike/v4/list/future?type=1&page=1&size=20&pdduid="
				+ user.getUid() + "&is_back=1";
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("User-Agent", user.getUserAgent());
		heads.put("Accept-Encoding", "gzip");
		heads.put("Content-Type", "application/json;charset=UTF-8");
		heads.put("AccessToken", user.getToken());
		heads.put("Cookie", "api_uid=" + user.getApiUid());
		String content = MobileHttpUrlConnectUtils.httpGet(url, heads, null);
		if (StringUtils.isNotBlank(content)) {
			JSONObject jsonObj = JSONObject.parseObject(content);
			return jsonObj;
		}
		return null;
	}

	/**
	 * 商品详细信息
	 * 
	 * @param goodsId
	 * @return
	 */
	public JSONObject productDetail(Long goodsId) {
		String url = "https://api.yangkeduo.com/api/oak/integration/render?pdduid=" + user.getUid();
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("User-Agent", user.getUserAgent());
		heads.put("Accept-Encoding", "gzip");
		heads.put("Content-Type", "application/json;charset=UTF-8");
		heads.put("AccessToken", user.getToken());
		heads.put("Cookie", "api_uid=" + user.getApiUid());
		String postData = "{\"goods_id\":" + goodsId + ",\"page_from\":2}";
		String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
		logger.info("pddms-{}: 查询商品详细.{}", user.getUsername(), content);

		if (StringUtils.isNotBlank(content)) {
			JSONObject jsonObj = JSONObject.parseObject(content);

			return jsonObj;
		}
		return null;
	}

	public JSONObject submitMsOrder(String groupId, String skuId, String goodsId, String groupOrderId) {
		String url = "https://api.yangkeduo.com/api/van-spike/order?pdduid=" + user.getUid();
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("User-Agent", user.getUserAgent());
		heads.put("Accept-Encoding", "gzip");
		heads.put("Content-Type", "application/json;charset=UTF-8");
		// heads.put("Cookie", "api_uid=" + user.getApiUid());
		heads.put("AccessToken", user.getToken());
		// heads.put("ETag", "b0LzKmIm");
		heads.put("p-mode", "1");
		// heads.put("PDD-CONFIG", "00102");
		// heads.put("anti-token",
		// "2afEFCN1/ylZYEt5aQ/mRzIgcfIHxRZbykbUbVh8ki8pe9+5SbZH7wn4ygPpN9BgPpRwUZeN6WWm4/0Nae4Ob3GA/pjimoFKjyPhTlaCN52M1rsx74WiX3parsnfSL5z5PJKaZhntISHM2g3oBjqSdWnnrkm9l4YO9FyIkFgiaE9azNSTqqZsW137kSzQZBrwPJqK75xr2kD86Jpq4+lsspZLz1roXHOc8i8Tjocu6NZTG76exK2E8pSxXxTG7AVA3OQkPx9lU/hh8gfa2uEoRwjSNeTjXaFlw+5Hgjt1zCYYTyZhL86N1OQsMr9YKDUA/RSdnfr/dG+WwfTG2nP/dtg4QJvMFumPZyapUyTANPPFo5/COGHfNC/rll4UGRHThGKA6eAfB5boDjnu5fmCI9eHv2GVnnw5HzzLbcw8NerheB1rl3OrBkTosdqr3r3PzsTYgf0kj/0xWokauuQHL3pSkBxWhVatWMMyDNAcOVpSq5ys05OzS4bj5VOOq04qFyaml43nTQn561RTOOT7LuO/vb1aBXUJtlvg0cwXmNkoMbbWHPPeHJFk3BQyxqFi/Nnr9b4UQpX/hdpn/9QabK7RkYoYfoQKM6hEbHN8nYv6gM4dD05xzqAtkj7/TVTr4ntZjfC6YaNeoQXTRrLnqBxw==");
		JSONArray goods = new JSONArray();
		JSONObject goods01 = new JSONObject(true);
		goods01.fluentPut("goods_id", goodsId).fluentPut("sku_id", skuId).fluentPut("sku_number", 1);
		goods.add(goods01);
		JSONObject postData = new JSONObject(true);
		postData.fluentPut("address_id", user.getAddressId()).fluentPut("goods", goods).fluentPut("group_id", groupId)
				.fluentPut("anti_content", anti_content).fluentPut("page_from", 2).fluentPut("duoduo_type", 0)
				.fluentPut("biz_type", 0).fluentPut("attribute_fields", null).fluentPut("source_channel", 0)
				.fluentPut("source_type", 0).fluentPut("pay_app_id", 4).fluentPut("is_app", "1")
				.fluentPut("version", 1);
		if (StringUtils.isNotBlank(groupOrderId)) {
			postData.fluentPut("group_order_id", groupOrderId);
		}
		String content = MobileHttpUrlConnectUtils.httpPost(url, postData.toJSONString(), heads, null);
		// logger.info("pddms-{}: 提交订单结果={}", user.getUsername(), content);

		if (StringUtils.isNotBlank(content)) {
			JSONObject jsonObj = JSONObject.parseObject(content);
			return jsonObj;
		}
		return null;
	}

	/**
	 * app上商品购买
	 * 
	 * @param activityId
	 * @param groupId
	 * @param skuId
	 * @param goodsId
	 * @return
	 */
	public JSONObject submitAppOrder(String activityId, String groupId, String skuId, String goodsId) {
		String url = "http://api.yangkeduo.com/order?pdduid=" + user.getUid();
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("User-Agent", user.getUserAgent());
		heads.put("Accept-Encoding", "gzip");
		heads.put("Content-Type", "application/json;charset=UTF-8");
		heads.put("Cookie", "api_uid=" + user.getApiUid());
		heads.put("AccessToken", user.getToken());
		heads.put("ETag", "b0LzKmIm");
		heads.put("lat", "TDDLQTU2MDK3WJ54Z5UTYICXCCBN374EJ6JF5M67PGTZ3CNTSZSQ103f010");
		heads.put("PDD-CONFIG", "00102");
		heads.put("Referer", "Android");
		heads.put("X-PDD-QUERIES", "width=1080&height=1920&net=1&brand=xiaomi&model=MI+5X&osv=7.1.2&appv=4.33.0&pl=2");
		// heads.put("anti-token",
		// "2afEFCN1/ylZYEt5aQ/mRzIgeygAjbO3Jsh0iivSzz3lSMLzTYmFdDosscIsMkz9HOj+Oc68NXRl2Q6gnUNNo1QttFKVney5cdoiS+FNOssMdJ5qJ+z5GQJl5QPjdQ3r4edw0yW5m4cJCo5CNfEyBHRcy/RPbrzoqTn0hrbU47FWtjvNmIKZyzTomuAZQvOW6utRUTblriX2tbJjv2SMQBz1jaFKYZvIlAulB9BAx2tXZHrdsNk/Q20Tl6acmPLeQimj0SX+be0vden5WSNnxb4LsVLinsBgy8bAAZkXSUtpJA1GJW3YIC6iC8vouXqyzrRk0hI56PMV/jviaj+OWIneCoidKUhm2MfCog3jX9svpqlLGHPleNxMo8SDRWK97tnqG0QB8DLXJ8BP939+kB6WiHMAlxaWpS7JjwPj9UZq6gQkmmDVL3kJf+va5sYaBsuixM1Q0uyruN6Wk/qqNk9EIZEJGpe+7EEVsV6HQLqjB/FdyhAwRTmksoagBhzq0Emgvn6xozDg4vFGjARqavp09jgMsXsxy8ZAFeTiWNWbVOUIaw0BJTEhr5nsjLYfcgcs69FGBQc5uQaSrHEOE/iZOZHFwHtrl5w5ahVwd7e3FMn7VceBDM1TFbk0reG8AGjSHvVsesmW+AiqYxnBdfsNA==");
		JSONArray goods = new JSONArray();
		JSONObject goods01 = new JSONObject(true);
		goods01.fluentPut("goods_id", goodsId).fluentPut("sku_id", skuId).fluentPut("sku_number", 1);
		goods.add(goods01);
		JSONObject postData = new JSONObject(true);
		if (StringUtils.isNotBlank(activityId)) {
			postData = postData.fluentPut("activity_id", activityId);
		}
		postData.fluentPut("address_id", user.getAddressId()).fluentPut("award_type", 0).fluentPut("biz_type", 0)
				.fluentPut("coupon_number", 0).fluentPut("type", 0).fluentPut("goods", goods)
				.fluentPut("group_id", groupId).fluentPut("is_app", 1).fluentPut("page_from", "0")
				.fluentPut("pay_app_id", "35").fluentPut("source_type", 0).fluentPut("version", 1)
				.fluentPut("version_type", 1);
		String content = MobileHttpUrlConnectUtils.httpPost(url, postData.toJSONString(), heads, null);
		logger.info("pddms-{}: 提交订单结果.{}", user.getUsername(), content);
		if (StringUtils.isNotBlank(content)) {
			JSONObject jsonObj = JSONObject.parseObject(content);
			return jsonObj;
		}
		return null;
	}

	/**
	 * 微信上面商品购买
	 * 
	 * @param activityId
	 * @param groupId
	 * @param skuId
	 * @param goodsId
	 * @return
	 */
	public JSONObject submitWxOrder(String activityId, String groupId, String skuId, String goodsId) {
		String url = "https://mobile.yangkeduo.com/proxy/api/api/van-spike/order?pdduid=1852788720";
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("Accept-Encoding", "gzip, deflate");
		heads.put("User-Agent", user.getUserAgent());
		heads.put("X-Requested-With", "com.tencent.mm");
		heads.put("Origin", "https://mobile.yangkeduo.com");
		heads.put("Content-Type", "application/json;charset=UTF-8");
		heads.put("Cookie", "PDDAccessToken=" + user.getToken() + ";");
		heads.put("accesstoken", user.getToken());
		JSONArray goods = new JSONArray();
		JSONObject goods01 = new JSONObject(true);
		goods01.fluentPut("goods_id", goodsId).fluentPut("sku_id", skuId).fluentPut("sku_number", 1);
		goods.add(goods01);
		JSONObject postData = new JSONObject(true);
		postData.fluentPut("address_id", user.getAddressId()).fluentPut("goods", goods).fluentPut("group_id", groupId)
				.fluentPut("duoduo_type", 0).fluentPut("biz_type", 0).fluentPut("attribute_fields", null)
				.fluentPut("source_channel", 0).fluentPut("is_app", 0).fluentPut("pay_app_id", "2")
				.fluentPut("source_type", 0).fluentPut("version", 1);
		String content = MobileHttpUrlConnectUtils.httpPost(url, postData.toJSONString(), heads, null);
		logger.info(content);
		if (StringUtils.isNotBlank(content)) {
			JSONObject jsonObj = JSONObject.parseObject(content);
			return jsonObj;
		}
		return null;
	}

	/**
	 * 秒杀商品预约
	 * 
	 * @param goodsId
	 * @param goodsName
	 * @param startDate
	 * @return
	 */
	public boolean yuyue(String goodsId, String goodsName, Date startDate) {
		String url = "https://api.yangkeduo.com/api/massage/push/add?pdduid=" + user.getUid();
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("User-Agent", user.getUserAgent());
		heads.put("Accept-Encoding", "gzip");
		heads.put("Content-Type", "application/json;charset=UTF-8");
		heads.put("AccessToken", user.getToken());
		heads.put("Cookie", "api_uid=" + user.getApiUid());
		JSONObject postData = new JSONObject(true);
		long startTime = startDate.getTime() / 1000;
		postData.fluentPut("goodsId", goodsId).fluentPut("goodsName", goodsName).fluentPut("spikeType", 0)
				.fluentPut("startTime", startTime);
		String content = MobileHttpUrlConnectUtils.httpPost(url, postData.toJSONString(), heads, null);
		logger.info("pddms-{}: 商品预约结果={}", user.getUsername(), content);
		JSONObject jsonObj = JSONObject.parseObject(content);
		if (jsonObj.getBooleanValue("success")) {
			return true;
		}
		return false;
	}

	/**
	 * 查询活动id
	 * 
	 * @param groupId
	 * @param skuId
	 * @param goodsId
	 * @return
	 */
	public String getActId(String groupId, String skuId, String goodsId) {
		String url = "https://mobile.yangkeduo.com/order_checkout.html?sku_id=" + skuId + "&group_id=" + groupId
				+ "&goods_id=" + goodsId + "&goods_number=1";
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("User-Agent", user.getUserAgent());
		heads.put("Accept-Encoding", "gzip");
		heads.put("Cookie", "PDDAccessToken=" + user.getToken() + ";");
		String content = MobileHttpUrlConnectUtils.httpGet(url, heads, null);
		int start = content.indexOf("activityId") + 13;
		int end = content.indexOf("\"", start);
		String actId = content.substring(start, end);
		logger.info("pddms-{}:查询活动id={}", user.getUsername(), actId);
		if ("ull,".equals(actId))
			return null;
		return actId;
	}

	/**
	 * 可购买幸运人气王商品列表
	 * 
	 * @return
	 */
	public JSONObject luckyProductList() {
		String url = "https://api.pinduoduo.com/api/activity/rumbia/issues/list?page=1&size=50&sort_rule=1&pdduid="
				+ user.getUid();
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("User-Agent", user.getUserAgent());
		heads.put("Accept-Encoding", "gzip");
		heads.put("Content-Type", "application/json;charset=UTF-8");
		heads.put("AccessToken", user.getToken());
		heads.put("ETag", "b0LzKmIm");
		heads.put("PDD-CONFIG", "00102");
		heads.put("p-mode", "1");
		String content = MobileHttpUrlConnectUtils.httpGet(url, heads, null);
		// logger.info("pddms-{}:可购买幸运人气王商品列表={}",user.getUsername(), content);
		if (StringUtils.isNotBlank(content)) {
			JSONObject jsonObj = JSONObject.parseObject(content);
			return jsonObj;
		}
		return null;
	}

	/**
	 * 幸运人气王详细数据
	 * 
	 * @param issue_id
	 * @return
	 */
	public JSONObject luckyProductDetail(String issue_id) {
		String url = "https://api.pinduoduo.com/api/activity/rumbia/goods/detail?timeout=1500&issue_id=" + issue_id
				+ "&pdduid=" + user.getUid();
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("User-Agent", user.getUserAgent());
		heads.put("Accept-Encoding", "gzip");
		heads.put("Content-Type", "application/json;charset=UTF-8");
		heads.put("AccessToken", user.getToken());
		heads.put("ETag", "b0LzKmIm");
		heads.put("PDD-CONFIG", "00102");
		heads.put("p-mode", "1");
		String content = MobileHttpUrlConnectUtils.httpGet(url, heads, null);
		// logger.info("pddms-{}:幸运人气王详细数据={}",user.getUsername(), content);
		if (StringUtils.isNotBlank(content)) {
			JSONObject jsonObj = JSONObject.parseObject(content);
			return jsonObj;
		}
		return null;
	}

	/**
	 * 购买幸运人气王商品
	 * 
	 * @param issueId
	 * @param groupId
	 * @param skuId
	 * @param goodsId
	 * @return
	 */
	public JSONObject submitLuckyOrder(String issueId, String groupId, String skuId, String goodsId) {
		String url = "https://api.pinduoduo.com/api/activity/rumbia/order/create?pdduid=" + user.getUid();
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("User-Agent", user.getUserAgent());
		heads.put("Accept-Encoding", "gzip");
		heads.put("Content-Type", "application/json;charset=UTF-8");
		heads.put("AccessToken", user.getToken());
		heads.put("ETag", "b0LzKmIm");
		heads.put("PDD-CONFIG", "00102");
		heads.put("p-mode", "1");
		JSONObject postData = new JSONObject(true);
		postData.fluentPut("goods_sku_id", skuId).fluentPut("goods_sku_number", 1).fluentPut("group_id", groupId)
				.fluentPut("goods_id", goodsId).fluentPut("issue_id", issueId).fluentPut("list_site", 2)
				.fluentPut("anti_content", anti_content).fluentPut("address_id", user.getAddressId())
				.fluentPut("biz_type", 2016).fluentPut("pay_app_id", 4).fluentPut("is_app", 1);
		String content = MobileHttpUrlConnectUtils.httpPost(url, postData.toJSONString(), heads, null);
		logger.info("pddms-{}:购买幸运人气王商品下单结果={}", user.getUsername(), content);
		if (StringUtils.isNotBlank(content)) {
			JSONObject jsonObj = JSONObject.parseObject(content);
			return jsonObj;
		}
		return null;
	}

	/**
	 * 已购幸运人气王列表
	 * 
	 * @return
	 */
	public JSONObject luckyShareList() {
		String url = "https://api.pinduoduo.com/api/activity/rumbia/issues/me?pdduid=" + user.getUid();
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("User-Agent", user.getUserAgent());
		heads.put("Accept-Encoding", "gzip");
		heads.put("Content-Type", "application/json;charset=UTF-8");
		heads.put("AccessToken", user.getToken());
		heads.put("ETag", "b0LzKmIm");
		heads.put("PDD-CONFIG", "00102");
		heads.put("p-mode", "1");
		String content = MobileHttpUrlConnectUtils.httpGet(url, heads, null);
		if (StringUtils.isNotBlank(content)) {
			JSONObject jsonObj = JSONObject.parseObject(content);
			return jsonObj;
		}
		return null;
	}

	/**
	 * 幸运人气购买功后的分享
	 * 
	 * @param issueId
	 * @return
	 */
	public JSONObject luckyShare(String issueId) {
		String url = "https://api.yangkeduo.com/api/activity/rumbia/share/count?pdduid=" + user.getUid();
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("User-Agent", user.getUserAgent());
		heads.put("Accept-Encoding", "gzip");
		heads.put("Content-Type", "application/json;charset=UTF-8");
		heads.put("AccessToken", user.getToken());
		heads.put("ETag", "b0LzKmIm");
		heads.put("PDD-CONFIG", "00102");
		heads.put("p-mode", "1");
		JSONObject postData = new JSONObject(true);
		postData.fluentPut("issue_id", issueId).fluentPut("version", 2);
		String content = MobileHttpUrlConnectUtils.httpPost(url, postData.toJSONString(), heads, null);
		if (StringUtils.isNotBlank(content)) {
			JSONObject jsonObj = JSONObject.parseObject(content);
			return jsonObj;
		}
		return null;
	}

	/**
	 * 查询幸运人气可分享次数
	 * 
	 * @param issueId
	 * @return
	 */
	public JSONObject queryShareTask(String issueId) {
		String url = "https://api.pinduoduo.com/api/activity/rumbia/task/share/query?issue_id=" + issueId + "&pdduid="
				+ user.getUid();
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("User-Agent", user.getUserAgent());
		heads.put("Accept-Encoding", "gzip");
		heads.put("Content-Type", "application/json;charset=UTF-8");
		heads.put("AccessToken", user.getToken());
		heads.put("ETag", "b0LzKmIm");
		heads.put("PDD-CONFIG", "00102");
		heads.put("p-mode", "1");
		JSONObject postData = new JSONObject(true);
		postData.fluentPut("issue_id", issueId).fluentPut("version", 2);
		String content = MobileHttpUrlConnectUtils.httpPost(url, postData.toJSONString(), heads, null);
		if (StringUtils.isNotBlank(content)) {
			JSONObject jsonObj = JSONObject.parseObject(content);
			return jsonObj;
		}
		return null;
	}

	/**
	 * 完成幸运人气王分享任务
	 * 
	 * @param issueId
	 * @return
	 */
	public JSONObject luckyShareTask(String issueId) {
		String url = "https://api.pinduoduo.com/api/activity/rumbia/task/share/complete?issue_id=" + issueId
				+ "&pdduid=" + user.getUid();
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("User-Agent", user.getUserAgent());
		heads.put("Accept-Encoding", "gzip");
		heads.put("Content-Type", "application/json;charset=UTF-8");
		heads.put("AccessToken", user.getToken());
		heads.put("ETag", "b0LzKmIm");
		heads.put("PDD-CONFIG", "00102");
		heads.put("p-mode", "1");
		JSONObject postData = new JSONObject(true);
		postData.fluentPut("issue_id", issueId).fluentPut("version", 2);
		String content = MobileHttpUrlConnectUtils.httpPost(url, postData.toJSONString(), heads, null);
		if (StringUtils.isNotBlank(content)) {
			JSONObject jsonObj = JSONObject.parseObject(content);
			return jsonObj;
		}
		return null;
	}

	public JSONObject coinsCollect() {
		String url = "https://api.pinduoduo.com/api/market/peppa/coins/collect?pdduid=" + user.getUid();
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("User-Agent", user.getUserAgent());
		heads.put("Accept-Encoding", "gzip");
		heads.put("Content-Type", "application/json;charset=UTF-8");
		heads.put("AccessToken", user.getToken());
		heads.put("ETag", "b0LzKmIm");
		heads.put("PDD-CONFIG", "00102");
		heads.put("p-mode", "1");
		JSONObject postData = new JSONObject(true);
		postData.fluentPut("anti_content", anti_content);
		String content = MobileHttpUrlConnectUtils.httpPost(url, postData.toJSONString(), heads, null);
		if (StringUtils.isNotBlank(content)) {
			JSONObject jsonObj = JSONObject.parseObject(content);
			return jsonObj;
		}
		return null;
	}

	/**
	 * 订单明细 payStatus 0:待支付 2:已付款 chatStatusPrompt 待支付 待分享 待收货 待评价 orderStatus 0:待支付 待分享
	 * 1:待收货 2:交易已取消
	 * 
	 * @param orderSn
	 * @return
	 */
	public JSONObject queryOrderDetail(String orderSn) {
		String url = "https://mobile.yangkeduo.com/order.html?order_sn=" + orderSn
				+ "&refer_page_name=my_order&_x_src=homepop&_x_platform=wechat";
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("User-Agent", user.getUserAgent());
		heads.put("Accept-Encoding", "gzip");
		heads.put("Cookie", "PDDAccessToken=" + user.getToken() + ";");
		String content = MobileHttpUrlConnectUtils.httpGet(url, heads, null);
		int start = content.indexOf("window.rawData=") + 15;
		int end = content.indexOf("}};", start) + 2;
		content = content.substring(start, end);
		JSONObject jsonObj = JSONObject.parseObject(content);
		logger.info("pddms-{}:orderSn={},订单明细={}", user.getUsername(), orderSn, jsonObj);
		return jsonObj.getJSONObject("data");
	}

	public boolean isPayed(String orderSn) {
		try {
			JSONObject order = queryOrderDetail(orderSn);
			if (order.getIntValue("payStatus") == 2) {
				return true;
			} else
				return false;
		} catch (Exception e) {
			return false;
		}

	}

	/**
	 * 我的评价列表
	 * @param orderSn
	 * @return
	 */
	public JSONObject myComments() {
		String url = "https://mobile.yangkeduo.com/my_comments.html";
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("User-Agent", user.getUserAgent());
		heads.put("Accept-Encoding", "gzip");
		heads.put("Cookie", "PDDAccessToken=" + user.getToken() + ";");
		String content = MobileHttpUrlConnectUtils.httpGet(url, heads, null);
		int start = content.indexOf("window.rawData=") + 15;
		int end = content.indexOf("}};", start) + 2;
		content = content.substring(start, end);
		JSONObject jsonObj = JSONObject.parseObject(content);
		logger.info("pddms-{}:我的评价列表={}", user.getUsername(), jsonObj);
		return jsonObj.getJSONObject("data");
	}
	
	/**
	 * 评价
	 * 
	 * @param activityId
	 * @param groupId
	 * @param skuId
	 * @param goodsId
	 * @return
	 */
	public JSONObject submitComment(String goodsId, String orderSn, String comment, String[] imageUrls) {
		//SIGN_ON_BEHALF  暂存 /SIGN已签收   SEND 送货

		JSONArray pictures = new JSONArray();
		for (String imageUrl : imageUrls) {
			JSONObject uploadResult = uploadPic(imageUrl);
			if (uploadResult == null)
				return null;
			pictures.add(uploadResult);
		}
		String url = "https://mobile.yangkeduo.com/proxy/api/v2/order/goods/review?pdduid=" + user.getUid();
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("User-Agent", user.getUserAgent());
		heads.put("Accept", "application/json, text/plain, */*");
		heads.put("Accept-Encoding", "gzip");
		heads.put("Content-Type", "application/json;charset=UTF-8");
		heads.put("Cookie", "api_uid=" + user.getApiUid());
		heads.put("AccessToken", user.getToken());
		heads.put("ETag", "b0LzKmIm");
		heads.put("lat", "TDDLQTU2MDK3WJ54Z5UTYICXCCBN374EJ6JF5M67PGTZ3CNTSZSQ103f010");
		heads.put("PDD-CONFIG", "00102");
		heads.put("Referer", "Android");
		heads.put("X-PDD-QUERIES", "width=1080&height=1920&net=1&brand=xiaomi&model=MI+5X&osv=7.1.2&appv=4.33.0&pl=2");
		JSONArray goods = new JSONArray();
		JSONObject goods01 = new JSONObject(true);
		goods.add(goods01);
		JSONObject postData = new JSONObject(true);
		postData.fluentPut("goods_id", goodsId).fluentPut("order_sn", orderSn).fluentPut("desc_score", 5)
				.fluentPut("logistics_score", 5).fluentPut("service_score", 5).fluentPut("comment", comment)
				.fluentPut("pictures", pictures).fluentPut("labels", new JSONArray()).fluentPut("anonymous", 0)
				.fluentPut("anti_content", "");
		String content = MobileHttpUrlConnectUtils.httpPost(url, postData.toJSONString(), heads, null);
		logger.info("pddms-{}: 提交评论结果.{}", user.getUsername(), content);
		if (StringUtils.isNotBlank(content)) {
			//{"review_id":"241849880151973085","rating_id":241849880151973085,"masked_comment":"给个好评，给个好评，给个好评，给个好评"}

			JSONObject jsonObj = JSONObject.parseObject(content);
			return jsonObj;
		}
		return null;
	}

	/**
	 * 上传评论照片前的签名
	 * 
	 * @param content
	 * @param imageUrl
	 * @return
	 */
	public String signature() {
		String url = "https://mobile.yangkeduo.com/proxy/api/image/signature?" + user.getUid();
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("User-Agent", user.getUserAgent());
		heads.put("Accept-Encoding", "gzip");
		heads.put("Content-Type", "application/json;charset=UTF-8");
		heads.put("Cookie", "api_uid=" + user.getApiUid());
		heads.put("AccessToken", user.getToken());
		heads.put("ETag", "b0LzKmIm");
		heads.put("lat", "TDDLQTU2MDK3WJ54Z5UTYICXCCBN374EJ6JF5M67PGTZ3CNTSZSQ103f010");
		heads.put("PDD-CONFIG", "00102");
		heads.put("Referer", "Android");
		heads.put("X-PDD-QUERIES", "width=1080&height=1920&net=1&brand=xiaomi&model=MI+5X&osv=7.1.2&appv=4.33.0&pl=2");
		JSONArray goods = new JSONArray();
		JSONObject goods01 = new JSONObject(true);
		goods.add(goods01);
		JSONObject postData = new JSONObject(true);
		postData.fluentPut("bucket_tag", "review_image");
		String content = MobileHttpUrlConnectUtils.httpPost(url, postData.toJSONString(), heads, null);
		logger.info("pdd-{}: 签名结果.{}", user.getUsername(), content);
		// {"signature":"26d0d8efc73e31d30b0f3f1cb70cd30ae883f97d"}
		if (StringUtils.isNotBlank(content)) {
			JSONObject jsonObj = JSONObject.parseObject(content);
			return jsonObj.getString("signature");
		}
		return null;
	}

	public JSONObject uploadPic(String imageUrl) {
		String sign = signature();
		if (StringUtils.isBlank(sign)) {
			logger.error("pdd-{}: 获取签名失败", user.getUsername());
			return null;
		}

		String base64Code = "";
		try {
			base64Code = "data:image/jpeg;base64," + FileUtils.getBase64CodeByCommpass(imageUrl, 900, 1200, 0.7f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (StringUtils.isBlank(sign)) {
			logger.error("pdd-{}: base64加密失败!", user.getUsername());
			return null;
		}
		String url = "https://file.yangkeduo.com/v2/store_image";
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("User-Agent", user.getUserAgent());
		heads.put("Accept-Encoding", "gzip");
		heads.put("Content-Type", "application/json;charset=UTF-8");
		heads.put("Cookie", "api_uid=" + user.getApiUid());
		heads.put("AccessToken", user.getToken());
		heads.put("ETag", "b0LzKmIm");
		heads.put("lat", "TDDLQTU2MDK3WJ54Z5UTYICXCCBN374EJ6JF5M67PGTZ3CNTSZSQ103f010");
		heads.put("PDD-CONFIG", "00102");
		heads.put("Referer", "Android");
		heads.put("X-PDD-QUERIES", "width=1080&height=1920&net=1&brand=xiaomi&model=MI+5X&osv=7.1.2&appv=4.33.0&pl=2");
		JSONArray goods = new JSONArray();
		JSONObject goods01 = new JSONObject(true);
		goods.add(goods01);
		JSONObject postData = new JSONObject(true);
		postData.fluentPut("upload_sign", sign).fluentPut("image", base64Code);
		String content = MobileHttpUrlConnectUtils.httpPost(url, postData.toJSONString(), heads, null);
		logger.info("pdd-{}: 上传图片结果.{}", user.getUsername(), content);
		// {"url":"https://t22img.yangkeduo.com/review3/review/2019-08-27/1bf508e0c9440664417cc16eb5748189.jpeg","width":900,"height":640}
		if (StringUtils.isNotBlank(content)) {
			JSONObject jsonObj = JSONObject.parseObject(content);
			return jsonObj;
		}
		return null;
	}

}
