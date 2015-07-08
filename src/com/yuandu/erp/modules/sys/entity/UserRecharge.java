package com.yuandu.erp.modules.sys.entity;

import javax.validation.constraints.NotNull;

import com.yuandu.erp.common.persistence.DataEntity;

public class UserRecharge extends DataEntity<UserRecharge>{

	private static final long serialVersionUID = 1L;
	
	private Float balance;
	private User supplier;
	
	public Float getBalance() {
		return balance;
	}
	public void setBalance(Float balance) {
		this.balance = balance;
	}
	@NotNull(message="充值商家不能为空")
	public User getSupplier() {
		return supplier;
	}
	public void setSupplier(User supplier) {
		this.supplier = supplier;
	}

}
