<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script>

function login(){
	
	var ajax = new XMLHttpRequest();
	ajax.open('post','loginaction');
	ajax.setRequestHeader("Content-Type", "application/json");// 이기ㅓ 안쓰면 415 에러 남
	var data = { id : document.getElementById('id').value , pw : document.getElementById('pw').value}
	ajax.send(JSON.stringify(data));
	
}



</script>
</head>
<body>
<h1>테스트페이지</h1>
<form>
아이디 : <input type="text" name="id" id="id">
비밀번호 : <input type="text" name="pw" id="pw">
<input type="button" onclick="login()" value="전송"> 
</form>
<form> <!-- 
<form method="post" action="/loginactionpost">  
아이디 :: <input type="text" id="id" name="id" >
비밀번호 :: <input type="text" id="pw" name="pw">
<input type="submit" value="post전송">  -->
</form>
</body>
</html>