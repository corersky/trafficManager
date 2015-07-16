package com.yuandu.erp.modules.business.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuandu.erp.common.persistence.Page;
import com.yuandu.erp.common.service.CrudService;
import com.yuandu.erp.modules.business.dao.PartnerOrderDao;
import com.yuandu.erp.modules.business.entity.PartnerOrder;
import com.yuandu.erp.modules.sys.entity.User;

/**
 * 运营商订单Service
 */
@Service
@Transactional(readOnly = true)
public class PartnerOrderService extends CrudService<PartnerOrderDao, PartnerOrder> {


	public PartnerOrder get(String id) {
		PartnerOrder entity = dao.get(id);
		return entity;
	}
	
	public Page<PartnerOrder> find(Page<PartnerOrder> page, PartnerOrder partnerOrder) {
		partnerOrder.setPage(page);
		page.setList(dao.findList(partnerOrder));
		return page;
	}
	
	@Transactional(readOnly = false)
	public void save(PartnerOrder entity) {
		super.save(entity);
	}

	@Transactional(readOnly = false)
	public void updateStatus(User user,String partnerOrderNo, String status) throws Exception {
		
		PartnerOrder entity = new PartnerOrder();
		entity.setPartnerOrderNo(partnerOrderNo);
		entity.setStatus(status);
		entity.setUpdateBy(user);
		entity.preUpdate();
		
		dao.updateStatus(entity);
	}

	public PartnerOrder getByPartnerOrder(String partnerOrderNo) {
		
		return dao.getByPartnerOrder(partnerOrderNo);
	}
}