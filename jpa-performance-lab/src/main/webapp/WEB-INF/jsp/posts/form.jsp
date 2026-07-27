<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title><c:out value="${pageTitle}"/></title>

    <c:url var="boardCss" value="/css/board.css"/>
    <link rel="stylesheet" href="${boardCss}">
</head>
<body>
<main class="board-container form-container">
    <header class="board-header">
        <div>
            <h1><c:out value="${pageTitle}"/></h1>
            <p>제목, 작성자와 내용을 입력해주세요.</p>
        </div>
    </header>

    <c:url var="actionUrl" value="${formAction}"/>

    <form:form
            method="post"
            action="${actionUrl}"
            modelAttribute="postForm">

        <div class="form-field">
            <form:label path="title">제목</form:label>
            <form:input path="title" maxlength="200"/>
            <form:errors path="title" cssClass="field-error"/>
        </div>

        <div class="form-field">
            <form:label path="author">작성자</form:label>
            <form:input path="author" maxlength="50"/>
            <form:errors path="author" cssClass="field-error"/>
        </div>

        <div class="form-field">
            <form:label path="content">내용</form:label>
            <form:textarea path="content" rows="12"/>
            <form:errors path="content" cssClass="field-error"/>
        </div>

        <div class="form-actions">
            <c:url var="listUrl" value="/posts"/>

            <a class="button secondary" href="${listUrl}">취소</a>

            <button class="button primary" type="submit">
                <c:out value="${submitLabel}"/>
            </button>
        </div>
    </form:form>
</main>
</body>
</html>