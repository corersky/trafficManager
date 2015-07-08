package com.yuandu.erp.webservice.utils;

import com.yuandu.erp.modules.business.entity.PartnerOrder;

public class PartnerOrderResponse extends BaseResponse{

	private static final long serialVersionUID = 1L;
	
	private PartnerOrder data;

	public PartnerOrder getData() {
		return data;
	}

	public void setData(PartnerOrder data) {
		this.data = data;
	}

}
