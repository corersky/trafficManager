package com.yuandu.erp.webservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuandu.erp.modules.business.entity.PartnerOrder;
import com.yuandu.erp.modules.business.entity.Recharge;
import com.yuandu.erp.modules.business.service.PartnerOrderService;
import com.yuandu.erp.modules.business.service.RechargeService;
import com.yuandu.erp.modules.sys.entity.User;
import com.yuandu.erp.modules.sys.utils.UserUtils;
import com.yuandu.erp.webservice.utils.BusinessUtil;
import com.yuandu.erp.webservice.utils.DefaultResponse;
import com.yuandu.erp.webservice.utils.ProductCacheUtil;
import com.yuandu.erp.webservice.utils.ProductResponse;

/**
 * 充值Service
 */
@Service
@Transactional(readOnly = true)
public class ProductService {
	
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
	public ProductResponse productListByMobile(String mobile) throws Exception {
		
		return BusinessUtil.productListByMobile(mobile);
	}

	/*	
	 * 说明:公钥获取,用户登录等接口的数据加密
	 * 接口版本:v1
	 * 接口类型:public
	 * 接口名:common/getPublicKey
	 * 缓存时间:1天
	 * 接口鉴权:否
	 * 返回值(Json) publickey RSA公钥*/
	public String getPublicKey() throws Exception{
		String response = ProductCacheUtil.getPubKey();
		
		return response;
	}
	
	/* 
	 * 供合作方查询订单信息 需要使用缓存
	 * 接口版本:v1
	 * 接口类型:private
	 * 接口名:order/queryOrderByPartnerOrderNo
	 * 缓存时间:0天
	 * 接口鉴权:是
	 */
	public PartnerOrder queryOrderByPartnerOrderNo(String partnerOrderNo)throws Exception{
		return ProductCacheUtil.getPartnerOrder(partnerOrderNo);
	}
	
	/*	
	 * 说明:供合作方给指定手机号充值流量
	 * 接口版本:v1
	 * 接口类型:private
	 * 接口名:order/buyFlow
	 * 缓存时间:1天
	 * 接口鉴权:是
	 * 返回值(Json)*/
	public DefaultResponse buyFlow(String channel,String product,String mobile){
		DefaultResponse response = new DefaultResponse();
		try {
			//判断用户合法
			User user = UserUtils.getByNo(channel);
			if(user==null){
				response.setCode("0001");
				response.setMsg("非法用户："+channel);
				return response;
			}
			Recharge recharge = new Recharge();
			recharge.setMobile(mobile);
			recharge.setProductId(product);
			recharge.setCreateBy(user);
			//生成订单编号
			String partnerOrderNo = rechargeService.createOrder();
			recharge.setPartnerOrderNo(partnerOrderNo);
		
			return rechargeService.saveRecharge(recharge);
		} catch (Exception e) {
			response.setCode("0001");
			response.setMsg("系统错误："+e.getMessage());
		}
		
		return response;
	}

	public void notifyStatus(String partnerOrderNo, String status) throws Exception {
		
		UserUtils.updateBalance(partnerOrderNo,status);//需要先更新余额  后更新状态
		//更新充值记录
		rechargeService.updateStatus(partnerOrderNo,status);
		//更新运营商订单
		partnerOrderService.updateStatus(partnerOrderNo,status);
		ProductCacheUtil.clearCache(partnerOrderNo);

	}
	
}