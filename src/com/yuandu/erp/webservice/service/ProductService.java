package com.yuandu.erp.webservice.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuandu.erp.webservice.bean.ProductResponse;

/**
 * 充值Service
 */
@Service
@Transactional(readOnly = true)
public class ProductService {

	/*
	 * 说明:供合作方根据手机号过滤此手机号能使用的商品
	 * 版本号:v1
	 * 接口类型:private
	 * 接口名:product/productListByMobile
	 * 缓存时间:0
	 * 接口鉴权:是
	 * 参数(GET)*/
	public List<ProductResponse> productListByMobile(String mobile) {
		
		return null;
	}

	
}