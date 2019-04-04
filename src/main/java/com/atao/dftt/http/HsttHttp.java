package com.atao.dftt.http;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.atao.dftt.model.HsttUser;
import com.atao.dftt.util.HsttUtils;
import com.atao.dftt.util.MobileHttpUrlConnectUtils;

public class HsttHttp {
	private static Logger logger = LoggerFactory.getLogger(HsttHttp.class);
	private static Map<String, HsttHttp> userMap = new HashMap<String, HsttHttp>();
	public HsttUser user;
	public static Random random = new Random();
	public int s = 1;
	public int times = 0;
	public int timeswatch = 0;
	public int readHotWordNum = 0;
	public int readBaozNum = 0;

	public HsttHttp(HsttUser user) {
		this.user = user;
	}

	public static synchronized HsttHttp getInstance(HsttUser user) {
		HsttHttp http = (HsttHttp) userMap.get(user.getUsername());
		if (http == null) {
			http = new HsttHttp(user);
			userMap.put(user.getUsername(), http);
		}
		return http;
	}

	public HsttHttp refreshUser(HsttUser user) {
		HsttHttp http = new HsttHttp(user);
		userMap.put(user.getUsername(), http);
		return http;
	}

	public JSONObject login() {
		try {
			String url = "http://api.zizhengjiankang.com/kk-api-v112/user/loginByPasswordV2";
			JSONObject data = new JSONObject(true);
			data.fluentPut("tel_num", user.getUsername()).fluentPut("pwd", user.getPassword())
					.fluentPut("mac", user.getImei() + "_" + user.getImsi() + "_" + user.getMac())
					.fluentPut("version", "update" + HsttUtils.appversion);
			String postData = data.toJSONString();
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", "okhttp/3.8.1");
			heads.put("Content-Type", "text/plain;charset=utf-8");
			String token = HsttUtils.getToken("|#loginByPasswordV2");
			heads.put("token", token);
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			content = HsttUtils.decode(content);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getIntValue("status") == 100) {
				logger.info("hstt-{}:用户登陆成功,result={}", user.getUsername(), object.toString());
				return object;
			}
			logger.error("hstt-{}:用户登陆时失败,result={}", user.getUsername(), content);
		} catch (Exception e) {
			logger.error("hstt-{}:用户登陆时发生异常!{}", user.getUsername(), e.getMessage());
		}
		return null;
	}

	public JSONObject queryMyCoin() {
		try {
			String url = "http://api.zizhengjiankang.com/kk-api-v112/reward/getRewardListV2";
			String postData = "tel_num=" + user.getUsername();
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", "okhttp/3.8.1");
			heads.put("Content-Type", "application/x-www-form-urlencoded");
			String token = HsttUtils.getToken(user.getUsername() + "|#getRewardListV2");
			heads.put("token", token);
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			content = HsttUtils.decode(content);
			JSONObject object = JSONObject.parseObject(content);
			return object;
		} catch (Exception e) {
			logger.error("hstt-{}:查询今日获取的金币信息异常,msg={}", user.getUsername(), e.getMessage());
		}
		return null;
	}

	public boolean qiandao() {
		try {
			String url = "http://api.zizhengjiankang.com/kk-api-v112/update/signV4?uid=" + user.getUsername();
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", "okhttp/3.8.1");
			String content = MobileHttpUrlConnectUtils.httpGet(url, heads, null);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getIntValue("status") == 100) {
				return true;
			}
			logger.error("hstt-{}:签到失败,content={}", user.getUsername(), content);
		} catch (Exception e) {
			logger.error("hstt-{}:签到异常,msg={}", user.getUsername(), e.getMessage());
		}
		return false;
	}

	public boolean daka() {
		try {
			String url = "http://api.zizhengjiankang.com/kk-api-v112/update/timewardV2";
			JSONObject data = new JSONObject(true);
			JSONObject app = new JSONObject(true);
			app.fluentPut("appId", "9eb8ef4a").fluentPut("appPackage", "com.xcm.huasheng")
					.fluentPut("appVersion", HsttUtils.appversion).fluentPut("appName", "com.xcm.huasheng");
			JSONObject device = new JSONObject(true);
			device.fluentPut("imei", user.getImei()).fluentPut("mac", user.getMac())
					.fluentPut("androidId", user.getAndroidId()).fluentPut("model", user.getModel())
					.fluentPut("vendor", user.getVendor()).fluentPut("screenWidth", 1080)
					.fluentPut("screenHeight", 1920).fluentPut("osType", 1).fluentPut("osVersion", user.getOsVersion())
					.fluentPut("deviceType", 1).fluentPut("ua", user.getUserAgent()).fluentPut("ppi", 480)
					.fluentPut("screenOrientation", 1).fluentPut("brand", user.getBrand())
					.fluentPut("imsi", user.getImsi());
			JSONObject network = new JSONObject(true);
			network.fluentPut("ip", "192.168.1.100").fluentPut("connectionType", 100).fluentPut("operatorType", 99)
					.fluentPut("cellular_id", 0).fluentPut("lat", 0).fluentPut("lon", 0);
			JSONObject slot = new JSONObject(true);
			slot.fluentPut("slotId", "5831418").fluentPut("slotheight", "720").fluentPut("slotwidth", "1280");
			data.fluentPut("requestId", user.getRequestId() + "_" + System.currentTimeMillis() + "_0001")
					.fluentPut("protocolType", 1).fluentPut("app", app).fluentPut("device", device)
					.fluentPut("network", network).fluentPut("slot", slot).fluentPut("tel_num", user.getUsername())
					.fluentPut(user.getBrand(), user.getBrand()).fluentPut("tag", "1").fluentPut("canalId", "update")
					.fluentPut("macID", user.getImei() + "_" + user.getImsi() + "_" + user.getMac());
			String postData = data.toJSONString();
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", "okhttp/3.8.1");
			heads.put("Content-Type", "application/json; charset=utf-8");
			String token = HsttUtils.getToken("|#timewardV2");
			heads.put("token", token);
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			content = HsttUtils.decode(content);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getIntValue("status") == 100) {
				JSONObject ad = object.getJSONObject("ydtAd");
				if (ad != null) {
					reportAd(ad);
				}
				if(extraReward()) {
					readAd(ad);
				}
				return true;
			}
			logger.error("hstt:{}-打卡失败,msg={}", user.getUsername(), content);
		} catch (Exception e) {
			logger.error("hstt:{}-打卡异常,msg={}", user.getUsername(), e.getMessage());
		}
		return false;
	}
	
	public boolean extraReward() {
		try {
			String url = "http://api.zizhengjiankang.com/kk-api-v112/update/extraReward";
			
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", "okhttp/3.8.1");
			heads.put("Content-Type", "application/x-www-form-urlencoded");
			String token = HsttUtils.getToken(user.getUsername() + "|#extraReward");
			heads.put("token", token);
			String postData = "uid=" + user.getUsername();
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			content = HsttUtils.decode(content);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getIntValue("status") == 100) {
				return true;
			}
			logger.error("hstt:{}-额外奖励失败,msg={}", user.getUsername(), content);
		} catch (Exception e) {
			logger.error("hstt:{}-额外奖励异常,msg={}", user.getUsername(), e.getMessage());
		}
		return false;
	}


	public JSONObject openBox() {
		try {
			String url = "http://api.zizhengjiankang.com/kk-api-v112/app/getBaoZangV2";
			String postData = "tel_num=" + user.getUsername();
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", "okhttp/3.8.1");
			heads.put("Content-Type", "application/x-www-form-urlencoded");
			String token = HsttUtils.getToken(user.getUsername() + "|#getBaoZangV2");
			heads.put("token", token);
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			JSONObject object = JSONObject.parseObject(content);
			return object;
		} catch (Exception e) {
			logger.error("hstt-{}:开宝箱,msg={}", user.getUsername(), e.getMessage());
		}
		return null;
	}

	public JSONObject hotwordV2() {
		try {
			String url = "http://api.zizhengjiankang.com/kk-api-v112/index/hotwordV2?uid=" + user.getUsername();
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", "okhttp/3.8.1");
			String token = HsttUtils.getToken(user.getUsername() + "|#hotwordV2");
			heads.put("token", token);
			String content = MobileHttpUrlConnectUtils.httpGet(url, heads, null);
			content = HsttUtils.decode(content);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getIntValue("status") == 100) {
				return object;
			}
			logger.error("hstt-{}:查询热门搜索失败,content={}", user.getUsername(), content);
		} catch (Exception e) {
			logger.error("hstt-{}:查询今日获取的金币信息异常,msg={}", user.getUsername(), e.getMessage());
		}
		return null;
	}

	public JSONObject searchV2() {
		try {
			String url = "http://api.zizhengjiankang.com/kk-api-v112/update/searchV2";
			String postData = "uid=" + user.getUsername();
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", "okhttp/3.8.1");
			heads.put("Content-Type", "application/x-www-form-urlencoded");
			String token = HsttUtils.getToken(user.getUsername() + "|#searchV2");
			heads.put("token", token);
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			content = HsttUtils.decode(content);
			JSONObject object = JSONObject.parseObject(content);
			return object;
		} catch (Exception e) {
			logger.error("hstt-{}:查询今日获取的金币信息异常,msg={}", user.getUsername(), e.getMessage());
		}
		return null;
	}

	public JSONObject searchrewardV3(int type) {
		try {
			String url = "http://api.zizhengjiankang.com/kk-api-v112/update/searchrewardV3";
			String postData = "uid=" + user.getUsername() + "&type=" + type;
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", "okhttp/3.8.1");
			heads.put("Content-Type", "application/x-www-form-urlencoded");
			String token = HsttUtils.getToken(user.getUsername() + type + "|#searchrewardV3");
			heads.put("token", token);
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			content = HsttUtils.decode(content);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getIntValue("status") == 100) {
				logger.info("hstt-{}:领取搜索奖励成功,type={},content={}", user.getUsername(), type, object);
				return object;
			}
		} catch (Exception e) {
			logger.error("hstt-{}:查询今日获取的金币信息异常,msg={}", user.getUsername(), e.getMessage());
		}
		return null;
	}

	public boolean userBehavior(String sensor, String type) {
		try {
			String url = "http://api.zizhengjiankang.com/kk-api-v112/statistics/user_behavior";
			JSONObject data = new JSONObject(true);
			data.fluentPut("imei", user.getImei()).fluentPut("sensor", sensor).fluentPut("tel_num", user.getUsername())
					.fluentPut("type", type);
			String postData = data.toJSONString();
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", "okhttp/3.8.1");
			heads.put("Content-Type", "application/json; charset=utf-8");
			int code = MobileHttpUrlConnectUtils.httpPostStatusCode(url, postData, heads, null);
			if (code == 200) {
				// logger.info("hstt-{}:上报{}行为成功,msg={}", user.getUsername(), type, code);
				return true;
			}
			logger.error("hstt-{}:上报{}行为失败,msg={}", user.getUsername(), type, code);
		} catch (Exception e) {
			logger.error("hstt-{}:上报{}行为异常,msg={}", user.getUsername(), type, e.getMessage());
		}
		return false;
	}

	/**
	 * mob log
	 * 
	 * @return
	 */
	public boolean mobCdata(JSONArray datas) {
		try {
			String url = "http://c.data.mob.com/v3/cdata";
			JSONObject mobLog = HsttUtils.initMobLog(user, datas);
			String m = HsttUtils.mobEncode(mobLog.toJSONString());
			String postData = "appkey=" + HsttUtils.appkey + "&m=" + m;
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", user.getMobUseragent());
			heads.put("Content-Type", "application/x-www-form-urlencoded");
			heads.put("User-Identity", HsttUtils.getUserIdentity(user));
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getIntValue("status") == 200) {
				return true;
			}
			logger.error("hstt-{}:上报mob日志数据结果失败,content={}", user.getUsername(), content);
		} catch (Exception e) {
			logger.error("hstt-{}:上报app启动动作异常,msg={}", user.getUsername(), e.getMessage());
		}
		return false;

	}

	public boolean moblog4(String m) {
		try {
			String url = "http://api.share.mob.com/log4";
			String postData = "m=" + m + "&t=0";
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", user.getMobUseragent());
			heads.put("Content-Type", "application/x-www-form-urlencoded");
			heads.put("User-Identity", HsttUtils.getUserIdentity(user));
			int code = MobileHttpUrlConnectUtils.httpPostStatusCode(url, postData, heads, null);
			if (code == 200) {
				return true;
			}
			logger.error("hstt-{}:上报moblog4结果失败,code={}", user.getUsername(), code);
		} catch (Exception e) {
			logger.error("hstt-{}:上报app启动动作异常,msg={}", user.getUsername(), e.getMessage());
		}
		return false;

	}

	/**
	 * app启动时发送日志
	 * 
	 * @return
	 */
	public boolean getApp() {
		try {
			String url = "http://api.zizhengjiankang.com/kk-api-v112/statistics/getApp";
			JSONObject data = new JSONObject(true);
			String appList = "[{\"appName\":\"最右\",\"pkgName\":\"cn.xiaochuankeji.tieba\",\"version\":\"407030\"},{\"appName\":\"淘头条\",\"pkgName\":\"com.ly.taotoutiao\",\"version\":\"233\"},{\"appName\":\"ofo共享单车\",\"pkgName\":\"so.ofo.labofo\",\"version\":\"17755\"},{\"appName\":\"小米直播助手\",\"pkgName\":\"com.mi.liveassistant\",\"version\":\"452001\"},{\"appName\":\"我的小米\",\"pkgName\":\"com.xiaomi.vipaccount\",\"version\":\"1135\"},{\"appName\":\"芝麻头条\",\"pkgName\":\"cn.com.funmeet.news\",\"version\":\"1502\"},{\"appName\":\"王者荣耀\",\"pkgName\":\"com.tencent.tmgp.sgame\",\"version\":\"42010604\"},{\"appName\":\"爱奇艺\",\"pkgName\":\"com.qiyi.video\",\"version\":\"81200\"},{\"appName\":\"趣头条\",\"pkgName\":\"com.jifen.qukan\",\"version\":\"30902000\"},{\"appName\":\"中国农业银行\",\"pkgName\":\"com.android.bankabc\",\"version\":\"33\"},{\"appName\":\"韭黄头条\",\"pkgName\":\"com.jiuhuang.news\",\"version\":\"108\"},{\"appName\":\"美团\",\"pkgName\":\"com.sankuai.meituan\",\"version\":\"900100401\"},{\"appName\":\"优酷视频\",\"pkgName\":\"com.youku.phone\",\"version\":\"177\"},{\"appName\":\"QQ音乐\",\"pkgName\":\"com.tencent.qqmusic\",\"version\":\"913\"},{\"appName\":\"游戏服务\",\"pkgName\":\"com.xiaomi.gamecenter.sdk.service\",\"version\":\"510200\"},{\"appName\":\"语音设置\",\"pkgName\":\"com.iflytek.speechcloud\",\"version\":\"10053\"},{\"appName\":\"QQ邮箱\",\"pkgName\":\"com.tencent.androidqqmail\",\"version\":\"10134093\"},{\"appName\":\"搜索\",\"pkgName\":\"com.android.quicksearchbox\",\"version\":\"20180906\"},{\"appName\":\"猎豹清理大师\",\"pkgName\":\"com.cleanmaster.mguard_cn\",\"version\":\"60791066\"},{\"appName\":\"米币支付\",\"pkgName\":\"com.xiaomi.payment\",\"version\":\"1046026\"},{\"appName\":\"宝宝树孕育\",\"pkgName\":\"com.babytree.apps.pregnancy\",\"version\":\"588\"},{\"appName\":\"铁路12306\",\"pkgName\":\"com.MobileTicket\",\"version\":\"194\"},{\"appName\":\"浏览器\",\"pkgName\":\"com.android.browser\",\"version\":\"20181116\"},{\"appName\":\"msa\",\"pkgName\":\"com.miui.systemAdSolution\",\"version\":\"2018120503\"},{\"appName\":\"大众头条\",\"pkgName\":\"com.build.dazhong\",\"version\":\"15\"},{\"appName\":\"小米风行播放插件\",\"pkgName\":\"com.funshion.video.player\",\"version\":\"20\"},{\"appName\":\"网易云课堂\",\"pkgName\":\"com.netease.edu.study\",\"version\":\"2018121017\"},{\"appName\":\"百度输入法小米版\",\"pkgName\":\"com.baidu.input_mi\",\"version\":\"467\"},{\"appName\":\"百度输入法小米版\",\"pkgName\":\"com.baidu.input_miv6\",\"version\":\"216\"},{\"appName\":\"安徽专技在线\",\"pkgName\":\"com.witfore.xxapp.ahzj\",\"version\":\"6\"},{\"appName\":\"饿了么\",\"pkgName\":\"me.ele\",\"version\":\"309\"},{\"appName\":\"优步-Uber\",\"pkgName\":\"com.didi.echo\",\"version\":\"84\"},{\"appName\":\"交通银行\",\"pkgName\":\"com.bankcomm.Bankcomm\",\"version\":\"3211\"},{\"appName\":\"微信\",\"pkgName\":\"com.tencent.mm\",\"version\":\"1360\"},{\"appName\":\"mab\",\"pkgName\":\"com.xiaomi.ab\",\"version\":\"138\"},{\"appName\":\"小米金融\",\"pkgName\":\"com.xiaomi.jr\",\"version\":\"98\"},{\"appName\":\"合肥掌上公交\",\"pkgName\":\"com.keli.hfbussecond\",\"version\":\"223\"},{\"appName\":\"小爱语音引擎\",\"pkgName\":\"com.xiaomi.mibrain.speech\",\"version\":\"11\"},{\"appName\":\"TalkBack\",\"pkgName\":\"com.google.android.marvin.talkback\",\"version\":\"60100343\"},{\"appName\":\"起点读书\",\"pkgName\":\"com.qidian.QDReader\",\"version\":\"368\"},{\"appName\":\"语音识别设置\",\"pkgName\":\"com.baidu.duersdk.opensdk\",\"version\":\"23\"},{\"appName\":\"滴滴出行\",\"pkgName\":\"com.sdu.didi.psnger\",\"version\":\"461\"},{\"appName\":\"中国银行\",\"pkgName\":\"com.chinamworld.bocmbci\",\"version\":\"100\"},{\"appName\":\"应用宝\",\"pkgName\":\"com.tencent.android.qqdownloader\",\"version\":\"7171130\"},{\"appName\":\"快应用\",\"pkgName\":\"com.miui.hybrid\",\"version\":\"10200301\"},{\"appName\":\"百度\",\"pkgName\":\"com.baidu.searchbox\",\"version\":\"46421248\"},{\"appName\":\"淘宝联盟\",\"pkgName\":\"com.alimama.moon\",\"version\":\"612000\"},{\"appName\":\"快狗打车(原58速运)\",\"pkgName\":\"com.wuba.huoyun\",\"version\":\"5038\"},{\"appName\":\"点点新闻\",\"pkgName\":\"com.yingliang.clicknews\",\"version\":\"109000\"},{\"appName\":\"小米画报\",\"pkgName\":\"com.mfashiongallery.emag\",\"version\":\"2018103130\"},{\"appName\":\"58同城\",\"pkgName\":\"com.wuba\",\"version\":\"81401\"},{\"appName\":\"音乐\",\"pkgName\":\"com.miui.player\",\"version\":\"10351\"},{\"appName\":\"MIUI论坛\",\"pkgName\":\"com.miui.miuibbs\",\"version\":\"304\"},{\"appName\":\"全国汽车票\",\"pkgName\":\"com.wxws.myticket\",\"version\":\"90\"},{\"appName\":\"万能遥控\",\"pkgName\":\"com.duokan.phone.remotecontroller\",\"version\":\"581\"},{\"appName\":\"网易火车票\",\"pkgName\":\"com.netease.railwayticket\",\"version\":\"40501\"},{\"appName\":\"淘新闻\",\"pkgName\":\"com.coohua.xinwenzhuan\",\"version\":\"30322\"},{\"appName\":\"拼多多\",\"pkgName\":\"com.xunmeng.pinduoduo\",\"version\":\"43300\"},{\"appName\":\"FDex2\",\"pkgName\":\"formatfa.xposed.Fdex2\",\"version\":\"1\"},{\"appName\":\"爱淘优惠\",\"pkgName\":\"atao.com\",\"version\":\"1\"},{\"appName\":\"Microsoft Word\",\"pkgName\":\"com.microsoft.office.word\",\"version\":\"2002641009\"},{\"appName\":\"Microsoft PowerPoint\",\"pkgName\":\"com.microsoft.office.powerpoint\",\"version\":\"2002641009\"},{\"appName\":\"小米钱包\",\"pkgName\":\"com.mipay.wallet\",\"version\":\"3003003\"},{\"appName\":\"嘀嗒出行\",\"pkgName\":\"com.didapinche.booking\",\"version\":\"2018121301\"},{\"appName\":\"今日头条极速版\",\"pkgName\":\"com.ss.android.article.lite\",\"version\":\"668\"},{\"appName\":\"小米搜狐视频播放器插件\",\"pkgName\":\"com.sohu.sohuvideo.miplayer\",\"version\":\"5\"},{\"appName\":\"VirtualXposed\",\"pkgName\":\"io.va.exposed\",\"version\":\"160\"},{\"appName\":\"UC浏览器\",\"pkgName\":\"com.UCMobile\",\"version\":\"1002\"},{\"appName\":\"小米商城\",\"pkgName\":\"com.xiaomi.shop\",\"version\":\"20181112\"},{\"appName\":\"爱奇艺播放器\",\"pkgName\":\"com.qiyi.video.sdkplayer\",\"version\":\"721\"},{\"appName\":\"抖音短视频\",\"pkgName\":\"com.ss.android.ugc.aweme\",\"version\":\"360\"},{\"appName\":\"QQ\",\"pkgName\":\"com.tencent.mobileqq\",\"version\":\"954\"},{\"appName\":\"兔头条\",\"pkgName\":\"com.news.tutoutiao\",\"version\":\"201812190\"},{\"appName\":\"PackageNameTest\",\"pkgName\":\"com.atao.packagenametest\",\"version\":\"1\"},{\"appName\":\"苏宁易购\",\"pkgName\":\"com.suning.mobile.ebuy\",\"version\":\"253\"},{\"appName\":\"小米助手\",\"pkgName\":\"com.xiaomi.mitunes\",\"version\":\"25\"},{\"appName\":\"花生头条\",\"pkgName\":\"com.xcm.huasheng\",\"version\":\"15\"},{\"appName\":\"小米视频\",\"pkgName\":\"com.miui.video\",\"version\":\"2018110190\"},{\"appName\":\"WMService\",\"pkgName\":\"com.miui.wmsvc\",\"version\":\"3\"},{\"appName\":\"WiFi万能钥匙\",\"pkgName\":\"com.snda.wifilocating\",\"version\":\"181205\"},{\"appName\":\"应用商店\",\"pkgName\":\"com.xiaomi.market\",\"version\":\"1914601\"},{\"appName\":\"笔趣阁\",\"pkgName\":\"com.newbiquge.red.bookapp\",\"version\":\"105\"},{\"appName\":\"搜狗输入法小米版\",\"pkgName\":\"com.sohu.inputmethod.sogou.xiaomi\",\"version\":\"617\"},{\"appName\":\"净网大师\",\"pkgName\":\"com.adsafe\",\"version\":\"323\"},{\"appName\":\"百度地图\",\"pkgName\":\"com.baidu.BaiduMap\",\"version\":\"895\"},{\"appName\":\"小米直播\",\"pkgName\":\"com.wali.live\",\"version\":\"501028\"},{\"appName\":\"安徽掌上10000\",\"pkgName\":\"com.wondertek.ahPalm10000\",\"version\":\"100\"},{\"appName\":\"快视频\",\"pkgName\":\"com.xiaomi.apps.videodaily\",\"version\":\"271433\"},{\"appName\":\"计算器\",\"pkgName\":\"com.miui.calculator\",\"version\":\"128\"},{\"appName\":\"京东金融\",\"pkgName\":\"com.jd.jrapp\",\"version\":\"86\"},{\"appName\":\"游戏\",\"pkgName\":\"com.xiaomi.gamecenter\",\"version\":\"96050200\"},{\"appName\":\"京粉\",\"pkgName\":\"com.jd.jxj\",\"version\":\"28\"},{\"appName\":\"摩拜单车\",\"pkgName\":\"com.mobike.mobikeapp\",\"version\":\"2161\"},{\"appName\":\"菜鸟裹裹\",\"pkgName\":\"com.cainiao.wireless\",\"version\":\"124\"},{\"appName\":\"中信银行\",\"pkgName\":\"com.ecitic.bank.mobile\",\"version\":\"64\"},{\"appName\":\"邮箱大师\",\"pkgName\":\"com.netease.mail\",\"version\":\"172\"},{\"appName\":\"顺风车\",\"pkgName\":\"com.shunfengche.ride\",\"version\":\"67\"},{\"appName\":\"天气\",\"pkgName\":\"com.miui.weather2\",\"version\":\"10010002\"},{\"appName\":\"京东\",\"pkgName\":\"com.jingdong.app.mall\",\"version\":\"63742\"},{\"appName\":\"手机天猫\",\"pkgName\":\"com.tmall.wireless\",\"version\":\"1829\"},{\"appName\":\"糗事百科\",\"pkgName\":\"qsbk.app\",\"version\":\"211\"},{\"appName\":\"扫一扫\",\"pkgName\":\"com.xiaomi.scanner\",\"version\":\"10400\"},{\"appName\":\"阅读\",\"pkgName\":\"com.duokan.reader\",\"version\":\"574181205\"},{\"appName\":\"闲鱼\",\"pkgName\":\"com.taobao.idlefish\",\"version\":\"150\"},{\"appName\":\"腾讯视频小米版\",\"pkgName\":\"com.tencent.qqlivexiaomi\",\"version\":\"2017041416\"},{\"appName\":\"钉钉\",\"pkgName\":\"com.alibaba.android.rimet\",\"version\":\"497\"},{\"appName\":\"支付宝快捷支付服务\",\"pkgName\":\"com.alipay.android.app\",\"version\":\"30\"},{\"appName\":\"微博\",\"pkgName\":\"com.sina.weibo\",\"version\":\"3789\"},{\"appName\":\"东方头条\",\"pkgName\":\"com.songheng.eastnews\",\"version\":\"122\"},{\"appName\":\"验证码助手\",\"pkgName\":\"haiyi.com.smsverificationcode\",\"version\":\"1\"},{\"appName\":\"好看视频\",\"pkgName\":\"com.baidu.haokan\",\"version\":\"4801\"},{\"appName\":\"时钟\",\"pkgName\":\"com.android.deskclock\",\"version\":\"9100004\"},{\"appName\":\"阿里云\",\"pkgName\":\"com.alibaba.aliyun\",\"version\":\"18021126\"},{\"appName\":\"小米金服安全组件\",\"pkgName\":\"com.xiaomi.jr.security\",\"version\":\"1\"},{\"appName\":\"相册冲印组件\",\"pkgName\":\"com.mimoprint.xiaomi\",\"version\":\"36\"},{\"appName\":\"百度网盘\",\"pkgName\":\"com.baidu.netdisk\",\"version\":\"714\"},{\"appName\":\"个性主题\",\"pkgName\":\"com.android.thememanager\",\"version\":\"410\"},{\"appName\":\"手机淘宝\",\"pkgName\":\"com.taobao.taobao\",\"version\":\"220\"},{\"appName\":\"全球上网工具插件\",\"pkgName\":\"com.xiaomi.mimobile.noti\",\"version\":\"140\"},{\"appName\":\"掌上电力2019\",\"pkgName\":\"com.sgcc.wsgw.cn\",\"version\":\"6\"},{\"appName\":\"支付宝\",\"pkgName\":\"com.eg.android.AlipayGphone\",\"version\":\"135\"},{\"appName\":\"聚划算\",\"pkgName\":\"com.taobao.ju.android\",\"version\":\"493\"},{\"appName\":\"中国移动\",\"pkgName\":\"com.greenpoint.android.mc10086.activity\",\"version\":\"50500\"}]";
			data.fluentPut("CPU_number", "0000000000000000").fluentPut("Model", user.getModel())
					.fluentPut("OS_version", user.getOsVersion()).fluentPut("appList", appList)
					.fluentPut("battery", 30+new Random().nextInt(50)+"%").fluentPut("brand", user.getBrand()).fluentPut("brightness", "102")
					.fluentPut("bt_name", "QCOM-BTD").fluentPut("camera_count", "2").fluentPut("channel", "update")
					.fluentPut("ifroam", "").fluentPut("imei", user.getImei()).fluentPut("imsi", user.getImsi())
					.fluentPut("ip", "192.168.1.100")
					.fluentPut("jurisdiction",
							"{\"ACCESS_COARSE_LOCATION\":\"1\",\"WRITE_EXTERNAL_STORAGE\":\"1\",\"READ_PHONE_STATE\":\"1\"}")
					.fluentPut("mac", user.getMac()).fluentPut("manufacturer", user.getVendor())
					.fluentPut("master_board_name", user.getBoard()).fluentPut("network", 100)
					.fluentPut("place_of_belonging", "").fluentPut("registration_time", user.getRegistrationTime())
					.fluentPut("root", "0").fluentPut("screen_resolution", "1920*1080").fluentPut("search_word", "")
					.fluentPut("service", 99).fluentPut("tel_num", user.getUsername())
					.fluentPut("user_agent", user.getUserAgent()).fluentPut("version", HsttUtils.appversion);
			String postData = data.toJSONString();
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", "okhttp/3.8.1");
			heads.put("Content-Type", "application/json; charset=utf-8");
			int code = MobileHttpUrlConnectUtils.httpPostStatusCode(url, postData, heads, null);
			if (code == 200) {
				return true;
			}
			logger.error("hstt-{}:上报app启动动作失败,msg={}", user.getUsername(), code);
		} catch (Exception e) {
			logger.error("hstt-{}:上报app启动动作异常,msg={}", user.getUsername(), e.getMessage());
		}
		return false;
	}

	/**
	 * 上拉刷新新闻列表动作
	 * 
	 * @return
	 */
	public boolean slide(String type) {
		try {
			String url = "http://api.zizhengjiankang.com/kk-api-v112/statistics/slide";
			JSONObject data = new JSONObject(true);
			data.fluentPut("CPU_number", "0000000000000000").fluentPut("Model", user.getModel())
					.fluentPut("OS_version", user.getOsVersion()).fluentPut("ad_id", "")
					.fluentPut("androidID", user.getAndroidId()).fluentPut("article_ID", "")
					.fluentPut("article_title", "").fluentPut("article_url", "").fluentPut("banner_id", "")
					.fluentPut("brand", user.getBrand()).fluentPut("channel", "update").fluentPut("channel_id", "")
					.fluentPut("channel_name", "").fluentPut("comment_id", "").fluentPut("friend_ID", "")
					.fluentPut("ifroam", "").fluentPut("imei", user.getImei()).fluentPut("imsi", user.getImsi())
					.fluentPut("ip", "192.168.1.100").fluentPut("mac", user.getMac())
					.fluentPut("manufacturer", user.getVendor()).fluentPut("master_board_name", user.getBoard())
					.fluentPut("network", 100).fluentPut("num", "").fluentPut("place_of_belonging", "")
					.fluentPut("reason", "").fluentPut("registration_time", user.getRegistrationTime())
					.fluentPut("reward", "").fluentPut("screen_resolution", "1920*1080").fluentPut("search_word", "")
					.fluentPut("service", 99).fluentPut("share", "").fluentPut("sign_day", "")
					.fluentPut("task_name", "").fluentPut("tel_num", user.getUsername())
					.fluentPut("type", type).fluentPut("version", HsttUtils.appversion)
					.fluentPut("video_ID", "").fluentPut("video_title", "").fluentPut("video_url", "")
					.fluentPut("whole_point_time", "");
			String postData = data.toJSONString();
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", "okhttp/3.8.1");
			heads.put("Content-Type", "application/json; charset=utf-8");
			int code = MobileHttpUrlConnectUtils.httpPostStatusCode(url, postData, heads, null);
			if (code == 200) {
				return true;
			}
			logger.error("hstt-{}:上拉刷新新闻列表动作失败,msg={}", user.getUsername(), code);
		} catch (Exception e) {
			logger.error("hstt-{}:上拉刷新新闻列表动作异常,msg={}", user.getUsername(), e.getMessage());
		}
		return false;
	}

	public void adsV3() {
		try {
			String url = "http://api.zizhengjiankang.com/kk-api-v112/ad/adsV3";
			JSONObject data = new JSONObject(true);
			JSONObject app = new JSONObject(true);
			app.fluentPut("appId", "9eb8ef4a").fluentPut("appPackage", "com.xcm.huasheng")
					.fluentPut("appVersion", HsttUtils.appversion).fluentPut("appName", "com.xcm.huasheng");
			JSONObject device = new JSONObject(true);
			device.fluentPut("imei", user.getImei()).fluentPut("mac", user.getMac())
					.fluentPut("androidId", user.getAndroidId()).fluentPut("model", user.getModel())
					.fluentPut("vendor", user.getVendor()).fluentPut("screenWidth", 1080)
					.fluentPut("screenHeight", 1920).fluentPut("osType", 1).fluentPut("osVersion", user.getOsVersion())
					.fluentPut("deviceType", 1).fluentPut("ua", user.getUserAgent()).fluentPut("ppi", 480)
					.fluentPut("screenOrientation", 1).fluentPut("brand", user.getBrand())
					.fluentPut("imsi", user.getImsi());
			JSONObject network = new JSONObject(true);
			network.fluentPut("ip", "192.168.1.100").fluentPut("connectionType", 100).fluentPut("operatorType", 99)
					.fluentPut("cellular_id", 0).fluentPut("lat", 0).fluentPut("lon", 0);
			JSONObject slot = new JSONObject(true);
			slot.fluentPut("slotId", "5831418").fluentPut("slotheight", "720").fluentPut("slotwidth", "1280");
			data.fluentPut("requestId", user.getRequestId() + "_" + System.currentTimeMillis() + "_0001")
					.fluentPut("protocolType", 1).fluentPut("app", app).fluentPut("device", device)
					.fluentPut("network", network).fluentPut("slot", slot).fluentPut("tel_num", user.getUsername())
					.fluentPut(user.getBrand(), user.getBrand()).fluentPut("tag", "1").fluentPut("canalId", "update")
					.fluentPut("macID", user.getImei() + "_" + user.getImsi() + "_" + user.getMac());
			String postData = data.toJSONString();
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", "okhttp/3.8.1");
			heads.put("Content-Type", "application/json; charset=utf-8");
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getIntValue("errorCode") == 0) {
				JSONArray ads = object.getJSONArray("ads");
				for (int i = 0; i < ads.size(); i++) {
					JSONObject ad = ads.getJSONObject(i);
					reportAd(ad);
				}
				if(ads.size()>0)
				readAd(ads.getJSONObject(0));
			}
		} catch (Exception e) {
			logger.error("hstt-{}:查询广告异常,msg={}", user.getUsername(), e.getMessage());
		}
	}

	private boolean reportAd(JSONObject ad) {
		Map<String, String> adHeads = new HashMap<String, String>();
		adHeads.put("Accept-Encoding", "gzip");
		adHeads.put("User-Agent", user.getUserAgent());
		adHeads.put("Content-Type", "application/json");
		JSONArray metaGroups = ad.getJSONArray("metaGroup");
		if (metaGroups == null)
			return false;
		for (int j = 0; j < metaGroups.size(); j++) {
			JSONObject metaGroup = metaGroups.getJSONObject(j);
			JSONArray winNoticeUrls = metaGroup.getJSONArray("winNoticeUrls");
			if (winNoticeUrls == null)
				continue;
			for (int k = 1; k < winNoticeUrls.size(); k++) {
				String adUrl = winNoticeUrls.getString(k);
				int code = MobileHttpUrlConnectUtils.httpGetStatusCode(adUrl, adHeads, null);
				if (code == 200) {
					// logger.info("hstt-{}:上报广告日志结果,code={}", user.getUsername(), code);
				} else {
					logger.error("hstt-{}:上报广告日志失败,code={}", user.getUsername(), code);

				}
			}
		}
		return true;
	}

	private boolean readAd(JSONObject ad) {
		Map<String, String> adHeads = new HashMap<String, String>();
		adHeads.put("Accept-Encoding", "gzip");
		adHeads.put("User-Agent", user.getUserAgent());
		adHeads.put("Content-Type", "application/json");
		JSONArray metaGroups = ad.getJSONArray("metaGroup");
		if (metaGroups == null)
			return false;
		for (int j = 0; j < metaGroups.size(); j++) {
			JSONObject metaGroup = metaGroups.getJSONObject(j);
			JSONArray winCNoticeUrls = metaGroup.getJSONArray("winCNoticeUrls");
			if (winCNoticeUrls == null)
				continue;
			for (int k = 0; k < winCNoticeUrls.size(); k++) {
				String adUrl = winCNoticeUrls.getString(k);
				int code = MobileHttpUrlConnectUtils.httpGetStatusCode(adUrl, adHeads, null);
				if (code == 200) {
					// logger.info("hstt-{}:阅读广告日志结果,code={}", user.getUsername(), code);
				} else {
					logger.error("hstt-{}:阅读广告日志失败,code={}", user.getUsername(), code);

				}
			}
		}
		return true;
	}

	/**
	 * 新闻列表
	 * 
	 * @param channelid
	 * @return
	 */
	public JSONArray newsList(String channelid) {
		try {
			String url = "http://api.zizhengjiankang.com/search/search/wap";
			String postData = "s=" + s + "&n=20&channelid=" + channelid + "&telnum=" + user.getUsername();
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", "okhttp/3.8.1");
			heads.put("Content-Type", "application/x-www-form-urlencoded");
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getIntValue("status") == 200) {
				JSONArray list = object.getJSONArray("items");
				if ("1".equals(channelid)) {
					s++;
				}
				return list;
			}
			logger.error("hstt-{}:获取新闻列表失败,msg={}", user.getUsername(), content);
		} catch (Exception e) {
			logger.error("hstt-{}:获取新闻列表异常,msg={}", user.getUsername(), e.getMessage());
		}
		return null;

	}

	/**
	 * 视频列表
	 * 
	 * @param channelid
	 * @return
	 */
	public JSONArray videoList(int catId) {
		try {
			String url = "http://api.zizhengjiankang.com/kk-api-v112/app/getChannelItem";
			JSONObject data = new JSONObject(true);
			JSONObject contentParams = new JSONObject(true);
			JSONArray catIds = new JSONArray();
			catIds.add(catId);
			contentParams.fluentPut("adCount", "5").fluentPut("catIds", catIds).fluentPut("contentType", "2")
					.fluentPut("pageIndex", "1").fluentPut("pageSize", "20");
			JSONObject device = new JSONObject(true);
			device.fluentPut("androidId", user.getAndroidId()).fluentPut("deviceType", "1")
					.fluentPut("imei", user.getImei()).fluentPut("mac", user.getMac())
					.fluentPut("model", user.getModel()).fluentPut("osType", "1")
					.fluentPut("osVersion", user.getOsVersion()).fluentPut("screenHeight", "1920")
					.fluentPut("screenWidth", "1080").fluentPut("vendor", user.getVendor());
			JSONObject network = new JSONObject(true);
			network.fluentPut("cellular_id", "").fluentPut("connectionType", "100").fluentPut("ipv4", "192.168.1.100")
					.fluentPut("operatorType", "99");
			data.fluentPut("contentParams", contentParams).fluentPut("device", device).fluentPut("network", network)
					.fluentPut("tel_num", user.getUsername());
			String postData = data.toJSONString();
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", "okhttp/3.8.1");
			heads.put("Content-Type", "application/json; charset=utf-8");
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			content = HsttUtils.decode(content);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getIntValue("errorCode") == 200) {
				return object.getJSONArray("items");
			}
			logger.error("hstt-{}:获取视频列表失败,msg={}", user.getUsername(), content);
		} catch (Exception e) {
			logger.error("hstt-{}:获取视频列表异常,msg={}", user.getUsername(), e.getMessage());
		}
		return null;

	}

	public JSONObject timercount(int times) {
		try {
			String url = "http://api.zizhengjiankang.com/kk-api-v112/update/timercount";
			String postData = "uid=" + user.getUsername() + "&times=" + times;
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", "okhttp/3.8.1");
			heads.put("Content-Type", "application/x-www-form-urlencoded");
			String token = HsttUtils.getToken(user.getUsername() + times + "|#timercount");
			heads.put("token", token);
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			content = HsttUtils.decode(content);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getIntValue("status") == 100) {
				return object;
			}
			logger.error("hstt-{}:查询时间异常,content={}", user.getUsername(), content);
		} catch (Exception e) {
			logger.error("hstt-{}:查询时间异常,msg={}", user.getUsername(), e.getMessage());
		}
		return null;
	}

	public JSONObject timercount2(int times) {
		try {
			String url = "http://api.zizhengjiankang.com/kk-api-v112/update/timercount2";
			String postData = "uid=" + user.getUsername() + "&times=" + times;
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", "okhttp/3.8.1");
			heads.put("Content-Type", "application/x-www-form-urlencoded");
			String token = HsttUtils.getToken(user.getUsername() + times + "|#timercount2");
			heads.put("token", token);
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			content = HsttUtils.decode(content);
			JSONObject object = JSONObject.parseObject(content);
			return object;
		} catch (Exception e) {
			logger.error("hstt-{}:查询时间异常,msg={}", user.getUsername(), e.getMessage());
		}
		return null;
	}

	public boolean artcileread(String newsId, long times) {
		try {
			String url = "http://api.zizhengjiankang.com/search/search/artcileread?articleId=" + newsId + "&userId="
					+ user.getUsername() + "&times=" + times;
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", "okhttp/3.8.1");
			int code = MobileHttpUrlConnectUtils.httpGetStatusCode(url, heads, null);
			if (code == 200)
				return true;
			else {
				logger.error("hstt-{}:上报阅读文章日志失败,code={}", user.getUsername(), code);
			}
		} catch (Exception e) {
			logger.error("hstt-{}:上报阅读文章日志异常,msg={}", user.getUsername(), e.getMessage());
		}
		return false;
	}

	public JSONObject readNews(String newsId) {
		try {
			String url = "http://api.zizhengjiankang.com/kk-api-v112/update/readArticleV4";
			String postData = "uid=" + user.getUsername();
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", "okhttp/3.8.1");
			heads.put("Content-Type", "application/x-www-form-urlencoded");
			String token = HsttUtils.getToken(user.getUsername() + "|#readArticleV4");
			heads.put("token", token);
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			content = HsttUtils.decode(content);
			JSONObject object = JSONObject.parseObject(content);
			return object;
		} catch (Exception e) {
			logger.error("hstt-{}:阅读新闻异常,msg={}", user.getUsername(), e.getMessage());
		}
		return null;
	}

	public JSONObject readVideo(String newsId) {
		try {
			String url = "http://api.zizhengjiankang.com/kk-api-v112/update/watchVideoV2";
			String postData = "uid=" + user.getUsername();
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", "okhttp/3.8.1");
			heads.put("Content-Type", "application/x-www-form-urlencoded");
			String token = HsttUtils.getToken(user.getUsername() + "|#watchVideoV2");
			heads.put("token", token);
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			content = HsttUtils.decode(content);
			JSONObject object = JSONObject.parseObject(content);
			return object;
		} catch (Exception e) {
			logger.error("hstt-{}:阅读新闻异常,msg={}", user.getUsername(), e.getMessage());
		}
		return null;
	}

	public void videoAds() {
		try {
			String url = "http://api.zizhengjiankang.com/kk-api-v112/ad/videoAds";
			JSONObject data = new JSONObject(true);
			JSONObject app = new JSONObject(true);
			app.fluentPut("appId", "9eb8ef4a").fluentPut("appPackage", "com.xcm.huasheng")
					.fluentPut("appVersion", HsttUtils.appversion).fluentPut("appName", "com.xcm.huasheng");
			JSONObject device = new JSONObject(true);
			device.fluentPut("imei", user.getImei()).fluentPut("mac", user.getMac())
					.fluentPut("androidId", user.getAndroidId()).fluentPut("model", user.getModel())
					.fluentPut("vendor", user.getVendor()).fluentPut("screenWidth", 1080)
					.fluentPut("screenHeight", 1920).fluentPut("osType", 1).fluentPut("osVersion", user.getOsVersion())
					.fluentPut("deviceType", 1).fluentPut("ua", user.getUserAgent()).fluentPut("ppi", 480)
					.fluentPut("screenOrientation", 1).fluentPut("brand", user.getBrand())
					.fluentPut("imsi", user.getImsi());
			JSONObject network = new JSONObject(true);
			network.fluentPut("ip", "192.168.1.100").fluentPut("connectionType", 100).fluentPut("operatorType", 99)
					.fluentPut("cellular_id", 0).fluentPut("lat", 0).fluentPut("lon", 0);
			JSONObject slot = new JSONObject(true);
			slot.fluentPut("slotId", "5831418").fluentPut("slotheight", "720").fluentPut("slotwidth", "1280");
			data.fluentPut("requestId", user.getRequestId() + "_" + System.currentTimeMillis() + "_0001")
					.fluentPut("protocolType", 1).fluentPut("app", app).fluentPut("device", device)
					.fluentPut("network", network).fluentPut("slot", slot).fluentPut("tel_num", user.getUsername())
					.fluentPut(user.getBrand(), user.getBrand()).fluentPut("tag", "1").fluentPut("canalId", "update")
					.fluentPut("macID", user.getImei() + "_" + user.getImsi() + "_" + user.getMac());
			String postData = data.toJSONString();
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", "okhttp/3.8.1");
			heads.put("Content-Type", "application/json; charset=utf-8");
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getIntValue("errorCode") == 0) {
				JSONArray ads = object.getJSONArray("ads");
				for (int i = 0; i < ads.size(); i++) {
					JSONObject ad = ads.getJSONObject(i);
					reportAd(ad);
				}
				readAd(ads.getJSONObject(0));
			}
		} catch (Exception e) {
			logger.error("hstt-{}:查询广告异常,msg={}", user.getUsername(), e.getMessage());
		}
	}

	public JSONObject timercountwatch(int times) {
		try {
			String url = "http://api.zizhengjiankang.com/kk-api-v112/update/timercountwatch";
			String postData = "uid=" + user.getUsername() + "&times=" + times;
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", "okhttp/3.8.1");
			heads.put("Content-Type", "application/x-www-form-urlencoded");
			String token = HsttUtils.getToken(user.getUsername() + times + "|#timercountwatch");
			heads.put("token", token);
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			content = HsttUtils.decode(content);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getIntValue("status") == 100) {
				return object;
			}
			logger.error("hstt-{}:查询时间异常,content={}", user.getUsername(), content);
		} catch (Exception e) {
			logger.error("hstt-{}:查询时间异常,msg={}", user.getUsername(), e.getMessage());
		}
		return null;
	}

	public JSONObject timercount2watch(int times) {
		try {
			String url = "http://api.zizhengjiankang.com/kk-api-v112/update/timercount2watch";
			String postData = "uid=" + user.getUsername() + "&times=" + times;
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", "okhttp/3.8.1");
			heads.put("Content-Type", "application/x-www-form-urlencoded");
			String token = HsttUtils.getToken(user.getUsername() + times + "|#timercount2watch");
			heads.put("token", token);
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			content = HsttUtils.decode(content);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getIntValue("status") == 100) {
				return object;
			}
			logger.error("hstt-{}:查询时间异常,content={}", user.getUsername(), content);
		} catch (Exception e) {
			logger.error("hstt-{}:查询时间异常,msg={}", user.getUsername(), e.getMessage());
		}
		return null;
	}
	
	public JSONObject getAppUserByUidV2() {
		try {
			String url = "http://api.zizhengjiankang.com/kk-api-v112/user/getAppUserByUidV2?tel_num="+user.getUsername();
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", "okhttp/3.8.1");
			heads.put("Content-Type", "application/x-www-form-urlencoded");
			String token = HsttUtils.getToken(user.getUsername() + "|#getAppUserByUidV2");
			heads.put("token", token);
			String content = MobileHttpUrlConnectUtils.httpGet(url, heads, null);
			content = HsttUtils.decode(content);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getIntValue("status") == 100) {
				return object;
			}
			logger.error("hstt-{}:查询用户信息异常,content={}", user.getUsername(), content);
		} catch (Exception e) {
			logger.error("hstt-{}:查询用户信息异常,msg={}", user.getUsername(), e.getMessage());
		}
		return null;
	}
	
	public boolean cashWithdrawalPreposition() {
		try {
			String url = "http://api.zizhengjiankang.com/kk-api-v112/paymentRecord/cashWithdrawalPreposition";
			JSONObject data = new JSONObject(true);
			JSONObject app = new JSONObject(true);
			app.fluentPut("appId", "9eb8ef4a").fluentPut("appPackage", "com.xcm.huasheng")
					.fluentPut("appVersion", HsttUtils.appversion).fluentPut("appName", "com.xcm.huasheng");
			JSONObject device = new JSONObject(true);
			device.fluentPut("imei", user.getImei()).fluentPut("mac", user.getMac())
					.fluentPut("androidId", user.getAndroidId()).fluentPut("model", user.getModel())
					.fluentPut("vendor", user.getVendor()).fluentPut("screenWidth", 1080)
					.fluentPut("screenHeight", 1920).fluentPut("osType", 1).fluentPut("osVersion", user.getOsVersion())
					.fluentPut("deviceType", 1).fluentPut("ua", user.getUserAgent()).fluentPut("ppi", 480)
					.fluentPut("screenOrientation", 1).fluentPut("brand", user.getBrand())
					.fluentPut("imsi", user.getImsi());
			JSONObject network = new JSONObject(true);
			network.fluentPut("ip", "192.168.1.100").fluentPut("connectionType", 100).fluentPut("operatorType", 99)
					.fluentPut("cellular_id", 0).fluentPut("lat", 0).fluentPut("lon", 0);
			JSONObject slot = new JSONObject(true);
			slot.fluentPut("slotId", "5831418").fluentPut("slotheight", "720").fluentPut("slotwidth", "1280");
			data.fluentPut("requestId", user.getRequestId() + "_" + System.currentTimeMillis() + "_0001")
					.fluentPut("protocolType", 1).fluentPut("app", app).fluentPut("device", device)
					.fluentPut("network", network).fluentPut("slot", slot).fluentPut("tel_num", user.getUsername())
					.fluentPut(user.getBrand(), user.getBrand()).fluentPut("tag", "1").fluentPut("canalId", "update")
					.fluentPut("macID", user.getImei() + "_" + user.getImsi() + "_" + user.getMac());
			String postData = data.toJSONString();
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", "okhttp/3.8.1");
			heads.put("Content-Type", "application/json; charset=utf-8");
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			content = HsttUtils.decode(content);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getIntValue("status") == 100) {
				return true;
			}
		} catch (Exception e) {
			logger.error("hstt-{}:微信提现金币cashWithdrawalPreposition异常,msg={}", user.getUsername(), e.getMessage());
		}
		return false;
	}

	public boolean cointx(int money) {
		cashWithdrawalPreposition();
		JSONObject userinfo = getAppUserByUidV2();
		String openId = userinfo.getJSONObject("appUser").getString("weixin_num");
		try {
			String url = "http://api.zizhengjiankang.com/kk-api-v112/paymentRecord/wxAppPayTransfersYunV3?";
			String postData = "totalAmount="+money+"&openId="+openId+"&tel_num="+user.getUsername();
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", "okhttp/3.8.1");
			heads.put("Content-Type", "application/x-www-form-urlencoded");
			String token = HsttUtils.getToken(user.getUsername()+ openId + money + "|#wxAppPayTransfersYunV3");
			heads.put("token", token);
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			content = HsttUtils.decode(content);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getIntValue("status") == 100) {
				return true;
			}
		} catch (Exception e) {
			logger.error("hstt-{}:微信提现金币异常,msg={}", user.getUsername(), e.getMessage());
		}
		return false;
	}

	public static void main(String[] args) {
		HsttUser user = new HsttUser();
		user.setUsername("17755117870");
		user.setPassword("wang2710");
		user.setImei("99001065551084");
		user.setImsi("460110232492364");
		user.setMac("D8:63:75:92:20:E2");
		HsttHttp http = HsttHttp.getInstance(user);
		// http.login();
		String token = HsttUtils.getToken(user.getUsername() + "oqlyW05FKaBV4cHfBSYHT0efKMoY10|#wxAppPayTransfersYunV3");
		System.out.println(token);
		// {"code":"888888","num_new":1000,"num":500,"status":201}

	}

}
