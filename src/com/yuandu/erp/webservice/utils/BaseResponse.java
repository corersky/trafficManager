package com.yuandu.erp.webservice.utils;

import java.io.Serializable;

public class BaseResponse implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String code;
	private String msg;
	private String mustShow;
	
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

	public void isCorrect()throws Exception{
		if(!"0000".equals(getCode())){
			throw new RuntimeException(getMsg());
		}
	}
}
