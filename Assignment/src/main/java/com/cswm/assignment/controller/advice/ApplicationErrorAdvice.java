 package com.cswm.assignment.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cswm.assignment.exceptions.ApplicationException;
import com.cswm.assignment.model.Message;
@ControllerAdvice
public class ApplicationErrorAdvice {

	/*
	 * Advice whenever there will be an application exception.
	 * Errorcodes can be configured in the Error Enum
	 */
	@ResponseBody
	@ExceptionHandler(ApplicationException.class)
	@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
	Message appliactionExceptionHandler(ApplicationException ex) {
		return new Message(HttpStatus.NOT_ACCEPTABLE,ex.getMessage());
	}


}
