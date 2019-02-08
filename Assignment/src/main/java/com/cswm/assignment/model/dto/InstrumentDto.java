package com.cswm.assignment.model.dto;

import java.time.LocalDateTime;

public class InstrumentDto {

	private Long instrumentId;

	private String instrumentName;

	private String createdBy;

	private LocalDateTime createdOn;

	public InstrumentDto() {
	}

	public Long getInstrumentId() {
		return instrumentId;
	}

	public void setInstrumentId(Long instrumentId) {
		this.instrumentId = instrumentId;
	}

	public String getInstrumentName() {
		return instrumentName;
	}

	public void setInstrumentName(String instrumentName) {
		this.instrumentName = instrumentName;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}

	@Override
	public String toString() {
		return "Instrument [instrumentId=" + instrumentId + ", instrumentName=" + instrumentName + ", createdBy="
				+ createdBy + ", createdOn=" + createdOn + "]";
	}


	
}
