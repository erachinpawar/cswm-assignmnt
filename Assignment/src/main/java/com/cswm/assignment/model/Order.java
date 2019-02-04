package com.cswm.assignment.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "orders_inv")
public class Order {

	public enum OrderType {
		MARKET_ORDER, LIMIT_ORDER
	}

	public enum OrderStatus {
		VALID, INVALID, NO_EXECUTION_ON_BOOK_YET
	}

	@Transient
	private static final Double LIMIT_ORDER_PRICE=50d;

	@Id
	@Column(name = "order_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orders_inv_seq")
	private Long orderId;

	@Column(name = "order_name")
	private String orderName;

	@OneToOne
	@JoinColumn(name = "instrument_id")
	private Instrument instrument;

	@Column(name = "order_quantity")
	private Long orderQuantity;

	@Column(name = "order_price")
	private Double orderprice;


	
	
	@Transient
	private OrderStatus orderStatus;

	@Enumerated(EnumType.STRING)
	@Column(name = "order_type", length = 20)
	private OrderType orderType;

	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "order_book_id", nullable = true)
	@JsonBackReference
	private OrderBook orderBook;

	@Column(name = "execution_quantity")
	private Double executionQuantity;

	@Column(name = "created_by")
	private String createdBy;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "created_on")
	private Date createdOn;

	public Order() {
	}

	public Order(OrderBook orderBook, String createdBy, Date createdOn, Order order) {
		super();
		this.orderId = order.orderId;
		this.orderName = order.orderName;
		this.instrument = order.instrument;
		this.orderQuantity = order.orderQuantity;
		this.orderprice = order.orderprice;
		this.orderStatus = order.orderStatus;
		this.orderType = order.orderType;
		this.orderBook = orderBook;
		this.executionQuantity = order.executionQuantity;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
		if (OrderType.LIMIT_ORDER == order.orderType)
			this.orderprice = LIMIT_ORDER_PRICE;
	}

	public Order(Order order, double executionQuantity) {
		super();
		this.orderId = order.orderId;
		this.orderName = order.orderName;
		this.instrument = order.instrument;
		this.orderQuantity = order.orderQuantity;
		this.orderprice = order.orderprice;
		this.orderStatus = order.orderStatus;
		this.orderType = order.orderType;
		this.orderBook = order.orderBook;
		this.executionQuantity = order.executionQuantity;
		this.createdBy = order.createdBy;
		this.createdOn = order.createdOn;
		if (OrderType.LIMIT_ORDER == order.orderType)
			this.orderprice = LIMIT_ORDER_PRICE;
		if (0d != executionQuantity)
			this.executionQuantity = executionQuantity;
	}

	public Order(String orderName, Instrument instrument, Long orderQuantity, Double orderprice,
			OrderStatus orderStatus, OrderType orderType, OrderBook orderBook, Double executionQuantity,
			String createdBy, Date createdOn) {
		super();
		this.orderName = orderName;
		this.instrument = instrument;
		this.orderQuantity = orderQuantity;
		this.orderprice = orderprice;
		this.orderStatus = orderStatus;
		this.orderType = orderType;
		this.orderBook = orderBook;
		if (OrderType.LIMIT_ORDER ==orderType)
			this.orderprice = LIMIT_ORDER_PRICE;
		this.executionQuantity = executionQuantity;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
	}

	public Long getOrderId() {
		return orderId;
	}

	public String getOrderName() {
		return orderName;
	}

	public Instrument getInstrument() {
		return instrument;
	}

	public Long getOrderQuantity() {
		return orderQuantity;
	}

	public Double getOrderprice() {
		return orderprice;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public OrderType getOrderType() {
		return orderType;
	}

	public OrderBook getOrderBook() {
		return orderBook;
	}

	public Double getExecutionQuantity() {
		return executionQuantity;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	@Override
	public String toString() {
		return "Order [orderId=" + orderId + ", orderName=" + orderName + ", instrument=" + instrument
				+ ", orderQuantity=" + orderQuantity + ", orderprice=" + orderprice + ", orderStatus=" + orderStatus
				+ ", orderType=" + orderType + ", executionQuantity=" + executionQuantity + ", createdBy=" + createdBy
				+ ", createdOn=" + createdOn + "]";
	}

	static public class OrderBuilder {
		private Long orderId;
		private String orderName;
		private Instrument instrument;
		private Long orderQuantity;
		private Double orderprice;
		private OrderStatus orderStatus;
		private OrderType orderType;
		private OrderBook orderBook;
		private Double executionQuantity;
		private String createdBy;
		private Date createdOn;

		public OrderBuilder(Order order) {
			this.orderId = order.orderId;
			this.orderName = order.orderName;
			this.instrument = order.instrument;
			this.orderQuantity = order.orderQuantity;
			this.orderprice = order.orderprice;
			this.orderStatus = order.orderStatus;
			this.orderType = order.orderType;
			this.orderBook = order.orderBook;
			this.executionQuantity = order.executionQuantity;
			if (OrderType.LIMIT_ORDER == order.orderType)
				this.orderprice = LIMIT_ORDER_PRICE;
			this.createdBy = order.createdBy;
			this.createdOn = order.createdOn;
		}

		public OrderBuilder() {
		}

		public Long getOrderId() {
			return orderId;
		}

		public void setOrderId(Long orderId) {
			this.orderId = orderId;
		}

		public String getOrderName() {
			return orderName;
		}

		public void setOrderName(String orderName) {
			this.orderName = orderName;
		}

		public Instrument getInstrument() {
			return instrument;
		}

		public void setInstrument(Instrument instrument) {
			this.instrument = instrument;
		}

		public Long getOrderQuantity() {
			return orderQuantity;
		}

		public void setOrderQuantity(Long orderQuantity) {
			this.orderQuantity = orderQuantity;
		}

		public Double getOrderprice() {
			return orderprice;
		}

		public void setOrderprice(Double orderprice) {
			this.orderprice = orderprice;
		}

		public OrderStatus getOrderStatus() {
			return orderStatus;
		}

		public void setOrderStatus(OrderStatus orderStatus) {
			this.orderStatus = orderStatus;
		}

		public OrderType getOrderType() {
			return orderType;
		}

		public void setOrderType(OrderType orderType) {
			this.orderType = orderType;
		}

		public OrderBook getOrderBook() {
			return orderBook;
		}

		public void setOrderBook(OrderBook orderBook) {
			this.orderBook = orderBook;
		}

		public Double getExecutionQuantity() {
			return executionQuantity;
		}

		public void setExecutionQuantity(Double executionQuantity) {
			this.executionQuantity = executionQuantity;
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

		@DateTimeFormat(pattern = "yyyy-MM-dd")
		public void setCreatedOn(Date createdOn) {
			this.createdOn = createdOn;
		}

		public Order build() {
			return new Order(this);
		}

	}

	public Order(OrderBuilder orderBuilder) {
		super();
		this.orderId = orderBuilder.orderId;
		this.orderName = orderBuilder.orderName;
		this.instrument = orderBuilder.instrument;
		this.orderQuantity = orderBuilder.orderQuantity;
		this.orderprice = orderBuilder.orderprice;
		this.orderStatus = orderBuilder.orderStatus;
		this.orderType = orderBuilder.orderType;
		this.orderBook = orderBuilder.orderBook;
		this.executionQuantity = orderBuilder.executionQuantity;
		this.createdBy = orderBuilder.createdBy;
		this.createdOn = orderBuilder.createdOn;
		if (OrderType.LIMIT_ORDER == orderBuilder.orderType)
			this.orderprice = LIMIT_ORDER_PRICE;
	}

}
