package com.cswm.assignment;

/*
 * All the URIs constants are maintained here 
 */
public class UrlConstants {
	
	
	private UrlConstants() {
		throw new IllegalStateException("Constants class can not be instantiated");
	}
	public static final  String  URL_GET_ORDER_BOOK_STATISTICS="/orderbooks/{orderBookId}/stastitics";
	public static final  String  URL_GET_ORDER_STATISTICS="/orderbooks/{orderBookId}/orderStatistics/{orderId}";
	public static final  String  URL_CREATE_ORDER_BOOK="/orderbooks/create";
	public static final  String  URL_ADD_ORDER_BOOK="/orderbooks/{orderBookId}/orders";
	public static final  String  URL_CLOSE_ORDER_BOOK="/orderbooks/{orderBookId}/close";
	public static final  String  URL_EXECUTE_ORDER_BOOK="/orderbooks/{orderBookId}/execution";
}
