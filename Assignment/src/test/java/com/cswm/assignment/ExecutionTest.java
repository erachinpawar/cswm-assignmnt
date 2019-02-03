package com.cswm.assignment;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.cswm.assignment.applicationUtils.ErrorMessageEnum;
import com.cswm.assignment.model.Execution;
import com.cswm.assignment.model.Message;

public class ExecutionTest extends AbstractTest {

	@Test
	public void getExecutionsTest() throws Exception {
		String uri = "/executions";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		Execution[] orders = mapFromJson(content, Execution[].class);
		assertEquals(2, orders.length);
	}

	@Test
	public void getExecutionTest() throws Exception {
		String uri = "/executions/1001";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		Execution execution = mapFromJson(content, Execution.class);
		assertEquals(200, status);
		assertEquals("Execution-1", execution.getExecutionName());
	}

	@Test
	public void getExecutionNotFoundTest() throws Exception {
		String uri = "/executions/1010";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		Message message = mapFromJson(content, Message.class);
		assertEquals(404, status);
		assertEquals(ErrorMessageEnum.EXECUTION_NOT_FOUND.getMessage(), message.getMessage());
	}

	@Test
	public void deleteExecutionTest() throws Exception {
		String uri = "/executions/1001";
		MvcResult mvcResult = mvc
				.perform(MockMvcRequestBuilders.delete(uri).contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		Message message = mapFromJson(content, Message.class);
		assertEquals(406, status);
		assertEquals(ErrorMessageEnum.DELETE_EXECUTION_UNSUPPORTED.getMessage(), message.getMessage());
	}

}
