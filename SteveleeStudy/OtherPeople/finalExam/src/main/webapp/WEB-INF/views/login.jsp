<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>login</title>
</head>
<body>
<h1>login</h1>

<form action="/loginaction" method="post">
	id :: <input type="text" name="id" />
	pw :: <input type="password" name="pw"/>
	<input type="submit" value="로그인">
</form>

</body>
</html>