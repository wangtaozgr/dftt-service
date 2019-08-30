package com.atao.dftt.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import com.atao.util.StringUtils;

public class TdHttpUrlConnectUtils {

	public static void printCookie(CookieStore cookieStore) {
		List<HttpCookie> listCookie = cookieStore.getCookies();
		listCookie.forEach(httpCookie -> {
			System.out.println("--------------------------------------");
			System.out.println("domain : " + httpCookie.getDomain());
			System.out.println("maxAge : " + httpCookie.getMaxAge());
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

	public static Map<String, String> httpGet(String url, Map<String, String> heads) {
		Map<String, String> result = new HashMap<String, String>();
		HttpURLConnection connection = null;
		InputStream is = null;
		StringBuilder sb = new StringBuilder();
		try {
			// 准备请求的网络地址
			URL urL = new URL(url);
			// 调用openConnection得到网络连接，网络连接处于就绪状态
			connection = (HttpURLConnection) urL.openConnection();
			// 设置网络连接超时时间5S
			connection.setConnectTimeout(30 * 1000);
			// 设置读取超时时间
			connection.setReadTimeout(30 * 1000);
			if (heads != null) {
				for (String key : heads.keySet()) {
					connection.setRequestProperty(key, heads.get(key));
				}
			}
			connection.connect();
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				String encoding = connection.getContentEncoding();
				if ("gzip".equals(encoding)) {
					GZIPInputStream gZIPInputStream = new GZIPInputStream(connection.getInputStream());
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(gZIPInputStream));
					String line = null;
					while ((line = bufferedReader.readLine()) != null) { // 转化为UTF-8的编码格式
						line = new String(line.getBytes("UTF-8"));
						sb.append(line);
					}
					bufferedReader.close();
				} else {
					BufferedReader bufferedReader = new BufferedReader(
							new InputStreamReader(connection.getInputStream()));
					String line = null;
					while ((line = bufferedReader.readLine()) != null) { // 转化为UTF-8的编码格式
						line = new String(line.getBytes("UTF-8"));
						sb.append(line);
					}
					bufferedReader.close();
				}
			}
			result.put("responseCode", connection.getResponseCode()+"");
			System.out.println(connection.getResponseCode());
			String cookie = "";
	        String key = null;  
	        for(int i = 1; (key = connection.getHeaderFieldKey(i)) != null; i++){  
	            if(key.equalsIgnoreCase("set-cookie")){  
	                String cookieVal = connection.getHeaderField(i);  
	                cookie += cookieVal.substring(0, cookieVal.indexOf(";")+1);  
	            } 
	        }
			if(StringUtils.isNotBlank(cookie)) {
				cookie = cookie.substring(0,cookie.length()-1);
				System.out.println(cookie);
				result.put("cookie", cookie);
			}
			result.put("content", sb.toString());
			return result;
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

	public static Map<String, String> httpPost(String url, String postData, Map<String, String> heads, String charsetName) {// Post请求的url，与get不同的是不需要带参数
		if (StringUtils.isBlank(charsetName))
			charsetName = "UTF-8";
		Map<String, String> result = new HashMap<String, String>();
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
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
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
						line = new String(line.getBytes("UTF-8"));
						sb.append(line);
					}
					bufferedReader.close();
				} else {
					BufferedReader bufferedReader = new BufferedReader(
							new InputStreamReader(connection.getInputStream()));
					String line = null;
					while ((line = bufferedReader.readLine()) != null) { // 转化为UTF-8的编码格式
						line = new String(line.getBytes("UTF-8"));
						sb.append(line);
					}
					bufferedReader.close();
				}
			}
			result.put("responseCode", connection.getResponseCode()+"");
			System.out.println(connection.getResponseCode());
			String cookie = "";
	        String key = null;  
	        for(int i = 1; (key = connection.getHeaderFieldKey(i)) != null; i++){  
	            if(key.equalsIgnoreCase("set-cookie")){  
	                String cookieVal = connection.getHeaderField(i);  
	                cookie += cookieVal.substring(0, cookieVal.indexOf(";")+1);  
	            } 
	        }
			if(StringUtils.isNotBlank(cookie)) {
				cookie = cookie.substring(0,cookie.length()-1);
				System.out.println(cookie);
				result.put("cookie", cookie);
			}
			result.put("content", sb.toString());
			return result;
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
