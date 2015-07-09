package com.yuandu.erp.modules.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuandu.erp.common.persistence.Page;
import com.yuandu.erp.common.service.CrudService;
import com.yuandu.erp.common.utils.StringUtils;
import com.yuandu.erp.modules.business.dao.RechargeDao;
import com.yuandu.erp.modules.business.entity.PartnerOrder;
import com.yuandu.erp.modules.business.entity.Recharge;
import com.yuandu.erp.modules.sys.dao.UserDao;
import com.yuandu.erp.modules.sys.entity.User;
import com.yuandu.erp.modules.sys.utils.DictUtils;
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
		
		// 调用查询接口
		PartnerOrder order = ProductCacheUtil.getPartnerOrder(recharge.getPartnerOrderNo());
		String status = order.getStatus();
		
		recharge.setStatus(status);
		recharge.setType(order.getFlowType());
		recharge.setFlowSize(order.getFlowSize());
		//设置balance
		Double balance = order.getFee();
		User user = UserUtils.getUser();
		if(recharge.getCreateBy()!=null&&recharge.getCreateBy().getId()!=null){
			user = recharge.getCreateBy();
		}
		Double userRate = user.getFeeRate();
		if(balance!=null&&userRate!=null){
			String adminRate = DictUtils.getDictValue("公司商务汇率", "company_rate", "1");
			double rate = StringUtils.toDouble(adminRate);
			balance = balance/rate * userRate;
			recharge.setBalance(balance);
		}
		dao.insert(recharge);
		//更新用户余额
		UserUtils.updateBalance(UserUtils.getUser(),recharge.getBalance(), status);
		
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
	 */
	public void updateStatus(String partnerOrderNo, String status) {
		Recharge entity = new Recharge();
		entity.setPartnerOrderNo(partnerOrderNo);
		entity.setStatus(status);
		
		entity.preUpdate();
		dao.updateStatus(entity);
	}
}