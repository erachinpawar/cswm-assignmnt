package com.cswm.assignment.controller;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cswm.assignment.model.Instrument;
import com.cswm.assignment.service.InstrumentService;


@RestController
@RequestMapping("/instruments")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class InstrumentController {
	
	@Autowired
	InstrumentService instrumentService;
	
	@GetMapping
	public List<Instrument> getInstruments() {
		return instrumentService.getInstruments();
	}
	
	@PostMapping
	public Instrument addInstrument(@RequestBody Instrument instrument) {
		Instrument newInstrument = instrumentService.addInstrument(instrument);
		return newInstrument;
	}
	
	@PutMapping("/{instrumentId}")
	public Instrument updateInstrument(@PathVariable("instrumentId") Long instrumentid,@RequestBody Instrument instrument) {
		return instrumentService.updateInstrument(instrument,instrumentid);
	}
	
	@DeleteMapping("/{instrumentId}")
	public void deleteInstrument(@PathVariable("instrumentId") Long instrumentId) {
		instrumentService.removeInstrument(instrumentId);
	}
	
	@GetMapping("/{instrumentId}")
	public Instrument getInstrument(@PathVariable("instrumentId") Long instrumentId) {
		Instrument instrument = instrumentService.getInstrument(instrumentId);
		return instrument;
		
	}
	

}



	
