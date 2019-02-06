<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
	xmlns:th="http://www.thymeleaf.org">
<head>
<meta charset="UTF-8">
<title>Credit-Suisse WM</title>
<link rel="stylesheet" type="text/css" th:href="@{/css/home.css}" />
<link rel="" type="" th:href="@{javascript/utils.js}">
<link rel="icon"
	href="http://logok.org/wp-content/uploads/2014/10/Credit_Suisse_Logo-880x600.png">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
</head>
<body>
	<div class="container">



		<div class="panel-group" style="margin-top: 40px">
			<div class="panel panel-primary">
				<div class="panel-heading">
					<span th:utext="${userName}"></span>
				</div>
				<div class="menu-navigation">
					<ul>
						<li><a href="/admin/home">Home</a></li>
						<li><a href="/admin/myorderbook">Order Book Inventory</a></li>
						<li><a href="/admin/AboutMe">About Me</a></li>
					</ul>
				</div>
			</div>
			<br> <br>
			<p class="admin-message-text text-center" th:utext="${adminMessage}"></p>

			<form action="#" th:action="@{/saveOrderBook}"
				th:object="${orderBook}" method="post">

				<p>
					Book Id : <input type="text" th:field="*{orderBookId}"
						readonly="readonly" />
				<p>
					Order Book Status : <select th:field="*{orderBookStatus}">
						<option
							th:each="bookStatus : ${T(com.cswm.assignment.applicationUtils.OrderBookStatus).values()}"
							th:value="${bookStatus}" th:text="${bookStatus}"
							th:selected="*{orderBookStatus}"></option>

					</select>
					
				<p >
					Order Book Instrument Name: <input type="text"
						th:field="*{instrument.instrumentId}" th:readonly="${orderBook.orderBookId!=null}" />
				</p>

				<p>
					Order Book Created By : <input type="text" th:field="*{createdBy}"
						readonly="readonly" />
				<p>
					Order Book Created on : <input type="text" th:field="*{createdOn}"
						readonly="readonly" />
				<%-- <h3 th:if="${null!=orderBook.orders and  !orderBook.orders.empty}">Order
					List in the Order Book:</h3>
				<table border="2" width="70%" cellpadding="2"
					th:if="${null!=orderBook.orders and !orderBook?.orders?.empty}">
					<tr>
						<th>Order Id</th>
						<th>Order Name</th>
						<th>Instrument Name</th>
						<th>Quantity</th>
						<th>Total Price Of Order</th>
						<th>Status of Order</th>
						<th>Order Type</th>
						<th>Execution Quantity</th>
						<th>Created By</th>
						<th>Created ON</th>
						<th>View Order Statistics</th>
					</tr>

					<tr th:each="order : ${orderBook.orders}">
						<td th:text="${order?.orderId}" />
						<td th:text="${order?.orderName}" />
						<td th:text="${order?.instrument.instrumentName}" />
						<td
							th:text="${#numbers.formatDecimal(order?.orderQuantity, 0, 2)}" />
						<td th:text="${#numbers.formatDecimal(order?.orderprice, 0, 2)}" />
						<td th:text="${order?.orderStatus}" />
						<td th:text="${order?.orderType}" />
						<td
							th:text="${#numbers.formatDecimal(order?.executionQuantity, 0, 2)}" />
						<td th:text="${order.createdBy}" />
						<td th:text="${order.createdOn}" />

						<td><a th:href="@{'/getOrderStats/'+ ${order?.orderId}}">Order
								Statistics </a></td>
					</tr>

				</table>

				<h3
					th:if="${null!=orderBook.executions and !orderBook?.executions?.empty}">Execution
					List to the Order Book:</h3>
				<table border="2" width="70%" cellpadding="2"
					th:if="${null!=orderBook.executions and!orderBook?.executions?.empty}">
					<tr>
						<th>Execution Id</th>
						<th>Execution Name</th>
						<th>Execution Price</th>
						<th>Quantity</th>
						<th>Created By</th>
						<th>Created ON</th>
					</tr>

					<tr th:each="execution : ${orderBook.executions}">
						<td th:text="${execution.executionId}" />
						<td th:text="${execution.executionName}" />
						<td th:text="${#numbers.formatDecimal(execution.price, 0, 2)}" />
						<td th:text="${#numbers.formatDecimal(execution.quantity, 0, 2)}" />
						<td th:text="${execution.createdBy}" />
						<td th:text="${execution.createdOn}" />
					</tr>

				</table> --%>

				<br /> <br>
				<p>
					<input type="submit" value="Submit" /> <input type="reset"
						value="Reset" />
					<button>
						<a th:href="@{'/admin/myorderbook'}">OrderBook List </a>
					</button>

					<button
						th:if="${orderBook.orderBookStatus == T(com.cswm.assignment.applicationUtils.OrderBookStatus).CLOSED
								and orderBook.executionStatus!=T(com.cswm.assignment.applicationUtils.ExecutionStatus).EXECUTED 
								and orderBook.orderBookId !=0} ">
						<a
							th:href="@{'/addExecutionToOrderBook/'+ ${orderBook.orderBookId}}">Add
							Executions to order Book </a>
					</button>

					<button
						th:if="${orderBook.orderBookStatus == T(com.cswm.assignment.applicationUtils.OrderBookStatus).OPEN 
								and orderBook.orderBookId != null} ">
						<a th:href="@{'/addOrderToOrderBook/'+ ${orderBook.orderBookId}}">Add
							Order to order Book </a>
					</button>
					<button>
						<a th:href="@{'/getBookStats/'+ ${orderBook.orderBookId}}">OrderBook
							Statistics </a>
					</button>

				</p>
			</form>



		</div>
	</div>
</body>
</html>