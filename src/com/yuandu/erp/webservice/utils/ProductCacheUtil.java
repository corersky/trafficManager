package com.yuandu.erp.webservice.utils;

import com.yuandu.erp.common.utils.CacheUtils;
import com.yuandu.erp.common.utils.StringUtils;
import com.yuandu.erp.modules.business.entity.PartnerOrder;

public class ProductCacheUtil {
	
	private static final String PUBLIC_KEY_CACHE = "publicKeyCache";
	private static final String PUBLIC_KEY_CACHE_ID = "public_key_cache_id";
	
	private static final String RECHARGE_PARTNER_ORDER = "partnerOrderCache";
	
	/**
	 * 获取公钥publickey
	 * @param id
	 * @return 取不到返回null
	 * @throws Exception 
	 */
	public static String getPubKey() throws Exception{
		String pulickey = (String) CacheUtils.get(PUBLIC_KEY_CACHE, PUBLIC_KEY_CACHE_ID);
		if (StringUtils.isEmpty(pulickey)){
			//接口获取publickey
			pulickey = BusinessUtil.getPublicKey();

			CacheUtils.put(PUBLIC_KEY_CACHE, PUBLIC_KEY_CACHE_ID, pulickey);
		}
		return pulickey;
	}
	
	public static String encodeMsg(String msg) throws Exception{
		EncrypRSA rsaUtil = EncrypRSA.create();
		
		return rsaUtil.encrypt(getPubKey(),msg);
	}

	/**
	 * 获取缓存订单信息
	 * @param partnerOrder
	 * @return
	 * @throws Exception
	 */
	public static PartnerOrder getPartnerOrder(String partnerOrder) throws Exception{
		PartnerOrder order = (PartnerOrder) CacheUtils.get(RECHARGE_PARTNER_ORDER, partnerOrder);
		//添加缓存
		if (order==null){
			PartnerOrderResponse response = BusinessUtil.queryOrderByPartnerOrderNo(partnerOrder);
			order = response.getData();

			CacheUtils.put(RECHARGE_PARTNER_ORDER, partnerOrder, order);
		}
		return order;
	}

	public static void updatePartnerOrder(String partnerOrderNo, String status) throws Exception {
		PartnerOrder order = getPartnerOrder(partnerOrderNo);
		//更新缓存
		if (order!=null){
			order.setStatus(status);
		}
	}
}
