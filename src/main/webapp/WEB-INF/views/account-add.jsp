<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8"/>
  <title>Add Existing Account</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>

  <!-- theme -->
  <link rel="stylesheet" href="<c:url value='/css/base.css?v=0.2'/>"/>
  <link rel="stylesheet" href="<c:url value='/css/layout.css?v=0.2'/>"/>
  <link rel="stylesheet" href="<c:url value='/css/components/buttons.css'/>"/>
  <link rel="stylesheet" href="<c:url value='/css/components/forms.css?v=0.5'/>"/>
</head>
<body>

<!-- flash banner (logout, …) -->
<div id="flash" role="alert" aria-live="assertive" class="flash"></div>

<!-- ── Header ────────────────────────────────────────────── -->
<header class="layout__header">
  <h1>Add Existing Account</h1>
  <form action="<c:url value='/logout'/>"   /* GET – kills cookie in AuthFilter */
        method="get"
        onsubmit="document.getElementById('flash').textContent='You’ve been logged out.'">
    <button type="submit" class="btn btn--secondary">Logout</button>
  </form>
</header>

<!-- ── Navigation (collapses < 600 px) ───────────────────── -->
<nav class="layout__nav" aria-label="Primary">
  <button class="nav__toggle btn btn--icon"
          aria-expanded="false" aria-controls="nav-list"
          onclick="
            const n=document.getElementById('nav-list');
            const x=this.getAttribute('aria-expanded')==='true';
            this.setAttribute('aria-expanded', !x); n.hidden=x;">
  </button>

  <ul id="nav-list" class="nav__list" hidden>
    <li><a class="btn btn--primary"   href="<c:url value='/home'/>">🏠 Dashboard</a></li>
    <li><a class="btn btn--primary"   href="<c:url value='/transfer'/>">💸 Transfer</a></li>
    <li><a class="btn btn--primary"   href="<c:url value='/billpay'/>">🧾 Bill Pay</a></li>
    <li><a class="btn btn--secondary" href="<c:url value='/account/add'/>">➕ Add Account</a></li>
  </ul>
</nav>

<!-- ── Main ──────────────────────────────────────────────── -->
<main class="layout__main">
  <section class="panel" style="max-width:480px;margin-inline:auto" aria-labelledby="form-heading">

    <h2 id="form-heading">Link an Existing Bank Account</h2>

    <!-- optional server-side error -->
    <c:if test="${not empty error}">
      <p class="alert alert--error">
        <strong>Error:</strong> <c:out value="${error}"/>
      </p>
    </c:if>

    <!-- form -->
    <form class="form" method="post" action="<c:url value='/account/add'/>" novalidate>
      <label for="accountNumber">Account Number</label>
      <input id="accountNumber" name="accountNumber"
             inputmode="numeric" required
             placeholder="e.g. AC0418361021"/>

      <label for="pin">PIN (4 digits)</label>
      <input id="pin" name="pin" type="password"
             pattern="\d{4}" minlength="4" maxlength="4"
             inputmode="numeric" title="Exactly four digits" required/>

      <button type="submit" class="btn btn--primary" style="margin-top:.8rem">
        ➕ Add Account
      </button>
    </form>

    <a class="btn btn--link" href="<c:url value='/home'/>" style="margin-top:1.5rem">
      ← Back to Dashboard
    </a>
  </section>
</main>

<!-- ── Footer ────────────────────────────────────────────── -->
<footer class="layout__footer"><p>&copy;&nbsp;2025 MyBankApp</p></footer>

<!-- keyboard-only focus helper -->
<script>
  document.body.addEventListener('keydown', () => document.body.classList.add('user-is-tabbing'));
  document.body.addEventListener('mousedown', () => document.body.classList.remove('user-is-tabbing'));
</script>
</body>
</html>
