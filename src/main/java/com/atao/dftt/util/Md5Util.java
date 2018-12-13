package com.atao.dftt.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Util {
	private static final String[] a = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e",
			"f" };

	private static String md5(byte[] paramArrayOfByte) {
		StringBuffer localStringBuffer = new StringBuffer();
		for (int i=0;i < paramArrayOfByte.length;i++) {
			localStringBuffer.append(md5(paramArrayOfByte[i]));
		}
		return localStringBuffer.toString();
	}

	 private static String md5(byte paramByte)
	  {
	    int i = paramByte;
	    if (paramByte < 0)
	      i = paramByte + 256;
	    paramByte = (byte) (i / 16);
	    return a[paramByte] + a[(i % 16)];
	  }

	 public static String md5(String param) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			byte[] bs = md.digest(param.getBytes());
			return md5(bs);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
		
	}

}
