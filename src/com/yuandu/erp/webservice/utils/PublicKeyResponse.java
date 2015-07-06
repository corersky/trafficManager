package com.yuandu.erp.webservice.utils;

import java.io.Serializable;
import java.util.Map;

import com.google.common.collect.Maps;

public class PublicKeyResponse implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String code;
	private String msg;
	private String mustShow;
	
	private Map<String,String> data = Maps.newHashMap();

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getMustShow() {
		return mustShow;
	}

	public void setMustShow(String mustShow) {
		this.mustShow = mustShow;
	}

	public Map<String, String> getData() {
		return data;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}

}
