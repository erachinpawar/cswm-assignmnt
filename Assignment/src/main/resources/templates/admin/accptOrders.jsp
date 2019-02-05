<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
	xmlns:th="http://www.thymeleaf.org">
<head>
<meta charset="UTF-8">
<title>CS-WMPage Page</title>
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
<title>Credit-Suisse WM</title>
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
			<br>
			<br>
			<p class="admin-message-text text-center" th:utext="${adminMessage}"></p>

			<form action="#" th:action="@{/saveOrderBook}"
				th:object="${orderBook}" method="post">

				<p>
					Book Id : <input type="text" th:field="*{orderBookId}"
						readonly="readonly" />
				<p>
					Book Name : <input type="text" th:field="*{orderBookName}" />
				</p>
				<p>
					Order Book Execution Status : <input type="text"
						th:field="*{executionStatus}" readonly="readonly" />
				<p>
					Order Book Created By : <input type="text" th:field="*{createdBy}" />
				<p>
					Order Book Created on : <input type="text" th:field="*{createdOn}" />
				</p>

				<h1>Orphan Orders for the Instrument:</h1>
				<span
					th:text="${#numbers.formatDecimal(orderSum, 0, 'COMMA', 2, 'POINT')}">
					<table border="2" width="70%" cellpadding="2">
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
							<th>Accept</th>
						</tr>

						<tr th:each="order : ${orphanOrders}">
							<td th:text="${order.orderId}" />
							<td th:text="${order.orderName}" />
							<td th:text="${order.instrument.instrumentName}" />
							<td th:text="${order.orderQuantity}" />
							<td th:text="${order.orderprice}" />
							<td th:text="${order?.orderStatus?.statusValue}" />
							<td th:text="${order.orderType.orderType}" />
							<td th:text="${order.executionQuantity}" />
							<td th:text="${order.createdBy}" />
							<td th:text="${order.createdOn}" />
							<td><a
								th:href="@{'/orderBooks/'+${orderBook.orderBookId}+'/acceptOrder/' + ${order.orderId} }"
								th:if="${orderBook.orderBookStatus.orderBookStatusId == 1001} ">Accept
									Order</a></td>
						</tr>

					</table>

					<h3>Execution List to the Order Book:</h3>
					<table border="2" width="70%" cellpadding="2">
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
							<td th:text="${execution.price}" />
							<td th:text="${execution.quantity}" />
							<td th:text="${execution.createdBy}" />
							<td th:text="${execution.createdOn}" />
						</tr>

					</table>

				</span> <br /> <br>
				<p>
					<input type="reset" value="Reset" />
					<button>
						<a th:href="@{'/orderBookEdit/'+ ${orderBook.orderBookId} }">Back
							To Order Book</a>
					</button>
					<button>
						<a th:href="@{'/admin/myorderbook'}">OrderBook List </a>
					</button>
				</p>
			</form>



		</div>
	</div>
</body>
</html>