<%@include file="/jspf/header.jspf" %>

<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <link rel="stylesheet" href="css/order.css" type="text/css"/>
    <header>
        <div><h1><a href="/">Super Taxi</a></h1></div>
        <nav>
            <%--        <a href="/myPage">My space</a>--%>
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

<div class="main-block">
    <h1><fmt:message key="orderForm"/></h1>
    <form action="/makeOrder" method="post">
        <div class="info">
            <select name="loc_from">
                <option value="" disabled selected><fmt:message key="urLoc"/></option>
                <c:forEach items="${locationList}" var="item">
                    <option value=<c:out value="${item}"/>><c:out value="${item}"/></option>
                </c:forEach>
            </select>
            <select name="loc_to">
                <option value="" disabled selected><fmt:message key="destination"/></option>
                <c:forEach items="${locationList}" var="item">
                    <option value=<c:out value="${item}"/>><c:out value="${item}"/></option>
                </c:forEach>
            </select>

            <select name="passengers">
                <option value="" disabled selected><fmt:message key="numOfPassengers"/></option>
                <option value="2">2</option>
                <option value="4">4</option>
                <option value="6">6</option>
            </select>

            <select name="class">
                <option value="cheap" selected><fmt:message key="cheap"/></option>
                <option value="comfort"><fmt:message key="comfort"/></option>
                <option value="business"><fmt:message key="business"/></option>
            </select>

        </div>

        <c:choose>
            <c:when test="${requestScope.error != null}">
                <div style="color:red; text-align: center;"><fmt:message key="${requestScope.error}"/></div>
            </c:when>
        </c:choose>
        <button type="submit" class="button"><fmt:message key="submit"/></button>
    </form>
</div>
</body>
</html>
