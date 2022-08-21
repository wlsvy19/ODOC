<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>로그인</title>
</head>
<body>
    <table width="500" cellpadding="0" cellspacing="0" border="1">
        <thead>
            <tr>
                <th>아이디</th>
                <th>비밀번호</th>
				<th>기능1</th>
				<th>기능2</th>
            </tr>
        </thead>
        <tbody>
                <tr>
                    <td><input type="text" id="inputId"/></td>
                    <td><input type="text" id="inputPw"/></td>
					<td><button>로그인</button></td>
					<td><button>사용자 목록 보기</button></td>
                </tr>
        </tbody>
    </table>
</body>
</html>