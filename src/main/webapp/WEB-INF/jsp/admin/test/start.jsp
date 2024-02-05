<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="adminPermission" uri="http://adminpermission.city.com" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>测试</title>
    <link rel="stylesheet" href="${context}/layui/css/layui.css">
    <link rel="stylesheet" href="${context}/layui/css/admin.css">
    <link rel="stylesheet" href="${context}/css/style.css?${s_vs}">
    <style type="text/css">
        .layuiadmin-card-list p.layuiadmin-big-font {
            font-size: 18px;
        }

        .box {
            padding: 10px;
        }

        .box .boxtitle {
            font-weight: bold;
            font-size: 12px;
        }

        .box .boxtxt {
            font-size: 12px;
        }

        .txtcolor {
            color: green;
            padding-right: 5px;
        }

        .boxline {
            width: 100;
            height: 1px;
            background-color: #e6e6e6;
            margin-top: 8px;
            margin-bottom: 8px;
        }
    </style>
    <!--[if lt IE 9]>
      <script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
      <script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body>

<div class="layui-fluid">

    <input type="text" name="money" id="money" value="1.00"/>
    <input type="button" value="付款" onclick="gotoPay()"/>
</div>

<script src="${context}/js/jquery.min.js"></script>
<script src="${context}/layui/layui.js?${s_vs}"></script>
<script src="${context}/js/common.js?${s_vs}"></script>
<script src="${context}/lib/echarts/echarts.min.js?${s_vs}"></script>
<script>
    function gotoPay() {
        var m = $("#money").val();
        if (m == "") {
            alert("金额不能为空");
            return;
        }
        window.open("order?amount=" + m);
    }
</script>
</body>
</html>
