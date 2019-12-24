package com.atao.dftt.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.atao.dftt.util.MobileHttpUrlConnectUtils;
import com.atao.dftt.util.TaodanUtils;
import com.atao.util.StringUtils;

/**
 * 多多好评
 */
public class TaodanHttp {
	private static Logger logger = LoggerFactory.getLogger(TaodanHttp.class);
	public static Map<String, String> cookies = new HashMap<String, String>();
	public static Map<String, String> userAgents = new HashMap<String, String>();
	public String username;
	public String cookie;
	public String userAgent = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36";

	public TaodanHttp(String username) {
		this.username = username;
		String cookie = cookies.get(username);
		this.cookie = cookie;
	}

	public TaodanHttp(String username, String cookie) {
		this.username = username;
		cookies.put(username, cookie);
		this.cookie = cookie;
	}

	public TaodanHttp(String username, String cookie, String userAgent) {
		this.username = username;
		cookies.put(username, cookie);
		userAgents.put(username, userAgent);
		this.cookie = cookie;
	}

	public static synchronized TaodanHttp getInstance(String username) {
		TaodanHttp userHttp = new TaodanHttp(username);
		userHttp.cookie = cookies.get(username);
		// userHttp.userAgent = userAgents.get(username);
		return userHttp;
	}

	/**
	 * 登陆成功后自动跳转到用户个人中心
	 */
	public List<String> MyRecTaskList(int state, int page) {
		return MyRecTaskList(state, page, 2);
	}

	public List<String> MyRecTaskList(int state, int page, int num) {
		List<String> taskIds = new ArrayList<String>();
		if (num < 1)
			return taskIds;
		try {
			HttpClient client = HttpClients.custom().build();
			HttpGet get = new HttpGet();
			String url = "http://www.053666.cn/user/MyRecTaskList.aspx?state=" + state + "&page=" + page;
			get.setURI(new URI(url));
			get.addHeader("User-Agent", userAgent);
			get.addHeader("Accept", "*/*");
			get.addHeader("Accept-Language", "zh-CN,zh;q=0.9");
			get.addHeader("Accept-Encoding", "gzip, deflate");
			get.addHeader("Referer", "http://www.053666.cn/user/MyRecTaskList.aspx?state=3");
			get.addHeader("Connection", "keep-alive");
			get.addHeader("Cookie", cookie);
			HttpResponse resp = client.execute(get);
			String content = readStreamByEncoding(resp.getEntity().getContent(), "UTF-8");
			if (resp.getStatusLine().getStatusCode() == 521) {
				num--;
				boolean s = reSetCookie(content);
				if (!s)
					return taskIds;
				return MyRecTaskList(state, page, num);
			}
			Document doc = Jsoup.parse(content);
			Elements ids = doc.getElementsByAttributeValue("value", "查看任务");
			for (int i = 0; i < ids.size(); i++) {
				String onclick = ids.get(i).attr("onclick");
				int end = onclick.indexOf("'});");
				String taskId = onclick.substring(onclick.indexOf("id=") + 3, end);
				taskIds.add(taskId);
			}
			return taskIds;
		} catch (Exception e) {
			return taskIds;
		}
	}

	public boolean updateTaskFh(String taskId) {
		try {
			String url = "http://www.053666.cn/wap/user/TaskDetails.aspx?id=" + taskId;
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("User-Agent", userAgent);
			heads.put("Accept", "application/json, text/javascript, */*; q=0.01");
			heads.put("Accept-Encoding", "gzip, deflate");
			heads.put("Accept-Language", "zh-CN,zh;q=0.9");
			heads.put("Referer", "http://www.053666.cn/user/myrectaskList.aspx?state=9,2,3,4,6");
			heads.put("X-Requested-With", "XMLHttpRequest");
			heads.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			heads.put("Origin", "http://www.053666.cn");
			heads.put("Cookie", cookie);
			String postData = "PingYuType=1&taskid=" + taskId + "&hdShaiTuPic=&hdShaiTuPic=&hdShaiTuPic=";
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			logger.info("taskId={}|更新淘单任务成发货状态结果={}", taskId, content);
			JSONObject json = JSONObject.parseObject(content);
			if (json.getBooleanValue("IsSuccess"))
				return true;
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	public String getPjContentOld(String taskId) {
		try {
			String url = "http://www.053666.cn/user/RecProductForm.aspx?id=" + taskId;
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("User-Agent", userAgent);
			// heads.put("Accept","*/*");
			// heads.put("Accept-Encoding", "gzip, deflate");
			heads.put("Accept-Language", "zh-CN,zh;q=0.9");
			heads.put("Referer", "http://www.053666.cn/user/MyRecTaskList.aspx?state=3");
			// heads.put("Upgrade-Insecure-Requests", "1");
			heads.put("Connection", "Keep-Alive");
			heads.put("Cookie", cookie);
			String content = MobileHttpUrlConnectUtils.httpGet(url, heads, "GBK");
			return content;
		} catch (Exception e) {
			return null;
		}
	}

	public String getPjContent(String taskId) {
		return getPjContent(taskId, 2);
	}

	public String getPjContent(String taskId, int num) {
		if (num < 1)
			return null;
		try {
			HttpClient client = HttpClients.custom().build();
			HttpGet get = new HttpGet();
			String gotourl = "http://www.053666.cn/user/RecProductForm.aspx?id=" + taskId;
			get.setURI(new URI(gotourl));
			get.addHeader("User-Agent", userAgent);
			get.addHeader("Accept", "*/*");
			get.addHeader("Accept-Language", "zh-CN,zh;q=0.9");
			get.addHeader("Accept-Encoding", "gzip, deflate");
			get.addHeader("Referer", "http://www.053666.cn/user/MyRecTaskList.aspx?state=3");
			get.addHeader("Connection", "keep-alive");
			get.addHeader("Cookie", cookie);
			HttpResponse resp = client.execute(get);
			String content = readStreamByEncoding(resp.getEntity().getContent(), "UTF-8");
			if (resp.getStatusLine().getStatusCode() == 521) {
				num--;
				boolean s = reSetCookie(content);
				if (!s)
					return null;
				return getPjContent(taskId, num);
			}
			return content;
		} catch (Exception e) {
			return null;
		}
	}

	private boolean reSetCookie(String content) {
		String c = TaodanUtils.getJslClearance(content);
		if (StringUtils.isBlank(c)) {
			return false;
		}
		int i1 = cookie.indexOf("__jsl_clearance");
		if (i1 > -1) {
			int i2 = cookie.indexOf(";", i1);
			cookie = cookie.substring(0, i1) + c + cookie.substring(i2);
		} else {
			cookie = cookie + ";" + c;
		}
		cookies.put(this.username, cookie);
		return true;
	}

	/**
	 * 淘单确认收货
	 */
	public String recProductFrom(String username, String taskId, String filePath) {
		String hdShowSexPic = uploadCommentImg(username, taskId, filePath);
		if (StringUtils.isNotBlank(hdShowSexPic)) {
			try {
				String url = "http://www.053666.cn/user/RecProductForm.aspx?id=" + taskId;
				String postData = "sendtasktaskid=" + taskId + "&hdShowSexPic=" + hdShowSexPic + "&taskid=" + taskId;
				Map<String, String> heads = new HashMap<String, String>();
				heads.put("Accept", "application/json, text/javascript, */*; q=0.01");
				heads.put("Accept-Encoding", "gzip, deflate");
				heads.put("Accept-Language", "zh-CN,zh;q=0.9");
				heads.put("User-Agent", userAgent);
				heads.put("X-Requested-With", "XMLHttpRequest");
				heads.put("Referer", "http://www.053666.cn/user/RecProductForm.aspx?id=" + taskId);
				heads.put("Origin", "http://www.053666.cn");
				heads.put("Cookie", cookie);
				String result = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
				logger.info("taodan-{}:{}|确认收货结果.result={}", username, taskId, result);
				JSONObject obj = JSONObject.parseObject(result);
				if (obj.getBooleanValue("IsSuccess")) {
					return "";
				} else {
					return "淘单确认收货失败!";
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("taodan-{}:{}|淘单确认收货异常! msg={}", username, taskId, e.getMessage());
				return "淘单确认收货异常!";
			}
		} else {
			return "淘单上传图片异常!";
		}
	}

	public String uploadCommentImg(String username, String taskId, String filePath) {
		try {
			String url = "http://www.053666.cn/ashx/upload_json.ashx";
			Map<String, String> postDataMap = new HashMap<String, String>();
			postDataMap.put("SaveFile", "TaskUpPic");
			postDataMap.put("control", "file");
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept", "application/json, text/javascript, */*; q=0.01");
			heads.put("Accept-Encoding", "gzip, deflate");
			heads.put("Accept-Language", "zh-CN,zh;q=0.9");
			heads.put("User-Agent", userAgent);
			heads.put("X-Requested-With", "XMLHttpRequest");
			heads.put("Referer", "http://www.053666.cn/user/RecProductForm.aspx?id=" + taskId);
			heads.put("Origin", "http://www.053666.cn");
			heads.put("Cookie", cookie);
			String BOUNDARY = getBoundary();
			String result = MobileHttpUrlConnectUtils.uploadFile(url, postDataMap, filePath, BOUNDARY, heads, null);
			logger.info("taodan-{}:{}|上传图片结果.result={}", username, taskId, result);
			JSONObject obj = JSONObject.parseObject(result);
			if (obj.getIntValue("error") == 0) {
				return obj.getString("url");
			} else {
				logger.info("taodan-{}:{}|上传图片失败.result={}", username, taskId, obj);
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("taodan-{}:{}|上传图片异常! msg={}", username, taskId, e.getMessage());
			return null;
		}
	}

	private String getBoundary() {
		String random = "gOdGDotltrKycQGF";
		return "WebKitFormBoundary" + random;
	}

	public JSONObject taskDetail(String taskId) {
		JSONObject json = null;
		try {
			String url = "http://www.053666.cn/wap/user/TaskDetails.aspx?id=" + taskId;
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("User-Agent", userAgent);
			heads.put("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
			heads.put("Accept-Encoding", "gzip, deflate");
			heads.put("Accept-Language", "zh-CN,zh;q=0.9");
			heads.put("Referer", "http://www.053666.cn/user/myrectaskList.aspx?state=9,2,3,4,6");
			heads.put("Upgrade-Insecure-Requests", "1");
			String content = MobileHttpUrlConnectUtils.httpGet(url, heads, null);
			// logger.info("taskId={}|淘单任务详细={}", taskId, content);
			Document doc = Jsoup.parse(content);
			String title = doc.getElementsByClass("mui-title").get(0).text();
			if ("等待接单".equals(title)) {
				Element li = doc.getElementsByClass("lind").get(0);
				String mallLogo = li.getElementsByTag("img").get(0).attr("src");
				String mallName = li.text();
				logger.info("taskId={}|mallName={}|mallLogo={}", taskId, mallName, mallLogo);
				mallName = mallName.substring(mallName.indexOf("["));
				mallName = mallName.replace("[店铺名:", "").replace("]", "").replace(" ", "").trim();
				mallName = mallName.trim();
				json = new JSONObject();
				json.put("mallName", mallName);
				json.put("mallLogo", mallLogo);
				logger.info("taskId={}|mallName={}|mallLogo={}", taskId, mallName, mallLogo);
			}
		} catch (Exception e) {
			return null;
		}
		return json;
	}

	private static String getImageName(String img) {
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

	/**
	 * 
	 * 提现页面
	 */
	public String withdraw() {
		return withdraw(2);
		/*try {
			String url = "http://www.053666.cn/user/Withdraw.aspx";
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("User-Agent", userAgent);
			heads.put("Accept", "");
			heads.put("Accept-Encoding", "gzip, deflate");
			heads.put("Accept-Language", "zh-CN,zh;q=0.9");
			heads.put("Referer", "http://www.053666.cn/user/mymoney.aspx");
			heads.put("Upgrade-Insecure-Requests", "1");
			heads.put("Cookie", cookie);
			String content = MobileHttpUrlConnectUtils.httpGet(url, heads, null);
			Document doc = Jsoup.parse(content);
			String __VIEWSTATE = doc.getElementById("__VIEWSTATE").val();
			String __VIEWSTATEGENERATOR = doc.getElementById("__VIEWSTATEGENERATOR").val();
			String money = doc.getElementById("tdSpecies").val();
			int m = Float.valueOf(money).intValue();
			logger.info("taodan-|提现金额:money={}", m);
			if (m < 500) {
				logger.info("taodan-|金额不足.money={}", money);
				return "";
			}
			return actWithdraw(__VIEWSTATE, __VIEWSTATEGENERATOR, m - 1);
		} catch (Exception e) {
			return null;
		}*/
	}

	public String withdraw(int num) {
		if (num < 1)
			return null;
		try {
			HttpClient client = HttpClients.custom().build();
			HttpGet get = new HttpGet();
			String url = "http://www.053666.cn/user/Withdraw.aspx";
			get.setURI(new URI(url));
			get.addHeader("User-Agent", userAgent);
			get.addHeader("Accept", "*/*");
			get.addHeader("Accept-Language", "zh-CN,zh;q=0.9");
			get.addHeader("Accept-Encoding", "gzip, deflate");
			get.addHeader("Referer", "http://www.053666.cn/user/mymoney.aspx");
			get.addHeader("Connection", "keep-alive");
			get.addHeader("Cookie", cookie);
			HttpResponse resp = client.execute(get);
			String content = readStreamByEncoding(resp.getEntity().getContent(), "UTF-8");
			if (resp.getStatusLine().getStatusCode() == 521) {
				num--;
				boolean s = reSetCookie(content);
				if (!s)
					return null;
				return withdraw(num);
			}
			Document doc = Jsoup.parse(content);
			String __VIEWSTATE = doc.getElementById("__VIEWSTATE").val();
			String __VIEWSTATEGENERATOR = doc.getElementById("__VIEWSTATEGENERATOR").val();
			String money = doc.getElementById("tdSpecies").val();
			int m = Float.valueOf(money).intValue();
			logger.info("taodan-|提现金额:money={}", m);
			if (m < 500) {
				logger.info("taodan-|金额不足.money={}", money);
				return "";
			}
			return actWithdraw(__VIEWSTATE, __VIEWSTATEGENERATOR, m - 1);
		} catch (Exception e) {
			return null;
		}
	}

	public String actWithdraw(String __VIEWSTATE, String __VIEWSTATEGENERATOR, int money) {
		return actWithdraw(__VIEWSTATE, __VIEWSTATEGENERATOR, money, 2);
		/*try {
			String url = "http://www.053666.cn/user/Withdraw.aspx";
			String postData = "__VIEWSTATE=" + __VIEWSTATE + "&__VIEWSTATEGENERATOR=" + __VIEWSTATEGENERATOR + "&money="
					+ money;
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept", "");
			heads.put("Accept-Encoding", "gzip, deflate");
			heads.put("Accept-Language", "zh-CN,zh;q=0.9");
			heads.put("User-Agent", userAgent);
			heads.put("X-Requested-With", "XMLHttpRequest");
			heads.put("Referer", "http://www.053666.cn/user/Withdraw.aspx");
			heads.put("Origin", "http://www.053666.cn");
			heads.put("Cookie", cookie);
			String result = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			logger.info("taodan-|提现结果.result={}", result);
			JSONObject obj = JSONObject.parseObject(result);
			if (obj.getBooleanValue("IsSuccess")) {
				return "";
			} else {
				return "提现失败!";
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("taodan-|提现异常! msg={}", e.getMessage());
			return "提现异常!";
		}*/
	}
	
	public String actWithdraw(String __VIEWSTATE, String __VIEWSTATEGENERATOR, int money, int num) {
		if (num < 1)
			return null;
		try {
			HttpClient client = HttpClients.custom().build();
			HttpPost post = new HttpPost();
			String url = "http://www.053666.cn/user/Withdraw.aspx";
			post.setURI(new URI(url));
			post.addHeader("Host", "www.053666.cn");
			post.addHeader("User-Agent", userAgent);
			post.addHeader("Accept", "*/*");
			post.addHeader("Accept-Language", "zh-CN,zh;q=0.9");
			post.addHeader("Accept-Encoding", "gzip, deflate");
			post.addHeader("X-Requested-With", "XMLHttpRequest");
			post.addHeader("Referer", "http://www.053666.cn/user/Withdraw.aspx");
			post.addHeader("Origin", "http://www.053666.cn");
			post.addHeader("Connection", "keep-alive");
			post.addHeader("Cookie", cookie);
			List<NameValuePair> parms = new ArrayList<NameValuePair>();
			parms.add(new BasicNameValuePair("__VIEWSTATE", __VIEWSTATE));
			parms.add(new BasicNameValuePair("__VIEWSTATEGENERATOR", __VIEWSTATEGENERATOR));
			parms.add(new BasicNameValuePair("money", money+""));
			post.setEntity(new UrlEncodedFormEntity(parms, "UTF-8"));
			HttpResponse resp = client.execute(post);
			String content = readStreamByEncoding(resp.getEntity().getContent(), "UTF-8");
			if (resp.getStatusLine().getStatusCode() == 521) {
				num--;
				boolean s = reSetCookie(content);
				if (!s)
					return null;
				return actWithdraw(__VIEWSTATE, __VIEWSTATEGENERATOR, money, num);
			}
			logger.info("taodan-|提现结果.result={}", content);
			JSONObject obj = JSONObject.parseObject(content);
			if (obj.getBooleanValue("IsSuccess")) {
				return "";
			} else {
				return "提现失败!";
			}
		} catch (Exception e) {
			return null;
		}
	}

	public static void main(String[] args)
			throws InterruptedException, UnsupportedOperationException, IOException, URISyntaxException {
		String c = "__jsluid_h=c51630181c3b60d625764719c98049d6; gdxidpyhxdE=GxNISJaUPuE3ioE7jT8az2ClNrkVMiCzdk6mXN8J7%5CYyGxnl%5CtbKOf971ggiqpg3zwqy2OyXRSr%2FpQGWcPhlOgrppeScYSKZRJySK7OCn34RsE8JltEY72k0Y3gRN47%2FWc%2FyaliZshDGRnVS%2B9t1lgoEfeWZ7b0sywnMMfXGEtXiW3B3%3A1575358073480; _9755xjdesxxd_=32; YD00958817530477%3AWM_NI=7f1%2BXX0DIUDk6CKJBbrJnaqIEg9j0XQ13qhtfdHNzhZZc2jrLfOQpVO%2BVdzqIPOZsYuPfnVy17J5tSPE%2FXQpN%2BW%2FbrKDn9EWrfWZA1aIppL66uqXkVEitdR2rjIy1eBRdFc%3D; YD00958817530477%3AWM_NIKE=9ca17ae2e6ffcda170e2e6eeaee64a898d8e93cf74a6ef8fa3c45b979a8bafb77aa399978cf739a2ec9895ef2af0fea7c3b92af5baffccdb54f49c87b6c761f69d85abc16ab296a282f44387b9f78bb35493effd92eb80a6bc88aff9649a86add3e97a90ba81b2f35e8df59a85f97b8b9081bbf253b790aa8fd573f3e986d6f759949be1b9d47c8ef599d0e17a899ca2dac85db2ba99bbc43ba397c091cf3ef6abbfb9ea5e87b7bc85d13ab2b6879ab46f828eaca7e237e2a3; YD00958817530477%3AWM_TID=lii6Tvq%2FWopEEBQVFRZ5qLSNhpnapW90; .ASPXAUTH=A414DD5B7A2DCC8F2A93022F509BB4486EE1F60991DFF00D884F9C2F20D0773B91ADD15425986D7A2CCF5ED303057012DC121E3338E2B706CF43F988E715C2F3302D6AD6D7BD50151DE595F1C2347DE842FD2ECF6524C539742B22942CA4600E996DAA827C5176A9BE18CDB04A12DBCE8146C24D65008D6FBFC0124E5BAC98C1D7977CC22AAC4A65C5D281FD5C6E16FDC0C4B30C137E022283D5C71ABC263007200636A4107E1D9CD782B02661E31909CA1417A716D295189CEF49725C3E5FCA; ASP.NET_SessionId=keb0o1lf5jcczwwefq2hf2rc; rowcounts_100083=0,0,876,0,0,21; __jsl_clearance=1575360765.513|0|tj%2FcaZt%2FnsuaN1pKm%2B%2BSdvt67Lk%3D; reftime=2019-12-03 16:55:48; refcount=4";
		TaodanHttp http = new TaodanHttp("", c);
		/*
		 * http.taskDetail("11456166"); String name = "  储腾官方旗舰店";
		 * System.out.println(name); name = name.replace(" ", ""); name = name.trim();
		 * System.out.println(name);
		 */
		/*
		 * String logo =
		 * "http://\\\\t16img.yangkeduo.com\\pdd_ims\\61a2587a580fccca9d7a159163622371.png";
		 * System.out.println(getImageName(logo));
		 */
		//http.MyRecTaskList(3, 1, 2);
		http.withdraw();
		// System.out.println(s);

		/*
		 * HttpClient client = HttpClients.custom().build(); HttpGet get = new
		 * HttpGet(); String gotourl =
		 * "http://www.053666.cn/user/RecProductForm.aspx?id=13338337"; get.setURI(new
		 * URI(gotourl)); get.addHeader("User-Agent",
		 * "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36"
		 * ); get.addHeader("Accept", "*"); get.addHeader("Accept-Language",
		 * "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3"); get.addHeader("Accept-Encoding",
		 * "gzip, deflate, br"); get.addHeader("Connection", "keep-alive");
		 * get.addHeader("Cookie", c); HttpResponse resp = client.execute(get); String
		 * content = readStreamByEncoding(resp.getEntity().getContent(), "UTF-8");
		 * System.out.println(content); String cookie =
		 * TaodanUtils.getJslClearance(content); System.out.println(cookie);
		 */
	}

	private static String readStreamByEncoding(InputStream in, String encoding) throws IOException {
		StringBuffer cont = new StringBuffer();
		BufferedReader br = new BufferedReader(new InputStreamReader(in, encoding));
		String tmp = "";
		while ((tmp = br.readLine()) != null) {
			cont.append(tmp);
		}
		br.close();
		return cont.toString();
	}

	public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

}
