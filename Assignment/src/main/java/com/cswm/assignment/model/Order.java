package com.cswm.assignment.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "orders_inv")
@Data
@Builder
public class Order {

	@Id
	@Column(name = "order_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orders_inv_seq")
	private Long orderId;

	@OneToOne
	@JoinColumn(name = "instrument_id")
	private final Instrument instrument;

	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "order_book_id", nullable = true)
	@JsonBackReference
	private final OrderBook orderBook;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "orderDetails_id")
	private final OrderDetails orderDetails;

	@Column(name = "order_quantity")
	@ColumnDefault("0")
	private final Long orderQuantity;

	@Column(name = "order_price")
	@ColumnDefault("0") //
	private final BigDecimal orderprice;

	@Column(name = "created_by")
	private final String createdBy;

	@Column(name = "created_on")
	private final LocalDateTime createdOn;

	public Order() {
		this(null, null, null, null, null, null, null, null);
	}

	public Order(Long orderId, Instrument instrument, OrderBook orderBook, OrderDetails orderDetails,
			Long orderQuantity, BigDecimal orderprice, String createdBy, LocalDateTime createdOn) {
		this.orderId = orderId;
		this.instrument = instrument;
		this.orderBook = orderBook;
		this.orderDetails = orderDetails;
		this.orderQuantity = orderQuantity;
		this.orderprice = orderprice;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
	}

	public Long getOrderId() {
		return orderId;
	}

	public Instrument getInstrument() {
		return instrument;
	}

	public OrderBook getOrderBook() {
		return orderBook;
	}

	public OrderDetails getOrderDetails() {
		return orderDetails;
	}

	public Long getOrderQuantity() {
		return orderQuantity;
	}

	public BigDecimal getOrderprice() {
		return orderprice;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	@Override
	public String toString() {
		return "Order [orderId=" + orderId + ", instrument=" + instrument + ", orderDetails=" + orderDetails
				+ ", orderQuantity=" + orderQuantity + ", orderprice=" + orderprice + ", createdBy=" + createdBy
				+ ", createdOn=" + createdOn + "]";
	}

}
