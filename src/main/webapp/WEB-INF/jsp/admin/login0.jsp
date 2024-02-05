<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML>
<html>
<head>
    <meta charset="UTF-8">
    <title>${sysconfig.cname}</title>
    <link rel="shortcut icon" href="/favicon.ico" type="image/x-icon"/>
    <link rel="stylesheet" href="${context}/admin/login/style.css?1">
    <style>
        .draw {
            position: fixed;
            width: 1px;
            line-height: 1px;
            pointer-events: none;
        }

        @keyframes floatOne {
            0% {
                opacity: 1;
            }
            50% {
                opacity: 1;
            }
            100% {
                opacity: 0;
                transform: translate3D(0, -20px, 0) scale(5) rotate(45deg);
            }
        }
    </style>
</head>
<body style="background-color: #ccc">
<!--
background: url(${context}/admin/login/bg${bg}.jpg) no-repeat;background-size: cover;-webkit-background-size: cover;-o-background-size: cover;
<div style="position:absolute;height:100%;width:100%;top:0;left:0;z-index:-1;">
<img src="${context}/admin/login/bg${bg}.jpg" style="width:100%;height:100%;"/>
</div>
 -->
<img src="${context}${bg}" style="display:none;" id="imgdata"/>

<div class="lg-container">
    <h1>登录后台</h1>
    <form id="lg-form" name="lg-form" method="post">

        <div style="display: flex">
            <input type="text" name="username" id="username" placeholder="请输入用户名" title="用户名"/>
        </div>

        <div style="display: flex">
            <input type="password" name="password" id="password" placeholder="请输入密码" title="密码"/>
        </div>

        <div style="display: flex">
            <input type="text" name="key" id="key" placeholder="动态口令,未设置请为空" title="动态口令"/>
        </div>

        <div id="validate" <c:if test="${validateCode==null}">style="display: none;"</c:if>>
            <div style="width:50%;float:left;">
                <input type="text" name="validCode" id="validCode" placeholder="验证码" style="width:100%;"/>
            </div>
            <div style="width:40%;float:right;">
                <img alt="验证码" src="${validateCode}" id="validateImage" onclick="updateValidateCode();">
            </div>
        </div>
        <div style="clear: both;height:1px;margin:0px;padding:0px;"></div>
        <div style="text-align:center;">
            <button type="button" onclick="login()">登录系统</button>
        </div>

    </form>
</div>
<script type="text/javascript" src="${context}/js/jquery.min.js"></script>
<script src="${context}/admin/login/jquery.placeholder.min.js"></script>
<script src="${context}/layui/layui.js"></script>

<script type="text/javascript">
    var imgurl = "${context}${bg}";
    var img_w = 0, img_h = 0;

    $(function () {
        $('input, textarea').placeholder();
        layui.use(['layer'], function () {
        });
        $("body").keydown(function (event) {
            if (event.keyCode == "13") {//keyCode=13是回车键
                login();
            }
        });


        // var bgImage = new Image();
        // bgImage.src = imgurl;
        //
        // bgImage.onload = function () {
        //     showBgimg();
        // };
        //
        // $(window).resize(function () {
        //     showBgimg();
        // });
    });
    if (window != top) {
        top.location.href = location.href;
    }

    // function showBgimg() {
    //     var sw = $(window).width();
    //     var sh = $(window).height();
    //
    //     $("body").attr("style", "background:url(" + imgurl + ") no-repeat;background-size:" + sw + "px " + sh + "px ;");
    // }

    var load_index = 0;

    function login() {
        var userName = $("#username").val();
        var pwd = $("#password").val();
        var validCode = $("#validCode").val();
        if (userName == "" || pwd == "") {
            layer.msg("用户名和密码不能为空！", {time: 1000});
            return;
        }
        load_index = layer.load(1, {
            shade: [0.1, '#fff'] //0.1透明度的白色背景
        });

        $.ajax({
            type: 'post',
            url: 'login',
            data: {userName: userName, pwd: pwd, validCode: validCode, key: $("#key").val()},
            dataType: "json",
            cache: false,
            success: function (data) {
                layer.close(load_index);
                layer.msg(data.msg, {time: 1000});
                if (data.code == 200) {//登录成功
                    window.location.href = "main";
                } else {//has code?
                    if (data.result) {
                        $("#validateImage").attr("src", data.result);
                        $("#validate").show();
                    }
                }
            },
            error: function (xhr, desc, err) {
                layer.msg("数据请求失败:" + desc, {time: 1000});
                layer.close(load_index);
            }
        });
    }

    function updateValidateCode() {
        $("#validateImage").attr("src", "validateImage?" + (new Date().getTime()));
    }
</script>
</body>
</html>
