<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>데이터 핸들링</title>
</head>

<script>
function sendData () {
    console.log('send!!!!')
    
    
    
    var formId = document.getElementById('id').value
    var formName = document.getElementById('name').value
    var formAge = document.getElementById('age').value

    var person = {
        id : formId,
        name: formName,
        age: formAge
    }
    console.log('그냥 person', person)
    
    var xhr = new XMLHttpRequest
    xhr.open('post', 'dtoObject')

	var strPerson = JSON.stringify(person)
	console.log('String화한 Person',strPerson)

    xhr.setRequestHeader("Content-Type", "application/json")
    xhr.send(strPerson)
	


}
    
</script>

<body>
    <h1>데이터 핸들링</h1>
    <table border="1">
        <thead>
            <tr>
                <td>아이디</td>
                <td>이름</td>
                <td>나이</td>
                <td>기능1</td>
                <td>기능2</td>
            </tr>
        </thead>
        <tbody>
        	<tr>
        	<form action="reqParam" method="post">
        		<td><input type="text" id="id" name="id"/></td>
        		<td><input type="text" id="name" name="name"/></td>
                <td><input type="text" id="age" name="age"/></td>
        		<td><button type="submit">Submit Btn</button></td>
        	</form>
        	<td><button onclick="sendData()">AJAX Btn</button></td>
        	</tr>
        </tbody>
    </table>
</body>
</html>