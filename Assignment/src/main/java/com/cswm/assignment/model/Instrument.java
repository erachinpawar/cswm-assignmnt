package com.cswm.assignment.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Entity
@Table(name = "instrument_inv")
@Data
public class Instrument {

	@Id
	@Column(name = "instrument_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "instruments_seq")
	private Long instrumentId;

	@Column(name = "instrument_name")
	private String instrumentName;

	@Column(name = "created_by")
	private String createdBy;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "created_on")
	private Date createdOn;

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

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	@Override
	public String toString() {
		return "refInstruments [refInstrumentId=" + instrumentId + ", instrumentName=" + instrumentName
				+ ",  createdBy=" + createdBy + ", createdOn=" + createdOn + "]";
	}

}
