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
<title>Insert title here</title>
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

			<form action="#" th:action="@{/addExecution}"
				th:object="${execution}" method="post">
				
				<p> Book Id : <input type="text" th:field="*{orderBook.orderBookId}" readonly="readonly"/>
				<p> Order Book Status :  <input type="text" th:field="${orderBook.orderBookStatus}" readonly="readonly"/></p>
				<p> Order Book Instrument ID: <input type="text" th:field="${orderBook.instrument.instrumentName}" readonly="readonly"/></p>
				<p> Order Book Execution Status : <input type="text" th:field="${orderBook.executionStatus}" readonly="readonly" />
				<p> Order Book Created By  : <input type="text" th:field="${orderBook.createdBy}" readonly="readonly"/>
				<p> Order Book Created on : <input type="text" th:field="${orderBook.createdOn}" readonly="readonly"/>
				
				<h3>Enter Execution Details :</h3>
				
				<p> Execution Id : <input type="text" th:field="*{executionId}" readonly="readonly"/>
				<p> Execution Name : <input type="text" th:field="*{executionName}" th:readonly="${execution.executionName!=''}"/>
				<p> Price : <input type="text" th:field="*{price}" th:readonly="${execution.executionName!=''}"/>
				<p> Quantity : <input type="text" th:field="*{quantity}" />
				<p> Created By  : <input type="text" th:field="*{createdBy}" readOnly="readonly"/>
				<p> Created on : <input type="text" th:field="*{createdOn}" readOnly="readonly"/>
				<br>
				<br>
				<input type="submit" value="Submit" /> 
				<input type="reset" value="Reset" />
				<button><a th:href="@{'/admin/myorderbook'}">OrderBook List </a></button>
				<button><a th:href="@{'/orderBookEdit/'+ ${orderBook.orderBookId} }">Back To Order Book</a></button>
				<button><a th:href="@{'/admin/myorderbook'}">OrderBook List </a></button>
			</form>



		</div>
	</div>
</body>
</html>