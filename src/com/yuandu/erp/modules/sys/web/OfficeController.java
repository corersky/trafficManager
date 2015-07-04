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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yuandu.erp.common.config.Global;
import com.yuandu.erp.common.persistence.FlexPage;
import com.yuandu.erp.common.utils.LongUtils;
import com.yuandu.erp.common.utils.StringUtils;
import com.yuandu.erp.common.web.BaseController;
import com.yuandu.erp.modules.sys.entity.Office;
import com.yuandu.erp.modules.sys.entity.User;
import com.yuandu.erp.modules.sys.service.OfficeService;
import com.yuandu.erp.modules.sys.utils.DictUtils;
import com.yuandu.erp.modules.sys.utils.UserUtils;

/**
 * 机构Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/office")
public class OfficeController extends BaseController {

	@Autowired
	private OfficeService officeService;
	
	@ModelAttribute
	public Office get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return officeService.get(StringUtils.toLong(id));
		}else{
			return new Office();
		}
	}
	
	/**
	 * 获取一条数据
	 * @param id
	 * @return
	 */
	@RequiresPermissions("sys:office:view")
	@RequestMapping(value = "find")
	public @ResponseBody Office find(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return officeService.get(StringUtils.toLong(id));
		}else{
			return new Office();
		}
	}

	@RequiresPermissions("sys:office:view")
	@RequestMapping(value = {"index"})
	public String index(Office office, Model model) {
		return "modules/sys/officeIndex";
	}

	@RequiresPermissions("sys:office:view")
	@RequestMapping(value = {"list", ""})
	public String list(Office office,HttpServletRequest request, HttpServletResponse response, Model model) {
		model.addAttribute("office", office);
		return "modules/sys/officeList";
	}
	
	@RequiresPermissions("sys:office:view")
	@RequestMapping(value = "findPage")
	public @ResponseBody FlexPage<Office> findPage(Office office,HttpServletRequest request, HttpServletResponse response, Model model) {
		FlexPage<Office> page = officeService.findPage(new FlexPage<Office>(request, response), office); 
		return page;
	}
	
	@RequiresPermissions("sys:office:view")
	@RequestMapping(value = "form")
	public String form(Office office, Model model) {
		User user = UserUtils.getUser();
		if (office.getParent()==null || office.getParent().getId()==null){
			office.setParent(user.getOffice());
		}
		office.setParent(officeService.get(office.getParent().getId()));
		if (office.getArea()==null){
			office.setArea(user.getOffice().getArea());
		}
		// 自动获取排序号
		if (LongUtils.isBlank(office.getId())&&office.getParent()!=null){
			int size = 0;
			List<Office> list = officeService.findAll();
			for (int i=0; i<list.size(); i++){
				Office e = list.get(i);
				if (e.getParent()!=null && e.getParent().getId()!=null
						&& e.getParent().getId().equals(office.getParent().getId())){
					size++;
				}
			}
			office.setCode(office.getParent().getCode() + StringUtils.leftPad(String.valueOf(size > 0 ? size+1 : 1), 3, "0"));
		}
		model.addAttribute("office", office);
		return "modules/sys/officeForm";
	}
	
	@RequiresPermissions("sys:office:edit")
	@RequestMapping(value = "save")
	public String save(Office office, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, office)){
			return form(office, model);
		}
		officeService.save(office);
		
		if(office.getChildDeptList()!=null){
			Office childOffice = null;
			for(String id : office.getChildDeptList()){
				childOffice = new Office();
				childOffice.setName(DictUtils.getDictLabel(id, "sys_office_common", "未知"));
				childOffice.setParent(office);
				childOffice.setArea(office.getArea());
				childOffice.setType("2");
				childOffice.setGrade(String.valueOf(Integer.valueOf(office.getGrade())+1));
				childOffice.setUseable(Global.YES);
				officeService.save(childOffice);
			}
		}
		
		addMessage(redirectAttributes, "保存机构'" + office.getName() + "'成功");
	//	Long id = 0==office.getParentId() ? 0 : office.getParentId();
		return "redirect:" + adminPath + "/sys/office/list";
	}
	
	@RequiresPermissions("sys:office:edit")
	@RequestMapping(value = "delete")
	public String delete(Office office, RedirectAttributes redirectAttributes) {
		officeService.delete(office);
		addMessage(redirectAttributes, "删除机构成功");
		return "redirect:" + adminPath + "/sys/office/list?id="+office.getParentId();
	}

	/**
	 * 获取机构JSON数据。
	 * @param extId 排除的ID
	 * @param type	类型（1：公司；2：部门/小组/其它：3：用户）
	 * @param grade 显示级别
	 * @param response
	 * @return
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId, @RequestParam(required=false) String type,@RequestParam(required=false) String actType,
			@RequestParam(required=false) Long grade, @RequestParam(required=false) Boolean isAll, @RequestParam(required=false) Boolean isMyCompany, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Office> list = officeService.findList(isAll);
		for (int i=0; i<list.size(); i++){
			Office e = list.get(i);
			if ((StringUtils.isBlank(extId) || (extId!=null && !extId.equals(e.getId())))
					&& (type == null || (type != null && (type.equals("1") ? type.equals(e.getType()) : true)))
					&& (grade == null || (grade != null && Integer.parseInt(e.getGrade()) <= grade.intValue()))
					&& Global.YES.equals(e.getUseable())
					&& (actType == null || "many".equals(e.getActType()) || (actType != null && actType.equals(e.getActType())))
				){
				
				if(isMyCompany!=null&&isMyCompany){//本公司机构
					String company = ","+UserUtils.getUser().getCompany().getId()+",";
					if(!e.getParentIds().contains(company)&&
							e.getId().compareTo(UserUtils.getUser().getCompany().getId())!=0){
						continue;//只获取本公司
					}
				}
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParentId());
				map.put("pIds", e.getParentIds());
				map.put("name", e.getName());
				if (type != null && "3".equals(type)){
					map.put("isParent", true);
				}
				mapList.add(map);
			}
		}
		return mapList;
	}
}
