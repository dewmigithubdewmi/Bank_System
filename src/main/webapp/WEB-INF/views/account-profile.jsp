<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8"/>
  <title>Account Profile</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <link rel="stylesheet" href="<c:url value='/css/base.css? v-0.2'/>"/>
  <link rel="stylesheet" href="<c:url value='/css/layout.css? v-0.2'/>"/>
  <link rel="stylesheet" href="<c:url value='/css/components/buttons.css'/>"/>
  <link rel="stylesheet" href="<c:url value='/css/components/forms.css? v-0.2'/>"/>
</head>
<body>

<div id="flash" role="alert" aria-live="assertive" class="flash"></div>

<header class="layout__header"><h1>Account Profile</h1></header>

<main class="layout__main">
  <section>
    <form method="post" action="<c:url value='/account/edit'/>" class="form">
      <input type="hidden" name="accountId" value="${account.id}"/>

      <label>Full&nbsp;Name:</label>
      <p>${account.fullName}</p>

      <label>NIC:</label>
      <input name="nic" value="${account.nic}" required/>

      <label>Address:</label>
      <input name="address" value="${account.address}" required/>

      <label>Mobile:</label>
      <input name="mobile" value="${account.mobile}" required/>

      <label>Account&nbsp;Number:</label>
      <p>${account.accountNumber}</p>

      <label>Balance&nbsp;(Rs.):</label>
      <input name="balance" type="number" step="0.01" value="${account.balance}" required/>

      <div style="margin-top:1.5rem;display:flex;gap:.8rem">
        <button type="submit" class="btn btn--primary">💾 Save Changes</button>
        <a href="<c:url value='/home'/>" class="btn btn--secondary">Cancel</a>
      </div>
    </form>

    <!-- Delete -->
    <form method="post"
          action="<c:url value='/account/delete'/>"
          onsubmit="return confirm('Delete this account?');"
          style="margin-top:1rem">
      <input type="hidden" name="accountId" value="${account.id}"/>
      <button type="submit" class="btn btn--danger">🗑️ Delete Account</button>
    </form>
  </section>
</main>

<footer class="layout__footer"><p>&copy; 2025 MyBankApp</p></footer>
</body>
</html>
