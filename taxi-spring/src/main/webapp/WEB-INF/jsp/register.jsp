<%@include file="/jspf/header.jspf"%>

<head>
    <link href="https://fonts.googleapis.com/css?family=Roboto:300,400,500,700" rel="stylesheet">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.4.1/css/all.css"
          integrity="sha384-5sAR7xN1Nv6T6+dT2mhtzEpVJvfS3NScPQTrOxhwjIuvcA67KV2R5Jz6kr4abQsz" crossorigin="anonymous">
    <link href="css/register.css" rel="stylesheet">

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
        background: #2c3e50;
        color: white;
        padding: 14px 0;
        margin: 10px 0;
        border: none;
        cursor: grab;
        width: 48%;
    }
    h1 {
        text-align:center;
        fone-size:18;
    }
    button:hover {
        opacity: 0.8;
    }
    .formcontainer {
        text-align: center;
        margin: 24px 50px 12px;
    }
    .container {
        padding: 16px 0;
        text-align:left;
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
<script type="text/javascript">
    function setLang(lang){
        document.cookie = "lang=" + lang + ";";
        location.reload();
    }
</script>
<body>
<form action="/register" method="post">
    <h1><fmt:message key="registerForm"/></h1>
    <div class="formcontainer">
        <div class="container">
            <label><strong><fmt:message key="usernameText"/></strong></label>
            <input type="text" placeholder=<fmt:message key="enterUsername"/>; name="username" value="<c:out value="${requestScope.username}" />" required>
            <label><strong><fmt:message key="emailText"/></strong></label>
            <input type="text" placeholder=<fmt:message key="enterEmail"/>; name="email" value="<c:out value="${requestScope.email}" />" required>
            <label><strong><fmt:message key="passwordText"/></strong></label>
            <input type="password" placeholder=<fmt:message key="enterPassword"/>; name="password" value="<c:out value="${requestScope.password}" />" required>
        </div>
        <c:choose>
            <c:when test="${requestScope.error != null}">
                <div style="color:red; text-align: center;"><fmt:message key="${requestScope.error}"/></div>
            </c:when>
        </c:choose>
        <button type="submit"><strong><fmt:message key="register"/></strong></button>
        <div class="container" style="background-color: #eee">
            <label style="padding-left: 15px">
                <%--                <input type="checkbox" name="remember"--%>
                <%--                       value="<c:out value="${requestScope.remember}" default=""/>"> Remember me--%>
            </label>
            <span class="psw">
                <a href="/logIn"><fmt:message key="alreadyHaveAcc"/></a>
                <a href="/"><fmt:message key="returnToMain"/></a>
            </span>

        </div>
    </div>
</form>
</body>
</html>