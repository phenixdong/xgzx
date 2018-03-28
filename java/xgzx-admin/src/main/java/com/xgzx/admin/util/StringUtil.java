package com.xgzx.admin.util;

import java.util.List;

public class StringUtil {

	public static String listToStr(List<String> list) {
		String resp = "";
		for (String item : list) {
			resp = resp + item + ",";
		}
		resp = resp.substring(0, resp.length() - 1);
		System.out.println("listToStr " + resp);
		return resp;
	}
	
	
}
