package com.atao.dftt.util;

import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class FreemarkerUtils {
	public static String loadTemplate(String templateName, Map<String, Object> dataMap) {
		Configuration configuration = new Configuration();
		configuration.setDefaultEncoding("utf-8");
		configuration.setClassForTemplateLoading(FreemarkerUtils.class, "/templates");// 模版文件的存放路径
		StringWriter out = new StringWriter();
		try {
			// 读取模版文件
			Template t = configuration.getTemplate(templateName, "utf-8");
			t.process(dataMap, out);
			return out.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	public static void main(String[] args) throws MalformedURLException, InterruptedException {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("userImg", "http://pinduoduoimg.yangkeduo.com/avatar/default/9.png");
		List<String> pjImageUrls = new ArrayList<String>();
		pjImageUrls.add("https://t22img.yangkeduo.com/review3/review/2019-08-27/1fd6c38e1047e01652e3da472e1bc23f.jpeg");
		dataMap.put("pjImgs", pjImageUrls);
		dataMap.put("goodsImg", "https://t00img.yangkeduo.com/goods/images/2019-02-01/47a29f7c-b7ed-4f9c-a8f7-793c340837ca.jpg");
		String html = loadTemplate("pj.ftl", dataMap);
		//html = html.replace("\r\n", "").replace("\n", "").replace("\r", "");
		//System.out.println(html);
		FileUtils.htmlToImage(html, 900, "D:\\1234.png");
	}
}
