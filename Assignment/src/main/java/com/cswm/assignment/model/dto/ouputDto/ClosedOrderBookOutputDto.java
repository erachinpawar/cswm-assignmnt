package com.cswm.assignment.model.dto.ouputDto;

import com.cswm.assignment.applicationutils.OrderBookStatus;

public class ClosedOrderBookOutputDto {

	private Long orderBookId;

	private InstrumentOutputDto instrument;

	private OrderBookStatus orderBookStatus;

	public Long getOrderBookId() {
		return orderBookId;
	}

	public void setOrderBookId(Long orderBookId) {
		this.orderBookId = orderBookId;
	}

	public InstrumentOutputDto getInstrument() {
		return instrument;
	}

	public void setInstrument(InstrumentOutputDto instrument) {
		this.instrument = instrument;
	}

	public OrderBookStatus getOrderBookStatus() {
		return orderBookStatus;
	}

	public void setOrderBookStatus(OrderBookStatus orderBookStatus) {
		this.orderBookStatus = orderBookStatus;
	}

}
