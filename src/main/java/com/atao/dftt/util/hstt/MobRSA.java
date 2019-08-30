package com.atao.dftt.util.hstt;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public class MobRSA {
	private int keySize;

	public MobRSA(int arg1) {
		super();
		this.keySize = arg1;
	}

	public byte[] decode(byte[] arg3, BigInteger arg4, BigInteger arg5) throws Exception {
		DataInputStream v3 = new DataInputStream(new ByteArrayInputStream(arg3));
		ByteArrayOutputStream v0 = new ByteArrayOutputStream();
		while (v3.available() > 0) {
			byte[] v1 = new byte[v3.readInt()];
			v3.readFully(v1);
			v0.write(this.decodeBlock(v1, arg4, arg5));
		}

		v3.close();
		v0.close();
		return v0.toByteArray();
	}

	private byte[] decodeBlock(byte[] arg2, BigInteger arg3, BigInteger arg4) throws Exception {
		return this.recoveryPaddingBlock(new BigInteger(arg2).modPow(arg3, arg4).toByteArray());
	}

	public byte[] encode(byte[] arg16, BigInteger arg17, BigInteger arg18) throws Exception {
		byte[] v7 = arg16;
		MobRSA v8 = this;
		int v9 = v8.keySize / 8;
		int v10 = v9 - 11;
		ByteArrayOutputStream v11 = new ByteArrayOutputStream();
		DataOutputStream v12 = new DataOutputStream(((OutputStream) v11));
		int v13, v14;
		for (v13 = 0; v7.length > v13; v13 += v14) {
			v14 = Math.min(v7.length - v13, v10);
			byte[] v0 = v8.encodeBlock(v7, v13, v14, arg17, arg18, v9);
			v12.writeInt(v0.length);
			v12.write(v0);
		}

		v12.close();
		return v11.toByteArray();
	}

	private byte[] encodeBlock(byte[] arg3, int arg4, int arg5, BigInteger arg6, BigInteger arg7, int arg8)
			throws Exception {
		if (arg3.length != arg5 || arg4 != 0) {
			byte[] v0 = new byte[arg5];
			System.arraycopy(arg3, arg4, v0, 0, arg5);
			arg3 = v0;
		}

		BigInteger v4 = new BigInteger(this.paddingBlock(arg3, arg8));
		if (v4.compareTo(arg7) > 0) {
			throw new Exception("the message must be smaller than the modulue");
		}

		return v4.modPow(arg6, arg7).toByteArray();
	}

	public BigInteger[] genKeys() throws Exception {
		SecureRandom v0 = new SecureRandom();
		return this.genKeys(BigInteger.probablePrime(this.keySize / 2 - 1, ((Random) v0)),
				BigInteger.probablePrime(this.keySize / 2 - 1, ((Random) v0)),
				BigInteger.probablePrime(this.keySize / 2, ((Random) v0)));
	}

	public BigInteger[] genKeys(BigInteger arg4, BigInteger arg5, BigInteger arg6) throws Exception {
		if (arg6.compareTo(BigInteger.ONE) <= 0) {
			throw new Exception("e must be larger than 1");
		}

		BigInteger[] v0 = new BigInteger[3];
		BigInteger v1 = arg4.multiply(arg5);
		arg4 = v1.subtract(arg4).subtract(arg5).add(BigInteger.ONE);
		if (arg6.compareTo(arg4) >= 0) {
			throw new Exception("e must be smaller than (p-1)*(q-1)");
		}

		if (arg4.gcd(arg6).compareTo(BigInteger.ONE) != 0) {
			throw new Exception("e must be coprime with (p-1)*(q-1)");
		}

		arg4 = arg6.modInverse(arg4);
		v0[0] = arg6;
		v0[1] = arg4;
		v0[2] = v1;
		return v0;
	}

	private byte[] paddingBlock(byte[] arg6, int arg7) throws Exception {
		if (arg6.length > arg7 - 1) {
			throw new Exception("Message too large");
		}

		byte[] v0 = new byte[arg7];
		v0[0] = 1;
		int v3 = arg6.length;
		v0[1] = ((byte) (v3 >> 24));
		v0[2] = ((byte) (v3 >> 16));
		v0[3] = ((byte) (v3 >> 8));
		v0[4] = ((byte) v3);
		System.arraycopy(arg6, 0, v0, arg7 - v3, v3);
		return v0;
	}

	private byte[] recoveryPaddingBlock(byte[] arg5) throws Exception {
		if (arg5[0] != 1) {
			throw new Exception("Not RSA Block");
		}

		int v1 = ((arg5[1] & 255) << 24) + ((arg5[2] & 255) << 16) + ((arg5[3] & 255) << 8) + (arg5[4] & 255);
		byte[] v2 = new byte[v1];
		System.arraycopy(arg5, arg5.length - v1, v2, 0, v1);
		return v2;
	}
}
