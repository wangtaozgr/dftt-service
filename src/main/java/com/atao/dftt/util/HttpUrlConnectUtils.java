package com.atao.dftt.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import com.atao.util.StringUtils;

public class HttpUrlConnectUtils {

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

	public static String httpGet(String url, boolean redirect) {
		HttpURLConnection connection = null;
		InputStream is = null;
		StringBuilder sb = new StringBuilder();
		try {
			// 准备请求的网络地址
			URL urL = new URL(url);
			// 调用openConnection得到网络连接，网络连接处于就绪状态
			connection = (HttpURLConnection) urL.openConnection();
			// 设置网络连接超时时间5S
			connection.setConnectTimeout(3 * 1000);
			// 设置读取超时时间
			connection.setReadTimeout(3 * 1000);
			connection.setInstanceFollowRedirects(redirect);
			connection.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
			connection.connect();
			/*Map<String, List<String>> map = connection.getHeaderFields();
			System.out.println("显示响应Header信息...\n");
			System.out.println(connection.getHeaderField("Set-Cookie"));
			for (Map.Entry<String, List<String>> entry : map.entrySet()) {
				System.out.println("Key : " + entry.getKey() + " ,Value : " + entry.getValue());
			}*/
			// if连接请求码成功
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				is = connection.getInputStream();
				byte[] bytes = new byte[1024];
				int i = 0;
				while ((i = is.read(bytes)) != -1) {
					sb.append(new String(bytes, 0, i, "utf-8"));
				}
				is.close();
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

	private static String zipInputStream(InputStream is) throws IOException {
		GZIPInputStream gzip = new GZIPInputStream(is);
		BufferedReader in = new BufferedReader(new InputStreamReader(gzip, "UTF-8"));
		StringBuffer buffer = new StringBuffer();
		String line;
		while ((line = in.readLine()) != null)
			buffer.append(line + "\n");
		is.close();
		return buffer.toString();
	}

	public static String httpPost(String url, String postData, Map<String, String> heads, String charsetName) {// Post请求的url，与get不同的是不需要带参数
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
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
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
			System.out.println(connection.getResponseCode());
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				is = connection.getInputStream();
				byte[] bytes = new byte[1024];
				int i = 0;
				while ((i = is.read(bytes)) != -1) {
					sb.append(new String(bytes, 0, i, charsetName));
				}
				is.close();
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
}
