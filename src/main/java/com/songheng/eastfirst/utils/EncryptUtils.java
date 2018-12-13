package com.songheng.eastfirst.utils;

public class EncryptUtils {
	public static native String getShieldedParams(String arg0);

	static {
		System.out.println(System.getProperty("java.library.path"));
		// System.loadLibrary("/share/code/libproguard.so");
		System.load("/share/code/libproguard.so");
	}

	public static void main(String[] args)

	{
		System.out.println("start");
		EncryptUtils.getShieldedParams("112333");
		System.out.println("start");

	}
}
