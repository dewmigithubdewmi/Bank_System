<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8"/>
  <title>Admin Dashboard</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <link rel="stylesheet" href="<c:url value='/css/base.css? v-0.2'/>"/>
  <link rel="stylesheet" href="<c:url value='/css/layout.css? v-0.3'/>"/>
  <link rel="stylesheet" href="<c:url value='/css/components/buttons.css'/>"/>
  <link rel="stylesheet" href="<c:url value='/css/components/tables.css'/>"/>
  <link rel="stylesheet" href="<c:url value='/css/components/forms.css? v-0.2'/>"/>
</head>
<body>

<header class="layout__header">
  <h1>Welcome, Admin <c:out value="${currentUser.firstName}"/></h1>
  <a href="<c:url value='/logout'/>" class="btn btn--secondary">Logout</a>
</header>

<main class="layout__main">
  <section>
    <a class="btn btn--primary" href="<c:url value='/admin/advert-add'/>">➕ Create Advert</a>

    <c:if test="${not empty param.msg}">
      <p style="color:#4caf50;margin-top:1rem">
        <c:choose>
          <c:when test="${param.msg=='advertUpdated'}">Advertisement updated.</c:when>
          <c:when test="${param.msg=='advertDeleted'}">Advertisement deleted.</c:when>
        </c:choose>
      </p>
    </c:if>

    <h2 style="margin-top:1.5rem">Your Advertisements</h2>

    <c:choose>
      <c:when test="${empty adverts}">
        <p>No advertisements yet.</p>
      </c:when>
      <c:otherwise>
        <table class="table table--striped">
          <thead>
            <tr><th>Title</th><th>Description</th><th>Rules</th><th>Actions</th></tr>
          </thead>
          <tbody>
          <c:forEach var="ad" items="${adverts}">
            <tr>
              <td><c:out value="${ad.title}"/></td>
              <td>
                <c:choose>
                  <c:when test="${fn:length(ad.description) > 120}">
                    <c:out value="${fn:substring(ad.description,0,120)}"/>…
                  </c:when>
                  <c:otherwise><c:out value="${ad.description}"/></c:otherwise>
                </c:choose>
              </td>
              <td>
                <c:choose>
                  <c:when test="${fn:length(ad.rules) > 120}">
                    <c:out value="${fn:substring(ad.rules,0,120)}"/>…
                  </c:when>
                  <c:otherwise><c:out value="${ad.rules}"/></c:otherwise>
                </c:choose>
              </td>
              <td style="white-space:nowrap">
                <a href="<c:url value='/admin/advert-edit?id=${ad.id}'/>"
                   class="btn btn--secondary">Edit</a>
                <form action="<c:url value='/admin/advert-delete'/>" method="post"
                      style="display:inline" onsubmit="return confirm('Delete this ad?');">
                  <input type="hidden" name="id" value="${ad.id}"/>
                  <button type="submit" class="btn btn--danger">Delete</button>
                </form>
              </td>
            </tr>
          </c:forEach>
          </tbody>
        </table>
      </c:otherwise>
    </c:choose>
  </section>
</main>

<footer class="layout__footer"><p>&copy; 2025 MyBankApp</p></footer>
</body>
</html>
