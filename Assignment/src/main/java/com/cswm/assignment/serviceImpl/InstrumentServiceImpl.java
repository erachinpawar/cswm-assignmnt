package com.cswm.assignment.serviceImpl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cswm.assignment.applicationUtils.ErrorMessageEnum;
import com.cswm.assignment.exceptions.ApplicationException;
import com.cswm.assignment.exceptions.NotFoundException;
import com.cswm.assignment.model.Instrument;
import com.cswm.assignment.repository.InstrumentRepository;
import com.cswm.assignment.service.InstrumentService;

@Service
@Transactional
public class InstrumentServiceImpl implements InstrumentService {
	
	@Autowired
	InstrumentRepository instrumentRepository;
	@Override
	public Instrument getInstrument(String instrumentId) {
		return instrumentRepository.findFirstByInstrumentName(instrumentId)
				.orElseThrow(() -> new NotFoundException(ErrorMessageEnum.INSRTUMENT_NOT_PRESENT));
	}
	
	@Override
	public Instrument getInstrumentById(Long instrumentId) {
		return instrumentRepository.findFirstByInstrumentId(instrumentId)
				.orElseThrow(() -> new NotFoundException(ErrorMessageEnum.INSRTUMENT_NOT_PRESENT));
	}

	@Override
	public synchronized Instrument  addInstrument(Instrument instrument) {
		validateInstrumentName(instrument);
		return instrumentRepository.save(instrument);
	}
	
	private void validateInstrumentName(Instrument instrument) {
		if (null == instrument.getInstrumentName() || instrument.getInstrumentName().isEmpty())
			throw new ApplicationException(ErrorMessageEnum.INSRTUMENT_NAME_INVALID);
	}

	
}
