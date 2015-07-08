package com.yuandu.erp.modules.business.dao;

import org.apache.ibatis.annotations.Param;

import com.yuandu.erp.common.persistence.CrudDao;
import com.yuandu.erp.common.persistence.annotation.MyBatisDao;
import com.yuandu.erp.modules.business.entity.PartnerOrder;

/**
 * 运营商订单DAO接口
 */
@MyBatisDao
public interface PartnerOrderDao extends CrudDao<PartnerOrder> {

	void updateStatus(PartnerOrder entity);

	/*
	 * 根据第三方订单号查询
	 */
	PartnerOrder getByPartnerOrder(@Param(value="partnerOrderNo")String partnerOrderNo);
}