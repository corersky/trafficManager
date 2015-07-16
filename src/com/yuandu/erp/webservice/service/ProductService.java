package com.yuandu.erp.webservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuandu.erp.common.utils.StringUtils;
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
	public ProductResponse productListByMobile(User user,String mobile) throws Exception {
		return BusinessUtil.productListByMobile(user,mobile);
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
	public PartnerOrder queryOrderByPartnerOrderNo(User user,String partnerOrderNo)throws Exception{
		return ProductCacheUtil.getPartnerOrder(user,partnerOrderNo);
	}
	
	/*	
	 * 说明:供合作方给指定手机号充值流量
	 * 接口版本:v1
	 * 接口类型:private
	 * 接口名:order/buyFlow
	 * 缓存时间:1天
	 * 接口鉴权:是
	 * 返回值(Json)*/
	@Transactional(readOnly = false)
	public DefaultResponse buyFlow(User user,String product,String mobile){
		DefaultResponse response = new DefaultResponse();
		try {
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
			response.setMsg("手机：["+mobile+"] 购买失败！");
		}
		
		return response;
	}

	/**
	 * 说明:合作方状态改变通知接口
	 * orderNo
	 * partnerOrderNo
	 * status
	 */
	@Transactional(readOnly = false)
	public void notifyStatus(String orderNo,String partnerOrderNo, String status) throws Exception {
		
		String userId = null;//用户id
		Recharge recharge = ProductCacheUtil.getRecharge(partnerOrderNo);
		if(recharge!=null){
			userId = recharge.getCreateBy().getId();
		}
		if(StringUtils.isEmpty(userId)){//订单不存在
			throw new RuntimeException("订单["+partnerOrderNo+"] 不存在");
		}
		
		User user = UserUtils.get(userId);
		String isRefund = UserUtils.updateBalance(user,orderNo,partnerOrderNo,status);//需要先更新余额  后更新状态
		//更新充值记录
		rechargeService.updateStatus(user,partnerOrderNo,status,isRefund);
		//更新运营商订单
		partnerOrderService.updateStatus(user,partnerOrderNo,status);
		ProductCacheUtil.clearCache(partnerOrderNo);
	}
	
}