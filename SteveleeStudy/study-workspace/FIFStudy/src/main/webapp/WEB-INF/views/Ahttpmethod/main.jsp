<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>데이터 핸들링</title>
</head>
<body>
    <h1>데이터 핸들링</h1>
    <table>
        <thead>
            <tr>
                <td>아이디</td>
                <td>이름</td>
                <td>기능</td>
            </tr>
        </thead>
        <tbody>
        	<tr>
        	<form action="reqParam" method="post">
        		<td><input type="text" id="inputId" name="userId"/></td>
        		<td><input type="text" id="inputName" name="userName"/></td>
        		<td><button type="submit">Send</button></td>
        	</form>
        	</tr>
        </tbody>
    </table>
</body>
</html>