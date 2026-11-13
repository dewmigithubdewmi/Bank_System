<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8"/>
  <title>Bill Pay</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <link rel="stylesheet" href="<c:url value='/css/base.css? v-0.2'/>"/>
  <link rel="stylesheet" href="<c:url value='/css/layout.css? v-0.2'/>"/>
  <link rel="stylesheet" href="<c:url value='/css/components/buttons.css'/>"/>
  <link rel="stylesheet" href="<c:url value='/css/components/tables.css'/>"/>

  <script>function go(url){ window.location=url; }</script>
</head>
<body>

<header class="layout__header">
  <h1>Bill Payment</h1>
  <a href="<c:url value='/billpay/add'/>" class="btn btn--primary">➕ Add New Payee</a>
</header>

<main class="layout__main">
  <section>
    <h2>Your Payees</h2>

    <c:choose>
      <c:when test="${empty payees}">
        <p>No payees yet.</p>
      </c:when>
      <c:otherwise>
        <table class="table table--striped">
          <thead>
            <tr><th>Type</th><th>Identifier</th><th>Nickname</th><th>Actions</th></tr>
          </thead>
          <tbody>
            <c:forEach var="p" items="${payees}">
              <tr onclick="go('<c:url value="/billpay/pay?id=${p.id}"/>')">
                <td>${p.billType}</td>
                <td>${p.identifier}</td>
                <td>${p.nickName}</td>
                <td onclick="event.stopPropagation()">
                
                <a  href="${pageContext.request.contextPath}/billpay/edit?id=${p.id}"
                class="btn btn--secondary"
                 onclick="event.stopPropagation();">
                 Edit
                 </a>
                  <form method="post" action="<c:url value='/billpay/delete'/>"
                        style="display:inline"
                        onsubmit="event.stopPropagation();
                                  return confirm('Delete this payee?');">
                    <input type="hidden" name="payeeId" value="${p.id}"/>
                    <button type="submit" class="btn btn--danger">Delete</button>
                  </form>
                </td>
              </tr>
            </c:forEach>
          </tbody>
        </table>
      </c:otherwise>
    </c:choose>

    <a href="<c:url value='/home'/>" class="btn btn--link" style="margin-top:1.5rem">← Back to Dashboard</a>
  </section>
</main>

<footer class="layout__footer"><p>&copy; 2025 MyBankApp</p></footer>
</body>
</html>
