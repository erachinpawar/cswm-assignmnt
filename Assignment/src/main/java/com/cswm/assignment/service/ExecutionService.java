package com.cswm.assignment.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cswm.assignment.model.Execution;
import com.cswm.assignment.model.OrderBook;

@Service
public interface ExecutionService {

	public List<Execution> getExecutions();

	public Execution getExecution(Long executionId);

	public Execution deleteExecution(Long executionId);

	public Execution execurtiogetTempExecutionForOrder(OrderBook orderBook);


}
