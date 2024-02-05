<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>批量通知商户</title>
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

        <div class="layui-form-item">
            <label class="layui-form-label"><span class="span-red">*</span>开始时间</label>
            <div class="layui-input-block">
                <input type="text" name="startTime" id="startTime" lay-verify="required" lay-verType="tips"
                       placeholder="开始时间" autocomplete="off" class="layui-input"/>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label"><span class="span-red">*</span>结束时间</label>
            <div class="layui-input-block">
                <input type="text" name="endTime" id="endTime" lay-verify="required|dateValid" lay-verType="tips"
                       placeholder="结束时间" autocomplete="off" class="layui-input"/>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label"><span class="span-red">*</span>商户</label>
            <div class="layui-input-block">
                <select name="merchantId" id="merchantId" lay-search>
                    <option value="">所有商户</option>
                    <c:forEach items="${modelList }" var="c">
                        <option value="${c.id}">${c.username} [ ${c.name} ]</option>
                    </c:forEach>
                </select>
            </div>
        </div>

        <div class="layui-form-item">
            <div class="layui-input-block">
                <button class="layui-btn" lay-submit lay-filter="edit">批量通知商户</button>
                <button type="reset" class="layui-btn layui-btn-primary">重置</button>
            </div>
        </div>

    </form>


</div>
<script src="${context}/js/jquery.min.js"></script>
<script src="${context}/layui/layui.js"></script>
<script src="${context}/js/common.js"></script>
<script>
    var lay_index;
    var path = "startBatchNotifyMerchant";

    $(function () {

        layui.use(['form', 'layer', 'laydate'], function () {
            var form = layui.form;

            var laydate = layui.laydate;

            laydate.render({
                elem: '#startTime' //指定元素
                , type: "datetime"
                , format: 'yyyy-MM-dd HH:mm:ss'
            });

            laydate.render({
                elem: '#endTime' //指定元素
                , type: "datetime"
                , format: 'yyyy-MM-dd HH:mm:ss'
            });

            //按钮事件
            form.on('submit(edit)', function (data) {
                lay_index = layer.load(1, {
                    shade: [0.1, '#fff'] //0.1透明度的白色背景
                });
                $.ajax({
                    type: 'post',
                    url: path,
                    data: getFormJsonData("#editForm"),
                    dataType: "json",
                    cache: false,
                    success: function (data) {
                        layer.close(lay_index);
                        if (data.code == 200) {
                            window.parent.refreshPage(data.msg);
                        } else {
                            layer.msg(data.msg);
                        }
                    },
                    error: function (xhr, desc, err) {
                        layer.msg("数据请求失败:" + desc);
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
