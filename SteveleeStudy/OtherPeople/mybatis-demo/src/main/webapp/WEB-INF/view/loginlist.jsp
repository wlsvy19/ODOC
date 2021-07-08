<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<h1>LoginList</h1>

<c:choose>
    <c:when test="${!empty sessionScope.sessionedUser}">
        <%--    세션이 없을 경우 처리    --%>
    </c:when>
</c:choose>

<table border="1">
<c:forEach items="${userList}" var="list">
        <tr>
            <td>
                ${list.id};
            </td>
            <td>
                ${list.pw};
            </td>
            <td>
                ${list.sessionid};
            </td>
            <td>
                ${list.logindate};
            </td>
        </tr>
</c:forEach>
</table>
</body>
</html>