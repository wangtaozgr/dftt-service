package com.atao.dftt.util.mayitt;

import java.security.Key;
import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import com.atao.dftt.util.AndroidBase64;

public class SecurityHelper {
	private static final int a = 20;
	private static final int b = 8;

	public SecurityHelper() {
		super();
	}

	public static String a(String arg5, String arg6) {
		int v0 = 8;
		try {
			byte[] v1 = new byte[v0];
			MessageDigest v2 = MessageDigest.getInstance("MD5");
			v2.update(arg5.getBytes());
			byte[] v2_1 = v2.digest();
			int v3;
			for (v3 = 0; v3 < v0; ++v3) {
				v1[v3] = v2_1[v3];
			}

			SecretKey v5_1 = SecretKeyFactory.getInstance("PBEWithMD5AndDES")
					.generateSecret(new PBEKeySpec(arg5.toCharArray()));
			PBEParameterSpec v0_1 = new PBEParameterSpec(v1, 20);
			Cipher v2_2 = Cipher.getInstance("PBEWithMD5AndDES");
			v2_2.init(1, ((Key) v5_1), ((AlgorithmParameterSpec) v0_1));
			byte[] v5_2 = v2_2.doFinal(arg6.getBytes());
			arg6 = new String(AndroidBase64.encode(v1, 10));
			String v1_1 = new String(AndroidBase64.encode(v5_2, 10));
			return arg6 + v1_1;
		} catch (Exception v5) {
			return null;
		}
	}

	public static String b(String arg3, String arg4) {
		int v1 = 12;
		try {
			String v0 = arg4.substring(0, v1);
			arg4 = arg4.substring(v1, arg4.length());
			byte[] v0_1 = AndroidBase64.decode(v0.getBytes(), 10);
			byte[] v4 = AndroidBase64.decode(arg4.getBytes(), 10);
			SecretKey v3_1 = SecretKeyFactory.getInstance("PBEWithMD5AndDES")
					.generateSecret(new PBEKeySpec(arg3.toCharArray()));
			PBEParameterSpec v1_1 = new PBEParameterSpec(v0_1, 20);
			Cipher v0_2 = Cipher.getInstance("PBEWithMD5AndDES");
			v0_2.init(2, ((Key) v3_1), ((AlgorithmParameterSpec) v1_1));
			return new String(v0_2.doFinal(v4));
		} catch (Exception v3) {
			return null;
		}
	}

	public static String c(String arg6, String arg7) {
		int v0 = 8;
		try {
			byte[] v1 = new byte[v0];
			MessageDigest v2 = MessageDigest.getInstance("MD5");
			v2.update(arg6.getBytes());
			byte[] v2_1 = v2.digest();
			int v4;
			for (v4 = 0; v4 < v0; ++v4) {
				v1[v4] = v2_1[v4];
			}

			String v2_2 = new String(AndroidBase64.encode(v1, 10));
			IvParameterSpec v1_1 = new IvParameterSpec(v2_2.substring(0, v0).getBytes());
			SecretKey v6_1 = SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(arg6.getBytes()));
			Cipher v0_1 = Cipher.getInstance("DES/CBC/PKCS5Padding");
			v0_1.init(1, ((Key) v6_1), ((AlgorithmParameterSpec) v1_1));
			arg7 = new String(AndroidBase64.encode(v0_1.doFinal(arg7.getBytes()), 10));
			return v2_2 + arg7;
		} catch (Exception v6) {
			return null;
		}
	}

	public static String d(String arg3, String arg4) {
		int v1 = 8;
		try {
			String v0 = arg4.substring(0, v1);
			byte[] v4 = AndroidBase64.decode(arg4.substring(12, arg4.length()).getBytes(), 10);
			IvParameterSpec v1_1 = new IvParameterSpec(v0.getBytes());
			SecretKey v3_1 = SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(arg3.getBytes()));
			Cipher v0_1 = Cipher.getInstance("DES/CBC/PKCS5Padding");
			v0_1.init(2, ((Key) v3_1), ((AlgorithmParameterSpec) v1_1));
			return new String(v0_1.doFinal(v4));
		} catch (Exception v3) {
			return null;
		}
	}
}
