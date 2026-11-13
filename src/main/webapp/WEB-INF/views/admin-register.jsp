<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
  com.sliit.bankapp.model.User old =
      (com.sliit.bankapp.model.User) request.getAttribute("oldAdmin");
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8"/>
  <title>Admin Registration</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <link rel="stylesheet" href="<c:url value='/css/base.css? v-0.2'/>"/>
  <link rel="stylesheet" href="<c:url value='/css/layout.css? v-0.2'/>"/>
  <link rel="stylesheet" href="<c:url value='/css/components/buttons.css'/>"/>
  <link rel="stylesheet" href="<c:url value='/css/components/forms.css? v-0.2'/>"/>
</head>
<body>

<header class="layout__header"><h1>Create Admin Account</h1></header>

<main class="layout__main">
  <section>
    <c:if test="${not empty error}">
      <p class="error">${error}</p>
    </c:if>

    <form method="post" action="<c:url value='/admin-register'/>" class="form" style="max-width:600px">
      <label>First Name:
        <input name="firstName" value="<c:out value='${old!=null?old.firstName:""}'/>" required/>
      </label>

      <label>Last Name:
        <input name="lastName" value="<c:out value='${old!=null?old.lastName:""}'/>" required/>
      </label>

      <label>Email:
        <input type="email" name="email" value="<c:out value='${old!=null?old.email:""}'/>" required/>
      </label>

      <label>Password:
        <input type="password" name="password" required/>
      </label>

      <div style="margin-top:1rem;display:flex;gap:.8rem">
        <button type="submit" class="btn btn--primary">Register</button>
        <a href="<c:url value='/login.jsp'/>" class="btn btn--secondary">Cancel</a>
      </div>
    </form>
  </section>
</main>

<footer class="layout__footer"><p>&copy; 2025 MyBankApp</p></footer>
</body>
</html>
