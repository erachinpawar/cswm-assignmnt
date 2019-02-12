package com.cswm.assignment.applicationutils;


/*
 * All Error codes are maintained in the enum
 * 
 */
public enum ErrorMessageEnum {

	ORDER_BOOK_NOT_FOUND("Order Book Id not present in the system."),
	EXECUTION_NOT_FOUND("Execution not present in the system."),
	INSRTUMENT_NOT_PRESENT("Instrument is not present in the order."),
	BOOK_WITHOUT_INSTRUMENT("Order Book Can not have empty instrument."),
	NOT_EXECUTED_ORDER_BOOK_PRESENT("Non executed order book present for the same instrument."),
	ORDER_QUANTITY_INVALID("Order Quantity can not be empty or negative."),
	ORDER_BOOK_NOT_OPEN("In order to add orders in order book, Order book status should be OPEN."),
	ORDER_NOT_BELONG_TO_INSTRUMENT(
			"Order and Order Book does not belongs to same instrument. So can not add order to the order book."),
	BOOK_STATUS_NOT_OPEN("Order Book Status should be open to close it."),
	EXECUTION_QTY_INVALID("Execution Quantity Invalid."),
	EXECUTION_PRICE_INVALID("Execution price is not equal to prev executions."),
	ORDER_BOOK_EXECUTED("Order Book Fully executed.No more executions allowed."),
	PARTIALLY_EXECUTED("Execution Partially executed as the order demand limit for the book is reached."),
	ORDER_NOT_FOUND("Order Id not present in the system"), EXECUTION_PRICE_ZERO("Execution price can not be zero"), 
	ORDER_BOOK_NOT_CLOSED("Order book status is not closed. Order book status should be closed in order to add execution.");

	private String message;

	ErrorMessageEnum(String messageCode) {
		this.message = messageCode;
	}

	public String getMessage() {
		return message;
	}

}
