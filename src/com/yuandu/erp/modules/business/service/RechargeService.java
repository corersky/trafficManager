package com.yuandu.erp.modules.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuandu.erp.common.persistence.Page;
import com.yuandu.erp.common.service.CrudService;
import com.yuandu.erp.modules.business.dao.RechargeDao;
import com.yuandu.erp.modules.business.entity.PartnerOrder;
import com.yuandu.erp.modules.business.entity.Recharge;
import com.yuandu.erp.modules.sys.dao.UserDao;
import com.yuandu.erp.modules.sys.entity.User;
import com.yuandu.erp.modules.sys.utils.UserUtils;
import com.yuandu.erp.webservice.utils.BusinessUtil;
import com.yuandu.erp.webservice.utils.DefaultResponse;
import com.yuandu.erp.webservice.utils.ProductCacheUtil;

/**
 * 充值Service
 */
@Service
@Transactional(readOnly = true)
public class RechargeService extends CrudService<RechargeDao, Recharge> {

	@Autowired
	private UserDao userDao;
	@Autowired
	private PartnerOrderService partnerOrderService;

	public Recharge get(String id) {
		Recharge entity = dao.get(id);
		return entity;
	}
	
	public Page<Recharge> find(Page<Recharge> page, Recharge recharge) {
		recharge.getSqlMap().put("dsf", dataScopeFilter(UserUtils.getUser(), "o", "u"));
		recharge.setPage(page);
		page.setList(dao.findList(recharge));
		return page;
	}
	
	@Transactional(readOnly = false)
	public DefaultResponse saveRecharge(Recharge recharge) throws Exception {
		
		recharge.preInsert();
		// 先调用充值接口
		DefaultResponse response = BusinessUtil.buyFlow(recharge);
		String orderNo = response.getData().get("orderNo");
		recharge.setOrderNo(orderNo);
		recharge.setNotifyUrl(Recharge.notify_url);
		
		User user = UserUtils.getUser();
		if(recharge.getCreateBy()!=null&&recharge.getCreateBy().getId()!=null){
			user = recharge.getCreateBy();
		}
		// 调用查询接口
		PartnerOrder order = ProductCacheUtil.getPartnerOrder(user,recharge.getPartnerOrderNo());
		// 保存order方便查询
		order.setId(null);
		partnerOrderService.save(order);
		String status = order.getStatus();
		
		recharge.setStatus(status);
		recharge.setType(order.getFlowType());
		recharge.setFlowSize(order.getFlowSize());
		//设置balance
		Double balance = order.getBalance();
		recharge.setFeeRate(order.getFeeRate());
		recharge.setFee(order.getFee());//实际单价
		recharge.setBalance(balance);
		recharge.setAdminFee(order.getAdminFee());
		
		//更新用户余额（直接扣款  后期退款  超级管理员扣款）
		UserUtils.purchaseBalance(UserUtils.getUser(),recharge.getBalance(),recharge.getAdminFee(),order.getPartnerOrderNo());
		dao.insert(recharge);//后插入
		
		return response;
	}
	
	/**
	 * 产生订单号
	 * @return
	 */
	@Transactional(readOnly = false)
	public String createOrder(){
		return dao.createOrder();
	}

	/**
	 * 更新账单状态
	 * @param partnerOrderNo
	 * @param status
	 * @throws Exception 
	 */
	@Transactional(readOnly = false)
	public void updateStatus(User user,String partnerOrderNo, String status,String isRefund) throws Exception {
		
		Recharge entity = new Recharge();
		entity.setPartnerOrderNo(partnerOrderNo);
		entity.setStatus(status);
		entity.setUpdateBy(user);
		entity.setIsRefund(isRefund);
		entity.preUpdate();
		
		dao.updateStatus(entity);
	}

	/**
	 * 根据订单号查询
	 * @param partnerOrderNo
	 * @return
	 */
	public Recharge getByPartnerOrder(String partnerOrderNo) {
		return dao.getByPartnerOrder(partnerOrderNo);
	}
}