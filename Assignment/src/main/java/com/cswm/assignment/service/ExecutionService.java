package com.cswm.assignment.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cswm.assignment.model.Execution;

@Service
public interface ExecutionService {

	public List<Execution> getExecutions();

	public Execution getExecution(Long executionId);

	public Execution deleteExecution(Long executionId);

}
