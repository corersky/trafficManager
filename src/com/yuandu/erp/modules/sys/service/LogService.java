package com.yuandu.erp.modules.sys.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuandu.erp.common.persistence.FlexPage;
import com.yuandu.erp.common.service.CrudService;
import com.yuandu.erp.common.utils.DateUtils;
import com.yuandu.erp.modules.sys.dao.LogDao;
import com.yuandu.erp.modules.sys.entity.Log;

/**
 * 日志Service
 */
@Service
@Transactional(readOnly = true)
public class LogService extends CrudService<LogDao, Log> {

	public FlexPage<Log> findPage(FlexPage<Log> page, Log log) {
		
		// 设置默认时间范围，默认当前月
		if (log.getBeginDate() == null){
			log.setBeginDate(DateUtils.setDays(DateUtils.parseDate(DateUtils.getDate()), 1));
		}
		if (log.getEndDate() == null){
			log.setEndDate(DateUtils.addMonths(log.getBeginDate(), 1));
		}
		
		return super.findPage(page, log);
		
	}
	
}
