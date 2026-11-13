<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8"/>
  <title>Transfer Money</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <link rel="stylesheet" href="<c:url value='/css/base.css? v-0.2'/>"/>
  <link rel="stylesheet" href="<c:url value='/css/layout.css'/>"/>
  <link rel="stylesheet" href="<c:url value='/css/components/buttons.css'/>"/>
  <link rel="stylesheet" href="<c:url value='/css/components/forms.css'/>"/>
</head>
<body>

<header class="layout__header"><h1>Account Transfer</h1></header>

<main class="layout__main">
  <section style="max-width:600px; margin:auto;">
    <c:if test="${not empty error}"><p class="error">${error}</p></c:if>

    <form method="post" action="<c:url value='/transfer'/>" class="form">
      <label>Pay from:
        <select name="fromId" required>
          <c:forEach var="a" items="${accounts}">
            <option value="${a.id}">
              ${a.accountNumber} – Rs.&nbsp;${a.balance}
            </option>
          </c:forEach>
        </select>
      </label>

      <label>Pay to (account number):
        <input name="toNumber" required/>
      </label>

      <label>Amount:
        <input name="amount" type="number" step="0.01" min="0.01" required/>
      </label>

      <button type="submit" class="btn btn--primary">Transfer</button>
    </form>

    <a href="<c:url value='/home'/>" class="btn btn--link" style="margin-top:1.5rem">← Back to Dashboard</a>
  </section>
</main>

<footer class="layout__footer"><p>&copy; 2025 MyBankApp</p></footer>
</body>
</html>
