package com.atao.dftt.util.wltt;

import java.nio.ByteBuffer;

public class Hex {
	private static final char[] DIGITS_LOWER;
	private static final char[] DIGITS_UPPER;

	static {
		DIGITS_LOWER = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		DIGITS_UPPER = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
	}

	public Hex() {
		super();
	}

	public static byte[] decodeHex(String arg1) {
		return decodeHex(arg1.toCharArray());
	}

	public static byte[] decodeHex(char[] arg6) {
		int v0 = 0;
		int v2 = arg6.length;
		byte[] v3 = new byte[v2 >> 1];
		int v1;
		for (v1 = 0; v0 < v2; ++v1) {
			int v4 = toDigit(arg6[v0], v0) << 4;
			++v0;
			v4 |= toDigit(arg6[v0], v0);
			++v0;
			v3[v1] = ((byte) (v4 & 255));
		}

		return v3;
	}

	public static char[] encodeHex(ByteBuffer arg1) {
		return encodeHex(arg1, true);
	}

	public static char[] encodeHex(ByteBuffer arg1, boolean arg2) {
		char[] v0 = arg2 ? DIGITS_LOWER : DIGITS_UPPER;
		return encodeHex(arg1, v0);
	}

	protected static char[] encodeHex(ByteBuffer arg1, char[] arg2) {
		return encodeHex(arg1.array(), arg2);
	}

	protected static char[] encodeHex(byte[] arg6, char[] arg7) {
		int v0 = 0;
		int v2 = arg6.length;
		char[] v3 = new char[v2 << 1];
		int v1;
		for (v1 = 0; v1 < v2; ++v1) {
			int v4 = v0 + 1;
			v3[v0] = arg7[(arg6[v1] & 240) >>> 4];
			v0 = v4 + 1;
			v3[v4] = arg7[arg6[v1] & 15];
		}

		return v3;
	}

	public static char[] encodeHex(byte[] arg1) {
		return encodeHex(arg1, true);
	}

	public static char[] encodeHex(byte[] arg1, boolean arg2) {
		char[] v0 = arg2 ? DIGITS_LOWER : DIGITS_UPPER;
		return encodeHex(arg1, v0);
	}

	public static String encodeHexString(byte[] arg2) {
		return new String(encodeHex(arg2));
	}

	public static String encodeHexString(ByteBuffer arg2) {
		return new String(encodeHex(arg2));
	}

	public static String encodeHexString(ByteBuffer arg2, boolean arg3) {
		return new String(encodeHex(arg2, arg3));
	}

	public static String encodeHexString(byte[] arg2, boolean arg3) {
		return new String(encodeHex(arg2, arg3));
	}

	protected static int toDigit(char arg1, int arg2) {
		return Character.digit(arg1, 16);
	}
}
