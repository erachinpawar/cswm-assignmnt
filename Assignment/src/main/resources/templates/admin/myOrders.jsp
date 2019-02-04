<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:th="http://www.thymeleaf.org">
<head>
<meta charset="UTF-8">
<title>CS-WMPage Page</title>
<link rel="stylesheet" type="text/css" th:href="@{/css/home.css}" />
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
						<li><a href="/admin/myOrders">Order Inventory</a></li>
						<li><a href="/admin/AboutMe">About Me</a></li>
					</ul>
				</div>

			</div>
			<br><br>
			<p class="admin-message-text text-center" th:utext="${adminMessage}"></p>
			<h1>Order List</h1>
			<table border="2" width="70%" cellpadding="2">
				<tr>
					<th>Order Id</th>
					<th>Order Name</th>
					<th>Instrument Name </th>
					<th>Quantity</th>
					<th>Order Price</th>
					<th>Order Status</th>
					<th>Order Type</th>
					<th>Order Book Name</th>
					<th>Execution Quantity</th>					
					<th>Created By</th>
					<th>Created ON</th>
					<th>Edit</th>
					<th>Delete</th>
					<th>Statistics</th>
				</tr>

				<tr th:each="order : ${allOrders}">
					<td th:text="${order.orderId}"/>
					<td th:text="${order.orderName}"/>
					<td th:text="${order?.instrument?.instrumentName}"/>
					<td th:text="${order.orderQuantity}"/>
					<td th:text="${order.orderprice}"/>
					<td th:text="${order?.orderStatus?.statusValue}"/>
					<td th:text="${order?.orderType?.orderType}"/>
					<td th:text="${order?.orderBook?.orderBookName}"/>
					<td th:text="${order.executionQuantity}"/>
					<td th:text="${order.createdBy}"/>
					<td th:text="${order.createdOn}"/>
					<td ><a th:href="@{'/orderEdit/' + ${order.orderId}}"
						th:if="${order?.orderBook == null} ">Edit</a> </td>
					<td ><a th:href="@{'/orderDelete/' + ${order.orderId}}"
					th:if="${order?.orderBook == null}">Delete</a> </td>
					
					<td ><a th:href="@{'/orders/' + ${order.orderId} + '/getOrderStats'}">View Statistics</a> </td>
					
					
				</tr>

			</table>
			<br /> <a href="/createOrder">Create New Order</a>
		</div>



	</div>


</body>
</html>