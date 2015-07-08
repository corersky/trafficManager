package com.yuandu.erp.webservice.utils;

import java.util.Map;

import com.google.common.collect.Maps;

public class DefaultResponse extends BaseResponse{

	private static final long serialVersionUID = 1L;
	
	private Map<String,String> data = Maps.newHashMap();

	public Map<String, String> getData() {
		return data;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}
	
}
