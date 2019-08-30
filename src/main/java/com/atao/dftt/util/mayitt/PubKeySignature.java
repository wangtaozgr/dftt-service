package com.atao.dftt.util.mayitt;

public class PubKeySignature {
	// PubKeySignature.a
	/**
	 * 打包一个不同包名的apk，PubKeySignature.a中包名设成com.ldzs.zhangxin，获取到公钥为2KEJ0tWKbKWqr2aSAOASljXdSqqBhQIDAQAB
	 */
	public static String publicKey = "2KEJ0tWKbKWqr2aSAOASljXdSqqBhQIDAQAB";
	public static String a(char arg3) {
		String v2 = publicKey.substring(0, publicKey.length() - arg3 % 10);
		return v2;
	}

	public static String a(String s) {
		char v0 = RandomValidateCode.a();
		String v6 = SecurityHelper.c(PubKeySignature.a(v0), s);
		//String v6 = SecurityHelper.c("PAT-cpkdmd-brjigshd_ox1bav7p", s);
		return RandomValidateCode.a(v6, v0);
	}
	
	public static void main(String[] args) {
		//a("channel=c1001&device_type=2&sm_device_id=20181224211015c3dd055d19d7fe76a487e2520bfe9bb6017d6f4d715feba1&access=WIFI&version_code=535&request_time=1545792747&sign=5fc2eec01b9a19e4f3531e92cf8887ba");
		String s = a("channel=c1001&device_type=2&device_id=2064239&sm_device_id=20181224211015c3dd055d19d7fe76a487e2520bfe9bb6017d6f4d715feba1&access=WIFI&version_code=535&request_time=1545979048&sign=406b71fc1a936c5b268c4ac1f4597768");
		System.out.println(s);
	}
}
