package com.yuandu.erp.modules.business.web;

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
import com.yuandu.erp.modules.business.entity.Recharge;
import com.yuandu.erp.modules.business.service.RechargeService;
import com.yuandu.erp.webservice.service.ProductService;
import com.yuandu.erp.webservice.utils.ProductResponse;

/**
 * 用户付费Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/business/recharge")
public class RechargeController extends BaseController {

	@Autowired
	private RechargeService rechargeService;
	@Autowired
	private ProductService productService;
	
	
	@ModelAttribute
	public Recharge get(@RequestParam(required=false) String id) {
		Recharge entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = rechargeService.get(id);
		}
		if (entity == null){
			entity = new Recharge();
		}
		return entity;
	}
	
	@RequiresPermissions("business:recharge:view")
	@RequestMapping(value = {"list", ""})
	public String list(Recharge recharge, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Recharge> page = rechargeService.find(new Page<Recharge>(request, response), recharge);
		model.addAttribute("page", page);
		return "modules/business/rechargeList";
	}

	@RequiresPermissions("business:recharge:view")
	@RequestMapping(value = "form")
	public String form(Recharge recharge, Model model) {
		if (StringUtils.isNotBlank(recharge.getId())){
			recharge = rechargeService.get(recharge.getId());
		}
		model.addAttribute("recharge", recharge);
		return "modules/business/rechargeForm";
	}

	@RequiresPermissions("business:recharge:edit")
	@RequestMapping(value = "save")
	public String save(Recharge recharge, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, recharge)){
			return form(recharge, model);
		}
		try {
			// 检验基础项目
			if(recharge.getFlowSize()<=0){
				addMessage(redirectAttributes, "充值流量不能为空，不能操作！");
				return "redirect:" + adminPath + "/business/recharge/form?id="+recharge.getId();
			}
			if (StringUtils.isBlank(recharge.getMobile())){
				addMessage(redirectAttributes, "手机号不能为空，不能操作！");
				return "redirect:" + adminPath + "/business/recharge/form?id="+recharge.getId();
			}
			if (StringUtils.isBlank(recharge.getProductId())){
				addMessage(redirectAttributes, "购买产品不能为空，不能操作！");
				return "redirect:" + adminPath + "/business/recharge/form?id="+recharge.getId();
			}
			rechargeService.saveRecharge(recharge);
			addMessage(redirectAttributes, "保存通知'" + recharge.getMobile() + "'成功");
		} catch (Exception e) {
			addMessage(redirectAttributes, "保存通知'" + e.getMessage());
		}
		return "redirect:" + adminPath + "/business/recharge/?repage";
	}
	
	@RequiresPermissions("business:recharge:edit")
	@RequestMapping(value = "delete")
	public String delete(Recharge recharge, RedirectAttributes redirectAttributes) {
		rechargeService.delete(recharge);
		addMessage(redirectAttributes, "删除记录成功");
		return "redirect:" + adminPath + "/business/recharge/?repage";
	}
	
	@RequiresPermissions("business:recharge:view")
	@RequestMapping(value = "rechargeList")
	public String rechargeList(Recharge recharge, Model model, RedirectAttributes redirectAttributes) {
		ProductResponse result = new ProductResponse();
		
		try {
			if(!StringUtils.isMobileNO(recharge.getMobile())){
				model.addAttribute("message", "请填写正确的手机号！");
			}else{
				result = productService.productListByMobile(recharge.getMobile());
			}
		} catch (Exception e) {
			addMessage(redirectAttributes, e.getMessage());
		}
		
		model.addAttribute("result", result);
		return "modules/business/rechargeForm";
	}
	
}