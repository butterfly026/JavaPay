<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>编辑商户提现</title>
    <link rel="stylesheet" href="${context}/layui/css/layui.css">
    <style>
        .admin-main {
            margin: 15px;
        }

        .span-red {
            color: red;
        }
    </style>
    <!--[if lt IE 9]>
      <script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
      <script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<div class="admin-main">
    <form id="editForm" action="addSaveBatch" class="layui-form" method="post" enctype="multipart/form-data">

        <div style="color:red;width:90%;padding-left:20px;">余额：<span
                style="color:green;">${money} ${sysconfig.currency }</span>，冻结金额：<span
                style="color:green;">${frozenMoney} ${sysconfig.currency }</span>，可提现金额：<span
                style="color:green;">${amount} ${sysconfig.currency }</span>，单笔最低提现额度：<span style="color:green;"
                                                                                            id="zdtx">${minCommission}</span> ${sysconfig.currency }
        </div>
        <div style="color:red;width:90%;padding-left:20px;">手续费扣减模式：<span style="color:green;">外扣</span>。<span
                style="color:green;">如果未设置支付密码，请先去首页设置支付密码！支付密码默认为空</span></div>
        <div style="color:red;width:90%;padding-left:20px;margin-bottom:10px;">下发单笔费率：<span
                id="bankMoney">提现金额 * ${cashConfig.bankRatio.ratioCommission}%+${cashConfig.bankRatio.commission}</span>
        </div>
        <div style="color:red;width:90%;padding-left:20px;margin-bottom:10px;">代付单笔费率：<span
                id="bankMoney">提现金额 * ${cashConfig.dfbankRatio.ratioCommission}%+${cashConfig.dfbankRatio.commission}</span>
        </div>
        <!-- 批量提现导入模板下载 -->
        <div style="width:90%;padding-left:20px;margin-bottom:10px;">
            <a href="${context}/admin/daifu.xls" target="_blank" style="color:blue;">点击下载提现导入模板</a>
        </div>

        <!-- 批量提现导入 -->
        <div class="layui-form-item">
            <label class="layui-form-label"><span class="span-red">*</span>Excel文件</label>
            <div class="layui-input-block">
                <input type="file" name="efile" id="efile" lay-verify="required" lay-verType="tips"
                       placeholder="请选择Excel文件" class="layui-input"/>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label"><span class="span-red">*</span>支付密码</label>
            <div class="layui-input-block">
                <input type="password" name="payPwd" id="payPwd" lay-verify="required" lay-verType="tips"
                       placeholder="支付密码" autocomplete="off" class="layui-input"/>
            </div>
        </div>

        <div class="layui-form-item">
            <div class="layui-input-block">
                <button class="layui-btn" lay-submit lay-filter="edit">提交</button>
                <button type="reset" class="layui-btn layui-btn-primary">重置</button>
            </div>
        </div>
    </form>
</div>
<script src="${context}/js/jquery.min.js"></script>
<script src="${context}/layui/layui.js?${s_vs}"></script>
<script src="${context}/js/common.js?${s_vs}"></script>
<script>
    var errorInfo = '${errorinfo}';
    var downloadUrl = '${downloadUrl}';
    var total = '${total}';
    var suc = '${suc}';
    var error = '${error}';

    $(function () {
        layui.use(['form', 'layer'], function () {
            if (errorInfo != "") {
                layer.alert(errorInfo, {title: '提示'});
            } else {
                if (downloadUrl != '') {
                    layer.alert("共计" + total + "条,成功" + suc + "条,失败" + error + "条<br/>导入结果下载：<a href='" + downloadUrl + "' target='_blank' style='color:blue;'>下载导入结果</a>", {title: '提示'});
                    window.parent.searchData();
                }

            }
        });
    });
</script>
</body>
</html>
