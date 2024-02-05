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
    <form id="editForm" class="layui-form">
        <input type="hidden" name="ids" value="${ids}">

        <div style="color:red;width:90%;padding-left:20px;margin-bottom:10px;">
            总计提现金额：${realMoney} ${sysconfig.currency }，总手续费：${commission} ${sysconfig.currency }，实际扣款：${amount} ${sysconfig.currency }</div>

        <div class="layui-form-item">
            <label class="layui-form-label"><span class="span-red">*</span>代付方式</label>
            <div class="layui-input-block">
                <select name="payType" id="payType" lay-verify="required" lay-search>
                    <option value="1">API代付</option>
                </select>
            </div>
        </div>

        <div class="layui-form-item" id="rg_client1">
            <label class="layui-form-label"><span class="span-red">*</span>API上游</label>
            <div class="layui-input-block">
                <select name="modelId" id="modelId" lay-verify="required" lay-search lay-filter="up_keyname">
                    <c:forEach items="${modelList }" var="c">
                        <option value="${c.id}">${c.username}-${c.name}[余额${c.money} ${sysconfig.currency },冻结${c.frozenMoney } ${sysconfig.currency },可用${c.amount } ${sysconfig.currency }]</option>
                    </c:forEach>
                </select>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">备注</label>
            <div class="layui-input-block">
                <textarea name="remark" id="remark" placeholder="备注" class="layui-textarea"></textarea>
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
    var lay_index;
    var path = "addSave";
    var addFlag = true;//是否为新增

    var bankProvince = "${cash.bankProvince }";
    var bankCity = "${cash.bankCity }";

    $(function () {

        layui.use(['form', 'layer'], function () {
            var form = layui.form;

            //按钮事件
            form.on('submit(edit)', function (data) {
                lay_index = layer.load(1, {
                    shade: [0.1, '#fff'] //0.1透明度的白色背景
                });
                $.ajax({
                    type: 'post',
                    url: "batchCashSubmit",
                    data: getFormJsonData("#editForm"),
                    dataType: "json",
                    cache: false,
                    success: function (data) {
                        layer.close(lay_index);
                        if (data.code == 200) {
                            alert(data.msg + " " + data.result);
                            window.parent.refreshPage(data.msg);
                        } else {
                            layer.alert(data.msg, {icon: 5});
                        }
                    },
                    error: function (xhr, desc, err) {
                        layer.alert("数据请求失败:" + desc, {icon: 5});
                        layer.close(lay_index);
                    }
                });
                return false;
            });

            initSelectAll();
        });


    });

</script>
</body>
</html>
