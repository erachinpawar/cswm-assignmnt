package com.cswm.assignment.applicationUtils;

public enum ErrorMessageEnum {

	ORDER_BOOK_NOT_FOUND("Order Book Id not present in the system"),
	DELETE_BOOK_NOT_SUPPORTED("Order Book deletion is not allowed in the system"),
	ORDER_NOT_FOUND_FOR_BOOK("Provided order id is not present fot in the given order book"),
	ADD_ORDER_ORDERBOOK_CLOSED("Orders can not be aded to closed order book"),
	ADD_ORDER_ORDERBOOK_STATUS_UNKNOWN("Unable to add the orders to order Book with unknown status"),
	UPDATE_ORDER_NOT_ALLOWED("Orders are immutable and not allowed to update in the system"),
	DELETE_ORDER_NOT_SUPPORTED("Order deletion after accept is not allowed in the system"),
	REMOVE_ORDER_ORDERBOOK_CLOSED("Orders can not be removed from order book"),
	REMOVE_ORDER_ORDERBOOK_STATUS_UNKNOWN("orders can be removed only from the order book with open status"),
	ORDER_NOT_FOUND("Order Id not present in the system"),
	REMOVE_ORDER_ORDERBOOK("Order associated with some order book already"),
	CAN_NOT_ADD_EXECUTION_IN_CREATION("Executions cannot be added while creating the order book"),
	ORDERS_ASSOCIATED_WITH_OTHER_BOOK("Some of the orders are already associated with other order book"),
	INSRTUMENT_NOT_PRESENT("Instrument is not present in the database"),
	DEPENDENT_INSTRUMENT("Dependant Order / Order Book present in the system"),
	EXECUTION_NOT_FOUND("Execution not present in the system"),
	DELETE_EXECUTION_UNSUPPORTED("Execution deletion is not allowed in the system"),
	EXECUTION_CAN_NOT_BE_ADDED("Executions can not be added on open order book"),
	ADD_EXECUTION_ORDERBOOK_STATUS_UNKNOWN("Unable to add executions to order Book with unknown status"),
	ORDER_BOOK_EXECUTED("Order Book Fully executed.No more executions allowed"),
	PARTIALLY_EXECUTED("Execution Partially executed as the order demand limit for the book is reached"),
	BOOK_EXECUTED_IN_CREATION("Order Book Execution Status can not be Executed at creation"),
	BOOK_NAME_BLANK("Order Book Name can not be blank"),
	BOOK_WITHOUT_INSTRUMENT("Order Book Can not have empty instrument"),
	ORDER_BOOK_CAN_NOT_BECLOSED("Can not create the order book with status closed"),
	ORDER_NOT_BELONG_TO_INSTRUMENT(
			"Order and Order Book does not belongs to same instrumene. So Can not add instrument."),
	ORDER_NAME_INVALID("Order Name can not be empty"),
	ORDER_QUANTITY_INVALID("Order Quantity can not be empty or negative"),
	ORDER_PRICE_INVALID("Order Price can not be empty or negative"), ORDER_TYPE_INVALID("Order Type can not be empty"),
	INSRTUMENT_NAME_INVALID("Instument name can not be empty"),
	BOOK_STATUS_OPEN_CLOSE("Possible values of the book status are OPEN / CLOSE only"),
	INVALID_EXECUTION_NAME("Execution Name Invalid"), EXECUTION_QTY_INVALID("Execution Quantity Invalid"),
	EXECUTION_PRICE_INVALID("Execution price is not equal to prev executions");

	private String message;

	ErrorMessageEnum(String messageCode) {
		this.message = messageCode;
	}

	public String getMessage() {
		return message;
	}

}
