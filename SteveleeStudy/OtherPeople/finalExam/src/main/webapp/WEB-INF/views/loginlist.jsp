<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>login list</title>
</head>
<body>
	<h1>login list</h1>

	${histories }
	
	<table border="1">
		<c:forEach items="${histories }" var="history" varStatus="num">
			<tr>
				<td><c:out value="${num.index }"></c:out></td>
				<td><c:out value="${history.getid() }"></c:out></td>
				<td><c:out value="${history.getid() }"></c:out></td>
				<td><c:out value="${history.getSessionid() }"></c:out></td>
				<td><c:out value="${history.getLoginDate() }"></c:out></td>
			<tr>
		</c:forEach>
	</table>
	
	<div>
		<input id="search" type="text" />
		<button onclick="find()">검색</button>
	</div>

	<div class="historyList">

	</div>

	<script>
		function find() {

			const data = {
				inputData: document.querySelector("#search").value
			}

			const ajax = new XMLHttpRequest();
			
			ajax.open("post", "/loginhistoryfield");
			ajax.setRequestHeader("Content-Type", "application/json");
			ajax.send(JSON.stringify(data));

			ajax.onload = () => {
				ajax.response;
			}
		}
	</script>
</body>
</html>