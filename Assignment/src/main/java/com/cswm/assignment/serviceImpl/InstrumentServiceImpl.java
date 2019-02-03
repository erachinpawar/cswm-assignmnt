package com.cswm.assignment.serviceImpl;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cswm.assignment.applicationUtils.ErrorMessageEnum;
import com.cswm.assignment.exceptions.ApplicationException;
import com.cswm.assignment.exceptions.NotFoundException;
import com.cswm.assignment.model.Instrument;
import com.cswm.assignment.repository.InstrumentRepository;
import com.cswm.assignment.service.InstrumentService;
import com.cswm.assignment.service.OrderBookService;
import com.cswm.assignment.service.OrderService;

@Service
@Transactional
public class InstrumentServiceImpl implements InstrumentService {

	@Autowired
	InstrumentRepository instrumentRepository;
	@Autowired
	OrderService orderService;
	@Autowired
	OrderBookService orderBookService;

	@Override
	public List<Instrument> getInstruments() {
		return instrumentRepository.findAll();
	}

	@Override
	public Instrument getInstrument(Long instrumentId) {
		return instrumentRepository.findFirstByInstrumentId(instrumentId)
				.orElseThrow(() -> new NotFoundException(ErrorMessageEnum.INSRTUMENT_NOT_PRESENT));
	}

	@Override
	public Instrument addInstrument(Instrument instrument) {
		validateInstrumentName(instrument);
		return instrumentRepository.save(instrument);
	}

	private void validateInstrumentName(Instrument instrument) {
		if (null == instrument.getInstrumentName() || instrument.getInstrumentName().isEmpty())
			throw new ApplicationException(ErrorMessageEnum.INSRTUMENT_NAME_INVALID);
	}

	@Override
	public Instrument updateInstrument(Instrument instrument, Long instrumentid) {
		instrument.setInstrumentId(instrumentid);
		return instrumentRepository.save(instrument);
	}

	@Override
	public void removeInstrument(Long instrumentId) {
		if (CollectionUtils.isEmpty(orderService.getOrdersByInstruments(getInstrument(instrumentId)))
				&& CollectionUtils.isEmpty(orderBookService.getOrderBooksByInstruments(getInstrument(instrumentId))))
			instrumentRepository.delete(getInstrument(instrumentId));
		else {
			throw new ApplicationException(ErrorMessageEnum.DEPENDENT_INSTRUMENT);
		}

	}

}
