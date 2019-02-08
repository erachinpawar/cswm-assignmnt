package com.cswm.assignment.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cswm.assignment.exceptions.NotFoundException;
import com.cswm.assignment.model.Message;

@ControllerAdvice
class NotFoundAdvice {

	/*
	 * No resource present or found
	 */
	@ResponseBody
	@ExceptionHandler(NotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	Message NotFoundHandler(NotFoundException ex) {
		return new Message(HttpStatus.NOT_FOUND,ex.getMessage());
	}
}
