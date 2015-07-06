package com.yuandu.erp.modules.business.web;

import java.util.List;

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

import com.google.common.collect.Lists;
import com.yuandu.erp.common.persistence.Page;
import com.yuandu.erp.common.utils.StringUtils;
import com.yuandu.erp.common.web.BaseController;
import com.yuandu.erp.modules.business.entity.Recharge;
import com.yuandu.erp.modules.business.service.RechargeService;
import com.yuandu.erp.webservice.bean.ProductPojo;
import com.yuandu.erp.webservice.service.ProductService;

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
		// 如果充值成功，则不能再进行操作
		if (StringUtils.isNotBlank(recharge.getId())){
			Recharge e = rechargeService.get(recharge.getId());
			if ("1".equals(e.getStatus())){
				addMessage(redirectAttributes, "已充值成功，不能操作！");
				return "redirect:" + adminPath + "/business/recharge/form?id="+recharge.getId();
			}
		}
		rechargeService.save(recharge);
		addMessage(redirectAttributes, "保存通知'" + recharge.getMobile() + "'成功");
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
		List<ProductPojo> list = Lists.newArrayList();
		
		if(!StringUtils.isMobileNO(recharge.getMobile())){
			model.addAttribute("message", "请填写正确的手机号！");
		}else{
			list = productService.productListByMobile(recharge.getMobile());
		}
		model.addAttribute("list", list);
		return "modules/business/rechargeForm";
	}
	
}