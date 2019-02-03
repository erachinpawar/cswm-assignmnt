package com.cswm.assignment.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Entity
@Table(name = "order_book_inv")
@Data
public class OrderBook {

	public enum OrderBookStatus {
		OPEN, CLOSED
	}

	public enum ExecutionStatus {
		EXECUTED, NOT_EXECUTED
	}

	@Id
	@Column(name = "order_book_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_book_inv_seq")
	private Long orderBookId;

	@Column(name = "order_book_name")
	private String orderBookName;

	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "instrument_id")
	private Instrument instrument;

	@Enumerated(EnumType.STRING)
	@Column(name = "order_Book_Status", length = 20)
	private OrderBookStatus orderBookStatus;;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "orderBook", cascade = CascadeType.ALL)
	@JsonManagedReference
	private Set<Execution> executions;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "orderBook", cascade = CascadeType.ALL)
	@JsonManagedReference
	private Set<Order> orders;

	@Enumerated(EnumType.STRING)
	@Column(name = "is_book_executed")
	private ExecutionStatus executionStatus;

	@Column(name = "created_by")
	private String createdBy;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "created_on")
	private Date createdOn;

	
	
	public OrderBook() {
	}

	public Long getOrderBookId() {
		return orderBookId;
	}

	public void setOrderBookId(Long orderBookId) {
		this.orderBookId = orderBookId;
	}

	public String getOrderBookName() {
		return orderBookName;
	}

	public void setOrderBookName(String orderBookName) {
		this.orderBookName = orderBookName;
	}

	public Instrument getInstrument() {
		return instrument;
	}

	public void setInstrument(Instrument instrument) {
		this.instrument = instrument;
	}

	public OrderBookStatus getOrderBookStatus() {
		return orderBookStatus;
	}

	public void setOrderBookStatus(OrderBookStatus orderBookStatus) {
		this.orderBookStatus = orderBookStatus;
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


	

	public ExecutionStatus getExecutionStatus() {
		return executionStatus;
	}

	public void setExecutionStatus(ExecutionStatus executionStatus) {
		this.executionStatus = executionStatus;
	}

	public Set<Execution> getExecutions() {
		return executions;
	}

	public void setExecutions(Set<Execution> executions) {
		this.executions = executions;
	}

	public Set<Order> getOrders() {
		return orders;
	}

	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}

	@Override
	public String toString() {
		return "OrderBook [orderBookId=" + orderBookId + ", orderBookName=" + orderBookName + ", instrument="
				+ instrument + ", orderBookStatus=" + orderBookStatus + ", executions=" + executions + ", orders="
				+ orders + ", executionStatus=" + executionStatus + ", createdBy=" + createdBy + ", createdOn="
				+ createdOn + "]";
	}

}
