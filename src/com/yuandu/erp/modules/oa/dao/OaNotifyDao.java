package com.yuandu.erp.modules.oa.dao;

import com.yuandu.erp.common.persistence.CrudDao;
import com.yuandu.erp.common.persistence.annotation.MyBatisDao;
import com.yuandu.erp.modules.oa.entity.OaNotify;

/**
 * 通知通告DAO接口
 */
@MyBatisDao
public interface OaNotifyDao extends CrudDao<OaNotify> {
	
	/**
	 * 获取通知数目
	 * @param oaNotify
	 * @return
	 */
	public Long findCount(OaNotify oaNotify);
	
}