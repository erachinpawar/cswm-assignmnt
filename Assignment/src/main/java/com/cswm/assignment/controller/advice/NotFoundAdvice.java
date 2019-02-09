package com.cswm.assignment.controller.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cswm.assignment.exceptions.NotFoundException;
import com.cswm.assignment.model.Message;

@ControllerAdvice
class NotFoundAdvice {

	final private Logger logger = LoggerFactory.getLogger(NotFoundAdvice.class);

	/*
	 * No resource present or found
	 */
	@ResponseBody
	@ExceptionHandler(NotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	Message NotFoundHandler(NotFoundException ex) {
		logger .error("Not found Exception occured : ",ex);
		return new Message(HttpStatus.NOT_FOUND,ex.getMessage());
	}
}
