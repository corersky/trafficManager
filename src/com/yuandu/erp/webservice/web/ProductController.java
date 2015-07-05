package com.yuandu.erp.webservice.web;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yuandu.erp.webservice.bean.ProductResponse;

/**
 * 用户付费Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/product")
public class ProductController {
	/*
	 * 说明:供合作方根据手机号过滤此手机号能使用的商品
	 * 版本号:v1
	 * 接口类型:private
	 * 接口名:product/productListByMobile
	 * 缓存时间:0
	 * 接口鉴权:是
	 * 参数(GET)*/
	@RequestMapping(value = "productListByMobile")
	public @ResponseBody List<ProductResponse> productListByMobile(@RequestParam String mobile) {
		//不能为空(需要根据Pubkey进行Rsa加密)
		//添加鉴权接口
		
		return null;
	}


}
