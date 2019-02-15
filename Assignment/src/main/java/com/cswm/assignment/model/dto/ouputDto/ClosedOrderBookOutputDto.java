package com.cswm.assignment.model.dto.ouputDto;

import com.cswm.assignment.applicationutils.OrderBookStatus;

public class ClosedOrderBookOutputDto {

	private Long orderBookId;

	private Long instrumentId;

	private OrderBookStatus orderBookStatus;

	public Long getOrderBookId() {
		return orderBookId;
	}

	public void setOrderBookId(Long orderBookId) {
		this.orderBookId = orderBookId;
	}

	public Long getInstrumentId() {
		return instrumentId;
	}

	public void setInstrumentId(Long instrumentId) {
		this.instrumentId = instrumentId;
	}

	public OrderBookStatus getOrderBookStatus() {
		return orderBookStatus;
	}

	public void setOrderBookStatus(OrderBookStatus orderBookStatus) {
		this.orderBookStatus = orderBookStatus;
	}

}
