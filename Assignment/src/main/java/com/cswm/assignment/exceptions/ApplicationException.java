package com.cswm.assignment.exceptions;

import com.cswm.assignment.applicationutils.ErrorMessageEnum;

public class ApplicationException extends RuntimeException{

	private static final long serialVersionUID = 6741961997937330364L;
	
	public ApplicationException(ErrorMessageEnum errorCode) {
		super(errorCode.getMessage());
	}

}
