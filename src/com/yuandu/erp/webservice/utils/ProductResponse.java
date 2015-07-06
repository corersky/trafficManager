package com.yuandu.erp.webservice.utils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.yuandu.erp.webservice.bean.ProductPojo;

public class ProductResponse implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String code;
	private String msg;
	private String mustShow;
	
	private List<ProductPojo> data = Lists.newArrayList();

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

	public List<ProductPojo> getData() {
		return data;
	}

	public void setData(List<ProductPojo> data) {
		this.data = data;
	}

}
