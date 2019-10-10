package com.atao.dftt.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
import com.atao.dftt.util.MobileHttpUrlConnectUtils;
import com.atao.dftt.util.TdHttpUrlConnectUtils;
import com.atao.dftt.util.qrcode.QRCodeUtil;
import com.atao.util.StringUtils;

/**
 * 多多好评
 */
public class TaodanHttp {
	private static Logger logger = LoggerFactory.getLogger(TaodanHttp.class);
	public static Map<String, String> cookies = new HashMap<String, String>();
	public String cookie;
	private String userAgent = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36";

	public TaodanHttp(String username) {
		String cookie = cookies.get(username);
		this.cookie = cookie;
	}

	public TaodanHttp(String username, String cookie) {
		cookies.put(username, cookie);
		this.cookie = cookie;
	}

	public static synchronized TaodanHttp getInstance(String username) {
		TaodanHttp userHttp = new TaodanHttp(username);
		userHttp.cookie = cookies.get(username);
		return userHttp;
	}

	/**
	 * 登陆成功后自动跳转到用户个人中心
	 */
	public List<String> MyRecTaskList(int state, int page) {
		List<String> taskIds = new ArrayList<String>();
		try {
			String url = "http://www.053666.cn/user/MyRecTaskList.aspx?state=" + state + "&page=" + page;
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("User-Agent", userAgent);
			heads.put("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
			heads.put("Accept-Encoding", "gzip, deflate");
			heads.put("Accept-Language", "zh-CN,zh;q=0.9");
			heads.put("Referer", "http://www.053666.cn/user/myrectaskList.aspx?state=9,2,3,4,6");
			heads.put("Upgrade-Insecure-Requests", "1");
			heads.put("Cookie", cookie);
			String content = MobileHttpUrlConnectUtils.httpGet(url, heads, null);
			Document doc = Jsoup.parse(content);
			Elements ids = doc.getElementsByAttributeValue("value", "查看任务");
			for (int i = 0; i < ids.size(); i++) {
				String onclick = ids.get(i).attr("onclick");
				int end  = onclick.indexOf("'});");
				String taskId = onclick.substring(onclick.indexOf("id=")+3, end);
				taskIds.add(taskId);
			}
		} catch (Exception e) {
			return null;
		}
		return taskIds;
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
	
	public String getPjContent(String taskId) {
		try {
			String url = "http://www.053666.cn/user/RecProductForm.aspx?id=" + taskId;
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("User-Agent", userAgent);
			heads.put("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
			heads.put("Accept-Encoding", "gzip, deflate");
			heads.put("Accept-Language", "zh-CN,zh;q=0.9");
			heads.put("Referer", "http://www.053666.cn/user/myrectaskList.aspx?state=9,2,3,4,6");
			heads.put("Upgrade-Insecure-Requests", "1");
			heads.put("Cookie", cookie);
			String content = MobileHttpUrlConnectUtils.httpGet(url, heads, null);
			return content;
		} catch (Exception e) {
			return null;
		}
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
			//logger.info("taskId={}|淘单任务详细={}", taskId, content);
			Document doc = Jsoup.parse(content);
			String title = doc.getElementsByClass("mui-title").get(0).text();
			if("等待接单".equals(title)){
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
		try {
			String url = "http://www.053666.cn/user/Withdraw.aspx";
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("User-Agent", userAgent);
			heads.put("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
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
			float m = Float.valueOf(money).floatValue();
			if(m<500) {
				logger.info("taodan-|金额不足.money={}", money);
				return "";
			}
			return actWithdraw(__VIEWSTATE, __VIEWSTATEGENERATOR, money);
		} catch (Exception e) {
			return null;
		}
	}
	
	public String actWithdraw(String __VIEWSTATE, String __VIEWSTATEGENERATOR, String money) {
		try {
			String url = "http://www.053666.cn/user/Withdraw.aspx";
			String postData = "__VIEWSTATE="+__VIEWSTATE+"&__VIEWSTATEGENERATOR="+__VIEWSTATEGENERATOR+"&money="+money;
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept", "application/json, text/javascript, */*; q=0.01");
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
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		String c = "__jsluid_h=11e0d806fc02faf8d778cab3dd922dde;_9755xjdesxxd_=32;gdxidpyhxdE=v0LcC7sMPs2NTdokMYRpzXb699iV6i74YXIZ%5CUhhJEatKz4ai%5CwObKK957MffJCjvwN%5CWgV%2FL63%2F12K031yLTUVOTCh%5CMSfeeztPe9VG%2BCftHaoanbNUviQ3CguKdbRLQCoHdTp6w1jY4mHQ1uV3mLyJtYqTavWZHu2Pt%5C%2FoYt7Ze72t%3A1570147989228;YD00958817530477%3AWM_NI=IISE1aK3ZD0CSxF9e4L56l9D6hp65hwT%2BjiQjmTYrRJELWF%2F8kwewNOHqaOcpAS%2F%2B6yGpP%2Fbml7k5NIz6chSMRYAHbmXxRKvLBVfHtEnCIYR9I4oGD1kCdoWSB8%2B1w2SQmM%3D;YD00958817530477%3AWM_NIKE=9ca17ae2e6ffcda170e2e6eed7c534b89b9a91ce6f8d868ab7c45e829e9bbbbc34fb93a9b7e58090aba499d62af0fea7c3b92a8b979da6b46b89ed96a5b34991a88adae84090ebfa8dbc50f6b8c0d7b864b58ce58bec5baab39cbaf15a8a9b84b8c525fcb5b986f6468d98bf82ce69b49f9dd0aa3ebb99c0b1ae6aafb4f98cb8468ba6fa84eb598f8e97a2cb6aaf88b8b3ca7e909e9a87cb8087b28daadc5afb97fc90c650f3b58d85ee5d919ef8ccb47df28d9f8ed037e2a3;YD00958817530477%3AWM_TID=wXLUQttkjEZARRFBAUctp8MhBa4avkgW;Hm_lvt_d7682ab43891c68a00de46e9ce5b76aa=1569914864,1570015650,1570147122,1570202425;ASP.NET_SessionId=3srib3bm35yl50s3pv5ftonf;.ASPXAUTH=2F89562BA333403B43CDB1FD7E557F62B77DE84701BA4A435ABF68F49DD52694D5DACF7A5A8E27BAFA746B655DCAE14C3BAEE2FD623BD0CA6C6EA2996D1EE47D351BC0A41AFA018AD838CE03547049089BC9F62EA423C2036C1EA4FC505F3526F053D7ED39389A93390893F4E81DAE9ED2135A5397D0FF5161E069C40CD466BDA0A4D366F2D62E68726206A84D453D1F70D680B49D01132837D7CB5BA44BE42464BB3CA5055A274E5F5BA6E99284E8720D93773088EBABA2CCE07F49B8C06E24;rowcounts_72345=2,0,351,1,0,27;reftime=2019-10-08 12:44:24;refcount=4;";
		TaodanHttp http = new TaodanHttp("", c);
		/*http.taskDetail("11456166");
		String name = "  储腾官方旗舰店";
		System.out.println(name);
		name = name.replace(" ", "");
		name = name.trim();
		System.out.println(name);*/
		/*String logo = "http://\\\\t16img.yangkeduo.com\\pdd_ims\\61a2587a580fccca9d7a159163622371.png";
		System.out.println(getImageName(logo));*/
		http.withdraw();

	}

	public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

}
