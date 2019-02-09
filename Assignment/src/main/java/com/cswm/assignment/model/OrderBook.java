package com.cswm.assignment.model;

import java.time.LocalDateTime;
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

import com.cswm.assignment.applicationutils.OrderBookStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Entity
@Table(name = "order_book_inv")
@Data
public class OrderBook {

	@Id
	@Column(name = "order_book_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_book_inv_seq")
	private Long orderBookId;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "instrument_id")
	private Instrument instrument;

	@Enumerated(EnumType.STRING)
	@Column(name = "order_Book_Status", length = 20)
	private OrderBookStatus orderBookStatus;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "orderBook", cascade = CascadeType.ALL)
	@JsonManagedReference
	private Set<Execution> executions;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "orderBook", cascade = CascadeType.ALL)
	@JsonManagedReference
	private Set<Order> orders;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_on")
	private LocalDateTime createdOn;

	public OrderBook() {
	}

	public Long getOrderBookId() {
		return orderBookId;
	}

	public void setOrderBookId(Long orderBookId) {
		this.orderBookId = orderBookId;
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

	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
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
		return "OrderBook [orderBookId=" + orderBookId + ", instrument=" + instrument + ", orderBookStatus="
				+ orderBookStatus + ", executions=" + executions + ", orders=" + orders + ",  createdBy=" + createdBy
				+ ", createdOn=" + createdOn + "]";
	}

}
