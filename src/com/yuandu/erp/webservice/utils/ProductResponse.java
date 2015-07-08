package com.yuandu.erp.webservice.utils;

import java.util.List;

import com.google.common.collect.Lists;
import com.yuandu.erp.webservice.bean.ProductPojo;

public class ProductResponse extends BaseResponse{

	private static final long serialVersionUID = 1L;
	
	private List<ProductPojo> data = Lists.newArrayList();

	public List<ProductPojo> getData() {
		return data;
	}

	public void setData(List<ProductPojo> data) {
		this.data = data;
	}

}
