package com.atao.dftt.util;

public class WlttZipHelper {
	private static char[] a = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
			'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
			'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4',
			'5', '6', '7', '8', '9', '+', '/' };
	private static byte[] b = new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1,
			-1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
			10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30,
			31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1 };

	public static String a(byte[] arg8) {
		StringBuffer v1 = new StringBuffer();
		int v2 = arg8.length;
		int v0 = 0;
		while (v0 < v2) {
			int v3 = v0 + 1;
			int v4 = arg8[v0] & 255;
			if (v3 == v2) {
				v1.append(a[v4 >>> 2]);
				v1.append(a[(v4 & 3) << 4]);
				v1.append("==");
			} else {
				int v5 = v3 + 1;
				v3 = arg8[v3] & 255;
				if (v5 == v2) {
					v1.append(a[v4 >>> 2]);
					v1.append(a[(v4 & 3) << 4 | (v3 & 240) >>> 4]);
					v1.append(a[(v3 & 15) << 2]);
					v1.append("=");
				} else {
					v0 = v5 + 1;
					v5 = arg8[v5] & 255;
					v1.append(a[v4 >>> 2]);
					v1.append(a[(v4 & 3) << 4 | (v3 & 240) >>> 4]);
					v1.append(a[(v3 & 15) << 2 | (v5 & 192) >>> 6]);
					v1.append(a[v5 & 63]);
					continue;
				}
			}

			break;
		}

		return v1.toString();
	}
}
