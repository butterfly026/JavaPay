<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>充值详情</title>
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
            <td colspan="6"><b>充值订单基础信息</b></td>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td width="10%"><b>订单编号</b></td>
            <td width="23%">${order.sn}</td>
            <td width="10%"><b>商户号</b></td>
            <td width="23%">${order.merchantNo}</td>
            <td width="10%"><b>商户名</b></td>
            <td>${order.merchantName}</td>
        </tr>

        <tr>
            <td width="10%"><b>充值金额</b></td>
            <td width="23%">${order.money} ${sysconfig.currency }</td>

            <td width="10%"><b>账号类型</b></td>
            <td width="23%">
                <c:choose>
                    <c:when test="${order.accType==0 }"><span class="layui-badge layui-bg-green">银行卡</span></c:when>
                    <c:when test="${order.accType==1 }"><span class="layui-badge layui-bg-blue">UPI账号</span></c:when>
                    <c:otherwise>其他</c:otherwise>
                </c:choose>
            </td>

            <td width="10%"><b>订单状态</b></td>
            <td>
                <c:choose>
                    <c:when test="${order.status==0 }">发起充值</c:when>
                    <c:when test="${order.status==1 }"><span class="layui-badge">已拒绝</span></c:when>
                    <c:when test="${order.status==2 }"><span class="layui-badge layui-bg-blue">待充值</span></c:when>
                    <c:when test="${order.status==3 }"><span class="layui-badge layui-bg-orange">已充值</span></c:when>
                    <c:when test="${order.status==4 }"><span class="layui-badge layui-bg-green">充值成功</span></c:when>
                    <c:when test="${order.status==5 }"><span class="layui-badge">充值失败</span></c:when>
                    <c:otherwise>其他</c:otherwise>
                </c:choose>
            </td>
        </tr>

        <tr>
            <td width="10%"><b>卡号</b></td>
            <td width="23%">${order.cardNo}</td>
            <td width="10%"><b>持卡人姓名</b></td>
            <td width="23%">${order.cardName}</td>

            <td width="10%"><b>银行名</b></td>
            <td>${order.bankName}</td>

        </tr>

        <tr>
            <td width="10%"><b>IFSC</b></td>
            <td width="23%">${order.bankIfsc}</td>

            <td width="10%"><b>国家</b></td>
            <td>${order.bankNation}</td>

        </tr>

        <tr>
            <td width="10%"><b>备注信息</b></td>
            <td colspan="5">${order.remark}</td>
        </tr>

        </tbody>
    </table>

    <!-- 变更记录 -->

</div>
<script src="${context}/js/jquery.min.js"></script>
<script src="${context}/layui/layui.js"></script>

</body>
</html>
