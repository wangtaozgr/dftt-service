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
			this.a.init(Cipher.ENCRYPT_MODE, ((Key) v1), v0_1);
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
	
	public final String decode(String s)
			throws IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		byte[] b1 = new byte[s.length()/2];
		for(int i=0;i<s.length();i=i+2) {
			String v0 = s.substring(i, i+2);
			int v1 = Integer.parseInt(v0, 16);
			b1[i/2] = (byte) v1;
		}
		byte[] b2 = this.b.doFinal(b1);
		return new String(b2,"utf-8");
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
	
	public static void main(String[] args) throws IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		WlttCipher a = new WlttCipher();
		String s = "54e5398c215cfd89573bb97d318ff0ae1ec873768aa39f0370ba2ebb73714d0d04931f8d65e0ca0f8e5fb95779dece0acc919db278efe0c8ef8ce85dc7ef3aa83908b96a19d28427045b6f17e3d7b63362754f5045648ccc7659188393fa2315c313c2a1637d797d3dc4874421103fc087010b39aadeb68df43a28afe3d7990cd3c987b91683495a631ff3ff394300d69fa2a9a7d0fec3418aa41a886e3d0fe338ea13c63fa59b938e7b169f5972cb1774ce2fbf804d5817679a3d8908a4e26e2b2f9505874df9d41e24a65ca853e3e6a7bc1849030f352eab7c563fad511d6f81dffa1759d0da6c5ce9ab833535e7cbb85e2bef3e7f54bc5224aff629e0cdf346d4c74411e76585623dc1c8b879012fe6fdfc6299e11bea4f5762c8c6b29a11367958b21172faaeb2a2987b5d07432d62c9e69542cf6c0ab8c151f4aad49a95ff48f8f6fb655aeab19ccab4b74391578604a255f683da7e2a67867837d3a1d444c3a4685272b8fc64b19ac6a8c19abd00c3c107e8c860eead6929efa1b353b3aac9ed87386af0473e3c6e89cfa9437dc742036b3bc029883862c4602ef0a7e64bacbabcea188212a29fc607cb0783305c79741e26a5f56258bce9912f7271b0e4f284759b26888377a7bd7e58b13cfd4feba6ebdf17d3957f7f5e5666af6c4134c991138c6ea806465a54d3569d4e3d8b83b2f73f82d06a9331f7e7513cb3f4197e6a38661809d4d2eb24345e8547fff5d6996a8d0658b48351d785e661567b34318121206f72081ee8d34c96b0ea0b874b0b947aca0957b831b5dd53124dabb4dd0f335e670d438d36d019fc001b283e8e1bbef06405d675e7ec7a817c7e87363dd2de23ed250bb6acc7dcd6a849ff8ff257487ffd654f9e4bf051a420f8e6d7274991783429c3c2c1128067863b86f6846f3baea1d6c835e31a0cb77f152e86a4db6564c7403eadc838fc080de71f5c7a91235e23d7f0a14f2ce32fb05659d539e09cf09f9446816a63d07ea823bff7a24448a3ba0f373fdef8056fec55478b64950c9ed5b7efddfefc1c8452697ea682ded5c14e3faec0f3fd5d4f60c01808b04910844d972a1cc1441f686af8867b7d984a1e05b3e0d1caf9421cdafd5ee2a841bb507516c9210ddf2e4b7f64450664822f17fd563d1267b45af975adc3647e3fd5676b34ca452f0a90504823fe25bdee1a8670484581dffa1759d0da6c8df584f6aca4a498fd2c0823393f0fefe4f284759b26888377a7bd7e58b13cfd4feba6ebdf17d395e5b72c86010e7bd736c6d00583ea8b0780a6f48a37584366415f62bc678473e693750d3cb1f6391ed539e09cf09f9446e4cd853c56d18010912258968ce350c2ad5c1395e4a337898b64950c9ed5b7ef8ba1f59870df0b3f6e6ef01013d7440f309dccc8041737785206ddaf2aca70c3b55200bd0c32fcedb0570d441cc18573874b0b947aca0957b831b5dd53124dabb5442a6ae2c88035577a847372bfa228605a98b1bcd34862bcdf81c53d11948c2b3187096ac7217eb6acc7dcd6a849ff075547f5ace3267e9e4bf051a420f8e69f5eecc7b7d449300caf5a8d4d6f4c7456fc467be758b770cb5607440b24bfc232018044dbe93d681abeac83d3d42111ed7a263bf17aea2c0cf96d842ffaccdedc543286f4a7fbf32d61cd63ed1b559dfbea4d0f4f66c4391426bb9e2685e32b12cff4d4b712f5b6c41098b43a36900b67de3584226d3280d7dd3fcc3d791646eb69da83eaf4149cdf250c0e020c20bb0e99487dcd4f993e68f2b54438743f3ad5f623a940395d07f4e0ceca5807b860acf9964b3ca709004d06e2011776f5382cb801f6f20da7fd925ea60d04b12c33d7551f75e92ad7f29e1a8172cd67844e0456b60472d283578e27b855a6e9697a7981417f5efc07e1b05d54090410219b6f2b44852cffc4d4519936ce5aed05f3264809357494d3190226d1a8462fcc97413c6f8a13a0d527f8534c3ac66c1f11166118184ea6ebb58245eec23a3e4296598dcf64058cacc9d86e8688d3259c66a6e61c01d3f90d8e8b47bd442d5a6e3716d87c5b6cbb8ca2dbab6b97fc8687814dd3fff3def6c871daf74e5c62f570571cd1e9603c80c316624daa25e6eb9fa10c37bc4fea8bc4e1fb006d0875b19ec82cefd906d7942422dd2c1f15d4c001968d2130dd2ff1876d4493ebbe20cd4ab2";
		System.out.println(a.decode(s));
		
	}
}
