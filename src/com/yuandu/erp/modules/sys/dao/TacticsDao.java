package com.yuandu.erp.modules.sys.dao;

import com.yuandu.erp.common.persistence.CrudDao;
import com.yuandu.erp.common.persistence.annotation.MyBatisDao;
import com.yuandu.erp.modules.sys.entity.Tactics;

/**
 * 区域DAO接口
 */
@MyBatisDao
public interface TacticsDao extends CrudDao<Tactics> {

	void deleteByUserId(Tactics tactics);
	
}
