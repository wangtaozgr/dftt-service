package com.atao.dftt.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atao.util.StringUtils;
import com.eclipsesource.v8.V8;

public class MobileHttpUrlConnectUtils {
	private static Logger logger = LoggerFactory.getLogger(MobileHttpUrlConnectUtils.class);

	public static void printCookie(CookieStore cookieStore) {
		List<HttpCookie> listCookie = cookieStore.getCookies();
		listCookie.forEach(httpCookie -> {
			System.out.println("--------------------------------------");
			System.out.println("domain : " + httpCookie.getDomain());
			// System.out.println("maxAge : " + httpCookie.getMaxAge());
			System.out.println("httpCookie : " + httpCookie);
		});
	}

	public static String getCookie(CookieStore cookieStore, String name) {
		List<HttpCookie> listCookie = cookieStore.getCookies();
		for (HttpCookie httpCookie : listCookie) {
			if (name.equals(httpCookie.getName()))
				return httpCookie.getValue();

		}
		return "";
	}

	public static String httpGet(String url, Map<String, String> heads, String charsetName) {
		if (StringUtils.isBlank(charsetName))
			charsetName = "UTF-8";

		HttpURLConnection connection = null;
		InputStream is = null;
		StringBuilder sb = new StringBuilder();
		try {
			// 准备请求的网络地址
			URL urL = new URL(url);
			// 调用openConnection得到网络连接，网络连接处于就绪状态
			connection = (HttpURLConnection) urL.openConnection();
			// 设置网络连接超时时间5S
			connection.setConnectTimeout(5 * 1000);
			// 设置读取超时时间
			connection.setReadTimeout(30 * 1000);
			if (heads != null) {
				for (String key : heads.keySet()) {
					connection.setRequestProperty(key, heads.get(key));
				}
			}
			connection.setDoOutput(true);
			connection.setDoInput(true);

			//connection.setInstanceFollowRedirects(false);
			connection.connect();
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				String encoding = connection.getContentEncoding();
				if ("gzip".equals(encoding)) {
					GZIPInputStream gZIPInputStream = new GZIPInputStream(connection.getInputStream());
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(gZIPInputStream));
					String line = null;
					while ((line = bufferedReader.readLine()) != null) { // 转化为UTF-8的编码格式
						line = new String(line.getBytes(charsetName));
						sb.append(line);
					}
					bufferedReader.close();
				} else {
					BufferedReader bufferedReader = new BufferedReader(
							new InputStreamReader(connection.getInputStream()));
					String line = null;
					while ((line = bufferedReader.readLine()) != null) { // 转化为UTF-8的编码格式
						line = new String(line.getBytes(charsetName));
						sb.append(line);
					}
					bufferedReader.close();
				}
			} else if (connection.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP) {
				sb.append(connection.getHeaderField("Location"));
			}else {
				BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(connection.getInputStream()));
				String line = null;
				while ((line = bufferedReader.readLine()) != null) { // 转化为UTF-8的编码格式
					line = new String(line.getBytes(charsetName));
					sb.append(line);
				}
				bufferedReader.close();
			}
			return sb.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return null;
	}

	public static int httpGetStatusCode(String url, Map<String, String> heads, String charsetName) {
		HttpURLConnection connection = null;
		try {
			// 准备请求的网络地址
			URL urL = new URL(url);
			// 调用openConnection得到网络连接，网络连接处于就绪状态
			connection = (HttpURLConnection) urL.openConnection();
			// 设置网络连接超时时间5S
			connection.setConnectTimeout(3 * 1000);
			// 设置读取超时时间
			connection.setReadTimeout(3 * 1000);
			if (heads != null) {
				for (String key : heads.keySet()) {
					connection.setRequestProperty(key, heads.get(key));
				}
			}
			connection.connect();
			// if连接请求码成功
			return connection.getResponseCode();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return 500;
	}

	public static String httpPost(String url, String postData, Map<String, String> heads, String charsetName) {// Post请求的url，与get不同的是不需要带参数
		if (StringUtils.isBlank(charsetName))
			charsetName = "UTF-8";
		URL postUrl;
		HttpURLConnection connection = null;
		StringBuilder sb = new StringBuilder();
		try {
			postUrl = new URL(url);
			connection = (HttpURLConnection) postUrl.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			// connection.setUseCaches(false);
			// connection.setInstanceFollowRedirects(false);
			if (heads != null) {
				for (String key : heads.keySet()) {
					connection.setRequestProperty(key, heads.get(key));
				}
			}
			byte[] bypes = null;
			if (StringUtils.isNotBlank(postData)) {
				bypes = postData.toString().getBytes();
				connection.setRequestProperty("Content-Length", String.valueOf(bypes.length));
			}
			connection.connect();
			if (bypes != null) {
				connection.getOutputStream().write(bypes);// 输入参数
			}
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				String encoding = connection.getContentEncoding();
				if ("gzip".equals(encoding)) {
					GZIPInputStream gZIPInputStream = new GZIPInputStream(connection.getInputStream());
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(gZIPInputStream));
					String line = null;
					while ((line = bufferedReader.readLine()) != null) { // 转化为UTF-8的编码格式
						line = new String(line.getBytes(charsetName));
						sb.append(line);
					}
					bufferedReader.close();
				} else {
					BufferedReader bufferedReader = new BufferedReader(
							new InputStreamReader(connection.getInputStream()));
					String line = null;
					while ((line = bufferedReader.readLine()) != null) { // 转化为UTF-8的编码格式
						line = new String(line.getBytes(charsetName));
						sb.append(line);
					}
					bufferedReader.close();
				}
			} else if (connection.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP) {
				sb.append(connection.getHeaderField("Location"));
			}
			return sb.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return null;
	}

	public static int httpPostStatusCode(String url, String postData, Map<String, String> heads, String charsetName) {// Post请求的url，与get不同的是不需要带参数
		if (StringUtils.isBlank(charsetName))
			charsetName = "UTF-8";
		URL postUrl;
		HttpURLConnection connection = null;
		InputStream is = null;
		StringBuilder sb = new StringBuilder();
		try {
			postUrl = new URL(url);
			connection = (HttpURLConnection) postUrl.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(false);
			if (heads != null) {
				for (String key : heads.keySet()) {
					connection.setRequestProperty(key, heads.get(key));
				}
			}
			byte[] bypes = null;
			if (StringUtils.isNotBlank(postData)) {
				bypes = postData.toString().getBytes();
				connection.setRequestProperty("Content-Length", String.valueOf(bypes.length));
			}
			connection.connect();
			if (bypes != null) {
				connection.getOutputStream().write(bypes);// 输入参数
			}
			return connection.getResponseCode();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return 0;

	}

	/**
	 * 淘单上传图片
	 * @param url
	 * @param postDataMap
	 * @param filePath
	 * @param BOUNDARY
	 * @param heads
	 * @param charsetName
	 * @return
	 */
	public static String uploadFile(String url, Map<String, String> postDataMap, String filePath, String BOUNDARY,
			Map<String, String> heads, String charsetName) {// Post请求的url，与get不同的是不需要带参数
		if (StringUtils.isBlank(charsetName))
			charsetName = "UTF-8";
		File file = new File(filePath);
		URL postUrl;
		HttpURLConnection connection = null;
		StringBuilder sb = new StringBuilder();
		try {
			postUrl = new URL(url);
			connection = (HttpURLConnection) postUrl.openConnection();
			connection.setUseCaches(false);
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + "----" + BOUNDARY);
			if (heads != null) {
				for (String key : heads.keySet()) {
					connection.setRequestProperty(key, heads.get(key));
				}
			}
			connection.connect();
			StringBuilder contentBody1 = new StringBuilder();
			StringBuilder contentBody2 = new StringBuilder();
			String boundary = "------" + BOUNDARY + "\r\n";
			DataOutputStream out = new DataOutputStream(connection.getOutputStream());
			byte[] end_data = ("------" + BOUNDARY + "--").getBytes();
			if (file != null) {
				contentBody1.append(boundary);
				contentBody1.append(
						"Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getName() + "\"" + "\r\n");
				contentBody1.append("Content-Type: image/png" + "\r\n");
				contentBody1.append("\r\n");
				out.write(contentBody1.toString().getBytes());
				// 读取excel文件
				DataInputStream dis = new DataInputStream(new FileInputStream(file));
				int bytes = 0;
				byte[] bufferOut = new byte[(int) file.length()];
				bytes = dis.read(bufferOut);
				out.write(bufferOut, 0, bytes);
				out.write("\r\n".getBytes());
				dis.close();
				// 第二部分参数：其他参数promoId,operator
				Set<String> set = postDataMap.keySet();
				Iterator<String> iterator = set.iterator();
				while (iterator.hasNext()) {
					String key = iterator.next();
					String value = postDataMap.get(key);
					contentBody2.append(boundary);
					contentBody2.append("Content-Disposition: form-data; name=\"" + key + "\"" + "\r\n");
					contentBody2.append("\r\n");
					contentBody2.append(value + "\r\n");
				}
				out.write(contentBody2.toString().getBytes());
				out.write(end_data);
				out.flush();
				// 从服务器获得回答的内容
				if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
					String encoding = connection.getContentEncoding();
					if ("gzip".equals(encoding)) {
						GZIPInputStream gZIPInputStream = new GZIPInputStream(connection.getInputStream());
						BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(gZIPInputStream));
						String line = null;
						while ((line = bufferedReader.readLine()) != null) { // 转化为UTF-8的编码格式
							line = new String(line.getBytes(charsetName));
							sb.append(line);
						}
						bufferedReader.close();
					} else {
						BufferedReader bufferedReader = new BufferedReader(
								new InputStreamReader(connection.getInputStream()));
						String line = null;
						while ((line = bufferedReader.readLine()) != null) { // 转化为UTF-8的编码格式
							line = new String(line.getBytes(charsetName));
							sb.append(line);
						}
						bufferedReader.close();
					}
				} else if (connection.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP) {
					sb.append(connection.getHeaderField("Location"));
				}
				out.close();
				return sb.toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return null;
	}
	
	/**
	 * 赚客上传商品搜索图片
	 * @param url
	 * @param postDataMap
	 * @param filePath
	 * @param BOUNDARY
	 * @param heads
	 * @param charsetName
	 * @return
	 */
	public static String uploadZhuankeFile(String url, Map<String, String> postDataMap, String filePath, String BOUNDARY,
			Map<String, String> heads, String charsetName) {// Post请求的url，与get不同的是不需要带参数
		if (StringUtils.isBlank(charsetName))
			charsetName = "UTF-8";
		File file = new File(filePath);
		URL postUrl;
		HttpURLConnection connection = null;
		StringBuilder sb = new StringBuilder();
		try {
			postUrl = new URL(url);
			connection = (HttpURLConnection) postUrl.openConnection();
			connection.setUseCaches(false);
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + "----" + BOUNDARY);
			if (heads != null) {
				for (String key : heads.keySet()) {
					connection.setRequestProperty(key, heads.get(key));
				}
			}
			connection.connect();
			StringBuilder contentBody1 = new StringBuilder();
			StringBuilder contentBody2 = new StringBuilder();
			String boundary = "------" + BOUNDARY + "\r\n";
			DataOutputStream out = new DataOutputStream(connection.getOutputStream());
			byte[] end_data = ("------" + BOUNDARY + "--").getBytes();
			if (file != null) {
				contentBody1.append(boundary);
				contentBody1.append(
						"Content-Disposition: form-data; name=\"imgFile\"; filename=\"" + file.getName() + "\"" + "\r\n");
				contentBody1.append("Content-Type: image/png" + "\r\n");
				contentBody1.append("\r\n");
				out.write(contentBody1.toString().getBytes());
				// 读取excel文件
				DataInputStream dis = new DataInputStream(new FileInputStream(file));
				int bytes = 0;
				byte[] bufferOut = new byte[(int) file.length()];
				bytes = dis.read(bufferOut);
				out.write(bufferOut, 0, bytes);
				out.write("\r\n".getBytes());
				dis.close();
				// 第二部分参数：其他参数promoId,operator
				Set<String> set = postDataMap.keySet();
				Iterator<String> iterator = set.iterator();
				while (iterator.hasNext()) {
					String key = iterator.next();
					String value = postDataMap.get(key);
					contentBody2.append(boundary);
					contentBody2.append("Content-Disposition: form-data; name=\"" + key + "\"" + "\r\n");
					contentBody2.append("\r\n");
					contentBody2.append(value + "\r\n");
				}
				out.write(contentBody2.toString().getBytes());
				out.write(end_data);
				out.flush();
				// 从服务器获得回答的内容
				if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
					String encoding = connection.getContentEncoding();
					if ("gzip".equals(encoding)) {
						GZIPInputStream gZIPInputStream = new GZIPInputStream(connection.getInputStream());
						BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(gZIPInputStream));
						String line = null;
						while ((line = bufferedReader.readLine()) != null) { // 转化为UTF-8的编码格式
							line = new String(line.getBytes(charsetName));
							sb.append(line);
						}
						bufferedReader.close();
					} else {
						BufferedReader bufferedReader = new BufferedReader(
								new InputStreamReader(connection.getInputStream()));
						String line = null;
						while ((line = bufferedReader.readLine()) != null) { // 转化为UTF-8的编码格式
							line = new String(line.getBytes(charsetName));
							sb.append(line);
						}
						bufferedReader.close();
					}
				} else if (connection.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP) {
					sb.append(connection.getHeaderField("Location"));
				}
				out.close();
				return sb.toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return null;
	}
	
	public static String http302Get(String url, Map<String, String> heads, String charsetName) {
		if (StringUtils.isBlank(charsetName))
			charsetName = "UTF-8";

		HttpURLConnection connection = null;
		InputStream is = null;
		StringBuilder sb = new StringBuilder();
		try {
			// 准备请求的网络地址
			URL urL = new URL(url);
			// 调用openConnection得到网络连接，网络连接处于就绪状态
			connection = (HttpURLConnection) urL.openConnection();
			// 设置网络连接超时时间5S
			connection.setConnectTimeout(5 * 1000);
			// 设置读取超时时间
			connection.setReadTimeout(30 * 1000);
			if (heads != null) {
				for (String key : heads.keySet()) {
					connection.setRequestProperty(key, heads.get(key));
				}
			}
			connection.setInstanceFollowRedirects(false);
			connection.connect();
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				String encoding = connection.getContentEncoding();
				if ("gzip".equals(encoding)) {
					GZIPInputStream gZIPInputStream = new GZIPInputStream(connection.getInputStream());
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(gZIPInputStream));
					String line = null;
					while ((line = bufferedReader.readLine()) != null) { // 转化为UTF-8的编码格式
						line = new String(line.getBytes(charsetName));
						sb.append(line);
					}
					bufferedReader.close();
				} else {
					BufferedReader bufferedReader = new BufferedReader(
							new InputStreamReader(connection.getInputStream()));
					String line = null;
					while ((line = bufferedReader.readLine()) != null) { // 转化为UTF-8的编码格式
						line = new String(line.getBytes(charsetName));
						sb.append(line);
					}
					bufferedReader.close();
				}
			} else if (connection.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP) {
				sb.append(connection.getHeaderField("Location"));
			}
			return sb.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return null;
	}
	
	private static String getJslClearance(String body) {
        //V8:谷歌开源的运行JavaScript脚本的库. 参数:globalAlias=window, 表示window为全局别名,
        // 告诉V8在运行JavaScript代码时, 不要从代码里找window的定义.
        V8 runtime = V8.createV8Runtime("window");
        //将第一次请求pdf资源时获取到的字符串提取成V8可执行的JavaScript代码
        body = body.trim()
                .replace("<script>", "")
                .replace("</script>", "")
                .replace("eval(y.replace(/\\b\\w+\\b/g, function(y){return x[f(y,z)-1]||(\"_\"+y)}))",
                        "y.replace(/\\b\\w+\\b/g, function(y){return x[f(y,z)-1]||(\"_\"+y)})");
        //用V8执行该段代码获取新的动态JavaScript脚本
        String result = runtime.executeStringScript(body);

        //获取 jsl_clearance 的第一段, 格式形如: 1543915851.312|0|
        String startStr = "document.cookie='";
        int i1 = result.indexOf(startStr) + startStr.length();
        int i2 = result.indexOf("|0|");
        String  cookie1 = result.substring(i1, i2 + 3);
        /*
        获取 jsl_clearance 的第二段,格式形如: DW2jqgJO5Bo45yYRKLlFbnqQuD0%3D。
        主要原理是: 新的动态JavaScript脚本是为浏览器设置cookie, 且cookie名为__jsl_clearance
        其中第一段值(格式形如:1543915851.312|0|)已经明文写好, 用字符串处理方法即可获取.
        第二段则是一段JavaScript函数, 需要有V8运行返回,
        该函数代码需要通过一些字符串定位, 提取出来, 交给V8运行.
         */
        startStr = "|0|'+(function(){";
        int i3 = result.indexOf(startStr) + startStr.length();
        int i4 = result.indexOf("})()+';Expires");
        String code = result.substring(i3, i4).replace(";return _n", "; _n");
        int i5 = code.indexOf("document.createElement");
        if(i5>-1) {
        	int i6 = code.indexOf(";return",i5);
        	code = code.substring(0,i5)+"'www.053666.cn'"+code.substring(i6);
        }
        String cookie2 = runtime.executeStringScript(code);

        /*
        拼接两段字符串, 返回jsl_clearance的完整的值.
        格式形如: 1543915851.312|0|DW2jqgJO5Bo45yYRKLlFbnqQuD0%3D
        */
        return cookie1 + cookie2;
    }
	
	public static void main(String[] args) {
		System.out.println(getJslClearance("<script>var x=\"27@catch@@match@String@cookie@@return@@false@https@search@@g@eval@19@Path@split@@firstChild@o@@@if@@36@setTimeout@0xEDB88320@02@Expires@addEventListener@@@Nov@__jsl_clearance@1574830962@p@D@try@div@@parseInt@headless@@toLowerCase@@d@fromCharCode@0xFF@@06@join@rOm9XFMtA3QKV7nYsPGT4lifyWwkq5vcjH2IdxUoCbhERLaz81DNB6@@@new@2@reverse@challenge@151@@1500@0@@QRX@substr@attachEvent@Array@@GMT@@else@@@@@@@@@@charAt@@@var@captcha@@@for@chars@document@I@DOMContentLoaded@3@f@@charCodeAt@onreadystatechange@while@window@href@@@1@e@@8@i6@RegExp@A@JgSe0upZ@toString@@@replace@@location@Wed@createElement@innerHTML@pathname@a@@length@@function@42@\".replace(/@*$/,\"\").split(\"@\"),y=\"31 3m=4e(){r('45.3h=45.49+45.c.43(/[\\\\?|&]32-23/,\\\\'\\\\')',26);37.6='17=18.24|27|'+(4e(){31 3=[4e(3m){8 3m},4e(3){8 3},(4e(){31 3m=37.47('1c');3m.48='<4a 3h=\\\\'/\\\\'>n</4a>';3m=3m.k.3h;31 3=3m.4(/b?:\\\\/\\\\//)[27];3m=3m.2a(3.4c).1h();8 4e(3){35(31 n=27;n<3.4c;n++){3[n]=3m.2q(3[n])};8 3.1o('')}})(),4e(3m){8 f('5.1k('+3m+')')}],n=['l',[(-~{}+[])+[21]],'3o',[+[(+!-[]), (+!-[])]+[[]][27]][27].2q(21),[((+!-[])-~!/!/+(-~{}<<-~{})-~!/!/+(-~{}<<-~{})+[[]][27])+(-~!/!/+(-~{}<<-~{})-~!/!/+(-~{}<<-~{})+[])],[(-~!/!/+(-~~~[]-~~~[]^-~{})+[[]][27])],[(-~!/!/+(-~{}<<-~{})-~!/!/+(-~{}<<-~{})+[])+((+!-[])-~!/!/+(-~{}<<-~{})-~!/!/+(-~{}<<-~{})+[[]][27])],'3l',[[(+!-[])-~!/!/-~[(-~{}+[-~(+!-[])])/[-~(+!-[])]]]+[(+[])],(-~!/!/+(-~{}<<-~{})-~!/!/+(-~{}<<-~{})+[])+[(+!-[])-~!/!/-~[(-~{}+[-~(+!-[])])/[-~(+!-[])]]],[(-~~~[]-~~~[])*[-~((-~[]|(-~{}<<-~{})))]]+[(-~~~[]-~~~[])*[-~((-~[]|(-~{}<<-~{})))]]],'38',[(-~{}+[])+[(+[])]+[(+!-[])-~!/!/-~[(-~{}+[-~(+!-[])])/[-~(+!-[])]]]],[(3a)/(+!/!/)+[]+[]][27].2q(~~''),[[(-~~~[]-~~~[])*[-~((-~[]|(-~{}<<-~{})))]]+((+!-[])-~!/!/+(-~{}<<-~{})-~!/!/+(-~{}<<-~{})+[[]][27])],(!!3g.1f+[[]][27]).2q((-~!/!/+[(-~{}<<-~{})]>>(-~{}<<-~{}))),[((+!-[])-~!/!/+(-~{}<<-~{})-~!/!/+(-~{}<<-~{})+[[]][27])+((+!-[])-~!/!/+(-~{}<<-~{})-~!/!/+(-~{}<<-~{})+[[]][27]),[(-~~~[]-~~~[])*[-~((-~[]|(-~{}<<-~{})))]]+(-~{}+[])],[(-~!/!/+(-~~~[]-~~~[]^-~{})+[[]][27])],'19',[[(-~~~[]-~~~[])*[-~((-~[]|(-~{}<<-~{})))]]+[(+[])]],'29',(!+[]+[]+[[]][27]).2q((-~!/!/+[(-~{}<<-~{})]>>(-~{}<<-~{}))),'3q%',[(-~!/!/+(-~{}<<-~{})-~!/!/+(-~{}<<-~{})+[])],'1a'];35(31 3m=27;3m<n.4c;3m++){n[3m]=3[[3k,21,3k,27,3a,21,3a,3k,3a,3k,3a,27,3a,27,3a,21,3k,3a,3k,27,3k,21,3k][3m]](n[3m])};8 n.1o('')})()+';12=46, 1-16-g 1n:11:4f 2e;h=/;'};o((4e(){1b{8 !!3g.13;}2(3l){8 a;}})()){37.13('39',3m,a)}2g{37.2b('3e',3m)}\",f=function(x,y){var a=0,b=0,c=0;x=x.split(\"\");y=y||99;while((a=x.shift())&&(b=a.charCodeAt(0)-77.5))c=(Math.abs(b)<13?(b+48.5):parseInt(a,36))+y*c;return c},z=f(y.match(/\\w/g).sort(function(x,y){return f(x)-f(y)}).pop());while(z++)try{eval(y.replace(/\\b\\w+\\b/g, function(y){return x[f(y,z)-1]||(\"_\"+y)}));break}catch(_){}</script>"));
	}
}
