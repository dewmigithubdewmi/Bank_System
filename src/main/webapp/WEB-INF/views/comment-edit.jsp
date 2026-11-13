<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8"/>
  <title>Edit Comment</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <link rel="stylesheet" href="<c:url value='/css/base.css? v-0.2'/>"/>
  <link rel="stylesheet" href="<c:url value='/css/layout.css? v-0.5'/>"/>
  <link rel="stylesheet" href="<c:url value='/css/components/buttons.css'/>"/>
  <!-- new comment styling -->
  <link rel="stylesheet" href="<c:url value='/css/components/comments.css'/>"/>
</head>
<body>

<header class="layout__header"><h1>Edit Comment</h1></header>

<main class="layout__main">
  <section>
    <form method="post" action="<c:url value='/comment/edit'/>" class="comment-form">
      <input type="hidden" name="id" value="${comment.id}"/>
      <input type="hidden" name="adId" value="${comment.adId}"/>

      <textarea name="content" rows="4" required>${comment.content}</textarea>

      <div style="margin-top:1rem;display:flex;gap:.8rem">
        <button type="submit" class="btn btn--primary">Save</button>
        <a href="<c:url value='/ad?id=${comment.adId}'/>" class="btn btn--secondary">Cancel</a>
      </div>
    </form>
  </section>
</main>

<footer class="layout__footer"><p>&copy; 2025 MyBankApp</p></footer>
</body>
</html>
