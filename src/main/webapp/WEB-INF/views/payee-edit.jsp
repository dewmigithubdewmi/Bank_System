<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    com.sliit.bankapp.model.Payee p =
        (com.sliit.bankapp.model.Payee) request.getAttribute("oldPayee");
%>
<!DOCTYPE html>
<html>
<head>
  <title>Edit Payee</title>
  <style>
    label { display:block; margin:8px 0; }
    .error { color:#c00; font-weight:bold; margin:12px 0; }
  </style>
  <script>
    function toggleSections(){
      const v = document.getElementById("billType").value;
      const on  = id => document.getElementById(id).style.display = "block";
      const off = id => document.getElementById(id).style.display = "none";
      off("wc"); off("tel"); off("cred");

      document.querySelectorAll("#wc input,#tel input,#cred input")
              .forEach(i => i.disabled = true);

      if (v==="WATER"||v==="CURRENT"){
        on("wc");
        document.querySelectorAll("#wc input").forEach(i=>i.disabled=false);
      } else if (v==="TELEPHONE"){
        on("tel");
        document.querySelectorAll("#tel input").forEach(i=>i.disabled=false);
      } else {
        on("cred");
        document.querySelectorAll("#cred input").forEach(i=>i.disabled=false);
      }
    }
    window.onload = toggleSections;
  </script>
</head>
<body>

<h2>Edit Payee</h2>

<c:if test="${not empty error}">
  <div class="error">${error}</div>
</c:if>

<form method="post" action="${pageContext.request.contextPath}/billpay/edit">
  <input type="hidden" name="payeeId" value="${p.id}"/>

  <label>Bill Type:
    <select name="billType" id="billType" onchange="toggleSections()">
      <option value="WATER"    ${p.billType=="WATER"    ? "selected":""}>Water Bill</option>
      <option value="CURRENT"  ${p.billType=="CURRENT"  ? "selected":""}>Current Bill</option>
      <option value="TELEPHONE"${p.billType=="TELEPHONE"? "selected":""}>Telephone Bill</option>
      <option value="CREDIT"   ${p.billType=="CREDIT"   ? "selected":""}>Credit Card Bill</option>
    </select>
  </label>
  <hr/>

  <!-- Water / Current -->
  <div id="wc">
    <label>Account&nbsp;Number:
      <input name="accountNo" pattern="\d{6,20}"
             value="${p.billType=='WATER'||p.billType=='CURRENT' ? p.identifier : ''}">
    </label>
    <label>Account&nbsp;Name:
      <input name="accountName"
             value="${p.billType=='WATER'||p.billType=='CURRENT' ? p.acctName   : ''}">
    </label>
  </div>

  <!-- Telephone -->
  <div id="tel">
    <label>Mobile&nbsp;Number:
      <input name="mobileNo" pattern="(\+94|0)7\d{8}"
             placeholder="+94771234567 or 0771234567"
             value="${p.billType=='TELEPHONE' ? p.identifier : ''}">
    </label>
  </div>

  <!-- Credit -->
  <div id="cred">
    <label>Credit&nbsp;Card&nbsp;Number:
      <input name="cardNo" pattern="\d{16}"
             placeholder="16 digits"
             value="${p.billType=='CREDIT' ? p.identifier : ''}">
    </label>
  </div>

  <label>Nickname:
    <input name="nick" value="${p.nickName}">
  </label><br/>

  <button type="submit">Save Changes</button>
  <a href="${pageContext.request.contextPath}/billpay">
    <button type="button">Cancel</button>
  </a>
</form>

</body>
</html>
