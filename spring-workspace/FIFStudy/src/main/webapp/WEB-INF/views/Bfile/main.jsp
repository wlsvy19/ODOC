<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>파일 다루기</title>
</head>
<script>
    function savePerson () {
        console.log('***saveUser()***')

        var person = {
            id: document.getElementById('formId').value,
            name: document.getElementById('formName').value,
            age: document.getElementById('formAge').value
        }
        console.log(person)
        var strPerson = JSON.stringify(person)
        console.log(strPerson)

        var xhr = new XMLHttpRequest
        xhr.open('post', 'savePerson')
        xhr.setRequestHeader('Content-Type', 'application/json')
        xhr.send(strPerson)
    } // saveUser()

</script>
<body>
    <table border="1">
        <thead style="text-align: center;">
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
                <td><input id="formId"/></td>
                <td><input id="formName"/></td>
                <td><input id="formAge"/></td>
                <td><button onclick="savePerson()">사용자 저장</button></td>
                <td><button><a href="/app/showPersonList">사용자 리스트 보기</a></button></td>
            </tr>
        </tbody>
    </table>
</body>
</html>