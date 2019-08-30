package com.atao.dftt.http;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.atao.dftt.model.JkdttUser;
import com.atao.dftt.util.CommonUtils;
import com.atao.dftt.util.JkdttUtils;
import com.atao.dftt.util.MobileHttpUrlConnectUtils;
import com.atao.dftt.util.TaottUtils;
import com.atao.dftt.util.WlttUtils;
import com.atao.util.StringUtils;

public class JkdttHttp {
	private static Logger logger = LoggerFactory.getLogger(JkdttHttp.class);
	private static Map<String, JkdttHttp> userMap = new HashMap<String, JkdttHttp>();
	public JkdttUser user;
	public int page = 1;
	public static Random random = new Random();
	public String bdjssdkId = "f8488431";

	public JkdttHttp(JkdttUser user) {
		this.user = user;
	}
	
	public static synchronized JkdttHttp getInstance(JkdttUser user) {
		JkdttHttp http = (JkdttHttp) userMap.get(user.getUsername());
		if (http == null) {
			http = new JkdttHttp(user);
			userMap.put(user.getUsername(), http);
		}
		return http;
	}
	
	public JkdttHttp refreshUser(JkdttUser user) {
		JkdttHttp http = new JkdttHttp(user);
		userMap.put(user.getUsername(), http);
		return http;
	}

	public JSONObject login() {
		try {
			String url = "https://www.xiaodouzhuan.cn/jkd/minfo/call.action";
			JSONObject param = new JSONObject(true);
			JSONObject pars = new JSONObject(true);
			pars.fluentPut("mobile", user.getUsername()).fluentPut("openid", user.getOpenId())
					.fluentPut("os", user.getOs()).fluentPut("pwd", JkdttUtils.getPwd(user.getPassword()))
					.fluentPut("requestNum", 0).fluentPut("umengid", user.getUmengid())
					.fluentPut("uniqueid", user.getUniqueid());
			param.fluentPut("app_id", JkdttUtils.appid).fluentPut("app_token", JkdttUtils.apptoken)
					.fluentPut("channel", user.getChannel()).fluentPut("mobileinfo", user.getVendor())
					.fluentPut("openid", user.getOpenId()).fluentPut("os", user.getOs()).fluentPut("pars", pars)
					.fluentPut("sdktype", "bd_jssdk;bd_sdk;gdt_sdk;tt_sdk;sg_sdk;gdt_api;tt_api;zk_api")
					.fluentPut("vercode", JkdttUtils.appversioncode).fluentPut("version", JkdttUtils.appversion)
					.fluentPut("version_token", JkdttUtils.appversiontoken);
			String postData = "opttype=LOGIN_MB&jdata=" + CommonUtils.encode(param.toJSONString());
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", user.getUserAgent());
			heads.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			JSONObject object = JSONObject.parseObject(content);
			logger.info("jkdtt-{}:用户登陆结果,content={}", user.getUsername(), content);
			if ("ok".equals(object.getString("ret"))) {
				return object;
			}
			logger.error("jkdtt-{}:用户登陆异常,content={}", user.getUsername(), content);
		} catch (Exception e) {
			logger.error("jkdtt-{}:读取新闻异常,msg={}", user.getUsername(), e.getMessage());
		}
		return null;
	}
	
	/*public JSONObject login() {
		try {
			String url = "https://www.xiaodouzhuan.cn/jkd/login/userlogin.action";
			JSONObject param = new JSONObject(true);
			param.fluentPut("logout", "0").fluentPut("memid", "微鲤看看").fluentPut("smid", user.getSmid())
					.fluentPut("umengid", user.getUmengid()).fluentPut("uniqueid", user.getUniqueid()).fluentPut("upopenid", "").fluentPut("appid", JkdttUtils.appid)
					.fluentPut("apptoken", JkdttUtils.apptoken).fluentPut("appversion", JkdttUtils.appversion)
					.fluentPut("appversioncode", JkdttUtils.appversioncode).fluentPut("channel", user.getChannel())
					.fluentPut("mobileinfo", user.getVendor()).fluentPut("openid", "")
					.fluentPut("os", user.getOs())
					.fluentPut("sdktype", "bd_jssdk;bd_sdk;gdt_sdk;tt_sdk;sg_sdk;gdt_api;tt_api;zk_api;dk_api")
					.fluentPut("token", JkdttUtils.getToken(user));
			String postData = "jsondata=" + CommonUtils.encode(param.toJSONString());
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", user.getUserAgent());
			heads.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			JSONObject object = JSONObject.parseObject(content);
			if ("ok".equals(object.getString("ret"))) {
				return object;
			}
			logger.error("jkdtt-{}:用户登陆失败,content={}", user.getUsername(), content);
		} catch (Exception e) {
			logger.error("jkdtt-{}:读取新闻异常,msg={}", user.getUsername(), e.getMessage());
		}
		return null;
	}*/

	public JSONObject queryMyCoin() {
		try {
			String url = "https://www.xiaodouzhuan.cn/jkd/minfo/call.action";
			JSONObject param = new JSONObject(true);
			JSONObject pars = new JSONObject(true);
			pars.fluentPut("ispush", 1).fluentPut("openid", user.getOpenId()).fluentPut("os", user.getOs())
					.fluentPut("vercode", JkdttUtils.appversioncode);
			param.fluentPut("app_id", JkdttUtils.appid).fluentPut("app_token", JkdttUtils.apptoken)
					.fluentPut("channel", user.getChannel()).fluentPut("mobileinfo", user.getVendor())
					.fluentPut("openid", user.getOpenId()).fluentPut("os", user.getOs()).fluentPut("pars", pars)
					.fluentPut("sdktype", "bd_jssdk;bd_sdk;gdt_sdk;tt_sdk;sg_sdk;gdt_api;tt_api;zk_api")
					.fluentPut("vercode", JkdttUtils.appversioncode).fluentPut("version", JkdttUtils.appversion)
					.fluentPut("version_token", JkdttUtils.appversiontoken);
			String postData = "opttype=INF_ME&jdata=" + CommonUtils.encode(param.toJSONString());
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", user.getUserAgent());
			heads.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			JSONObject object = JSONObject.parseObject(content);
			if ("ok".equals(object.getString("ret"))) {
				return object;
			}
			logger.error("jkdtt-{}:查询今日获取的金币信息异常,msg={}", user.getUsername(), content);
		} catch (Exception e) {
			logger.error("jkdtt-{}:查询今日获取的金币信息异常,msg={}", user.getUsername(), e.getMessage());
		}
		return null;
	}

	public boolean qiandao() {
		try {
			String url = "https://www.xiaodouzhuan.cn/jkd/minfo/call.action";
			JSONObject param = new JSONObject(true);
			JSONObject pars = new JSONObject(true);
			pars.fluentPut("openid", user.getOpenId()).fluentPut("os", user.getOs()).fluentPut("vercode",
					JkdttUtils.appversioncode);
			param.fluentPut("app_id", JkdttUtils.appid).fluentPut("app_token", JkdttUtils.apptoken)
					.fluentPut("channel", user.getChannel()).fluentPut("mobileinfo", user.getVendor())
					.fluentPut("openid", user.getOpenId()).fluentPut("os", user.getOs()).fluentPut("pars", pars)
					.fluentPut("sdktype", "bd_jssdk;bd_sdk;gdt_sdk;tt_sdk;sg_sdk;gdt_api;tt_api;zk_api")
					.fluentPut("vercode", JkdttUtils.appversioncode).fluentPut("version", JkdttUtils.appversion)
					.fluentPut("version_token", JkdttUtils.appversiontoken);
			String postData = "optversion=1.0&opttype=HOME_SIGN&jdata=" + CommonUtils.encode(param.toJSONString());
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", user.getUserAgent());
			heads.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			JSONObject object = JSONObject.parseObject(content);
			logger.info("jkdtt-{}:用户签到结果,content={}", user.getUsername(), content);
			if ("ok".equals(object.getString("ret"))) {
				return true;
			}
			logger.error("jkdtt-{}:用户签到异常,content={}", user.getUsername(), content);
		} catch (Exception e) {
			logger.error("jkdtt-{}:用户签到异常,msg={}", user.getUsername(), e.getMessage());
		}
		return false;
	}

	public boolean daka() {
		try {
			String url = "https://www.xiaodouzhuan.cn/jkd/minfo/call.action";
			JSONObject param = new JSONObject(true);
			JSONObject pars = new JSONObject(true);
			pars.fluentPut("openid", user.getOpenId()).fluentPut("os", user.getOs()).fluentPut("vercode",
					JkdttUtils.appversioncode);
			param.fluentPut("app_id", JkdttUtils.appid).fluentPut("app_token", JkdttUtils.apptoken)
					.fluentPut("channel", user.getChannel()).fluentPut("mobileinfo", user.getVendor())
					.fluentPut("openid", user.getOpenId()).fluentPut("os", user.getOs()).fluentPut("pars", pars)
					.fluentPut("sdktype", "bd_jssdk;bd_sdk;gdt_sdk;tt_sdk;sg_sdk;gdt_api;tt_api;zk_api")
					.fluentPut("vercode", JkdttUtils.appversioncode).fluentPut("version", JkdttUtils.appversion)
					.fluentPut("version_token", JkdttUtils.appversiontoken);
			String postData = "optversion=1.0&opttype=HOME_BOX&jdata=" + CommonUtils.encode(param.toJSONString());
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", user.getUserAgent());
			heads.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			JSONObject object = JSONObject.parseObject(content);
			logger.info("jkdtt-{}:定时打卡结果,content={}", user.getUsername(), content);
			if ("ok".equals(object.getString("ret"))) {
				if (object.getJSONObject("datas").getIntValue("isShare") == 1) {
					// boolean share = dakaSharePost();
					Thread.sleep(3000);
					// if (share)
					dakaShare();
				}
				return true;
			}
			logger.error("jkdtt-{}:定时打卡异常,content={}", user.getUsername(), content);
		} catch (Exception e) {
			logger.error("jkdtt-{}:定时打卡异常,msg={}", user.getUsername(), e.getMessage());
		}
		return false;
	}

	public boolean dakaSharePost() {
		try {
			String url = "https://www.xiaodouzhuan.cn/jkd/share/share.action";
			JSONObject param = new JSONObject(true);
			param.fluentPut("artid", "").fluentPut("shareclass", "receive").fluentPut("sharetype", "timeline")
					.fluentPut("appid", JkdttUtils.appid).fluentPut("apptoken", JkdttUtils.apptoken)
					.fluentPut("appversion", JkdttUtils.appversion)
					.fluentPut("appversioncode", JkdttUtils.appversioncode).fluentPut("channel", user.getChannel())
					.fluentPut("mobileinfo", user.getVendor()).fluentPut("openid", user.getOpenId())
					.fluentPut("os", user.getOs())
					.fluentPut("sdktype", "bd_jssdk;bd_sdk;gdt_sdk;tt_sdk;sg_sdk;gdt_api;tt_api;zk_api;dk_api")
					.fluentPut("token", JkdttUtils.getToken(user));
			String postData = "jdata=" + CommonUtils.encode(param.toJSONString());
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", user.getUserAgent());
			heads.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			JSONObject object = JSONObject.parseObject(content);
			logger.info("jkdtt-{}:定时打卡分享请求结果,content={}", user.getUsername(), content);
			if ("ok".equals(object.getString("ret"))) {
				return true;
			}
			logger.error("jkdtt-{}:定时打卡分享请求异常,content={}", user.getUsername(), content);
		} catch (Exception e) {
			logger.error("jkdtt-{}:定时打卡分享异常,msg={}", user.getUsername(), e.getMessage());
		}
		return false;
	}

	public boolean dakaShare() {
		try {
			String url = "https://www.xiaodouzhuan.cn/jkd/minfo/call.action";
			JSONObject param = new JSONObject(true);
			JSONObject pars = new JSONObject(true);
			pars.fluentPut("openid", user.getOpenId()).fluentPut("os", user.getOs()).fluentPut("sharetype", "box");
			param.fluentPut("app_id", JkdttUtils.appid).fluentPut("app_token", JkdttUtils.apptoken)
					.fluentPut("channel", user.getChannel()).fluentPut("mobileinfo", user.getVendor())
					.fluentPut("openid", user.getOpenId()).fluentPut("os", user.getOs()).fluentPut("pars", pars)
					.fluentPut("sdktype", "bd_jssdk;bd_sdk;gdt_sdk;tt_sdk;sg_sdk;gdt_api;tt_api;zk_api")
					.fluentPut("vercode", JkdttUtils.appversioncode).fluentPut("version", JkdttUtils.appversion)
					.fluentPut("version_token", JkdttUtils.appversiontoken);
			String postData = "opttype=BOXSIGN_SHARE&jdata=" + CommonUtils.encode(param.toJSONString());
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", user.getUserAgent());
			heads.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			JSONObject object = JSONObject.parseObject(content);
			logger.info("jkdtt-{}:定时打卡分享结果,content={}", user.getUsername(), content);
			if ("ok".equals(object.getString("ret"))) {
				return true;
			}
			logger.error("jkdtt-{}:定时打卡分享异常,content={}", user.getUsername(), content);
		} catch (Exception e) {
			logger.error("jkdtt-{}:定时打卡分享异常,msg={}", user.getUsername(), e.getMessage());
		}
		return false;
	}

	public JSONArray newsList() {
		try {
			String url = "https://www.xiaodouzhuan.cn/jkd/newmobile/artlist.action";
			JSONObject param = new JSONObject(true);
			param.fluentPut("cateid", "3").fluentPut("optaction", "up").fluentPut("page", page)
					.fluentPut("pagesize", "12").fluentPut("searchtext", "").fluentPut("appid", JkdttUtils.appid)
					.fluentPut("apptoken", JkdttUtils.apptoken).fluentPut("appversion", JkdttUtils.appversion)
					.fluentPut("appversioncode", JkdttUtils.appversioncode).fluentPut("channel", user.getChannel())
					.fluentPut("mobileinfo", user.getVendor()).fluentPut("openid", user.getOpenId())
					.fluentPut("os", user.getOs())
					.fluentPut("sdktype", "bd_jssdk;bd_sdk;gdt_sdk;tt_sdk;sg_sdk;gdt_api;tt_api;zk_api;dk_api")
					.fluentPut("token", JkdttUtils.getToken(user));
			String postData = "jsondata=" + CommonUtils.encode(param.toJSONString());
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", user.getUserAgent());
			heads.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			// logger.info("jkdtt-{}:获取新闻列表,msg={}", user.getUsername(), content);
			JSONObject object = JSONObject.parseObject(content);
			if ("ok".equals(object.getString("ret"))) {
				JSONArray list = object.getJSONArray("artlist");
				JSONObject renderAdvert = object.getJSONObject("renderAdvert");
				if (renderAdvert != null)
					reportAd(renderAdvert);
				page++;
				return list;
			}
			logger.error("jkdtt-{}:获取新闻列表失败,msg={}", user.getUsername(), content);
		} catch (Exception e) {
			logger.error("jkdtt-{}:获取新闻列表异常,msg={}", user.getUsername(), e.getMessage());
		}
		return null;

	}

	public JSONObject readNews(String newsId) {
		try {
			String url = "https://www.xiaodouzhuan.cn/jkd/account/readAccount.action";
			JSONObject param = new JSONObject(true);
			param.fluentPut("artid", newsId).fluentPut("paytype", 1).fluentPut("readmodel", 1).fluentPut("readtime", 0)
					.fluentPut("securitykey", "").fluentPut("appid", JkdttUtils.appid)
					.fluentPut("apptoken", JkdttUtils.apptoken).fluentPut("appversion", JkdttUtils.appversion)
					.fluentPut("appversioncode", JkdttUtils.appversioncode).fluentPut("channel", user.getChannel())
					.fluentPut("mobileinfo", user.getVendor()).fluentPut("openid", user.getOpenId())
					.fluentPut("os", user.getOs())
					.fluentPut("sdktype", "bd_jssdk;bd_sdk;gdt_sdk;tt_sdk;sg_sdk;gdt_api;tt_api;zk_api;dk_api")
					.fluentPut("token", JkdttUtils.getToken(user));
			String postData = "jsondata=" + CommonUtils.encode(param.toJSONString());
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", user.getUserAgent());
			heads.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			heads.put("Cookie", "JSESSIONID=F14A9D1B424EE17D556EB4242B4E87D4");
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			JSONObject object = JSONObject.parseObject(content);
			return object;
		} catch (Exception e) {
			logger.error("jkdtt-{}:读取新闻异常,msg={}", user.getUsername(), e.getMessage());
		}
		return null;
	}

	public JSONObject readVideo(String newsId, String securitykey) {
		try {
			String url = "https://www.xiaodouzhuan.cn/jkd/account/readAccount.action";
			JSONObject param = new JSONObject(true);
			param.fluentPut("artid", newsId).fluentPut("paytype", 2).fluentPut("readmodel", 1).fluentPut("readtime", 30)
					.fluentPut("securitykey", securitykey).fluentPut("appid", JkdttUtils.appid)
					.fluentPut("apptoken", JkdttUtils.apptoken).fluentPut("appversion", JkdttUtils.appversion)
					.fluentPut("appversioncode", JkdttUtils.appversioncode).fluentPut("channel", user.getChannel())
					.fluentPut("mobileinfo", user.getVendor()).fluentPut("openid", user.getOpenId())
					.fluentPut("os", user.getOs())
					.fluentPut("sdktype", "bd_jssdk;bd_sdk;gdt_sdk;tt_sdk;sg_sdk;gdt_api;tt_api;zk_api;dk_api")
					.fluentPut("token", JkdttUtils.getToken(user));
			String postData = "jsondata=" + CommonUtils.encode(param.toJSONString());
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", user.getUserAgent());
			heads.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			JSONObject object = JSONObject.parseObject(content);
			return object;
		} catch (Exception e) {
			logger.error("jkdtt-{}:读取新闻视频异常,msg={}", user.getUsername(), e.getMessage());
		}
		return null;
	}

	public JSONObject readNewsLog(String newsId) {
		try {
			String url = "https://www.xiaodouzhuan.cn/jkd/minfo/call.action";
			JSONObject pars = new JSONObject(true);
			pars.fluentPut("openid", user.getOpenId()).fluentPut("os", user.getOs()).fluentPut("vercode",
					JkdttUtils.appversioncode);
			JSONObject param = new JSONObject(true);
			param.fluentPut("app_id", JkdttUtils.appid).fluentPut("app_token", JkdttUtils.apptoken)
					.fluentPut("channel", user.getChannel()).fluentPut("mobileinfo", user.getVendor())
					.fluentPut("openid", user.getOpenId()).fluentPut("os", user.getOs()).fluentPut("pars", pars)
					.fluentPut("sdktype", "bd_jssdk;bd_sdk;gdt_sdk;tt_sdk;sg_sdk;gdt_api;tt_api;zk_api")
					.fluentPut("vercode", JkdttUtils.appversioncode).fluentPut("version", JkdttUtils.appversion)
					.fluentPut("version_token", JkdttUtils.appversiontoken);
			String postData = "optversion=1.0&opttype=ART_READ&jdata=" + CommonUtils.encode(param.toJSONString());
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", user.getUserAgent());
			heads.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			// logger.info("jkdtt-{}:发送阅读新闻日志结果,content={}", user.getUsername(), content);
			JSONObject object = JSONObject.parseObject(content);
			if ("ok".equals(object.getString("ret"))) {
				return object;
			}
			logger.error("jkdtt-{}:发送阅读新闻日志失败,content={}", user.getUsername(), content);
		} catch (Exception e) {
			logger.error("jkdtt-{}:发送阅读新闻日志异常,msg={}", user.getUsername(), e.getMessage());
		}
		return null;
	}

	public void newsDetail(String url) {
		try {
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			heads.put("Accept-Encoding", "gzip, deflate");
			heads.put("Accept-Language", "zh-CN,en-US;q=0.9");
			heads.put("X-Requested-With", "com.xiangzi.jukandian");
			heads.put("Upgrade-Insecure-Requests", "1");
			heads.put("Pragma", "no-cache");
			heads.put("Cache-Control", "no-cache");
			heads.put("Cookie", "xz_jkd_appkey=" + user.getOpenId() + "!" + user.getOs() + "!"
					+ JkdttUtils.appversioncode + ";domain=www.xiaodouzhuan.cn; Path=/;");
			// heads.put("User-Agent", "Mozilla/5.0 (Linux; Android 7.1.2; MI 5X
			// Build/N2G47H; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0
			// Chrome/64.0.3282.137 Mobile Safari/537.36");
			String content = MobileHttpUrlConnectUtils.httpGet(url, heads, null);
			// logger.info("jkdtt-{}:查询新闻详细的,content={}", user.getUsername(), content);
		} catch (Exception e) {
			logger.error("jkdtt-{}:查询新闻详细的评论日志异常,msg={}", user.getUsername(), e.getMessage());
		}
	}

	public JSONObject commentListLog(String newsId) {
		try {
			String url = "https://www.xiaodouzhuan.cn/jkd/minfo/call.action";
			JSONObject pars = new JSONObject(true);
			pars.fluentPut("article_id", newsId).fluentPut("openid", user.getOpenId());
			JSONObject param = new JSONObject(true);
			param.fluentPut("app_id", JkdttUtils.appid).fluentPut("app_token", JkdttUtils.apptoken)
					.fluentPut("channel", user.getChannel()).fluentPut("mobileinfo", user.getVendor())
					.fluentPut("openid", user.getOpenId()).fluentPut("os", user.getOs()).fluentPut("pars", pars)
					.fluentPut("sdktype", "bd_jssdk;bd_sdk;gdt_sdk;tt_sdk;sg_sdk;gdt_api;tt_api;zk_api")
					.fluentPut("vercode", JkdttUtils.appversioncode).fluentPut("version", JkdttUtils.appversion)
					.fluentPut("version_token", JkdttUtils.appversiontoken);
			String postData = "opttype=INF_ART_COMMENTS&jdata=" + CommonUtils.encode(param.toJSONString());
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", user.getUserAgent());
			heads.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			JSONObject object = JSONObject.parseObject(content);
			if ("ok".equals(object.getString("ret"))) {
				return object;
			}
			logger.error("jkdtt-{}:查询新闻详细的评论日志失败,content={}", user.getUsername(), content);

		} catch (Exception e) {
			logger.error("jkdtt-{}:查询新闻详细的评论日志异常,msg={}", user.getUsername(), e.getMessage());
		}
		return null;
	}

	public JSONObject reportAd(String ad_name, String ad_type_name, String adid, String ads_name, String advpositionid,
			String appid, String image_type, String import_type, String title) {
		try {
			String url = "https://www.xiaodouzhuan.cn/jkd/minfo/call.action";
			JSONObject pars = JkdttUtils.initPars(user, ad_name, ad_type_name, adid, ads_name, advpositionid, appid,
					image_type, import_type, title);
			JSONObject param = new JSONObject(true);
			param.fluentPut("app_id", JkdttUtils.appid).fluentPut("app_token", JkdttUtils.apptoken)
					.fluentPut("channel", user.getChannel()).fluentPut("mobileinfo", user.getVendor())
					.fluentPut("openid", user.getOpenId()).fluentPut("os", user.getOs()).fluentPut("pars", pars)
					.fluentPut("sdktype", "bd_jssdk;bd_sdk;gdt_sdk;tt_sdk;sg_sdk;gdt_api;tt_api;zk_api")
					.fluentPut("vercode", JkdttUtils.appversioncode).fluentPut("version", JkdttUtils.appversion)
					.fluentPut("version_token", JkdttUtils.appversiontoken);
			String postData = "optversion=1.0&opttype=AD_EXPOSURE&jdata=" + CommonUtils.encode(param.toJSONString());
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", user.getUserAgent());
			heads.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			// logger.info("jkdtt-{}:发送广告结果,content={}", user.getUsername(), content);
			JSONObject object = JSONObject.parseObject(content);
			if ("ok".equals(object.getString("ret"))) {
				return object;
			}
			logger.error("jkdtt-{}:发送广告失败,content={}", user.getUsername(), content);
		} catch (Exception e) {
			logger.error("jkdtt-{}:发送广告异常,msg={}", user.getUsername(), e.getMessage());
		}
		return null;
	}

	public JSONObject reportAd(JSONObject ad) {
		try {
			String url = "https://www.xiaodouzhuan.cn/jkd/minfo/call.action";
			JSONObject pars = null;
			if ("baidu".equals(ad.getString("provider_code"))) {
				pars = JkdttUtils.initPars(user, "bd_sdk", "SDK", "", "百度", ad.getString("ad_posid"),
						ad.getString("ad_appid"), ad.getString("ad_pic_mode"), "listview_a", JkdttUtils.getTitle());
			} else if ("gdt".equals(ad.getString("provider_code"))) {
				pars = JkdttUtils.initPars(user, "gdt_sdk", "SDK", "", "广点通", ad.getString("ad_posid"),
						ad.getString("ad_appid"), ad.getString("ad_pic_mode"), "listview_a", JkdttUtils.getTitle());
			} else if ("toutiao".equals(ad.getString("provider_code"))) {
				pars = JkdttUtils.initPars(user, "tt_sdk", "SDK", "", "头条", ad.getString("ad_posid"),
						ad.getString("ad_appid"), ad.getString("ad_pic_mode"), "listview_a", JkdttUtils.getTitle());
			} else {
				logger.error("jkdtt-{}:发送广告数据有问题,content={}", user.getUsername(), ad.toJSONString());
				return null;
			}
			JSONObject param = new JSONObject(true);
			param.fluentPut("app_id", JkdttUtils.appid).fluentPut("app_token", JkdttUtils.apptoken)
					.fluentPut("channel", user.getChannel()).fluentPut("mobileinfo", user.getVendor())
					.fluentPut("openid", user.getOpenId()).fluentPut("os", user.getOs()).fluentPut("pars", pars)
					.fluentPut("sdktype", "bd_jssdk;bd_sdk;gdt_sdk;tt_sdk;sg_sdk;gdt_api;tt_api;zk_api")
					.fluentPut("vercode", JkdttUtils.appversioncode).fluentPut("version", JkdttUtils.appversion)
					.fluentPut("version_token", JkdttUtils.appversiontoken);
			String postData = "optversion=1.0&opttype=AD_EXPOSURE&jdata=" + CommonUtils.encode(param.toJSONString());
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", user.getUserAgent());
			heads.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			// logger.info("jkdtt-{}:发送广告结果,content={}", user.getUsername(), content);
			JSONObject object = JSONObject.parseObject(content);
			if ("ok".equals(object.getString("ret"))) {
				return object;
			}
			logger.error("jkdtt-{}:发送广告失败,content={}", user.getUsername(), content);
		} catch (Exception e) {
			logger.error("jkdtt-{}:发送广告异常,msg={}", user.getUsername(), e.getMessage());
		}
		return null;
	}

	public JSONObject videoDetail(JSONObject video) {
		try {
			String url = "https://www.xiaodouzhuan.cn/jkd/newmobile/artDetail.action";
			JSONObject param = new JSONObject(true);
			param.fluentPut("artid", video.getString("art_id")).fluentPut("comments", video.getIntValue("comments"))
					.fluentPut("relate", 1).fluentPut("requestid", video.getString("request_id"))
					.fluentPut("scenetype", "art_recommend").fluentPut("appid", JkdttUtils.appid)
					.fluentPut("apptoken", JkdttUtils.apptoken).fluentPut("appversion", JkdttUtils.appversion)
					.fluentPut("appversioncode", JkdttUtils.appversioncode).fluentPut("channel", user.getChannel())
					.fluentPut("mobileinfo", user.getVendor()).fluentPut("openid", user.getOpenId())
					.fluentPut("os", user.getOs())
					.fluentPut("sdktype", "bd_jssdk;bd_sdk;gdt_sdk;tt_sdk;sg_sdk;gdt_api;tt_api;zk_api;dk_api")
					.fluentPut("token", JkdttUtils.getToken(user));
			String postData = "jsondata=" + CommonUtils.encode(param.toJSONString());
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", "okhttp/3.8.1");
			heads.put("Content-Type", "application/x-www-form-urlencoded");
			heads.put("appsign", "xzwlsign");
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			JSONObject object = JSONObject.parseObject(content);
			if ("ok".equals(object.getString("ret"))) {
				JSONObject renderAdvert = object.getJSONObject("render_advert2");
				if (renderAdvert != null)
					reportAd(renderAdvert);
				return object;
			}
			logger.error("jkdtt-{}:获取视频详细页面广告列表失败,msg={}", user.getUsername(), content);
		} catch (Exception e) {
			logger.error("jkdtt-{}:获取视频详细页面广告列表异常,msg={}", user.getUsername(), e.getMessage());
		}
		return null;

	}

	/**
	 * 一次提现10元
	 */
	public boolean cointx() {
		try {
			String url = "https://www.xiaodouzhuan.cn/jkd/weixin20/userWithdraw/userWithdrawPost.action";
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept", "*/*");
			heads.put("Accept-Encoding", "gzip, deflate");
			heads.put("Accept-Language", "zh-CN,en-US;q=0.9");
			heads.put("X-Requested-With", "XMLHttpRequest");
			heads.put("Pragma", "no-cache");
			heads.put("Cache-Control", "no-cache");
			heads.put("Cookie", "xz_jkd_appkey=" + user.getOpenId() + "!" + user.getOs() + "!"
					+ JkdttUtils.appversioncode + ";domain=www.xiaodouzhuan.cn; Path=/;");
			heads.put("User-Agent", user.getUserAgent());
			heads.put("Referer", "https://www.xiaodouzhuan.cn/jkd/weixin20/userWithdraw/userWithdraw.action");
			heads.put("Origin", "https://www.xiaodouzhuan.cn");
			String postData = "type=wx&sum=10&mobile=%E6%8F%90%E7%8E%B0%E6%88%90%E5%8A%9F";
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			if (StringUtils.isNotBlank(content) && content.contains("提现已受理")) {
				logger.info("jkdtt-{}:微信提现成功,content={}", user.getUsername(), content);
				return true;
			}
			logger.error("jkdtt-{}:微信提现失败,msg={}", user.getUsername(), content);
		} catch (Exception e) {
			logger.error("jkdtt-{}:微信提现异常,msg={}", user.getUsername(), e.getMessage());
		}
		return false;
	}

	public static void main(String[] args) {
		JkdttUser user = new JkdttUser();
		user.setUsername("17755117870");
		JkdttHttp http = JkdttHttp.getInstance(user);
	}

}
