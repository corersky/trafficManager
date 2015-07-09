package com.yuandu.erp.webservice.utils;

import com.yuandu.erp.common.utils.CacheUtils;
import com.yuandu.erp.common.utils.SpringContextHolder;
import com.yuandu.erp.common.utils.StringUtils;
import com.yuandu.erp.modules.business.entity.PartnerOrder;
import com.yuandu.erp.modules.business.entity.Recharge;
import com.yuandu.erp.modules.business.service.PartnerOrderService;
import com.yuandu.erp.modules.business.service.RechargeService;
import com.yuandu.erp.modules.sys.entity.User;

public class ProductCacheUtil {
	
	private static final String PUBLIC_KEY_CACHE = "publicKeyCache";
	private static final String PUBLIC_KEY_CACHE_ID = "public_key_cache_id";
	
	private static final String RECHARGE_PARTNER_ORDER = "partnerOrderCache";
	private static final String RECHARGE_RECHARGE_ORDER = "rechargeOrderCache";
	
	private static PartnerOrderService partnerOrderService = SpringContextHolder.getBean(PartnerOrderService.class);
	private static RechargeService rechargeService = SpringContextHolder.getBean(RechargeService.class);
	
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
	public static PartnerOrder getPartnerOrder(User user,String partnerOrder) throws Exception{
		PartnerOrder order = (PartnerOrder) CacheUtils.get(RECHARGE_PARTNER_ORDER, partnerOrder);
		//添加缓存
		if (order==null){
			//首先查询数据库  如果数据库 不存在 则httpclient
			order = partnerOrderService.getByPartnerOrder(partnerOrder);
			if(order == null){
				PartnerOrderResponse response = BusinessUtil.queryOrderByPartnerOrderNo(user,partnerOrder);
				order = response.getData();
			}
			CacheUtils.put(RECHARGE_PARTNER_ORDER, partnerOrder, order);
		}
		return order;
	}
	
	/**
	 * 获取缓存本地购买信息
	 * @param partnerOrder
	 * @return
	 * @throws Exception
	 */
	public static Recharge getRecharge(String partnerOrder) throws Exception{
		Recharge recharge = (Recharge) CacheUtils.get(RECHARGE_RECHARGE_ORDER, partnerOrder);
		if (recharge==null){
			//信息获取
			recharge = rechargeService.getByPartnerOrder(partnerOrder);
			CacheUtils.put(RECHARGE_RECHARGE_ORDER, partnerOrder, recharge);//添加缓存
		}
		return recharge;
	}

	public static void clearCache(String partnerOrderNo){
		CacheUtils.remove(RECHARGE_PARTNER_ORDER, partnerOrderNo);
		CacheUtils.remove(RECHARGE_RECHARGE_ORDER, partnerOrderNo);
	}
}
