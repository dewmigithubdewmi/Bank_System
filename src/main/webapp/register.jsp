<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8"/>
  <title>Create Account</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>

  <!-- Versioned CSS for cache-busting -->
  <link rel="stylesheet" href="<c:url value='/css/base.css?v=0.1'/>"/>
  <link rel="stylesheet" href="<c:url value='/css/layout.css?v=0.1'/>"/>
  <link rel="stylesheet" href="<c:url value='/css/components/buttons.css?v=0.1'/>"/>
  <link rel="stylesheet" href="<c:url value='/css/components/forms.css?v=0.1'/>"/>
</head>
<body>

  <!-- Flash messages -->
  <div id="flash" role="alert" aria-live="assertive" class="flash"></div>

  <header class="layout__header" role="banner">
    <h1>Create Account</h1>
  </header>

  <main class="layout__main" role="main">
    <section style="max-width:450px; margin-inline:auto;">
      <form method="post"
            action="<c:url value='/register'/>"
            class="form"
            novalidate
            onsubmit="if(!this.checkValidity()){ alert('Please fill out all fields.'); return false; }">

        <label for="firstName">First Name</label>
        <input id="firstName" name="firstName" required placeholder="e.g. Jane"/>

        <label for="lastName">Last Name</label>
        <input id="lastName" name="lastName" required placeholder="e.g. Doe"/>

        <label for="email">Email</label>
        <input id="email" type="email" name="email" required placeholder="you@example.com"/>

        <label for="password">Password</label>
        <input id="password" type="password" name="password" required/>

        <button type="submit" class="btn btn--primary" style="margin-top:1rem;">
          Register &amp; Login
        </button>
      </form>
    </section>
  </main>

  <footer class="layout__footer" role="contentinfo">
    <p>&copy; 2025 MyBankApp</p>
  </footer>

  <script>
    // focus-visible helper
    document.body.addEventListener('keydown', ()=>document.body.classList.add('user-is-tabbing'));
    document.body.addEventListener('mousedown', ()=>document.body.classList.remove('user-is-tabbing'));
  </script>
</body>
</html>
