package com.cswm.assignment.service;

import org.springframework.stereotype.Service;

import com.cswm.assignment.model.Instrument;

@Service
public interface InstrumentService {


	Instrument addInstrument(Instrument instrument);

	Instrument getInstrumentById(Long instrumentId);

}
