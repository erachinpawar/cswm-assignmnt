package com.cswm.assignment.applicationUtils;

public class StatsUtil {

	public static Double getlinearExecutionPerOrder(Long size, Long quantity) {
		return (double) (quantity.doubleValue() / size.doubleValue());
	}

	public static Long getEffectiveQtyForExec(Long accumltdOrders, Double accExecQty, Long quantity) {
		if ((accExecQty + quantity) > accumltdOrders)
			return (long) (accumltdOrders - accExecQty);
		return quantity;
	}

}
