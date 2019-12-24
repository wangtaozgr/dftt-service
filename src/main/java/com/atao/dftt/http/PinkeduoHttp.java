package com.atao.dftt.http;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.atao.dftt.util.MobileHttpUrlConnectUtils;

/**
 * 拼客多
 */
public class PinkeduoHttp {
	private static Logger logger = LoggerFactory.getLogger(PinkeduoHttp.class);
	public static Map<String, String> cookies = new HashMap<String, String>();
	public static Map<String, String> authorizations = new HashMap<String, String>();
	public String cookie;
	public String Authorization;
	private String userAgent = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36";

	public PinkeduoHttp(String username) {
		String cookie = cookies.get(username);
		String Authorization = authorizations.get(username); 
		this.cookie = cookie;
		this.Authorization = Authorization;
	}

	public PinkeduoHttp(String username, String cookie, String Authorization) {
		cookies.put(username, cookie);
		authorizations.put(username, Authorization);
		this.cookie = cookie;
		this.Authorization = Authorization;
	}

	public static synchronized PinkeduoHttp getInstance(String username) {
		PinkeduoHttp userHttp = new PinkeduoHttp(username);
		userHttp.cookie = cookies.get(username);
		userHttp.Authorization = authorizations.get(username);
		return userHttp;
	}


	/**
	 * 拼多多确认收货
	 */
	public boolean confirmRecGoods(String taskId) {
		try {
			String url = "https://wap.pinke66.com/popup/confirmreceipt";
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("User-Agent", userAgent);
			heads.put("Accept", "application/json, text/javascript, */*; q=0.01");
			heads.put("Accept-Encoding", "gzip, deflate");
			heads.put("Accept-Language", "zh-CN,zh;q=0.9");
			heads.put("Referer", "https://wap.pinke66.com/popup/taskdetails?task_id=" + taskId + "&status=23");
			heads.put("X-Requested-With", "XMLHttpRequest");
			heads.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			heads.put("Origin", "https://wap.pinke66.com");
			heads.put("Cookie", cookie);
			heads.put("Authorization", Authorization);
			String postData = "task_id=" + taskId + "&imgs_xt_1=";
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			logger.info("taskId={}|更新拼客多任务成发货状态结果={}", taskId, content);
			JSONObject json = JSONObject.parseObject(content);
			if (json.getIntValue("code") == 200)
				return true;
		} catch (Exception e) {
			return false;
		}
		return false;
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
			if (m < 500) {
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
			String postData = "__VIEWSTATE=" + __VIEWSTATE + "&__VIEWSTATEGENERATOR=" + __VIEWSTATEGENERATOR + "&money="
					+ money;
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
		String Authorization = "";
		PinkeduoHttp http = new PinkeduoHttp("", c , Authorization);
		http.withdraw();

	}

	public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

}
