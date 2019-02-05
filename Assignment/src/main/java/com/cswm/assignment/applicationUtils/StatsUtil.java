package com.cswm.assignment.applicationUtils;

public class StatsUtil {


	public static Long getEffectiveQtyForExec(Long accumltdOrders, Long accExecQty, Long quantity) {
		if ((accExecQty + quantity) > accumltdOrders)
			return (long) (accumltdOrders - accExecQty);
		return quantity;
	}

	public static Long getProRataExecution(Long orderQuantity, Long effectiveQuanty, Long accumltdOrders) {
		return (long) (orderQuantity*effectiveQuanty)/accumltdOrders;
	}
	
	

}
