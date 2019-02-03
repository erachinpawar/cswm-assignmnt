package com.cswm.assignment.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cswm.assignment.model.Instrument;

@Service
public interface InstrumentService {

	List<Instrument> getInstruments();

	Instrument getInstrument(Long instrumentid);

	Instrument addInstrument(Instrument instrument);

	Instrument updateInstrument(Instrument instrument, Long instrumentid);

	void removeInstrument(Long instrumentId);


	

	
	
}
