package com.atao.dftt.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;
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
	
	public static String decode(String arg3) throws UnsupportedEncodingException, IOException {
		if (!StringUtils.isBlank(arg3)) {
			byte[] buff = WlttZipHelper.decode(arg3);
			ByteArrayInputStream bin = new ByteArrayInputStream(buff);
			GZIPInputStream g = new GZIPInputStream(bin);
			BufferedReader reader = new BufferedReader(new InputStreamReader(g));  
			String result = reader.readLine();
			reader.close();
			g.close();
			return result;
		}
		return arg3;
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException, IOException {
		String s = WlttZipUtil.decode("H4sIAAAAAAAAAJWWSa4dMQhFtwQ2xng5uGH/S8ipDN8kihR9KfXKLrgdDHujrzhNx6kba8y+95q3a1RJPn0nZp8emX2V9Ck729ubZ2pXrtjqWnF9PDkpFW/U5pa57jtP8pyl6+424xVv8DfOi3HPfNUzoy+JvTx5qYW1KTa2l87X79zeu7c5rAaP3eKcM30sDY71ytYpums/LdX7vPOr+x6LadZUpdeRmKKy+8q8b3vcsp4tsr7715Jz+1kx91KPbmuk0011/i3rItcXn8mV80q9003BwTQjnAt41Hu8pAKnnLH24r9zK6fGmu1sndPOa7UrxO4InT5XdnCWSHvNX9ut1pBBzbfWNX3N0sfJGP315zn30bAlXaqP9nLPM76v3aF6vUJvVeocCzLSzzhv5Y7eRx9vnr1j8A2wfrOG7TMa91d5W7Bxq5tfO+Cl+oA2hjcQ1BM75hJt9bxuHV5fT3W/hIvp7cTx3VaqdhoasYF7tsp8u2X7AB1XpvV2eXc9X8PaKT9CaUeHlsGHrVyjyqKivLaPkbSn65zctjm9dMxwsWxjFPzcnK+l82wGdPfUa2anp3mMNtsOKjVuyOPJdwDiipx+VOaLw1XvJZTSO5BtINpIkPouEg/PEpuAfvzFOpUoYKJTa9J9932ExoL32jGX9nDHfHwuz859UEFE0697ihCQF4rsMg6q/Fj1HAW6YAVDC2Rnm7rlWaF76NvNg+vnhPE7H+8hq7pWb6e/ffHE7Rir4PENd8r1Y9rtcJt2CHkZ4uYjh90+HDGhUYjsSGz2inbFc3UEPt8c3L57mS6aSLpyDeHIxdzNug1MarOqBoDhz+AwNVn0oXciKg4Mn7sbSaE0Lk5HEvpe3K8op7cUZGRb9rKZpAPlU44ijju6Nru5t118hFyfT7nWOexXFBBFdLfoL55uBExrQi0TVb8zEysRIfDm/d52X+vvtiF7O5EDbZeSbRXSqjYmkVBFRlmtZ7tkaEIrYeUXGgwEIcvaQivtqDaARGV9h6M78+obaWK3Q/uva8rZc5biR2RjF+365yLp6It34gseuW9qwX/CNscw1kQzCuHndTJBBlkGEDgRucmqZQhZnQzCPLDZ+q6a2RB5JCWAEzxeIpSjxNGAodhObMkn4rHJ1Hvr1WdgG83hNh3e+e0oaqh8h0vqDnTlcgTOYxPhKmF2iazUw5umtI3AiLm5511hqU/G7k+unqTQpucm9zwMGQZBQ5CUn9VUKKGB8iw3G+Lkdmuol7dpTBtX2uCOOcCquxH1308Tj9lJygakJSQ+/ddrY9/3SFzkYTT1m3ck+giaQV6WtqJuA/vWvwFBPtf/+uuBYvuiQuV9L3Y/6JF8htr4PC3p9dEx+WynOSWa2j7o5QsPX/wgDJUNhATY++X32blEOxl+AV8FbZAJy5mKHRpbflR9dVj2PmP98hs7ERUJK7eYa+XP8YaKoi4zyMXEjEnGjTKiMSrGvFDVAGiShXvwRMjFgwTPu6jw8519vAfD/59+HczW9HyAFNIHkz6/zklfJmUDuJEUrZvJavEhc5lRZ8C8LiZ42+TFFBLlzMaY//UrMvpyZ+TnEZ/v169M1vc4gSyN1YN4T14OggtasCDGIJ7nJuECj2LUQRqDDKm267TePtEj9v1Wv9gtGWpYlsXh2xkgDMPhfOSA5dHSqeWXzQTVnYOTzmCskQuWk7H+TTis4KQPMCHK+mYkiYYkiWcUYM1ZqMh/MrO3rczBsos9GAGsOkS4gBdbSfcFMT4vQuB7zS/fkTvvZY8537qipDuArZufGsluW6BL6h150hAP8ScMF9IOQMm6hSCj2sfZR2xn47qfXDuzTRAZMVPGHvBOjhC4dhY2jjHV+icXCqIXlhRhvE+v0YOlBtkyrxv2w9OrDTLm2wO/1jplDsY7Kb2Q8ixGrX5Z3cAIh/AxGNrM9NnY9zrz/TW+y+j3t8irnCsU6cIyGTqJfkJ/GJWAU9PFbtg2cTg+GA84DqVLP4/d4cqAD+JGFhfbYgrqkg9ISjBH7ucbxcz3Cna2lMvSUJjxWyEOdGkhAOYaAy3sfamwRzT7BAdk/TEgSOy4bDLMojjfcnvZBINkgayxuIRTCEIU0pbceFBLjOCZ+7XI0oyY2OAgntXsZLssD87CXWROTHr/6Gb9JOUJt6lYd7Kis4GxeOFVxKYgJR2Ls/u6f65kQSIGNruq8pCNkVQjrY49ZcwQAKzBg72IXZ7ZA2k8moQ4KmN4fn0zEL6BSw7dpp0IR+/41P+aDByQAIrK3f4AOtEI2jAMAAA=");
		System.out.println(s);
	}
}
