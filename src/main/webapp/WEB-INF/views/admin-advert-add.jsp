<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
  com.sliit.bankapp.model.Advertisement oldAd =
      (com.sliit.bankapp.model.Advertisement) request.getAttribute("oldAd");
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8"/>
  <title><c:out value="${oldAd == null ? 'Create Advertisement' : 'Edit Advertisement'}"/></title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>

  <!-- Shared theme -->
  <link rel="stylesheet" href="<c:url value='/css/base.css'/>"/>
  <link rel="stylesheet" href="<c:url value='/css/layout.css'/>"/>
  <link rel="stylesheet" href="<c:url value='/css/components/buttons.css'/>"/>
  <link rel="stylesheet" href="<c:url value='/css/components/forms.css? v-0.2'/>"/>
</head>
<body>

  <div id="flash" role="alert" aria-live="assertive" class="flash"></div>

  <header class="layout__header" role="banner">
    <h1>
      <c:out value="${oldAd == null ? 'Create Advertisement' : 'Edit Advertisement'}"/>
    </h1>
    <form action="${pageContext.request.contextPath}/logout"
          method="get"
          aria-label="Log out"
          onsubmit="document.getElementById('flash').textContent='You’ve been logged out.'">
      <button type="submit" class="btn btn--secondary" title="Log out">
        Logout
      </button>
    </form>
  </header>

  <main class="layout__main" role="main">
    <section style="max-width:700px; margin-inline:auto;">
      <!-- server-side error -->
      <c:if test="${not empty error}">
        <p style="background:#e63946;color:#fff;padding:.6rem 1rem;border-radius:.5rem;">
          <strong>Error:</strong> <c:out value="${error}"/>
        </p>
      </c:if>

      <form method="post"
            action="${pageContext.request.contextPath}${oldAd == null ? '/admin/advert-add' : '/admin/advert-edit'}"
            class="form">

        <c:if test="${oldAd != null}">
          <input type="hidden" name="adId" value="${oldAd.id}"/>
        </c:if>

        <label for="title">Title:</label>
        <input id="title"
               name="title"
               value="<c:out value='${oldAd != null ? oldAd.title : ""}'/>"
               required/>

        <label for="rules">Rules:</label>
        <textarea id="rules" name="rules" rows="4" required style = "background:rgba(255,255,255,.15)">
<c:out value='${oldAd != null ? oldAd.rules : ""}'/></textarea>

        <label for="description">Description:</label>
        <textarea id="description" name="description" rows="4" required style = "background:rgba(255,255,255,.15)" >
<c:out value='${oldAd != null ? oldAd.description : ""}'/></textarea>

        <div style="margin-top:1.5rem; display:flex; gap:.8rem;">
          <button type="submit" class="btn btn--primary">
            <c:out value="${oldAd == null ? 'Save Advertisement' : 'Save Changes'}"/>
          </button>
          <a href="${pageContext.request.contextPath}/admin/home" class="btn btn--secondary">
            Cancel
          </a>
        </div>

      </form>
    </section>
  </main>

  <footer class="layout__footer" role="contentinfo">
    <p>&copy; 2025 MyBankApp</p>
  </footer>

  <script>
    // focus‐visible helper
    document.body.addEventListener('keydown', ()=>document.body.classList.add('user-is-tabbing'));
    document.body.addEventListener('mousedown', ()=>document.body.classList.remove('user-is-tabbing'));
  </script>
</body>
</html>
