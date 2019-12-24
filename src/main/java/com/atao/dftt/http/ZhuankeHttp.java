package com.atao.dftt.http;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javax.imageio.ImageIO;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.atao.dftt.api.vo.DataVo;
import com.atao.dftt.util.CommonUtils;
import com.atao.dftt.util.FileUtils;
import com.atao.dftt.util.MobileHttpUrlConnectUtils;
import com.atao.dftt.util.qrcode.BufferedImageLuminanceSource;
import com.atao.util.StringUtils;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

/**
 * 多多好评
 */
public class ZhuankeHttp {
	private static Logger logger = LoggerFactory.getLogger(ZhuankeHttp.class);
	public static Map<String, String> cookies = new HashMap<String, String>();
	public String cookie;
	private String userAgent = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36";

	public ZhuankeHttp(String username) {
		String cookie = cookies.get(username);
		this.cookie = cookie;
	}

	public ZhuankeHttp(String username, String cookie) {
		cookies.put(username, cookie);
		this.cookie = cookie;
	}

	public static synchronized ZhuankeHttp getInstance(String username) {
		ZhuankeHttp userHttp = new ZhuankeHttp(username);
		userHttp.cookie = cookies.get(username);
		return userHttp;
	}

	public boolean checkGoods(String taskId, String goodsUrl) {
		try {
			String url = "http://zk.gxrdwl.com/index.php/Mobile/TaskMem/checkUrl";
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("User-Agent", userAgent);
			heads.put("Accept", "*/*");
			heads.put("Accept-Encoding", "gzip, deflate");
			heads.put("Accept-Language", "zh-CN,zh;q=0.9");
			heads.put("Referer", "http://zk.gxrdwl.com/index.php/Mobile/TaskMem/wview?id=" + taskId);
			heads.put("X-Requested-With", "XMLHttpRequest");
			heads.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			heads.put("Origin", "http://zk.gxrdwl.com");
			heads.put("Cookie", cookie);
			String postData = "id=" + taskId + "&checkUrl=" + goodsUrl;
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			// logger.info("taskId={}|检测商品是否正确接口={}", taskId, content);
			if (content.contains("success"))
				return true;
		} catch (Exception e) {
			return false;
		}
		return false;
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
			logger.info("taskId={}|更新赚客任务成发货状态结果={}", taskId, content);
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
			String url = "http://zk.gxrdwl.com/index.php/Main/TaskMem/tview/id/" + taskId;
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("User-Agent", userAgent);
			heads.put("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			heads.put("Accept-Encoding", "gzip, deflate");
			heads.put("Accept-Language", "zh-CN,zh;q=0.9");
			heads.put("Referer", "http://zk.gxrdwl.com/index.php/Main/TaskMem/tlist?mv=3");
			heads.put("Upgrade-Insecure-Requests", "1");
			heads.put("Cookie", cookie);
			String content = MobileHttpUrlConnectUtils.httpGet(url, heads, null);
			return content;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 提醒收货
	 * 
	 */
	public List<String> txReceiveGoods() {
		List<String> taskIds = new ArrayList<String>();
		try {
			String url = "http://zk.gxrdwl.com/index.php/Main/TaskMem/tlist?mv=9";
			// url = "http://zk.gxrdwl.com/index.php/Main/TaskMem/tlist?mv=3";
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("User-Agent", userAgent);
			heads.put("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
			heads.put("Accept-Encoding", "gzip, deflate");
			heads.put("Accept-Language", "zh-CN,zh;q=0.9");
			heads.put("Referer", "http://zk.gxrdwl.com/index.php/Main/TaskMem/tlist");
			heads.put("Upgrade-Insecure-Requests", "1");
			heads.put("Cookie", cookie);
			String content = MobileHttpUrlConnectUtils.httpGet(url, heads, null);
			Document doc = Jsoup.parse(content);
			Element list = doc.getElementsByClass("list").get(0);
			Elements tables = list.getElementsByTag("table");
			Element table = null;
			String taskId = "";
			for (int i = 0; i < tables.size(); i++) {
				table = tables.get(i);
				if ("list".equals(table.className()))
					continue;
				taskId = table.getElementsByAttributeValue("value", "查看任务").get(0).attr("onClick");
				taskId = taskId.replace("viewTask(", "").replace(")", "");
				taskIds.add(taskId);
			}
			logger.info("zhuanke-|提醒收货结果.result={}", taskIds);
			return taskIds;
		} catch (Exception e) {
			return new ArrayList<String>();
		}
	}

	public String uploadProductImg(String username, String taskId, String filePath) {
		try {
			String url = "http://zk.gxrdwl.com/index.php/Main/Upload/upload_json_task";
			Map<String, String> postDataMap = new HashMap<String, String>();
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
			heads.put("Accept-Encoding", "gzip, deflate");
			heads.put("Accept-Language", "zh-CN,zh;q=0.9");
			heads.put("User-Agent", userAgent);
			heads.put("X-Requested-With", "XMLHttpRequest");
			heads.put("Referer", "http://zk.gxrdwl.com/index.php/Main/TaskMem/wview/id/" + taskId);
			heads.put("Origin", "http://zk.gxrdwl.com");
			heads.put("Upgrade-Insecure-Requests", "1");
			heads.put("Cookie", cookie);
			String BOUNDARY = getBoundary();
			String result = MobileHttpUrlConnectUtils.uploadZhuankeFile(url, postDataMap, filePath, BOUNDARY, heads,
					null);
			logger.info("zhuanke-{}:{}|上传图片结果.result={}", username, taskId, result);
			JSONObject obj = JSONObject.parseObject(result);
			if (obj.getIntValue("error") == 0) {
				return obj.getString("url");
			} else {
				logger.info("zhuanke-{}:{}|上传图片失败.result={}", username, taskId, obj);
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("zhuanke-{}:{}|上传图片异常! msg={}", username, taskId, e.getMessage());
			return null;
		}
	}

	private String getBoundary() {
		String random = "gOdGDotltrKycQGF";
		return "WebKitFormBoundary" + random;
	}

	/**
	 * 赚客确认收货
	 */
	public String recProductFrom(String username, String taskId, String filePath) {
		String tpSH = uploadProductImg(username, taskId, filePath);
		if (StringUtils.isBlank(tpSH)) {
			logger.info("zhuanke-{}:{}|赚客确认收货失败,上传图片失败", username, taskId);
			return "赚客确认收货失败";
		}
		tpSH = "//zk.gxrdwl.com" + tpSH;
		logger.info("zhuanke-{}:{}|赚客确认收货上传图片={}", username, taskId, tpSH);

		try {
			String url = "http://zk.gxrdwl.com/index.php/Main/TaskMem/tview/id/" + taskId;
			String postData = "id=" + taskId + "&tpSH=" + CommonUtils.encode(tpSH);
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept", "application/json, text/javascript, */*; q=0.01");
			heads.put("Accept-Encoding", "gzip, deflate");
			heads.put("Accept-Language", "zh-CN,zh;q=0.9");
			heads.put("User-Agent", userAgent);
			heads.put("X-Requested-With", "XMLHttpRequest");
			heads.put("Referer", "http://zk.gxrdwl.com/index.php/Main/TaskMem/tview/id/" + taskId);
			heads.put("Origin", "http://zk.gxrdwl.com");
			heads.put("Cookie", cookie);
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			logger.info("zhuanke-{}:{}|确认收货结果.result={}", username, taskId, content);
			if (content.contains("success")) {
				return "";
			} else {
				return "赚客确认收货失败!";
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("zhuanke-{}:{}|赚客确认收货异常! msg={}", username, taskId, e.getMessage());
			return "赚客确认收货异常!";
		}

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
			String url = "http://zk.gxrdwl.com/index.php/Main/Finance/outdo";
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("User-Agent", userAgent);
			heads.put("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
			heads.put("Accept-Encoding", "gzip, deflate");
			heads.put("Accept-Language", "zh-CN,zh;q=0.9");
			heads.put("Referer", "http://zk.gxrdwl.com/index.php/Main/Center/index");
			heads.put("Upgrade-Insecure-Requests", "1");
			heads.put("Cookie", cookie);
			String content = MobileHttpUrlConnectUtils.httpGet(url, heads, null);
			Document doc = Jsoup.parse(content);
			String dodate = doc.getElementsByAttributeValue("name", "dodate").get(0).val();
			Elements tds = doc.getElementById("editform").getElementsByTag("td");
			String money = tds.get(2).text();
			int m = Float.valueOf(money).intValue();
			logger.info("zhuanke-|提现金额:money={}", m);
			if (m < 200) {
				logger.info("zhuanke-|金额不足.money={}", money);
				return "金额小于200，提现失败!";
			}
			return actWithdraw(dodate, m);
		} catch (Exception e) {
			return null;
		}
	}

	public String actWithdraw(String dodate, int money) {
		try {
			String url = "http://zk.gxrdwl.com/index.php/Main/Finance/outdo";
			String postData = "dodate=" + dodate + "&doprice=" + money;
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept", "application/json, text/javascript, */*; q=0.01");
			heads.put("Accept-Encoding", "gzip, deflate");
			heads.put("Accept-Language", "zh-CN,zh;q=0.9");
			heads.put("User-Agent", userAgent);
			heads.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			heads.put("X-Requested-With", "XMLHttpRequest");
			heads.put("Referer", "http://zk.gxrdwl.com/index.php/Main/Finance/outdo");
			heads.put("Origin", "http://zk.gxrdwl.com");
			heads.put("Cookie", cookie);
			String result = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			logger.info("zhuanke-|提现结果.result={}", result);
			if (result.contains("success")) {
				return "";
			} else {
				return "提现失败!";
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("zhuanke-|提现异常! msg={}", e.getMessage());
			return "提现异常!";
		}
	}

	public String decode(String ewmurl) {
		try {
			URL url = new URL(ewmurl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty("Cookie", cookie);
			// 设置超时间为3秒
			conn.setConnectTimeout(10 * 1000);
			// 防止屏蔽程序抓取而返回403错误
			conn.setRequestProperty("User-Agent", userAgent);
			// 得到输入流
			InputStream inputStream = conn.getInputStream();
			BufferedImage image = ImageIO.read(inputStream);
			if (image == null) {
				return null;
			}
			BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
			BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
			Result result;
			Hashtable hints = new Hashtable();
			hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
			hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
			hints.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);
			result = new MultiFormatReader().decode(bitmap, hints);
			String resultStr = result.getText();
			return resultStr;
		} catch (Exception e) {
			return "";
		}
	}

	public static void main(String[] args) throws InterruptedException {
		String c = "__jsluid_h=9a98e5726e6d3e8fabaae7407f50ea36; UM_distinctid=16dbf6f9a54653-065f290c28d051-454c092b-1fa400-16dbf6f9a556de; PHPSESSID=i40ann5bcgk8jehjcg5qck80d0; CNZZDATA1253974813=46574889-1570871775-%7C1572334135; userMemZkRdwl=think%3A%7B%22id%22%3A%2277828%22%2C%22username%22%3A%22ZK77828%22%2C%22truename%22%3A%22%25E6%25B1%25AA%25E6%25B6%259B%22%2C%22sftype%22%3A%222%22%7D; Hm_lvt_d7682ab43891c68a00de46e9ce5b76aa=1571297210,1573207698; Hm_lpvt_d7682ab43891c68a00de46e9ce5b76aa=1573275101; __jsl_clearance=1573441751.205|0|aHM39mHnnESy3rqQzvLWvQtJ3H0%3D";
		ZhuankeHttp http = new ZhuankeHttp("", c);
		String ewm = http.decode("http://zk.gxrdwl.com/index.php/Main/TaskMem/QRcode?id=58984690112");
		System.out.println(ewm);
		// http.checkGoods("7272568",
		// "https://mobile.yangkeduo.com/goods.html?goods_id=30435517642");
		// http.txReceiveGoods();
		// System.out.println(http.decode("http://zk.gxrdwl.com/index.php/Main/TaskMem/QRcodeUrl?id=7480470&tdb=1"));
		// zk.gxrdwl.com/UploadPic/Task/2019/20191016/20191016142836_14952.png
		// {"error":0,"url":"\/UploadPic\/Task\/2019\/20191016\/20191016142539_92554.png"}
		// https://p.pinduoduo.com/Id2XBFcQ
		// String u = http.httpGet("https://p.pinduoduo.com/Id2XBFcQ", heads, null);
		// System.out.println(u);
		// https://mobile.yangkeduo.com/duo_coupon_landing.html?goods_id=54342085854&pid=9290340_115513700&cpsSign=CC_191016_9290340_115513700_95749f43e8ac07c3be19057a15e827c5&duoduo_type=2&refer_page_name=app&refer_page_id=10784_1571808893907_XTwZ4qjobd&refer_page_sn=10784
		//http.withdraw();

	}

	public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

}
