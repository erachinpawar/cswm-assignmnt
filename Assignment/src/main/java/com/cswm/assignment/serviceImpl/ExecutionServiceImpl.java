package com.cswm.assignment.serviceImpl;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cswm.assignment.ApplicationConstants;
import com.cswm.assignment.applicationUtils.ErrorMessageEnum;
import com.cswm.assignment.exceptions.NotFoundException;
import com.cswm.assignment.model.Execution;
import com.cswm.assignment.model.OrderBook;
import com.cswm.assignment.repository.ExecutionRepository;
import com.cswm.assignment.service.ExecutionService;

@Service
public class ExecutionServiceImpl implements ExecutionService {

	@Autowired
	ExecutionRepository executionRepository;

	@Override
	public Execution getExecution(Long executionId) {
		return executionRepository.findById(executionId)
				.orElseThrow(() -> new NotFoundException(ErrorMessageEnum.EXECUTION_NOT_FOUND));
	}

	@Override
	public Execution getTempExecutionForOrder(OrderBook orderBook) {

		Execution execution = new Execution();

		if (!orderBook.getExecutions().isEmpty()) {
			Execution ordBookExec = orderBook.getExecutions().iterator().next();
			execution.setPrice(ordBookExec.getPrice());
		} else {
		}
		execution.setCreatedBy(ApplicationConstants.DEFAULT_USER);
		execution.setCreatedOn(LocalDateTime.now());
		execution.setOrderBook(orderBook);
		return execution;
	}

}
