<%@include file="../jspf/header.jspf" %>

<head>
    <meta charset="UTF-8">
    <title><fmt:message key="mainTitle"/></title>
    <link rel="stylesheet" href="css/index.css" type="text/css"/>
</head>
<header>
    <div><h1><a href="/">Super Taxi</a></h1></div>
    <nav>
        <%--        <a href="/myPage"><fmt:message key="mySpace"/></a>--%>
        <%--        <space></space>--%>

        <c:choose>
            <c:when test="${sessionScope.user != null && sessionScope.user.role.user}">
                <a href="/makeOrder"><fmt:message key="order"/></a>
            </c:when>
            <c:when test="${sessionScope.user != null && sessionScope.user.role.admin}">
                <a href="/statistics"><fmt:message key="statistics"/></a>
            </c:when>
        </c:choose>
        <space></space>
        <c:choose>
            <c:when test="${sessionScope.user == null}">
                <a href="/logIn"><fmt:message key="logIn"/></a>
            </c:when>
            <c:otherwise>
                <a href="/logOut"><fmt:message key="logOut"/></a>
            </c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${requestScope.lang == 'en'}">
                <a href="javascript:setLang('ua')"><fmt:message key="lang"/></a>
            </c:when>
            <c:otherwise>
                <a href="javascript:setLang('en')"><fmt:message key="lang"/></a>
            </c:otherwise>
        </c:choose>
    </nav>
</header>

<script type="text/javascript">
    function setLang(lang) {
        document.cookie = "lang=" + lang + ";";
        location.reload();
    }
</script>

<body>
<p>
<div>

</div>
</p>
</body>
</html>
