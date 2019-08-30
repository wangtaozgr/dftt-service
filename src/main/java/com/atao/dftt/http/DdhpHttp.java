package com.atao.dftt.http;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.atao.dftt.model.DdhpUser;
import com.atao.dftt.util.HttpUrlConnectUtils;
import com.atao.dftt.util.TdHttpUrlConnectUtils;
import com.atao.dftt.util.qrcode.QRCodeUtil;
import com.atao.util.StringUtils;

/**
 * 多多好评
 */
public class DdhpHttp {
	private static Logger logger = LoggerFactory.getLogger(DdhpHttp.class);
	private static Map<String, DdhpHttp> tdMap = new HashMap<String, DdhpHttp>();
	public Map<String, String> cookies = new HashMap<String, String>();
	private DdhpUser user;
	private String userAgent = "Mozilla/5.0 (Linux; U; Android 7.1.2; zh-CN; MI 5X Build/N2G47H) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/57.0.2987.108 UCBrowser/12.2.2.1002 Mobile Safari/537.36";
	//pddTaskType 1:扫码 2:搜索
    //pddTaskEvaluateType  3:字图评价 2：指定评价 1：自由评价
	//pddTaskOrderType 1:参团购买 2:开新团购买 3:有团参团，无团开团
	public DdhpHttp(DdhpUser user) {
		this.user = user;
	}

	public static synchronized DdhpHttp getInstance(DdhpUser user) {
		DdhpHttp userHttp = (DdhpHttp) tdMap.get(user.getUsername());
		if (userHttp == null) {
			userHttp = new DdhpHttp(user);
			tdMap.put(user.getUsername(), userHttp);
		}
		return userHttp;
	}

	public void addCookie(String cookiestr) {
		for (String cookie : cookiestr.split(";")) {
			String[] c = cookie.split("=");
			cookies.put(c[0], c[1]);
		}
	}

	private String getCookie() {
		String cookiestr = "";
		Set<Entry<String, String>> set = cookies.entrySet();
		Iterator<Entry<String, String>> iterator = set.iterator();
		while (iterator.hasNext()) {
			Entry<String, String> obj = iterator.next();
			cookiestr += obj.getKey() + "=" + obj.getValue() + ";";
		}
		if (StringUtils.isNotBlank(cookiestr))
			cookiestr = cookiestr.substring(0, cookiestr.length() - 1);
		return cookiestr;
	}

	public boolean login() {
		try {
			String url = "http://app.ddhaoping.com/api/v1/pwdLogin";
			String postData = "{\"phone\":\""+user.getUsername()+"\",\"pwd\":\""+user.getPwd()+"\"}";
			Map<String, String> heads = new HashMap<String, String>();
			
			heads.put("User-Agent", userAgent);
			heads.put("Accept", "application/json, text/plain, */*");
			heads.put("Accept-Encoding", "gzip, deflate");
			heads.put("Accept-Language", "zh-CN,en-US;q=0.8");
			heads.put("Content-Type", "application/json;charset=UTF-8");
			heads.put("Referer", "http://app.ddhaoping.com/");
			heads.put("Origin", "http://app.ddhaoping.com/");
			heads.put("Host", "app.ddhaoping.com");
			Map<String, String> result = TdHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			if(result==null) return false;
			if (result.get("cookie") != null) {
				addCookie(result.get("cookie"));
			}
			JSONObject object = JSONObject.parseObject(result.get("content"));
			logger.info("DdhpUser-{}:用户登陆结果,content={}", user.getUsername(), result.get("content"));
			if (object.getIntValue("code")==0) {
				myBuyerAccount();
				return true;
			}
			logger.error("DdhpUser-{}:用户登陆异常,content={}", user.getUsername(), result.get("content"));
		} catch (Exception e) {
			logger.error("DdhpUser-{}:用户登陆异常,msg={}", user.getUsername(), e.getMessage());
		}
		return false;
	}
	
	/**
	 * 登陆成功后自动跳转到用户个人中心
	 */
	public boolean myBuyerAccount() {
		try {
			String url = "http://app.ddhaoping.com/api/v1/myBuyerAccount";
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("User-Agent", userAgent);
			heads.put("Accept", "application/json, text/plain, */*");
			heads.put("Accept-Encoding", "gzip, deflate");
			heads.put("Accept-Language", "zh-CN,en-US;q=0.8");
			heads.put("Referer", "http://app.ddhaoping.com/");
			heads.put("Host", "app.ddhaoping.com");
			String cookie = getCookie();
			heads.put("Cookie", cookie);
			String content = HttpUrlConnectUtils.httpGet(url, heads, false);
			JSONObject object = JSONObject.parseObject(content);
			logger.info("DdhpUser-{}:个人中心,content={}", user.getUsername(), object);
			if (object.getIntValue("code")==0) {
				return true;
			}
			logger.error("DdhpUser-{}:个人中心异常,content={}", user.getUsername(), object);
		} catch (Exception e) {
			logger.error("DdhpUser-{}:个人中心异常,msg={}", user.getUsername(), e.getMessage());
		}
		return false;
	}

	public JSONArray tasklist(int page) {
		try {
			String url = "http://app.ddhaoping.com/api/v1/taskList?limit=8&page="+page;
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("User-Agent", userAgent);
			heads.put("Accept", "application/json, text/plain, */*");
			heads.put("Accept-Encoding", "gzip, deflate");
			heads.put("Accept-Language", "zh-CN,en-US;q=0.8");
			heads.put("Referer", "http://app.ddhaoping.com/");
			heads.put("Host", "app.ddhaoping.com");
			String cookie = getCookie();
			heads.put("Cookie", cookie);
			String content = HttpUrlConnectUtils.httpGet(url, heads, false);
			JSONObject object = JSONObject.parseObject(content);
			logger.info("DdhpUser-{}:任务列表,content={}", user.getUsername(), object);
			if (object.getIntValue("code")==0) {
				return object.getJSONArray("data");
			}else if  (object.getIntValue("code")==-1&&object.getString("msg").contains("登录超时")) {
				if(login()) {
					return tasklist(1);
				}
			}
			logger.error("DdhpUser-{}:任务列表异常,content={}", user.getUsername(), object);
		} catch (Exception e) {
			logger.error("DdhpUser-{}:任务列表异常,msg={}", user.getUsername(), e.getMessage());
		}
		return null;
	}
	
	public JSONObject reciveTask(String taskId) {
		try {
			String url = "http://app.ddhaoping.com/api/v1/takeTask";
			String postData = "{\"pddTaskId\":\""+taskId+"\"}";
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("User-Agent", userAgent);
			heads.put("Accept", "application/json, text/plain, */*");
			heads.put("Accept-Encoding", "gzip, deflate");
			heads.put("Accept-Language", "zh-CN,en-US;q=0.8");
			heads.put("Content-Type", "application/json;charset=UTF-8");
			heads.put("Referer", "http://app.ddhaoping.com/");
			heads.put("Origin", "http://app.ddhaoping.com/");
			heads.put("Host", "app.ddhaoping.com");
			String cookie = getCookie();
			heads.put("Cookie", cookie);
			String content = HttpUrlConnectUtils.httpPost(url, postData, heads, null);
			JSONObject object = JSONObject.parseObject(content);
			logger.info("DdhpUser-{}:接取任务结果,content={}", user.getUsername(), content);
			return object;
		} catch (Exception e) {
			logger.error("DdhpUser-{}:接取任务异常,msg={}", user.getUsername(), e.getMessage());
		}
		return null;
	}

	public static void main(String[] args) throws InterruptedException {
		DdhpUser user = new DdhpUser();
		user.setUsername("17755117870");
		user.setPwd("wang2710");
		DdhpHttp http = DdhpHttp.getInstance(user);
		http.tasklist(1);
		
	}

	public void setCookies(Map<String, String> cookies) {
		this.cookies = cookies;
	}

}
