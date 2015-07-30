package com.yuandu.erp.modules.sys.entity;

import com.yuandu.erp.common.persistence.DataEntity;

public class Tactics extends DataEntity<Tactics>{

	private static final long serialVersionUID = 1L;
	
	private User user;			// 关联用户
	private String feeType;		//流量类型
	private String consumeType;	//消费类型（目前只有按月）
	private Double maxConsume;	//最大消费
	private Double minConsume;	// 最小消费
	private Double feeRate;		// 商务费率
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getFeeType() {
		return feeType;
	}
	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}
	public String getConsumeType() {
		return consumeType;
	}
	public void setConsumeType(String consumeType) {
		this.consumeType = consumeType;
	}
	public Double getMaxConsume() {
		return maxConsume;
	}
	public void setMaxConsume(Double maxConsume) {
		this.maxConsume = maxConsume;
	}
	public Double getMinConsume() {
		return minConsume;
	}
	public void setMinConsume(Double minConsume) {
		this.minConsume = minConsume;
	}
	public Double getFeeRate() {
		return feeRate;
	}
	public void setFeeRate(Double feeRate) {
		this.feeRate = feeRate;
	}
	
}
