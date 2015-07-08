package com.yuandu.erp.webservice.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yuandu.erp.modules.business.service.PartnerOrderService;
import com.yuandu.erp.modules.business.service.RechargeService;
import com.yuandu.erp.webservice.bean.ProductPojo;
import com.yuandu.erp.webservice.utils.ProductCacheUtil;

/**
 * 用户付费Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/product")
public class ProductController {
	
	@Autowired
	private RechargeService rechargeService;
	@Autowired
	private PartnerOrderService partnerOrderService;
	
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
	public @ResponseBody String notifyStatus(@RequestParam long orderNo,@RequestParam String partnerOrderNo
			,@RequestParam String status) throws Exception {
		
		//更新充值记录
		rechargeService.updateStatus(partnerOrderNo,status);
		//更新运营商订单
		partnerOrderService.updateStatus(partnerOrderNo,status);
		ProductCacheUtil.updatePartnerOrder(partnerOrderNo,status);
		
		return "订单：["+partnerOrderNo+"]  状态更新成功";
	}
}
