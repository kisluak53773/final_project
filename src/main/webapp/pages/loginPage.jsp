<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<fmt:setLocale value="${locale}" scope="session"/>
<fmt:setBundle basename="prop.pagecontent"/>

<html>
<head>
    <title><fmt:message key="auth.loginTitle"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" type="text/css">
</head>
<body>
<div class="login_container">
    <div class="mainText">
        <h1><fmt:message key="auth.loginHeader"/></h1>
        <span><fmt:message key="auth.loginRequest"/>
        </span>
    </div>
    <div class="login_inner_container">
        <form class="login-form validate-form" method="post"
              action="${pageContext.request.contextPath}/controller">
            <input type="hidden" name="command" value="login">
            <div class="login_inner_wrapper_container">
                    <div class="login_inner_wrapper_container_items">
                        <p><fmt:message key="auth.email"/></p>
                        <input type="text" class="form-control" id="email" name="email" required="required">
                    </div>
                    <div class="login_inner_wrapper_container_items">
                        <p><fmt:message key="auth.password"/></p>
                        <input type="password" class="form-control" id="password" name="password" required="required">
                    </div>
                <button type="submit" class="btn btn-online lg"><fmt:message key="auth.butnLogin"/></button>
                <a href="${pageContext.request.contextPath}/controller?command=to_register_page"><fmt:message key="auth.btnRegister"/></a>
            </div>
          </form>
    </div>
</div>
</body>
</html>