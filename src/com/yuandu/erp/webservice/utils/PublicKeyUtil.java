package com.yuandu.erp.webservice.utils;

import com.yuandu.erp.common.utils.CacheUtils;
import com.yuandu.erp.common.utils.SpringContextHolder;
import com.yuandu.erp.common.utils.StringUtils;
import com.yuandu.erp.webservice.service.ProductService;

public class PublicKeyUtil {
	
	private static ProductService productService = SpringContextHolder.getBean(ProductService.class);
	
	private static final String PUBLIC_KEY_CACHE = "publicKeyCache";
	private static final String PUBLIC_KEY_CACHE_ID = "public_key_cache_id";
	
	/**
	 * 获取公钥publickey
	 * @param id
	 * @return 取不到返回null
	 */
	public static String get(){
		String pulickey = (String) CacheUtils.get(PUBLIC_KEY_CACHE, PUBLIC_KEY_CACHE_ID);
		if (StringUtils.isEmpty(pulickey)){
			//接口获取publickey
			pulickey = productService.getPublicKey();

			CacheUtils.put(PUBLIC_KEY_CACHE, PUBLIC_KEY_CACHE_ID, pulickey);
		}
		return pulickey;
	}
	
	public static String encodeMsg(String msg) throws Exception{
		EncrypRSA rsaUtil = EncrypRSA.create();
		
		return rsaUtil.encrypt(get(),msg);
	}

}
