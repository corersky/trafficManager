package com.yuandu.erp.modules.sys.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yuandu.erp.common.persistence.FlexPage;
import com.yuandu.erp.common.web.BaseController;
import com.yuandu.erp.modules.sys.entity.Log;
import com.yuandu.erp.modules.sys.service.LogService;

/**
 * 日志Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/log")
public class LogController extends BaseController {

	@Autowired
	private LogService logService;
	
	@RequiresPermissions("sys:log:view")
	@RequestMapping(value = {"list", ""})
	public String list(Log log, HttpServletRequest request, HttpServletResponse response, Model model) {
        model.addAttribute("log", log);
		return "modules/sys/logList";
	}
	
	@RequiresPermissions("sys:role:view")
	@RequestMapping(value = "findPage")
	public @ResponseBody FlexPage<Log> findPage(Log log,HttpServletRequest request, HttpServletResponse response, Model model) {
		FlexPage<Log> page = logService.findPage(new FlexPage<Log>(request, response), log); 
		return page;
	}

}
