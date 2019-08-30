package com.atao.dftt.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import com.atao.util.StringUtils;

public class HttpPostUploadUtil {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String filepath = "C:\\Users\\Public\\Pictures\\Sample Pictures\\123.jpg";
		String urlStr = "http://file.yangkeduo.com/v3/store_image";
		Map<String, String> textMap = new HashMap<String, String>();
		//textMap.put("upload_sign", "4fd00a3ea40cf4459b936c40962202f7bf2fa00a");
		Map<String, String> fileMap = new HashMap<String, String>();
		//fileMap.put("userfile", filepath);
		String ret = formUpload(urlStr, textMap, fileMap);
		System.out.println(ret);
	}

	/**
	 * 上传图片
	 * 
	 * @param urlStr
	 * @param textMap
	 * @param fileMap
	 * @return
	 */
	public static String formUpload(String urlStr, Map<String, String> textMap, Map<String, String> fileMap) {
		String res = "";
		HttpURLConnection conn = null;
		String BOUNDARY = "ae014ab8-dcbe-40de-b2c3-9244be86f12c"; // boundary就是request头和上传文件内容的分隔符
		try {
			URL url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Accept-Encoding", "gzip");
			conn.setRequestProperty("User-Agent", "android Mozilla/5.0 (Linux; Android 7.1.2; MI 5X Build/N2G47H; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/64.0.3282.137 Mobile Safari/537.36  phh_android_version/4.33.0 phh_android_build/a2d7218bda21579f6f922dd29885b0d04b040338_pdd_patch phh_android_channel/xm");
			conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
			conn.setRequestProperty("Cookie", "api_uid=rBUGzVwUht4AJXpZhdyiAg==");
			conn.setRequestProperty("AccessToken", "5SP2FH37RBJ6XC2OVAPUDCJ3C3JB67L4TVRN7VQTQX4VBPY5YSKA103f010");
			conn.setRequestProperty("ETag", "b0LzKmIm");
			conn.setRequestProperty("lat", "TDDLQTU2MDK3WJ54Z5UTYICXCCBN374EJ6JF5M67PGTZ3CNTSZSQ103f010");
			conn.setRequestProperty("PDD-CONFIG", "00102");
			conn.setRequestProperty("Referer", "Android");
			conn.setRequestProperty("X-PDD-QUERIES", "width=1080&height=1920&net=1&brand=xiaomi&model=MI+5X&osv=7.1.2&appv=4.33.0&pl=2");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Host", "file.yangkeduo.com");
			conn.connect();
			byte[] fbytes = null;
			if (textMap != null) {
				StringBuffer strBuf = new StringBuffer();
				Iterator<Map.Entry<String, String>> iter = textMap.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry<String, String> entry = iter.next();
					String inputName = (String) entry.getKey();
					String inputValue = (String) entry.getValue();
					if (inputValue == null) {
						continue;
					}
					strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"\r\n\r\n");
					strBuf.append(inputValue);
				}
				//conn.getOutputStream().write(strBuf.toString().getBytes());// 输入参数
				fbytes = strBuf.toString().getBytes();
			}
			
			if (fileMap != null) {
				Iterator<Map.Entry<String, String>> iter = fileMap.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry<String, String> entry = iter.next();
					String inputName = (String) entry.getKey();
					String inputValue = (String) entry.getValue();
					if (inputValue == null) {
						continue;
					}
					File file = new File(inputValue);
					String filename = file.getName();

					StringBuffer strBuf = new StringBuffer();
					strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\"image\"; filename=\"1852788720_"+new Date().getTime()+".jpg"
							+ "\"\r\n");
					strBuf.append("Content-Type:" + "image/jpeg" + "\r\n\r\n");
					conn.getOutputStream().write(strBuf.toString().getBytes());// 输入参数
					DataInputStream in = new DataInputStream(new FileInputStream(file));
					
					int bytes = 0;
					byte[] bufferOut = new byte[1024];
					while ((bytes = in.read(bufferOut)) != -1) {
						conn.getOutputStream().write(bufferOut, 0, bytes);
					}
					in.close();
				}
			}
			byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
			conn.getOutputStream().write(endData);
			/*OutputStream out = new DataOutputStream(conn.getOutputStream());
			// text
			if (textMap != null) {
				StringBuffer strBuf = new StringBuffer();
				Iterator<Map.Entry<String, String>> iter = textMap.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry<String, String> entry = iter.next();
					String inputName = (String) entry.getKey();
					String inputValue = (String) entry.getValue();
					if (inputValue == null) {
						continue;
					}
					strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"\r\n\r\n");
					strBuf.append(inputValue);
				}
				out.write(strBuf.toString().getBytes());
			}

			if (fileMap != null) {
				Iterator<Map.Entry<String, String>> iter = fileMap.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry<String, String> entry = iter.next();
					String inputName = (String) entry.getKey();
					String inputValue = (String) entry.getValue();
					if (inputValue == null) {
						continue;
					}
					File file = new File(inputValue);
					String filename = file.getName();

					StringBuffer strBuf = new StringBuffer();
					strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\"image\"; filename=\"1852788720_"+new Date().getTime()+".jpg"
							+ "\"\r\n");
					strBuf.append("Content-Type:" + "image/jpeg" + "\r\n\r\n");
					out.write(strBuf.toString().getBytes());
					DataInputStream in = new DataInputStream(new FileInputStream(file));
					int bytes = 0;
					byte[] bufferOut = new byte[1024];
					while ((bytes = in.read(bufferOut)) != -1) {
						out.write(bufferOut, 0, bytes);
					}
					in.close();
				}
			}*/

			/*byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
			out.write(endData);
			out.flush();
			out.close();*/
			StringBuilder sb = new StringBuilder();
			System.out.println(conn.getResponseCode());
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				String encoding = conn.getContentEncoding();
				if ("gzip".equals(encoding)) {
					GZIPInputStream gZIPInputStream = new GZIPInputStream(conn.getInputStream());
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(gZIPInputStream));
					String line = null;
					while ((line = bufferedReader.readLine()) != null) { // 转化为UTF-8的编码格式
						line = new String(line.getBytes("UTF-8"));
						sb.append(line);
					}
					bufferedReader.close();
				} else {
					BufferedReader bufferedReader = new BufferedReader(
							new InputStreamReader(conn.getInputStream()));
					String line = null;
					while ((line = bufferedReader.readLine()) != null) { // 转化为UTF-8的编码格式
						line = new String(line.getBytes("UTF-8"));
						sb.append(line);
					}
					bufferedReader.close();
				}
			} else if (conn.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP) {
				sb.append(conn.getHeaderField("Location"));
			}
			return sb.toString();
		} catch (Exception e) {
			System.out.println("发送POST请求出错。" + urlStr);
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
		return res;
	}

}