package com.atao.dftt.util.jkdtt;

import java.io.IOException;
import java.io.OutputStream;

public class b extends f {
	private static final char[] Lu;

	static {
		Lu = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
				'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
				'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8',
				'9', '+', '/' };
	}

	public b() {
		super();
	}

	protected void a(OutputStream arg6, byte[] arg7, int arg8, int arg9) throws IOException {
		int v1;
		int v0;
		int v4 = 61;
		if (arg9 == 1) {
			v0 = arg7[arg8];
			arg6.write(b.Lu[v0 >>> 2 & 63]);
			arg6.write(b.Lu[v0 << 4 & 48]);
			arg6.write(v4);
			arg6.write(v4);
		} else if (arg9 == 2) {
			v0 = arg7[arg8];
			v1 = arg7[arg8 + 1];
			arg6.write(b.Lu[v0 >>> 2 & 63]);
			arg6.write(b.Lu[(v0 << 4 & 48) + (v1 >>> 4 & 15)]);
			arg6.write(b.Lu[v1 << 2 & 60]);
			arg6.write(v4);
		} else {
			v0 = arg7[arg8];
			v1 = arg7[arg8 + 1];
			int v2 = arg7[arg8 + 2];
			arg6.write(b.Lu[v0 >>> 2 & 63]);
			arg6.write(b.Lu[(v0 << 4 & 48) + (v1 >>> 4 & 15)]);
			arg6.write(b.Lu[(v1 << 2 & 60) + (v2 >>> 6 & 3)]);
			arg6.write(b.Lu[v2 & 63]);
		}
	}

	protected int kl() {
		return 3;
	}

	protected int km() {
		return 57;
	}
}
