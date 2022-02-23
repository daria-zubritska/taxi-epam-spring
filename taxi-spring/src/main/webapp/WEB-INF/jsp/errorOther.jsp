<%@include file="/jspf/header.jspf"%>

<head>
    <link href="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
    <link href="/css/error.css" rel="stylesheet">
    <script src="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js"></script>
    <script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
</head>
<body>
<div class="page-wrap d-flex flex-row align-items-center">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-12 text-center">
                <span class="display-1 d-block">500</span>
                <div class="mb-4 lead"><fmt:message key="theServerHadTrouble"/></div>
                <a href="/" class="btn btn-link"><fmt:message key="returnToMain"/></a>
            </div>
        </div>
    </div>
</div>
</body>
</html>
