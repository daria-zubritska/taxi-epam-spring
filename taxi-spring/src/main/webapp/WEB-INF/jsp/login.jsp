<%@include file="/jspf/header.jspf" %>

<head>
    <title>Simple login form</title>
    <link href="https://fonts.googleapis.com/css?family=Roboto:300,400,500,700" rel="stylesheet">
    <%--    <link href="css/login.css" rel="stylesheet">--%>

</head>
<style>
    html, body {
        display: flex;
        justify-content: center;
        font-family: Roboto, Arial, sans-serif;
        font-size: 15px;
        background: #1abc9c;
    }

    form {
        border: 5px solid #f1f1f1;
        background: white;
    }

    input[type=text], input[type=password] {
        width: 100%;
        padding: 16px 8px;
        margin: 8px 0;
        display: inline-block;
        border: 1px solid #ccc;
        box-sizing: border-box;
    }

    button {
        background-color: #2c3e50;
        color: white;
        padding: 14px 0;
        margin: 10px 0;
        border: none;
        cursor: grabbing;
        width: 100%;
    }

    h1 {
        text-align: center;
        fone-size: 18;
    }

    button:hover {
        opacity: 0.8;
    }

    .formcontainer {
        text-align: left;
        margin: 24px 50px 12px;
    }

    .container {
        padding: 16px 0;
        text-align: left;
    }

    span.psw {
        float: right;
        padding-top: 0;
        padding-right: 15px;
    }

    /* Change styles for span on extra small screens */
    @media screen and (max-width: 300px) {
        span.psw {
            display: block;
            float: none;
        }
    }
</style>
<header>

</header>
<body>
<form action="/logIn" method="post">
    <h1><fmt:message key="loginForm"/></h1>
    <div class="formcontainer">
        <hr/>
        <div class="container">
            <label><strong><fmt:message key="emailText"/></strong></label>
            <input type="text" placeholder=<fmt:message key="enterEmail"/>; name="email" required
                   value="<c:out value="${requestScope.email}" />">
            <label><strong><fmt:message key="passwordText"/></strong></label>
            <input type="password" placeholder=<fmt:message key="enterPassword"/>; name="password" required
                   value="<c:out value="${requestScope.password}" />">
        </div>
        <c:choose>
            <c:when test="${requestScope.error != null}">
                <div style="color:red; text-align: center;"><fmt:message key="${requestScope.error}"/></div>
            </c:when>
        </c:choose>
        <button type="submit"><fmt:message key="logIn"/></button>
        <div class="container" style="background-color: #eee">
            <span class="psw">
                <a href="/register" class="text-info"><fmt:message key="register"/></a>
                <a href="/" class="text-info"><fmt:message key="returnToMain"/></a>
            </span>
        </div>

    </div>
</form>
</body>
</html>