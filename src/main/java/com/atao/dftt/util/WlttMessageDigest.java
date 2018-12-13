package com.atao.dftt.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class WlttMessageDigest {
	public static String a = "YEK_3@F+)jd%sUesfC^>dK.dlc";

	public static String sign(String arg7) throws NoSuchAlgorithmException {
		String v0;
		if (arg7 == null) {
			v0 = null;
		} else {
			byte[] v0_1 = arg7.getBytes();
			MessageDigest v2 = MessageDigest.getInstance("MD5");
			v2.reset();
			v2.update(v0_1);
			byte[] v2_1 = v2.digest();
			StringBuffer v3 = new StringBuffer();
			int v0_2;
			for (v0_2 = 0; v0_2 < v2_1.length; ++v0_2) {
				v3.append(String.format("%02X", Byte.valueOf(v2_1[v0_2])));
			}
			v0 = v3.toString();
		}

		return v0;
	}

	public static String sign(byte[] arg5) {
		String v0;
		byte[] v1 = digest(arg5);
		if (v1 == null) {
			v0 = null;
		} else {
			String v2 = "0123456789abcdef";
			StringBuilder v3 = new StringBuilder(v1.length * 2);
			int v0_1;
			for (v0_1 = 0; v0_1 < v1.length; ++v0_1) {
				v3.append(v2.charAt(v1[v0_1] >> 4 & 15));
				v3.append(v2.charAt(v1[v0_1] & 15));
			}

			v0 = v3.toString();
		}

		return v0;
	}

	private static byte[] digest(byte[] arg2) {
		byte[] v0 = null;
		try {
			MessageDigest v1_1 = MessageDigest.getInstance("MD5");
			if (v1_1 != null) {
				v1_1.update(arg2);
				v0 = v1_1.digest();
			}
		} catch (Exception v1) {
			v1.printStackTrace();
		}
		return v0;
	}
}
