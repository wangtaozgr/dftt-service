package com.atao.dftt.http;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.atao.base.util.StringUtils;
import com.atao.dftt.api.vo.DataVo;
import com.atao.dftt.model.PddUser;
import com.atao.dftt.util.CommonUtils;
import com.atao.dftt.util.FileUtils;
import com.atao.dftt.util.FreemarkerUtils;
import com.atao.dftt.util.MobileHttpUrlConnectUtils;
import com.atao.util.DateUtils;

import ch.qos.logback.core.util.FileUtil;

public class PddHttp {
	private static Logger logger = LoggerFactory.getLogger(PddHttp.class);
	private static Map<String, PddHttp> userMap = new HashMap<String, PddHttp>();
	public PddUser user;
	public String anti_content = "0alAfandFyhYq9duvXi6MC9Z7uUPkCjk__lgNHfdJ_jX-jT2p_BSBiwdqOn12lMiu5zQ_2i07iQMq0mDk9i7AMAJ8QlQDJ59P0y2qxqw0EBP8mXG95BJlMhYTnI-urdWuirueate3z_NKI7Z3ddCLDMcMfYtj_DqCHb7i1VOJcidcrzzSHcIEm1Uj8UpIIFUfEZm5B5PWe0bCvHH-5yYBx39WEESSTgL1_vcks1igt-7wjb4GMiuUr1zm-358T53qpI3r-u5lgCxQXwPZb5UYJQLvokQLxcKEITbQeK8cS1oOOUnkNUugmCeXNGOp8m-njSKxJVtg99QGsdEpGbeDPKogcIF4ro0ZUxC-EKMxBWhI-02CZs446ox4Sxkop9t3O_U9-w4mUNs64WqIXbwawbUFP1TYarLSl6y-SizKOxqdybDsjOac0yrN6E3cdnIYzI_ogpWvY1awyZPLVqzOD0ypJrcfrajL7aQUoiT0t0VYlKVQXf6RhHe2NA7cq4C3ewrIhXJIf8ZXuH5rtuilwImIS6vD-dr65quLho-jxLdOAlDa_-VdFXlSpexXeVE9Tr0flqHrnz78kxukZnJuW0moWbhSN4EjbqBW4mbLoORjM9KZ5_ARhKa7NxqqQrj3QHy50y7PMqQB9rTf3b4DIftxnCqyK8lLnBIBuS1-Hx7_-Yxs_EFHUwpFCmhhuosxWaqNoFmzDTT8iQQCK-qdtIwhxE3pw-33_FyihVkIvHhrVgiaiMMRr4zt8VC9";
	// 多多进宝token
	public String ddjbToken = "727CRKX32I4IEJTRN2OCT47GBFHFQ45ZNKC6OP3WAEYPK4MWAXVA103f010";
	public PddHttp(PddUser user) {
		this.user = user;
	}

	public static synchronized PddHttp getInstance(PddUser user) {
		PddHttp http = (PddHttp) userMap.get(user.getUsername());
		if (http == null) {
			http = new PddHttp(user);
			userMap.put(user.getUsername(), http);
		}
		return http;
	}

	public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
		PddUser user = new PddUser();
		// user.setUid("1852788720");
		// user.setAddressId("4169994514");
		// user.setToken("M2TAWPOSR2ETG2IIK7YQUFEJTY6K6YQLU3RJ4KX4WAWAPUPAC62Q103f010");
		// user.setApiUid("rBUGzVwUht4AJXpZhdyiAg==");
		/*
		 * user.setUid("7887501021770"); user.setAddressId("4526882634");
		 * user.setToken("EL5PTDOEG4TQANDZ7YXEJXQFSLKECCHFPYTZXD5QXYLU4SYRTEWA1024a60");
		 * user.setApiUid("rBUGzVwUht4AJXpZhdyiAg==");
		 */
		user.setUsername("17755117870");
		user.setUid("1723813480085");
		user.setAddressId("4169994514");
		user.setToken("HFYZ4SBUH6HTDI5UC5KJKUM5HOVLVVHIBQMBYILCKZS3XSY5OAZQ113f010");
		// 34GNCXZLFY645W2FESF67UUUO7SZJPPPUPJVI6K3C2BLND5UAPPQ113f010
		// 34GNCXZLFY645W2FESF67UUUO7SZJPPPUPJVI6K3C2BLND5UAPPQ113f010

		// user.setToken("X6W7XXM5X77SH5CTPZYPRCPYTP55B624B3BJGY3LQ2K367UVLA2A100a459");
		user.setApiUid("rBUGzVwUht4AJXpZhdyiAg==");
		user.setUserAgent(
				"android Mozilla/5.0 (Linux; Android 7.1.2; MI 5X Build/N2G47H; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/64.0.3282.137 Mobile Safari/537.36  phh_android_version/4.33.0 phh_android_build/a2d7218bda21579f6f922dd29885b0d04b040338_pdd_patch phh_android_channel/xm");
		PddHttp http = PddHttp.getInstance(user);
		http.productDetail02("1029935892");
		//System.out.println(http.getShareUrl("2389768826"));
		// String pddImgPath = "D:/pddImage";
		//http.downloadAllCommentImage(pddImgPath);
		//https://api.pinduoduo.com/order/191003-670944789414080?pdduid=1852788720
		//https://mobile.yangkeduo.com/transac_ddgj_agreement.html?_t_timestamp=transac_order_comment&goods_id=33829331083&review_id=249457907694608523&hide_share_menu=1&refer_page_name=order_detail&refer_page_id=10038_1570530158364_hIB2LgJj5E&refer_page_sn=10038
		//191009-470549136492977
		//http.orderDetail02("191009-470549136492977");
		/*String goodsId = "8777464881";
		JSONObject productDetail = http.productDetail(goodsId);
		JSONObject goods = productDetail.getJSONObject("goods");
		String groupId = goods.getJSONArray("group").getJSONObject(1).getString("group_id");
		JSONArray skus = productDetail.getJSONArray("sku");
		
		JSONObject activity = productDetail.getJSONObject("activity_collection").getJSONObject("activity");
		//String activityId = activity.getString("activity_id");
		JSONObject sku = skus.getJSONObject(0);
		String skuId = sku.getString("sku_id");
		//http.orderCheckout(groupId, skuId, goodsId);
		
		JSONObject order = http.submitAppOrder("", groupId, skuId, goodsId, "", true);*/


		//JSONObject order = http.queryOrderDetail("191002-518060770234080");
		//System.out.println(order);
		//http.myComments();
		/*String goodsId = http.searchGoodsIdByMallId("422573401", http.getImageName("http://\\\\t00img.yangkeduo.com\\goods\\images\\2019-09-01\\1c71140e-f051-4d6a-ab99-c5b6a974f9b4.jpg?imageMogr2\\strip%7CimageView2\\2\\w\\1300\\q\\80"));
		System.out.println(goodsId);*/
	/*	String json = "{\"q_opt\":0,\"server_time\":1570090002,\"special_query\":{},\"is_repeated\":true,\"debugInfo\":{},\"dynamic_filter_bars\":{\"inside_filter\":[],\"outside_filter\":[],\"logo_filter\":[],\"sort\":[{\"name\":\"\",\"style\":2,\"dataReport\":{\"impr\":{\"parameter\":{\"type\":\"mult\"},\"page_el_sn\":\"470319\"}},\"type\":\"sort\",\"items\":[{\"name\":\"综合\",\"dataReport\":{\"click\":{\"parameter\":{\"type\":\"default\"},\"page_el_sn\":\"470319\"}},\"detail\":\"综合排序\",\"value\":\"default\",\"selected\":true},{\"name\":\"评分\",\"dataReport\":{\"click\":{\"parameter\":{\"type\":\"_credit\"},\"page_el_sn\":\"470319\"}},\"detail\":\"评分排序\",\"value\":\"_credit\"}]},{\"name\":\"销量\",\"style\":1,\"dataReport\":{\"click\":{\"parameter\":{\"type\":\"_sales\"},\"page_el_sn\":\"470319\"}},\"type\":\"sort\",\"value\":\"_sales\"},{\"name\":\"价格\",\"style\":3,\"type\":\"sort\",\"items\":[{\"name\":\"\",\"dataReport\":{\"click\":{\"parameter\":{\"type\":\"price\"},\"page_el_sn\":\"470319\"}},\"value\":\"price\"},{\"name\":\"\",\"dataReport\":{\"click\":{\"parameter\":{\"type\":\"_price\"},\"page_el_sn\":\"470319\"}},\"value\":\"_price\"}]},{\"style\":5,\"type\":\"split\"},{\"name\":\"筛选\",\"style\":4,\"dataReport\":{\"click\":{\"parameter\":{\"type\":\"filter_old\"},\"page_el_sn\":\"470418\"}},\"type\":\"filter_old\"}]},\"query_mode\":0,\"hide_sort_bar\":false,\"is_black\":false,\"pre_load\":{\"screen\":2.0},\"qc\":null,\"total\":963,\"search_mall\":{\"malls\":[]},\"server_time_ms\":1570090002568,\"rcmd_query\":null,\"exposure_ext_idx\":null,\"goods_style\":0,\"flip\":\"100;3;0;80;e273cb61-94ec-4a2c-9b5a-1f5749db6432\",\"qc2\":null,\"last_page\":false,\"search_ext\":{\"prefetch_goods_scene\":\"0\"},\"expansion\":[],\"filter\":{\"haitao\":\"off\",\"price\":[{\"start\":0,\"end\":9},{\"start\":9,\"end\":15},{\"start\":15,\"end\":20},{\"start\":20,\"end\":-1}],\"property_top\":[],\"property\":[{\"col_num\":\"4\",\"name\":\"精选服务\",\"id\":-1,\"items\":[{\"name\":\"退货包运费\",\"id\":3},{\"name\":\"只换不修\",\"id\":18}]},{\"col_num\":4,\"name\":\"适用品牌\",\"click_to_pick\":false,\"style\":2,\"id\":427,\"priority\":0,\"type\":\"property\",\"items\":[{\"name\":\"OPPO\",\"id\":95486},{\"name\":\"华为\",\"id\":95487},{\"name\":\"VIVO\",\"id\":95488},{\"name\":\"苹果\",\"id\":95489},{\"name\":\"360\",\"id\":95490},{\"name\":\"小米\",\"id\":95491},{\"name\":\"魅族\",\"id\":95492},{\"name\":\"荣耀\",\"id\":95493}]},{\"col_num\":4,\"name\":\"流行元素\",\"click_to_pick\":false,\"style\":2,\"id\":321,\"priority\":100,\"type\":\"property\",\"items\":[{\"name\":\"磨砂\",\"id\":95508},{\"name\":\"文字\",\"id\":95509},{\"name\":\"超薄\",\"id\":95510},{\"name\":\"浮雕\",\"id\":95511},{\"name\":\"定制\",\"id\":95512},{\"name\":\"透明\",\"id\":95513},{\"name\":\"镶钻\",\"id\":95514},{\"name\":\"流沙\",\"id\":95515}]},{\"col_num\":4,\"name\":\"质地\",\"click_to_pick\":false,\"style\":2,\"id\":426,\"priority\":200,\"type\":\"property\",\"items\":[{\"name\":\"软胶\",\"id\":95519},{\"name\":\"钢化玻璃\",\"id\":95520},{\"name\":\"TPU\",\"id\":95521},{\"name\":\"塑料\",\"id\":95522},{\"name\":\"金属\",\"id\":95523},{\"name\":\"仿皮\",\"id\":95524},{\"name\":\"亚克力\",\"id\":95525},{\"name\":\"木\",\"id\":95526}]},{\"col_num\":4,\"name\":\"款式\",\"click_to_pick\":false,\"style\":2,\"id\":324,\"priority\":300,\"type\":\"property\",\"items\":[{\"name\":\"保护壳\",\"id\":95539},{\"name\":\"全包式\",\"id\":95540},{\"name\":\"边框式\",\"id\":95541},{\"name\":\"翻盖式\",\"id\":95542},{\"name\":\"钱包式\",\"id\":95543},{\"name\":\"臂带式\",\"id\":95544},{\"name\":\"防水袋\",\"id\":95545}]}],\"flagship\":\"off\",\"property_rank\":[],\"favmall\":\"on\",\"property_tag\":[],\"group\":\"off\",\"promotion\":{\"is_selected\":false,\"is_on\":true,\"text\":{\"unselected\":\"点击优先展示4周年庆活动商品\",\"bold\":\"\",\"selected\":\"已为您优先展示4周年庆活动商品\"},\"id\":4,\"status\":\"off\"}},\"act_entry\":null,\"ads\":{\"malls\":[]},\"rcmd_query_list\":[],\"rebuy_goods\":{},\"expansion_list\":[],\"p_search\":{\"list_id\":\"PWyuumpipu\",\"is_sink\":false,\"is_no_result\":false,\"is_prop_filter\":1,\"cat_id\":3045,\"is_third_search\":false,\"page\":5,\"rn\":\"e273cb61-94ec-4a2c-9b5a-1f5749db6432\",\"request_id\":\"2302e0f1-ee57-4f41-ae01-9f98ce84de11\",\"exp_info\":{\"bucket\":\"ly0000_12,ly0001_1,ly0002_58,ly0003_48,ly0004_97,ly0005_1,ly0006_26,ly0007_55,ly0008_4,ly0009_32,ly0010_1,ly0011_56,ly0012_13,ly0013_21,ly0014_9,ly0015_74,ly0016_67,ly0017_64,ly0018_5,ly0019_64,ly0020_94\",\"bucketMap\":{\"ly0020\":94,\"ly0002\":58,\"ly0013\":21,\"ly0001\":1,\"ly0012\":13,\"ly0000\":12,\"ly0011\":56,\"ly0010\":1,\"ly0006\":26,\"ly0017\":64,\"ly0005\":1,\"ly0016\":67,\"ly0004\":97,\"ly0015\":74,\"ly0003\":48,\"ly0014\":9,\"ly0009\":32,\"ly0008\":4,\"ly0019\":64,\"ly0007\":55,\"ly0018\":5},\"exp\":\"sh5\"}},\"style\":0,\"need_ad_logo\":false,\"qc_level\":0,\"rn\":\"e273cb61-94ec-4a2c-9b5a-1f5749db6432\",\"fallback\":false,\"items\":[{\"thumb_url\":\"http://t00img.yangkeduo.com/goods/images/2019-06-02/821282ebdbd3469a03b96fcced310833.jpeg\",\"country\":\"\",\"long_thumb_url\":\"\",\"hd_thumb_url\":\"http://t00img.yangkeduo.com/goods/images/2019-06-02/aa691f5d6c469ffd6d9f4d1b4fe34ee3.jpeg\",\"customer_num\":2,\"hd_url\":\"https://t00img.yangkeduo.com/goods/images/2019-06-02/61b14c07-75d3-4ed9-bb64-8899cfbb735c.jpg\",\"tag_text\":{\"channel\":null,\"promotion\":null},\"tag_ext\":{\"source\":1},\"sales\":5,\"event_type\":0,\"price\":990,\"tag_list\":[{\"text\":\"极速退款\",\"text_color\":\"#FF5706\",\"type\":12}],\"activity_type\":0,\"link_url\":\"goods.html?goods_id=10786057309\",\"tag\":-1,\"goods_name\":\"oppoR9s手机壳R9splus液态硅胶r9tm保护套r9sk防摔r9全包r9plus软\",\"hd_thumb_wm\":\"\",\"is_app\":0,\"sales_tip\":\"已拼5件\",\"coupon\":0,\"image_url\":\"http://t00img.yangkeduo.com/openapi/images/2019-06-01/965ccbd47f63cff529a6cbe1c933d992.jpeg\",\"goods_id\":10786057309,\"long_thumb_wm\":\"\",\"prop_tag_list\":[],\"list_type\":0,\"normal_price\":1090,\"icon_list\":[{\"width\":45,\"id\":10000001,\"url\":\"https://pinduoduoimg.yangkeduo.com/deposit/shipintag3.png\",\"height\":45}],\"market_price\":3800,\"image_wm\":\"\",\"p_search\":{\"gin_cat_id_1\":2933,\"gin_cat_id_3\":3045,\"quick_refund_tag_type\":1,\"list_id\":\"PWyuumpipu\",\"spacex\":{\"ctr\":0.001957317814230919,\"expand_beta\":109871.0,\"take_rate\":0.0,\"price\":0.0,\"alpha\":null,\"delta_score\":-1.0,\"tag\":0.0,\"type\":null,\"version\":0.0,\"beta\":null},\"is_third_search\":false,\"is_sink\":false,\"price\":990,\"icon_list\":[10000001],\"cat_id\":3045,\"has_tag\":1,\"page\":5,\"rn\":\"e273cb61-94ec-4a2c-9b5a-1f5749db6432\",\"request_id\":\"2302e0f1-ee57-4f41-ae01-9f98ce84de11\",\"exp_info\":{\"bucket\":\"ly0000_12,ly0001_1,ly0002_58,ly0003_48,ly0004_97,ly0005_1,ly0006_26,ly0007_55,ly0008_4,ly0009_32,ly0010_1,ly0011_56,ly0012_13,ly0013_21,ly0014_9,ly0015_74,ly0016_67,ly0017_64,ly0018_5,ly0019_64,ly0020_94\",\"bucketMap\":{\"ly0020\":94,\"ly0002\":58,\"ly0013\":21,\"ly0001\":1,\"ly0012\":13,\"ly0000\":12,\"ly0011\":56,\"ly0010\":1,\"ly0006\":26,\"ly0017\":64,\"ly0005\":1,\"ly0016\":67,\"ly0004\":97,\"ly0015\":74,\"ly0003\":48,\"ly0014\":9,\"ly0009\":32,\"ly0008\":4,\"ly0019\":64,\"ly0007\":55,\"ly0018\":5},\"exp\":\"sh5\"},\"is_clk_req\":false},\"short_name\":\"oppoR9s手机壳R9splus液态硅胶r9tm保护套r9sk防摔r9全包r9plus软\",\"thumb_wm\":\"\"},{\"thumb_url\":\"http://t00img.yangkeduo.com/goods/images/2019-07-09/f89a069da98ab48817e8e705ec0c3611.jpeg\",\"country\":\"\",\"long_thumb_url\":\"\",\"hd_thumb_url\":\"http://t00img.yangkeduo.com/goods/images/2019-07-09/0c03cb57a91ea68a5a2a6aefc5591a29.jpeg\",\"customer_num\":2,\"hd_url\":\"https://t00img.yangkeduo.com/goods/images/2019-07-09/aed58b9d-0165-4b6b-9661-44cff23c4d46.jpg\",\"tag_text\":{\"channel\":null,\"promotion\":null},\"tag_ext\":{\"source\":1},\"sales\":24,\"event_type\":0,\"price\":880,\"tag_list\":[],\"activity_type\":0,\"link_url\":\"goods.html?goods_id=22893178764\",\"tag\":-1,\"goods_name\":\"ins液态纯色oppor17手机壳r15硅胶r11s防摔r15x软壳r9splus男女k1\",\"hd_thumb_wm\":\"\",\"is_app\":0,\"sales_tip\":\"已拼24件\",\"coupon\":0,\"image_url\":\"http://omsproductionimg.yangkeduo.com/images/\",\"goods_id\":22893178764,\"long_thumb_wm\":\"\",\"prop_tag_list\":[],\"list_type\":0,\"normal_price\":980,\"icon_list\":[],\"market_price\":1880,\"image_wm\":\"\",\"p_search\":{\"gin_cat_id_1\":2933,\"gin_cat_id_3\":3045,\"list_id\":\"PWyuumpipu\",\"spacex\":{\"ctr\":0.002197271678596735,\"expand_beta\":109871.0,\"take_rate\":0.0,\"price\":0.0,\"alpha\":null,\"delta_score\":-1.0,\"tag\":0.0,\"type\":null,\"version\":0.0,\"beta\":null},\"is_third_search\":false,\"is_sink\":false,\"price\":880,\"cat_id\":3045,\"has_tag\":0,\"page\":5,\"rn\":\"e273cb61-94ec-4a2c-9b5a-1f5749db6432\",\"request_id\":\"2302e0f1-ee57-4f41-ae01-9f98ce84de11\",\"exp_info\":{\"bucket\":\"ly0000_12,ly0001_1,ly0002_58,ly0003_48,ly0004_97,ly0005_1,ly0006_26,ly0007_55,ly0008_4,ly0009_32,ly0010_1,ly0011_56,ly0012_13,ly0013_21,ly0014_9,ly0015_74,ly0016_67,ly0017_64,ly0018_5,ly0019_64,ly0020_94\",\"bucketMap\":{\"ly0020\":94,\"ly0002\":58,\"ly0013\":21,\"ly0001\":1,\"ly0012\":13,\"ly0000\":12,\"ly0011\":56,\"ly0010\":1,\"ly0006\":26,\"ly0017\":64,\"ly0005\":1,\"ly0016\":67,\"ly0004\":97,\"ly0015\":74,\"ly0003\":48,\"ly0014\":9,\"ly0009\":32,\"ly0008\":4,\"ly0019\":64,\"ly0007\":55,\"ly0018\":5},\"exp\":\"sh5\"},\"is_clk_req\":false},\"short_name\":\"ins液态纯色oppor17手机壳r15硅胶r11s防摔r15x软壳r9splus男女k1\",\"thumb_wm\":\"\"},{\"thumb_url\":\"http://t00img.yangkeduo.com/goods/images/2019-05-26/4c3b57cd4c100029457c680691b25f35.jpeg\",\"country\":\"\",\"long_thumb_url\":\"\",\"hd_thumb_url\":\"http://t00img.yangkeduo.com/goods/images/2019-05-26/396b5b937821d9ef8698c2db85e6cea5.jpeg\",\"customer_num\":2,\"hd_url\":\"https://t00img.yangkeduo.com/goods/images/2019-05-26/c2c4f980-9947-4217-ad80-60fc003fd1ea.png\",\"tag_text\":{\"channel\":null,\"promotion\":null},\"tag_ext\":{\"source\":1},\"sales\":323,\"event_type\":0,\"price\":1680,\"tag_list\":[{\"text\":\"极速退款\",\"text_color\":\"#FF5706\",\"type\":12}],\"activity_type\":0,\"link_url\":\"goods.html?goods_id=1250189902\",\"mall_name\":\"浪鸟3C数码旗舰店\",\"tag\":-1,\"goods_name\":\"oppor9手机壳液态硅胶全包防摔壳r9splus保护套R9m网红个性男女款\",\"hd_thumb_wm\":\"\",\"is_app\":0,\"sales_tip\":\"已拼323件\",\"coupon\":0,\"image_url\":\"http://omsproductionimg.yangkeduo.com/images/\",\"goods_id\":1250189902,\"long_thumb_wm\":\"\",\"prop_tag_list\":[],\"list_type\":0,\"normal_price\":1980,\"icon_list\":[],\"market_price\":2690,\"image_wm\":\"\",\"p_search\":{\"gin_cat_id_1\":2933,\"gin_cat_id_3\":3045,\"quick_refund_tag_type\":1,\"list_id\":\"PWyuumpipu\",\"spacex\":{\"ctr\":0.0027253474108874798,\"expand_beta\":109871.0,\"take_rate\":0.0,\"price\":0.0,\"alpha\":null,\"delta_score\":-1.0,\"tag\":0.0,\"type\":null,\"version\":0.0,\"beta\":null},\"is_third_search\":false,\"is_sink\":false,\"price\":1680,\"cat_id\":3045,\"has_tag\":1,\"page\":5,\"rn\":\"e273cb61-94ec-4a2c-9b5a-1f5749db6432\",\"request_id\":\"2302e0f1-ee57-4f41-ae01-9f98ce84de11\",\"exp_info\":{\"bucket\":\"ly0000_12,ly0001_1,ly0002_58,ly0003_48,ly0004_97,ly0005_1,ly0006_26,ly0007_55,ly0008_4,ly0009_32,ly0010_1,ly0011_56,ly0012_13,ly0013_21,ly0014_9,ly0015_74,ly0016_67,ly0017_64,ly0018_5,ly0019_64,ly0020_94\",\"bucketMap\":{\"ly0020\":94,\"ly0002\":58,\"ly0013\":21,\"ly0001\":1,\"ly0012\":13,\"ly0000\":12,\"ly0011\":56,\"ly0010\":1,\"ly0006\":26,\"ly0017\":64,\"ly0005\":1,\"ly0016\":67,\"ly0004\":97,\"ly0015\":74,\"ly0003\":48,\"ly0014\":9,\"ly0009\":32,\"ly0008\":4,\"ly0019\":64,\"ly0007\":55,\"ly0018\":5},\"exp\":\"sh5\"},\"is_clk_req\":false},\"short_name\":\"oppor9手机壳液态硅胶全包防摔壳r9splus保护套R9m网红个性男女款\",\"thumb_wm\":\"\",\"mall_style\":1},{\"thumb_url\":\"http://t00img.yangkeduo.com/goods/images/2019-03-27/6b28cdad2b30df6d8c78b8630fd57a3c.jpeg\",\"country\":\"\",\"long_thumb_url\":\"\",\"hd_thumb_url\":\"http://t00img.yangkeduo.com/goods/images/2019-03-27/7341dec713e8809ddfbc8bc6e401e170.jpeg\",\"customer_num\":2,\"hd_url\":\"https://t00img.yangkeduo.com/goods/images/2019-03-27/b05954dc-51af-4667-967e-d3a57c1832bc.jpg\",\"tag_text\":{\"channel\":null,\"promotion\":\"满14返1\"},\"tag_ext\":{\"source\":1},\"sales\":253,\"event_type\":0,\"price\":880,\"tag_list\":[{\"text\":\"极速退款\",\"text_color\":\"#FF5706\",\"type\":12},{\"text\":\"满14返1\",\"text_color\":\"#FF5706\",\"type\":2}],\"activity_type\":0,\"link_url\":\"goods.html?goods_id=6926496481\",\"tag\":-1,\"goods_name\":\"卡通熊液态硅胶oppor9s/r17手机壳r15/r9plus挂绳a83A73/A79全包\",\"hd_thumb_wm\":\"\",\"is_app\":0,\"sales_tip\":\"已拼253件\",\"coupon\":0,\"image_url\":\"http://omsproductionimg.yangkeduo.com/images/\",\"goods_id\":6926496481,\"long_thumb_wm\":\"\",\"prop_tag_list\":[],\"list_type\":0,\"normal_price\":980,\"icon_list\":[],\"market_price\":1630,\"image_wm\":\"\",\"p_search\":{\"gin_cat_id_1\":2933,\"promotion_type\":3,\"gin_cat_id_3\":3045,\"quick_refund_tag_type\":1,\"list_id\":\"PWyuumpipu\",\"spacex\":{\"ctr\":0.007437799125909805,\"expand_beta\":109871.0,\"take_rate\":0.0,\"price\":0.0,\"alpha\":null,\"delta_score\":-1.0,\"tag\":0.0,\"type\":null,\"version\":0.0,\"beta\":null},\"is_third_search\":false,\"is_sink\":false,\"price\":880,\"cat_id\":3045,\"has_tag\":1,\"page\":5,\"rn\":\"e273cb61-94ec-4a2c-9b5a-1f5749db6432\",\"request_id\":\"2302e0f1-ee57-4f41-ae01-9f98ce84de11\",\"exp_info\":{\"bucket\":\"ly0000_12,ly0001_1,ly0002_58,ly0003_48,ly0004_97,ly0005_1,ly0006_26,ly0007_55,ly0008_4,ly0009_32,ly0010_1,ly0011_56,ly0012_13,ly0013_21,ly0014_9,ly0015_74,ly0016_67,ly0017_64,ly0018_5,ly0019_64,ly0020_94\",\"bucketMap\":{\"ly0020\":94,\"ly0002\":58,\"ly0013\":21,\"ly0001\":1,\"ly0012\":13,\"ly0000\":12,\"ly0011\":56,\"ly0010\":1,\"ly0006\":26,\"ly0017\":64,\"ly0005\":1,\"ly0016\":67,\"ly0004\":97,\"ly0015\":74,\"ly0003\":48,\"ly0014\":9,\"ly0009\":32,\"ly0008\":4,\"ly0019\":64,\"ly0007\":55,\"ly0018\":5},\"exp\":\"sh5\"},\"is_clk_req\":false},\"short_name\":\"卡通熊液态硅胶oppor9s/r17手机壳r15/r9plus挂绳a83A73/A79全包\",\"thumb_wm\":\"\"},{\"thumb_url\":\"https://t00img.yangkeduo.com/goods/images/2019-06-21/934a5955-2010-4fc6-ba0a-20e611b4fbba.jpg\",\"country\":\"\",\"long_thumb_url\":\"\",\"hd_thumb_url\":\"https://t00img.yangkeduo.com/goods/images/2019-06-21/934a5955-2010-4fc6-ba0a-20e611b4fbba.jpg\",\"customer_num\":2,\"hd_url\":\"https://t00img.yangkeduo.com/goods/images/2019-06-21/934a5955-2010-4fc6-ba0a-20e611b4fbba.jpg\",\"tag_text\":{\"channel\":null,\"promotion\":\"满15返1\"},\"tag_ext\":{\"source\":1},\"sales\":28000,\"event_type\":0,\"price\":790,\"tag_list\":[{\"text\":\"极速退款\",\"text_color\":\"#FF5706\",\"type\":12},{\"text\":\"满15返1\",\"text_color\":\"#FF5706\",\"type\":2}],\"activity_type\":0,\"link_url\":\"goods.html?goods_id=7139204008&gallery_id=111558617269\",\"mall_name\":\"麦冠数码旗舰店\",\"tag\":-1,\"goods_name\":\"oppor17手机壳a9/a5/a59/a7x/a57液态r9s/r15x/r11全包Reno软壳a7\",\"hd_thumb_wm\":\"\",\"is_app\":0,\"sales_tip\":\"已拼2.8万件\",\"ad\":{\"start_position\":1,\"set\":\"sh5\",\"query\":\"oppor9splus手机壳液态硅胶\",\"goods_id\":7139204008,\"exp_list\":[{\"bucket\":\"7\",\"exp\":\"cacheABLayerSearch\",\"strategy\":\"NotCache\"},{\"bucket\":\"0\",\"exp\":\"starkSearchMallAdLayer\",\"strategy\":\"searchBase\"},{\"bucket\":\"15\",\"exp\":\"algoLayerRankCreative\",\"strategy\":\"rankCreativeBaseAA\"},{\"bucket\":\"79\",\"exp\":\"freshUser\",\"strategy\":\"base\"},{\"bucket\":\"2\",\"exp\":\"fullAlgoLayerCostControl\",\"strategy\":\"base\"},{\"bucket\":\"0\",\"exp\":\"starkSearchGoodsProsLayer\",\"strategy\":\"searchBase\"},{\"bucket\":\"1\",\"exp\":\"searchSameStylePicLayer\",\"strategy\":\"AdSameStypePicV2\"},{\"bucket\":\"1\",\"exp\":\"algoLayerStarkTarget\",\"strategy\":\"targetBase\"},{\"bucket\":\"1\",\"exp\":\"searchExtendAdNumLayer\",\"strategy\":\"addAdNum\"},{\"bucket\":\"1\",\"exp\":\"starkSearchLayer\",\"strategy\":\"AdSameStypePic\"},{\"bucket\":\"38\",\"exp\":\"algoLayerStarkSearch\",\"strategy\":\"q2aEcpmCutV1_200\"},{\"bucket\":\"18\",\"exp\":\"algoLayerStarkUnified\",\"strategy\":\"UnifiedBase\"},{\"bucket\":\"8\",\"exp\":\"admixPhysicEngineLayer\",\"strategy\":\"StarkVipSearchEngine\"}],\"creative_id\":87509534,\"target\":0,\"search_id\":\"_dlGWKebPyGEDTuglG3lYstKuKFHTDbx6sCi4mlCmfXM2HUqdDweJLN5Y88KjheV\",\"trigger_goods_id\":0,\"product_type\":1,\"sub_target\":\"0\",\"mall_id\":429995173,\"match_type\":3,\"scenes_type\":0,\"bid\":\"fgR3uLgiCPxSu08PLNXSAZ-WBQ4a489i9p2Dd7-DB9q-JXGtut32btG820liFzYGeuO1QqB3vlwjc5HDrEWIW4UWDhA1wLxsAvr2oZ5W1AD7-BhgZ1torSD6vjd-6wzasF_Rkh6mxh9RM9D_4fEtCSYAOGqkqKos0ifrhx_lj5qFCi0LzAVw_2b4ymHS5MBKAyFbJoTesRDnegxLJRNS0UuRsUhDPSUzibVGLGv1b6Enh9GevXb-GgSKqfS9FcKjux6sJH3N6DmaqTDu6vOQrn_YeogTAC_b93XxGYXo4KNQpVCbWVvkNF2BDC8EmibdnlWWIBZP3joSxx4_2YZyA_nIzmx7UruES1t7QYj-sc_I289mfV4U5PdBFZTlw2R-mE4lbQJozYnmn3Pfz7nGizvnqsEv_SHKBnVJYy2gAujAvP2EpdUkha2YqoNBjm1fXpCXe16ZjNbXXoyhqZk_2Z9N8ZzrXKGCnkXYdfMkyWUv6_REehJNrA2eWkPlh_Nufq8z-WY3IAESQDelGBm13YjfaNF-G9ToxOlSfzaNHx9MYdGuJNr-ruBscEXA7wSsuFh9kRc6o_3BP8bHkTWvsJrnP6b3gRi2jI1rsxF5_420vQAVlRCnmRwrK5fJbJmiAYfTDxu9qGT_vzfSQ9W-VkkcuN-TbWjajgf50MgJi_g=\",\"goods_id_list\":[],\"ispr\":null},\"coupon\":0,\"image_url\":\"https://t00img.yangkeduo.com/goods/images/2019-06-21/934a5955-2010-4fc6-ba0a-20e611b4fbba.jpg\",\"goods_id\":7139204008,\"long_thumb_wm\":\"\",\"prop_tag_list\":[],\"list_type\":0,\"normal_price\":1090,\"icon_list\":[{\"width\":135,\"id\":4,\"url\":\"http://t13img.yangkeduo.com/cart/2019-09-25/10cc9b58c977f700c82449f71ebe65cc.png\",\"height\":45},{\"width\":45,\"id\":10000001,\"url\":\"https://pinduoduoimg.yangkeduo.com/deposit/shipintag3.png\",\"height\":45}],\"market_price\":1880,\"image_wm\":\"\",\"p_search\":{\"gin_cat_id_1\":2933,\"promotion_type\":3,\"gin_cat_id_3\":3045,\"quick_refund_tag_type\":1,\"list_id\":\"PWyuumpipu\",\"is_third_search\":false,\"creative\":{\"gallery_id\":111558617269,\"id\":87509534},\"is_sink\":false,\"price\":790,\"icon_list\":[4,10000001],\"cat_id\":3045,\"has_tag\":1,\"page\":5,\"rn\":\"e273cb61-94ec-4a2c-9b5a-1f5749db6432\",\"request_id\":\"2302e0f1-ee57-4f41-ae01-9f98ce84de11\",\"exp_info\":{\"bucket\":\"ly0000_12,ly0001_1,ly0002_58,ly0003_48,ly0004_97,ly0005_1,ly0006_26,ly0007_55,ly0008_4,ly0009_32,ly0010_1,ly0011_56,ly0012_13,ly0013_21,ly0014_9,ly0015_74,ly0016_67,ly0017_64,ly0018_5,ly0019_64,ly0020_94\",\"bucketMap\":{\"ly0020\":94,\"ly0002\":58,\"ly0013\":21,\"ly0001\":1,\"ly0012\":13,\"ly0000\":12,\"ly0011\":56,\"ly0010\":1,\"ly0006\":26,\"ly0017\":64,\"ly0005\":1,\"ly0016\":67,\"ly0004\":97,\"ly0015\":74,\"ly0003\":48,\"ly0014\":9,\"ly0009\":32,\"ly0008\":4,\"ly0019\":64,\"ly0007\":55,\"ly0018\":5},\"exp\":\"sh5\"},\"is_clk_req\":false},\"short_name\":\"oppor17手机壳a9/a5/a59/a7x/a57液态r9s/r15x/r11全包Reno软壳a7\",\"thumb_wm\":\"\",\"mall_style\":1,\"creative_ad_info\":{\"image_url\":\"https://t00img.yangkeduo.com/goods/images/2019-06-21/934a5955-2010-4fc6-ba0a-20e611b4fbba.jpg\",\"image_id\":111558617269}},{\"thumb_url\":\"http://t00img.yangkeduo.com/goods/images/2019-07-09/b0aead783d188d942ac736ac486242a2.jpeg\",\"country\":\"\",\"long_thumb_url\":\"\",\"hd_thumb_url\":\"http://t00img.yangkeduo.com/goods/images/2019-07-09/30a38370c05b1fe4b04f39b41159a864.jpeg\",\"customer_num\":2,\"hd_url\":\"https://t00img.yangkeduo.com/goods/images/2019-01-15/eee90da6-b4c0-4d16-b26c-060711da4f4a.jpg\",\"tag_text\":{\"channel\":null,\"promotion\":\"满20返3\"},\"tag_ext\":{\"source\":1},\"sales\":1,\"event_type\":0,\"price\":990,\"tag_list\":[{\"text\":\"极速退款\",\"text_color\":\"#FF5706\",\"type\":12},{\"text\":\"满20返3\",\"text_color\":\"#FF5706\",\"type\":2}],\"activity_type\":0,\"link_url\":\"goods.html?goods_id=22720348226\",\"tag\":-1,\"goods_name\":\"OPPOR17手机壳女R15/R11S/R11/R9S/R9PLUS液态流沙A7X/A5/A3硅胶【预售：10月5日发完】\",\"hd_thumb_wm\":\"\",\"is_app\":0,\"sales_tip\":\"已拼1件\",\"coupon\":0,\"image_url\":\"http://omsproductionimg.yangkeduo.com/images/\",\"goods_id\":22720348226,\"long_thumb_wm\":\"\",\"prop_tag_list\":[],\"list_type\":0,\"normal_price\":1090,\"icon_list\":[],\"market_price\":2680,\"image_wm\":\"\",\"p_search\":{\"gin_cat_id_1\":2933,\"promotion_type\":3,\"gin_cat_id_3\":3045,\"quick_refund_tag_type\":1,\"list_id\":\"PWyuumpipu\",\"spacex\":{\"ctr\":0.004519878886640072,\"expand_beta\":109871.0,\"take_rate\":0.0,\"price\":0.0,\"alpha\":null,\"delta_score\":-1.0,\"tag\":0.0,\"type\":null,\"version\":0.0,\"beta\":null},\"is_third_search\":false,\"is_sink\":false,\"price\":990,\"cat_id\":3045,\"has_tag\":1,\"page\":5,\"rn\":\"e273cb61-94ec-4a2c-9b5a-1f5749db6432\",\"request_id\":\"2302e0f1-ee57-4f41-ae01-9f98ce84de11\",\"exp_info\":{\"bucket\":\"ly0000_12,ly0001_1,ly0002_58,ly0003_48,ly0004_97,ly0005_1,ly0006_26,ly0007_55,ly0008_4,ly0009_32,ly0010_1,ly0011_56,ly0012_13,ly0013_21,ly0014_9,ly0015_74,ly0016_67,ly0017_64,ly0018_5,ly0019_64,ly0020_94\",\"bucketMap\":{\"ly0020\":94,\"ly0002\":58,\"ly0013\":21,\"ly0001\":1,\"ly0012\":13,\"ly0000\":12,\"ly0011\":56,\"ly0010\":1,\"ly0006\":26,\"ly0017\":64,\"ly0005\":1,\"ly0016\":67,\"ly0004\":97,\"ly0015\":74,\"ly0003\":48,\"ly0014\":9,\"ly0009\":32,\"ly0008\":4,\"ly0019\":64,\"ly0007\":55,\"ly0018\":5},\"exp\":\"sh5\"},\"is_clk_req\":false},\"short_name\":\"OPPOR17手机壳女R15/R11S/R11/R9S/R9PLUS液态流沙A7X/A5/A3硅胶\",\"thumb_wm\":\"\"},{\"thumb_url\":\"http://t00img.yangkeduo.com/goods/images/2019-08-16/a115080fd35cbd7259b66726ae832e86.jpeg\",\"country\":\"\",\"long_thumb_url\":\"\",\"hd_thumb_url\":\"http://t00img.yangkeduo.com/goods/images/2019-08-16/42cdce127a4beb86f27cbbdb5c39409e.jpeg\",\"customer_num\":2,\"hd_url\":\"http://t00img.yangkeduo.com/openapi/images/2019-08-16/348f88c46fec2bea185257d1a2f7f2b0.jpeg\",\"tag_text\":{\"channel\":null,\"promotion\":\"满13返1\"},\"tag_ext\":{\"source\":1},\"sales\":0,\"event_type\":0,\"price\":930,\"tag_list\":[{\"text\":\"极速退款\",\"text_color\":\"#FF5706\",\"type\":12},{\"text\":\"满13返1\",\"text_color\":\"#FF5706\",\"type\":2}],\"activity_type\":0,\"link_url\":\"goods.html?goods_id=35064877838\",\"tag\":-1,\"goods_name\":\"oppor9splus手机壳oppor9s液态硅胶r9保护套r9plus全包防摔男女款\",\"hd_thumb_wm\":\"\",\"is_app\":0,\"sales_tip\":\"\",\"coupon\":0,\"image_url\":\"http://omsproductionimg.yangkeduo.com/images/\",\"goods_id\":35064877838,\"long_thumb_wm\":\"\",\"prop_tag_list\":[],\"list_type\":0,\"normal_price\":1110,\"icon_list\":[{\"width\":45,\"id\":10000001,\"url\":\"https://pinduoduoimg.yangkeduo.com/deposit/shipintag3.png\",\"height\":45}],\"market_price\":3790,\"image_wm\":\"\",\"p_search\":{\"gin_cat_id_1\":2933,\"promotion_type\":3,\"gin_cat_id_3\":3045,\"quick_refund_tag_type\":1,\"list_id\":\"PWyuumpipu\",\"spacex\":{\"ctr\":0.0040326048620045185,\"expand_beta\":109871.0,\"take_rate\":0.0,\"price\":0.0,\"alpha\":null,\"delta_score\":-1.0,\"tag\":0.0,\"type\":null,\"version\":0.0,\"beta\":null},\"is_third_search\":false,\"is_sink\":false,\"price\":930,\"icon_list\":[10000001],\"cat_id\":3045,\"has_tag\":1,\"page\":5,\"rn\":\"e273cb61-94ec-4a2c-9b5a-1f5749db6432\",\"request_id\":\"2302e0f1-ee57-4f41-ae01-9f98ce84de11\",\"exp_info\":{\"bucket\":\"ly0000_12,ly0001_1,ly0002_58,ly0003_48,ly0004_97,ly0005_1,ly0006_26,ly0007_55,ly0008_4,ly0009_32,ly0010_1,ly0011_56,ly0012_13,ly0013_21,ly0014_9,ly0015_74,ly0016_67,ly0017_64,ly0018_5,ly0019_64,ly0020_94\",\"bucketMap\":{\"ly0020\":94,\"ly0002\":58,\"ly0013\":21,\"ly0001\":1,\"ly0012\":13,\"ly0000\":12,\"ly0011\":56,\"ly0010\":1,\"ly0006\":26,\"ly0017\":64,\"ly0005\":1,\"ly0016\":67,\"ly0004\":97,\"ly0015\":74,\"ly0003\":48,\"ly0014\":9,\"ly0009\":32,\"ly0008\":4,\"ly0019\":64,\"ly0007\":55,\"ly0018\":5},\"exp\":\"sh5\"},\"is_clk_req\":false},\"short_name\":\"oppor9splus手机壳oppor9s液态硅胶r9保护套r9plus全包防摔男女款\",\"thumb_wm\":\"\"},{\"thumb_url\":\"http://t00img.yangkeduo.com/goods/images/2019-08-15/4271528662c66a81c6af5c56661b7300.jpeg\",\"country\":\"\",\"long_thumb_url\":\"\",\"hd_thumb_url\":\"http://t00img.yangkeduo.com/goods/images/2019-08-15/fff60079481d4601658bdaf1d4767c93.jpeg\",\"customer_num\":2,\"hd_url\":\"http://t00img.yangkeduo.com/openapi/images/2019-08-15/43ad47deed8a4d5ef0235bef67d410a0.jpg\",\"tag_text\":{\"channel\":null,\"promotion\":null},\"tag_ext\":{\"source\":1},\"sales\":1,\"event_type\":0,\"price\":1480,\"tag_list\":[],\"activity_type\":0,\"link_url\":\"goods.html?goods_id=34957363587\",\"tag\":-1,\"goods_name\":\"oppoR9s手机壳oppoR9splus液态硅胶软oppo r9s女r9sk防摔r9st简约\",\"hd_thumb_wm\":\"\",\"is_app\":0,\"sales_tip\":\"已拼1件\",\"coupon\":0,\"image_url\":\"http://omsproductionimg.yangkeduo.com/images/\",\"goods_id\":34957363587,\"long_thumb_wm\":\"\",\"prop_tag_list\":[],\"list_type\":0,\"normal_price\":1580,\"icon_list\":[],\"market_price\":1680,\"image_wm\":\"\",\"p_search\":{\"gin_cat_id_1\":2933,\"gin_cat_id_3\":3045,\"list_id\":\"PWyuumpipu\",\"spacex\":{\"ctr\":0.0033452026546001434,\"expand_beta\":109871.0,\"take_rate\":0.0,\"price\":0.0,\"alpha\":null,\"delta_score\":-1.0,\"tag\":0.0,\"type\":null,\"version\":0.0,\"beta\":null},\"is_third_search\":false,\"is_sink\":false,\"price\":1480,\"cat_id\":3045,\"has_tag\":0,\"page\":5,\"rn\":\"e273cb61-94ec-4a2c-9b5a-1f5749db6432\",\"request_id\":\"2302e0f1-ee57-4f41-ae01-9f98ce84de11\",\"exp_info\":{\"bucket\":\"ly0000_12,ly0001_1,ly0002_58,ly0003_48,ly0004_97,ly0005_1,ly0006_26,ly0007_55,ly0008_4,ly0009_32,ly0010_1,ly0011_56,ly0012_13,ly0013_21,ly0014_9,ly0015_74,ly0016_67,ly0017_64,ly0018_5,ly0019_64,ly0020_94\",\"bucketMap\":{\"ly0020\":94,\"ly0002\":58,\"ly0013\":21,\"ly0001\":1,\"ly0012\":13,\"ly0000\":12,\"ly0011\":56,\"ly0010\":1,\"ly0006\":26,\"ly0017\":64,\"ly0005\":1,\"ly0016\":67,\"ly0004\":97,\"ly0015\":74,\"ly0003\":48,\"ly0014\":9,\"ly0009\":32,\"ly0008\":4,\"ly0019\":64,\"ly0007\":55,\"ly0018\":5},\"exp\":\"sh5\"},\"is_clk_req\":false},\"short_name\":\"oppoR9s手机壳oppoR9splus液态硅胶软oppo r9s女r9sk防摔r9st简约\",\"thumb_wm\":\"\"},{\"thumb_url\":\"http://t00img.yangkeduo.com/goods/images/images/2019-09-19/e3dc55846a3fb3f8eed97acd1cc743ca.jpeg\",\"country\":\"\",\"long_thumb_url\":\"\",\"hd_thumb_url\":\"http://t00img.yangkeduo.com/goods/images/images/2019-09-19/7317086517d66fef7c9888ac07e62de4.jpeg\",\"customer_num\":2,\"hd_url\":\"http://t00img.yangkeduo.com/openapi/images/images/2019-09-19/e554bd649708ce1776d468011b7d37ed.jpeg\",\"tag_text\":{\"channel\":null,\"promotion\":null},\"tag_ext\":{\"source\":1},\"sales\":4,\"event_type\":0,\"price\":680,\"tag_list\":[],\"activity_type\":0,\"link_url\":\"goods.html?goods_id=45841487607\",\"tag\":-1,\"goods_name\":\"oppor11手机壳reno2新品r11plus女款r11s硅胶Reno/z全包r9液态r9s\",\"hd_thumb_wm\":\"\",\"is_app\":0,\"sales_tip\":\"已拼4件\",\"coupon\":0,\"image_url\":\"http://t00img.yangkeduo.com/openapi/images/images/2019-09-19/d52b9b71e48d45da5f0b782fe9db3462.jpeg\",\"goods_id\":45841487607,\"long_thumb_wm\":\"\",\"prop_tag_list\":[],\"list_type\":0,\"normal_price\":780,\"icon_list\":[],\"market_price\":1680,\"image_wm\":\"\",\"p_search\":{\"gin_cat_id_1\":2933,\"gin_cat_id_3\":3045,\"list_id\":\"PWyuumpipu\",\"spacex\":{\"ctr\":0.004286589566618204,\"expand_beta\":109871.0,\"take_rate\":0.0,\"price\":0.0,\"alpha\":null,\"delta_score\":-1.0,\"tag\":0.0,\"type\":null,\"version\":0.0,\"beta\":null},\"is_third_search\":false,\"is_sink\":false,\"price\":680,\"cat_id\":3045,\"has_tag\":0,\"page\":5,\"rn\":\"e273cb61-94ec-4a2c-9b5a-1f5749db6432\",\"request_id\":\"2302e0f1-ee57-4f41-ae01-9f98ce84de11\",\"exp_info\":{\"bucket\":\"ly0000_12,ly0001_1,ly0002_58,ly0003_48,ly0004_97,ly0005_1,ly0006_26,ly0007_55,ly0008_4,ly0009_32,ly0010_1,ly0011_56,ly0012_13,ly0013_21,ly0014_9,ly0015_74,ly0016_67,ly0017_64,ly0018_5,ly0019_64,ly0020_94\",\"bucketMap\":{\"ly0020\":94,\"ly0002\":58,\"ly0013\":21,\"ly0001\":1,\"ly0012\":13,\"ly0000\":12,\"ly0011\":56,\"ly0010\":1,\"ly0006\":26,\"ly0017\":64,\"ly0005\":1,\"ly0016\":67,\"ly0004\":97,\"ly0015\":74,\"ly0003\":48,\"ly0014\":9,\"ly0009\":32,\"ly0008\":4,\"ly0019\":64,\"ly0007\":55,\"ly0018\":5},\"exp\":\"sh5\"},\"is_clk_req\":false},\"short_name\":\"oppor11手机壳reno2新品r11plus女款r11s硅胶Reno/z全包r9液态r9s\",\"thumb_wm\":\"\"},{\"thumb_url\":\"http://t00img.yangkeduo.com/goods/images/2019-05-06/931983226f8ebf59a01b1e1d2fc45150.jpeg\",\"country\":\"\",\"long_thumb_url\":\"\",\"hd_thumb_url\":\"http://t00img.yangkeduo.com/goods/images/2019-05-06/6d0d00dfafe5b11772da0a2c397f49d1.jpeg\",\"customer_num\":2,\"hd_url\":\"https://t00img.yangkeduo.com/goods/images/2019-05-06/7048a7ed-991d-4087-a6e8-92e7a840765f.jpg\",\"tag_text\":{\"channel\":null,\"promotion\":\"满16返2\"},\"tag_ext\":{\"source\":1},\"sales\":97,\"event_type\":0,\"price\":1380,\"tag_list\":[{\"text\":\"极速退款\",\"text_color\":\"#FF5706\",\"type\":12},{\"text\":\"只换不修\",\"text_color\":\"#FF5706\",\"type\":7},{\"text\":\"满16返2\",\"text_color\":\"#FF5706\",\"type\":2}],\"activity_type\":0,\"link_url\":\"goods.html?goods_id=8507308715\",\"mall_name\":\"诺朵数码旗舰店\",\"tag\":-1,\"goods_name\":\"oppor17/r15/r11s/r9s/a5/a7液态硅胶手机壳a7x/r9全包软壳女plus\",\"hd_thumb_wm\":\"\",\"is_app\":0,\"sales_tip\":\"已拼97件\",\"coupon\":0,\"image_url\":\"https://t00img.yangkeduo.com/goods/images/2019-05-06/8e5cf69b-d745-49b4-a338-88399b1f0e5e.jpg\",\"goods_id\":8507308715,\"long_thumb_wm\":\"\",\"prop_tag_list\":[],\"list_type\":0,\"normal_price\":1480,\"icon_list\":[],\"market_price\":4800,\"image_wm\":\"\",\"p_search\":{\"gin_cat_id_1\":2933,\"promotion_type\":3,\"gin_cat_id_3\":3045,\"quick_refund_tag_type\":1,\"list_id\":\"PWyuumpipu\",\"spacex\":{\"ctr\":0.0024275248870253563,\"expand_beta\":109871.0,\"take_rate\":0.0,\"price\":0.0,\"alpha\":null,\"delta_score\":-1.0,\"tag\":0.0,\"type\":null,\"version\":0.0,\"beta\":null},\"is_third_search\":false,\"is_sink\":false,\"price\":1380,\"cat_id\":3045,\"not_fix_tag_type\":1,\"has_tag\":1,\"page\":5,\"rn\":\"e273cb61-94ec-4a2c-9b5a-1f5749db6432\",\"request_id\":\"2302e0f1-ee57-4f41-ae01-9f98ce84de11\",\"exp_info\":{\"bucket\":\"ly0000_12,ly0001_1,ly0002_58,ly0003_48,ly0004_97,ly0005_1,ly0006_26,ly0007_55,ly0008_4,ly0009_32,ly0010_1,ly0011_56,ly0012_13,ly0013_21,ly0014_9,ly0015_74,ly0016_67,ly0017_64,ly0018_5,ly0019_64,ly0020_94\",\"bucketMap\":{\"ly0020\":94,\"ly0002\":58,\"ly0013\":21,\"ly0001\":1,\"ly0012\":13,\"ly0000\":12,\"ly0011\":56,\"ly0010\":1,\"ly0006\":26,\"ly0017\":64,\"ly0005\":1,\"ly0016\":67,\"ly0004\":97,\"ly0015\":74,\"ly0003\":48,\"ly0014\":9,\"ly0009\":32,\"ly0008\":4,\"ly0019\":64,\"ly0007\":55,\"ly0018\":5},\"exp\":\"sh5\"},\"is_clk_req\":false},\"short_name\":\"oppor17/r15/r11s/r9s/a5/a7液态硅胶手机壳a7x/r9全包软壳女plus\",\"thumb_wm\":\"\",\"mall_style\":1},{\"thumb_url\":\"https://t00img.yangkeduo.com/goods/images/images/2019-09-27/e4128148-a3c9-410c-83f9-ef4842a491a8.jpg\",\"country\":\"\",\"long_thumb_url\":\"\",\"hd_thumb_url\":\"https://t00img.yangkeduo.com/goods/images/images/2019-09-27/e4128148-a3c9-410c-83f9-ef4842a491a8.jpg\",\"customer_num\":2,\"hd_url\":\"https://t00img.yangkeduo.com/goods/images/images/2019-09-27/e4128148-a3c9-410c-83f9-ef4842a491a8.jpg\",\"tag_text\":{\"channel\":null,\"promotion\":null},\"tag_ext\":{\"source\":1},\"sales\":0,\"event_type\":0,\"price\":1990,\"tag_list\":[{\"text\":\"极速退款\",\"text_color\":\"#FF5706\",\"type\":12}],\"activity_type\":0,\"link_url\":\"goods.html?goods_id=48142966315&gallery_id=212011889627\",\"tag\":-1,\"goods_name\":\"oppor9splus手机壳夜光男定制r9splus手机套防摔r9splus硅胶外壳\",\"hd_thumb_wm\":\"\",\"is_app\":0,\"sales_tip\":\"5人想买\",\"ad\":{\"start_position\":1,\"set\":\"sh5\",\"query\":\"oppor9splus手机壳液态硅胶\",\"goods_id\":48142966315,\"exp_list\":[{\"bucket\":\"7\",\"exp\":\"cacheABLayerSearch\",\"strategy\":\"NotCache\"},{\"bucket\":\"0\",\"exp\":\"starkSearchMallAdLayer\",\"strategy\":\"searchBase\"},{\"bucket\":\"15\",\"exp\":\"algoLayerRankCreative\",\"strategy\":\"rankCreativeBaseAA\"},{\"bucket\":\"79\",\"exp\":\"freshUser\",\"strategy\":\"base\"},{\"bucket\":\"2\",\"exp\":\"fullAlgoLayerCostControl\",\"strategy\":\"base\"},{\"bucket\":\"0\",\"exp\":\"starkSearchGoodsProsLayer\",\"strategy\":\"searchBase\"},{\"bucket\":\"1\",\"exp\":\"searchSameStylePicLayer\",\"strategy\":\"AdSameStypePicV2\"},{\"bucket\":\"1\",\"exp\":\"algoLayerStarkTarget\",\"strategy\":\"targetBase\"},{\"bucket\":\"1\",\"exp\":\"searchExtendAdNumLayer\",\"strategy\":\"addAdNum\"},{\"bucket\":\"1\",\"exp\":\"starkSearchLayer\",\"strategy\":\"AdSameStypePic\"},{\"bucket\":\"38\",\"exp\":\"algoLayerStarkSearch\",\"strategy\":\"q2aEcpmCutV1_200\"},{\"bucket\":\"18\",\"exp\":\"algoLayerStarkUnified\",\"strategy\":\"UnifiedBase\"},{\"bucket\":\"8\",\"exp\":\"admixPhysicEngineLayer\",\"strategy\":\"StarkVipSearchEngine\"}],\"creative_id\":105676282,\"target\":0,\"search_id\":\"_dlGWKebPyGEDTuglG3lYstKuKFHTDbx6sCi4mlCmfXM2HUqdDweJLN5Y88KjheV\",\"trigger_goods_id\":0,\"product_type\":1,\"sub_target\":\"0\",\"mall_id\":468599394,\"match_type\":2001,\"scenes_type\":0,\"bid\":\"Kg6FUk240m9j0KtTsqQgFZScy7w8bSlnF6O7YXi3INjxK57VYYk0fbngcxuulB3ta_BUDGiXMAPTUmtcJ9BfgNHCByvkgPlTngpHpxD8djAQ7V1OxGYfPMvbahQtAk8hs3-kp--7EBQSuTAt8WpdDCs9-Ph_Zq7oi8o2A4BERH3SbXeMkCnckLkLszBpRQhKruq32wTHSFEFX0CWOY4ta5jA6JWwtVE6ZnmDlEG4d4Bw8fd_dr825DgpLmad6jmXfvbkXchp55n8lhB5FmWzbEIHleeqde8LO-D60fX4iIopLPWiXUydCCnQeF3R5FnFpz-U0585JbBzo396lRs7P55VliAWT946EsceP9mGcgM01stmdHLc3YPU9rz4ntalGf_DF464ZUS2dyH5sW0OfnwRwoOvpMBH1M4rYgN2SCZeDdP-NHqyxoUUq3PfCY7hdW4MC155-WD9G4cpbxg2KOYiLOkA4rSvAyXRAjn3uFx9mqb633GSkC_FvXQQ7sInAMAxw8A3qhHHzjs29I4PY8JnxPScW1mEnshD5gFhnteo27RyTbqfaLzOIYmHHHgjZ3nUfZiFsfTSlMaZ9ZPyN-15Q5lsCXgMZAdPJyxLpBLgfbTw4IocD66yRRHfqTJpdFQ8VcIPexOCnTETbwSLyA1Id3_iTZ8ALdauxvkfMKkoqb5qLepkghzdfhAdj5yQ\",\"goods_id_list\":[],\"ispr\":null},\"coupon\":0,\"image_url\":\"https://t00img.yangkeduo.com/goods/images/images/2019-09-27/e4128148-a3c9-410c-83f9-ef4842a491a8.jpg\",\"goods_id\":48142966315,\"long_thumb_wm\":\"\",\"prop_tag_list\":[],\"list_type\":0,\"normal_price\":2090,\"icon_list\":[],\"market_price\":2990,\"image_wm\":\"\",\"p_search\":{\"gin_cat_id_1\":2933,\"gin_cat_id_3\":3045,\"sales_tip\":1,\"quick_refund_tag_type\":1,\"list_id\":\"PWyuumpipu\",\"is_third_search\":false,\"creative\":{\"gallery_id\":212011889627,\"id\":105676282},\"is_sink\":false,\"price\":1990,\"cat_id\":3045,\"has_tag\":1,\"page\":5,\"rn\":\"e273cb61-94ec-4a2c-9b5a-1f5749db6432\",\"request_id\":\"2302e0f1-ee57-4f41-ae01-9f98ce84de11\",\"exp_info\":{\"bucket\":\"ly0000_12,ly0001_1,ly0002_58,ly0003_48,ly0004_97,ly0005_1,ly0006_26,ly0007_55,ly0008_4,ly0009_32,ly0010_1,ly0011_56,ly0012_13,ly0013_21,ly0014_9,ly0015_74,ly0016_67,ly0017_64,ly0018_5,ly0019_64,ly0020_94\",\"bucketMap\":{\"ly0020\":94,\"ly0002\":58,\"ly0013\":21,\"ly0001\":1,\"ly0012\":13,\"ly0000\":12,\"ly0011\":56,\"ly0010\":1,\"ly0006\":26,\"ly0017\":64,\"ly0005\":1,\"ly0016\":67,\"ly0004\":97,\"ly0015\":74,\"ly0003\":48,\"ly0014\":9,\"ly0009\":32,\"ly0008\":4,\"ly0019\":64,\"ly0007\":55,\"ly0018\":5},\"exp\":\"sh5\"},\"is_clk_req\":false},\"short_name\":\"oppor9splus手机壳夜光男定制r9splus手机套防摔r9splus硅胶外壳\",\"thumb_wm\":\"\",\"creative_ad_info\":{\"image_url\":\"https://t00img.yangkeduo.com/goods/images/images/2019-09-27/e4128148-a3c9-410c-83f9-ef4842a491a8.jpg\",\"image_id\":212011889627}},{\"thumb_url\":\"http://t00img.yangkeduo.com/goods/images/2019-07-07/68cd7c46bec5020e42e7e98c83ab1faa.jpeg\",\"country\":\"\",\"long_thumb_url\":\"\",\"hd_thumb_url\":\"http://t00img.yangkeduo.com/goods/images/2019-07-07/5318cda2b5c0090d8243c7ede51415e9.jpeg\",\"customer_num\":2,\"hd_url\":\"https://t00img.yangkeduo.com/goods/images/2019-07-07/aa2b38b4-a0a3-4103-8cc3-0045ab89f77d.jpg\",\"tag_text\":{\"channel\":null,\"promotion\":null},\"tag_ext\":{\"source\":1},\"sales\":262,\"event_type\":0,\"price\":890,\"tag_list\":[],\"activity_type\":0,\"link_url\":\"goods.html?goods_id=22198096113\",\"tag\":-1,\"goods_name\":\"OPPOA9手机壳A3/A5液态A59/A83/A79/A7X硅胶R9s/R11/R17/R15/reno\",\"hd_thumb_wm\":\"\",\"is_app\":0,\"sales_tip\":\"已拼262件\",\"coupon\":0,\"image_url\":\"http://omsproductionimg.yangkeduo.com/images/\",\"goods_id\":22198096113,\"long_thumb_wm\":\"\",\"prop_tag_list\":[],\"list_type\":0,\"normal_price\":1020,\"icon_list\":[],\"market_price\":1550,\"image_wm\":\"\",\"p_search\":{\"gin_cat_id_1\":2933,\"gin_cat_id_3\":3045,\"list_id\":\"PWyuumpipu\",\"spacex\":{\"ctr\":0.0031334967352449894,\"expand_beta\":109871.0,\"take_rate\":0.0,\"price\":0.0,\"alpha\":null,\"delta_score\":-1.0,\"tag\":0.0,\"type\":null,\"version\":0.0,\"beta\":null},\"is_third_search\":false,\"is_sink\":false,\"price\":890,\"cat_id\":3045,\"has_tag\":0,\"page\":5,\"rn\":\"e273cb61-94ec-4a2c-9b5a-1f5749db6432\",\"request_id\":\"2302e0f1-ee57-4f41-ae01-9f98ce84de11\",\"exp_info\":{\"bucket\":\"ly0000_12,ly0001_1,ly0002_58,ly0003_48,ly0004_97,ly0005_1,ly0006_26,ly0007_55,ly0008_4,ly0009_32,ly0010_1,ly0011_56,ly0012_13,ly0013_21,ly0014_9,ly0015_74,ly0016_67,ly0017_64,ly0018_5,ly0019_64,ly0020_94\",\"bucketMap\":{\"ly0020\":94,\"ly0002\":58,\"ly0013\":21,\"ly0001\":1,\"ly0012\":13,\"ly0000\":12,\"ly0011\":56,\"ly0010\":1,\"ly0006\":26,\"ly0017\":64,\"ly0005\":1,\"ly0016\":67,\"ly0004\":97,\"ly0015\":74,\"ly0003\":48,\"ly0014\":9,\"ly0009\":32,\"ly0008\":4,\"ly0019\":64,\"ly0007\":55,\"ly0018\":5},\"exp\":\"sh5\"},\"is_clk_req\":false},\"short_name\":\"OPPOA9手机壳A3/A5液态A59/A83/A79/A7X硅胶R9s/R11/R17/R15/reno\",\"thumb_wm\":\"\"},{\"thumb_url\":\"http://t00img.yangkeduo.com/goods/images/2019-06-07/7ba74d5b003f4207da53503321752dfe.jpeg\",\"country\":\"\",\"long_thumb_url\":\"\",\"hd_thumb_url\":\"http://t00img.yangkeduo.com/goods/images/2019-06-07/b072d4bb08fb850779372a3cd22c27ec.jpeg\",\"customer_num\":2,\"hd_url\":\"https://t00img.yangkeduo.com/goods/images/2019-06-07/09ed0df1-7d3e-436f-9c77-be6782a19616.jpg\",\"tag_text\":{\"channel\":null,\"promotion\":\"满12返1\"},\"tag_ext\":{\"source\":1},\"sales\":38,\"event_type\":0,\"price\":760,\"tag_list\":[{\"text\":\"极速退款\",\"text_color\":\"#FF5706\",\"type\":12},{\"text\":\"满12返1\",\"text_color\":\"#FF5706\",\"type\":2}],\"activity_type\":0,\"link_url\":\"goods.html?goods_id=13151935137\",\"tag\":-1,\"goods_name\":\" oppo r17手机壳ins网红r15x/k1液态硅胶f11/r9s/r11splus纯色软壳\",\"hd_thumb_wm\":\"\",\"is_app\":0,\"sales_tip\":\"已拼38件\",\"coupon\":0,\"image_url\":\"http://omsproductionimg.yangkeduo.com/images/\",\"goods_id\":13151935137,\"long_thumb_wm\":\"\",\"prop_tag_list\":[],\"list_type\":0,\"normal_price\":990,\"icon_list\":[],\"market_price\":2800,\"image_wm\":\"\",\"p_search\":{\"gin_cat_id_1\":2933,\"promotion_type\":3,\"gin_cat_id_3\":3045,\"quick_refund_tag_type\":1,\"list_id\":\"PWyuumpipu\",\"spacex\":{\"ctr\":0.0025475993752479553,\"expand_beta\":109871.0,\"take_rate\":0.0,\"price\":0.0,\"alpha\":null,\"delta_score\":-1.0,\"tag\":0.0,\"type\":null,\"version\":0.0,\"beta\":null},\"is_third_search\":false,\"is_sink\":false,\"price\":760,\"cat_id\":3045,\"has_tag\":1,\"page\":5,\"rn\":\"e273cb61-94ec-4a2c-9b5a-1f5749db6432\",\"request_id\":\"2302e0f1-ee57-4f41-ae01-9f98ce84de11\",\"exp_info\":{\"bucket\":\"ly0000_12,ly0001_1,ly0002_58,ly0003_48,ly0004_97,ly0005_1,ly0006_26,ly0007_55,ly0008_4,ly0009_32,ly0010_1,ly0011_56,ly0012_13,ly0013_21,ly0014_9,ly0015_74,ly0016_67,ly0017_64,ly0018_5,ly0019_64,ly0020_94\",\"bucketMap\":{\"ly0020\":94,\"ly0002\":58,\"ly0013\":21,\"ly0001\":1,\"ly0012\":13,\"ly0000\":12,\"ly0011\":56,\"ly0010\":1,\"ly0006\":26,\"ly0017\":64,\"ly0005\":1,\"ly0016\":67,\"ly0004\":97,\"ly0015\":74,\"ly0003\":48,\"ly0014\":9,\"ly0009\":32,\"ly0008\":4,\"ly0019\":64,\"ly0007\":55,\"ly0018\":5},\"exp\":\"sh5\"},\"is_clk_req\":false},\"short_name\":\" oppo r17手机壳ins网红r15x/k1液态硅胶f11/r9s/r11splus纯色软壳\",\"thumb_wm\":\"\"},{\"thumb_url\":\"http://t00img.yangkeduo.com/goods/images/2019-08-25/001a19d549bee6cc94506061788698ef.jpeg\",\"country\":\"\",\"long_thumb_url\":\"\",\"hd_thumb_url\":\"http://t00img.yangkeduo.com/goods/images/2019-08-25/0211fd6c130ac54e0d8a22d04eefb30a.jpeg\",\"customer_num\":2,\"hd_url\":\"http://t00img.yangkeduo.com/openapi/images/2019-08-14/0f7dc0a74e54914398d2a5c72bad4776.jpeg\",\"tag_text\":{\"channel\":null,\"promotion\":null},\"tag_ext\":{\"source\":1},\"sales\":0,\"event_type\":0,\"price\":990,\"tag_list\":[{\"text\":\"极速退款\",\"text_color\":\"#FF5706\",\"type\":12}],\"activity_type\":0,\"link_url\":\"goods.html?goods_id=34437684694\",\"tag\":-1,\"goods_name\":\"oppoR9s手机壳R9splus液态tm硅胶r9sk保护套r9防摔r9m全包plus软t\",\"hd_thumb_wm\":\"\",\"is_app\":0,\"sales_tip\":\"1人想买\",\"coupon\":0,\"image_url\":\"http://omsproductionimg.yangkeduo.com/images/\",\"goods_id\":34437684694,\"long_thumb_wm\":\"\",\"prop_tag_list\":[],\"list_type\":0,\"normal_price\":1090,\"icon_list\":[{\"width\":45,\"id\":10000001,\"url\":\"https://pinduoduoimg.yangkeduo.com/deposit/shipintag3.png\",\"height\":45}],\"market_price\":3800,\"image_wm\":\"\",\"p_search\":{\"gin_cat_id_1\":2933,\"gin_cat_id_3\":3045,\"sales_tip\":1,\"quick_refund_tag_type\":1,\"list_id\":\"PWyuumpipu\",\"spacex\":{\"ctr\":0.0026599413249641657,\"expand_beta\":109871.0,\"take_rate\":0.0,\"price\":0.0,\"alpha\":null,\"delta_score\":-1.0,\"tag\":0.0,\"type\":null,\"version\":0.0,\"beta\":null},\"is_third_search\":false,\"is_sink\":false,\"price\":990,\"icon_list\":[10000001],\"cat_id\":3045,\"has_tag\":1,\"page\":5,\"rn\":\"e273cb61-94ec-4a2c-9b5a-1f5749db6432\",\"request_id\":\"2302e0f1-ee57-4f41-ae01-9f98ce84de11\",\"exp_info\":{\"bucket\":\"ly0000_12,ly0001_1,ly0002_58,ly0003_48,ly0004_97,ly0005_1,ly0006_26,ly0007_55,ly0008_4,ly0009_32,ly0010_1,ly0011_56,ly0012_13,ly0013_21,ly0014_9,ly0015_74,ly0016_67,ly0017_64,ly0018_5,ly0019_64,ly0020_94\",\"bucketMap\":{\"ly0020\":94,\"ly0002\":58,\"ly0013\":21,\"ly0001\":1,\"ly0012\":13,\"ly0000\":12,\"ly0011\":56,\"ly0010\":1,\"ly0006\":26,\"ly0017\":64,\"ly0005\":1,\"ly0016\":67,\"ly0004\":97,\"ly0015\":74,\"ly0003\":48,\"ly0014\":9,\"ly0009\":32,\"ly0008\":4,\"ly0019\":64,\"ly0007\":55,\"ly0018\":5},\"exp\":\"sh5\"},\"is_clk_req\":false},\"short_name\":\"oppoR9s手机壳R9splus液态tm硅胶r9sk保护套r9防摔r9m全包plus软t\",\"thumb_wm\":\"\"},{\"thumb_url\":\"http://t00img.yangkeduo.com/goods/images/2019-07-06/15f197bde48bac7bc2325603fbe79dfd.jpeg\",\"country\":\"\",\"long_thumb_url\":\"\",\"hd_thumb_url\":\"http://t00img.yangkeduo.com/goods/images/2019-07-06/23ba2acf8640bc7921c141f5ecfeecc1.jpeg\",\"customer_num\":2,\"hd_url\":\"https://t00img.yangkeduo.com/goods/images/2019-07-06/cfbbad38-c53f-4103-a291-8ce8161cf15c.jpg\",\"tag_text\":{\"channel\":null,\"promotion\":\"满24返2\"},\"tag_ext\":{\"source\":1},\"sales\":2,\"event_type\":0,\"price\":790,\"tag_list\":[{\"text\":\"极速退款\",\"text_color\":\"#FF5706\",\"type\":12},{\"text\":\"极速发货\",\"text_color\":\"#FF5706\",\"type\":11},{\"text\":\"满24返2\",\"text_color\":\"#FF5706\",\"type\":2}],\"activity_type\":0,\"link_url\":\"goods.html?goods_id=21865480616\",\"tag\":-1,\"goods_name\":\"oppoR9Splus手机壳R9全包R9Sm液态硅胶R9Sk防摔R9St男女款保护套\",\"hd_thumb_wm\":\"\",\"is_app\":0,\"sales_tip\":\"已拼2件\",\"coupon\":0,\"image_url\":\"https://t00img.yangkeduo.com/goods/images/2019-07-05/6445121f-904d-4b9c-b681-0a2594fd592f.jpg\",\"goods_id\":21865480616,\"long_thumb_wm\":\"\",\"prop_tag_list\":[],\"list_type\":0,\"normal_price\":890,\"icon_list\":[{\"width\":45,\"id\":10000001,\"url\":\"https://pinduoduoimg.yangkeduo.com/deposit/shipintag3.png\",\"height\":45}],\"market_price\":1380,\"image_wm\":\"\",\"p_search\":{\"gin_cat_id_1\":2933,\"promotion_type\":3,\"gin_cat_id_3\":3045,\"quick_refund_tag_type\":1,\"list_id\":\"PWyuumpipu\",\"h24_send_tag_type\":1,\"spacex\":{\"ctr\":0.00224699336104095,\"expand_beta\":109871.0,\"take_rate\":0.0,\"price\":0.0,\"alpha\":null,\"delta_score\":-1.0,\"tag\":0.0,\"type\":null,\"version\":0.0,\"beta\":null},\"is_third_search\":false,\"is_sink\":false,\"price\":790,\"icon_list\":[10000001],\"cat_id\":3045,\"has_tag\":1,\"page\":5,\"rn\":\"e273cb61-94ec-4a2c-9b5a-1f5749db6432\",\"request_id\":\"2302e0f1-ee57-4f41-ae01-9f98ce84de11\",\"exp_info\":{\"bucket\":\"ly0000_12,ly0001_1,ly0002_58,ly0003_48,ly0004_97,ly0005_1,ly0006_26,ly0007_55,ly0008_4,ly0009_32,ly0010_1,ly0011_56,ly0012_13,ly0013_21,ly0014_9,ly0015_74,ly0016_67,ly0017_64,ly0018_5,ly0019_64,ly0020_94\",\"bucketMap\":{\"ly0020\":94,\"ly0002\":58,\"ly0013\":21,\"ly0001\":1,\"ly0012\":13,\"ly0000\":12,\"ly0011\":56,\"ly0010\":1,\"ly0006\":26,\"ly0017\":64,\"ly0005\":1,\"ly0016\":67,\"ly0004\":97,\"ly0015\":74,\"ly0003\":48,\"ly0014\":9,\"ly0009\":32,\"ly0008\":4,\"ly0019\":64,\"ly0007\":55,\"ly0018\":5},\"exp\":\"sh5\"},\"is_clk_req\":false},\"short_name\":\"oppoR9Splus手机壳R9全包R9Sm液态硅胶R9Sk防摔R9St男女款保护套\",\"thumb_wm\":\"\"},{\"thumb_url\":\"http://t00img.yangkeduo.com/goods/images/2019-08-09/f5388cfd08325f1da77f1af1a7f28d90.jpeg\",\"country\":\"\",\"long_thumb_url\":\"\",\"hd_thumb_url\":\"http://t00img.yangkeduo.com/goods/images/2019-08-09/eedaf6cad7b8ab3becdf28bea0b1373c.jpeg\",\"customer_num\":2,\"hd_url\":\"http://t00img.yangkeduo.com/openapi/images/2019-08-09/87d65045accdcd2ee3500f55bf3ee38f.jpg\",\"tag_text\":{\"channel\":null,\"promotion\":null},\"tag_ext\":{\"source\":1},\"sales\":1,\"event_type\":0,\"price\":1480,\"tag_list\":[],\"activity_type\":0,\"link_url\":\"goods.html?goods_id=32952335263\",\"tag\":-1,\"goods_name\":\"oppor9s手机壳r9splus液态硅胶oppo网红女r9sk保护套oppo简约纯色\",\"hd_thumb_wm\":\"\",\"is_app\":0,\"sales_tip\":\"已拼1件\",\"coupon\":0,\"image_url\":\"http://omsproductionimg.yangkeduo.com/images/\",\"goods_id\":32952335263,\"long_thumb_wm\":\"\",\"prop_tag_list\":[],\"list_type\":0,\"normal_price\":1580,\"icon_list\":[],\"market_price\":1680,\"image_wm\":\"\",\"p_search\":{\"gin_cat_id_1\":2933,\"gin_cat_id_3\":3045,\"list_id\":\"PWyuumpipu\",\"spacex\":{\"ctr\":0.0028090858832001686,\"expand_beta\":109871.0,\"take_rate\":0.0,\"price\":0.0,\"alpha\":null,\"delta_score\":-1.0,\"tag\":0.0,\"type\":null,\"version\":0.0,\"beta\":null},\"is_third_search\":false,\"is_sink\":false,\"price\":1480,\"cat_id\":3045,\"has_tag\":0,\"page\":5,\"rn\":\"e273cb61-94ec-4a2c-9b5a-1f5749db6432\",\"request_id\":\"2302e0f1-ee57-4f41-ae01-9f98ce84de11\",\"exp_info\":{\"bucket\":\"ly0000_12,ly0001_1,ly0002_58,ly0003_48,ly0004_97,ly0005_1,ly0006_26,ly0007_55,ly0008_4,ly0009_32,ly0010_1,ly0011_56,ly0012_13,ly0013_21,ly0014_9,ly0015_74,ly0016_67,ly0017_64,ly0018_5,ly0019_64,ly0020_94\",\"bucketMap\":{\"ly0020\":94,\"ly0002\":58,\"ly0013\":21,\"ly0001\":1,\"ly0012\":13,\"ly0000\":12,\"ly0011\":56,\"ly0010\":1,\"ly0006\":26,\"ly0017\":64,\"ly0005\":1,\"ly0016\":67,\"ly0004\":97,\"ly0015\":74,\"ly0003\":48,\"ly0014\":9,\"ly0009\":32,\"ly0008\":4,\"ly0019\":64,\"ly0007\":55,\"ly0018\":5},\"exp\":\"sh5\"},\"is_clk_req\":false},\"short_name\":\"oppor9s手机壳r9splus液态硅胶oppo网红女r9sk保护套oppo简约纯色\",\"thumb_wm\":\"\"},{\"thumb_url\":\"https://t00img.yangkeduo.com/goods/images/2019-07-27/950ef9bb-2225-4689-80b7-7b41f107e469.jpg\",\"country\":\"\",\"long_thumb_url\":\"\",\"hd_thumb_url\":\"https://t00img.yangkeduo.com/goods/images/2019-07-27/950ef9bb-2225-4689-80b7-7b41f107e469.jpg\",\"customer_num\":2,\"hd_url\":\"https://t00img.yangkeduo.com/goods/images/2019-07-27/950ef9bb-2225-4689-80b7-7b41f107e469.jpg\",\"tag_text\":{\"channel\":null,\"promotion\":null},\"tag_ext\":{\"source\":1},\"sales\":233,\"event_type\":0,\"price\":860,\"tag_list\":[{\"text\":\"极速退款\",\"text_color\":\"#FF5706\",\"type\":12}],\"activity_type\":0,\"link_url\":\"goods.html?goods_id=27835815694&gallery_id=178485214369\",\"mall_name\":\"念智数码旗舰店\",\"tag\":-1,\"goods_name\":\"oppor17手机壳液态硅胶防摔r11/r11s/r11plus全包r15/r15x男女套\",\"hd_thumb_wm\":\"\",\"is_app\":0,\"sales_tip\":\"已拼233件\",\"ad\":{\"start_position\":1,\"set\":\"sh5\",\"query\":\"oppor9splus手机壳液态硅胶\",\"goods_id\":27835815694,\"exp_list\":[{\"bucket\":\"7\",\"exp\":\"cacheABLayerSearch\",\"strategy\":\"NotCache\"},{\"bucket\":\"0\",\"exp\":\"starkSearchMallAdLayer\",\"strategy\":\"searchBase\"},{\"bucket\":\"15\",\"exp\":\"algoLayerRankCreative\",\"strategy\":\"rankCreativeBaseAA\"},{\"bucket\":\"79\",\"exp\":\"freshUser\",\"strategy\":\"base\"},{\"bucket\":\"2\",\"exp\":\"fullAlgoLayerCostControl\",\"strategy\":\"base\"},{\"bucket\":\"0\",\"exp\":\"starkSearchGoodsProsLayer\",\"strategy\":\"searchBase\"},{\"bucket\":\"1\",\"exp\":\"searchSameStylePicLayer\",\"strategy\":\"AdSameStypePicV2\"},{\"bucket\":\"1\",\"exp\":\"algoLayerStarkTarget\",\"strategy\":\"targetBase\"},{\"bucket\":\"1\",\"exp\":\"searchExtendAdNumLayer\",\"strategy\":\"addAdNum\"},{\"bucket\":\"1\",\"exp\":\"starkSearchLayer\",\"strategy\":\"AdSameStypePic\"},{\"bucket\":\"38\",\"exp\":\"algoLayerStarkSearch\",\"strategy\":\"q2aEcpmCutV1_200\"},{\"bucket\":\"18\",\"exp\":\"algoLayerStarkUnified\",\"strategy\":\"UnifiedBase\"},{\"bucket\":\"8\",\"exp\":\"admixPhysicEngineLayer\",\"strategy\":\"StarkVipSearchEngine\"}],\"creative_id\":92785200,\"target\":0,\"search_id\":\"_dlGWKebPyGEDTuglG3lYstKuKFHTDbx6sCi4mlCmfXM2HUqdDweJLN5Y88KjheV\",\"trigger_goods_id\":0,\"product_type\":1,\"sub_target\":\"0\",\"mall_id\":642791150,\"match_type\":3,\"scenes_type\":0,\"bid\":\"Op1BvN9DLeISYf8iam4A1Af7qEBwJE1QL5fQNWfWQOHZ06atY87xrUb2BRHqwVBQ7SOkVnuPCvCDPj-86ZdggXYdJAJy45DcQCmXZxeoU1NjEGnlxu7iA4F1UakUIaZWCC4uf3ZIACr3FKTcsqZ7bm2znH6ZNdbylB_77Qqx02o_JH1mHS13NM6Um3LTAk221opWt96yvVWsE1N9Q1I441ODyTrdVpyDc6za-R-l8glx6CPsPLvznPXrPmgrvkUGQB8rUis4uglJBYdLuFBvQ4vs1jHpz9IPRLeAzthVgKmBlBzUslHp5xyg8Me-TBQY2vAzGdmpWHdfTi7pP8zj93CxLflI4fp67SdeAybSZx3mJiM6TV4HhpbXEkG-3gQiZYbvq0h2p2jsoriJx5iDhEewuESHPv1Igu6_yJCDTR0d8bgXQiONKlgWsGyzliglXbZLAqUeP6Qs_tQg0yVHF6BPkbRGPxQZQOWjNQjij6hSkBQJ6QeFXd9kzbqMub_PpOvVubCnFuUdw5nD7rpLVOR4vTOYwT6JLrylpFdU-tHAu0I3Bvx_OPlxkat9OK8g07HkFKF-SG9ce6XdY6dTeGIjKv9xNe952J7CPa30e_PimMEHDjUYlANfG4Zlf570aYba_oVOcQatKKWn-cKf4ov6tuWoecHZWJiPezzowM0=\",\"goods_id_list\":[],\"ispr\":null},\"coupon\":0,\"image_url\":\"https://t00img.yangkeduo.com/goods/images/2019-07-27/950ef9bb-2225-4689-80b7-7b41f107e469.jpg\",\"goods_id\":27835815694,\"long_thumb_wm\":\"\",\"prop_tag_list\":[],\"list_type\":0,\"normal_price\":1080,\"icon_list\":[],\"market_price\":3880,\"image_wm\":\"\",\"p_search\":{\"gin_cat_id_1\":2933,\"gin_cat_id_3\":3045,\"quick_refund_tag_type\":1,\"list_id\":\"PWyuumpipu\",\"is_third_search\":false,\"creative\":{\"gallery_id\":178485214369,\"id\":92785200},\"is_sink\":false,\"price\":860,\"cat_id\":3045,\"has_tag\":1,\"page\":5,\"rn\":\"e273cb61-94ec-4a2c-9b5a-1f5749db6432\",\"request_id\":\"2302e0f1-ee57-4f41-ae01-9f98ce84de11\",\"exp_info\":{\"bucket\":\"ly0000_12,ly0001_1,ly0002_58,ly0003_48,ly0004_97,ly0005_1,ly0006_26,ly0007_55,ly0008_4,ly0009_32,ly0010_1,ly0011_56,ly0012_13,ly0013_21,ly0014_9,ly0015_74,ly0016_67,ly0017_64,ly0018_5,ly0019_64,ly0020_94\",\"bucketMap\":{\"ly0020\":94,\"ly0002\":58,\"ly0013\":21,\"ly0001\":1,\"ly0012\":13,\"ly0000\":12,\"ly0011\":56,\"ly0010\":1,\"ly0006\":26,\"ly0017\":64,\"ly0005\":1,\"ly0016\":67,\"ly0004\":97,\"ly0015\":74,\"ly0003\":48,\"ly0014\":9,\"ly0009\":32,\"ly0008\":4,\"ly0019\":64,\"ly0007\":55,\"ly0018\":5},\"exp\":\"sh5\"},\"is_clk_req\":false},\"short_name\":\"oppor17手机壳液态硅胶防摔r11/r11s/r11plus全包r15/r15x男女套\",\"thumb_wm\":\"\",\"mall_style\":1,\"creative_ad_info\":{\"image_url\":\"https://t00img.yangkeduo.com/goods/images/2019-07-27/950ef9bb-2225-4689-80b7-7b41f107e469.jpg\",\"image_id\":178485214369}},{\"thumb_url\":\"http://t00img.yangkeduo.com/goods/images/2019-08-04/fb7bf5c916afa089402a55d80db2d2b8.jpeg\",\"country\":\"\",\"long_thumb_url\":\"\",\"hd_thumb_url\":\"http://t00img.yangkeduo.com/goods/images/2019-08-04/e08500d21c331baaf53b09e016b783f8.jpeg\",\"customer_num\":2,\"hd_url\":\"https://t00img.yangkeduo.com/goods/images/2019-08-04/a1c4ab72-3988-4dec-b082-c4ae545440b6.jpg\",\"tag_text\":{\"channel\":null,\"promotion\":null},\"tag_ext\":{\"source\":1},\"sales\":0,\"event_type\":0,\"price\":880,\"tag_list\":[],\"activity_type\":0,\"link_url\":\"goods.html?goods_id=30173598872\",\"tag\":-1,\"goods_name\":\"OPPOR9S液态硅胶手机壳 r9splus防摔男女款软壳r9sk极简款保护套t\",\"hd_thumb_wm\":\"\",\"is_app\":0,\"sales_tip\":\"3人想买\",\"coupon\":0,\"image_url\":\"http://omsproductionimg.yangkeduo.com/images/\",\"goods_id\":30173598872,\"long_thumb_wm\":\"\",\"prop_tag_list\":[],\"list_type\":0,\"normal_price\":980,\"icon_list\":[],\"market_price\":2600,\"image_wm\":\"\",\"p_search\":{\"gin_cat_id_1\":2933,\"gin_cat_id_3\":3045,\"sales_tip\":1,\"list_id\":\"PWyuumpipu\",\"spacex\":{\"ctr\":0.002413785085082054,\"expand_beta\":109871.0,\"take_rate\":0.0,\"price\":0.0,\"alpha\":null,\"delta_score\":-1.0,\"tag\":0.0,\"type\":null,\"version\":0.0,\"beta\":null},\"is_third_search\":false,\"is_sink\":false,\"price\":880,\"cat_id\":3045,\"has_tag\":0,\"page\":5,\"rn\":\"e273cb61-94ec-4a2c-9b5a-1f5749db6432\",\"request_id\":\"2302e0f1-ee57-4f41-ae01-9f98ce84de11\",\"exp_info\":{\"bucket\":\"ly0000_12,ly0001_1,ly0002_58,ly0003_48,ly0004_97,ly0005_1,ly0006_26,ly0007_55,ly0008_4,ly0009_32,ly0010_1,ly0011_56,ly0012_13,ly0013_21,ly0014_9,ly0015_74,ly0016_67,ly0017_64,ly0018_5,ly0019_64,ly0020_94\",\"bucketMap\":{\"ly0020\":94,\"ly0002\":58,\"ly0013\":21,\"ly0001\":1,\"ly0012\":13,\"ly0000\":12,\"ly0011\":56,\"ly0010\":1,\"ly0006\":26,\"ly0017\":64,\"ly0005\":1,\"ly0016\":67,\"ly0004\":97,\"ly0015\":74,\"ly0003\":48,\"ly0014\":9,\"ly0009\":32,\"ly0008\":4,\"ly0019\":64,\"ly0007\":55,\"ly0018\":5},\"exp\":\"sh5\"},\"is_clk_req\":false},\"short_name\":\"OPPOR9S液态硅胶手机壳 r9splus防摔男女款软壳r9sk极简款保护套t\",\"thumb_wm\":\"\"},{\"thumb_url\":\"http://t00img.yangkeduo.com/goods/images/2019-09-12/edd95b83e07df325386f11b5dfa68656.jpeg\",\"country\":\"\",\"long_thumb_url\":\"\",\"hd_thumb_url\":\"http://t00img.yangkeduo.com/goods/images/2019-09-12/7833c095dd0a255f4c953cedce47925d.jpeg\",\"customer_num\":2,\"hd_url\":\"https://t00img.yangkeduo.com/goods/images/2019-09-12/42c85f92-f004-46c7-8483-a711f5fbe4a4.jpg\",\"tag_text\":{\"channel\":null,\"promotion\":null},\"tag_ext\":{\"source\":1},\"sales\":1,\"event_type\":0,\"price\":860,\"tag_list\":[{\"text\":\"极速退款\",\"text_color\":\"#FF5706\",\"type\":12}],\"activity_type\":3,\"link_url\":\"goods.html?goods_id=43764656015\",\"tag\":-1,\"goods_name\":\"oppoR9s手机壳R9splus液态硅胶R9保护套tm全包边opr软壳R9S防摔t\",\"hd_thumb_wm\":\"\",\"is_app\":0,\"sales_tip\":\"已拼1件\",\"coupon\":0,\"image_url\":\"http://omsproductionimg.yangkeduo.com/images/\",\"goods_id\":43764656015,\"long_thumb_wm\":\"\",\"prop_tag_list\":[],\"list_type\":0,\"normal_price\":1180,\"icon_list\":[],\"market_price\":2800,\"image_wm\":\"\",\"p_search\":{\"gin_cat_id_1\":2933,\"gin_cat_id_3\":3045,\"quick_refund_tag_type\":1,\"list_id\":\"PWyuumpipu\",\"spacex\":{\"ctr\":0.002267294330522418,\"expand_beta\":109871.0,\"take_rate\":0.0,\"price\":0.0,\"alpha\":null,\"delta_score\":-1.0,\"tag\":0.0,\"type\":null,\"version\":0.0,\"beta\":null},\"is_third_search\":false,\"is_sink\":false,\"price\":860,\"cat_id\":3045,\"has_tag\":1,\"page\":5,\"rn\":\"e273cb61-94ec-4a2c-9b5a-1f5749db6432\",\"request_id\":\"2302e0f1-ee57-4f41-ae01-9f98ce84de11\",\"exp_info\":{\"bucket\":\"ly0000_12,ly0001_1,ly0002_58,ly0003_48,ly0004_97,ly0005_1,ly0006_26,ly0007_55,ly0008_4,ly0009_32,ly0010_1,ly0011_56,ly0012_13,ly0013_21,ly0014_9,ly0015_74,ly0016_67,ly0017_64,ly0018_5,ly0019_64,ly0020_94\",\"bucketMap\":{\"ly0020\":94,\"ly0002\":58,\"ly0013\":21,\"ly0001\":1,\"ly0012\":13,\"ly0000\":12,\"ly0011\":56,\"ly0010\":1,\"ly0006\":26,\"ly0017\":64,\"ly0005\":1,\"ly0016\":67,\"ly0004\":97,\"ly0015\":74,\"ly0003\":48,\"ly0014\":9,\"ly0009\":32,\"ly0008\":4,\"ly0019\":64,\"ly0007\":55,\"ly0018\":5},\"exp\":\"sh5\"},\"is_clk_req\":false},\"short_name\":\"oppoR9s手机壳R9splus液态硅胶R9保护套tm全包边opr软壳R9S防摔t\",\"thumb_wm\":\"\"},{\"thumb_url\":\"http://t00img.yangkeduo.com/goods/images/2019-08-22/3faf7a50b4b8ee5422a79be7322621ae.jpeg\",\"country\":\"\",\"long_thumb_url\":\"\",\"hd_thumb_url\":\"http://t00img.yangkeduo.com/goods/images/2019-08-22/62d5da2c71f5ee8727fa83d3a1d1850a.jpeg\",\"customer_num\":2,\"hd_url\":\"https://t00img.yangkeduo.com/goods/images/2019-08-22/a8e13a5d-bd97-4e4d-9a20-449dac484ed2.jpg\",\"tag_text\":{\"channel\":null,\"promotion\":null},\"tag_ext\":{\"source\":1},\"sales\":10,\"event_type\":0,\"price\":1280,\"tag_list\":[{\"text\":\"极速退款\",\"text_color\":\"#FF5706\",\"type\":12}],\"activity_type\":0,\"link_url\":\"goods.html?goods_id=37069452752\",\"tag\":-1,\"goods_name\":\"oppor9s/r9m/r9splus手机壳全包边软壳硅胶防摔超薄液态k男女款tm\",\"hd_thumb_wm\":\"\",\"is_app\":0,\"sales_tip\":\"已拼10件\",\"coupon\":0,\"image_url\":\"http://omsproductionimg.yangkeduo.com/images/\",\"goods_id\":37069452752,\"long_thumb_wm\":\"\",\"prop_tag_list\":[],\"list_type\":0,\"normal_price\":1480,\"icon_list\":[{\"width\":45,\"id\":10000001,\"url\":\"https://pinduoduoimg.yangkeduo.com/deposit/shipintag3.png\",\"height\":45}],\"market_price\":3680,\"image_wm\":\"\",\"p_search\":{\"gin_cat_id_1\":2933,\"gin_cat_id_3\":3045,\"quick_refund_tag_type\":1,\"list_id\":\"PWyuumpipu\",\"spacex\":{\"ctr\":0.0023962866980582476,\"expand_beta\":109871.0,\"take_rate\":0.0,\"price\":0.0,\"alpha\":null,\"delta_score\":-1.0,\"tag\":0.0,\"type\":null,\"version\":0.0,\"beta\":null},\"is_third_search\":false,\"is_sink\":false,\"price\":1280,\"icon_list\":[10000001],\"cat_id\":3045,\"has_tag\":1,\"page\":5,\"rn\":\"e273cb61-94ec-4a2c-9b5a-1f5749db6432\",\"request_id\":\"2302e0f1-ee57-4f41-ae01-9f98ce84de11\",\"exp_info\":{\"bucket\":\"ly0000_12,ly0001_1,ly0002_58,ly0003_48,ly0004_97,ly0005_1,ly0006_26,ly0007_55,ly0008_4,ly0009_32,ly0010_1,ly0011_56,ly0012_13,ly0013_21,ly0014_9,ly0015_74,ly0016_67,ly0017_64,ly0018_5,ly0019_64,ly0020_94\",\"bucketMap\":{\"ly0020\":94,\"ly0002\":58,\"ly0013\":21,\"ly0001\":1,\"ly0012\":13,\"ly0000\":12,\"ly0011\":56,\"ly0010\":1,\"ly0006\":26,\"ly0017\":64,\"ly0005\":1,\"ly0016\":67,\"ly0004\":97,\"ly0015\":74,\"ly0003\":48,\"ly0014\":9,\"ly0009\":32,\"ly0008\":4,\"ly0019\":64,\"ly0007\":55,\"ly0018\":5},\"exp\":\"sh5\"},\"is_clk_req\":false},\"short_name\":\"oppor9s/r9m/r9splus手机壳全包边软壳硅胶防摔超薄液态k男女款tm\",\"thumb_wm\":\"\"}]}";
		String sellerName = "瑞星";
		String goodsName = "oppor9splus手机壳液态硅胶";*/
		//http.searchMallId(goodsName, sellerName);
		
		/*JSONArray items = JSONObject.parseObject(json).getJSONArray("items");
		for(int i=0;i<items.size();i++) {
			JSONObject item = items.getJSONObject(i);
			String goods_id = item.getString("goods_id");
			JSONObject goods = http.productChromeDetail(goods_id);
			String mallName = goods.getJSONObject("store").getJSONObject("initDataObj").getJSONObject("mall")
					.getString("mallName");
			logger.info("pdd-{}:mallName={}", user.getUsername(), mallName);
			if (mallName.contains(sellerName)) {
				String mallId = goods.getJSONObject("store").getJSONObject("initDataObj").getJSONObject("mall").getString("mallID");
				logger.info("pdd-{}:成功查找到商品.goodsId={}|mallId={}", user.getUsername(), goods_id, mallId);
				break;
			}
		}*/
		/*String url = "https://mobile.yangkeduo.com/addresses.html?refer_page_name=personal&refer_page_id=10001_1570101243221_T3HnnRTgod&refer_page_sn=10001";
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("Accept-Encoding", "gzip, deflate, br");
		heads.put("Accept-Language", "zh-CN,zh;q=0.9");
		heads.put("User-Agent", user.getUserAgent());
		heads.put("AccessToken", user.getToken());
		heads.put("Cookie", "PDDAccessToken=" + user.getToken() + ";");
		String content = MobileHttpUrlConnectUtils.httpGet(url, heads, null);
		System.out.println(content);*/
	}
	
	/**
	 * 从第一页开始下载图片，直到所有
	 * @param pddImgPath
	 * @return
	 */
	public int downloadAllCommentImage(String pddImgPath) {
		logger.info("pdd-{}:开始下载评论图片.", user.getUsername());
		JSONObject json = this.myComments();
		JSONArray myComments = json.getJSONObject("store").getJSONArray("list");
		int count = 0;
		for (int i = 0; i < myComments.size(); i++) {
			JSONObject myComment = myComments.getJSONObject(i);
			JSONObject order = myComment.getJSONObject("order_info");
			String orderSn = order.getString("order_sn");
			String orginImg = pddImgPath + File.separator + orderSn + ".png";
			if (new File(orginImg).exists()) {
				//logger.info("pdd-{}:结束下载评论图片.count={}", user.getUsername(), count);
				//return count;
				continue;
			}
			Map<String, Object> dataMap = parseComment(myComment);
			String html = FreemarkerUtils.loadTemplate("pj.ftl", dataMap);
			FileUtils.htmlToImage(html, 900, orginImg);
			count++;
			logger.info("pdd-{}:{}-下载评论图片成功.", user.getUsername(), orderSn);
		}
		JSONObject last = myComments.getJSONObject(myComments.size()-1);
		return myComment(pddImgPath, count, 2, last.getString("review_id"), last.getString("time"));
	}
	
	/**
	 * 下载我的评论第几页
	 * @param pddImgPath
	 * @param count
	 * @param page
	 * @param lastReviewId
	 * @param lastCreateTime
	 * @return
	 */
	private int myComment(String pddImgPath, int count, int page, String lastReviewId, String lastCreateTime) {
		if(page>8) return count;
		logger.info("pdd-{}:开始下载评论图片.page={}", user.getUsername(),page);
		JSONObject json = myComments(page, lastReviewId, lastCreateTime);
		JSONArray myComments = json.getJSONArray("data");
		if(myComments==null||myComments.size()<1) {
			return count;
		}
		for (int i = 0; i < myComments.size(); i++) {
			JSONObject myComment = myComments.getJSONObject(i);
			JSONObject order = myComment.getJSONObject("order_info");
			String orderSn = order.getString("order_sn");
			String orginImg = pddImgPath + File.separator + orderSn + ".png";
			if (new File(orginImg).exists()) {
				/*logger.info("pdd-{}:结束下载评论图片.count={}", user.getUsername(), count);
				return count;*/
				continue;
			}
			Map<String, Object> dataMap = parseComment(myComment);
			String html = FreemarkerUtils.loadTemplate("pj.ftl", dataMap);
			FileUtils.htmlToImage(html, 900, orginImg);
			count++;
			logger.info("pdd-{}:{}-下载评论图片成功.", user.getUsername(), orderSn);
		}
		logger.info("pdd-{}:结束下载评论图片.page={}", user.getUsername(),page);
		JSONObject last = myComments.getJSONObject(myComments.size()-1);
		page++;
		return myComment(pddImgPath, count, page, last.getString("review_id"), last.getString("time"));
	}
	
	/**
	 * 解析评论内容
	 * @param myComment
	 * @return
	 */
	private Map<String, Object> parseComment(JSONObject myComment){
		Map<String, Object> dataMap = new HashMap<String, Object>();
		JSONObject order = myComment.getJSONObject("order_info");
		dataMap.put("userImg", myComment.getString("avatar"));
		String pjTime = DateUtils.formatDate(new Date(myComment.getLongValue("time") * 1000), "yyyy.MM.dd");
		dataMap.put("pjTime", pjTime);
		List<String> pjImageUrls = new ArrayList<String>();
		JSONArray pictures = myComment.getJSONArray("pictures");
		int maxPicNum = pictures == null ? 0 : pictures.size() < 3 ? pictures.size() : 3;
		for (int j = 0; j < maxPicNum; j++) {
			JSONObject picture = pictures.getJSONObject(j);
			pjImageUrls.add(picture.getString("url"));
		}
		dataMap.put("pjImgs", pjImageUrls);
		dataMap.put("goodsImg", order.getString("goods_picture"));
		String goodsName = order.getString("goods_name");
		String goodsName01 = "";
		String goodsName02 = "";
		if (goodsName.length() > 38) {
			goodsName01 = goodsName.substring(0, 19);
			goodsName02 = goodsName.substring(19, 38);
		} else if (goodsName.length() > 19) {
			goodsName01 = goodsName.substring(0, 19);
			goodsName02 = goodsName.substring(19, goodsName.length());
		} else {
			goodsName01 = goodsName;
		}
		dataMap.put("goodsName01", goodsName01);// 19
		if (StringUtils.isNotBlank(goodsName02)) {
			dataMap.put("goodsName02", goodsName02);
		}
		JSONArray specs = JSONArray.parseArray(myComment.getString("specs"));
		String spec = "";
		for (int m = 0; m < specs.size(); m++) {
			spec += specs.getJSONObject(m).getString("spec_key") + ":"
					+ specs.getJSONObject(m).getString("spec_value");
		}
		String spec01 = "";
		String spec02 = "";
		if (spec.length() > 30) {
			spec01 = spec.substring(0, 15);
			spec02 = spec.substring(15, 30);
		} else if (spec.length() > 15) {
			spec01 = spec.substring(0, 15);
			spec02 = spec.substring(15, spec.length());
		} else {
			spec01 = spec;
		}
		dataMap.put("spec01", spec01);// 15
		dataMap.put("spec02", spec02);
		dataMap.put("orderAmount", order.getFloat("order_amount") / 100);
		String comment = myComment.getString("comment");
		String comment01 = "";
		String comment02 = "";
		String comment03 = "";
		if (comment.length() > 72) {
			comment01 = comment.substring(0, 24);
			comment02 = comment.substring(24, 48);
			comment03 = comment.substring(48, 72);
		} else if (comment.length() > 48) {
			comment01 = comment.substring(0, 24);
			comment02 = comment.substring(24, 48);
			comment03 = comment.substring(48, comment.length());
		} else if (comment.length() > 24) {
			comment01 = comment.substring(0, 24);
			comment02 = comment.substring(24, comment.length());
		} else {
			comment01 = comment;
		}
		dataMap.put("comment01", comment01);// 15
		dataMap.put("comment02", comment02);
		dataMap.put("comment03", comment03);
		return dataMap;
	}
	
	private String getImageName(String img) {
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
	 * 商品详细信息
	 * 
	 * @param goodsId
	 * @return
	 */
	public JSONObject productDetail(String goodsId) {
		String url = "https://api.yangkeduo.com/api/oak/integration/render?pdduid=" + user.getUid();
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("User-Agent", user.getUserAgent());
		heads.put("Accept-Encoding", "gzip");
		heads.put("Content-Type", "application/json;charset=UTF-8");
		heads.put("AccessToken", user.getToken());
		heads.put("Cookie", "api_uid=" + user.getApiUid());
		String postData = "{\"goods_id\":" + goodsId + ",\"page_from\":2}";
		String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
		//logger.info("pddms-{}: 查询商品详细.{}", user.getUsername(), content);
		if (StringUtils.isNotBlank(content)) {
			JSONObject jsonObj = JSONObject.parseObject(content);
			return jsonObj;
		}
		return null;
	}
	
	public JSONObject productDetail02(String goodsId) {
		String url = "http://api.yangkeduo.com/api/oakstc/v14/goods/"+goodsId+"?goods_id="+goodsId+"&pdduid=" + user.getUid();
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("User-Agent", user.getUserAgent());
		heads.put("Accept-Encoding", "gzip");
		heads.put("Content-Type", "application/json;charset=UTF-8");
		heads.put("AccessToken", user.getToken());
		heads.put("Cookie", "api_uid=" + user.getApiUid());
		String postData = "{\"goods_id\":" + goodsId + ",\"page_from\":2}";
		String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
		logger.info("pddms-{}: 查询商品详细.{}", user.getUsername(), content);
		if (StringUtils.isNotBlank(content)) {
			JSONObject jsonObj = JSONObject.parseObject(content);
			return jsonObj;
		}
		return null;
	}
	
	public JSONObject orderDetail(String orderSn) {
		String url = "https://api.pinduoduo.com/order/"+orderSn+"?pdduid=1852788720" + user.getUid();
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("User-Agent", user.getUserAgent());
		heads.put("Accept-Encoding", "gzip");
		heads.put("Content-Type", "application/json;charset=UTF-8");
		heads.put("AccessToken", user.getToken());
		heads.put("ETag", "b0LzKmIm");
		heads.put("PDD-CONFIG", "00102");
		heads.put("p-mode", "1");
		String content = MobileHttpUrlConnectUtils.httpGet(url, heads, null);
		logger.info(content);
		return null;
	}
	
	public JSONObject orderDetail02(String orderSn) {
		String url = "https://mobile.yangkeduo.com/order.html?order_sn="+orderSn+"&type=0&ts="+new Date().getTime();
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
		heads.put("Accept-Encoding", "gzip, deflate, br");
		heads.put("Accept-Language", "zh-CN,zh;q=0.9");
		heads.put("User-Agent", user.getUserAgent());
		heads.put("AccessToken", user.getToken());
		heads.put("Cookie", "PDDAccessToken=" + user.getToken() + ";");
		String content = MobileHttpUrlConnectUtils.httpGet(url, heads, null);
		int start = content.indexOf("window.rawData=") + 15;
		int end = content.indexOf("}};", start) + 2;
		content = content.substring(start, end);
		//logger.info("pddms-{}: 查询商品详细.{}", user.getUsername(), content);
		if (StringUtils.isNotBlank(content)) {
			JSONObject jsonObj = JSONObject.parseObject(content);
			return jsonObj;
		}
		return null;
	}

	public JSONArray groups(String goodsId) {
		String url = "https://api.yangkeduo.com/api/leibniz/goods/user/groups?pdduid=" + user.getUid();
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("User-Agent", user.getUserAgent());
		heads.put("Accept-Encoding", "gzip");
		heads.put("Content-Type", "application/json;charset=UTF-8");
		heads.put("AccessToken", user.getToken());
		heads.put("Cookie", "api_uid=" + user.getApiUid());
		String postData = "{\"goods_id\":" + goodsId + "}";
		String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
		// logger.info("pddms-{}: 查询拼组.{}", user.getUsername(), content);
		if (StringUtils.isNotBlank(content)) {
			JSONObject jsonObj = JSONObject.parseObject(content);
			JSONArray array = jsonObj.getJSONArray("local_group");
			JSONArray groups = new JSONArray();
			for (int i = 0; i < array.size(); i++) {
				JSONObject group = JSONObject.parseObject(array.getString(i));
				groups.add(group);
			}
			return groups;
		}
		return null;
	}

	/**
	 * 网页上的商品明细
	 * 
	 * @param goodsId
	 * @return
	 */
	public JSONObject productChromeDetail(String goodsId) {
		String url = "https://mobile.yangkeduo.com/goods.html?goods_id=" + goodsId;
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
		heads.put("Accept-Encoding", "gzip, deflate, br");
		heads.put("Accept-Language", "zh-CN,zh;q=0.9");
		heads.put("User-Agent", user.getUserAgent());
		heads.put("AccessToken", user.getToken());
		heads.put("Cookie", "PDDAccessToken=" + user.getToken() + ";");
		String content = MobileHttpUrlConnectUtils.httpGet(url, heads, null);
		int start = content.indexOf("window.rawData=") + 15;
		int end = content.indexOf("}};", start) + 2;
		content = content.substring(start, end);
		//logger.info("pddms-{}: 查询商品详细.{}", user.getUsername(), content);
		if (StringUtils.isNotBlank(content)) {
			JSONObject jsonObj = JSONObject.parseObject(content);
			return jsonObj;
		}
		return null;
	}

	public JSONObject submitMsOrder(String groupId, String skuId, String goodsId, String groupOrderId) {
		String url = "https://api.yangkeduo.com/api/van-spike/order?pdduid=" + user.getUid();
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("User-Agent", user.getUserAgent());
		heads.put("Accept-Encoding", "gzip");
		heads.put("Content-Type", "application/json;charset=UTF-8");
		// heads.put("Cookie", "api_uid=" + user.getApiUid());
		heads.put("AccessToken", user.getToken());
		// heads.put("ETag", "b0LzKmIm");
		heads.put("p-mode", "1");
		// heads.put("PDD-CONFIG", "00102");
		// heads.put("anti-token",
		// "2afEFCN1/ylZYEt5aQ/mRzIgcfIHxRZbykbUbVh8ki8pe9+5SbZH7wn4ygPpN9BgPpRwUZeN6WWm4/0Nae4Ob3GA/pjimoFKjyPhTlaCN52M1rsx74WiX3parsnfSL5z5PJKaZhntISHM2g3oBjqSdWnnrkm9l4YO9FyIkFgiaE9azNSTqqZsW137kSzQZBrwPJqK75xr2kD86Jpq4+lsspZLz1roXHOc8i8Tjocu6NZTG76exK2E8pSxXxTG7AVA3OQkPx9lU/hh8gfa2uEoRwjSNeTjXaFlw+5Hgjt1zCYYTyZhL86N1OQsMr9YKDUA/RSdnfr/dG+WwfTG2nP/dtg4QJvMFumPZyapUyTANPPFo5/COGHfNC/rll4UGRHThGKA6eAfB5boDjnu5fmCI9eHv2GVnnw5HzzLbcw8NerheB1rl3OrBkTosdqr3r3PzsTYgf0kj/0xWokauuQHL3pSkBxWhVatWMMyDNAcOVpSq5ys05OzS4bj5VOOq04qFyaml43nTQn561RTOOT7LuO/vb1aBXUJtlvg0cwXmNkoMbbWHPPeHJFk3BQyxqFi/Nnr9b4UQpX/hdpn/9QabK7RkYoYfoQKM6hEbHN8nYv6gM4dD05xzqAtkj7/TVTr4ntZjfC6YaNeoQXTRrLnqBxw==");
		JSONArray goods = new JSONArray();
		JSONObject goods01 = new JSONObject(true);
		goods01.fluentPut("goods_id", goodsId).fluentPut("sku_id", skuId).fluentPut("sku_number", 1);
		goods.add(goods01);
		JSONObject postData = new JSONObject(true);
		postData.fluentPut("address_id", user.getAddressId()).fluentPut("goods", goods).fluentPut("group_id", groupId)
				.fluentPut("anti_content", anti_content).fluentPut("page_from", 2).fluentPut("duoduo_type", 0)
				.fluentPut("biz_type", 0).fluentPut("attribute_fields", null).fluentPut("source_channel", 0)
				.fluentPut("source_type", 0).fluentPut("pay_app_id", 4).fluentPut("is_app", "1")
				.fluentPut("version", 1);
		if (StringUtils.isNotBlank(groupOrderId)) {
			postData.fluentPut("group_order_id", groupOrderId);
		}
		String content = MobileHttpUrlConnectUtils.httpPost(url, postData.toJSONString(), heads, null);
		// logger.info("pddms-{}: 提交订单结果={}", user.getUsername(), content);

		if (StringUtils.isNotBlank(content)) {
			JSONObject jsonObj = JSONObject.parseObject(content);
			return jsonObj;
		}
		return null;
	}

	/**
	 * app上商品购买
	 * 
	 * @param activityId
	 * @param groupId
	 * @param skuId
	 * @param goodsId
	 * @param ddtz
	 *            是否使用多多团长
	 * @return
	 */
	public JSONObject submitAppOrder(String activityId, String groupId, String skuId, String goodsId,
			String groupOrderId, boolean ddtz) {
		String url = "http://api.yangkeduo.com/order?pdduid=" + user.getUid();
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("User-Agent", user.getUserAgent());
		heads.put("Accept-Encoding", "gzip");
		heads.put("Content-Type", "application/json;charset=UTF-8");
		heads.put("Cookie", "api_uid=" + user.getApiUid());
		heads.put("AccessToken", user.getToken());
		heads.put("ETag", "b0LzKmIm");
		heads.put("lat", "TDDLQTU2MDK3WJ54Z5UTYICXCCBN374EJ6JF5M67PGTZ3CNTSZSQ103f010");
		heads.put("PDD-CONFIG", "00102");
		heads.put("Referer", "Android");
		heads.put("X-PDD-QUERIES", "width=1080&height=1920&net=1&brand=xiaomi&model=MI+5X&osv=7.1.2&appv=4.33.0&pl=2");
		// heads.put("anti-token",
		// "2afEFCN1/ylZYEt5aQ/mRzIgeygAjbO3Jsh0iivSzz3lSMLzTYmFdDosscIsMkz9HOj+Oc68NXRl2Q6gnUNNo1QttFKVney5cdoiS+FNOssMdJ5qJ+z5GQJl5QPjdQ3r4edw0yW5m4cJCo5CNfEyBHRcy/RPbrzoqTn0hrbU47FWtjvNmIKZyzTomuAZQvOW6utRUTblriX2tbJjv2SMQBz1jaFKYZvIlAulB9BAx2tXZHrdsNk/Q20Tl6acmPLeQimj0SX+be0vden5WSNnxb4LsVLinsBgy8bAAZkXSUtpJA1GJW3YIC6iC8vouXqyzrRk0hI56PMV/jviaj+OWIneCoidKUhm2MfCog3jX9svpqlLGHPleNxMo8SDRWK97tnqG0QB8DLXJ8BP939+kB6WiHMAlxaWpS7JjwPj9UZq6gQkmmDVL3kJf+va5sYaBsuixM1Q0uyruN6Wk/qqNk9EIZEJGpe+7EEVsV6HQLqjB/FdyhAwRTmksoagBhzq0Emgvn6xozDg4vFGjARqavp09jgMsXsxy8ZAFeTiWNWbVOUIaw0BJTEhr5nsjLYfcgcs69FGBQc5uQaSrHEOE/iZOZHFwHtrl5w5ahVwd7e3FMn7VceBDM1TFbk0reG8AGjSHvVsesmW+AiqYxnBdfsNA==");
		JSONArray goods = new JSONArray();
		JSONObject goods01 = new JSONObject(true);
		goods01.fluentPut("goods_id", goodsId).fluentPut("sku_id", skuId).fluentPut("sku_number", 1);
		goods.add(goods01);
		JSONObject postData = new JSONObject(true);
		JSONObject orderCheckout = orderCheckout(groupId, skuId, goodsId);
		JSONObject extendMap = orderCheckout.getJSONObject("store").getJSONObject("extendMap");
		JSONObject goodsInfo = orderCheckout.getJSONObject("store").getJSONObject("goodsInfo");
		activityId = goodsInfo.getString("activityId");
		postData = postData.fluentPut("activity_id", activityId);
		String create_order_token = extendMap.getString("create_order_token");
		String create_order_check = extendMap.getString("create_order_check");
		String original_front_env = extendMap.getString("original_front_env");
		String current_front_env = extendMap.getString("current_front_env");
		String uuid = extendMap.getString("PTRACER-TRACE-UUID");
		JSONObject attributeFields = new JSONObject(true);
		attributeFields.fluentPut("create_order_token", create_order_token).fluentPut("create_order_check", create_order_check).fluentPut("original_front_env", original_front_env).fluentPut("current_front_env", current_front_env).fluentPut("PTRACER-TRACE-UUID", uuid);
		int type = 0;
		int duoduo_type = 0;
		if (ddtz) {
			String ddtzUrl = getShareUrl(goodsId);
			if (StringUtils.isNotBlank(ddtzUrl)) {
				type = 2;
				duoduo_type=2;
				String ddPid = ddtzUrl.substring(ddtzUrl.indexOf("pid=") + 4,
						ddtzUrl.indexOf("&", ddtzUrl.indexOf("pid=") + 4));
				String cpsSign = ddtzUrl.substring(ddtzUrl.indexOf("cpsSign=") + 4,
						ddtzUrl.indexOf("&", ddtzUrl.indexOf("cpsSign") + 4));
				attributeFields.fluentPut("DUO_DUO_PID", ddPid).fluentPut("cps_sign", cpsSign);
			}
		}
		postData = postData.fluentPut("attribute_fields", attributeFields);
		/*postData.fluentPut("address_id", user.getAddressId()).fluentPut("award_type", 0).fluentPut("biz_type", 0)
				.fluentPut("coupon_number", 0).fluentPut("type", type).fluentPut("goods", goods)
				.fluentPut("group_id", groupId).fluentPut("is_app", 1).fluentPut("page_from", "0")
				.fluentPut("pay_app_id", "35").fluentPut("duoduo_type", duoduo_type).fluentPut("source_channel", -1).fluentPut("source_type", 0).fluentPut("version", 1).fluentPut("is_app", "1")
				.fluentPut("version_type", 1);*/
		postData.fluentPut("address_id", user.getAddressId()).fluentPut("group_id", groupId).fluentPut("duoduo_type", duoduo_type).fluentPut("type", type).fluentPut("biz_type", 0)
		.fluentPut("source_channel", -1).fluentPut("source_type", 0).fluentPut("goods", goods)
		.fluentPut("pay_app_id", "35").fluentPut("is_app", 1).fluentPut("version", 1).fluentPut("page_id", "10004_"+new Date().getTime()+"_UF7Vy91SFm");
		if (StringUtils.isNotBlank(groupOrderId)) {
			postData.fluentPut("group_order_id", groupOrderId);
		}
		String content = MobileHttpUrlConnectUtils.httpPost(url, postData.toJSONString(), heads, null);
		logger.info("pddms-{}: 提交订单结果.{}", user.getUsername(), content);
		//{"server_time":1570525629,"order_sn":"191008-040803042414080","group_order_id":"906040803042414080","order_amount":1200,"fp_id":"tzuo9hWfdAIDEoltJe_VHx1AmVVfBPKVhmyiuZFWl24"}
		if (StringUtils.isNotBlank(content)) {
			JSONObject jsonObj = JSONObject.parseObject(content);
			return jsonObj;
		}
		return null;
	}

	/**
	 * 微信上面商品购买
	 * 
	 * @param activityId
	 * @param groupId
	 * @param skuId
	 * @param goodsId
	 * @return
	 */
	public JSONObject submitWxOrder(String activityId, String groupId, String skuId, String goodsId) {
		String url = "https://mobile.yangkeduo.com/proxy/api/api/van-spike/order?pdduid=1852788720";
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("Accept-Encoding", "gzip, deflate");
		heads.put("User-Agent", user.getUserAgent());
		heads.put("X-Requested-With", "com.tencent.mm");
		heads.put("Origin", "https://mobile.yangkeduo.com");
		heads.put("Content-Type", "application/json;charset=UTF-8");
		heads.put("Cookie", "PDDAccessToken=" + user.getToken() + ";");
		heads.put("accesstoken", user.getToken());
		JSONArray goods = new JSONArray();
		JSONObject goods01 = new JSONObject(true);
		goods01.fluentPut("goods_id", goodsId).fluentPut("sku_id", skuId).fluentPut("sku_number", 1);
		goods.add(goods01);
		JSONObject postData = new JSONObject(true);
		postData.fluentPut("address_id", user.getAddressId()).fluentPut("goods", goods).fluentPut("group_id", groupId)
				.fluentPut("duoduo_type", 0).fluentPut("biz_type", 0).fluentPut("attribute_fields", null)
				.fluentPut("source_channel", 0).fluentPut("is_app", 0).fluentPut("pay_app_id", "2")
				.fluentPut("source_type", 0).fluentPut("version", 1);
		String content = MobileHttpUrlConnectUtils.httpPost(url, postData.toJSONString(), heads, null);
		logger.info(content);
		if (StringUtils.isNotBlank(content)) {
			JSONObject jsonObj = JSONObject.parseObject(content);
			return jsonObj;
		}
		return null;
	}

	/**
	 * 多多团长转换
	 * 
	 * @param goodsId
	 * @return
	 */
	public String getDuoGroupId(String goodsId) {
		String url = "https://api.pinduoduo.com/api/jinbaoapp/wechat_auth/group/create?pdduid=" + user.getUid();
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("Accept", "*/*");
		heads.put("User-Agent",
				"Mozilla/5.0 (Linux; Android 7.1.2; MI 5X Build/N2G47H; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/66.0.3359.126 MQQBrowser/6.2 TBS/044904 Mobile Safari/537.36 MMWEBID/4288 MicroMessenger/7.0.6.1460(0x27000634) Process/tools NetType/WIFI Language/zh_CN");
		heads.put("Accept-Encoding", "gzip, deflate, br");
		heads.put("X-Requested-With", "com.tencent.mm");
		heads.put("Content-Type", "application/json;charset=UTF-8");
		heads.put("Cookie", "api_uid=" + user.getApiUid());
		heads.put("AccessToken", ddjbToken);
		heads.put("Referer", "Android");
		String postData = "{\"duoGroupCreateVO\":{\"groupType\":2,\"goodsId\":" + goodsId + "}}";
		String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
		logger.info("pddms-{}: 获取多多团长组id.{}", user.getUsername(), content);
		if (StringUtils.isNotBlank(content)) {
			JSONObject jsonObj = JSONObject.parseObject(content);
			if (jsonObj.getBooleanValue("success"))
				return jsonObj.getJSONObject("result").getString("duoGroupId");
			// {"success":true,"errorCode":1000000,"errorMsg":null,"result":{"duoGroupId":38867390,"cpsSign":"CF_190911_9078882_108793272_be8a36f1303a22121f638ea41685a19f"}}
		}
		return null;
	}

	public String getDuoSign() {
		String url = "https://api.pinduoduo.com/api/jinbao/wechat_auth/create_token_sign?pdduid=" + user.getUid();
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("Accept", "*/*");
		heads.put("User-Agent",
				"Mozilla/5.0 (Linux; Android 7.1.2; MI 5X Build/N2G47H; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/66.0.3359.126 MQQBrowser/6.2 TBS/044904 Mobile Safari/537.36 MMWEBID/4288 MicroMessenger/7.0.6.1460(0x27000634) Process/tools NetType/WIFI Language/zh_CN");
		heads.put("Accept-Encoding", "gzip, deflate, br");
		heads.put("X-Requested-With", "com.tencent.mm");
		heads.put("Content-Type", "application/json;charset=UTF-8");
		heads.put("Cookie", "api_uid=" + user.getApiUid());
		heads.put("AccessToken", ddjbToken);
		heads.put("Referer", "Android");
		String postData = "{}";
		String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
		logger.info("pddms-{}: 获取多多团长sign={}", user.getUsername(), content);
		if (StringUtils.isNotBlank(content)) {
			JSONObject jsonObj = JSONObject.parseObject(content);
			if (jsonObj.getBooleanValue("success"))
				return jsonObj.getJSONObject("result").getString("sign");
		}
		return null;
	}

	/**
	 * 多多团长商品推广转换 有问题，弃用
	 */
	public String getShareUrlOld(String goodsId) {
		try {
			String groupId = getDuoGroupId(goodsId);
			String tokenSign = getDuoSign();
			String url = "http://m.pin18pin.com/share_mode.html?duo_group_id=" + groupId + "&goods_id=" + goodsId
					+ "&last_refer=duo_conversion&token_sign=" + tokenSign;
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,image/wxpic,image/sharpp,image/apng,image/tpg,*/*;q=0.8");
			heads.put("Accept-Encoding", "gzip, deflate");
			heads.put("Accept-Language", "zh-CN,en-US;q=0.9");
			heads.put("User-Agent",
					"Mozilla/5.0 (Linux; Android 7.1.2; MI 5X Build/N2G47H; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/66.0.3359.126 MQQBrowser/6.2 TBS/044904 Mobile Safari/537.36 MMWEBID/4288 MicroMessenger/7.0.6.1460(0x27000634) Process/tools NetType/WIFI Language/zh_CN");
			heads.put("Cookie", "PDDAccessToken=" + ddjbToken + ";");
			String content = MobileHttpUrlConnectUtils.httpGet(url, heads, null);
			int start = content.indexOf("window.rawData=") + 15;
			int end = content.indexOf("}};", start) + 2;
			content = content.substring(start, end);
			JSONObject jsonObj = JSONObject.parseObject(content);
			logger.info("pddms-{}:查询分享商品址结果={}", user.getUsername(), jsonObj);
			String shareUrl = jsonObj.getJSONObject("store").getJSONObject("goods").getString("weAppWebViewUrl");
			logger.info("pddms-{}:查询分享商品址={}", user.getUsername(), shareUrl);
			return shareUrl;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 多多团长商品推广转换
	 */
	public String getShareUrl(String goodsId) {
		String url = "https://api.pinduoduo.com/api/jinbaoapp/wechat_auth/coupon/generate_url?pdduid=" + user.getUid();
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("Accept", "*/*");
		heads.put("Accept-Encoding", "gzip, deflate, br");
		heads.put("Content-Type", "application/json;charset=UTF-8");
		// heads.put("X-Requested-With", "com.tencent.mm");
		heads.put("User-Agent",
				"Mozilla/5.0 (Linux; Android 7.1.2; MI 5X Build/N2G47H; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/66.0.3359.126 MQQBrowser/6.2 TBS/044904 Mobile Safari/537.36 MMWEBID/4288 MicroMessenger/7.0.6.1460(0x27000634) Process/tools NetType/WIFI Language/zh_CN");
		heads.put("Cookie", "api_uid=" + user.getApiUid());
		heads.put("AccessToken", ddjbToken);
		heads.put("Origin", "http://m.pin18pin.com");
		heads.put("Referer", "Android");
		JSONObject postData = new JSONObject(true);
		postData.fluentPut("goodsId", goodsId).fluentPut("generateShortUrl", false).fluentPut("sectionId", null)
				.fluentPut("pageId", null).fluentPut("searchId", null);
		String content = MobileHttpUrlConnectUtils.httpPost(url, postData.toJSONString(), heads, null);
		logger.info("pddms-{}: 获取推广url结果.{}", user.getUsername(), content);
		if (StringUtils.isNotBlank(content)) {
			JSONObject jsonObj = JSONObject.parseObject(content);
			if (jsonObj.getBooleanValue("success"))
				return jsonObj.getJSONObject("result").getString("weAppWebViewUrl");
		}
		return null;
	}

	public String genAliPayUrl(String orderSn) {
		try {
			String url = "https://api.pinduoduo.com/order/prepay?pdduid=" + user.getUid();
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("User-Agent", user.getUserAgent());
			heads.put("Accept-Encoding", "gzip");
			heads.put("Content-Type", "application/json;charset=UTF-8");
			heads.put("Cookie", "api_uid=" + user.getApiUid());
			heads.put("AccessToken", user.getToken());
			heads.put("ETag", "b0LzKmIm");
			heads.put("lat", "TDDLQTU2MDK3WJ54Z5UTYICXCCBN374EJ6JF5M67PGTZ3CNTSZSQ103f010");
			heads.put("PDD-CONFIG", "00102");
			heads.put("Referer", "Android");
			heads.put("X-PDD-QUERIES",
					"width=1080&height=1920&net=1&brand=xiaomi&model=MI+5X&osv=7.1.2&appv=4.33.0&pl=2");
			String returnurl = "https://m.yangkeduo.com/transac_wappay_callback.html?order_sn=" + orderSn;
			JSONObject pappay = new JSONObject(true);
			pappay.fluentPut("paid_times", 0).fluentPut("forbid_contractcode", "1").fluentPut("forbid_pappay", "1");
			JSONObject postData = new JSONObject(true);
			postData.fluentPut("pay_app_id", 9).fluentPut("version", 3).fluentPut("attribute_fields", pappay)
					.fluentPut("return_url", returnurl).fluentPut("order_sn", orderSn);
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData.toJSONString(), heads, null);
			//logger.info("pddms-{}: 生成支付宝地址结果.{}", user.getUsername(), content);
			if (StringUtils.isNotBlank(content)) {
				// https://mapi.alipay.com/gateway.do?service=alipay.wap.create.direct.pay.by.user&partner=2088911201740274&seller_id=pddzhifubao%40yiran.com&payment_type=1&notify_url=http%3A%2F%2Fpayv3.yangkeduo.com%2Fnotify%2F9&out_trade_no=XP0019090410200461997673007674&subject=%E8%AE%A2%E5%8D%95%E7%BC%96%E5%8F%B7190904-204448727833236&total_fee=5.37&return_url=https%3A%2F%2Fm.yangkeduo.com%2Ftransac_wappay_callback.html%3Forder_sn%3D190904-204448727833236&sign=a1f1zTYs%2BfGKzf68hrwCs5TNAL%2F0hOsqr7xTYkK0%2BG%2B0w2HG%2BGN7hlnJOa%2BseW%2BCMcgyCYigvwsvoIzGzyNU%2BeJs%2F5XXtvnEM22Kv7b1LZB%2B7TYpPTsIFWYi2cQACQSIyCTFnu92fBSvBaSlua4rcYlmQZzp%2FBv0X9HCW8DjpfI%3D&sign_type=RSA&goods_type=1&_input_charset=utf-8
				// https://mapi.alipay.com/gateway.do?service=alipay.wap.create.direct.pay.by.user&partner=2088911201740274&seller_id=pddzhifubao@yiran.com&payment_type=1&notify_url=http://payv3.yangkeduo.com/notify/9&out_trade_no=XP0019090410200461997673007674&subject=è®¢åç¼å·190904-204448727833236&total_fee=5.37&return_url=https://m.yangkeduo.com/transac_wappay_callback.html?order_sn=190904-204448727833236&sign=a1f1zTYs+fGKzf68hrwCs5TNAL/0hOsqr7xTYkK0+G+0w2HG+GN7hlnJOa+seW+CMcgyCYigvwsvoIzGzyNU+eJs/5XXtvnEM22Kv7b1LZB+7TYpPTsIFWYi2cQACQSIyCTFnu92fBSvBaSlua4rcYlmQZzp/Bv0X9HCW8DjpfI=&sign_type=RSA&goods_type=1&_input_charset=utf-8
				JSONObject jsonObj = JSONObject.parseObject(content);
				String aliPayUrl = jsonObj.getString("gateway_url") + "?";
				JSONObject params = jsonObj.getJSONObject("query");
				Set<String> keys = params.keySet();
				for (String key : keys) {
					aliPayUrl += key + "=" + CommonUtils.encode(params.getString(key)) + "&";
				}
				aliPayUrl = aliPayUrl.substring(0, aliPayUrl.length() - 1);
				return aliPayUrl;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		return null;
	}

	/**
	 * 秒杀商品预约
	 * 
	 * @param goodsId
	 * @param goodsName
	 * @param startDate
	 * @return
	 */
	public boolean yuyue(String goodsId, String goodsName, Date startDate) {
		String url = "https://api.yangkeduo.com/api/massage/push/add?pdduid=" + user.getUid();
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("User-Agent", user.getUserAgent());
		heads.put("Accept-Encoding", "gzip");
		heads.put("Content-Type", "application/json;charset=UTF-8");
		heads.put("AccessToken", user.getToken());
		heads.put("Cookie", "api_uid=" + user.getApiUid());
		JSONObject postData = new JSONObject(true);
		long startTime = startDate.getTime() / 1000;
		postData.fluentPut("goodsId", goodsId).fluentPut("goodsName", goodsName).fluentPut("spikeType", 0)
				.fluentPut("startTime", startTime);
		String content = MobileHttpUrlConnectUtils.httpPost(url, postData.toJSONString(), heads, null);
		logger.info("pddms-{}: 商品预约结果={}", user.getUsername(), content);
		JSONObject jsonObj = JSONObject.parseObject(content);
		if (jsonObj.getBooleanValue("success")) {
			return true;
		}
		return false;
	}

	/**
	 * 查询活动id
	 * 
	 * @param groupId
	 * @param skuId
	 * @param goodsId
	 * @return
	 */
	public JSONObject orderCheckout(String groupId, String skuId, String goodsId) {
		String url = "https://mobile.yangkeduo.com/order_checkout.html?sku_id=" + skuId + "&group_id=" + groupId
				+ "&goods_id=" + goodsId + "&goods_number=1&ts="+new Date().getTime();
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("User-Agent", user.getUserAgent());
		heads.put("Accept-Encoding", "gzip");
		heads.put("Cookie", "PDDAccessToken=" + user.getToken() + ";");
		String content = MobileHttpUrlConnectUtils.httpGet(url, heads, null);
		int start = content.indexOf("window.rawData=") + 15;
		int end = content.indexOf("}};", start) + 2;
		content = content.substring(start, end);
		JSONObject jsonObj = JSONObject.parseObject(content);
		//logger.info("pddms-{}:查询活动json={}", user.getUsername(), jsonObj);
		return jsonObj;
	}

	/**
	 * 可购买幸运人气王商品列表
	 * 
	 * @return
	 */
	public JSONObject luckyProductList() {
		String url = "https://api.pinduoduo.com/api/activity/rumbia/issues/list?page=1&size=50&sort_rule=1&pdduid="
				+ user.getUid();
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("User-Agent", user.getUserAgent());
		heads.put("Accept-Encoding", "gzip");
		heads.put("Content-Type", "application/json;charset=UTF-8");
		heads.put("AccessToken", user.getToken());
		heads.put("ETag", "b0LzKmIm");
		heads.put("PDD-CONFIG", "00102");
		heads.put("p-mode", "1");
		String content = MobileHttpUrlConnectUtils.httpGet(url, heads, null);
		// logger.info("pddms-{}:可购买幸运人气王商品列表={}",user.getUsername(), content);
		if (StringUtils.isNotBlank(content)) {
			JSONObject jsonObj = JSONObject.parseObject(content);
			return jsonObj;
		}
		return null;
	}

	/**
	 * 幸运人气王详细数据
	 * 
	 * @param issue_id
	 * @return
	 */
	public JSONObject luckyProductDetail(String issue_id) {
		String url = "https://api.pinduoduo.com/api/activity/rumbia/goods/detail?timeout=1500&issue_id=" + issue_id
				+ "&pdduid=" + user.getUid();
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("User-Agent", user.getUserAgent());
		heads.put("Accept-Encoding", "gzip");
		heads.put("Content-Type", "application/json;charset=UTF-8");
		heads.put("AccessToken", user.getToken());
		heads.put("ETag", "b0LzKmIm");
		heads.put("PDD-CONFIG", "00102");
		heads.put("p-mode", "1");
		String content = MobileHttpUrlConnectUtils.httpGet(url, heads, null);
		// logger.info("pddms-{}:幸运人气王详细数据={}",user.getUsername(), content);
		if (StringUtils.isNotBlank(content)) {
			JSONObject jsonObj = JSONObject.parseObject(content);
			return jsonObj;
		}
		return null;
	}

	/**
	 * 购买幸运人气王商品
	 * 
	 * @param issueId
	 * @param groupId
	 * @param skuId
	 * @param goodsId
	 * @return
	 */
	public JSONObject submitLuckyOrder(String issueId, String groupId, String skuId, String goodsId) {
		String url = "https://api.pinduoduo.com/api/activity/rumbia/order/create?pdduid=" + user.getUid();
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("User-Agent", user.getUserAgent());
		heads.put("Accept-Encoding", "gzip");
		heads.put("Content-Type", "application/json;charset=UTF-8");
		heads.put("AccessToken", user.getToken());
		heads.put("ETag", "b0LzKmIm");
		heads.put("PDD-CONFIG", "00102");
		heads.put("p-mode", "1");
		JSONObject postData = new JSONObject(true);
		postData.fluentPut("goods_sku_id", skuId).fluentPut("goods_sku_number", 1).fluentPut("group_id", groupId)
				.fluentPut("goods_id", goodsId).fluentPut("issue_id", issueId).fluentPut("list_site", 2)
				.fluentPut("anti_content", anti_content).fluentPut("address_id", user.getAddressId())
				.fluentPut("biz_type", 2016).fluentPut("pay_app_id", 4).fluentPut("is_app", 1);
		String content = MobileHttpUrlConnectUtils.httpPost(url, postData.toJSONString(), heads, null);
		logger.info("pddms-{}:购买幸运人气王商品下单结果={}", user.getUsername(), content);
		if (StringUtils.isNotBlank(content)) {
			JSONObject jsonObj = JSONObject.parseObject(content);
			return jsonObj;
		}
		return null;
	}

	/**
	 * 已购幸运人气王列表
	 * 
	 * @return
	 */
	public JSONObject luckyShareList() {
		String url = "https://api.pinduoduo.com/api/activity/rumbia/issues/me?pdduid=" + user.getUid();
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("User-Agent", user.getUserAgent());
		heads.put("Accept-Encoding", "gzip");
		heads.put("Content-Type", "application/json;charset=UTF-8");
		heads.put("AccessToken", user.getToken());
		heads.put("ETag", "b0LzKmIm");
		heads.put("PDD-CONFIG", "00102");
		heads.put("p-mode", "1");
		String content = MobileHttpUrlConnectUtils.httpGet(url, heads, null);
		if (StringUtils.isNotBlank(content)) {
			JSONObject jsonObj = JSONObject.parseObject(content);
			return jsonObj;
		}
		return null;
	}

	/**
	 * 幸运人气购买功后的分享
	 * 
	 * @param issueId
	 * @return
	 */
	public JSONObject luckyShare(String issueId) {
		String url = "https://api.yangkeduo.com/api/activity/rumbia/share/count?pdduid=" + user.getUid();
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("User-Agent", user.getUserAgent());
		heads.put("Accept-Encoding", "gzip");
		heads.put("Content-Type", "application/json;charset=UTF-8");
		heads.put("AccessToken", user.getToken());
		heads.put("ETag", "b0LzKmIm");
		heads.put("PDD-CONFIG", "00102");
		heads.put("p-mode", "1");
		JSONObject postData = new JSONObject(true);
		postData.fluentPut("issue_id", issueId).fluentPut("version", 2);
		String content = MobileHttpUrlConnectUtils.httpPost(url, postData.toJSONString(), heads, null);
		if (StringUtils.isNotBlank(content)) {
			JSONObject jsonObj = JSONObject.parseObject(content);
			return jsonObj;
		}
		return null;
	}

	/**
	 * 查询幸运人气可分享次数
	 * 
	 * @param issueId
	 * @return
	 */
	public JSONObject queryShareTask(String issueId) {
		String url = "https://api.pinduoduo.com/api/activity/rumbia/task/share/query?issue_id=" + issueId + "&pdduid="
				+ user.getUid();
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("User-Agent", user.getUserAgent());
		heads.put("Accept-Encoding", "gzip");
		heads.put("Content-Type", "application/json;charset=UTF-8");
		heads.put("AccessToken", user.getToken());
		heads.put("ETag", "b0LzKmIm");
		heads.put("PDD-CONFIG", "00102");
		heads.put("p-mode", "1");
		JSONObject postData = new JSONObject(true);
		postData.fluentPut("issue_id", issueId).fluentPut("version", 2);
		String content = MobileHttpUrlConnectUtils.httpPost(url, postData.toJSONString(), heads, null);
		if (StringUtils.isNotBlank(content)) {
			JSONObject jsonObj = JSONObject.parseObject(content);
			return jsonObj;
		}
		return null;
	}

	/**
	 * 完成幸运人气王分享任务
	 * 
	 * @param issueId
	 * @return
	 */
	public JSONObject luckyShareTask(String issueId) {
		String url = "https://api.pinduoduo.com/api/activity/rumbia/task/share/complete?issue_id=" + issueId
				+ "&pdduid=" + user.getUid();
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("User-Agent", user.getUserAgent());
		heads.put("Accept-Encoding", "gzip");
		heads.put("Content-Type", "application/json;charset=UTF-8");
		heads.put("AccessToken", user.getToken());
		heads.put("ETag", "b0LzKmIm");
		heads.put("PDD-CONFIG", "00102");
		heads.put("p-mode", "1");
		JSONObject postData = new JSONObject(true);
		postData.fluentPut("issue_id", issueId).fluentPut("version", 2);
		String content = MobileHttpUrlConnectUtils.httpPost(url, postData.toJSONString(), heads, null);
		if (StringUtils.isNotBlank(content)) {
			JSONObject jsonObj = JSONObject.parseObject(content);
			return jsonObj;
		}
		return null;
	}

	public JSONObject coinsCollect() {
		String url = "https://api.pinduoduo.com/api/market/peppa/coins/collect?pdduid=" + user.getUid();
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("User-Agent", user.getUserAgent());
		heads.put("Accept-Encoding", "gzip");
		heads.put("Content-Type", "application/json;charset=UTF-8");
		heads.put("AccessToken", user.getToken());
		heads.put("ETag", "b0LzKmIm");
		heads.put("PDD-CONFIG", "00102");
		heads.put("p-mode", "1");
		JSONObject postData = new JSONObject(true);
		postData.fluentPut("anti_content", anti_content);
		String content = MobileHttpUrlConnectUtils.httpPost(url, postData.toJSONString(), heads, null);
		if (StringUtils.isNotBlank(content)) {
			JSONObject jsonObj = JSONObject.parseObject(content);
			return jsonObj;
		}
		return null;
	}

	/**
	 * 订单明细 payStatus 0:待支付 2:已付款 chatStatusPrompt 待支付 待分享 待收货 待评价 orderStatus 0:待支付
	 * 待分享 1:待收货 待评价 2:交易已取消
	 * 
	 * @param orderSn
	 * @return
	 */
	public JSONObject queryOrderDetail(String orderSn) {
		try {
			String url = "https://mobile.yangkeduo.com/order.html?order_sn=" + orderSn
					+ "&refer_page_name=my_order&_x_src=homepop&_x_platform=wechat";
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("User-Agent", user.getUserAgent());
			heads.put("Accept-Encoding", "gzip");
			heads.put("Cookie", "PDDAccessToken=" + user.getToken() + ";");
			String content = MobileHttpUrlConnectUtils.httpGet(url, heads, null);
			int start = content.indexOf("window.rawData=") + 15;
			int end = content.indexOf("}};", start) + 2;
			content = content.substring(start, end);
			JSONObject jsonObj = JSONObject.parseObject(content);
			// logger.info("pddms-{}:orderSn={},订单明细={}", user.getUsername(), orderSn,
			// jsonObj);
			return jsonObj.getJSONObject("data");
		} catch (Exception e) {
			return null;
		}
	}

	public boolean isSign(JSONObject order) {
		JSONObject express = order.getJSONObject("expressInfo");
		if (express != null) {
			JSONArray traces = express.getJSONArray("traces");
			for (int i = 0; i < traces.size(); i++) {
				JSONObject trace = traces.getJSONObject(i);
				if (trace.getString("status").equals("SIGN") || trace.getString("status").equals("SIGN_ON_BEHALF"))
					return true;
			}
		}
		return false;
	}

	public boolean isPayed(String orderSn) {
		try {
			JSONObject order = queryOrderDetail(orderSn);
			if (order.getIntValue("payStatus") == 2) {
				return true;
			} else
				return false;
		} catch (Exception e) {
			return false;
		}

	}

	/**
	 * 我的评价列表
	 * 
	 * @param orderSn
	 * @return
	 */
	public JSONObject myComments() {
		String url = "https://mobile.yangkeduo.com/my_comments.html";
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("User-Agent", user.getUserAgent());
		heads.put("Accept-Encoding", "gzip");
		heads.put("Cookie", "PDDAccessToken=" + user.getToken() + ";");
		String content = MobileHttpUrlConnectUtils.httpGet(url, heads, null);
		int start = content.indexOf("window.rawData=") + 15;
		int end = content.indexOf("}};", start) + 2;
		content = content.substring(start, end);
		JSONObject jsonObj = JSONObject.parseObject(content);
		//logger.info("pddms-{}:我的评价列表={}", user.getUsername(), jsonObj);
		return jsonObj;
	}
	
	public JSONObject myComments(int page, String lastReviewId, String lastCreateTime) {
		String url = "https://mobile.yangkeduo.com/proxy/api/api/engels/review/my?pdduid="+user.getUid()+"&page="+page+"&size=10&last_review_id="+lastReviewId+"&last_create_time="+lastCreateTime;
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("User-Agent", user.getUserAgent());
		heads.put("Accept", "application/json, text/plain, */*");
		heads.put("Accept-Encoding", "gzip, deflate, br");
		heads.put("Cookie", "PDDAccessToken=" + user.getToken() + ";");
		heads.put("AccessToken", user.getToken());
		String content = MobileHttpUrlConnectUtils.httpGet(url, heads, null);
		JSONObject jsonObj = JSONObject.parseObject(content);
		//logger.info("pddms-{}:我的评价列表={}", user.getUsername(), jsonObj);
		return jsonObj;
	}

	/**
	 * 
	 * 确认收货
	 */
	public JSONObject confirmReciveGoods(String orderSn) {
		// SIGN_ON_BEHALF 暂存 /SIGN已签收 SEND 送货
		String url = "https://api.pinduoduo.com/order/" + orderSn + "/received?pdduid=" + user.getUid() + "&is_back=1";
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("User-Agent", user.getUserAgent());
		heads.put("Accept", "*/*");
		heads.put("Accept-Encoding", "gzip");
		heads.put("Content-Type", "application/json;charset=UTF-8");
		heads.put("Cookie", "api_uid=" + user.getApiUid());
		heads.put("AccessToken", user.getToken());
		heads.put("ETag", "b0LzKmIm");
		heads.put("lat", "TDDLQTU2MDK3WJ54Z5UTYICXCCBN374EJ6JF5M67PGTZ3CNTSZSQ103f010");
		heads.put("PDD-CONFIG", "00102");
		heads.put("Referer", "Android");
		heads.put("X-PDD-QUERIES", "width=1080&height=1920&net=1&brand=xiaomi&model=MI+5X&osv=7.1.2&appv=4.33.0&pl=2");
		JSONArray goods = new JSONArray();
		JSONObject goods01 = new JSONObject(true);
		goods.add(goods01);
		JSONObject postData = new JSONObject(true);
		postData.fluentPut("anti_content", "");
		String content = MobileHttpUrlConnectUtils.httpPost(url, postData.toJSONString(), heads, null);
		logger.info("pddms-{}: 确认收货结果.{}", user.getUsername(), content);
		if (StringUtils.isNotBlank(content)) {
			// {"server_time":1567419492,"quantity":null,"nickname":"177****7870","share_code":null}
			JSONObject jsonObj = JSONObject.parseObject(content);
			return jsonObj;
		}
		return null;
	}

	/**
	 * 评价
	 * 
	 * @param activityId
	 * @param groupId
	 * @param skuId
	 * @param goodsId
	 * @return
	 */
	public JSONObject submitComment(String goodsId, String orderSn, String comment, String[] imageUrls) {
		// SIGN_ON_BEHALF 暂存 /SIGN已签收 SEND 送货

		JSONArray pictures = new JSONArray();
		if (imageUrls != null) {
			for (String imageUrl : imageUrls) {
				JSONObject uploadResult = uploadPic(imageUrl);
				if (uploadResult == null)
					return null;
				pictures.add(uploadResult);
			}
		}
		String url = "https://mobile.yangkeduo.com/proxy/api/v2/order/goods/review?pdduid=" + user.getUid();
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("User-Agent", user.getUserAgent());
		heads.put("Accept", "application/json, text/plain, */*");
		heads.put("Accept-Encoding", "gzip");
		heads.put("Content-Type", "application/json;charset=UTF-8");
		heads.put("Cookie", "api_uid=" + user.getApiUid());
		heads.put("AccessToken", user.getToken());
		heads.put("ETag", "b0LzKmIm");
		heads.put("lat", "TDDLQTU2MDK3WJ54Z5UTYICXCCBN374EJ6JF5M67PGTZ3CNTSZSQ103f010");
		heads.put("PDD-CONFIG", "00102");
		heads.put("Referer", "Android");
		heads.put("X-PDD-QUERIES", "width=1080&height=1920&net=1&brand=xiaomi&model=MI+5X&osv=7.1.2&appv=4.33.0&pl=2");
		JSONArray goods = new JSONArray();
		JSONObject goods01 = new JSONObject(true);
		goods.add(goods01);
		JSONObject postData = new JSONObject(true);
		postData.fluentPut("goods_id", goodsId).fluentPut("order_sn", orderSn).fluentPut("desc_score", 5)
				.fluentPut("logistics_score", 5).fluentPut("service_score", 5).fluentPut("comment", comment)
				.fluentPut("pictures", pictures).fluentPut("labels", new JSONArray()).fluentPut("anonymous", 0)
				.fluentPut("anti_content", "");
		String content = MobileHttpUrlConnectUtils.httpPost(url, postData.toJSONString(), heads, null);
		logger.info("pddms-{}: 提交评论结果.{}", user.getUsername(), content);
		if (StringUtils.isNotBlank(content)) {
			// {"review_id":"241849880151973085","rating_id":241849880151973085,"masked_comment":"给个好评，给个好评，给个好评，给个好评"}
			JSONObject jsonObj = JSONObject.parseObject(content);
			return jsonObj;
		}
		return null;
	}

	/**
	 * 上传评论照片前的签名
	 * 
	 * @param content
	 * @param imageUrl
	 * @return
	 */
	public String signature() {
		String url = "https://mobile.yangkeduo.com/proxy/api/image/signature?" + user.getUid();
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("User-Agent", user.getUserAgent());
		heads.put("Accept-Encoding", "gzip");
		heads.put("Content-Type", "application/json;charset=UTF-8");
		heads.put("Cookie", "api_uid=" + user.getApiUid());
		heads.put("AccessToken", user.getToken());
		heads.put("ETag", "b0LzKmIm");
		heads.put("lat", "TDDLQTU2MDK3WJ54Z5UTYICXCCBN374EJ6JF5M67PGTZ3CNTSZSQ103f010");
		heads.put("PDD-CONFIG", "00102");
		heads.put("Referer", "Android");
		heads.put("X-PDD-QUERIES", "width=1080&height=1920&net=1&brand=xiaomi&model=MI+5X&osv=7.1.2&appv=4.33.0&pl=2");
		JSONArray goods = new JSONArray();
		JSONObject goods01 = new JSONObject(true);
		goods.add(goods01);
		JSONObject postData = new JSONObject(true);
		postData.fluentPut("bucket_tag", "review_image");
		String content = MobileHttpUrlConnectUtils.httpPost(url, postData.toJSONString(), heads, null);
		logger.info("pdd-{}: 签名结果.{}", user.getUsername(), content);
		// {"signature":"26d0d8efc73e31d30b0f3f1cb70cd30ae883f97d"}
		if (StringUtils.isNotBlank(content)) {
			JSONObject jsonObj = JSONObject.parseObject(content);
			return jsonObj.getString("signature");
		}
		return null;
	}

	/**
	 * 评论上传图片
	 * 
	 * @param imageUrl
	 * @return
	 */
	public JSONObject uploadPic(String imageUrl) {
		String sign = signature();
		if (StringUtils.isBlank(sign)) {
			logger.error("pdd-{}: 获取签名失败", user.getUsername());
			return null;
		}

		String base64Code = "";
		try {
			base64Code = "data:image/jpeg;base64," + FileUtils.getBase64CodeByCommpass(imageUrl, 900, 1200, 0.7f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (StringUtils.isBlank(sign)) {
			logger.error("pdd-{}: base64加密失败!", user.getUsername());
			return null;
		}
		String url = "https://file.yangkeduo.com/v2/store_image";
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("User-Agent", user.getUserAgent());
		heads.put("Accept-Encoding", "gzip");
		heads.put("Content-Type", "application/json;charset=UTF-8");
		heads.put("Cookie", "api_uid=" + user.getApiUid());
		heads.put("AccessToken", user.getToken());
		heads.put("ETag", "b0LzKmIm");
		heads.put("lat", "TDDLQTU2MDK3WJ54Z5UTYICXCCBN374EJ6JF5M67PGTZ3CNTSZSQ103f010");
		heads.put("PDD-CONFIG", "00102");
		heads.put("Referer", "Android");
		heads.put("X-PDD-QUERIES", "width=1080&height=1920&net=1&brand=xiaomi&model=MI+5X&osv=7.1.2&appv=4.33.0&pl=2");
		JSONArray goods = new JSONArray();
		JSONObject goods01 = new JSONObject(true);
		goods.add(goods01);
		JSONObject postData = new JSONObject(true);
		postData.fluentPut("upload_sign", sign).fluentPut("image", base64Code);
		String content = MobileHttpUrlConnectUtils.httpPost(url, postData.toJSONString(), heads, null);
		logger.info("pdd-{}: 上传图片结果.{}", user.getUsername(), content);
		// {"url":"https://t22img.yangkeduo.com/review3/review/2019-08-27/1bf508e0c9440664417cc16eb5748189.jpeg","width":900,"height":640}
		if (StringUtils.isNotBlank(content)) {
			JSONObject jsonObj = JSONObject.parseObject(content);
			return jsonObj;
		}
		return null;
	}

	/**
	 * 下载评价照片 用html2image转换 成功返回空字符串，失败返回原因
	 * 
	 * @param orderSn
	 */
	public String downloadCommentImage(String orderSn, String pddImgPath) {
		try {
			String orginImg = pddImgPath + File.separator + orderSn + ".png";
			if (new File(orginImg).exists()) {
				return "";
			}
			JSONObject json = myComments();
			JSONArray myComments = json.getJSONObject("store").getJSONArray("list");
			for (int i = 0; i < myComments.size(); i++) {
				JSONObject myComment = myComments.getJSONObject(i);
				JSONObject order = myComment.getJSONObject("order_info");
				if (orderSn.equals(order.getString("order_sn"))) {
					Map<String, Object> dataMap = new HashMap<String, Object>();
					dataMap.put("userImg", myComment.getString("avatar"));
					String pjTime = DateUtils.formatDate(new Date(myComment.getLongValue("time") * 1000), "yyyy.MM.dd");
					dataMap.put("pjTime", pjTime);
					List<String> pjImageUrls = new ArrayList<String>();
					JSONArray pictures = myComment.getJSONArray("pictures");
					int maxPicNum = pictures == null ? 0 : pictures.size() < 3 ? pictures.size() : 3;
					for (int j = 0; j < maxPicNum; j++) {
						JSONObject picture = pictures.getJSONObject(j);
						pjImageUrls.add(picture.getString("url"));
					}
					dataMap.put("pjImgs", pjImageUrls);
					dataMap.put("goodsImg", order.getString("goods_picture"));
					String goodsName = order.getString("goods_name");
					String goodsName01 = "";
					String goodsName02 = "";
					if (goodsName.length() > 38) {
						goodsName01 = goodsName.substring(0, 19);
						goodsName02 = goodsName.substring(19, 38);
					} else if (goodsName.length() > 19) {
						goodsName01 = goodsName.substring(0, 19);
						goodsName02 = goodsName.substring(19, goodsName.length());
					} else {
						goodsName01 = goodsName;
					}
					dataMap.put("goodsName01", goodsName01);// 19
					if (StringUtils.isNotBlank(goodsName02)) {
						dataMap.put("goodsName02", goodsName02);
					}
					JSONArray specs = JSONArray.parseArray(myComment.getString("specs"));
					String spec = "";
					for (int m = 0; m < specs.size(); m++) {
						spec += specs.getJSONObject(m).getString("spec_key") + ":"
								+ specs.getJSONObject(m).getString("spec_value");
					}
					String spec01 = "";
					String spec02 = "";
					if (spec.length() > 30) {
						spec01 = spec.substring(0, 15);
						spec02 = spec.substring(15, 30);
					} else if (spec.length() > 15) {
						spec01 = spec.substring(0, 15);
						spec02 = spec.substring(15, spec.length());
					} else {
						spec01 = spec;
					}
					dataMap.put("spec01", spec01);// 15
					dataMap.put("spec02", spec02);
					dataMap.put("orderAmount", order.getFloat("order_amount") / 100);
					String comment = myComment.getString("comment");
					String comment01 = "";
					String comment02 = "";
					String comment03 = "";
					if (comment.length() > 72) {
						comment01 = comment.substring(0, 24);
						comment02 = comment.substring(24, 48);
						comment03 = comment.substring(48, 72);
					} else if (comment.length() > 48) {
						comment01 = comment.substring(0, 24);
						comment02 = comment.substring(24, 48);
						comment03 = comment.substring(48, comment.length());
					} else if (comment.length() > 24) {
						comment01 = comment.substring(0, 24);
						comment02 = comment.substring(24, comment.length());
					} else {
						comment01 = comment;
					}
					dataMap.put("comment01", comment01);// 15
					dataMap.put("comment02", comment02);
					dataMap.put("comment03", comment03);
					String html = FreemarkerUtils.loadTemplate("pj.ftl", dataMap);
					FileUtils.htmlToImage(html, 900, orginImg);
					logger.info("pdd-{}:{}-下载评论图片成功.", user.getUsername(), orderSn);
					return "";
				}
			}
			logger.info("pdd-{}:{}-没有找到相关评论，无法下载评论图片.", user.getUsername(), orderSn);
			return "没有找到相关评论，无法下载评论图片!";
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("pdd-{}:{}-下载评论图片失败，发生异常!", user.getUsername(), orderSn);
			return "下载评论图片失败，发生异常!";
		}
	}

	public JSONArray searchProduct(String goodsName) {
		try {
			String url = "https://mobile.yangkeduo.com/search_result.html?search_key=" + CommonUtils.encode(goodsName)
					+ "&search_src=new&search_met=btn_sort&search_met_track=manual&refer_page_name=search_result";
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
			heads.put("Accept-Encoding", "gzip, deflate, br");
			heads.put("Accept-Language", "zh-CN,zh;q=0.9");
			heads.put("User-Agent", user.getUserAgent());
			heads.put("Cookie", "PDDAccessToken=" + user.getToken() + ";");
			heads.put("Upgrade-Insecure-Requests", "1");
			String content = MobileHttpUrlConnectUtils.httpGet(url, heads, null);
			Document doc = Jsoup.parse(content);
			content = doc.getElementById("__NEXT_DATA__").html();
			JSONObject jsonObj = JSONObject.parseObject(content);
			//logger.info("pddms-{}:搜索结果={}", user.getUsername(), jsonObj);
			return jsonObj.getJSONObject("props").getJSONObject("pageProps").getJSONObject("data")
					.getJSONObject("ssrListData").getJSONArray("list");
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 收藏商品
	 */
	public boolean scGoods(String goodsId) {
		String url = "https://mobile.yangkeduo.com/proxy/api/v2/favorite/like/" + goodsId + "?pdduid=" + user.getUid();
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("User-Agent", user.getUserAgent());
		heads.put("Accept", "*/*");
		heads.put("Accept-Encoding", "gzip");
		heads.put("Content-Type", "application/json;charset=UTF-8");
		heads.put("Cookie", "api_uid=" + user.getApiUid());
		heads.put("AccessToken", user.getToken());
		JSONObject postData = new JSONObject(true);
		postData.fluentPut("coupon_status", 1).fluentPut("like_from", 35);
		String content = MobileHttpUrlConnectUtils.httpPost(url, postData.toJSONString(), heads, null);
		logger.info("pdd-{}: 收藏商品结果.{}", user.getUsername(), content);
		if (StringUtils.isNotBlank(content)) {
			// {"time":1568622264,"server_time":1568622264}
			return true;
		}
		return false;
	}

	/**
	 * 收藏店铺
	 */
	public boolean scMaller(String mallerId) {
		String url = "https://mobile.yangkeduo.com/proxy/api/favorite/mall/like/" + mallerId + "?pdduid="
				+ user.getUid();
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("User-Agent", user.getUserAgent());
		heads.put("Accept", "*/*");
		heads.put("Accept-Encoding", "gzip");
		heads.put("Content-Type", "application/json;charset=UTF-8");
		heads.put("Cookie", "api_uid=" + user.getApiUid());
		heads.put("AccessToken", user.getToken());
		JSONObject postData = new JSONObject(true);
		postData.fluentPut("like_from", 10014);
		String content = MobileHttpUrlConnectUtils.httpPost(url, postData.toJSONString(), heads, null);
		logger.info("pdd-{}: 收藏店铺结果.{}", user.getUsername(), content);
		if (StringUtils.isNotBlank(content)) {
			// {"time":1568622264,"server_time":1568622264}
			return true;
		}
		return false;
	}

	public JSONArray searchMall(String mallName) {
		try {
			String url = "https://mobile.yangkeduo.com/search_result.html?search_type=mall&search_key="
					+ CommonUtils.encode(mallName);
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
			heads.put("Accept-Encoding", "gzip, deflate, br");
			heads.put("Accept-Language", "zh-CN,zh;q=0.9");
			heads.put("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36");
			heads.put("Cookie", "PDDAccessToken=" + user.getToken() + ";");
			heads.put("Upgrade-Insecure-Requests", "1");
			String content = MobileHttpUrlConnectUtils.httpGet(url, heads, null);
			Document doc = Jsoup.parse(content);
			content = doc.getElementById("__NEXT_DATA__").html();
			JSONObject jsonObj = JSONObject.parseObject(content);
			//logger.info("pdd-{}:搜索结果={}", user.getUsername(), jsonObj);
			return jsonObj.getJSONObject("props").getJSONObject("pageProps").getJSONObject("data")
					.getJSONArray("mallList");
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 店铺明细
	 */
	public JSONObject mallDetail(String mallId) {
		try {
			String url = "https://mobile.yangkeduo.com/mall_page.html?mall_id=" + mallId;
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
			heads.put("Accept-Encoding", "gzip, deflate, br");
			heads.put("Accept-Language", "zh-CN,zh;q=0.9");
			heads.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36");
			heads.put("Cookie", "PDDAccessToken=" + user.getToken() + ";");
			heads.put("Upgrade-Insecure-Requests", "1");
			String content = MobileHttpUrlConnectUtils.httpGet(url, heads, null);
			//logger.info("pddms-{}: 查询店铺详细.{}", user.getUsername(), content);
			int start = content.indexOf("window.rawData=") + 15;
			int end = content.indexOf("}};", start) + 2;
			content = content.substring(start, end);
			if (StringUtils.isNotBlank(content)) {
				JSONObject jsonObj = JSONObject.parseObject(content);
				return jsonObj;
			}
		} catch (Exception e) {
			return null;
		}
		return null;
	}
	
	/**
	 * 查询店铺id,直接通过店铺名称查不到的通过这个方法来查
	 * @param goodsName  不完整商品名称
	 * @param sellerName 完整店铺名称
	 * @return
	 */
	public String searchMallId(String goodsName, String sellerName) {
		JSONArray products = searchProduct(goodsName);
		JSONObject product = null;
		String goodsId = "", mallName = "";
		for (int i = 0; i < products.size(); i++) {
			product = products.getJSONObject(i);
			mallName = product.getString("mallName");
			goodsId = product.getString("goodsID");
			if (StringUtils.isNotBlank(mallName)) {
				if (mallName.equals(sellerName) && goodsName.equals(product.getString("goodsName"))) {
					return goodsId;
				} else
					continue;
			} else {
				JSONObject goods = productChromeDetail(goodsId);
				mallName = goods.getJSONObject("store").getJSONObject("initDataObj").getJSONObject("mall")
						.getString("mallName");
				System.out.println(mallName);
				if (mallName.equals(sellerName)) {
					String mallId = goods.getJSONObject("store").getJSONObject("initDataObj").getJSONObject("mall").getString("mallID");
					logger.info("pdd-{}:成功查找到商品.goodsId={}|mallId={}", user.getUsername(), goodsId, mallId);
					return mallId;
				}
			}
		}
		return null;
	}


	/**
	 * 通过商品标题和店铺名称搜索商品
	 * 
	 * @param goodsName
	 *            完整的商品名称
	 * @param sellerName
	 *            完整的店铺名称
	 * @return
	 */
	public String searchGoodsId(String goodsName, String sellerName) {
		JSONArray products = searchProduct(goodsName);
		JSONObject product = null;
		String goodsId = "", mallName = "";
		for (int i = 0; i < products.size(); i++) {
			product = products.getJSONObject(i);
			mallName = product.getString("mallName");
			goodsId = product.getString("goodsID");
			if (StringUtils.isNotBlank(mallName)) {
				if (mallName.equals(sellerName) && goodsName.equals(product.getString("goodsName"))) {
					return goodsId;
				} else
					continue;
			} else {
				JSONObject goods = productChromeDetail(goodsId);
				mallName = goods.getJSONObject("store").getJSONObject("initDataObj").getJSONObject("mall")
						.getString("mallName");
				String gName = goods.getJSONObject("store").getJSONObject("initDataObj").getJSONObject("goods")
						.getString("goodsName");
				logger.info("pdd-{}:mallName={}", user.getUsername(), mallName);
				if (mallName.equals(sellerName) && goodsName.equals(gName)) {
					String mallId = goods.getJSONObject("store").getJSONObject("initDataObj").getJSONObject("mall").getString("mallID");
					System.out.println("mallId="+mallId);
					logger.info("pdd-{}:成功查找到商品.goodsId={}|mallId={}", user.getUsername(), goodsId, mallId);
					return goodsId;
				}
			}
		}
		return null;
	}

	/**
	 * 通过商品标题和店铺名称搜索商品
	 * 
	 * @param mallName
	 *            完整的店铺名称
	 * @param goodsImg
	 *            商品图片
	 * @return
	 */
	public String searchGoodsIdByMallNameAndGoodImg(String mallName, String goodsImg) {
		JSONArray mallList = searchMall(mallName);
		if (mallList == null)
			return null;
		String mallId = "";
		for (int i = 0; i < mallList.size(); i++) {
			JSONObject mall = mallList.getJSONObject(i);
			if (mallName.equals(mall.getJSONArray("displayItems1").getJSONObject(0).getString("text"))) {
				mallId = mall.getString("mallId");
				break;
			}
		}
		if (StringUtils.isBlank(mallId))
			return "";
		JSONObject mallDetail = mallDetail(mallId);
		JSONArray products = mallDetail.getJSONObject("store").getJSONObject("mallGroupInfo")
				.getJSONObject("groupResult").getJSONArray("result");
		for (int i = 0; i < products.size(); i++) {
			JSONObject goods = products.getJSONObject(i);
			String goodsId = goods.getString("goodsId");
			JSONObject product = productDetail(goodsId);
			if (product.toString().contains(goodsImg))
				return goodsId;
		}
		return null;
	}

	/**
	 * @param mallId 商家id
	 * @param goodsImg 商品名称
	 * @return
	 */
	public String searchGoodsIdByMallId(String mallId, String goodsImg) {
		JSONObject mallDetail = mallDetail(mallId);
		JSONArray products = mallDetail.getJSONObject("store").getJSONObject("mallGroupInfo")
				.getJSONObject("groupResult").getJSONArray("result");
		for (int i = 0; i < products.size(); i++) {
			JSONObject goods = products.getJSONObject(i);
			String goodsId = goods.getString("goodsId");
			JSONObject product = productDetail(goodsId);
			if (product.toString().contains(goodsImg))
				return goodsId;
		}
		return null;
	}

	/**
	 * 搜索店铺，自动提示
	 */
	public String searchSuggestMall(String mallName, String mallImg) {
		try {
			String url = "https://mobile.yangkeduo.com/proxy/api/search_suggest?query=" + CommonUtils.encode(mallName)
					+ "&plat=H5&pdduid=" + user.getUid() + "&is_back=1";
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
			heads.put("Accept-Encoding", "gzip, deflate, br");
			heads.put("Accept-Language", "zh-CN,zh;q=0.9");
			heads.put("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36");
			heads.put("Cookie", "PDDAccessToken=" + user.getToken() + ";");
			heads.put("Upgrade-Insecure-Requests", "1");
			String content = MobileHttpUrlConnectUtils.httpGet(url, heads, null);
			JSONObject jsonObj = JSONObject.parseObject(content);
			JSONArray suggestList = jsonObj.getJSONArray("suggest_list");
			for (int i = 0; i < suggestList.size(); i++) {
				JSONObject mall = suggestList.getJSONObject(i).getJSONObject("item_data").getJSONObject("mall_head");
				if (mall.getString("mall_logo").contains(mallImg)) {
					return mall.getString("mall_id");
				}
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 搜索店铺id
	 * @param mallName 店铺名称
	 * @param mallImg 图片
	 * @return
	 */
	public String searchMall(String mallName, String mallImg) {
		String mallId = searchSuggestMall(mallName, mallImg);
		if (StringUtils.isNotBlank(mallId))
			return mallId;
		JSONArray mallList = searchMall(mallName);
		if (mallList == null)
			return null;
		for (int i = 0; i < mallList.size(); i++) {
			JSONObject mall = mallList.getJSONObject(i);
			if (mall.getString("mallLogo").contains(mallImg)) {
				mallId = mall.getString("mallId");
				return mallId;
			}
		}
		if (StringUtils.isBlank(mallId))
			return "";
		return mallId;
	}

}
