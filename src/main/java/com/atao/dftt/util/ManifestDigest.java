package com.atao.dftt.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

class ManifestDigest {
	private static final String TAG = "ManifestDigest";

	/** The digest of the manifest in our preferred order. */
	private final byte[] mDigest;

	/** What we print out first when toString() is called. */
	private static final String TO_STRING_PREFIX = "ManifestDigest {mDigest=";

	/** Digest algorithm to use. */
	private static final String DIGEST_ALGORITHM = "SHA-256";

	ManifestDigest(byte[] digest) {
		mDigest = digest;
	}

	static ManifestDigest fromInputStream(InputStream fileIs) {
		if (fileIs == null) {
			return null;
		}

		final MessageDigest md;
		try {
			md = MessageDigest.getInstance(DIGEST_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(DIGEST_ALGORITHM + " must be available", e);
		}

		final DigestInputStream dis = new DigestInputStream(new BufferedInputStream(fileIs), md);
		try {
			byte[] readBuffer = new byte[8192];
			while (dis.read(readBuffer, 0, readBuffer.length) != -1) {
				// not using
			}
		} catch (IOException e) {
			// Slog.w(TAG, "Could not read manifest");
			return null;
		} finally {
			// IoUtils.closeQuietly(dis);
		}

		final byte[] digest = md.digest();
		return new ManifestDigest(digest);
	}

	public int describeContents() {
		return 0;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof ManifestDigest)) {
			return false;
		}

		final ManifestDigest other = (ManifestDigest) o;

		return this == other || Arrays.equals(mDigest, other.mDigest);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(mDigest);
	}

}
