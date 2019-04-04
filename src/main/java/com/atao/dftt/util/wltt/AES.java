package com.atao.dftt.util.wltt;

import java.nio.charset.Charset;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {
	private static int AES_128;
	private static String AES_CBC_PADDING;
	public static String ALGORITHM;
	private static final Charset CHARSET;
	private static int IV_16;

	static {
		AES_128 = 128;
		IV_16 = 16;
		ALGORITHM = "AES";
		AES_CBC_PADDING = "AES/CBC/PKCS5Padding";
		CHARSET = Charset.forName("UTF-8");
	}

	public AES() {
		super();
	}

	private static String decrypt(String arg4, String arg5, String arg6) {
		try {
			arg6 = new String(decrypt(Hex.decodeHex(arg4), Hex.decodeHex(arg5), Hex.decodeHex(arg6)), CHARSET);
		} catch (Exception v0) {
			v0.printStackTrace();
		}

		return arg6;
	}

	private static byte[] decrypt(byte[] arg1, byte[] arg2, byte[] arg3) throws Exception {
		return encryptDecrpty(2, arg1, arg2, arg3);
	}

	private static String encrypt(String arg3, String arg4, String arg5) {
		try {
			arg5 = Hex.encodeHexString(encrypt(Hex.decodeHex(arg3), Hex.decodeHex(arg4), arg5.getBytes(CHARSET)));
		} catch (Exception v0) {
			v0.printStackTrace();
		}

		return arg5;
	}

	private static byte[] encrypt(byte[] arg1, byte[] arg2, byte[] arg3) throws Exception {
		return encryptDecrpty(1, arg1, arg2, arg3);
	}

	private static byte[] encryptDecrpty(int arg3, byte[] arg4, byte[] arg5, byte[] arg6) throws Exception {
		Cipher v0 = Cipher.getInstance(AES_CBC_PADDING);
		v0.init(arg3, new SecretKeySpec(arg4, ALGORITHM), new IvParameterSpec(arg5));
		return v0.doFinal(arg6);
	}

	public static String genHexIv() {
		byte[] v0 = new byte[IV_16];
		new SecureRandom().nextBytes(v0);
		return Hex.encodeHexString(v0);
	}

	public static String genSecretKey() {
		String v0_2;
		try {
			KeyGenerator v0_1 = KeyGenerator.getInstance(ALGORITHM);
			v0_1.init(AES_128);
			v0_2 = Hex.encodeHexString(v0_1.generateKey().getEncoded());
		} catch (Exception v0) {
			v0.printStackTrace();
			v0_2 = "";
		}

		return v0_2;
	}
}
