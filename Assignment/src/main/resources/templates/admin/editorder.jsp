
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

			<form action="#" th:action="@{/saveOrder}"
				th:object="${orderBuilder}" method="post">

				<p>
					Order Book Id : <input type="text"
						th:field="*{orderBook.orderBookId}" readonly="readonly" />
				</p>


				<p>
					Order Id : <input type="text" th:field="*{orderId}"
						readonly="readonly" />
				</p>
				<p>
					Order Name : <input type="text" th:field="*{orderName}" />
				</p>

				<p>
					Order Book Instrument ID: <input type="text" 
					th:field="*{instrument.instrumentId}" readonly="readonly" />
				<p>
					Order Book Instrument Name: <input type="text" 
					th:field="*{instrument.instrumentName}" readonly="readonly" />
				<p>
					Quantity : <input type="text" th:field="*{orderQuantity}" />
				</p>
				<p>
					Total Price Of Order : <input type="text" th:field="*{orderprice}" />
				</p>
				<p>
					Order Type : <select th:field="*{orderType}">
						<option
							th:each="orderType : ${T(com.cswm.assignment.applicationUtils.OrderType).values()}"
							th:value="${orderType}" 
							th:text="${orderType}"
							th:selected="*{orderType}"></option>
					</select>
				</p>

				<p>
					Execution Quantity : <input type="text"
						th:field="*{executionQuantity}" readonly="readonly" />
				</p>

				<p>
					Created By : <input type="text" th:field="*{createdBy}"
						readonly="readonly" />
				</p>
				<p>
					Created On : <input type="text" th:field="*{createdOn}"
						readonly="readonly" />
				</p>

				<br>
				<p>
					<input type="submit" value="Submit" /> <input type="reset"
						value="Reset" />

					<button>
						<a
							th:href="@{'/orderBookEdit/' + ${orderBuilder.orderBook.orderBookId}}">Back
							to Order Edit </a>
					</button>
					<button>
						<a th:href="@{'/admin/myorderbook'}">Order Book List </a>
					</button>
					</button>
						<button>
						<a th:href="@{'/getBookStats/'+ ${orderBuilder.orderId}}">OrderBook Statistics </a>
					</button>
				</p>
			</form>




		</div>



	</div>


</body>
</html>