package com.cswm.assignment;

import java.io.IOException;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.json.JsonParseException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.cswm.assignment.model.Instrument;
import com.cswm.assignment.service.ExecutionService;
import com.cswm.assignment.service.InstrumentService;
import com.cswm.assignment.service.OrderBookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@RunWith(SpringRunner.class)
@EntityScan(basePackageClasses = {AssignmentApplication.class, Jsr310JpaConverters.class})
@SpringBootTest(classes = AssignmentApplication.class)
public class AbstractTest {

	
	@Autowired
	ExecutionService executionService;
	
	@Autowired
	OrderBookService orderBookService;
	
	
	protected MockMvc mvc;
	@Autowired
	WebApplicationContext webApplicationContext;

	@Autowired
	InstrumentService instrumentService;

	@Before
	public void setUp() {
		this.mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		instrumentService.addInstrument(new Instrument("Instrument-1"));
	}

	protected String mapToJson(Object obj) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		 objectMapper.registerModule(new JavaTimeModule());
		return objectMapper.writeValueAsString(obj);
	}

	protected <T> T mapFromJson(String json, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {

		ObjectMapper objectMapper = new ObjectMapper();
		 objectMapper.registerModule(new JavaTimeModule());
		return objectMapper.readValue(json, clazz);
	}

}
