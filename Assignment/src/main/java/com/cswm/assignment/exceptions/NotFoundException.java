package com.cswm.assignment.exceptions;

import com.cswm.assignment.applicationutils.ErrorMessageEnum;

public class NotFoundException extends RuntimeException{

	private static final long serialVersionUID = -2108458591732591881L;

	public NotFoundException(ErrorMessageEnum orderBookNotFound) {
		super(orderBookNotFound.getMessage());
	}
	
}


