package com.yuandu.erp.modules.business.entity;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yuandu.erp.common.persistence.DataEntity;
import com.yuandu.erp.common.utils.IdGen;
import com.yuandu.erp.modules.sys.entity.User;
import com.yuandu.erp.modules.sys.utils.UserUtils;

/**
 * 付费Entity
 */
public class Recharge extends DataEntity<Recharge> {
	
	private static final long serialVersionUID = 1L;

	public static final String status_default = "0";//默认状态处理中
	public static final String notify_url = "http://partner.doniv.net:8001/TrafficManager/manager/product/notifyStatus";
	
	private String mobile;//充值手机号
	private Integer flowSize;
	private Double balance;//价格
	private Double fee;	   //实际单价价格
	private Double feeRate;//使用的费率
	private Double adminFee;//管理员价格
	private String status; //订单状态	0：初始状态 1：成功 2：失败 3：异常 4：处理中5：未知
	private String type;
	private String productId;//产品ID
	private String partnerOrderNo;//合作方的生成的订单号
	private String notifyUrl;//回调地址
	private String orderNo;//运营商订单号
	private String isRefund;// 是否退款

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

	public String getStatus() {
		return status;
	}

	@JsonIgnore
	public Double getAdminFee() {
		return adminFee;
	}

	public void setAdminFee(Double adminFee) {
		this.adminFee = adminFee;
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

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getIsRefund() {
		return isRefund;
	}

	public void setIsRefund(String isRefund) {
		this.isRefund = isRefund;
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

	@JsonIgnore
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

	public Double getFeeRate() {
		return feeRate;
	}

	public void setFeeRate(Double feeRate) {
		this.feeRate = feeRate;
	}

	/**
	 * 插入之前执行方法，需要手动调用
	 */
	@Override
	public void preInsert(){
		// 不限制ID为UUID，调用setIsNewRecord()使用自定义ID
		if (!this.isNewRecord){
			setId(IdGen.uuid());
		}
		User user = UserUtils.getUser();
		if(this.createBy!=null&&this.createBy.getId()!=null){
			user = this.createBy;
		}
		if (StringUtils.isNotBlank(user.getId())){
			this.updateBy = user;
			this.createBy = user;
		}
		this.updateDate = new Date();
		this.createDate = this.updateDate;
	}
}