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
						<li><a href="/admin/myOrders">Order Inventory</a></li>
						<li><a href="/admin/AboutMe">About Me</a></li>
					</ul>
				</div>

			</div>
			<br><br>
			<p class="admin-message-text text-center" th:utext="${adminMessage}"></p>

			<form action="#" th:action="@{/saveOrder}" th:object="${order}"
				method="post">
				<p>
					Order Id : <input type="text" th:field="*{orderId}"   readonly="readonly"/>
				</p>
				<p>
					Order Name : <input type="text" th:field="*{orderName}" />
				</p>
				
				

				<p> Order Instrument ID: 
									<select th:field="*{instrument.refInstrumentId}">
					                         <option th:each="entry : ${instrumentMap}"
					                                 th:value="${entry.key}"
					                                 th:utext="${entry.value.instrumentName}"
					                                 th:selected="*{instrument.refInstrumentId}">
					                         </option>
					                  </select>

				<p>
					Quantity : <input type="text" th:field="*{orderQuantity}" />
				</p>
				<p>
					Total Price Of Order : <input type="text" th:field="*{orderprice}" />
				</p>
				<p>
					Order Type : 
					<select th:field="*{orderType.orderTypeId}">
					                         <option th:each="entry : ${orderTypeMap}"
					                                 th:value="${entry.key}"
					                                 th:utext="${entry.value.orderType}"
					                                 th:selected="*{orderType.orderTypeId}">
					                         </option>
					                  </select>
				</p>
				
				<p>
					Execution Quantity : <input type="text" th:field="*{executionQuantity}"  readonly="readonly"/>
				</p>

				<p>
					Created By : <input type="text" th:field="*{createdBy}"  readonly="readonly"/>
				</p>
				<p>
					Created On : <input type="text" th:field="*{createdOn}"  readonly="readonly"/>
				</p>

				<br>
				<p>
					<input type="submit" value="Submit" /> <input type="reset"
						value="Reset" />
						
						<button><a th:href="@{'/admin/myOrders'}">Orders List </a></button>
				</p>
			</form>
			



				</div>



	</div>


</body>
</html>