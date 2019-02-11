package com.cswm.assignment.model.dto.inputDto;

import java.math.BigDecimal;

public class ExecutionInputDto {

	private BigDecimal price;
	private Long quantity;

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "ExecutionInputDto [price=" + price + ", quantity=" + quantity + "]";
	}

}
