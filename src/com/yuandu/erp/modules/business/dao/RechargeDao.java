package com.yuandu.erp.modules.business.dao;

import org.apache.ibatis.annotations.Param;

import com.yuandu.erp.common.persistence.CrudDao;
import com.yuandu.erp.common.persistence.annotation.MyBatisDao;
import com.yuandu.erp.modules.business.entity.Recharge;

/**
 * 充值DAO接口
 */
@MyBatisDao
public interface RechargeDao extends CrudDao<Recharge> {

	/**
	 * 生成编码
	 * @return
	 */
	public String createOrder();

	/**
	 * 更新状态
	 * @param entity
	 */
	public void updateStatus(Recharge entity);

	/**
	 * 根据订单号查询
	 * @param partnerOrderNo
	 * @return
	 */
	public Recharge getByPartnerOrder(@Param(value="partnerOrderNo")String partnerOrderNo);
	
}