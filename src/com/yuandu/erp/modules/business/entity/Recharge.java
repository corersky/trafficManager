package com.yuandu.erp.modules.business.entity;

import org.hibernate.validator.constraints.Length;

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
	
	public Recharge() {
		super();
	}

	public Recharge(String id){
		super(id);
	}

	@Length(min=0, max=20, message="标题长度必须介于 0 和 20 之间")
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

}