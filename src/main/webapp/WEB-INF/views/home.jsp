<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8"/>
  <title>Dashboard</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <!-- Theme CSS -->
  <link rel="stylesheet" href="<c:url value='/css/base.css? v-0.2'/>"/>
  <link rel="stylesheet" href="<c:url value='/css/layout.css? v-0.2'/>"/>
  <link rel="stylesheet" href="<c:url value='/css/components/buttons.css? v-0.1'/>"/>
  <link rel="stylesheet" href="<c:url value='/css/components/forms.css? v-0.04'/>"/>
  <link rel="stylesheet" href="<c:url value='/css/components/tables.css? v-0.2'/>"/>
  <link rel="stylesheet" href="<c:url value='/css/pages/dashboard.css?v-0.1'/>"/>
</head>
<body>

  <div id="flash" role="alert" aria-live="assertive" class="flash"></div>

  <header class="layout__header" role="banner">
    <h1>Welcome, <c:out value="${currentUser.firstName}"/></h1>
    <form action="${pageContext.request.contextPath}/logout"
          method="get"
          aria-label="Log out"
          onsubmit="document.getElementById('flash').textContent='You’ve been logged out.'">
      <button type="submit"
              class="btn btn--secondary"
              accesskey="L"
              title="Log out (Alt+L)">
        Logout
      </button>
    </form>
  </header>

  <nav class="layout__nav" aria-label="Primary actions">
    <button class="nav__toggle btn btn--icon"
            aria-expanded="false"
            aria-controls="nav-list"
            onclick="
              const nav = document.getElementById('nav-list');
              const expanded = this.getAttribute('aria-expanded') === 'true';
              this.setAttribute('aria-expanded', !expanded);
              nav.hidden = expanded;">
    </button>
    <ul id="nav-list" class="nav__list" hidden>
      <li>
        <a href="<c:url value='/transfer'/>"
           class="btn btn--primary"
           accesskey="T"
           title="Transfer Money (Alt+T)">
          💸 Transfer
        </a>
      </li>
      <li>
        <a href="<c:url value='/account/add'/>"
           class="btn btn--primary"
           accesskey="A"
           title="Add Account (Alt+A)">
          ➕ Add Account
        </a>
      </li>
      <li>
        <a href="<c:url value='/billpay'/>"
           class="btn btn--primary"
           accesskey="B"
           title="Bill Payment (Alt+B)">
          🧾 Bill Pay
        </a>
      </li>
    </ul>
  </nav>

  <main class="layout__main" role="main">
    <!-- Accounts -->
    <section class="panel" aria-labelledby="accounts-heading">
      <h2 id="accounts-heading">Your Bank Accounts</h2>
      <c:choose>
        <c:when test="${empty accounts}">
          <p>No accounts yet.</p>
        </c:when>
        <c:otherwise>
          <table class="table" aria-describedby="accounts-heading">
            <caption>Linked Accounts</caption>
            <thead>
              <tr>
                <th>Account #</th>
                <th>Balance (Rs.)</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach var="a" items="${accounts}">
                <tr>
                  <td>
                    <a href="<c:url value='/account/profile?id=${a.id}'/>">
                      <c:out value="${a.accountNumber}"/>
                    </a>
                  </td>
                  <td><c:out value="${a.balance}"/></td>
                  <td>
                    <form action="<c:url value='/account/remove'/>"
                          method="post"
                          onsubmit="return confirm('Remove account ${a.accountNumber}?');">
                      <input type="hidden" name="accountId" value="${a.id}"/>
                      <button type="submit"
                              class="btn btn--danger"
                              title="Remove">
                        🗑️
                      </button>
                    </form>
                  </td>
                </tr>
              </c:forEach>
            </tbody>
          </table>
        </c:otherwise>
      </c:choose>
    </section>

    <hr/>

    <!-- Create Account -->
    <section class="panel" aria-labelledby="new-account-heading">
      <h2 id="new-account-heading">Create New Account</h2>
      <form method="post"
            action="<c:url value='/account/create'/>"
            class="form"
            novalidate
            onsubmit="if (!this.checkValidity()) { alert('Please fill out all fields correctly.'); return false; }">
        <fieldset>
          <legend>Personal Details</legend>
          <label for="fullName">Full Name</label>
          <input id="fullName" name="fullName" required autofocus/>

          <label for="nic">NIC No.</label>
          <input id="nic" name="nic" required/>

          <label for="address">Address</label>
          <input id="address" name="address" required/>

          <label for="mobile">Mobile No.</label>
          <input id="mobile"
               name="mobile"
               pattern="\d{10}"
               minlength="10" maxlength="10"
               inputmode="numeric"
               placeholder="07XXXXXXXX"
               title="Exactly 10 digits, numbers only"
               required/>
        </fieldset>

        <fieldset>
          <legend>Account Details</legend>
          <label for="balance">Opening Balance</label>
          <input id="balance" name="balance" type="number" step="0.01" required/>

          <label for="pin">PIN (4 digits)</label>
          <input id="pin" name="pin" type="password" pattern="\d{4}" required/>
        </fieldset>

        <button type="submit" class="btn btn--primary">
          Create Account
        </button>
      </form>
    </section>

    <hr/>

    <!-- Advertisements -->
    <section class="panel" aria-labelledby="ads-heading">
      <h2 id="ads-heading">Latest Advertisements</h2>
      <c:choose>
        <c:when test="${empty publicAds}">
          <p>No ads published yet.</p>
        </c:when>
        <c:otherwise>
          <table class="table" aria-describedby="ads-heading">
            <caption>Public Ads</caption>
            <thead>
              <tr><th>Title</th></tr>
            </thead>
            <tbody>
              <c:forEach var="ad" items="${publicAds}">
                <tr>
                  <td>
                    <a href="<c:url value='/ad?id=${ad.id}'/>" class="btn btn--link">
                      📣 <c:out value="${ad.title}"/>
                    </a>
                  </td>
                </tr>
              </c:forEach>
            </tbody>
          </table>
        </c:otherwise>
      </c:choose>
    </section>
  </main>

  <footer class="layout__footer" role="contentinfo">
    <p>&copy; 2025 MyBankApp</p>
  </footer>

  <script>
    document.body.addEventListener('keydown', () => document.body.classList.add('user-is-tabbing'));
    document.body.addEventListener('mousedown', () => document.body.classList.remove('user-is-tabbing'));
  </script>
</body>
</html>
