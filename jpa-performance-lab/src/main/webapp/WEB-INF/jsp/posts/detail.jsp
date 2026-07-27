<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>게시글 상세</title>

    <c:url var="boardCss" value="/css/board.css"/>
    <link rel="stylesheet" href="${boardCss}">
</head>
<body>
<main class="board-container">
    <header class="board-header">
        <div>
            <h1>게시글 상세</h1>
            <p>선택한 게시글의 전체 내용</p>
        </div>
    </header>

    <article class="post-detail">
        <h2><c:out value="${post.title}"/></h2>

        <div class="post-meta">
            <span>작성자 <c:out value="${post.author}"/></span>
            <span>작성 <c:out value="${post.createdAt}"/></span>
            <span>수정 <c:out value="${post.updatedAt}"/></span>
        </div>

        <div class="post-content">
            <c:out value="${post.content}"/>
        </div>
    </article>

    <div class="actions">
        <c:url var="listUrl" value="/posts"/>
        <a class="button secondary" href="${listUrl}">목록</a>
    </div>
</main>
</body>
</html>