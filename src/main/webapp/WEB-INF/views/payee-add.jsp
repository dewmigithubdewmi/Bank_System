<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    // If the servlet forwarded here for “Edit”, it set oldPayee.
    com.sliit.bankapp.model.Payee oldPayee =
        (com.sliit.bankapp.model.Payee) request.getAttribute("oldPayee");
%>
<c:set var="isEdit" value="${oldPayee != null}" />

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>

  <!-- theme -->
  <link rel="stylesheet" href="<c:url value='/css/base.css'/>"/>
  <link rel="stylesheet" href="<c:url value='/css/layout.css? v-0.3'/>"/>
  <link rel="stylesheet" href="<c:url value='/css/components/buttons.css'/>"/>
  <link rel="stylesheet" href="<c:url value='/css/components/forms.css'/>"/>

  <title>
    <c:choose>
      <c:when test="${isEdit}">Edit Payee</c:when>
      <c:otherwise>Add Payee</c:otherwise>
    </c:choose>
  </title>

  <script>
    function swapForm() {
      const t = document.getElementById("billType").value;
      const show = id => document.getElementById(id).style.display = "block";
      const hide = id => document.getElementById(id).style.display = "none";

      // hide all
      ["waterCurrent","telephone","credit"].forEach(hide);
      // disable all inputs
      document.querySelectorAll(
        "#waterCurrent input, #telephone input, #credit input"
      ).forEach(i => i.disabled = true);

      if (t === "WATER" || t === "CURRENT") {
        show("waterCurrent");
        document.querySelectorAll("#waterCurrent input")
                .forEach(i => i.disabled = false);
      } else if (t === "TELEPHONE") {
        show("telephone");
        document.querySelectorAll("#telephone input")
                .forEach(i => i.disabled = false);
      } else if (t === "CREDIT") {
        show("credit");
        document.querySelectorAll("#credit input")
                .forEach(i => i.disabled = false);
      }
    }
    window.onload = swapForm;
  </script>
</head>

<body>

  <header class="layout__header">
    <h1><c:out value="${isEdit ? 'Edit Payee' : 'Add Payee'}"/></h1>
  </header>

  <main class="layout__main">
    <section>

      <c:if test="${not empty error}">
        <div class="error" role="alert"><c:out value="${error}"/></div>
      </c:if>

      <form method="post"
            action="${pageContext.request.contextPath}${isEdit ? '/billpay/edit' : '/billpay/add'}"
            class="form">

        <c:if test="${isEdit}">
          <input type="hidden" name="payeeId" value="${oldPayee.id}"/>
        </c:if>

        <label for="billType">Bill Type:</label>
        <select name="billType" id="billType" onchange="swapForm()">
          <option value="WATER"    ${isEdit && oldPayee.billType=='WATER'    ? 'selected':''}>Water Bill</option>
          <option value="CURRENT"  ${isEdit && oldPayee.billType=='CURRENT'  ? 'selected':''}>Current Bill</option>
          <option value="TELEPHONE"${isEdit && oldPayee.billType=='TELEPHONE'? 'selected':''}>Telephone Bill</option>
          <option value="CREDIT"   ${isEdit && oldPayee.billType=='CREDIT'   ? 'selected':''}>Credit Card Bill</option>
        </select>

        <hr/>

        <div id="waterCurrent">
          <label for="accountNo">Account Number:</label>
          <input id="accountNo" type="text" name="accountNo"
                 pattern="\d{6,20}"
                 value="${isEdit? oldPayee.identifier : ''}"
                 required/>

          <label for="accountName">Account Name:</label>
          <input id="accountName" type="text" name="accountName"
                 value="${isEdit? oldPayee.acctName : ''}"
                 required/>
        </div>

        <div id="telephone">
          <label for="mobileNo">Mobile Number:</label>
          <input id="mobileNo" type="text" name="mobileNo"
                 pattern="(\+94|0)7\d{8}"
                 placeholder="+94771234567 or 0771234567"
                 value="${isEdit? oldPayee.identifier : ''}"
                 required/>
          <small>Format: +947XXXXXXXX or 07XXXXXXXX</small>
        </div>

        <div id="credit">
          <label for="cardNo">Credit Card Number:</label>
          <input id="cardNo" type="text" name="cardNo"
                 pattern="\d{16}"
                 placeholder="16 digits"
                 value="${isEdit? oldPayee.identifier : ''}"
                 required/>
        </div>

        <label for="nick">Nickname:</label>
        <input id="nick" type="text" name="nick"
               value="${isEdit? oldPayee.nickName : ''}"
               required/>

        <div style="margin-top:1.5rem;">
          <button type="submit" class="btn btn--primary">
            <c:out value="${isEdit ? 'Save Changes' : 'Add Payee'}"/>
          </button>
          <a href="${pageContext.request.contextPath}/billpay" class="btn btn--secondary">
            Cancel
          </a>
        </div>
      </form>

    </section>
  </main>

  <footer class="layout__footer">
    <p>&copy; 2025 MyBankApp</p>
  </footer>

</body>
</html>
