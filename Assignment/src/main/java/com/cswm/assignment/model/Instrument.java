package com.cswm.assignment.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Entity
@ApiModel
@Table(name = "instrument_inv")
public class Instrument {

	@Id
	@Column(name = "instrument_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "instruments_seq")
	private Long instrumentId;

	@Column(name = "instrument_name")
	@ApiModelProperty("Name Should not be empty.")
	private String instrumentName;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_on")
	private LocalDateTime createdOn;

	public Instrument() {
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
