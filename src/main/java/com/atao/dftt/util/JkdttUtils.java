package com.atao.dftt.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.Key;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import com.alibaba.fastjson.JSONObject;
import com.atao.dftt.model.JkdttUser;
import com.atao.dftt.util.jkdtt.b;

public class JkdttUtils {
	public static Random random = new Random();

	public static int appversioncode = 597;
	public static String appversion = "5.9.7";
	public static String appversiontoken = "32";
	public static String apptoken = "xzwltoken070704";
	public static String appid = "xzwl";
	public static String sdktype = "bd_jssdk;bd_sdk;gdt_sdk;tt_sdk;sg_sdk;gdt_api;tt_api;zk_api;dk_api";
	public static String[] adtitles = new String[] { "百鬼夜行抄", "终于可以在手机上玩这游戏了", "无限制自由刷本", "各种道具靠打", "近视做激光手术",
			"做完激光手术眼还会近视吗", "尿尿刺痛怎么办", "失之毫厘", "出出律律", "尼卡伊塞", "徐传统", "移船就岸", "搠笔巡街", "呆", "山梨醇酐三硬脂酸酯", "威尼斯", "皮特",
			"箪食瓢浆", "比拉马运动", "握拳透掌", "说千说万", "维尼斯", "基希舍夫", "代你发梦", "广式月饼", "像梦一样自由", "黑糖放电", "什袭珍藏", "潘杨之睦", "悬车告老",
			"风驰云卷", "汪永益", "意前笔后", "金鱼鸭掌", "底盘前悬高度", "放心", "麻圆", "荐贤举能", "明鉴高悬", "惠然之顾", "端砚品式", "风尘之警", "心如刀锉",
			"不敢把回忆去细细的看", "莎呦娜啦", "人字梯", "今宵久久", "约翰奥比米克尔", "激扬清浊", "时来铁似金", "积玉堆金", "椎锋陷陈", "魂消魄夺", "路帝", "勇敢说不",
			"鳞叶番杏属", "红当当飞吻", "一代儒宗", "一代儿", "一代国色", "一代女皇", "一代宗匠", "一代宗工", "跟我来", "鲁莉娅", "铁扒仔鸡", "心中的故乡", "一年景",
			"一床两好", "一底一面", "一座尽倾", "一座尽惊", "一建", "梁山伯与茱丽叶", "山荆子", "真味珍", "三色堇", "一泓清水", "一泡子", "一波未平﹐一波又起", "一注",
			"一泻汪洋", "一泼滩", "如果说离开", "最后的要求", "摇摆女郎", "豹皮花属", "一言立信", "一言而喻", "一言而定", "一言订交", "一言诗", "一言赖语", "让我罩着你",
			"让爱在灿烂中死去", "心路", "隔断", "丁庄村委会", "丁庄街道", "丁庄镇", "丁店", "丁店镇", "丁庸", "三角装", "知心一个", "我曾经爱过一个女孩", "栗子鸡", "七姐妹",
			"七姑子", "七娘寨村委会", "七娘石", "七子", "七子八壻", "在遥远的地方", "希望的种子", "小麦赤霉病", "七卒", "七苏木村", "七莘路", "七莘路七号桥", "七莘路五号桥",
			"七莘路十号桥", "七菱八落", "五轮仪", "川味牛肉", "加一些想象", "情人的眼泪" };

	public static void main(String[] args) throws IOException {
		JkdttUser user = new JkdttUser();
		user.setOpenId("a84b215f392644b282a31aab020be1c3");
		user.setX("EfSVvMxVcNwqoE7agij");
		System.out.println(getPwd("wang2710"));
		String spRequestTokenKey = JkdttUtils.getSpRequestTokenKey("W8gLX661MY3NoOT2JLbgiJQEFEFpHARF%250A", "a84b215f392644b282a31aab020be1c3");
		System.out.println(spRequestTokenKey);

	}

	public static JSONObject initPars(JkdttUser user, String ad_name, String ad_type_name, String adid, String ads_name,
			String advpositionid, String appid, String image_type, String import_type, String title) {
		JSONObject json = new JSONObject(true);
		json.fluentPut("ad_name", ad_name).fluentPut("ad_type_name", ad_type_name).fluentPut("adid", adid)
				.fluentPut("ads_name", ads_name).fluentPut("advdesc", title).fluentPut("advpositionid", advpositionid)
				.fluentPut("appid", appid).fluentPut("image_type", image_type).fluentPut("import_type", import_type)
				.fluentPut("openid", user.getOpenId()).fluentPut("source", title).fluentPut("title", title);
		return json;
	}

	public static String getPwd(String pwd) {
		String v0_1 = CommonUtils.encode(CommonUtils.encode(encodePwd(pwd)));
		return v0_1;
	}

	public static String encodePwd(String arg4) {
		String v0_5 = "";
		byte[] v0_4;
		if (arg4 == null) {
			try {
				arg4 = "";
			} catch (Throwable v0) {
				return null;
			}
		}
		try {
			SecureRandom v0 = new SecureRandom();
			SecretKey v1 = SecretKeyFactory.getInstance("DESede")
					.generateSecret(new DESedeKeySpec("com.xiangzi.beizhuan.deckey".getBytes()));
			Cipher ENCRYPT_MODE_CIPHER = Cipher.getInstance("DESede/CBC/PKCS5Padding");
			IvParameterSpec v2 = new IvParameterSpec("cryptpwd".getBytes());
			ENCRYPT_MODE_CIPHER.init(1, ((Key) v1), ((AlgorithmParameterSpec) v2), v0);
			v0_4 = ENCRYPT_MODE_CIPHER.doFinal(arg4.getBytes("utf-8"));
			v0_5 = new b().encode(v0_4);
		} catch (Exception v0) {
			return null;
		}
		return v0_5;
	}

	public static String getTitle() {
		int len = adtitles.length;
		return adtitles[random.nextInt(len)];
	}

	public static String getSpRequestTokenKey(String skey, String openId) {
		String key = CommonUtils.decode(CommonUtils.decode(skey));
		try {
			return decode(key, openId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getToken(JkdttUser user) {
		String v1;
		String v2 = user.getOpenId() + "_" + System.currentTimeMillis() / 1000;
		try {
			v1 = m(a(v2.getBytes(), user.getX(), new byte[] { 100, 49, 99, 100, 51, 51, 49, 101 }));
			return cP(cP(v1));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String cP(String arg2) {
		String v0 = "";
		try {
			v0 = URLEncoder.encode(arg2, "utf-8");
		} catch (UnsupportedEncodingException v1) {
			v1.printStackTrace();
		}
		return v0;
	}

	public static String encode(String arg2, String arg3) throws Exception {
		return m(a(arg2.getBytes(), arg3, qh()));
	}

	public static String decode(String arg2, String arg3) throws Exception {
		return new String(b(cU(arg2), arg3, qh()));
	}

	public static byte[] cU(String arg1) throws Exception {
		return new com.atao.dftt.util.jkdtt.a().aA(arg1);
	}

	public static byte[] b(byte[] arg4, String arg5, byte[] arg6) throws Exception {
		Key v0 = cV(arg5);
		PBEParameterSpec v1 = new PBEParameterSpec(arg6, 100);
		Cipher v2 = Cipher.getInstance("PBEWITHMD5andDES");
		v2.init(2, v0, ((AlgorithmParameterSpec) v1));
		return v2.doFinal(arg4);
	}

	public static byte[] qh() throws Exception {
		return new byte[] { 100, 49, 99, 100, 51, 51, 49, 101 };
	}

	public static String m(byte[] arg1) throws Exception {
		return new b().l(arg1);
	}

	public static byte[] a(byte[] arg1, String arg5, byte[] arg6) throws Exception {
		Key v0 = cV(arg5);
		PBEParameterSpec v1 = new PBEParameterSpec(arg6, 100);
		Cipher v2 = Cipher.getInstance("PBEWITHMD5andDES");
		v2.init(1, v0, ((AlgorithmParameterSpec) v1));
		return v2.doFinal(arg1);
	}

	private static Key cV(String arg2) throws Exception {
		return SecretKeyFactory.getInstance("PBEWITHMD5andDES").generateSecret(new PBEKeySpec(arg2.toCharArray()));
	}
}
