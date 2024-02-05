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
        body, html {
            padding: 0;
            margin: 0;
        }

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

        .lgbtn {
            background-color: #D2D2D2;
            border-color: #D2D2D2;
            color: #000000;
        }

        .lgbtnsel {
            background-color: #3479B5;
            border-color: #3479B5;
            color: #FFFFFF;
        }

    </style>
</head>
<body>

<div class="container">
    <c:if test="${error!=null}">
        <div style="color:red;">${error}</div>
    </c:if>

    <div style="text-align:center;margin-top:10em;">
        <img src="${context}/paytm/paytm.png" style="width:40%;max-width:10em;"/>

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

        <button class="paybtn" onclick="goPay()" id="payBtn">GO TO PAY!</button>

        <div>
            <div style="text-align:center;margin-top:1em;" align=" center;">
                <b style="color:blue;">Payment Video:</b>
                <div class="btn-group">
                    <button onclick="playVideo(0,this)" class="btn lgbtn lgbtnsel">UPI</button>
                    <button onclick="playVideo(1,this)" class="btn lgbtn">Paytm</button>
                    <button onclick="playVideo(2,this)" class="btn lgbtn">Net Banking</button>
                </div>

                <div style="margin-top:0.5em;width:100%;margin:0;padding:0;">
                    <iframe id="videoIfm" src="${context}/video/video.html?vd=0&vs=3"
                            style="width: 90%;height:300px;margin-top: 10px;border: 0;overflow: hidden;"></iframe>
                </div>

            </div>

        </div>

    </div>


</div>

<c:if test="${error==null}">
    <div id="paytm-checkoutjs"></div>
    <script type="application/javascript" src="https://securegw.paytm.in/merchantpgpui/checkoutjs/merchants/${mid}.js"
            crossorigin="anonymous"></script>
    <script>
        var player;
        $(function () {
            var config = {
                "root": "",
                "flow": "DEFAULT",
                "data": {
                    "orderId": "${orderId}", /* update order id */
                    "token": "${txnToken}", /* update token value */
                    "tokenType": "TXN_TOKEN",
                    "amount": "${amount}" /* update amount  */
                },
                payMode: {
                    "order": ['UPI', 'BALANCE', 'CARD']
                    , "labels": {
                        "UPI": "UPI"
                    }
                },
                merchant: {
                    name: 'PAYTM PAY'
                },
                "handler": {
                    "notifyMerchant": function (eventName, data) {
                        console.log("eventName => ", eventName);
                        console.log("data => ", data);
                    }
                }
            };

            if (window.Paytm && window.Paytm.CheckoutJS) {
                window.Paytm.CheckoutJS.onLoad(function excecuteAfterCompleteLoad() {
                    // initialze configuration using init method
                    window.Paytm.CheckoutJS.init(config).then(function onSuccess() {
                        // after successfully updating configuration, invoke JS Checkout

                        $("#loaddiv").hide();
                        $("#payBtn").show();
                        //$("#paytm-checkoutjs").hide();
                        window.Paytm.CheckoutJS.invoke();
                    }).catch(function onError(error) {

                        $("#log").html("Error&nbsp;Msg:&nbsp;" + error.message + ",Please re-initiate payment order.");
                        $("#loaddiv").hide();
                        $("#payBtn").hide();
                        $("#paytm-checkoutjs").hide();
                    });
                });
            }


        });

        function goPay() {
            window.Paytm.CheckoutJS.invoke();
        }

        function playVideo(val, obj) {

            $(".btn").removeClass("lgbtnsel");
            $(obj).addClass("lgbtnsel");

            if (player) {
                player.stop();
                player = null;
            }
            $("#videoIfm").attr("src", "${context}/video/video.html?vd=" + val + "&vs=3");
        }


        function restartVideo() {
            loadVideo(vurl);
        }
    </script>
</c:if>

</body>
</html>
