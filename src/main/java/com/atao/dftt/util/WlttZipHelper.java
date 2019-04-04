package com.atao.dftt.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;

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

	public static byte[] decode(String s) {
		int len = s.length();
		byte[] b = new byte[len];
		for (int i = 0, j = 0; i < s.length(); i = i + 4, j = j + 3) {
			char c1 = s.charAt(i);
			int index1 = indexOfA(c1);
			int v401 = index1 << 2;
			char c4 = s.charAt(i + 3);
			int index4 = indexOfA(c4);
			int v502 = index4 & 63;
			char c2 = s.charAt(i + 1);
			int index2 = indexOfA(c2);
			int v402 = (index2 & 48) >> 4;
			int v301 = (index2 & 15) << 4;
			char c3 = s.charAt(i + 2);
			int index3 = indexOfA(c3);
			int v501 = (index3 & 3) << 6;// v5前2位
			int v302 = (index3 & 60) >> 2;// v3后四位
			int v3 = v301 + v302;
			int v4 = v401 + v402;
			int v5 = v501 + v502;
			b[j] = (byte) v4;
			b[j + 1] = (byte) v3;
			b[j + 2] = (byte) v5;
		}
		if (s.endsWith("==")) {

		} else if (s.endsWith("=")) {

		}
		return b;
	}

	public static int indexOfA(char c) {
		for (int i = 0; i < a.length; i++) {
			if (a[i] == c)
				return i;
		}
		return 0;
	}

	public static void parse(char a, char b, char c, char d, int v3) {
		/*
		 * int v4 = indexOfA(a) <<2; int m1 = b = (v4 & 3) << 4 | (v3 & 240) >>> 4; int
		 * m2 = (v3 & 15) << 2 | (v5 & 192) >>> 6; int m3 = v5 & 63;
		 */
	}

	public static void printInfo(int num) {
		System.out.println(Integer.toBinaryString(num));
	}

	public static void main(String[] args) throws IOException {
		byte[] b = decode("7704ff289e87fcc4d1e4ee2225616287d8d0ccd42a9010f1789b83b0264bd40bacac7496697178620f7500a66a0713f67edf74cd065c2cc50e0295cb2cca5fa35c6dfa2d91a97f0f5ace3498ce984fabc5f1fd1a92a6c83f8238df493cd074f9c9c7c72d46c742ca1fb4c7f86a71fdada97e10f678bb33313446e4c4894ae5b7a09f091c2c4a45127eeb10265511632504472e57570962a5b84a66d01cac0e59fcf622dbe4918aebb97a1caf6da13e4233245839043bbddbb65225657642c88deea1c8631807e66029d0e8d823c159a970af1829c3541ac839c00405fa6804ea9e31770bc63c2f21bb88da728c75cbae89e69522aef1bcd230abedd008957223f4542327cdaca2b56c65f20fdbd4e109fdabcbb96389cb62f255697e845cd116cc54240fb1b514ff12d47e4e2c0d68e7ad559c53e12abf665c7d233feeec3c52c7117d774f00402a6b507099b1361520989584b3217ec65cf17865010b1511a4b9e40ff405644c4b0431a67ac7d0fe6b0d4a1644a05268e718ed1288cf85435d8c2dc5e0aef80ca1773e7e4b77f5613825e3701333f073e9b92b7405b0f89e968a75275ebbeb5065353212fab86ad863848ac2963918006e03d96c333b744bea6173f0ce0d65a51b9cb6bd3ae62714b8c51bb9ca6f6673add5da6c81eb9391cbcd5c501e39ede28670f7cce00f34d53b4b9987f3aa707fa21eddbe36e1c2778ae435e59eae80867040d850d7505e5ad7d7f5845f49be7524cf91b9ad0c687f05638d039e0db9c68034ef5be98ade644f69bf43f94736e7a52e54a96192b2b3bb21733b2ecd2ec2265c932c464f514c0d8575df3a46fa43904bebf43c7c90f6bf9ee50fed546cac3714195fb623ae68bdcb2bbdf383268654767bf52d49b9a8c5b88cfed02d4fa6437c578172a87bbe72");
		System.out.println(new String(b,"gbk"));


	}
}
