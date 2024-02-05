<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>订单详情</title>
    <link rel="stylesheet" href="${context}/layui/css/layui.css">
    <style>
        .admin-main {
            margin: 15px;
        }

        .span-red {
            color: red;
        }

        td {
            word-break: break-all;
        }
    </style>
    <!--[if lt IE 9]>
      <script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
      <script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<div class="admin-main">
    <table class="layui-table" lay-size="sm">
        <colgroup>
            <col width="200">
            <col>
        </colgroup>
        <thead>
        <tr>
            <td colspan="6"><b>基础信息</b></td>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td width="10%"><b>创建时间</b></td>
            <td width="23%">${data.createTime}</td>
            <td width="10%"><b>日志类型</b></td>
            <td width="23%">
                <c:choose>
                    <c:when test="${data.type==1}">支付订单请求</c:when>
                    <c:when test="${data.type==2}">支付订单上游回调</c:when>
                    <c:when test="${data.type==3}">支付订单通知下游</c:when>
                    <c:when test="${data.type==4}">上游API提现请求</c:when>
                    <c:when test="${data.type==5}">API提现上游回调</c:when>
                    <c:when test="${data.type==6}">提现通知下游</c:when>
                    <c:when test="${data.type==7}">商户API提现请求</c:when>
                </c:choose>


            </td>
            <td width="10%"><b>订单类型</b></td>
            <td>
                <c:choose>
                    <c:when test="${data.snType==0}">支付订单</c:when>
                    <c:when test="${data.snType==1}">提现</c:when>
                </c:choose>
            </td>
        </tr>
        <tr>
            <td width="10%"><b>操作人</b></td>
            <td width="23%">${data.userName}</td>
            <td width="10%"><b>日志描述</b></td>
            <td colspan="3">${data.descript}</td>
        </tr>

        <tr>
            <td width="10%"><b>请求数据</b></td>
            <td colspan="5">${data.requestData}</td>
        </tr>

        <tr>
            <td width="10%"><b>接收数据</b></td>
            <td colspan="5">${data.responseData}</td>
        </tr>

        </tbody>
    </table>

    <table class="layui-table" lay-size="sm">
        <colgroup>
            <col width="200">
            <col>
        </colgroup>
        <thead>
        <tr>
            <td colspan="6"><b>其他数据</b></td>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td width="10%"><b>上游账号</b></td>
            <td width="23%">${data.clientNo}</td>
            <td width="10%"><b>上游名</b></td>
            <td width="23%">${data.clientName}</td>
            <td width="10%"><b>上游通道名</b></td>
            <td>${data.clientChannelName}</td>
        </tr>
        <tr>
            <td width="10%"><b>商户账号</b></td>
            <td width="23%">${data.merchantNo}</td>
            <td width="10%"><b>商户名</b></td>
            <td width="23%">${data.merchantName}</td>
            <td width="10%"><b>商户通道名</b></td>
            <td>${data.merchantChannelName}</td>
        </tr>
        </tbody>
    </table>

</div>
<script src="${context}/js/jquery.min.js"></script>
<script src="${context}/layui/layui.js"></script>

</body>
</html>
