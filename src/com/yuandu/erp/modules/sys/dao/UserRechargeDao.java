package com.yuandu.erp.modules.sys.dao;

import com.yuandu.erp.common.persistence.CrudDao;
import com.yuandu.erp.common.persistence.annotation.MyBatisDao;
import com.yuandu.erp.modules.sys.entity.UserRecharge;

/**
 * 用户充值DAO接口
 */
@MyBatisDao
public interface UserRechargeDao extends CrudDao<UserRecharge> {

}
