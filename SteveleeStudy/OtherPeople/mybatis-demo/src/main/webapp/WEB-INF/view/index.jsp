<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <style>
        .flex-container{
            width: 100%;
            height: 100vh;
            display: -webkit-box;
            display: -moz-box;
            display: -ms-flexbox;
            display: flex;

            -webkit-box-align: center;
            -moz-box-align: center;
            -ms-flex-align: center;
            align-items: center; /* 수직 정렬 */

            -webkit-box-pack: center;
            -moz-box-pack: center;
            -ms-flex-pack: center;
            justify-content: center; /* 수평 정렬 */
        }

        .hello{
            background-color: aquamarine;
        }
    </style>
</head>
<body>
<form action="/loginAction" method="post" id="login">
    <table align="center" class="flex-container">
        <tr>
            <td colspan="2">
                <h2 align="center" class="hello">ID와 PW를 입력해주세요.</h2>
            </td>
        </tr>

        <tr>
            <td>
                ID
            </td>
            <td>
                <input type="text" id="id" name="id"/>
            </td>
        </tr>
        <tr>
            <td>
                PW
            </td>
            <td>
                <input type="password" style="-ms-ime-mode: inactive" id="pw" name="pw"/>
            </td>
        </tr>
        <tr>
            <td>
                <input type="submit" value="로그인"/>
            </td>
            <td>
                <button type="button" onclick="location.href='join'">회원가입</button>
            </td>
        </tr>
    </table>
</form>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>

<script>
    $('#login').submit(function(){

        if ($('#id').val() == '' && $('#pw').val() == '') {
            alert("아이디와 비밀번호를 입력해주세요.");
            $('#id').focus();
            return false;
        }

        if ($('#id').val() == '' || $('#pw').val() == '') {
            alert("아이디 혹은 패스워드를 입력해주세요");
            $('#id').focus();
            return false;
        }
    });
</script>


</body>
</html>