package com.atao.dftt.util.jkdtt;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

public abstract class f {
	protected PrintStream Lx;

	public f() {
		super();
	}

	protected abstract void a(OutputStream arg1, byte[] arg2, int arg3, int arg4) throws IOException;

	protected void b(OutputStream arg1, int arg2) throws IOException {
	}

	protected int c(InputStream arg4, byte[] arg5) throws IOException {
		int v0 = 0;
		while (true) {
			if (v0 < arg5.length) {
				int v1 = arg4.read();
				if (v1 != -1) {
					arg5[v0] = ((byte) v1);
					++v0;
					continue;
				}
			} else {
				break;
			}

			return v0;
		}

		return arg5.length;
	}

	public void d(InputStream arg5, OutputStream arg6) throws IOException {
		byte[] v1 = new byte[this.km()];
		this.g(arg6);
		while (true) {
			int v2 = this.c(arg5, v1);
			if (v2 != 0) {
				this.b(arg6, v2);
				int v0;
				for (v0 = 0; v0 < v2; v0 += this.kl()) {
					if (this.kl() + v0 <= v2) {
						this.a(arg6, v1, v0, this.kl());
					} else {
						this.a(arg6, v1, v0, v2 - v0);
					}
				}

				if (v2 < this.km()) {
					break;
				}

				this.i(arg6);
				continue;
			}

			break;
		}

		this.h(arg6);
	}

	public void e(InputStream arg5, OutputStream arg6) throws IOException {
		byte[] v1 = new byte[this.km()];
		this.g(arg6);
		int v2 = this.c(arg5, v1);
		do {
			if (v2 == 0) {
				break;
			}

			this.b(arg6, v2);
			int v0;
			for (v0 = 0; v0 < v2; v0 += this.kl()) {
				if (this.kl() + v0 <= v2) {
					this.a(arg6, v1, v0, this.kl());
				} else {
					this.a(arg6, v1, v0, v2 - v0);
				}
			}

			this.i(arg6);
		} while (v2 >= this.km());

		this.h(arg6);
	}

	public String encode(byte[] arg3) {
		ByteArrayOutputStream v0 = new ByteArrayOutputStream();
		ByteArrayInputStream v1 = new ByteArrayInputStream(arg3);
		try {
			this.d(((InputStream) v1), ((OutputStream) v0));
			return v0.toString("8859_1");
		} catch (Exception v0_1) {
			throw new Error("CharacterEncoder.encode internal error");
		}
	}

	protected void g(OutputStream arg2) throws IOException {
		this.Lx = new PrintStream(arg2);
	}

	protected void h(OutputStream arg1) throws IOException {
	}

	protected void i(OutputStream arg2) throws IOException {
		this.Lx.println();
	}

	protected abstract int kl();

	protected abstract int km();

	public String l(byte[] arg3) {
		ByteArrayOutputStream v0 = new ByteArrayOutputStream();
		ByteArrayInputStream v1 = new ByteArrayInputStream(arg3);
		try {
			this.e(((InputStream) v1), ((OutputStream) v0));
		} catch (Exception v0_1) {
			throw new Error("CharacterEncoder.encodeBuffer internal error");
		}

		return v0.toString();
	}
}
