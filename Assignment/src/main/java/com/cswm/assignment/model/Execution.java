package com.cswm.assignment.model;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "execution")
public class Execution {

	@Id
	@Column(name = "execution_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "execution_seq")
	private Long executionId;

	@Column(name = "execution_name")
	private String executionName;

	@Column(name = "price")
	private Double price;

	@Column(name = "qty")
	private Long quantity;

	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "order_book_id", nullable = false)
	@JsonBackReference
	private OrderBook orderBook;
	
	@Column(name = "created_by")
	private String createdBy;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "created_on")
	private Date createdOn;

	public Execution() {
	}

	public Long getExecutionId() {
		return executionId;
	}

	public void setExecutionId(Long executionId) {
		this.executionId = executionId;
	}

	public String getExecutionName() {
		return executionName;
	}

	public void setExecutionName(String executionName) {
		this.executionName = executionName;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public OrderBook getOrderBook() {
		return orderBook;
	}

	public void setOrderBook(OrderBook orderBook) {
		this.orderBook = orderBook;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	@Override
	public String toString() {
		return "Execution [executionId=" + executionId + ", executionName=" + executionName + ", price=" + price
				+ ", quantity=" + quantity + ", createdBy=" + createdBy + ", createdOn="
				+ createdOn + "]";
	}

}
