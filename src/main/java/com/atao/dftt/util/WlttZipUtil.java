package com.atao.dftt.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPOutputStream;

import com.atao.util.StringUtils;

public class WlttZipUtil {
	public static String encoding = "UTF-8";

	public static String a(String arg3) throws UnsupportedEncodingException, IOException {
		if (!StringUtils.isBlank(arg3)) {
			ByteArrayOutputStream v1 = new ByteArrayOutputStream();
			GZIPOutputStream v2 = new GZIPOutputStream(((OutputStream) v1));
			v2.write(arg3.getBytes(encoding));
			v2.close();
			arg3 = WlttZipHelper.a(v1.toByteArray());
		}
		return arg3;
	}
}
