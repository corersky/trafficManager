package com.yuandu.erp.modules.sys.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.yuandu.erp.common.persistence.Page;
import com.yuandu.erp.common.utils.StringUtils;
import com.yuandu.erp.common.web.BaseController;
import com.yuandu.erp.modules.sys.entity.UserRecharge;
import com.yuandu.erp.modules.sys.service.UserRechargeService;
import com.yuandu.erp.modules.sys.utils.UserUtils;

@Controller
@RequestMapping(value = "${adminPath}/sys/userRecharge")
public class UserRechargeController extends BaseController{
	
	@Autowired
	private UserRechargeService userRechargeService;
	
	@ModelAttribute
	public UserRecharge get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return userRechargeService.get(id);
		}else{
			return new UserRecharge();
		}
	}
	
	/**
	 * 查询我的购买记录
	 * @param userRecharge
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:user.recharge:view")
	@RequestMapping(value = {"list", ""})
	public String list(UserRecharge userRecharge, HttpServletRequest request, HttpServletResponse response, Model model) {
		userRecharge.setSupplier(UserUtils.getUser());
		Page<UserRecharge> page = userRechargeService.findPageSupplier(new Page<UserRecharge>(request, response), userRecharge);
        model.addAttribute("page", page);
		return "modules/sys/userRechargeList";
	}

	/**
	 * 查询我的销售记录
	 * @param userRecharge
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys.user..recharge.sale.edit")
	@RequestMapping(value = "delete")
	public String delete(UserRecharge userRecharge, RedirectAttributes redirectAttributes) {
		userRechargeService.delete(userRecharge);
		addMessage(redirectAttributes, "删除充值纪录成功");
		return "redirect:" + adminPath + "/sys/userRecharge/list?repage";
	}
	
	@RequiresPermissions("sys.user..recharge.sale.view")
	@RequestMapping(value = "marketSale")
	public String marketSale(UserRecharge userRecharge, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<UserRecharge> page = userRechargeService.findPage(new Page<UserRecharge>(request, response), userRecharge);
		model.addAttribute("page", page);
		return "modules/sys/marketSaleList";
	}
	
}
