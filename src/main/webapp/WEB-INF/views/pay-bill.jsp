<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8"/>
  <title>Pay Bill</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <link rel="stylesheet" href="<c:url value='/css/base.css? v-0.1'/>"/>
  <link rel="stylesheet" href="<c:url value='/css/layout.css? v-0.3'/>"/>
  <link rel="stylesheet" href="<c:url value='/css/components/buttons.css'/>"/>
  <link rel="stylesheet" href="<c:url value='/css/components/forms.css'/>"/>
</head>
<body>

<header class="layout__header"><h1>Pay – ${payee.nickName}</h1></header>

<main class="layout__main">
  <section style="max-width:600px">
    <p><b>Bill Type:</b> ${payee.billType}<br/>
       <b>Identifier:</b> ${payee.identifier}
       <c:if test="${payee.acctName!=null}"> (<i>${payee.acctName}</i>)</c:if>
    </p>

    <c:if test="${not empty error}">
      <p class="error">${error}</p>
    </c:if>

    <form method="post" action="<c:url value='/billpay/pay'/>" class="form">
      <input type="hidden" name="payeeId" value="${payee.id}"/>

      <label>Pay from:
        <select name="fromId" required>
          <c:forEach var="a" items="${accounts}">
            <option value="${a.id}">
              ${a.accountNumber} – Rs.&nbsp;${a.balance}
            </option>
          </c:forEach>
        </select>
      </label>

      <label>Amount:
        <input name="amount" type="number" step="0.01" min="0.01" required/>
      </label>

      <div style="margin-top:1rem;display:flex;gap:.8rem">
        <button type="submit" class="btn btn--primary">Pay</button>
        <a href="<c:url value='/billpay'/>" class="btn btn--secondary">Cancel</a>
      </div>
    </form>
  </section>
</main>

<footer class="layout__footer"><p>&copy; 2025 MyBankApp</p></footer>
</body>
</html>
