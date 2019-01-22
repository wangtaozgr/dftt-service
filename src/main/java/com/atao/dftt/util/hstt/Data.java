package com.atao.dftt.util.hstt;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.zip.CRC32;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.SecretKeySpec;

import com.atao.dftt.util.AndroidBase64;
import com.atao.util.StringUtils;

public class Data {
	private static final String CHAT_SET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public Data() {
		super();
	}

	public static byte[] AES128Encode(byte[] arg8, byte[] arg9) throws Exception {
		SecretKeySpec v0 = new SecretKeySpec(arg8, "AES");
		StringBuilder v8 = new StringBuilder();
		v8.append("AES");
		v8.append("/EC");
		v8.append("B/PKCS7P");
		v8.append("adding");
		Cipher v8_1 = Data.getCipher(v8.toString(), "BC");
		v8_1.init(1, ((Key) v0));
		byte[] v0_1 = new byte[v8_1.getOutputSize(arg9.length)];
		v8_1.doFinal(v0_1, v8_1.update(arg9, 0, arg9.length, v0_1, 0));
		return v0_1;
	}

	public static byte[] AES128Encode(String arg8, String arg9) throws Exception, InvalidKeyException,
			IllegalBlockSizeException, ShortBufferException, BadPaddingException {
		if (arg8 != null) {
			if (arg9 == null) {
			} else {
				byte[] v8 = arg8.getBytes("UTF-8");
				byte[] v1 = new byte[16];
				System.arraycopy(v8, 0, v1, 0, Math.min(v8.length, 16));
				byte[] v3 = arg9.getBytes("UTF-8");
				SecretKeySpec v8_1 = new SecretKeySpec(v1, "AES");
				StringBuilder v9 = new StringBuilder();
				v9.append("AES");
				v9.append("/EC");
				v9.append("B/PKCS7P");
				v9.append("adding");
				Cipher v9_1 = Data.getCipher(v9.toString(), "BC");
				v9_1.init(1, ((Key) v8_1));
				v8 = new byte[v9_1.getOutputSize(v3.length)];
				v9_1.doFinal(v8, v9_1.update(v3, 0, v3.length, v8, 0));
				return v8;
			}
		}

		return null;
	}

	private static Cipher getCipher(String arg2, String arg3) throws Exception {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		Cipher v1 = null;
		if (!StringUtils.isEmpty(((CharSequence) arg3))) {
			try {
				Provider v3 = Security.getProvider(arg3);
				if (v3 != null) {
					v1 = Cipher.getInstance(arg2, v3);
				}

				if (v1 == null) {
					v1 = Cipher.getInstance(arg2);
				}
				return v1;
			} catch (Exception e) {
				if (v1 == null) {
					v1 = Cipher.getInstance(arg2);
				}
				return v1;
			}
		}
		if (v1 == null) {
			v1 = Cipher.getInstance(arg2);
		}
		return v1;
	}
}
