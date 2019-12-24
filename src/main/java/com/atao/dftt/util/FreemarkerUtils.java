package com.atao.dftt.util;

import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.atao.util.StringUtils;

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
/*		dataMap.put("userImg", "http://pinduoduoimg.yangkeduo.com/avatar/default/9.png");
		List<String> pjImageUrls = new ArrayList<String>();
		pjImageUrls.add("https://t22img.yangkeduo.com/review3/review/2019-08-27/1fd6c38e1047e01652e3da472e1bc23f.jpeg");
		dataMap.put("pjImgs", pjImageUrls);
		dataMap.put("goodsImg", "https://t00img.yangkeduo.com/goods/images/2019-02-01/47a29f7c-b7ed-4f9c-a8f7-793c340837ca.jpg");
		dataMap.put("goodsName01", "商品名称商品名称商品名称商品名称商品名");//19
		//dataMap.put("goodsName02", "商品名称商品名称商品名称商品名称商品名");
		dataMap.put("spec01", "说明说明说明说明说明说明参数参");//15
		dataMap.put("spec02", "第二行的说明第二行的说明第二行明第二行");
		dataMap.put("orderAmount", 2.34f);
		dataMap.put("comment01", "评论评论评论评论评论评论评论论评论评论评评论评论");//24
		dataMap.put("comment02", "评论评论评论评论评论评论评论论评论评论评评论评");
		dataMap.put("comment03", "评论评论评论评论评论评论评论论评论评论评评论评");
*/
		dataMap.put("keyword", "秋冬季ins慵懒风加绒");
		Map<String, Object> product1 = new HashMap<String, Object>();
		product1.put("goodsName", "FSK秋冬季ins慵懒风加绒秋冬季ins慵懒风加绒秋冬季ins慵懒风加绒秋冬季ins慵懒风加绒秋冬季ins慵懒风加绒秋冬季ins慵懒风加绒秋冬季ins慵懒风加绒秋冬季ins慵懒风加绒秋冬季ins慵懒风加绒秋冬季ins慵懒风加绒秋冬季ins慵懒风加绒秋冬季ins慵懒风加绒");
		product1.put("goodsImg", "http://t00img.yangkeduo.com/goods/images/2019-10-08/65911bda-71e3-4348-b9e1-1b4fa1fea508.jpg");
		product1.put("mallName", "FSK旗舰店");
		product1.put("mallStyle", "1");
		product1.put("tag01", "新品");
		product1.put("tagColor01", "#E02E24");
		product1.put("tagurl01", "http://t13img.yangkeduo.com/cart/2019-09-05/1ed4bd1f2bea6bb38e1b8544c146efee.png");

		product1.put("tagBackgroudColor01", "#fceeed");
		product1.put("tag02", "退货包运费");
		product1.put("tagColor02", "#FF5706");
		product1.put("tagBackgroudColor02", "#fff1eb");
		//爱逛街 满31返2
		int width = 225;
		String prefixIcons01 = "https://t13img.yangkeduo.com/cart/2019-10-07/dab9d276c35adf6fd76cb94e4fc88238.png";
		String prefixIcons02 = "";
		String prefixIcons03 = "https://pinduoduoimg.yangkeduo.com/deposit/shipintag3.png";
		product1.put("prefixIcons01", prefixIcons01);
		product1.put("prefixIcons03", prefixIcons03);
		if(StringUtils.isNotBlank(prefixIcons01)) {
			if(prefixIcons01.contains("shipintag3")) width = width - 30;
			else {
				width = width - 65;
			}
		}
		if(StringUtils.isNotBlank(prefixIcons02)) {
			if(prefixIcons02.contains("shipintag3")) width = width - 30;
			else {
				width = width - 65;
			}
		}
		if(StringUtils.isNotBlank(prefixIcons03)) {
			if(prefixIcons03.contains("shipintag3")) width = width - 30;
			else {
				width = width - 65;
			}
		}
		product1.put("goodsNameWith", width+"px");
		product1.put("price", "19.9");
		product1.put("salesTip", "已拼1.7万件");
		dataMap.put("product1", product1);
		
		
		
		
		Map<String, Object> product2 = new HashMap<String, Object>();
		product2.put("goodsName", "FSK秋冬季ins慵懒风加绒秋冬季ins慵懒风加绒秋冬季ins慵懒风加绒秋冬季ins慵懒风加绒秋冬季ins慵懒风加绒秋冬季ins慵懒风加绒秋冬季ins慵懒风加绒秋冬季ins慵懒风加绒秋冬季ins慵懒风加绒秋冬季ins慵懒风加绒秋冬季ins慵懒风加绒秋冬季ins慵懒风加绒");
		product2.put("goodsImg", "http://t00img.yangkeduo.com/goods/images/2019-10-08/65911bda-71e3-4348-b9e1-1b4fa1fea508.jpg");
		product2.put("mallName", "FSK旗舰店");
		product2.put("mallStyle", "0");
		/*product2.put("tag01", "新品");
		product2.put("tagColor01", "#E02E24");*/
		product2.put("tagBackgroudColor01", "#fceeed");
		product2.put("tag02", "退货包运费");
		product2.put("tagColor02", "#FF5706");
		product2.put("tagBackgroudColor02", "#fff1eb");
		product2.put("tagurl02", "");

		product2.put("prefixIcons01", prefixIcons01);
		product2.put("prefixIcons03", prefixIcons03);
		product2.put("goodsNameWith", width+"px");
		product2.put("price", "19.9");
		product2.put("salesTip", "已拼1.7万件");
		
		
		dataMap.put("product2", product2);
		dataMap.put("product3", product1);
		dataMap.put("product4", product1);

		String html = loadTemplate("productlist.ftl", dataMap);
		System.out.println(html);
		FileUtils.htmlToImage(html, 505, "D:\\123456.png");
		//FileUtils.htmlToImage("D:\\work\\workspace\\dftt-service\\src\\main\\resources\\public\\pdd\\pj2.html", "D:\\1212.png");
	}
}
