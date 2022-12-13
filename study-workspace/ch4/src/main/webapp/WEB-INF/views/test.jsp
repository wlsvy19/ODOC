<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script src="https://code.jquery.com/jquery-1.11.3.js"></script>
</head>
<body>
<h2>댓글 테스트</h2>
<button id="sendBtn" type="button">SEND</button>
<div id="commentList"></div>
<script>
    let bno = 436;
    let showList = function (bno) {
        $.ajax({
            type: 'GET',       // 요청 메서드
            url: '/ch4/comments?bno=' + bno,  // 요청 URI
            success: function (result) {
                $("#commentList").html(toHTML(result));    // 서버로부터 응답이 도착하면 호출될 함수
            },
            error: function () {
                alert("error")
            } // 에러가 발생했을 때, 호출될 함수
        }); // $.ajax()
    } // end showList()

    $(document).ready(function () {
        $("#sendBtn").click(function () {
            showList(bno); // 비동기 호출
        });
    });

    //$(".delBtn").click(function () { // 비동기 호출 후 바로 실행하는데 이때 삭제버튼이 로딩 안돼있어서 바꿔야함
    $("#commentList").on("click", ".delBtn", function () { // 이시점에 commentList 태그는 존재함
        let cno = $(this).parent().attr("data-cno");
        let bno = $(this).parent().attr("data-bno");

        $.ajax({
            type: 'DELETE',       // 요청 메서드
            url: '/ch4/comments/' + cno + '?bno=' + bno,  // 요청 URI
            success: function (result) {
                alert(result);
                showList(bno); // 삭제후 목록 다시 보여줘야함
            },
            error: function () {
                alert("error")
            } // 에러가 발생했을 때, 호출될 함수
        });
    });


    let toHTML = function (comments) {
        let tmp = "<ul>";

        comments.forEach(function (comment) {
            tmp += '<li data-cno=' + comment.cno
            tmp += ' data-pcno=' + comment.pcno
            tmp += ' data-bno=' + comment.bno + '>'
            tmp += ' commenter=<span class="commenter">' + comment.commenter + '</span>'
            tmp += ' comment=<span class="comment">' + comment.comment + '</span>'
            tmp += ' up-date=' + comment.up_date

            // 각 댓글마다 삭제버튼이므로 class 사용
            tmp += '<button class="delBtn">삭제</button>'
            tmp += '</li>'
        })
        return tmp + "</ul>";
    }
</script>
</body>
</html>