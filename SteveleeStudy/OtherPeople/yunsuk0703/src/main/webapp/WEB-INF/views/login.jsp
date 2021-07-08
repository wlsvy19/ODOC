<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page session="false" %>
<html>
<head>
	<title>Login</title>
</head>
<style>
	input{
		width: 100px;
	}

</style>
<script type="text/javascript">

</script>
<body>
<form action="/login" method="POST" onsubmit="login()">
	<div>
		<label>ID : </label>
		<input type="text" name="id">
	</div>
	<div>
		<label>PW : </label>
		<input type="password" name="pw">
	</div>
	<input type="button" onsubmit="login()" value="로그인">
</form>
</body>
</html>
