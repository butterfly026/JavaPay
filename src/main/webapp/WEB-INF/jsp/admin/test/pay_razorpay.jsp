<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="adminPermission" uri="http://adminpermission.city.com" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>Pay</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/css/bootstrap.min.css"/>
    <script src="https://cdn.jsdelivr.net/npm/jquery@1.12.4/dist/jquery.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/js/bootstrap.min.js"></script>
    <style>
        .paybtn {
            background-color: #3479B5;
            padding: 0.5em 1em 0.5em 1em;
            color: #FFFFFF;
            border: 0;
            border-radius: 0.3em;
            font-size: 1.2em;
            margin-top: 0.5em;
            display: none;
        }
    </style>
</head>
<body>

<div class="layui-fluid">
    <c:if test="${error!=null}">
        <div style="color:red;">${error}</div>
    </c:if>

    <div style="text-align:center;margin-top:10em;">

        <img src="${context}/paytm/razorpay.svg" style="width:40%;max-width:10em;background-color: #000000;"/>

        <div class="row dialogtitlebox">
            <div class="col-sm-5 col-xs-5 col-md-5"
                 style="text-align:right;padding:0px;font-size:1.2em;font-weight:bold;padding-right:10px;">
                OrderId:
            </div>
            <div class="col-sm-6 col-xs-6 col-md-6"
                 style="text-align:left;padding:0px;font-size:1.2em;font-weight:bold;color:green;">
                ${orderId}
            </div>

            <div class="col-sm-5 col-xs-5 col-md-5"
                 style="text-align:right;padding:0px;font-size:1.2em;font-weight:bold;padding-right:10px;">
                Amount:
            </div>
            <div class="col-sm-6 col-xs-6 col-md-6"
                 style="text-align:left;padding:0px;font-size:1.2em;font-weight:bold;color:green;">
                ${amount} ₹
            </div>

        </div>

        <div id="log" style="color:red;font-size:1.2em;margin-top:0.5em;"></div>

        <div id="loaddiv" style="color:green;">
            <img src="${context}/paytm/loading.gif"/>&nbsp;Loading...
        </div>

        <button id="rzp-button1" class="paybtn">GO TO PAY!</button>

    </div>


</div>

<c:if test="${error==null}">
    <script src="https://checkout.razorpay.com/v1/checkout.js"></script>
    <script>
        var options = {
            "key": "${mid}",
            "amount": "${amount}",
            "currency": "INR",
            "name": "Razorpay Pay",
            "description": "Transaction",
            "order_id": "${orderId}",
            "callback_url": "${context}/api/pay/rznotify",
            "prefill": {
                "name": "RazorMan",
                "email": "rzpay@gmail.com",
                "contact": ""
            },
            "theme": {
                "color": "#3399cc"
            }
        };
        $(function () {
            var rzp1 = new Razorpay(options);

            $("#rzp-button1").show();
            $("#loaddiv").hide();

            document.getElementById('rzp-button1').onclick = function (e) {
                rzp1.open();
                e.preventDefault();
            }
        });
    </script>
</c:if>

</body>
</html>
