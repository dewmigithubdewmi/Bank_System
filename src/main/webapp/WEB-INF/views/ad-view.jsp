<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8"/>
  <title><c:out value="${ad.title}"/> – Advertisement</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>

  <!-- shared theme -->
  <link rel="stylesheet" href="<c:url value='/css/base.css?v=0.3'/>"/>
  <link rel="stylesheet" href="<c:url value='/css/layout.css? v-0.2'/>"/>
  <link rel="stylesheet" href="<c:url value='/css/components/buttons.css'/>"/>

  <!-- new comment styling -->
  <link rel="stylesheet" href="<c:url value='/css/components/comments.css'/>"/>
</head>
<body>

<header class="layout__header"><h1><c:out value="${ad.title}"/></h1></header>

<main class="layout__main">
  <section>

    <!-- ── rules / description ─────────────────────────────── -->
    <h2>Rules</h2>
    <pre class="block"><c:out value="${ad.rules}"/></pre>

    <h2>Description</h2>
    <pre class="block"><c:out value="${ad.description}"/></pre>

    <!-- ── comments ────────────────────────────────────────── -->
    <h2 style="margin-top:2rem">Comments</h2>

    <!-- add-comment (always first bubble) -->
    <form method="post" action="<c:url value='/comment/add'/>" class="comment-form">
      <input type="hidden" name="adId" value="${ad.id}"/>
      <textarea name="content" rows="3" placeholder="Leave a comment…" required></textarea>
      <div class="comment-form__actions">
        <button type="submit" class="btn btn--primary">Post</button>
      </div>
    </form>

    <!-- thread -->
    <ul class="comment-thread">
      <c:forEach var="c" items="${comments}">
        <li class="comment-bubble">
          <div class="comment-avatar"
               aria-hidden="true">${c.author.substring(0,1)}</div>

          <div class="comment-body">
            <header>
              <b>${c.author}</b>
              <time datetime="${c.createdAt}">${c.createdAt}</time>
            </header>

            <p><c:out value="${c.content}"/></p>

            <!-- owner actions -->
            <c:if test="${currentUser.id==c.userId}">
              <div class="comment-actions">
                <a href="<c:url value='/comment/edit?id=${c.id}'/>"
                   class="btn btn--secondary btn--xs">Edit</a>
                <form method="post" action="<c:url value='/comment/delete'/>"
                      onsubmit="return confirm('Delete this comment?');" style="display:inline">
                  <input type="hidden" name="id" value="${c.id}"/>
                  <input type="hidden" name="adId" value="${ad.id}"/>
                  <button type="submit" class="btn btn--danger btn--xs">Delete</button>
                </form>
              </div>
            </c:if>
          </div>
        </li>
      </c:forEach>
    </ul>

    <a href="<c:url value='/home'/>" class="btn btn--link" style="margin-top:2rem">
      ← Back to Dashboard
    </a>
  </section>
</main>

<footer class="layout__footer"><p>&copy; 2025 MyBankApp</p></footer>
</body>
</html>
