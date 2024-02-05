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
            <td width="10%"><b>上游订单号</b></td>
            <td>${order.clientSn}</td>
        </tr>

        <tr>
            <td width="10%"><b>商户/代理号</b></td>
            <td width="23%">${order.merchantNo}</td>

            <td width="10%"><b>商户/代理名</b></td>
            <td width="23%">${order.merchantName}</td>

            <td width="10%"><b>用户类型</b></td>
            <td>${order.userType==0?'商户':'代理'}</td>
        </tr>

        <tr>
            <td width="10%"><b>商户提现金额</b></td>
            <td width="23%">${order.realMoney} ${sysconfig.currency }</td>
            <td width="10%"><b>提现手续费</b></td>
            <td width="23%">${order.commission} ${sysconfig.currency }</td>

            <td width="10%"><b>实际扣减金额</b></td>
            <td>${order.amount} ${sysconfig.currency }</td>

        </tr>

        <tr>
            <td width="10%"><b>代理</b></td>
            <td width="23%">${order.proxyName}</td>

            <td width="10%"><b>代理佣金</b></td>
            <td>${order.proxyMoney} ${sysconfig.currency }</td>

        </tr>

        <tr>
            <td width="10%"><b>上游帐号</b></td>
            <td width="23%">${order.clientNo}</td>
            <td width="10%"><b>上游名</b></td>
            <td width="23%">${order.clientName}</td>

            <td width="10%"><b>上游代付金额</b></td>
            <td>${order.clientMoney} ${sysconfig.currency }</td>

        </tr>

        <tr>
            <td width="10%"><b>提现类型</b></td>
            <td width="23%">${order.cashType}</td>

            <td width="10%"><b>创建时间</b></td>
            <td width="23%">${order.createTime}</td>

            <td width="10%"><b>支付时间</b></td>
            <td>${order.payTime}</td>

        </tr>

        <tr>
            <td width="10%"><b>提现类型</b></td>
            <td width="23%">
                <c:choose>
                    <c:when test="${order.btype==0 }"><span class="layui-badge layui-bg-green">银行卡</span></c:when>
                    <c:when test="${order.btype==1 }"><span class="layui-badge layui-bg-blue">UPI账号</span></c:when>
                    <c:otherwise>其他</c:otherwise>
                </c:choose>
            </td>

            <td width="10%"><b>提现账号</b></td>
            <td width="23%">${order.cardNo}</td>

            <td width="10%"><b>银行名</b></td>
            <td>${order.bankName}</td>

        </tr>

        <tr>
            <td width="10%"><b>持卡人姓名</b></td>
            <td width="23%">${order.cardName}</td>

            <td width="10%"><b>IFSC</b></td>
            <td width="23%">${order.bankIfsc}</td>

            <td width="10%"><b>国家</b></td>
            <td>${order.bankNation}</td>

        </tr>


        <tr>
            <td width="10%"><b>支行名</b></td>
            <td width="23%">${order.bankSubName}</td>

            <td width="10%"><b>银行省份</b></td>
            <td width="23%">${order.bankProvince}</td>

            <td width="10%"><b>开户城市</b></td>
            <td>${order.bankCity}</td>

        </tr>


        <tr>
            <td width="10%"><b>订单状态</b></td>
            <td width="23%">
                <c:choose>
                    <c:when test="${order.payStatus==0 }">等待审核</c:when>
                    <c:when test="${order.payStatus==1 }"><span class="layui-badge">失败</span></c:when>
                    <c:when test="${order.payStatus==2 }"><span class="layui-badge">已拒绝</span></c:when>
                    <c:when test="${order.payStatus==3 }"><span class="layui-badge layui-bg-blue">审核通过</span></c:when>
                    <c:when test="${order.payStatus==4 }"><span class="layui-badge layui-bg-orange">处理中</span></c:when>
                    <c:when test="${order.payStatus==5 }"><span class="layui-badge layui-bg-green">已支付</span></c:when>
                    <c:otherwise>其他</c:otherwise>
                </c:choose>
            </td>

            <td width="10%"><b>代付类型</b></td>
            <td colspan="3">
                <c:choose>
                    <c:when test="${order.payType==1 }">API代付</c:when>
                    <c:when test="${order.payType==0 }">人工代付</c:when>
                </c:choose>
            </td>

        </tr>

        <tr>
            <td width="10%"><b>通知状态</b></td>
            <td width="23%">${(order.dealStatus==null || order.dealStatus==0)?'待通知':'已通知'}</td>

            <td width="10%"><b>通知次数</b></td>
            <td width="23%">${order.notifyCount}</td>

            <td width="10%"><b>通知时间</b></td>
            <td>${order.notifyTime}</td>

        </tr>

        <tr>
            <td width="10%"><b>备注信息</b></td>
            <td colspan="5">${order.remark}</td>
        </tr>
        <tr>
            <td width="10%">通知回调地址</td>
            <td colspan="5">${order.notifyUrl}</td>
        </tr>
        <tr>
            <td width="10%"><b>API备注</b></td>
            <td colspan="5">${order.apiremark}</td>
        </tr>
        </tbody>
    </table>

    <!-- 变更记录 -->

</div>
<script src="${context}/js/jquery.min.js"></script>
<script src="${context}/layui/layui.js"></script>

</body>
</html>
