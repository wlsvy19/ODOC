<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>fastcampus</title>
    <link rel="stylesheet" href="<c:url value='/css/menu.css'/>">
    <script src="https://code.jquery.com/jquery-1.11.3.js"></script>
</head>
<style>
    * {
        box-sizing: border-box;
        margin: 0;
        padding: 0;
        font-family: "Noto Sans KR", sans-serif;
    }
    a {
        text-decoration: none;
        color: black;
    }
    button,
    input {
        border: none;
        outline: none;
    }
    .board-container {
        width: 60%;
        height: 1200px;
        margin: 0 auto;
        /* border: 1px solid black; */
    }
    .search-container {
        background-color: rgb(253, 253, 250);
        width: 100%;
        height: 110px;
        border: 1px solid #ddd;
        margin-top : 10px;
        margin-bottom: 30px;
        display: flex;
        justify-content: center;
        align-items: center;
    }
    .search-form {
        height: 37px;
        display: flex;
    }
    .search-option {
        width: 100px;
        height: 100%;
        outline: none;
        margin-right: 5px;
        border: 1px solid #ccc;
        color: gray;
    }
    .search-option > option {
        text-align: center;
    }
    .search-input {
        color: gray;
        background-color: white;
        border: 1px solid #ccc;
        height: 100%;
        width: 300px;
        font-size: 15px;
        padding: 5px 7px;
    }
    .search-input::placeholder {
        color: gray;
    }
    .search-button {
        /* 메뉴바의 검색 버튼 아이콘  */
        width: 20%;
        height: 100%;
        background-color: rgb(22, 22, 22);
        color: rgb(209, 209, 209);
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 15px;
    }
    .search-button:hover {
        color: rgb(165, 165, 165);
    }
    table {
        border-collapse: collapse;
        width: 100%;
        border-top: 2px solid rgb(39, 39, 39);
    }
    tr:nth-child(even) {
        background-color: #f0f0f070;
    }
    th,
    td {
        width:300px;
        text-align: center;
        padding: 10px 12px;
        border-bottom: 1px solid #ddd;
    }
    td {
        color: rgb(53, 53, 53);
    }
    .no      { width:150px;}
    .title   { width:50%;  }
    td.title   { text-align: left;  }
    td.writer  { text-align: left;  }
    td.viewcnt { text-align: right; }
    td.title:hover {
        text-decoration: underline;
    }
    .paging {
        color: black;
        width: 100%;
        align-items: center;
    }
    .page {
        color: black;
        padding: 6px;
        margin-right: 10px;
    }
    .paging-active {
        background-color: rgb(216, 216, 216);
        border-radius: 5px;
        color: rgb(24, 24, 24);
    }
    .paging-container {
        width:100%;
        height: 70px;
        display: flex;
        margin-top: 50px;
        margin : auto;
    }
    .btn-write {
        background-color: rgb(236, 236, 236); /* Blue background */
        border: none; /* Remove borders */
        color: black; /* White text */
        padding: 6px 12px; /* Some padding */
        font-size: 16px; /* Set a font size */
        cursor: pointer; /* Mouse pointer on hover */
        border-radius: 5px;
        margin-left: 30px;
    }
    .btn-write:hover {
        text-decoration: underline;
    }
</style>
<body>
<div id="menu">
    <ul>
        <li id="logo">fastcampus</li>
        <li><a href="<c:url value='/'/>">Home</a></li>
        <li><a href="<c:url value='/board/list'/>">게시판</a></li>
        <li><a href="<c:url value='/login/login'/>">로그인</a></li>
        <li><a href="<c:url value='/register/add'/>">회원가입</a></li>
        <li><a href=""><i class="fas fa-search small"></i></a></li>
    </ul>
</div>
<div style="text-align:center">
    <button type="button" id="writeBtn" onclick="location.href='<c:url value="/board/write"/>'">글쓰기</button>
    <table border="1">
        <tr>
            <th>번호</th>
            <th>제목</th>
            <th>이름</th>
            <th>등록일</th>
            <th>조회수</th>
        </tr>
        <c:forEach var="boardDto" items="${list}">
            <tr>
                <td>${boardDto.bno}</td>
                <td class="title"><a
                        href="<c:url value="/board/read?bno=${boardDto.bno}&page=${page}&pageSize=${pageSize}"/>">${boardDto.title}</a>
                </td>
                <td>${boardDto.writer}</td>
                <td>${boardDto.reg_date}</td>
                <td>${boardDto.view_cnt}</td>
            </tr>
        </c:forEach>
    </table>
    <br/>
    <%--네비게이션 탭--%>
    <div>
        <c:if test="${ph.showPrev}">
            <a href="<c:url value="/board/list?page=${ph.beginPage - 1}&pageSize=${ph.pageSize}"/>">&lt;</a>
        </c:if>
        <c:forEach var="i" begin="${ph.beginPage}" end="${ph.endPage}">
            <a href="<c:url value='/board/list?page=${i}&pageSize=${ph.pageSize}'/>">${i}</a>
        </c:forEach>
        <c:if test="${ph.showNext}">
            <a href="<c:url value="/board/list?page=${ph.endPage + 1}&pageSize=${ph.pageSize}"/>">&gt;</a>
        </c:if>
    </div>
</div>
<script>
    <%-- get방식이라 model값이 param 으로 전달됨 --%>
    <%--let msg = "${param.msg}"--%>
    <%-- RedirectAttributes 사용--%>
    let msg = "${msg}";
    if (msg == "DEL_OK") {
        alert("성공적으로 삭제되었습니다.");
    }
    if (msg == "DEL_ERROR") {
        alert("삭제에 실패했습니다.");
    }
    if (msg == "WRITE_OK") {
        alert("성공적으로 등록 되었습니다.");
    }
</script>
</body>
</html>