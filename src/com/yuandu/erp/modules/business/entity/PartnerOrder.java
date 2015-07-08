package com.yuandu.erp.modules.business.entity;

import com.yuandu.erp.common.persistence.DataEntity;

/**
 * 运营商订单信息
 * @author ivoter
 */
public class PartnerOrder extends DataEntity<PartnerOrder>{

	private static final long serialVersionUID = 1L;

	private String orderNo;			//		流量掌厅的订单号	
	private String partnerOrderNo;	//		合作方的订单号	
	private String mobile;			//		手机号	
	private Long partnerId;			//		合作方ID	
	private Integer operators;		//		运营商	0：电信 1：移动 2：联通
	private Long productId;			//		商品ID	
	private String productName;		//		商品名称	
	private Integer flowSize;		//		流量大小	单位（MB）
	private Double fee;				//		单价	
	private String flowType;		//		流量类型	
	private Integer count;			//		购买的商品数量	
	private Double totalFee	;		//	    合计金额	
	private String status;			//		订单状态	0：初始状态 1：成功 2：失败 3：异常 4：处理中5：未知
	private String notifyUrl;		//		回调地址	
	
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getPartnerOrderNo() {
		return partnerOrderNo;
	}
	public void setPartnerOrderNo(String partnerOrderNo) {
		this.partnerOrderNo = partnerOrderNo;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Long getPartnerId() {
		return partnerId;
	}
	public void setPartnerId(Long partnerId) {
		this.partnerId = partnerId;
	}
	public Integer getOperators() {
		return operators;
	}
	public void setOperators(Integer operators) {
		this.operators = operators;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public Integer getFlowSize() {
		return flowSize;
	}
	public void setFlowSize(Integer flowSize) {
		this.flowSize = flowSize;
	}
	public Double getFee() {
		return fee;
	}
	public void setFee(Double fee) {
		this.fee = fee;
	}
	public String getFlowType() {
		return flowType;
	}
	public void setFlowType(String flowType) {
		this.flowType = flowType;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Double getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(Double totalFee) {
		this.totalFee = totalFee;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getNotifyUrl() {
		return notifyUrl;
	}
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
}
