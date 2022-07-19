<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<fmt:setLocale value="${locale}" scope="session"/>
<fmt:setBundle basename="prop.pagecontent"/>

<html>
<head>
    <title><fmt:message key="main.servicesTitle"/></title>
    <c:if test="${requestScope.pagination==null}">
        <jsp:forward page="/controller?command=find_services"></jsp:forward>
    </c:if>
</head>
<body>

</body>
</html>
