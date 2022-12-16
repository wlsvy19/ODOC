<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page import="java.net.URLDecoder" %>
<c:set var="loginId" value="${sessionScope.id}"/>
<c:set var="loginOutLink" value="${loginId=='' ? '/login/login' : '/login/logout'}"/>
<c:set var="loginOut" value="${loginId=='' ? 'Login' : 'ID='+=loginId}"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.8.2/css/all.min.css"/>
    <link rel="stylesheet" href="<c:url value='/css/menu.css'/>">
    <style>
        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
            font-family: "Noto Sans KR", sans-serif;
        }

        .title {
            font-size: 50px;
            margin: 40px 0 30px 0;
        }

        .msg {
            height: 30px;
            text-align: center;
            font-size: 16px;
            color: red;
            margin-bottom: 20px;
        }


    </style>
    <title>가입정보</title>
</head>
<body>
<div id="menu">
    <ul>
        <li id="logo">fastcampus</li>
        <li><a href="<c:url value='/'/>">Home</a></li>
        <li><a href="<c:url value='/board/list'/>">Board</a></li>
        <li><a href="<c:url value='${loginOutLink}'/>">${loginOut}</a></li>
        <li><a href="<c:url value='/register/add'/>">회원가입</a></li>
        <li><a href=""><i class="fa fa-search"></i></a></li>
    </ul>
</div>

<div>
    <h1>id=${user.id}</h1>
    <h1>pwd=${user.pwd}</h1>
    <h1>name=${user.name}</h1>
    <h1>email=${user.email}</h1>
    <h1>birth=${user.birth}</h1>
    <h1>sns=${user.sns}</h1>
</div>
</body>
</html>