package com.atao.dftt.http;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.atao.base.util.StringUtils;
import com.atao.dftt.api.vo.DataVo;
import com.atao.dftt.model.PddUser;
import com.atao.dftt.util.CommonUtils;
import com.atao.dftt.util.FileUtils;
import com.atao.dftt.util.FreemarkerUtils;
import com.atao.dftt.util.MobileHttpUrlConnectUtils;
import com.atao.util.DateUtils;

import ch.qos.logback.core.util.FileUtil;

public class PddHttp {
	private static Logger logger = LoggerFactory.getLogger(PddHttp.class);
	private static Map<String, PddHttp> userMap = new HashMap<String, PddHttp>();
	public PddUser user;
	public String anti_content = "0alAfandFyhYq9duvXi6MC9Z7uUPkCjk__lgNHfdJ_jX-jT2p_BSBiwdqOn12lMiu5zQ_2i07iQMq0mDk9i7AMAJ8QlQDJ59P0y2qxqw0EBP8mXG95BJlMhYTnI-urdWuirueate3z_NKI7Z3ddCLDMcMfYtj_DqCHb7i1VOJcidcrzzSHcIEm1Uj8UpIIFUfEZm5B5PWe0bCvHH-5yYBx39WEESSTgL1_vcks1igt-7wjb4GMiuUr1zm-358T53qpI3r-u5lgCxQXwPZb5UYJQLvokQLxcKEITbQeK8cS1oOOUnkNUugmCeXNGOp8m-njSKxJVtg99QGsdEpGbeDPKogcIF4ro0ZUxC-EKMxBWhI-02CZs446ox4Sxkop9t3O_U9-w4mUNs64WqIXbwawbUFP1TYarLSl6y-SizKOxqdybDsjOac0yrN6E3cdnIYzI_ogpWvY1awyZPLVqzOD0ypJrcfrajL7aQUoiT0t0VYlKVQXf6RhHe2NA7cq4C3ewrIhXJIf8ZXuH5rtuilwImIS6vD-dr65quLho-jxLdOAlDa_-VdFXlSpexXeVE9Tr0flqHrnz78kxukZnJuW0moWbhSN4EjbqBW4mbLoORjM9KZ5_ARhKa7NxqqQrj3QHy50y7PMqQB9rTf3b4DIftxnCqyK8lLnBIBuS1-Hx7_-Yxs_EFHUwpFCmhhuosxWaqNoFmzDTT8iQQCK-qdtIwhxE3pw-33_FyihVkIvHhrVgiaiMMRr4zt8VC9";
	// 多多进宝token
	public String ddjbToken = "727CRKX32I4IEJTRN2OCT47GBFHFQ45ZNKC6OP3WAEYPK4MWAXVA103f010";

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
		user.setToken("AFN56XIZSLLHSHYSXY3DTH7MESMVPL25JHORBIVTI64T7H3NQXAQ103f010");
		//user.setToken("X6W7XXM5X77SH5CTPZYPRCPYTP55B624B3BJGY3LQ2K367UVLA2A100a459");
		user.setApiUid("rBUGzVwUht4AJXpZhdyiAg==");
		user.setUserAgent(
				"android Mozilla/5.0 (Linux; Android 7.1.2; MI 5X Build/N2G47H; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/64.0.3282.137 Mobile Safari/537.36  phh_android_version/4.33.0 phh_android_build/a2d7218bda21579f6f922dd29885b0d04b040338_pdd_patch phh_android_channel/xm");
		PddHttp http = PddHttp.getInstance(user);
		// http.uploadPic("https://t10.baidu.com/it/u=4232360919,2367790879&fm=76");
		// String[] imageUrls =
		// {"http://www.ddhaoping.com/static/index/images/common/icon-2.png","http://www.ddhaoping.com/static/index/images/common/icon-6.png"};
		// http.submitComment("24305851613", "190810-632542986914080",
		// "给个好评，给个好评，给个好评，给个好评", imageUrls);
		// http.signature();
		// http.downloadCommentImage("190820-351952896894080", "D:/pddImage");
		/*String ddtzUrl = http.getShareUrl("52336483");
		String ddPid = ddtzUrl.substring(ddtzUrl.indexOf("pid=") + 4,
		ddtzUrl.indexOf("&", ddtzUrl.indexOf("pid=") + 4));
		String cpsSign = ddtzUrl.substring(ddtzUrl.indexOf("cpsSign=") + 4,
		ddtzUrl.indexOf("&", ddtzUrl.indexOf("cpsSign") + 4));
		System.out.println(ddPid);
		System.out.println(cpsSign);*/
		http.getShareUrl("27465278165");
		//http.myComments();
		// http.downloadCommentImage("190810-632542986914080");
		// http.productChromeDetail("2304301941");
		// http.queryOrderDetail("190827-252971254554080");
		// http.genAliPayUrl("190904-261356585754080");
		//
		// System.out.println(http.groups("5239354092"));;
		// http.productDetail("5239354092");
		// http.queryOrderDetail("190827-252971254554080");
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
	 * 商品详细信息
	 * 
	 * @param goodsId
	 * @return
	 */
	public JSONObject productDetail(String goodsId) {
		String url = "https://api.yangkeduo.com/api/oak/integration/render?pdduid=" + user.getUid();
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("User-Agent", user.getUserAgent());
		heads.put("Accept-Encoding", "gzip");
		heads.put("Content-Type", "application/json;charset=UTF-8");
		heads.put("AccessToken", user.getToken());
		heads.put("Cookie", "api_uid=" + user.getApiUid());
		String postData = "{\"goods_id\":" + goodsId + ",\"page_from\":2}";
		String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
		//logger.info("pddms-{}: 查询商品详细.{}", user.getUsername(), content);
		if (StringUtils.isNotBlank(content)) {
			JSONObject jsonObj = JSONObject.parseObject(content);
			return jsonObj;
		}
		return null;
	}

	public JSONArray groups(String goodsId) {
		String url = "https://api.yangkeduo.com/api/leibniz/goods/user/groups?pdduid=" + user.getUid();
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("User-Agent", user.getUserAgent());
		heads.put("Accept-Encoding", "gzip");
		heads.put("Content-Type", "application/json;charset=UTF-8");
		heads.put("AccessToken", user.getToken());
		heads.put("Cookie", "api_uid=" + user.getApiUid());
		String postData = "{\"goods_id\":" + goodsId + "}";
		String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
		// logger.info("pddms-{}: 查询拼组.{}", user.getUsername(), content);
		if (StringUtils.isNotBlank(content)) {
			JSONObject jsonObj = JSONObject.parseObject(content);
			JSONArray array = jsonObj.getJSONArray("local_group");
			JSONArray groups = new JSONArray();
			for (int i = 0; i < array.size(); i++) {
				JSONObject group = JSONObject.parseObject(array.getString(i));
				groups.add(group);
			}
			return groups;
		}
		return null;
	}

	/**
	 * 网页上的商品明细
	 * 
	 * @param goodsId
	 * @return
	 */
	public JSONObject productChromeDetail(String goodsId) {
		String url = "https://m.yangkeduo.com/goods.html?goods_id=" + goodsId;
		Map<String, String> heads = new HashMap<String, String>();
		// heads.put("User-Agent", user.getUserAgent());
		heads.put("Accept-Encoding", "gzip");
		heads.put("Content-Type", "application/json;charset=UTF-8");
		// heads.put("AccessToken", user.getToken());
		// heads.put("Cookie", "api_uid=" + user.getApiUid());
		String content = MobileHttpUrlConnectUtils.httpGet(url, heads, null);
		int start = content.indexOf("window.rawData=") + 15;
		int end = content.indexOf("}};", start) + 2;
		content = content.substring(start, end);
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
	public JSONObject submitAppOrder(String activityId, String groupId, String skuId, String goodsId,
			String groupOrderId) {
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
		// "attribute_fields":{"DUO_DUO_PID":"9078882_108793272","cps_sign":"CC_190911_9078882_108793272_3cfa846ecfaf4b1e3999d09b06423e8c"}
		int type = 0;
		// https://mobile.yangkeduo.com/duo_coupon_landing.html?goods_id=24679811633&pid=9078882_108793272&cpsSign=CC_190911_9078882_108793272_36a69db855e0890a3f1f18660182031d&duoduo_type=2
		String ddtzUrl = getShareUrl(goodsId);
		if (StringUtils.isNotBlank(ddtzUrl)) {
			type = 2;
			String ddPid = ddtzUrl.substring(ddtzUrl.indexOf("pid=") + 4,
					ddtzUrl.indexOf("&", ddtzUrl.indexOf("pid=") + 4));
			String cpsSign = ddtzUrl.substring(ddtzUrl.indexOf("cpsSign=") + 4,
					ddtzUrl.indexOf("&", ddtzUrl.indexOf("cpsSign") + 4));
			JSONObject attributeFields = new JSONObject(true);
			attributeFields.fluentPut("DUO_DUO_PID", ddPid).fluentPut("cps_sign", cpsSign);
			postData = postData.fluentPut("attribute_fields", attributeFields);
		}
		postData.fluentPut("address_id", user.getAddressId()).fluentPut("award_type", 0).fluentPut("biz_type", 0)
				.fluentPut("coupon_number", 0).fluentPut("type", type).fluentPut("goods", goods)
				.fluentPut("group_id", groupId).fluentPut("is_app", 1).fluentPut("page_from", "0")
				.fluentPut("pay_app_id", "35").fluentPut("source_type", 0).fluentPut("version", 1)
				.fluentPut("version_type", 1);
		if (StringUtils.isNotBlank(groupOrderId)) {
			postData.fluentPut("group_order_id", groupOrderId);
		}
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
	 * 多多团长转换
	 * 
	 * @param goodsId
	 * @return
	 */
	public String getDuoGroupId(String goodsId) {
		String url = "https://api.pinduoduo.com/api/jinbaoapp/wechat_auth/group/create?pdduid=" + user.getUid();
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("Accept", "*/*");
		heads.put("User-Agent",
				"Mozilla/5.0 (Linux; Android 7.1.2; MI 5X Build/N2G47H; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/66.0.3359.126 MQQBrowser/6.2 TBS/044904 Mobile Safari/537.36 MMWEBID/4288 MicroMessenger/7.0.6.1460(0x27000634) Process/tools NetType/WIFI Language/zh_CN");
		heads.put("Accept-Encoding", "gzip, deflate, br");
		heads.put("X-Requested-With", "com.tencent.mm");
		heads.put("Content-Type", "application/json;charset=UTF-8");
		heads.put("Cookie", "api_uid=" + user.getApiUid());
		heads.put("AccessToken", ddjbToken);
		heads.put("Referer", "Android");
		String postData = "{\"duoGroupCreateVO\":{\"groupType\":2,\"goodsId\":" + goodsId + "}}";
		String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
		logger.info("pddms-{}: 获取多多团长组id.{}", user.getUsername(), content);
		if (StringUtils.isNotBlank(content)) {
			JSONObject jsonObj = JSONObject.parseObject(content);
			if (jsonObj.getBooleanValue("success"))
				return jsonObj.getJSONObject("result").getString("duoGroupId");
			// {"success":true,"errorCode":1000000,"errorMsg":null,"result":{"duoGroupId":38867390,"cpsSign":"CF_190911_9078882_108793272_be8a36f1303a22121f638ea41685a19f"}}
		}
		return null;
	}

	public String getDuoSign() {
		String url = "https://api.pinduoduo.com/api/jinbao/wechat_auth/create_token_sign?pdduid=" + user.getUid();
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("Accept", "*/*");
		heads.put("User-Agent",
				"Mozilla/5.0 (Linux; Android 7.1.2; MI 5X Build/N2G47H; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/66.0.3359.126 MQQBrowser/6.2 TBS/044904 Mobile Safari/537.36 MMWEBID/4288 MicroMessenger/7.0.6.1460(0x27000634) Process/tools NetType/WIFI Language/zh_CN");
		heads.put("Accept-Encoding", "gzip, deflate, br");
		heads.put("X-Requested-With", "com.tencent.mm");
		heads.put("Content-Type", "application/json;charset=UTF-8");
		heads.put("Cookie", "api_uid=" + user.getApiUid());
		heads.put("AccessToken", ddjbToken);
		heads.put("Referer", "Android");
		String postData = "{}";
		String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
		logger.info("pddms-{}: 获取多多团长sign={}", user.getUsername(), content);
		if (StringUtils.isNotBlank(content)) {
			JSONObject jsonObj = JSONObject.parseObject(content);
			if (jsonObj.getBooleanValue("success"))
				return jsonObj.getJSONObject("result").getString("sign");
		}
		return null;
	}

	/**
	 * 多多团长商品推广转换
	 * 有问题，弃用
	 */
	public String getShareUrlOld(String goodsId) {
		try {
			String groupId = getDuoGroupId(goodsId);
			String tokenSign = getDuoSign();
			String url = "http://m.pin18pin.com/share_mode.html?duo_group_id=" + groupId + "&goods_id=" + goodsId
					+ "&last_refer=duo_conversion&token_sign=" + tokenSign;
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,image/wxpic,image/sharpp,image/apng,image/tpg,*/*;q=0.8");
			heads.put("Accept-Encoding", "gzip, deflate");
			heads.put("Accept-Language", "zh-CN,en-US;q=0.9");
			heads.put("User-Agent",
					"Mozilla/5.0 (Linux; Android 7.1.2; MI 5X Build/N2G47H; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/66.0.3359.126 MQQBrowser/6.2 TBS/044904 Mobile Safari/537.36 MMWEBID/4288 MicroMessenger/7.0.6.1460(0x27000634) Process/tools NetType/WIFI Language/zh_CN");
			heads.put("Cookie", "PDDAccessToken=" + ddjbToken + ";");
			String content = MobileHttpUrlConnectUtils.httpGet(url, heads, null);
			int start = content.indexOf("window.rawData=") + 15;
			int end = content.indexOf("}};", start) + 2;
			content = content.substring(start, end);
			JSONObject jsonObj = JSONObject.parseObject(content);
			logger.info("pddms-{}:查询分享商品址结果={}", user.getUsername(), jsonObj);
			String shareUrl = jsonObj.getJSONObject("store").getJSONObject("goods").getString("weAppWebViewUrl");
			logger.info("pddms-{}:查询分享商品址={}", user.getUsername(), shareUrl);
			return shareUrl;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 多多团长商品推广转换
	 */
	public String getShareUrl( String goodsId) {
		String url = "https://api.pinduoduo.com/api/jinbaoapp/wechat_auth/coupon/generate_url?pdduid=" + user.getUid();
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("Accept", "*/*");
		heads.put("Accept-Encoding", "gzip, deflate, br");
		heads.put("Content-Type", "application/json;charset=UTF-8");
		//heads.put("X-Requested-With", "com.tencent.mm");
		heads.put("User-Agent", "Mozilla/5.0 (Linux; Android 7.1.2; MI 5X Build/N2G47H; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/66.0.3359.126 MQQBrowser/6.2 TBS/044904 Mobile Safari/537.36 MMWEBID/4288 MicroMessenger/7.0.6.1460(0x27000634) Process/tools NetType/WIFI Language/zh_CN");
		heads.put("Cookie", "api_uid=" + user.getApiUid());
		heads.put("AccessToken", ddjbToken);
		heads.put("Origin", "http://m.pin18pin.com");
		heads.put("Referer", "Android");
		JSONObject postData = new JSONObject(true);
		postData.fluentPut("goodsId", goodsId).fluentPut("generateShortUrl", false).fluentPut("sectionId", null)
				.fluentPut("pageId", null).fluentPut("searchId", null);
		String content = MobileHttpUrlConnectUtils.httpPost(url, postData.toJSONString(), heads, null);
		logger.info("pddms-{}: 获取推广url结果.{}", user.getUsername(), content);
		if (StringUtils.isNotBlank(content)) {
			JSONObject jsonObj = JSONObject.parseObject(content);
			if(jsonObj.getBooleanValue("success"))
			return jsonObj.getJSONObject("result").getString("weAppWebViewUrl");
		}
		return null;
	}

	public String genAliPayUrl(String orderSn) {
		try {
			String url = "https://api.pinduoduo.com/order/prepay?pdduid=" + user.getUid();
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
			heads.put("X-PDD-QUERIES",
					"width=1080&height=1920&net=1&brand=xiaomi&model=MI+5X&osv=7.1.2&appv=4.33.0&pl=2");
			String returnurl = "https://m.yangkeduo.com/transac_wappay_callback.html?order_sn=" + orderSn;
			JSONObject pappay = new JSONObject(true);
			pappay.fluentPut("paid_times", 0).fluentPut("forbid_contractcode", "1").fluentPut("forbid_pappay", "1");
			JSONObject postData = new JSONObject(true);
			postData.fluentPut("pay_app_id", 9).fluentPut("version", 3).fluentPut("attribute_fields", pappay)
					.fluentPut("return_url", returnurl).fluentPut("order_sn", orderSn);
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData.toJSONString(), heads, null);
			logger.info("pddms-{}: 生成支付宝地址结果.{}", user.getUsername(), content);
			if (StringUtils.isNotBlank(content)) {
				// https://mapi.alipay.com/gateway.do?service=alipay.wap.create.direct.pay.by.user&partner=2088911201740274&seller_id=pddzhifubao%40yiran.com&payment_type=1&notify_url=http%3A%2F%2Fpayv3.yangkeduo.com%2Fnotify%2F9&out_trade_no=XP0019090410200461997673007674&subject=%E8%AE%A2%E5%8D%95%E7%BC%96%E5%8F%B7190904-204448727833236&total_fee=5.37&return_url=https%3A%2F%2Fm.yangkeduo.com%2Ftransac_wappay_callback.html%3Forder_sn%3D190904-204448727833236&sign=a1f1zTYs%2BfGKzf68hrwCs5TNAL%2F0hOsqr7xTYkK0%2BG%2B0w2HG%2BGN7hlnJOa%2BseW%2BCMcgyCYigvwsvoIzGzyNU%2BeJs%2F5XXtvnEM22Kv7b1LZB%2B7TYpPTsIFWYi2cQACQSIyCTFnu92fBSvBaSlua4rcYlmQZzp%2FBv0X9HCW8DjpfI%3D&sign_type=RSA&goods_type=1&_input_charset=utf-8
				// https://mapi.alipay.com/gateway.do?service=alipay.wap.create.direct.pay.by.user&partner=2088911201740274&seller_id=pddzhifubao@yiran.com&payment_type=1&notify_url=http://payv3.yangkeduo.com/notify/9&out_trade_no=XP0019090410200461997673007674&subject=è®¢åç¼å·190904-204448727833236&total_fee=5.37&return_url=https://m.yangkeduo.com/transac_wappay_callback.html?order_sn=190904-204448727833236&sign=a1f1zTYs+fGKzf68hrwCs5TNAL/0hOsqr7xTYkK0+G+0w2HG+GN7hlnJOa+seW+CMcgyCYigvwsvoIzGzyNU+eJs/5XXtvnEM22Kv7b1LZB+7TYpPTsIFWYi2cQACQSIyCTFnu92fBSvBaSlua4rcYlmQZzp/Bv0X9HCW8DjpfI=&sign_type=RSA&goods_type=1&_input_charset=utf-8
				JSONObject jsonObj = JSONObject.parseObject(content);
				String aliPayUrl = jsonObj.getString("gateway_url") + "?";
				JSONObject params = jsonObj.getJSONObject("query");
				Set<String> keys = params.keySet();
				for (String key : keys) {
					aliPayUrl += key + "=" + CommonUtils.encode(params.getString(key)) + "&";
				}
				aliPayUrl = aliPayUrl.substring(0, aliPayUrl.length() - 1);
				logger.info("pddms-{}: 生成支付宝地址结果.{}", user.getUsername(), aliPayUrl);
				return aliPayUrl;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
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
	 * 订单明细 payStatus 0:待支付 2:已付款 chatStatusPrompt 待支付 待分享 待收货 待评价 orderStatus 0:待支付
	 * 待分享 1:待收货 待评价 2:交易已取消
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
		System.out.println(content);
		int start = content.indexOf("window.rawData=") + 15;
		int end = content.indexOf("}};", start) + 2;
		content = content.substring(start, end);
		JSONObject jsonObj = JSONObject.parseObject(content);
		logger.info("pddms-{}:orderSn={},订单明细={}", user.getUsername(), orderSn, jsonObj);
		return jsonObj.getJSONObject("data");
	}

	public boolean isSign(JSONObject order) {
		JSONObject express = order.getJSONObject("expressInfo");
		if (express != null) {
			JSONArray traces = express.getJSONArray("traces");
			for (int i = 0; i < traces.size(); i++) {
				JSONObject trace = traces.getJSONObject(i);
				if (trace.getString("status").equals("SIGN"))
					return true;
			}
		}
		return false;
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
	 * 
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
		return jsonObj;
	}

	/**
	 * 
	 * 确认收货
	 */
	public JSONObject confirmReciveGoods(String orderSn) {
		// SIGN_ON_BEHALF 暂存 /SIGN已签收 SEND 送货
		String url = "https://api.pinduoduo.com/order/" + orderSn + "/received?pdduid=" + user.getUid() + "&is_back=1";
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("User-Agent", user.getUserAgent());
		heads.put("Accept", "*/*");
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
		postData.fluentPut("anti_content", "");
		String content = MobileHttpUrlConnectUtils.httpPost(url, postData.toJSONString(), heads, null);
		logger.info("pddms-{}: 确认收货结果.{}", user.getUsername(), content);
		if (StringUtils.isNotBlank(content)) {
			// {"server_time":1567419492,"quantity":null,"nickname":"177****7870","share_code":null}
			JSONObject jsonObj = JSONObject.parseObject(content);
			return jsonObj;
		}
		return null;
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
		// SIGN_ON_BEHALF 暂存 /SIGN已签收 SEND 送货

		JSONArray pictures = new JSONArray();
		if (imageUrls != null) {
			for (String imageUrl : imageUrls) {
				JSONObject uploadResult = uploadPic(imageUrl);
				if (uploadResult == null)
					return null;
				pictures.add(uploadResult);
			}
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
			// {"review_id":"241849880151973085","rating_id":241849880151973085,"masked_comment":"给个好评，给个好评，给个好评，给个好评"}
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

	/**
	 * 评论上传图片
	 * 
	 * @param imageUrl
	 * @return
	 */
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

	/**
	 * 下载评价照片 用html2image转换
	 * 
	 * @param orderSn
	 */
	public boolean downloadCommentImage(String orderSn, String pddImgPath) {
		String orginImg = pddImgPath + File.separator + orderSn + ".png";
		if (new File(orginImg).exists())
			return true;
		JSONObject json = myComments();
		JSONArray myComments = json.getJSONObject("store").getJSONArray("list");
		for (int i = 0; i < myComments.size(); i++) {
			JSONObject myComment = myComments.getJSONObject(i);
			JSONObject order = myComment.getJSONObject("order_info");
			if (orderSn.equals(order.getString("order_sn"))) {
				Map<String, Object> dataMap = new HashMap<String, Object>();
				dataMap.put("userImg", myComment.getString("avatar"));
				String pjTime = DateUtils.formatDate(new Date(myComment.getLongValue("time") * 1000), "yyyy.MM.dd");
				dataMap.put("pjTime", pjTime);
				List<String> pjImageUrls = new ArrayList<String>();
				JSONArray pictures = myComment.getJSONArray("pictures");
				int maxPicNum = pictures == null ? 0 : pictures.size() < 3 ? pictures.size() : 3;
				for (int j = 0; j < maxPicNum; j++) {
					JSONObject picture = pictures.getJSONObject(j);
					pjImageUrls.add(picture.getString("url"));
				}
				dataMap.put("pjImgs", pjImageUrls);
				dataMap.put("goodsImg", order.getString("goods_picture"));
				String goodsName = order.getString("goods_name");
				String goodsName01 = "";
				String goodsName02 = "";
				if (goodsName.length() > 38) {
					goodsName01 = goodsName.substring(0, 19);
					goodsName02 = goodsName.substring(19, 38);
				} else if (goodsName.length() > 19) {
					goodsName01 = goodsName.substring(0, 19);
					goodsName02 = goodsName.substring(19, goodsName.length());
				} else {
					goodsName01 = goodsName;
				}
				dataMap.put("goodsName01", goodsName01);// 19
				if (StringUtils.isNotBlank(goodsName02)) {
					dataMap.put("goodsName02", goodsName02);
				}
				JSONArray specs = JSONArray.parseArray(myComment.getString("specs"));
				String spec = "";
				for (int m = 0; m < specs.size(); m++) {
					spec += specs.getJSONObject(m).getString("spec_key") + ":"
							+ specs.getJSONObject(m).getString("spec_value");
				}
				String spec01 = "";
				String spec02 = "";
				if (spec.length() > 30) {
					spec01 = spec.substring(0, 15);
					spec02 = spec.substring(15, 30);
				} else if (spec.length() > 15) {
					spec01 = spec.substring(0, 15);
					spec02 = spec.substring(15, spec.length());
				} else {
					spec01 = spec;
				}
				dataMap.put("spec01", spec01);// 15
				dataMap.put("spec02", spec02);
				dataMap.put("orderAmount", order.getFloat("order_amount") / 100);
				String comment = myComment.getString("comment");
				String comment01 = "";
				String comment02 = "";
				String comment03 = "";
				if (comment.length() > 72) {
					comment01 = comment.substring(0, 24);
					comment02 = comment.substring(24, 48);
					comment03 = comment.substring(48, 72);
				} else if (comment.length() > 48) {
					comment01 = comment.substring(0, 24);
					comment02 = comment.substring(24, 48);
					comment03 = comment.substring(48, comment.length());
				} else if (comment.length() > 24) {
					comment01 = comment.substring(0, 24);
					comment02 = comment.substring(24, comment.length());
				} else {
					comment01 = comment;
				}
				dataMap.put("comment01", comment01);// 15
				dataMap.put("comment02", comment02);
				dataMap.put("comment03", comment03);
				String html = FreemarkerUtils.loadTemplate("pj.ftl", dataMap);
				FileUtils.htmlToImage(html, 900, orginImg);
				return true;
			}
		}
		return false;
	}

}
