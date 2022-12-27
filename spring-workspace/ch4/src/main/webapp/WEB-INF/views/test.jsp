<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script src="https://code.jquery.com/jquery-1.11.3.js"></script>
</head>
<body>
<h2>댓글 테스트</h2>
comment: <input type="text" name="comment">
<button id="sendBtn" type="button">댓글등록</button>
<button id="modBtn" type="button">댓글수정</button>
<div id="commentList"></div>
<div id="replyForm" style="display: none">
    <input type="text" name="replyComment">
    <button id="wrtRepBtn" type="button">등록</button>
</div>
<script>
    let bno = 436; // 테스를 위해 게시물 번호 하드코딩

    <%-- DOM이 화면에 그러졌을 때--%>
    $(document).ready(function () {
        showList(bno); // 댓글 등록을 하고 비동기로 화면 댓글 리스트 호출

        // 댓글 쓰기 버튼 클릭시
        $("#sendBtn").click(function () {
            let comment = $("input[name=comment]").val();
            // 댓글 내용이 없으면
            if (comment.trim() == '') { // trim:공백 없애기
                alert("댓글을 입력해주세요.");
                $("input[name=comment]").focus();
                return;
            }
            $.ajax({
                type: 'POST',       // 요청 메서드
                url: '/ch4/comments?bno=' + bno,  // 요청 URI
                headers: {"content-type": "application/json"}, // 요청 헤더

                // 게시물 번호와 댓글내용을 JS객체로 만들어서 서버로 보냄
                data: JSON.stringify({bno: bno, comment: comment}),  // 서버로 전송할 데이터. stringify()로 직렬화 필요.
                success: function (result) {
                    alert(result);
                    $("input[name=comment]").val("");
                    showList(bno); // 댓글 등록을 하고 비동기로 화면 댓글 리스트 호출
                },
                error: function () {
                    alert("error")
                } // 에러가 발생했을 때, 호출될 함수
            }); // $.ajax()
        });

        // 댓글 수정 버튼 클릭시
        $("#modBtn").click(function () {
            let cno = $(this).attr("data-cno");
            let comment = $("input[name=comment]").val();
            // 댓글 내용이 없으면
            if (comment.trim() == '') { // trim:공백 없애기
                alert("댓글을 입력해주세요.");
                $("input[name=comment]").focus();
                return;
            }
            $.ajax({
                type: 'PATCH',       // 요청 메서드
                url: '/ch4/comments/' + cno,  // 요청 URI
                headers: {"content-type": "application/json"}, // 요청 헤더

                // 게시물 번호와 댓글내용을 JS객체로 만들어서 서버로 보냄
                data: JSON.stringify({cno: cno, comment: comment}),  // 서버로 전송할 데이터. stringify()로 직렬화 필요.
                success: function (result) {
                    alert(result);
                    $("input[name=comment]").val("");
                    showList(bno); // 댓글 등록을 하고 비동기로 화면 댓글 리스트 호출
                },
                error: function () {
                    alert("error")
                } // 에러가 발생했을 때, 호출될 함수
            }); // $.ajax()
        });

        // 대댓글 쓰기 버튼 클릭시
        $("#wrtRepBtn").click(function () {
            let comment = $("input[name=replyComment]").val();
            let pcno = $("#replyForm").parent().attr("data-pcno");

            // 댓글 내용이 없으면
            if (comment.trim() == '') {
                alert("댓글을 입력해주세요.");
                $("input[name=replyComment]").focus();
                return;
            }
            $.ajax({
                type: 'POST',       // 요청 메서드
                url: '/ch4/comments?bno=' + bno,  // 요청 URI
                headers: {"content-type": "application/json"}, // 요청 헤더

                // 게시물 번호와 댓글내용을 JS객체로 만들어서 서버로 보냄
                data: JSON.stringify({pcno: pcno, bno: bno, comment: comment}),  // 서버로 전송할 데이터. stringify()로 직렬화 필요.
                success: function (result) {
                    alert(result);
                    $("input[name=comment]").val("");
                    showList(bno); // 댓글 등록을 하고 비동기로 화면 댓글 리스트 호출
                },
                error: function () {
                    alert("error")
                } // 에러가 발생했을 때, 호출될 함수
            }); // $.ajax()
            $("#replyForm").css("display", "none");
            $("input[name=replyComment]").val("");
            $("#replyForm").appendTo("body");
        });
    });

    // 댓글 리스트 보여주기
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

    // div 태그에 댓글을 달아야하는데 안달려서 HTML태그로 만들어서 달기
    let toHTML = function (comments) {
        let tmp = "<ul>";

        comments.forEach(function (comment) {
            tmp += '<li data-cno=' + comment.cno
            tmp += ' data-pcno=' + comment.pcno
            tmp += ' data-bno=' + comment.bno + '>'
            if (comment.cno != comment.pcno) {
                tmp += "ㄴ"
            }
            tmp += ' commenter=<span class="commenter">' + comment.commenter + '</span>'
            tmp += ' comment=<span class="comment">' + comment.comment + '</span>'
            tmp += ' up-date=' + comment.up_date

            // 각 댓글마다 삭제버튼이므로 class 사용
            tmp += '<button class="delBtn">삭제</button>'
            tmp += '<button class="modBtn">수정</button>'
            tmp += '<button class="replyBtn">답글</button>'
            tmp += '</li>'
        })
        return tmp + "</ul>";
    }

    // 댓글 삭제 버튼을 클릭 했을 때
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

    // 개개인의 댓글수정 버튼을 클릭 했을 때
    $("#commentList").on("click", ".modBtn", function () {
        let cno = $(this).parent().attr("data-cno");

        // 선택한 li 태그의 class=comment 값 가져오기
        // $(this) <- 개개인 수정 버튼
        // 개개인 수정 버튼의 parent(부모) 는  li 딱 하나
        let comment = $("span.comment", $(this).parent()).text();

        // 1. comment의 내용을 input에 뿌려주기
        $("input[name=comment]").val(comment);
        // 2. cno 전달하기: 수정 버튼 속성에 data-cno로 cno값 저장
        $("#modBtn").attr("data-cno", cno);
    });

    // 댓글의 답글 버튼 클릭시
    $("#commentList").on("click", ".replyBtn", function () {
        // 1. replyForm 옮기기 - li태그에 붙이기
        $("#replyForm").appendTo($(this).parent());
        // 2. 답글 입력할 폼 보여주기
        $("#replyForm").css("display", "block");
    });
</script>
</body>
</html>