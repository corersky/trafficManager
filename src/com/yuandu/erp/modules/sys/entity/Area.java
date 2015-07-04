package com.yuandu.erp.modules.sys.entity;

import java.util.List;

import com.google.common.collect.Lists;
import com.yuandu.erp.common.persistence.DataEntity;

/**
 * 区域Entity
 */
public class Area extends DataEntity<Area> {

	private static final long serialVersionUID = 1L;
	private String areaCode;//区号
	private String name;// 名称
	private Long province;	// 排序
	private Long city; 	
	private String type;// 区域类型（1：省份、直辖市；2：地市；3：区县）
	private String remarks;	// 备注
	
	private String provinceName;
	private String cityName;
	private List<Area> child = Lists.newArrayList();
	
	public Area(){
		super();
	}

	public Area(Long id){
		super(id);
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getProvince() {
		return province;
	}

	public void setProvince(Long province) {
		this.province = province;
	}

	public Long getCity() {
		return city;
	}

	public void setCity(Long city) {
		this.city = city;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public List<Area> getChild() {
		return child;
	}

	public void setChild(List<Area> child) {
		this.child = child;
	}
	
}