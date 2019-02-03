package com.cswm.assignment.serviceImpl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cswm.assignment.applicationUtils.ErrorMessageEnum;
import com.cswm.assignment.exceptions.ApplicationException;
import com.cswm.assignment.exceptions.NotFoundException;
import com.cswm.assignment.model.Execution;
import com.cswm.assignment.repository.ExecutionRepository;
import com.cswm.assignment.service.ExecutionService;

@Service
@Transactional
public class ExecutionServiceImpl implements ExecutionService {

	@Autowired
	ExecutionRepository executionRepository;

	@Override
	public List<Execution> getExecutions() {
		return executionRepository.findAll();
	}

	@Override
	public Execution getExecution(Long executionId) {
		return executionRepository.findById(executionId)
				.orElseThrow(() -> new NotFoundException(ErrorMessageEnum.EXECUTION_NOT_FOUND));
	}

	@Override
	public Execution deleteExecution(Long executionId) {
		throw new ApplicationException(ErrorMessageEnum.DELETE_EXECUTION_UNSUPPORTED);
	}

}
