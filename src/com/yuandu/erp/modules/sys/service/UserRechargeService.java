package com.yuandu.erp.modules.sys.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuandu.erp.common.persistence.Page;
import com.yuandu.erp.common.service.CrudService;
import com.yuandu.erp.modules.sys.dao.UserRechargeDao;
import com.yuandu.erp.modules.sys.entity.UserRecharge;
import com.yuandu.erp.modules.sys.utils.UserUtils;

/**
 * 日志Service
 */
@Service
@Transactional(readOnly = true)
public class UserRechargeService extends CrudService<UserRechargeDao, UserRecharge> {

	public Page<UserRecharge> findPage(Page<UserRecharge> page, UserRecharge recharge) {
		recharge.getSqlMap().put("dsf", dataScopeFilter(UserUtils.getUser(), "o", "u"));
		recharge.setPage(page);
		page.setList(dao.findList(recharge));
		return page;
	}

	public Page<UserRecharge> findPageSupplier(Page<UserRecharge> page, UserRecharge recharge) {
		recharge.setPage(page);
		page.setList(dao.findList(recharge));
		return page;
	}
	
}
