<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
  com.sliit.bankapp.model.Payee old =
      (com.sliit.bankapp.model.Payee) request.getAttribute("oldPayee");
  boolean editing = old != null;
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8"/>
  <title><c:out value="${editing? 'Edit Payee':'Add Payee'}"/></title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <link rel="stylesheet" href="<c:url value='/css/base.css'/>"/>
  <link rel="stylesheet" href="<c:url value='/css/layout.css? v-0.3'/>"/>
  <link rel="stylesheet" href="<c:url value='/css/components/buttons.css'/>"/>
  <link rel="stylesheet" href="<c:url value='/css/components/forms.css'/>"/>

  <script>
    function swap() {
      const t = billType.value;
      ["water","tel","cred"].forEach(id => {
        const box = document.getElementById(id);
        box.style.display = (id==="water"&&(t==="WATER"||t==="CURRENT") ||
                             id==="tel"  && t==="TELEPHONE" ||
                             id==="cred" && t==="CREDIT") ? "block" : "none";
        box.querySelectorAll("input").forEach(i=>i.disabled = box.style.display==="none");
      });
    }
    window.onload = swap;
  </script>
</head>
<body>

<header class="layout__header"><h1><c:out value="${editing? 'Edit Payee':'Add Payee'}"/></h1></header>

<main class="layout__main">
  <section style="max-width:650px">
    <c:if test="${not empty error}"><p class="error">${error}</p></c:if>

    <form method="post"
          action="<c:url value='/billpay/${editing? "edit":"add"}'/>"
          class="form" novalidate>
      <c:if test="${editing}">
        <input type="hidden" name="payeeId" value="${old.id}"/>
      </c:if>

      <!-- bill type -->
      <label>Bill Type:
        <select name="billType" id="billType" onchange="swap()">
          <c:set var="bt" value="${editing? old.billType:'WATER'}"/>
          <option value="WATER"   ${bt=='WATER'?'selected':''}>Water Bill</option>
          <option value="CURRENT" ${bt=='CURRENT'?'selected':''}>Current Bill</option>
          <option value="TELEPHONE" ${bt=='TELEPHONE'?'selected':''}>Telephone Bill</option>
          <option value="CREDIT"  ${bt=='CREDIT'?'selected':''}>Credit Card Bill</option>
        </select>
      </label><hr/>

      <!-- Water / Current -->
      <div id="water">
        <label>Account Number:
          <input name="accountNo" pattern="\\d{6,20}"
                 value="${editing? old.identifier:''}"/>
        </label>
        <label>Account Name:
          <input name="accountName" value="${editing? old.acctName:''}"/>
        </label>
      </div>

      <!-- Telephone -->
      <div id="tel">
        <label>Mobile Number:
          <input name="mobileNo" pattern="(?:\\+94|0)7\\d{8}"
                 placeholder="+94771234567 or 0771234567"
                 value="${editing? old.identifier:''}"/>
        </label>
      </div>

      <!-- Credit Card -->
      <div id="cred">
        <label>Credit Card Number:
          <input name="cardNo" pattern="\\d{16}"
                 placeholder="16-digits"
                 value="${editing? old.identifier:''}"/>
        </label>
      </div>

      <label>Nickname:
        <input name="nick" value="${editing? old.nickName:''}" required/>
      </label>

      <div style="margin-top:1rem;display:flex;gap:.8rem">
        <button type="submit" class="btn btn--primary">
          <c:out value="${editing? 'Save Changes':'Add Payee'}"/>
        </button>
        <a href="<c:url value='/billpay'/>" class="btn btn--secondary">Cancel</a>
      </div>
    </form>
  </section>
</main>

<footer class="layout__footer"><p>&copy; 2025 MyBankApp</p></footer>
</body>
</html>
