package com.yuandu.erp.webservice.bean;

import java.io.Serializable;

public class ProductResponse implements Serializable{

	private static final long serialVersionUID = 1L;
	
	long id	;		//	商品ID	
	String name;	//		商品名称	
	String displayName;	//		商品详细名称	
	String operators;	//		运营商	0：电信 1：移动 2：联通
	String cityType;	//		城市类型	0：全国 1：省 2：市
	String cityId;		//		省市ID	
	String cityName;	//		省市名称	
	String fee;			//		单价	
	String img;			//		图片地址	
	String flowSize;	//		流量大小	单位MB
	String describe;	//		描述	
	String status;		//		状态	1：可用
	String comment;		//		备注	
	String specialNote;	//		特别说明	
	String orderAgreement;//		订购协议	
	String payments;	//		支付方式	
	String tagNet;		//		支持的网络类型	NET2G：2g网络 NET3G：3g网络 NET4G：4g网络 ALL：所有网络
	String flowType;	//		流量类型	PREVIOUS：前向 BACK：后向
	String RegionType;	//			["全国"] 、["省内"]
	String salesCount;	//		可用库存	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getOperators() {
		return operators;
	}
	public void setOperators(String operators) {
		this.operators = operators;
	}
	public String getCityType() {
		return cityType;
	}
	public void setCityType(String cityType) {
		this.cityType = cityType;
	}
	public String getCityId() {
		return cityId;
	}
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getFlowSize() {
		return flowSize;
	}
	public void setFlowSize(String flowSize) {
		this.flowSize = flowSize;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getSpecialNote() {
		return specialNote;
	}
	public void setSpecialNote(String specialNote) {
		this.specialNote = specialNote;
	}
	public String getOrderAgreement() {
		return orderAgreement;
	}
	public void setOrderAgreement(String orderAgreement) {
		this.orderAgreement = orderAgreement;
	}
	public String getPayments() {
		return payments;
	}
	public void setPayments(String payments) {
		this.payments = payments;
	}
	public String getTagNet() {
		return tagNet;
	}
	public void setTagNet(String tagNet) {
		this.tagNet = tagNet;
	}
	public String getFlowType() {
		return flowType;
	}
	public void setFlowType(String flowType) {
		this.flowType = flowType;
	}
	public String getRegionType() {
		return RegionType;
	}
	public void setRegionType(String regionType) {
		RegionType = regionType;
	}
	public String getSalesCount() {
		return salesCount;
	}
	public void setSalesCount(String salesCount) {
		this.salesCount = salesCount;
	}

}
