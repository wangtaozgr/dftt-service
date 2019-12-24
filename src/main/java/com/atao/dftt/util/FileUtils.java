package com.atao.dftt.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

import javax.imageio.ImageIO;

import gui.ava.html.Html2Image;
import gui.ava.html.renderer.ImageRenderer;
import net.coobird.thumbnailator.Thumbnails;
import sun.misc.BASE64Encoder;

public class FileUtils {
	
	public static String getBase64Code(String imgUrl) throws IOException {
		URL url = new URL(imgUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// 设置超时间为3秒
		conn.setConnectTimeout(3 * 1000);
		// 防止屏蔽程序抓取而返回403错误
		conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
		// 得到输入流
		InputStream inputStream = conn.getInputStream();
		byte[] data = readInputStream(inputStream);
		// 对字节数组Base64编码
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(data);// 返回Base64编码过的字节数组字符串
	}
	
	/**
	 * 压缩后生成basecode
	 * @param imgUrl
	 * @return
	 * @throws IOException
	 */
	public static String getBase64CodeByCommpass(String imgUrl, int width, int height, float quality) throws IOException {
		try {
			URL url = new URL(imgUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// 设置超时间为3秒
			conn.setConnectTimeout(3 * 1000);
			// 防止屏蔽程序抓取而返回403错误
			conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
			// 得到输入流
			InputStream inputStream = conn.getInputStream();
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
			Thumbnails.of(inputStream).size(width, height).outputQuality(quality).outputFormat("jpg").toOutputStream(out);
			BASE64Encoder encoder = new BASE64Encoder();
			return encoder.encode(out.toByteArray());// 返回Base64编码过的字节数组字符串
		}catch (Exception e) {
			return getBase64Code(imgUrl);
		}
		
	}

	
	public static byte[] getByte(String imgUrl) throws IOException{
		URL url = new URL(imgUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// 设置超时间为3秒
		conn.setConnectTimeout(3 * 1000);
		// 防止屏蔽程序抓取而返回403错误
		conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
		// 得到输入流
		InputStream inputStream = conn.getInputStream();
		byte[] data = readInputStream(inputStream);
		return data;
	}
	
	public static byte[] getByte(InputStream inputStream) throws IOException{
		byte[] data = readInputStream(inputStream);
		return data;
	}
	
	public static InputStream getInputStream(String imgUrl) throws IOException{
		URL url = new URL(imgUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// 设置超时间为3秒
		conn.setConnectTimeout(3 * 1000);
		// 防止屏蔽程序抓取而返回403错误
		conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
		// 得到输入流
		InputStream inputStream = conn.getInputStream();
		return inputStream;
	}
	
	public static File downLoadFromInputStream(InputStream inputStream, String fileName, String savePath) throws IOException {
		byte[] getData = readInputStream(inputStream);
		// 文件保存位置
		File saveDir = new File(savePath);
		if (!saveDir.exists()) {
			saveDir.mkdir();
		}
		File file = new File(saveDir + File.separator + fileName);
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(getData);
		if (fos != null) {
			fos.close();
		}
		if (inputStream != null) {
			inputStream.close();
		}
		return file;
	}

	public static File downLoadFromUrl(String urlStr, String fileName, String savePath) throws IOException {
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// 设置超时间为3秒
		conn.setConnectTimeout(3 * 1000);
		// 防止屏蔽程序抓取而返回403错误
		conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
		// 得到输入流
		InputStream inputStream = conn.getInputStream();
		// 获取自己数组
		byte[] getData = readInputStream(inputStream);
		// 文件保存位置
		File saveDir = new File(savePath);
		if (!saveDir.exists()) {
			saveDir.mkdir();
		}
		File file = new File(saveDir + File.separator + fileName);
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(getData);
		if (fos != null) {
			fos.close();
		}
		if (inputStream != null) {
			inputStream.close();
		}
		return file;
	}

	/**
	 * 从输入流中获取字节数组
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static byte[] readInputStream(InputStream inputStream) throws IOException {
		byte[] buffer = new byte[1024];
		int len = 0;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		while ((len = inputStream.read(buffer)) != -1) {
			bos.write(buffer, 0, len);
		}
		bos.close();
		return bos.toByteArray();
	}

	/**
	 * 生成唯一文件名标识<br>
	 * rule:system time+rand(100)
	 * 
	 * @return
	 */
	public static String generateFileName(String prefix, String ext) {
		return prefix + System.currentTimeMillis() + new Random().nextInt(1000) + "." + ext;
	}
	
	/**
	 * html 转换成图片文件
	 * @param html
	 * @param savePath
	 * @throws MalformedURLException
	 * @throws InterruptedException 
	 */
	public static void htmlToImage(String html, int width, String savePath) {
		Html2Image tool = Html2Image.fromHtml(html);
		ImageRenderer imaeRender = tool.getImageRenderer();
		imaeRender.setWidth(width);
		imaeRender.saveImage(savePath);
	}
	
	public static void htmlToImage(String fileName, String savePath) {
		Html2Image tool = Html2Image.fromFile(new File(fileName));
		ImageRenderer imaeRender = tool.getImageRenderer();
		imaeRender.setWidth(505);
		imaeRender.setHeight(866);
		imaeRender.saveImage(savePath);
	}
	
	/**
	 * html 转换成图片字节
	 * @param html
	 * @return
	 * @throws MalformedURLException
	 * @throws InterruptedException 
	 */
	public static byte[] htmlToImageOut(String html, int width) throws MalformedURLException {
		Html2Image tool = Html2Image.fromFile(new File(html));
		ImageRenderer imaeRender = tool.getImageRenderer();
		imaeRender.setWidth(width);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		imaeRender.saveImage(outputStream, false);
		return outputStream.toByteArray();
	}
	
	public static void main(String[] args) throws InterruptedException, IOException {
		//htmlToImage("D:\\work\\workspace\\dftt-service\\src\\main\\resources\\public\\pdd\\pj.html", 768, "D:\\004.png");
		//htmlToImage("C:\\Users\\Administrator\\Desktop\\a.html",  "D:\\005.png");
		//htmlToImageOut("D:\\work\\webstormworkspace\\helloword\\pj.html");
		
		//htmlToImage("D:\\work\\webstormworkspace\\helloword\\productlist.html",  "D:\\001.png");
		//htmlToImage("D:\\work\\webstormworkspace\\helloword\\pj.html",  "D:\\002.png");
		//htmlToImage("D:\\work\\webstormworkspace\\helloword\\mypj.html",  "D:\\003.png");
		downLoadFromUrl("http://zk.gxrdwl.com/index.php/Main/TaskMem/QRcode?id=48151946334","111.jpg","D:\\pddImage");
		
		String file = "D:\\pddImage\\222.png";
        try {
            URL url = new URL("http://zk.gxrdwl.com/index.php/Main/TaskMem/QRcode?id=48151946334");
            BufferedImage img = ImageIO.read(url);
            ImageIO.write(img, "jpg", new File(file));
        } catch (Exception e) {
            e.printStackTrace();
        } 



	}
}
