package com.example.colze.utils;

public class StringUtil {
	public static boolean isEmpty(String str) {
		if ((null != str) && (!("").equals(str))) {
			return false;
		}
		return true;
	}
}
