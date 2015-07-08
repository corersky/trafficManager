package com.yuandu.erp.webservice.utils;

import java.net.URLEncoder;
import java.util.Date;

import com.yuandu.erp.common.config.Global;
import com.yuandu.erp.common.mapper.JsonMapper;
import com.yuandu.erp.common.security.Digests;
import com.yuandu.erp.common.utils.DateUtils;
import com.yuandu.erp.common.utils.HttpClientUtil;
import com.yuandu.erp.common.utils.SpringContextHolder;
import com.yuandu.erp.common.utils.StringUtils;
import com.yuandu.erp.modules.business.entity.PartnerOrder;
import com.yuandu.erp.modules.business.entity.Recharge;
import com.yuandu.erp.modules.business.service.PartnerOrderService;

public class BusinessUtil {
	
	private static final String split = "/";
	private static final String format = "yyyyMMddHHmmss";
	
	private static PartnerOrderService partnerOrderService = SpringContextHolder.getBean(PartnerOrderService.class);
	
	/**
	 * 获取共key
	 * @return
	 * @throws Exception 
	 */
	public static final String getPublicKey() throws Exception{
		
		String weburl = Global.getConfig("flow.weburl");
		String version = Global.getConfig("flow.version");
		String partnerID = Global.getConfig("flow.partnerID");
		
		String url = weburl+version+split+"public"+split+partnerID+split+"common"+split+"getPublicKey";
		
		String result = HttpClientUtil.doGetURL(url);
		DefaultResponse response = (DefaultResponse) JsonMapper.fromJsonString(result, DefaultResponse.class);
		
		response.isCorrect();
		return response.getData().get("publicKey");
	}
	
	/**
	 * 充值流量接口
	 * 供合作方给指定手机号充值流量
	 * 接口类型:private
	 * 接口名:order/buyFlow
	 * 接口鉴权:是
	 * @throws Exception 
	 */
	public static final DefaultResponse buyFlow(Recharge recharge) throws Exception{
		String mobile = recharge.getMobile();		//充值的手机号
		mobile = ProductCacheUtil.encodeMsg(mobile);
		
		String productId = recharge.getProductId();	//产品ID
		String partnerOrderNo = recharge.getPartnerOrderNo();	//合作方订单id
		String notifyUrl = StringUtils.null2Empty(recharge.getNotifyUrl());				//回调地址

		String authAppkey = Global.getConfig("flow.authAppkey");
		String appSecrect = Global.getConfig("flow.appSecrect");
		String authTimespan = DateUtils.formatDate(new Date(), format);
		
		StringBuffer param = new StringBuffer();
		param.append(authAppkey);
		param.append("authTimespan="+authTimespan);
		param.append("mobile="+mobile);
		param.append("notifyUrl="+notifyUrl);
		param.append("partnerOrderNo="+partnerOrderNo);
		param.append("productId="+productId);
		param.append(appSecrect);
		
		//获取authSign
		String authSign = Digests.str2Md5(param.toString());
		authSign = URLEncoder.encode(authSign, "UTF-8");
		
		String weburl = Global.getConfig("flow.weburl");
		String version = Global.getConfig("flow.version");
		String partnerID = Global.getConfig("flow.partnerID");
		
		//拼接geturl
		StringBuffer url = new StringBuffer(weburl+version+split+"private"+split+partnerID+split+"order/buyFlow?");
		url.append("mobile="+URLEncoder.encode(mobile, "UTF-8")).append("&notifyUrl="+notifyUrl).append("&partnerOrderNo="+partnerOrderNo)
			.append("&productId="+productId).append("&authTimespan="+authTimespan).append("&authAppkey="+authAppkey).append("&authSign="+authSign);
		
		String result = HttpClientUtil.doGetURL(url.toString());
		
		DefaultResponse response =(DefaultResponse) JsonMapper.fromJsonString(result, DefaultResponse.class);
		response.isCorrect();
		return response;
	}
	
	/*
	 * 根据合作方的订单号查询订单信息
	 * 版本号:v1
	 * 接口类型:private
	 * 接口名:order/queryOrderByPartnerOrderNo
	 * 缓存时间:0
	 * 接口鉴权:是
	 */
	public static final PartnerOrderResponse queryOrderByPartnerOrderNo(String partnerOrderNo) throws Exception{
		//首先查询数据库  如果数据库 不存在 则httpclient
		PartnerOrder order = partnerOrderService.getByPartnerOrder(partnerOrderNo);
		if(order !=null){
			PartnerOrderResponse response = new PartnerOrderResponse();
			response.setCode("0000");
			response.setData(order);
			
			return response;
		}
		String authAppkey = Global.getConfig("flow.authAppkey");
		String appSecrect = Global.getConfig("flow.appSecrect");
		String authTimespan = DateUtils.formatDate(new Date(), format);
		
		StringBuffer param = new StringBuffer();
		param.append(authAppkey);
		param.append("authTimespan="+authTimespan);
		param.append("partnerOrderNo="+partnerOrderNo);
		param.append(appSecrect);
		
		//获取authSign
		String authSign = Digests.str2Md5(param.toString());
		authSign = URLEncoder.encode(authSign, "UTF-8");
		
		String weburl = Global.getConfig("flow.weburl");
		String version = Global.getConfig("flow.version");
		String partnerID = Global.getConfig("flow.partnerID");
		
		//拼接geturl
		StringBuffer url = new StringBuffer(weburl+version+split+"private"+split+partnerID+split+"order/queryOrderByPartnerOrderNo?");
		url.append("partnerOrderNo="+partnerOrderNo).append("&authTimespan="+authTimespan)
			.append("&authAppkey="+authAppkey).append("&authSign="+authSign);
		
		String result = HttpClientUtil.doGetURL(url.toString());
		
		PartnerOrderResponse response = (PartnerOrderResponse) JsonMapper.fromJsonString(result, PartnerOrderResponse.class);
		response.isCorrect();
		return response;
	}
	
	/*
	 * 获取合作方的可售商品列表
	 * 说明:供合作方自己可使用的商品列表
	 * 版本号:v1
	 * 接口类型:private
	 * 接口名:product/productList
	 * 缓存时间:0
	 * 接口鉴权:是
	 */
	public static final ProductResponse productList() throws Exception{
		String authAppkey = Global.getConfig("flow.authAppkey");
		String appSecrect = Global.getConfig("flow.appSecrect");
		String authTimespan = DateUtils.formatDate(new Date(), format);
		
		StringBuffer param = new StringBuffer();
		param.append(authAppkey);
		param.append("authTimespan="+authTimespan);
		param.append(appSecrect);
		
		//获取authSign
		String authSign = Digests.str2Md5(param.toString());
		authSign = URLEncoder.encode(authSign, "UTF-8");
		
		String weburl = Global.getConfig("flow.weburl");
		String version = Global.getConfig("flow.version");
		String partnerID = Global.getConfig("flow.partnerID");
		
		//拼接geturl
		StringBuffer url = new StringBuffer(weburl+version+split+"private"+split+partnerID+split+"product/productList?");
		url.append("authTimespan="+authTimespan).append("&authAppkey="+authAppkey).append("&authSign="+authSign);
		
		String result = HttpClientUtil.doGetURL(url.toString());
		ProductResponse response = (ProductResponse) JsonMapper.fromJsonString(result, ProductResponse.class);
		response.isCorrect();
		return response;
	}
	
	/*
	 * 根据手机号获取可购买商品列表
	 * 说明:供合作方根据手机号过滤此手机号能使用的商品
	 * 版本号:v1
	 * 接口类型:private
	 * 接口名:product/productListByMobile
 	 * 缓存时间:0
	 * 接口鉴权:是
	 */
	public static final ProductResponse productListByMobile(String mobile) throws Exception{
		String authAppkey = Global.getConfig("flow.authAppkey");
		String appSecrect = Global.getConfig("flow.appSecrect");
		String authTimespan = DateUtils.formatDate(new Date(), format);
		
		mobile = ProductCacheUtil.encodeMsg(mobile);
		
		StringBuffer param = new StringBuffer();
		param.append(authAppkey);
		param.append("authTimespan="+authTimespan);
		param.append("mobile="+mobile);
		param.append(appSecrect);
		
		//获取authSign
		String authSign = Digests.str2Md5(param.toString());
		authSign = URLEncoder.encode(authSign, "UTF-8");
		
		String weburl = Global.getConfig("flow.weburl");
		String version = Global.getConfig("flow.version");
		String partnerID = Global.getConfig("flow.partnerID");
		
		//拼接geturl
		StringBuffer url = new StringBuffer(weburl+version+split+"private"+split+partnerID+split+"product/productListByMobile?");
		url.append("mobile="+URLEncoder.encode(mobile, "UTF-8")).append("&authTimespan="+authTimespan).append("&authAppkey="+authAppkey).append("&authSign="+authSign);
		
		String result = HttpClientUtil.doGetURL(url.toString());
		ProductResponse response = (ProductResponse) JsonMapper.fromJsonString(result, ProductResponse.class);
		response.isCorrect();
		return response;
	}
	
}
