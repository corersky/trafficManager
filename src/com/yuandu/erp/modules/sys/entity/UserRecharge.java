package com.yuandu.erp.modules.sys.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.yuandu.erp.common.persistence.DataEntity;

public class UserRecharge extends DataEntity<UserRecharge>{

	private static final long serialVersionUID = 1L;
	
	private Double balance;
	private User supplier;
	
	private Date beginDate;		// 开始日期
	private Date endDate;		// 结束日期
	
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	@NotNull(message="充值商家不能为空")
	public User getSupplier() {
		return supplier;
	}
	public void setSupplier(User supplier) {
		this.supplier = supplier;
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

}
