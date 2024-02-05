<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport"
          content="maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,initial-scale=1.0"/>
    <meta name="format-detection" content="telephone=no,email=no,date=no,address=no">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="keywords" content="">
    <meta name="description" content="">
    <title>Tips</title>
<%--    <script src="https://cdn.jsdelivr.net/npm/jquery@1.12.4/dist/jquery.min.js"></script>--%>
    <script src="${context}/js/jquery.min.js"></script>
    <script src="${context}/js/vconsole.min.js"></script>
    <style type="text/css">
        body {
            background: seagreen;
        }

        .sk-fading-circle {
            margin: 100px auto;
            width: 80px;
            height: 80px;
            position: relative;
        }

        .sk-fading-circle .sk-circle {
            width: 100%;
            height: 100%;
            position: absolute;
            left: 0;
            top: 0;
        }

        .sk-fading-circle .sk-circle:before {
            content: '';
            display: block;
            margin: 0 auto;
            width: 15%;
            height: 15%;
            background-color: #fff;
            border-radius: 100%;
            -webkit-animation: sk-circleFadeDelay 1.2s infinite ease-in-out both;
            animation: sk-circleFadeDelay 1.2s infinite ease-in-out both;
        }

        .sk-fading-circle .sk-circle2 {
            -webkit-transform: rotate(30deg);
            -ms-transform: rotate(30deg);
            transform: rotate(30deg);
        }

        .sk-fading-circle .sk-circle3 {
            -webkit-transform: rotate(60deg);
            -ms-transform: rotate(60deg);
            transform: rotate(60deg);
        }

        .sk-fading-circle .sk-circle4 {
            -webkit-transform: rotate(90deg);
            -ms-transform: rotate(90deg);
            transform: rotate(90deg);
        }

        .sk-fading-circle .sk-circle5 {
            -webkit-transform: rotate(120deg);
            -ms-transform: rotate(120deg);
            transform: rotate(120deg);
        }

        .sk-fading-circle .sk-circle6 {
            -webkit-transform: rotate(150deg);
            -ms-transform: rotate(150deg);
            transform: rotate(150deg);
        }

        .sk-fading-circle .sk-circle7 {
            -webkit-transform: rotate(180deg);
            -ms-transform: rotate(180deg);
            transform: rotate(180deg);
        }

        .sk-fading-circle .sk-circle8 {
            -webkit-transform: rotate(210deg);
            -ms-transform: rotate(210deg);
            transform: rotate(210deg);
        }

        .sk-fading-circle .sk-circle9 {
            -webkit-transform: rotate(240deg);
            -ms-transform: rotate(240deg);
            transform: rotate(240deg);
        }

        .sk-fading-circle .sk-circle10 {
            -webkit-transform: rotate(270deg);
            -ms-transform: rotate(270deg);
            transform: rotate(270deg);
        }

        .sk-fading-circle .sk-circle11 {
            -webkit-transform: rotate(300deg);
            -ms-transform: rotate(300deg);
            transform: rotate(300deg);
        }

        .sk-fading-circle .sk-circle12 {
            -webkit-transform: rotate(330deg);
            -ms-transform: rotate(330deg);
            transform: rotate(330deg);
        }

        .sk-fading-circle .sk-circle2:before {
            -webkit-animation-delay: -1.1s;
            animation-delay: -1.1s;
        }

        .sk-fading-circle .sk-circle3:before {
            -webkit-animation-delay: -1s;
            animation-delay: -1s;
        }

        .sk-fading-circle .sk-circle4:before {
            -webkit-animation-delay: -0.9s;
            animation-delay: -0.9s;
        }

        .sk-fading-circle .sk-circle5:before {
            -webkit-animation-delay: -0.8s;
            animation-delay: -0.8s;
        }

        .sk-fading-circle .sk-circle6:before {
            -webkit-animation-delay: -0.7s;
            animation-delay: -0.7s;
        }

        .sk-fading-circle .sk-circle7:before {
            -webkit-animation-delay: -0.6s;
            animation-delay: -0.6s;
        }

        .sk-fading-circle .sk-circle8:before {
            -webkit-animation-delay: -0.5s;
            animation-delay: -0.5s;
        }

        .sk-fading-circle .sk-circle9:before {
            -webkit-animation-delay: -0.4s;
            animation-delay: -0.4s;
        }

        .sk-fading-circle .sk-circle10:before {
            -webkit-animation-delay: -0.3s;
            animation-delay: -0.3s;
        }

        .sk-fading-circle .sk-circle11:before {
            -webkit-animation-delay: -0.2s;
            animation-delay: -0.2s;
        }

        .sk-fading-circle .sk-circle12:before {
            -webkit-animation-delay: -0.1s;
            animation-delay: -0.1s;
        }

        @-webkit-keyframes sk-circleFadeDelay {
            0%, 39%, 100% {
                opacity: 0;
            }
            40% {
                opacity: 1;
            }
        }

        @keyframes sk-circleFadeDelay {
            0%, 39%, 100% {
                opacity: 0;
            }
            40% {
                opacity: 1;
            }
        }

        p {
            margin-block-start: 12px;
            margin-block-end: 12px;
        }

        .myButton {
            margin-top: 30px;
            box-shadow: 0px 10px 14px -7px #276873;
            background: linear-gradient(to bottom, #599bb3 5%, #408c99 50%);
            background-color: #599bb3;
            border-radius: 8px;
            display: inline-block;
            cursor: pointer;
            color: #ffffff;
            font-family: Arial;
            font-size: 16px;
            font-weight: bold;
            padding: 3px 16px;
            text-decoration: none;
            text-shadow: 0px 1px 0px #3d768a;
        }

        .myButton:hover {
            background: linear-gradient(to bottom, #408c99 5%, #599bb3 100%);
            background-color: #408c99;
        }

        .myButton:active {
            position: relative;
            top: 1px;
        }

        .title {
            text-align: center;
            padding: 30px 30px;
            color: white;
        }

        .content {
            text-align: center;
            width: 80%;
            height: 300px;
            margin: auto;
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
        }

        #tips {
            color: white;
            font-size: 26px;
        }

    </style>
</head>
<body>
<div class="content">
    <div id="loading">
        <div class="title">
            支付链接匹配中，请稍等
        </div>
        <div class="sk-fading-circle">
            <div class="sk-circle1 sk-circle"></div>
            <div class="sk-circle2 sk-circle"></div>
            <div class="sk-circle3 sk-circle"></div>
            <div class="sk-circle4 sk-circle"></div>
            <div class="sk-circle5 sk-circle"></div>
            <div class="sk-circle6 sk-circle"></div>
            <div class="sk-circle7 sk-circle"></div>
            <div class="sk-circle8 sk-circle"></div>
            <div class="sk-circle9 sk-circle"></div>
            <div class="sk-circle10 sk-circle"></div>
            <div class="sk-circle11 sk-circle"></div>
            <div class="sk-circle12 sk-circle"></div>
        </div>
    </div>
    <div class="tips" id="result">
        <div id="tips"></div>
        <div class="myButton" id="btnClick" onclick="loadUrl()">
            <p>点击重新获取链接</p>
        </div>
    </div>
</div>
</div>
<script>

    // showError();
    // loadUrl();
    var vConsole = new window.VConsole();

    let error = '${error}';
    let payUrl = '${payUrl}';
    if (payUrl) {
        window.location.href = payUrl;
    } else if (error) {
        showError(error);
    } else {
        loadUrl();
    }


    function showError(error) {
        $('#btnClick').hide();
        $('#loading').hide();
        $('#result').show();
        $('#tips').text(error != null ? error : "Did not get the payment link, please try again!")
    }

    function showLoading() {
        $('#loading').show();
        $('#result').hide();
    }


    function loadUrl() {
        showLoading()
        load()
    }

    var http = {};
    http.quest = function (option, callback) {
        var url = option.url;
        var method = option.method;
        var data = option.data;
        var timeout = option.timeout || 0;
        var xhr = new XMLHttpRequest();
        (timeout > 0) && (xhr.timeout = timeout);
        xhr.onreadystatechange = function () {
            if (xhr.readyState == 4) {
                if (xhr.status >= 200 && xhr.status < 400) {
                    var result = xhr.responseText;
                    try { result = JSON.parse(xhr.responseText); } catch (e) { }
                    callback && callback(null, result);
                } else {
                    callback && callback('status: ' + xhr.status);
                }
            }
        }.bind(this);
        xhr.open(method, url, true);
        if (typeof data === 'object') {
            try {
                data = JSON.stringify(data);
            } catch (e) { }
        }
        xhr.send(data);
        xhr.ontimeout = function () {
            callback && callback('timeout');
            console.log('%c连%c接%c超%c时', 'color:red', 'color:orange', 'color:purple', 'color:green');
        };
    };

    function load() {
        let data = {
            sn: '${sn}',
            time: new Date().getTime(),
        }
        //post请求
        http.post({ url: '/api/pay/orderPayUrl', data: data, timeout: 1000 }, function (err, result) {
            // 这里对结果进行处理
            if (result.code == 200) {
                let payUrl = result.data;
                window.location.href = payUrl;
            } else if (result.code == 408) {
                setTimeout(function () {
                    showError(result.msg)
                    $('#btnClick').show();
                }, 3000)
            } else {
                setTimeout(function () {
                    showError(result.msg)
                }, 3000)
            }
        });
    }

    function success(data) {
        let error = '${error}';
        if (error) {
            $('#tips').text(error)
        } else {
            let payUrl = '${payUrl}';
            if (payUrl) {
                window.location.href = payUrl;
            } else {
                $('#tips').text("Did not get the payment link, please try again!")
            }
        }
    }


</script>


</body>
</html>
