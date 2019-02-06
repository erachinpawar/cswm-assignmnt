<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
	xmlns:th="http://www.thymeleaf.org">
<head>
<meta charset="UTF-8">
<title>Credit-Suisse WM</title>
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
			<div class="centered">
				<table border="2" width="70%" cellpadding="2">
					<tr>
						<th>Order Book Id</th>
						<th>Status of Order Book</th>
						<th>Instrument</th>
						<th>Created By</th>
						<th>Created ON</th>
						<th>Delete</th>
						<th>Open/Close</th>
						<th>Order Book Statistics</th>
					</tr>

					<tr th:each="orderBook : ${orderBooks}">
						<td th:text="${orderBook.orderBookId}" />
						<td th:text="${orderBook?.instrument.instrumentId}"></td>
						<td th:text="${orderBook.orderBookStatus}" />
						<td th:text="${orderBook.createdBy}" />
						<td th:text="${orderBook.createdOn}" />
						<td><a
							th:href="@{'/orderBookEdit/' + ${orderBook.orderBookId}}">Edit</a>
						</td>
						<td>
							<a
							th:href="@{'/orderBookOpen/' + ${orderBook.orderBookId}}"
							th:if="${orderBook.orderBookStatus.toString()  == 'CLOSE'}">Open</a>
						</td>
						<td>
							<a th:href="@{'/getBookStats/'+ ${orderBook.orderBookId}}">OrderBook Statistics </a>
						</td>
						
					</tr>

				</table>
				<br /> <a href="/createOrderBook">Create New Order Book</a>
			</div>
		</div>



	</div>


</body>
</html>