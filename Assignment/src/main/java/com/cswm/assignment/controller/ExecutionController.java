package com.cswm.assignment.controller;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cswm.assignment.model.Execution;
import com.cswm.assignment.service.ExecutionService;

@RestController
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequestMapping("/executions")
public class ExecutionController {

	@Autowired
	ExecutionService executionService;

	@GetMapping
	public List<Execution> getExecutions() {
		return executionService.getExecutions();

	}

	@GetMapping("/{executionId}")
	public Execution getExecution(@PathVariable Long executionId) {
		return executionService.getExecution(executionId);

	}

	@DeleteMapping("/{executionId}")
	public Execution deleteExecution(@PathVariable Long executionId) {
		return executionService.deleteExecution(executionId);
	}

	

}
