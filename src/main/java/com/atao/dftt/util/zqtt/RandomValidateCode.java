package com.atao.dftt.util.zqtt;

import java.util.Random;

public class RandomValidateCode {
	private static final char[] a;

	static {
		a = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
				'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D',
				'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y',
				'Z' };
	}

	public RandomValidateCode() {
		super();
	}

	public static char a() {
		return RandomValidateCode.a[new Random().nextInt(RandomValidateCode.a.length)];
	}

	public static String a(String arg4, char arg5) {
		int v0 = arg5 % 10 % 3;
		arg4 = String.valueOf(arg5) + arg4;
		int v5;
		for (v5 = 0; v5 < v0; ++v5) {
			Random v1_1 = new Random();
			arg4 = arg4 + RandomValidateCode.a[v1_1.nextInt(RandomValidateCode.a.length)];
		}
		return arg4;
	}
}
