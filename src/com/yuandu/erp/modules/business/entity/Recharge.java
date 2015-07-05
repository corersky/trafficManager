package com.yuandu.erp.modules.business.entity;

import java.util.Date;

import com.yuandu.erp.common.persistence.DataEntity;

/**
 * 付费Entity
 */
public class Recharge extends DataEntity<Recharge> {
	
	private static final long serialVersionUID = 1L;
	
	private String mobile;//充值手机号
	private Integer flowCount;
	private String status;
	private String type;
	private String productId;//产品ID
	private String partnerOrderNo;//合作方的生成的订单号
	private String notifyUrl;//回调地址
	private String orderNo;//运营商订单号

	
	private Date beginDate;		// 开始日期
	private Date endDate;		// 结束日期
	
	public Recharge() {
		super();
	}

	public Recharge(String id){
		super(id);
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Integer getFlowCount() {
		return flowCount;
	}

	public void setFlowCount(Integer flowCount) {
		this.flowCount = flowCount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getPartnerOrderNo() {
		return partnerOrderNo;
	}

	public void setPartnerOrderNo(String partnerOrderNo) {
		this.partnerOrderNo = partnerOrderNo;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

}