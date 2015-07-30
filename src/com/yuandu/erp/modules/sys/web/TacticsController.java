package com.yuandu.erp.modules.sys.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Maps;
import com.yuandu.erp.common.utils.StringUtils;
import com.yuandu.erp.common.web.BaseController;
import com.yuandu.erp.modules.sys.entity.Tactics;
import com.yuandu.erp.modules.sys.service.TacticsService;

/**
 * 登录Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/tactics")
public class TacticsController extends BaseController{
	
	@Autowired
	private TacticsService tacticsService;
	
	@ModelAttribute("tactics")
	public Tactics get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return tacticsService.get(id);
		}else{
			return new Tactics();
		}
	}
	
	@RequiresPermissions("sys:user:view")
	@RequestMapping(value = "tacticsList")
	public String tacticsList(Tactics tactics, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<Tactics> tacticsList = tacticsService.findList(tactics);
		
		model.addAttribute("tacticsList", tacticsList);
		return "modules/sys/tacticsList";
	}
	
	@RequiresPermissions("sys:user:edit")
	@RequestMapping(value = "delete")
	public String delete(Tactics tactics, RedirectAttributes redirectAttributes) {
		tacticsService.delete(tactics);
		addMessage(redirectAttributes, "删除策略成功");
		return "redirect:" + adminPath + "/sys/tactics/tacticsList?user.id="+tactics.getUser().getId()
				+"&user.name="+tactics.getUser().getName();
	}
	
	@RequiresPermissions("sys:user:edit")
	@RequestMapping(value = "form")
	public String form(Tactics tactics, Model model) {
		model.addAttribute("tactics", tactics);
		return "modules/sys/tacticsForm";
	}
	
	@RequiresPermissions("sys:user:edit")
	@RequestMapping(value = "save")
	public @ResponseBody Map<String,Object> save(Tactics tactics, Model model, RedirectAttributes redirectAttributes) {
		Map<String,Object> result = Maps.newHashMap();
		
		if (!beanValidator(model, tactics)){
			result.put("success", false);
			result.put("msg", "数据校验错误");
		}
		tacticsService.save(tactics);
		
		result.put("success", true);
		result.put("msg", "保存策略成功");
		return result;
	}

}
