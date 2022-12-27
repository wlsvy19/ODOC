<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Person List</title>
</head>
<body>
<h1>사용자 리스트</h1>

<table border="1">
    <thead>
        <tr>
            <td>번호</td>
            <td>아이디</td>
            <td>이름</td>
            <td>나이</td>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="person" items="${personList}" varStatus="status">
            <tr>
                <td>${status.index}</td>
                <td>${person.getId()}</td>
                <td>${person.getName()}</td>
                <td>${person.getAge()}</td>
            </tr>
        </c:forEach>
    </tbody>
</table>

</body>
</html>