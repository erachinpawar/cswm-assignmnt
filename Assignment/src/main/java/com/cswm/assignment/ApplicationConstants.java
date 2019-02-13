package com.cswm.assignment;

public class ApplicationConstants {

	private ApplicationConstants() {
		throw new IllegalStateException("Constants class can not be instantiated");
	}

	public static final String APPLICATION_SHUTDOWN_WARN = "Applicatio Shutdown initiated it will be closed in 30 secs ..";
	public static final String DEFAULT_USER = "Default User";
	public static final String CLOSED_STATUS = "close";
	public static final String APPLICATION_SHUTDOWN_MSG = "Application closed now ....";
	public static final Long GRACEFUL_SHUTDOWN_WINDOW = 30l; /// IN SECONDS
	public static final String APPLICATION_NAME = "Instrument order management system";
	public static final String APPLICATION_NAME_DESC = "This page lists all the Orders placed.";
	public static final String APPLICATION_NAME_VERSION = "1.0-SNAPSHOT";

}
