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
    <h1><fmt:message key="orderSubmitForm"/></h1>
    <form action="/orderSubmit" method="post">

        <c:choose>
            <c:when test="${absentUserChoice != null}">

                <c:choose>
                    <c:when test="${doubleOrder == null}">
                        <h3><fmt:message key="chooseOther"/></h3>

                        <div class="metod">

                            <input type="radio" name="orderCheck" checked id=${orderChoice.id}; value=${orderChoice.id}>
                            <label class="radio" for=${orderChoice.id};>
                                <p>${orderChoice.carName}</p>
                                <p><ftm:message key="${orderChoice.carClass}"/></p>
                                <p><fmt:message key="passengers"/> ${orderChoice.passengers}</p>
                                <space></space>
                                <p><fmt:message key="cost"/> ${orderChoice.cost} <ftm:message key="uah"/></p>

                                <space></space>
                                <p><fmt:message key="costWithDiscount"/> ${orderChoice.costWithDiscount}
                                    <ftm:message key="uah"/></p>
                            </label>
                        </div>
                    </c:when>

                    <c:otherwise>
                        <h3><fmt:message key="chooseOther"/></h3>

                        <div class="metod">

                            <input type="radio" name="orderCheck" checked id=${doubleOrder.order1.id}; value=${doubleOrder.order1.id}>
                            <label class="radio" for=${doubleOrder.order1.id};>
                                <p>${doubleOrder.order1.carName}</p>
                                <p>${doubleOrder.order2.carName}</p>
                                <p><ftm:message key="${doubleOrder.order1.carClass}"/></p>
                                <p><fmt:message key="passengers"/> ${doubleOrder.order1.passengers}</p>
                                <space></space>
                                <p><fmt:message key="cost"/> ${doubleOrder.fullCost} <ftm:message key="uah"/></p>

                                <space></space>
                                <p><fmt:message key="costWithDiscount"/> ${doubleOrder.costWithDiscount}
                                    <ftm:message key="uah"/></p>
                            </label>
                        </div>
                    </c:otherwise>
                </c:choose>
            </c:when>

            <c:when test="${orderChoice != null}">
                <h3><fmt:message key="yourOrder"/></h3>

                <div class="metod">

                    <div>
                        <input type="radio" name="orderCheck" checked id=${orderChoice.id}; value=${orderChoice.id}>
                        <label class="radio" for=${orderChoice.id};>
                            <p>${orderChoice.carName}</p>
                            <p><ftm:message key="${orderChoice.carClass}"/></p>
                            <p><fmt:message key="passengers"/> ${orderChoice.passengers}</p>
                            <space></space>
                            <p><fmt:message key="cost"/> ${orderChoice.cost} <ftm:message key="uah"/></p>

                        </label>
                    </div>

                </div>
            </c:when>

            <c:otherwise>
                <div style="color:red; text-align: center;">
                    <fmt:message key="noValidCars"/>
                </div>
            </c:otherwise>


        </c:choose>

        <c:choose>
            <c:when test="${requestScope.error != null}">
                <div style="color:red; text-align: center;"><fmt:message key="${requestScope.error}"/></div>
            </c:when>
        </c:choose>

        <c:choose>
            <c:when test="${requestScope.wait != null}">
                <div style="text-align: center;"><fmt:message key="${requestScope.wait}"/></div>
            </c:when>
        </c:choose>

        <div style="display: flex">

            <c:choose>
                <c:when test="${requestScope.wait == null && (orderChoice != null || absentUserChoice != null)}">
                    <button type="submit" class="button" name="cancel" value="Cancel btn"><fmt:message
                            key="cancel"/></button>
                    <button type="submit" class="button" name="submit" value="Submit btn"><fmt:message
                            key="submit"/></button>
                </c:when>
                <c:otherwise>
                    <button style="align-self: center" type="submit" class="button" name="ok" value="Okay btn"><fmt:message
                            key="ok"/></button>
                </c:otherwise>
            </c:choose>


        </div>
    </form>
</div>
</body>
</html>
