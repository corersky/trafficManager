package com.yuandu.erp.modules.business.dao;

import com.yuandu.erp.common.persistence.CrudDao;
import com.yuandu.erp.common.persistence.annotation.MyBatisDao;
import com.yuandu.erp.modules.business.entity.Recharge;

/**
 * 充值DAO接口
 */
@MyBatisDao
public interface RechargeDao extends CrudDao<Recharge> {
	
	
}