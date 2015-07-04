package com.yuandu.erp.modules.sys.dao;

import com.yuandu.erp.common.persistence.TreeDao;
import com.yuandu.erp.common.persistence.annotation.MyBatisDao;
import com.yuandu.erp.modules.sys.entity.Office;

/**
 * 机构DAO接口
 */
@MyBatisDao
public interface OfficeDao extends TreeDao<Office> {
	
}
