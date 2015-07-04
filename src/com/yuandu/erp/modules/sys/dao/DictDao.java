package com.yuandu.erp.modules.sys.dao;

import java.util.List;

import com.yuandu.erp.common.persistence.CrudDao;
import com.yuandu.erp.common.persistence.annotation.MyBatisDao;
import com.yuandu.erp.modules.sys.entity.Dict;

/**
 * 字典DAO接口
 */
@MyBatisDao
public interface DictDao extends CrudDao<Dict> {

	public List<String> findTypeList(Dict dict);
	
}
