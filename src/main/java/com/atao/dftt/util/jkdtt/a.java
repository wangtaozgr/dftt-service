package com.atao.dftt.util.jkdtt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;

public class a extends e {
	private static final char[] Lu;
	private static final byte[] Lv;
	byte[] Lw;

	static {
		int v0 = 0;
		Lu = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
				'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
				'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8',
				'9', '+', '/' };
		Lv = new byte[256];
		int v1;
		for (v1 = 0; v1 < 255; ++v1) {
			a.Lv[v1] = -1;
		}

		while (v0 < a.Lu.length) {
			a.Lv[a.Lu[v0]] = ((byte) v0);
			++v0;
		}
	}

	public a() {
		super();
		this.Lw = new byte[4];
	}

	protected void a(PushbackInputStream arg9, OutputStream arg10, int arg11) throws IOException {
		int v2;
		int v3 = 3;
		int v1 = 2;
		int v0 = -1;
		if (arg11 < v1) {
			throw new IOException("BASE64Decoder: Not enough bytes for an atom.");
		}

		do {
			v2 = arg9.read();
			if (v2 == v0) {
				throw new IOException();
			}
			if (v2 == 10) {
				v2 = 13;
			}
		} while (v2 == 13);

		this.Lw[0] = ((byte) v2);
		if (this.a(((InputStream) arg9), this.Lw, 1, arg11 - 1) == v0) {
			throw new IOException();
		}

		v2 = arg11 <= v3 || this.Lw[v3] != 61 ? arg11 : v3;
		int v4 = v2 <= v1 || this.Lw[v1] != 61 ? v2 : v1;

		switch (v4) {
		case 2: {
			v1 = v0;
			v2 = a.Lv[this.Lw[1] & 255];
			v3 = a.Lv[this.Lw[0] & 255];
		}
		case 3: {
			v1 = a.Lv[this.Lw[v1] & 255];
			v2 = a.Lv[this.Lw[1] & 255];
			v3 = a.Lv[this.Lw[0] & 255];
		}
		case 4: {
			v0 = a.Lv[this.Lw[v3] & 255];
			v1 = a.Lv[this.Lw[v1] & 255];
			v2 = a.Lv[this.Lw[1] & 255];
			v3 = a.Lv[this.Lw[0] & 255];
		}
		}
		switch (v4) {
		case 2: {
			arg10.write(((byte) (v3 << 2 & 252 | v2 >>> 4 & 3)));
			break;
		}
		case 3: {
			arg10.write(((byte) (v3 << 2 & 252 | v2 >>> 4 & 3)));
			arg10.write(((byte) (v2 << 4 & 240 | v1 >>> 2 & 15)));
			break;
		}
		case 4: {
			arg10.write(((byte) (v3 << 2 & 252 | v2 >>> 4 & 3)));
			arg10.write(((byte) (v2 << 4 & 240 | v1 >>> 2 & 15)));
			arg10.write(((byte) (v0 & 63 | v1 << 6 & 192)));
			break;
		}
		}
		return;

	}

	protected int kl() {
		return 4;
	}

	protected int km() {
		return 72;
	}
}
