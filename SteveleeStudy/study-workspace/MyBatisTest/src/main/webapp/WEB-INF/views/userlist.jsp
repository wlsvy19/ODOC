<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>LgoinList</title>
</head>
<body>
<h1>MyBatis 연결 테스트 - steve db</h1>
    <table width="300" cellpadding="0" cellspacing="0" border="1">
        <thead>
            <tr>
                <th>아이디</th>
                <th>비밀번호</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="DTO" items="${list}">
                <tr>
                    <td>${DTO.id}</td>
                    <td>${DTO.pw}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>