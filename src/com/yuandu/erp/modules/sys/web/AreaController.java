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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yuandu.erp.common.config.Global;
import com.yuandu.erp.common.persistence.FlexPage;
import com.yuandu.erp.common.utils.FileInptUtil;
import com.yuandu.erp.common.utils.StringUtils;
import com.yuandu.erp.common.web.BaseController;
import com.yuandu.erp.modules.sys.entity.Area;
import com.yuandu.erp.modules.sys.entity.Office;
import com.yuandu.erp.modules.sys.service.AreaService;

/**
 * 区域Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/area")
public class AreaController extends BaseController {

	@Autowired
	private AreaService areaService;
	
	@ModelAttribute("area")
	public Area get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return areaService.get(StringUtils.toLong(id));
		}else{
			return new Area();
		}
	}
	
	@RequiresPermissions("sys:area:view")
	@RequestMapping(value = {"index"})
	public String index(Office office, Model model) {
		return "modules/sys/areaIndex";
	}

	@RequiresPermissions("sys:area:view")
	@RequestMapping(value = {"list", ""})
	public String list(Area area,HttpServletRequest request, HttpServletResponse response, Model model) {
		
		model.addAttribute("area", area);
		return "modules/sys/areaList";
	}
	
	@RequiresPermissions("sys:area:view")
	@RequestMapping(value = "findPage")
	public @ResponseBody FlexPage<Area> findPage(Area area,HttpServletRequest request, HttpServletResponse response, Model model) {
		
		FlexPage<Area> page = areaService.findPage(new FlexPage<Area>(request, response), area);
		return page;
	}

	@RequiresPermissions("sys:area:view")
	@RequestMapping(value = "form")
	public String form(Area area, Model model) {
		
		model.addAttribute("area", area);
		return "modules/sys/areaForm";
	}
	
	/**
	 * excel文件导入
	 * @param area
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:area:view")
	@RequestMapping(value = "importForm")
	public String importForm(Area area, Model model) {
		return "modules/sys/areaImportForm";
	}
	
	@RequiresPermissions("sys:area:edit")
	@RequestMapping(value = "save")
	public String save(Area area, Model model, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/sys/area";
		}
		if (!beanValidator(model, area)){
			return form(area, model);
		}
		areaService.save(area);
		addMessage(redirectAttributes, "保存区域'" + area.getName() + "'成功");
		return "redirect:" + adminPath + "/sys/area/";
	}
	
	@RequiresPermissions("sys:area:edit")
	@RequestMapping(value = "delete")
	public String delete(Area area, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/sys/area";
		}
//		if (Area.isRoot(id)){
//			addMessage(redirectAttributes, "删除区域失败, 不允许删除顶级区域或编号为空");
//		}else{
			areaService.delete(area);
			addMessage(redirectAttributes, "删除区域成功");
//		}
		return "redirect:" + adminPath + "/sys/area/";
	}

	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId,  @RequestParam(required=false) String type, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Area> list = areaService.findAll();
		
		Map<Long,Long> areaMap = Maps.newHashMap();
		
		for (int i=0; i<list.size(); i++){
			Area e = list.get(i);
			if (StringUtils.isBlank(extId) || (extId!=null && !extId.equals(e.getId()))){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId","0");
				map.put("roleType", "1");//省节点
				
				if(e.getCity()!=null&&e.getCity()!=0){
					map.put("pId", e.getCity());
					map.put("roleType", "3");//县节点
				}else if(e.getProvince()!=null&&e.getProvince()!=0){
					map.put("pId", e.getProvince());
					map.put("roleType", "2");//市节点
				}
				map.put("name", e.getName());
				if (type != null && "5".equals(type)){
					map.put("isParent", true);
					//设置小区关联
					if(!areaMap.containsKey(e.getId())){continue;}
				}
				mapList.add(map);
			}
		}
		return mapList;
	}
	
	/**
	 * 获取地区列表
	 * @param response
	 * @return
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "getMapData")
	public List<Area> getMapData(HttpServletResponse response) {
		List<Area> result = Lists.newArrayList();
		
		List<Area> list = areaService.findAll();
		Map<Long,Area> areas = Maps.newHashMap();
		
		Map<Long,Long> parentMap = Maps.newHashMap();
		for (Area entity:list){
			Long id = entity.getId();
			Long province = entity.getProvince();
			Long city = entity.getCity();
			
			if(city!=null&&city!=0){
				parentMap.put(id, city);
			}else if(province!=null&&province!=0){
				parentMap.put(id, province);
			}else{
				parentMap.put(id, 0l);
			}
			areas.put(id, entity);
		}
		
		for(Map.Entry<Long, Long> entry:parentMap.entrySet()){
			Long id = entry.getKey();
			Long parent = entry.getValue();
			
			if(parent==0){//省份
				result.add(areas.get(id));
			}else{
				Area parentArea = areas.get(parent);
				parentArea.getChild().add(areas.get(id));
			}
		}
		return result;
	}
	
	@RequiresPermissions("sys:area:edit")
	@RequestMapping(value = "importExcel")
	public @ResponseBody FileInptUtil importExcel(@RequestParam(value = "file") MultipartFile file,  
            HttpServletRequest request, HttpServletResponse response) {  
		
		FileInptUtil result = FileInptUtil.fileUpload(file, "areaExcel");
	    
	    return result;
	}
	
}
