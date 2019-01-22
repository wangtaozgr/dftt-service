package com.atao.dftt.util.jkdtt;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;

public abstract class e {
	public e() {
		super();
	}

	protected int a(InputStream arg5, byte[] arg6, int arg7, int arg8) throws IOException {
		int v0 = -1;
		int v1 = 0;
		while (true) {
			if (v1 < arg8) {
				int v2 = arg5.read();
				if (v2 != v0) {
					arg6[v1 + arg7] = ((byte) v2);
					++v1;
					continue;
				} else if (v1 != 0) {
					v0 = v1;
				}
			} else {
				return arg8;
			}
			return v0;
		}
	}

	protected void a(PushbackInputStream arg1, OutputStream arg2) throws IOException {
	}

	protected void a(PushbackInputStream arg2, OutputStream arg3, int arg4) throws IOException {
	}

	public byte[] aA(String arg4) throws IOException {
		byte[] v0 = new byte[arg4.length()];
		arg4.getBytes(0, arg4.length(), v0, 0);
		ByteArrayInputStream v1 = new ByteArrayInputStream(v0);
		ByteArrayOutputStream v0_1 = new ByteArrayOutputStream();
		this.c(((InputStream) v1), ((OutputStream) v0_1));
		return v0_1.toByteArray();
	}

	protected void b(PushbackInputStream arg1, OutputStream arg2) throws IOException {
	}

	public void c(InputStream arg7, OutputStream arg8) throws IOException {
		PushbackInputStream v3 = new PushbackInputStream(arg7);
		this.a(v3, arg8);
		int v0 = 0;
		try {
			while (true) {
				int v4 = this.c(v3, arg8);
				int v1;
				for (v1 = 0; this.kl() + v1 < v4; v1 += this.kl()) {
					this.a(v3, arg8, this.kl());
					v0 += this.kl();
				}

				if (this.kl() + v1 == v4) {
					this.a(v3, arg8, this.kl());
					v0 += this.kl();
				} else {
					this.a(v3, arg8, v4 - v1);
					v0 += v4 - v1;
				}

				this.d(v3, arg8);
			}
		} catch (Exception e) {
			this.b(v3, arg8);
			return;
		}
	}

	protected int c(PushbackInputStream arg2, OutputStream arg3) throws IOException {
		return this.km();
	}

	protected void d(PushbackInputStream arg1, OutputStream arg2) throws IOException {
	}

	protected abstract int kl();

	protected abstract int km();
}
