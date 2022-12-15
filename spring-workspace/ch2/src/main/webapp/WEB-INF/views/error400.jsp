<%@ page contentType="text/html;charset=utf-8" isErrorPage="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
	<title>error400.jsp</title>
</head>
<body>
<h1>예외가 발생했습니다.</h1>
발생한 예외(컨트롤러에서 예외 전달) : ${ex}<br>
예외 메시지 : ${ex.message}<br>
<br>
발생한 예외(jsp 기본객체 사용) : ${pageContext.exception}<br>
예외 메시지 : ${pageContext.exception.message}<br>

<ol>
<c:forEach items="${ex.stackTrace}" var="i">
	<li>${i.toString()}</li>
</c:forEach>
</ol>
</body>
</html>

