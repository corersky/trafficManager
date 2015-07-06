package com.yuandu.erp.webservice.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuandu.erp.webservice.bean.ProductPojo;
import com.yuandu.erp.webservice.utils.BusinessUtil;

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
	public List<ProductPojo> productListByMobile(String mobile) {
		
		return null;
	}

	/*	公钥获取接口
	 * 说明:公钥获取,用户登录等接口的数据加密
	 * 接口版本:v1
	 * 接口类型:public
	 * 接口名:common/getPublicKey
	 * 缓存时间:1天
	 * 接口鉴权:否
	 * 返回值(Json) publickey RSA公钥*/
	public String getPublicKey(){
		String response = BusinessUtil.getPublicKey();
		
		return response;
	}
	
}