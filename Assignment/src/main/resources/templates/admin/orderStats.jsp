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
			<h2>
				<p class="admin-message-text text-center" th:utext="${adminMessage}">
			</h2>

			<br>

			<form action="#" th:object="${order}" method="post">

				<p>
					Order Id : <input type="text" th:field="${order.orderId}"
						readonly="readonly" />
				<p>
					Order Name : <input type="text" th:field="${order.orderName}"
						readonly="readonly" />
				<p>
					Order Price : <input type="text" th:field="${order.orderprice}"
						readonly="readonly" />
				<p>
					Validity Status : <input type="text"
						th:field="${orderStatsVo.orderStatus}" readonly="readonly" />
				<p>
					Execution Quantity: <input type="text"
						th:field="${orderStatsVo.executionPrice}" readonly="readonly" />
				<p>
					Order Created By : <input type="text" th:field="${order.createdBy}"
						readonly="readonly" />
				<p>
					Order Created on : <input type="text" th:field="${order.createdOn}"
						readonly="readonly" />
			</form>

			<br> <br>
			<button>
				<a th:href="@{'/admin/myorderbook'}">Orders Book List </a>
			</button>
			</button>
			<button>
				<a
					th:href="@{'/orderBookEdit/' + ${order.orderBook.orderBookId}}">Back
					to Order Edit </a>
			</button>
		</div>
	</div>
</body>
</html>