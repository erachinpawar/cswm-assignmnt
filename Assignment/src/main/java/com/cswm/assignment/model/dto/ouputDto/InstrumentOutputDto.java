package com.cswm.assignment.model.dto.ouputDto;

public class InstrumentOutputDto {

	private Long instrumentId;

	public InstrumentOutputDto() {
	}

	public Long getInstrumentId() {
		return instrumentId;
	}

	public void setInstrumentId(Long instrumentId) {
		this.instrumentId = instrumentId;
	}

	@Override
	public String toString() {
		return "Instrument [instrumentId=" + instrumentId + "]";
	}

}
