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
            <td colspan="6"><b>订单基础信息</b></td>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td width="10%"><b>订单编号</b></td>
            <td width="23%">${order.sn}</td>
            <td width="10%"><b>商户订单号</b></td>
            <td width="23%">${order.merchantSn}</td>

            <td width="10%"><b>订单金额</b></td>
            <td>${order.money}元</td>
        </tr>
        <tr>

            <td width="10%"><b>应付金额</b></td>
            <td width="23%">${order.amount}元</td>

            <td width="10%"><b>创建时间</b></td>
            <td width="23%">${order.createTime}</td>

            <td width="10%"><b>支付时间</b></td>
            <td>${order.payTime}</td>
        </tr>
        <tr>
            <td width="10%"><b>支付平台</b></td>
            <td width="23%">${order.typeName}</td>

            <td width="10%"><b>订单状态</b></td>
            <td width="23%">${order.orderStatus==0?'待支付':'已支付'}</td>

            <td width="10%"><b>通知状态</b></td>
            <td>${(order.dealStatus==null || order.dealStatus==0)?'待通知':'已通知'}</td>
        </tr>
        <tr>
            <td width="10%"><b>通知次数</b></td>
            <td width="23%">${order.notifyCount}</td>

            <td width="10%"><b>通知时间</b></td>
            <td colspan="3">${order.notifyTime}</td>

        </tr>
        <tr>
            <td width="10%"><b>备注信息</b></td>
            <td colspan="5">${order.remark}</td>
        </tr>
        <tr>
            <td width="10%">支付链接</td>
            <td colspan="5"><a href="${order.payUrl}" target="_blank" style="color:#1E9FFF;">${order.payUrl}</a></td>
        </tr>
        <tr>
            <td width="10%">通知回调地址</td>
            <td colspan="5">${order.notifyUrl}</td>
        </tr>

        <c:if test="${order.orderStatus==0}">
            <tr>
                <td colspan="6" style="color:#ff0000;text-align: center;">
                    ↓↓↓↓↓ 本订单收入约为&nbsp;&nbsp;${order.merchantMoney}&nbsp;&nbsp;元，数据仅做参考，具体金额数据以订单成功时通道费率为准！ ↓↓↓↓↓
                </td>
            </tr>
        </c:if>
        <c:if test="${order.orderStatus==1}">
            <tr>
                <td colspan="6" style="color:#ff0000;text-align: center;">
                    本订单收入约为&nbsp;&nbsp;${order.merchantMoney}&nbsp;&nbsp;元
                </td>
            </tr>
        </c:if>
        </tbody>
    </table>

    <table class="layui-table" lay-size="sm">
        <colgroup>
            <col width="200">
            <col>
        </colgroup>
        <thead>
        <tr>
            <td colspan="6"><b>商户信息</b></td>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td width="10%"><b>商户号</b></td>
            <td width="23%">${order.merchantNo}</td>
            <td width="10%"><b>商户名</b></td>
            <td width="23%">${order.merchantName}</td>
            <td width="10%"><b>商户通道费率</b></td>
            <td>${order.merchantRatio}%</td>
        </tr>
        <tr>
            <td width="10%"><b>扣除手续费</b></td>
            <td width="23%">${order.merchantComission}元</td>
            <td width="10%"><b>订单收入金额</b></td>
            <td colspan="3">${order.merchantMoney}元</td>

        </tr>
        </tbody>
    </table>

</div>
<script src="${context}/js/jquery.min.js"></script>
<script src="${context}/layui/layui.js"></script>

</body>
</html>
