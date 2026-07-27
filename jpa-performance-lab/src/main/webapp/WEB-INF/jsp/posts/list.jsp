<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>게시판</title>

    <c:url var="boardCss" value="/css/board.css"/>
    <link rel="stylesheet" href="${boardCss}">
</head>
<body>
<main class="board-container">
    <header class="board-header">
        <div>
            <h1>게시판</h1>
            <p>JPA로 만드는 첫 번째 게시판</p>
        </div>

        <c:url var="newPostUrl" value="/posts/new"/>
        <a class="button primary" href="${newPostUrl}">글쓰기</a>
    </header>

    <table class="post-table">
        <thead>
        <tr>
            <th>번호</th>
            <th>제목</th>
            <th>작성자</th>
            <th>작성일</th>
        </tr>
        </thead>

        <tbody>
        <c:forEach var="post" items="${posts}">
            <c:url var="detailUrl" value="/posts/${post.id}"/>

            <tr>
                <td><c:out value="${post.id}"/></td>
                <td>
                    <a href="${detailUrl}">
                        <c:out value="${post.title}"/>
                    </a>
                </td>
                <td><c:out value="${post.author}"/></td>
                <td><c:out value="${post.createdAt}"/></td>
            </tr>
        </c:forEach>

        <c:if test="${empty posts}">
            <tr>
                <td class="empty" colspan="4">
                    등록된 게시글이 없습니다.
                </td>
            </tr>
        </c:if>
        </tbody>
    </table>
</main>
</body>
</html>