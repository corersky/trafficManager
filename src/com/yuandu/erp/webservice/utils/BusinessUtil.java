package com.yuandu.erp.webservice.utils;

import java.util.Date;

import com.yuandu.erp.common.config.Global;
import com.yuandu.erp.common.mapper.JsonMapper;
import com.yuandu.erp.common.security.Digests;
import com.yuandu.erp.common.utils.DateUtils;
import com.yuandu.erp.common.utils.HttpClientUtil;
import com.yuandu.erp.modules.business.entity.Recharge;

public class BusinessUtil {
	
	private static final String split = "/";
	private static final String format = "yyyyMMddHHmmss";
	
	/**
	 * 获取共key
	 * @return
	 */
	public static final String getPublicKey(){
		
		String weburl = Global.getConfig("flow.weburl");
		String version = Global.getConfig("flow.version");
		String partnerID = Global.getConfig("flow.partnerID");
		
		String url = weburl+version+split+"public"+split+partnerID+split+"common"+split+"getPublicKey";
		
		String result = HttpClientUtil.doGetURL(url);
		
		PublicKeyResponse response = (PublicKeyResponse) JsonMapper.fromJsonString(result, PublicKeyResponse.class);
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
	public static final void buyFlow(Recharge recharge) throws Exception{
		String mobile = recharge.getMobile();		//充值的手机号
		String productId = recharge.getProductId();	//产品ID
		String partnerOrderNo = recharge.getPartnerOrderNo();	//合作方订单id
		String notifyUrl = recharge.getNotifyUrl();				//回调地址
		

		String authAppkey = Global.getConfig("flow.authAppkey");
		String appSecrect = Global.getConfig("flow.appSecrect");
		String authTimespan = DateUtils.formatDate(new Date(), format);
		
		StringBuffer param = new StringBuffer();
		param.append(authAppkey);
		param.append("authTimespan="+authTimespan);
		param.append(" mobile="+PublicKeyUtil.encodeMsg(mobile));
		param.append(" productId="+productId);
		param.append(" partnerOrderNo="+partnerOrderNo);
		param.append(" notifyUrl="+notifyUrl);
		param.append(appSecrect);
		
		//获取authSign
		String authSign = Digests.str2Md5(param.toString());
		
		String weburl = Global.getConfig("flow.weburl");
		String version = Global.getConfig("flow.version");
		String partnerID = Global.getConfig("flow.partnerID");
		
		//拼接geturl
		StringBuffer url = new StringBuffer(weburl+version+split+"private"+split+partnerID+split+"order/buyFlow?");
		url.append("mobile="+mobile).append("&productId="+productId).append("&partnerOrderNo="+partnerOrderNo)
			.append("&notifyUrl="+notifyUrl).append("&authTimespan="+authTimespan).append("&authAppkey="+authAppkey)
			.append("&authSign="+authSign);
		
		String result = HttpClientUtil.doGetURL(url.toString());
		
	}
	
	/*
	 * 根据合作方的订单号查询订单信息
	 * 版本号:v1
	 * 接口类型:private
	 * 接口名:order/queryOrderByPartnerOrderNo
	 * 缓存时间:0
	 * 接口鉴权:是
	 */
	public static final void queryOrderByPartnerOrderNo(String partnerOrderNo){
		String authAppkey = Global.getConfig("flow.authAppkey");
		String appSecrect = Global.getConfig("flow.appSecrect");
		String authTimespan = DateUtils.formatDate(new Date(), format);
		
		StringBuffer param = new StringBuffer();
		param.append(authAppkey);
		param.append("authTimespan="+authTimespan);
		param.append(" partnerOrderNo="+partnerOrderNo);
		param.append(appSecrect);
		
		//获取authSign
		String authSign = Digests.str2Md5(param.toString());
		
		String weburl = Global.getConfig("flow.weburl");
		String version = Global.getConfig("flow.version");
		String partnerID = Global.getConfig("flow.partnerID");
		
		//拼接geturl
		StringBuffer url = new StringBuffer(weburl+version+split+"private"+split+partnerID+split+"order/queryOrderByPartnerOrderNo?");
		url.append("partnerOrderNo="+partnerOrderNo).append("&authTimespan="+authTimespan)
			.append("&authAppkey="+authAppkey).append("&authSign="+authSign);
		
		String result = HttpClientUtil.doGetURL(url.toString());
		
		System.out.println(result);
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
	public static final ProductResponse productList(){
		String authAppkey = Global.getConfig("flow.authAppkey");
		String appSecrect = Global.getConfig("flow.appSecrect");
		String authTimespan = DateUtils.formatDate(new Date(), format);
		
		StringBuffer param = new StringBuffer();
		param.append(authAppkey);
		param.append("authTimespan="+authTimespan);
		param.append(appSecrect);
		
		//获取authSign
		String authSign = Digests.str2Md5(param.toString());
		
		String weburl = Global.getConfig("flow.weburl");
		String version = Global.getConfig("flow.version");
		String partnerID = Global.getConfig("flow.partnerID");
		
		//拼接geturl
		StringBuffer url = new StringBuffer(weburl+version+split+"private"+split+partnerID+split+"product/productList?");
		url.append("authTimespan="+authTimespan).append("&authAppkey="+authAppkey).append("&authSign="+authSign);
		
		String result = HttpClientUtil.doGetURL(url.toString());
		ProductResponse response = (ProductResponse) JsonMapper.fromJsonString(result, ProductResponse.class);
		
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
		
		StringBuffer param = new StringBuffer();
		param.append(authAppkey);
		param.append("authTimespan="+authTimespan);
		param.append(" mobile="+PublicKeyUtil.encodeMsg(mobile));
		param.append(appSecrect);
		
		//获取authSign
		String authSign = Digests.str2Md5(param.toString());
		
		String weburl = Global.getConfig("flow.weburl");
		String version = Global.getConfig("flow.version");
		String partnerID = Global.getConfig("flow.partnerID");
		
		//拼接geturl
		StringBuffer url = new StringBuffer(weburl+version+split+"private"+split+partnerID+split+"product/productListByMobile?");
		url.append("mobile="+mobile).append("&authTimespan="+authTimespan).append("&authAppkey="+authAppkey).append("&authSign="+authSign);
		
		String result = HttpClientUtil.doGetURL(url.toString());
		ProductResponse response = (ProductResponse) JsonMapper.fromJsonString(result, ProductResponse.class);
		
		return response;
	}
	
	public static void main(String[] args) throws Exception {
		
		
//		String mobile = "LnGVsL8iIuN/nKWZqKmrTHQI7E2TD5de3q+QknG/6U+zVRYbBxrasVuLw+gE9hPw3BnDZaK1w287I4lP+L7Ht5TgexXYUDS1YOrw97sQe8T5kPQkMtrCxowIFi3yB1XCesa5nQr6GBSm8eiiYhovy7YMDvDjXhW+qu2wg4/WRjE=";		//充值的手机号
//		String productId = "1";	//产品ID
//		String partnerOrderNo = "2";	//合作方订单id
//		String notifyUrl = "";				//回调地址
//		
//
//		String authAppkey = Global.getConfig("flow.authAppkey");
//		String appSecrect = Global.getConfig("flow.appSecrect");
//		String authTimespan = DateUtils.formatDate(new Date(), format);
//		
//		
//		String pk = getPublicKey();
//		EncrypRSA rsaUtil = EncrypRSA.create();
//		
//		StringBuffer param = new StringBuffer();
//		param.append(authAppkey);
//		param.append("authTimespan="+authTimespan);
//		param.append(" mobile="+mobile);
//		param.append(" productId="+productId);
//		param.append(" partnerOrderNo="+partnerOrderNo);
//		param.append(" notifyUrl="+notifyUrl);
//		param.append(appSecrect);
//		
//		System.out.println(param);
//		
//		//获取authSign
//		String authSign = Digests.str2Md5(param.toString());
//		
//		String weburl = Global.getConfig("flow.weburl");
//		String version = Global.getConfig("flow.version");
//		String partnerID = Global.getConfig("flow.partnerID");
//		
//		//拼接geturl
//		StringBuffer url = new StringBuffer(weburl+version+split+"private"+split+partnerID+split+"order/buyFlow?");
//		url.append("mobile="+mobile).append("&productId="+productId).append("&partnerOrderNo="+partnerOrderNo)
//			.append("&notifyUrl="+notifyUrl).append("&authTimespan="+authTimespan).append("&authAppkey="+authAppkey)
//			.append("&authSign="+authSign);
//		
//		String result = HttpClientUtil.doGetURL(url.toString());
//		System.out.println(url);
//		System.out.println(result);
		
		EncrypRSA rsaUtil = EncrypRSA.create();
		String mobile = "LnGVsL8iIuN/nKWZqKmrTHQI7E2TD5de3q+QknG/6U+zVRYbBxrasVuLw+gE9hPw3BnDZaK1w287I4lP+L7Ht5TgexXYUDS1YOrw97sQe8T5kPQkMtrCxowIFi3yB1XCesa5nQr6GBSm8eiiYhovy7YMDvDjXhW+qu2wg4/WRjE=";//rsaUtil.encrypt(getPublicKey(), "18611966751");
		System.out.println("-----------"+mobile);
		String authAppkey = Global.getConfig("flow.authAppkey");
		String appSecrect = Global.getConfig("flow.appSecrect");
		String authTimespan = DateUtils.formatDate(new Date(), format);
		
		StringBuffer param = new StringBuffer();
		param.append(authAppkey);
		param.append("authTimespan="+authTimespan);
		
		param.append(" mobile="+mobile);
		param.append(appSecrect);
		
		//获取authSign
		String authSign = Digests.str2Md5(param.toString());
		
		String weburl = Global.getConfig("flow.weburl");
		String version = Global.getConfig("flow.version");
		String partnerID = Global.getConfig("flow.partnerID");
		
		//拼接geturl
		StringBuffer url = new StringBuffer(weburl+version+split+"private"+split+partnerID+split+"product/productListByMobile?");
		url.append("mobile="+mobile).append("&authTimespan="+authTimespan).append("&authAppkey="+authAppkey).append("&authSign="+authSign);
		
		String result = HttpClientUtil.doGetURL(url.toString());
		System.out.println(url);
		System.out.println(result);
	}
	
}
