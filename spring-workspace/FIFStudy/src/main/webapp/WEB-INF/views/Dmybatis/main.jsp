<%@ page contentType="text/html; charset=UTF-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>login</title>
</head>
<script>
    function send () {
        console.log('send btn click...')
        var user = {
            id: document.getElementById('userId').value,
            pw: document.getElementById('userPw').value,
        }
        console.log(user)
        var strUser = JSON.stringify(user)
        console.log(strUser)

        var xhr = new XMLHttpRequest
        xhr.open('post', 'mbLoginAction')
        xhr.setRequestHeader('Content-Type', 'application/json')
        xhr.send(strUser)
    }
</script>
<body>
    <h1>login</h1>
    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>PW</th>
                <th>기능</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td><input type="text" id="userId"/></td>
                <td><input type="text" id="userPw"/></td>
                <td><button onclick="send()">send</button></td>
            </tr>
        </tbody>
    </table>
</body>
</html>