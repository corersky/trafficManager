package com.yuandu.erp.webservice.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yuandu.erp.modules.business.entity.PartnerOrder;
import com.yuandu.erp.modules.sys.entity.User;
import com.yuandu.erp.modules.sys.utils.UserUtils;
import com.yuandu.erp.webservice.service.ProductService;
import com.yuandu.erp.webservice.utils.DefaultResponse;
import com.yuandu.erp.webservice.utils.PartnerOrderResponse;
import com.yuandu.erp.webservice.utils.ProductResponse;

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
	public @ResponseBody ProductResponse productListByMobile(@RequestParam String channel,@RequestParam String mobile) {
		ProductResponse response = new ProductResponse();
		try {
			User user = validateUser(channel);
			response = productService.productListByMobile(user,mobile);
		} catch (Exception e) {
			response.setCode("0001");
			response.setMsg("获取商品列表失败！");
		}
		return response;
	}

	/*
	 * 说明:合作方状态改变通知接口
	 * orderNo
	 * partnerOrderNo
	 * status
	 */
	@RequestMapping(value = "notifyStatus",method=RequestMethod.POST)
	public @ResponseBody DefaultResponse notifyStatus(@RequestParam String channel,@RequestParam String orderNo
			,@RequestParam String partnerOrderNo,@RequestParam String status) {
		DefaultResponse response = new DefaultResponse();
		try {
			User user = validateUser(channel);
			response.setCode("0000");
			response.setMsg("订单：["+partnerOrderNo+"]  状态更新成功");
			productService.notifyStatus(user,orderNo,partnerOrderNo,status);
		} catch (Exception e) {
			response.setCode("0001");
			response.setMsg("订单：["+partnerOrderNo+"]  状态更新失败 "+e.getMessage());
		}
		
		return response;
	}
	
	/*	
	 * 说明:供合作方给指定手机号充值流量
	 * 接口版本:v1
	 * 接口类型:private
	 * 接口名:order/buyFlow
	 * 缓存时间:1天
	 * 接口鉴权:是
	 * 返回值(Json)*/
	@RequestMapping(value = "buyFlow")
	public @ResponseBody DefaultResponse buyFlow(@RequestParam String channel,@RequestParam  String product,@RequestParam  String mobile){
		DefaultResponse response = new DefaultResponse();
		try {
			User user = validateUser(channel);
			response = productService.buyFlow(user, product, mobile);
		} catch (Exception e) {
			response.setCode("0001");
			response.setMsg("手机：["+mobile+"]  购买失败："+e.getMessage());
		}
		
		return response;
	}
	
	/*	
	 * 说明:供合作方给指定手机号充值流量
	 * 接口版本:v1
	 * 接口类型:private
	 * 接口名:order/buyFlow
	 * 缓存时间:1天
	 * 接口鉴权:是
	 * 返回值(Json)*/
	@RequestMapping(value = "queryOrderByPartnerOrderNo")
	public @ResponseBody PartnerOrderResponse queryOrderByPartnerOrderNo(@RequestParam String channel,@RequestParam  String partnerOrderNo){
		
		PartnerOrderResponse response = new PartnerOrderResponse();
		try {
			User user = validateUser(channel);
			response.setCode("0000");
			PartnerOrder order = productService.queryOrderByPartnerOrderNo(user, partnerOrderNo);
			response.setData(order);
		} catch (Exception e) {
			response.setCode("0001");
			response.setMsg("订单["+partnerOrderNo+"]获取失败："+e.getMessage());
		}
		
		return response;
	}
	
	private User validateUser(String channel)throws Exception{
		//判断用户合法
		User user = UserUtils.getByNo(channel);
		if(user==null){
			throw new RuntimeException("非法用户["+channel+"]");
		}
		return user;
	}
}
