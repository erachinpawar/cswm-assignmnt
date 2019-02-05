<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:th="http://www.thymeleaf.org">
<head>
<meta charset="UTF-8">
<title>Credit-Suisse WM</title>
<link rel="stylesheet" type="text/css" th:href="@{/css/home.css}" />
<link rel="icon"
	href="http://logok.org/wp-content/uploads/2014/10/Credit_Suisse_Logo-880x600.png">
<link rel="" type="" th:href="@{javascript/utils.js}">
<link rel="icon" href="https://pbs.twimg.com/profile_images/649126783535591424/v0rtnv5-_400x400.png">

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
			<br><br>
			<p class="admin-message-text text-center" th:utext="${adminMessage}"></p>

			<form action="#" th:action="@{/saveOrderBook}"
				th:object="${orderBook}" method="post">
				
				<p> Book Id : <input type="text" th:field="${orderBook.orderBookId}" readonly="readonly"/>
				<p> Book Name : <input type="text" th:field="${orderBook.orderBookName}" readonly="readonly"/></p>
				<p> Order Book Status :  <input type="text" th:field="${orderBook.orderBookStatus}" readonly="readonly"/></p>
				<p> Order Book Instrument ID: <input type="text" th:field="${orderBook.instrument.instrumentName}" readonly="readonly"/></p>
				<p> Order Book Execution Status : <input type="text" th:field="${orderBook.executionStatus}" readonly="readonly" />
				<p> Order Book Created By  : <input type="text" th:field="${orderBook.createdBy}" readonly="readonly"/>
				<p> Order Book Created on : <input type="text" th:field="${orderBook.createdOn}" readonly="readonly"/>
				

				<h3>Order List in the Order Book: </h3>
				<table border="2" width="70%" cellpadding="2">
					<tr>
						<th>Order Count</th>
						<th>Accumulated Order Count</th>
						<th>Valid order count</th>
						<th>Invalid order count</th>
						<th>Valid Demand</th>
						<th>Invalid Demand</th>
						<th>Execution Quantity</th>
						<th>Execution Price</th>
					</tr>
					<tr>
						<td th:text="${orderBookStatsVo.totalNoOfOrders}" />
						<td th:text="${orderBookStatsVo.totalNoofAccuOrders}" />
						<td th:text="${orderBookStatsVo.validOrderCount}" />
						<td th:text="${orderBookStatsVo.inValidOrderCount}" />
						<td th:text="${orderBookStatsVo.validDemand}" />
						<td th:text="${orderBookStatsVo.invalidDemand}" />
						<td th:text="${orderBookStatsVo.executionQty}" />
						<td th:text="${orderBookStatsVo.executionPrice}" />
					</tr>

				</table>
				<br>
				<br>
				
				<table border="2" width="70%" cellpadding="2">
					<tr>
						<th><b>Order Details</b></th>
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
					</tr>
					<tr th:each="entry : ${orderBookStatsVo.orderStats}">
						<B><td th:text="${entry.key}" /></B>
						<td th:text="${entry?.value?.orderId}" />
						<td th:text="${entry?.value?.orderName}" />
						<td th:text="${entry?.value?.instrument?.instrumentName}" />
						<td th:text="${entry?.value?.orderQuantity}" />
						<td th:text="${entry?.value?.orderprice}" />
						<td th:text="${entry?.value?.orderStatus}" />
						<td th:text="${entry?.value?.orderType}" />
						<td th:text="${entry?.value?.executionQuantity}" />
						<td th:text="${entry?.value?.createdBy}" />
						<td th:text="${entry?.value?.createdOn}" />
					</tr>
				</table>

				<br />  <br>
				<p>
					<button><a th:href="@{'/admin/myorderbook'}">OrderBook List </a></button>
					<button><a th:href="@{'/orderBookEdit/'+ ${orderBook.orderBookId} }">Back To Order Book</a></button>
					
					
				</p>
			</form>



		</div>
	</div>
</body>
</html>