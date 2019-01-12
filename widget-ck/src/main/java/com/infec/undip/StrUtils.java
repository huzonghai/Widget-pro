package com.infec.undip;

import java.util.Arrays;

public class StrUtils {
	// proguard auto change method content
	public static String deCrypt(String data) {
		return data;
	}

	// proguard data1 ,请不要修�?
	public static String deCrypt2(String data1) {
		return data1;
	}

	/**
	 * 把字节做处理后转换成字符url
	 * 
	 * @param bytes
	 * @return
	 */
	public static String bytes2str(byte[] bytes, int index) {
		byte[] copyByte = Arrays.copyOf(bytes, bytes.length);

		for (int i = 0; i < copyByte.length; i++) {
			copyByte[i] = (byte) (copyByte[i] + (2 * index + 3));
		}

		return new String(copyByte);
	}

	/**
	 * 把字符转换成字节
	 * 
	 * @param src
	 *            http address
	 * @return
	 */
	public static byte[] str2bytes(String src, int index) {
		byte[] bytes = src.getBytes();

		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) (bytes[i] - (2 * index + 3));
		}

		return bytes;
	}

	public static String substringBetween(String str, String open, String close) {
		if (str == null || open == null || close == null) {
			return null;
		}
		int start = str.indexOf(open);
		if (start != -1) {
			int end = str.indexOf(close, start + open.length());
			if (end != -1) {
				return str.substring(start + open.length(), end);
			}
		}
		return null;
	}

	public static boolean isEmpty(String str) {
		boolean ret = false;
		if (str == null || str.trim().length() == 0) {
			ret = true;
		}
		return ret;
	}
}
