package com.yuandu.erp.webservice.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yuandu.erp.webservice.bean.ProductPojo;
import com.yuandu.erp.webservice.service.ProductService;
import com.yuandu.erp.webservice.utils.DefaultResponse;

/**
 * 用户付费Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/product")
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	/*
	 * 说明:供合作方根据手机号过滤此手机号能使用的商品
	 * 版本号:v1
	 * 接口类型:private
	 * 接口名:product/productListByMobile
	 * 缓存时间:0
	 * 接口鉴权:是
	 * 参数(GET)*/
	@RequestMapping(value = "productListByMobile")
	public @ResponseBody List<ProductPojo> productListByMobile(@RequestParam String mobile) {
		//不能为空(需要根据Pubkey进行Rsa加密)
		//添加鉴权接口
		
		return null;
	}

	/*
	 * 说明:合作方状态改变通知接口
	 * orderNo
	 * partnerOrderNo
	 * status
	 */
	@RequestMapping(value = "notifyStatus",method = RequestMethod.POST)
	public @ResponseBody DefaultResponse notifyStatus(@RequestParam long orderNo,@RequestParam String partnerOrderNo
			,@RequestParam String status) {
		DefaultResponse response = new DefaultResponse();
		try {
			response.setCode("0000");
			response.setMsg("订单：["+partnerOrderNo+"]  状态更新成功");
			productService.notifyStatus(partnerOrderNo,status);
		} catch (Exception e) {
			response.setCode("0001");
			response.setMsg("订单：["+partnerOrderNo+"]  状态更新成功");
			response.setMsg("订单：["+partnerOrderNo+"]  状态更新失败");
		}
		
		return response;
	}
}
