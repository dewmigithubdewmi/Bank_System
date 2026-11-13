<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8"/>
  <title>Login</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <link rel="stylesheet" href="<c:url value='/css/base.css? v-0.2'/>"/>
  <link rel="stylesheet" href="<c:url value='/css/layout.css'/>"/>
  <link rel="stylesheet" href="<c:url value='/css/components/buttons.css'/>"/>
  <link rel="stylesheet" href="<c:url value='/css/components/forms.css'/>"/>
</head>
<body>

<header class="layout__header"><h1>Login</h1></header>

<main class="layout__main">
  <section style="max-width:450px; margin:auto">
    <c:if test="${not empty error}"><p class="error">${error}</p></c:if>

    <form method="post" action="<c:url value='/login'/>" class="form">
      <label>Email:
        <input type="email" name="email" required/>
      </label>
      <label>Password:
        <input type="password" name="password" required/>
      </label>
      <button type="submit" class="btn btn--primary">Login</button>
    </form>

    <a href="<c:url value='/register.jsp'/>" class="btn btn--link" style="margin-top:1rem">
      Create an Account
    </a>
  </section>
</main>

<footer class="layout__footer"><p>&copy; 2025 MyBankApp</p></footer>
</body>
</html>
