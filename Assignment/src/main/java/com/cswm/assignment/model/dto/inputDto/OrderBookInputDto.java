package com.cswm.assignment.model.dto.inputDto;

public class OrderBookInputDto {

	private Long instrumentId;

	public Long getInstrumentId() {
		return instrumentId;
	}

	public void setInstrumentId(Long instrumentId) {
		this.instrumentId = instrumentId;
	}

	@Override
	public String toString() {
		return "OrderBookCreateInputDto [instrumentId=" + instrumentId + "]";
	}

}
