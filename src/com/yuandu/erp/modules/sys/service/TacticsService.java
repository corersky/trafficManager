package com.yuandu.erp.modules.sys.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuandu.erp.common.service.CrudService;
import com.yuandu.erp.modules.sys.dao.TacticsDao;
import com.yuandu.erp.modules.sys.entity.Tactics;
import com.yuandu.erp.modules.sys.utils.UserUtils;

/**
 * 区域Service
 */
@Service
@Transactional(readOnly = true)
public class TacticsService extends CrudService<TacticsDao, Tactics> {

	@Transactional(readOnly = false)
	public void save(Tactics tactics) {
		super.save(tactics);
		UserUtils.clearCache(tactics.getUser());
	}
	
	@Transactional(readOnly = false)
	public void delete(Tactics tactics) {
		super.delete(tactics);
		UserUtils.clearCache(tactics.getUser());
	}
	
	@Transactional(readOnly = false)
	public void deleteByUserId(Tactics tactics) {
		dao.deleteByUserId(tactics);
		UserUtils.clearCache(tactics.getUser());
	}
	
}
