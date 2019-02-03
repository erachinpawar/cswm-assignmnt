package com.cswm.assignment;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.cswm.assignment.applicationUtils.ErrorMessageEnum;
import com.cswm.assignment.model.Instrument;
import com.cswm.assignment.model.Message;

public class InstrumentTest extends AbstractTest {

	@Test
	public void getInstrumentsTest() throws Exception {
		String uri = "/instruments";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		Instrument[] instruments = mapFromJson(content, Instrument[].class);
		assertEquals(3, instruments.length);
	}

	@Test
	public void getInstrumentTest() throws Exception {
		String uri = "/instruments/1002";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		Instrument instrument = mapFromJson(content, Instrument.class);
		assertEquals("Instrument-2", instrument.getInstrumentName());
	}

	@Test
	public void getInstrumentNoFoundTest() throws Exception {
		String uri = "/instruments/204";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		Message message = mapFromJson(content, Message.class);
		assertEquals(404, status);
		assertEquals(ErrorMessageEnum.INSRTUMENT_NOT_PRESENT.getMessage(), message.getMessage());
	}

	@Test
	public void getInstrumentNoNameTest() throws Exception {
		String uri = "/instruments";
		Instrument instrument = new Instrument();
		instrument.setCreatedBy("Unit test");
		instrument.setCreatedOn(new Date());

		String inputJson = super.mapToJson(instrument);
		MvcResult mvcResult = mvc.perform(
				MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		Message message = mapFromJson(content, Message.class);
		assertEquals(406, status);
		assertEquals(ErrorMessageEnum.INSRTUMENT_NAME_INVALID.getMessage(), message.getMessage());
	}

}
