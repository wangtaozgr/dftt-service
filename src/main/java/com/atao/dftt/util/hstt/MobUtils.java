package com.atao.dftt.util.hstt;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.Random;
import java.util.zip.GZIPOutputStream;

import com.atao.dftt.util.AndroidBase64;

public class MobUtils {
	public static Random random = new Random();

	public static String encode(String arg7) throws Exception {
		ByteArrayOutputStream v0 = new ByteArrayOutputStream();
		DataOutputStream v1 = new DataOutputStream(((OutputStream) v0));
		v1.writeLong(random.nextLong());
		v1.writeLong(random.nextLong());
		v1.flush();
		v1.close();
		byte[] v0_1 = v0.toByteArray();
		ByteArrayOutputStream v1_1 = new ByteArrayOutputStream();
		BufferedOutputStream v3 = new BufferedOutputStream(new GZIPOutputStream(((OutputStream) v1_1)));
		v3.write(arg7.getBytes("utf-8"));
		v3.flush();
		v3.close();
		byte[] v7 = Data.AES128Encode(v0_1, v1_1.toByteArray());
		v0_1 = new MobRSA(1024).encode(v0_1, new BigInteger(
				"ceeef5035212dfe7c6a0acdc0ef35ce5b118aab916477037d7381f85c6b6176fcf57b1d1c3296af0bb1c483fe5e1eb0ce9eb2953b44e494ca60777a1b033cc07",
				16),
				new BigInteger(
						"191737288d17e660c4b61440d5d14228a0bf9854499f9d68d8274db55d6d954489371ecf314f26bec236e58fac7fffa9b27bcf923e1229c4080d49f7758739e5bd6014383ed2a75ce1be9b0ab22f283c5c5e11216c5658ba444212b6270d629f2d615b8dfdec8545fb7d4f935b0cc10b6948ab4fc1cb1dd496a8f94b51e888dd",
						16));
		v1_1 = new ByteArrayOutputStream();
		DataOutputStream v2 = new DataOutputStream(((OutputStream) v1_1));
		v2.writeInt(v0_1.length);
		v2.write(v0_1);
		v2.writeInt(v7.length);
		v2.write(v7);
		v2.flush();
		v2.close();
		return AndroidBase64.encodeToString(v1_1.toByteArray(), 2);
	}

	/**
	 * mob log4j最后一位key
	 * @param arg2 MI 5X|25|Xiaomi|-1|1080x1920
	 * @param arg3 设备id device 45df133b4011e43a523aed63c32fcad63cf929cb
	 * @return
	 */
	public static String Base64AES(String arg2, String arg3) {
		try {
			String v0 = AndroidBase64.encodeToString(Data.AES128Encode(arg3, arg2), 0);
			if (!v0.contains("\n")) {
				v0 = v0.replace("\n", "");
			}
			return v0;
		} catch (Exception v3) {
			return null;
		}
	}

	public static void main(String[] args) throws Exception {
		//[EXT]:1547708014296|45df133b4011e43a523aed63c32fcad63cf929cb|25e07d9e5b85a|com.xcm.huasheng|15|3020101|1|wifi|EqiFv+SbPLr2LMkjO8x/+RP/0Zw7UX9ahXzQqmj/pu0=|40
		
		String s = Base64AES("MI 5X|25|Xiaomi|-1|1080x1920","45df133b4011e43a523aed63c32fcad63cf929cb");
		System.out.println(s);
	}
}
