package com.atao.dftt.util;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

public final class WlttCipher {
	Cipher a;
	Cipher b;
	private static String c = "DESede";
	private String d;
	private String e;

	public WlttCipher() {
		super();
		this.d = "";
		this.e = "";
		this.a = null;
		this.b = null;
		try {
			this.d = "PiadX_d(a+;@#!@3A^&EE>OP";
			SecureRandom v0_1 = new SecureRandom();
			SecretKey v1 = SecretKeyFactory.getInstance(c).generateSecret(new DESedeKeySpec(this.d.getBytes("utf-8")));
			this.a = Cipher.getInstance(c);
			this.a.init(1, ((Key) v1), v0_1);
			this.b = Cipher.getInstance(c);
			this.b.init(2, ((Key) v1), v0_1);
		} catch (Exception v0) {
		}
	}

	public WlttCipher(String arg5) {
		super();
		this.d = "";
		this.e = "";
		this.a = null;
		this.b = null;
		try {
			SecureRandom v0_1 = new SecureRandom();
			SecretKey v1 = SecretKeyFactory.getInstance(c).generateSecret(new DESedeKeySpec(arg5.getBytes("utf-8")));
			this.a = Cipher.getInstance(c);
			this.a.init(1, ((Key) v1), v0_1);
			this.b = Cipher.getInstance(c);
			this.b.init(2, ((Key) v1), v0_1);
		} catch (Exception v0) {
		}
	}

	public final String sign(String arg7)
			throws IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		byte[] v1 = this.a.doFinal(arg7.getBytes("utf-8"));
		StringBuffer v2 = new StringBuffer();
		int v0;
		for (v0 = 0; v0 < v1.length; ++v0) {
			String v3 = Integer.toHexString(v1[v0] & 255);
			if (v3.length() == 1) {
				v2.append("0" + v3);
			} else {
				v2.append(v3);
			}
		}
		return v2.toString();
	}

	public final String sign(int arg4, int arg5, int arg6, int arg7, int arg8) {
		String v0 = "";
		String v1 = Integer.toString(arg4) + Integer.toString(arg5) + Integer.toString(arg6) + Integer.toString(arg7)
				+ Integer.toString(arg8);
		try {
			v0 = WlttMessageDigest.sign(this.sign(v1));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		}
		return v0;
	}
}
