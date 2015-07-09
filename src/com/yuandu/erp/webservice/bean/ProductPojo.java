package com.yuandu.erp.webservice.bean;

import java.io.Serializable;

public class ProductPojo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	long id	;		//	商品ID	
	private String name;		//		商品名称	
	private String displayName;	//		商品详细名称	
	private String operators;	//		运营商	0：电信 1：移动 2：联通
	private String cityType;	//		城市类型	0：全国 1：省 2：市
	private String cityId;		//		省市ID	
	private String cityName;	//		省市名称	
	private Double fee;			//		单价	
	private String img;			//		图片地址	
	private Integer flowSize;	//		流量大小	单位MB
	private String describe;	//		描述	
	private String status;		//		状态	1：可用
	private String comment;		//		备注	
	private String specialNote;	//		特别说明	
	private String orderAgreement;//		订购协议	
	private String payments;	//		支付方式	
	private String tagNet;		//		支持的网络类型	NET2G：2g网络 NET3G：3g网络 NET4G：4g网络 ALL：所有网络
	private String flowType;	//		流量类型	PREVIOUS：前向 BACK：后向
	private String RegionType;	//			["全国"] 、["省内"]
	private Integer salesCount;	//		可用库存	
	private Double balance;		//		账户扣款
	
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
	public Double getFee() {
		return fee;
	}
	public void setFee(Double fee) {
		this.fee = fee;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public Integer getFlowSize() {
		return flowSize;
	}
	public void setFlowSize(Integer flowSize) {
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
	public Integer getSalesCount() {
		return salesCount;
	}
	public void setSalesCount(Integer salesCount) {
		this.salesCount = salesCount;
	}
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}

}
