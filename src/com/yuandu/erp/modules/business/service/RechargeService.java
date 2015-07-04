package com.yuandu.erp.modules.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuandu.erp.common.persistence.Page;
import com.yuandu.erp.common.service.CrudService;
import com.yuandu.erp.modules.business.dao.RechargeDao;
import com.yuandu.erp.modules.business.entity.Recharge;
import com.yuandu.erp.modules.sys.dao.UserDao;
import com.yuandu.erp.modules.sys.entity.User;
import com.yuandu.erp.modules.sys.utils.UserUtils;

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
		recharge.setPage(page);
		page.setList(dao.findList(recharge));
		return page;
	}
	
	@Transactional(readOnly = false)
	public void save(Recharge recharge) {
		// 先调用充值接口
		super.save(recharge);
		
		// 更新用户可用余额
		User user = new User();
		user.setId(UserUtils.getUser().getId());
		user.setFlowCount(recharge.getFlowCount());
		userDao.updateFlowCount(user);
	}
}