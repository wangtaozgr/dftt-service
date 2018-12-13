package com.atao.dftt.util;

public class Jiami {
	public static byte[] a(byte[] arg3) {
		byte[] v1 = new byte[arg3.length];
		int v0;
		for (v0 = 0; v0 < arg3.length; ++v0) {
			v1[v0] = ((byte) (arg3[v0] ^ -1));
		}

		return v1;
	}

	public static byte[] f(String arg7) {
		byte[] v1 = arg7.getBytes();
		int v2 = v1.length;
		byte[] v3 = new byte[v2 / 2];
		int v0;
		for (v0 = 0; v0 < v2; v0 += 2) {
			v3[v0 / 2] = ((byte) Integer.parseInt(new String(v1, v0, 2), 16));
		}

		return v3;
	}

	public static String g(String arg2) {
		return new String(a(f(arg2)));
	}
	
	public static void main(String[] args) {
		String s = "data=device=e0db559dfd303202&imei=866174010224216&city=null&area=null&bssid=e0db559dfd303202&hispos=安徽,宿&mac={\"bssid\":\"E0:DB:55:9D:FD:30\",\"state\":\"3\",\"ssid\":\"R11e0db559dfd303202\",\"lng\":\"null\",\"mac\":\"08:00:27:E6:F1:94\",\"lat\":\"null\",\"temperature\":\"41\",\"ele\":\"90\"}&osType=Android4.4.2&qid=dftt180920&ssid=e0db559dfd303202&machine=M6 Note&keystr=ac4050d14ecc7485&prince=M6 Note&deviceId=201809031524304d37e4323c2ad89dfc76deff9dca530b01a26a6bbe35725d";
		System.out.println(s.getBytes().length);
		
	}
}
