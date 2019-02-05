package com.cswm.assignment.service;

import org.springframework.stereotype.Service;

import com.cswm.assignment.model.Execution;
import com.cswm.assignment.model.OrderBook;

@Service
public interface ExecutionService {

	Execution getExecution(Long l);

	Execution getTempExecutionForOrder(OrderBook orderBook);
}
