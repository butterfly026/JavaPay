<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="adminPermission" uri="http://adminpermission.city.com" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>Payment</title>
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
    <c:if test="${error!=null}">
        <div style="color:red;">${error}</div>
    </c:if>
    <div>Loading...</div>
    <form method="post" action="https://securegw.paytm.in/order/process?mid=${mid}&orderId=${orderId}" name="paytm">
        <table border="1">
            <tbody>
            <input type="hidden" name="mid" value="${mid}">
            <input type="hidden" name="orderId" value="${orderId}">
            <input type="hidden" name="txnToken" id="txnToken" value="${txnToken}">
            </tbody>
        </table>
    </form>
</div>
<c:if test="${error==null}">
    <script>
        document.paytm.submit();
    </script>
</c:if>
</body>
</html>
