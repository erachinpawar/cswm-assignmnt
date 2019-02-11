package com.cswm.assignment.model.dto.inputDto;

import java.math.BigDecimal;

public class AddOrderInputDto {

	private Long instrumentId;
	private Long orderQuantity;
	private BigDecimal orderprice;

	public Long getInstrumentId() {
		return instrumentId;
	}

	public void setInstrumentId(Long instrumentId) {
		this.instrumentId = instrumentId;
	}

	public Long getOrderQuantity() {
		return orderQuantity;
	}

	public void setOrderQuantity(Long orderQuantity) {
		this.orderQuantity = orderQuantity;
	}

	public BigDecimal getOrderprice() {
		return orderprice;
	}

	public void setOrderprice(BigDecimal orderprice) {
		this.orderprice = orderprice;
	}

	@Override
	public String toString() {
		return "AddOrderInputDto [instrumentId=" + instrumentId + ", orderQuantity=" + orderQuantity + ", orderprice=" + orderprice + "]";
	}

}
