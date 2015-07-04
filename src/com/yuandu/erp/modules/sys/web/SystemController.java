package com.yuandu.erp.modules.sys.web;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Maps;
import com.yuandu.erp.common.utils.FileInptUtil;
import com.yuandu.erp.common.web.BaseController;

/**
 * 用户Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/sysController")
public class SystemController extends BaseController {

	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "deleteFile")
	public Map<String,Object> deleteFile(@RequestParam String id, HttpServletResponse response) {
		Map<String,Object> result = Maps.newHashMap();
		//查询表格  获取  绝对地址
		//判断文件是否存在
		//删除文件
		//返回删除信息
		
		return result;
	}
	
	/**
	 * 文件上传  bootstrap
	 * @param file
	 * @param request
	 * @param response
	 * @return
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "fileUpload")
	public @ResponseBody FileInptUtil fileUpload(@RequestParam(value = "file") MultipartFile file, @RequestParam String type) {  
	    
		FileInptUtil result = FileInptUtil.fileUpload(file, type);
		 
	    return result;
	}
    
}
