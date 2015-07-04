package com.yuandu.erp.common.utils;

public class LongUtils {

	/**
	 * 判断是否为空
	 * @param object 参数
	 * @return
	 */
	public static boolean isBlank(Long object) {
		if(object == null || "".equals(object)){
			return true;
		}
		return false;
	}

	/**
	 * 判断非空
	 * @param object 参数
	 * @return
	 */
	public static boolean isNotBlank(Long object) {
		if(object != null && !"".equals(object)){
			return true;
		}
		return false;
	}

}
