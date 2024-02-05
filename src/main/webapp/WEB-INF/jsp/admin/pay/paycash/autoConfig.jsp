<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2021-7-20
  Time: 20:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link rel="stylesheet" href="${context}/layui/css/layui.css">
</head>
<body>
<form id="editForm" class="layui-form" style="margin: 0 20px 0 20px;">
    <div class="layui-form-item" style="margin-top: 20px;">
        <label class="layui-form-label" style="width: 10%">自动代付开关</label>
        <div class="layui-input-block">
            <input type="checkbox" name="autoDaiFu" lay-skin="switch" lay-text="开启|关闭"
            <c:if test="${autoDaiFu}">
                   checked
            </c:if>
            >
        </div>
    </div>
    <div style="color:red;width:90%;padding-left:20px;margin-bottom:10px;">
        当前自动代付："${curModel.id}">${curModel.username}-${curModel.name}[余额${curModel.money} ${sysconfig.currency },冻结${curModel.frozenMoney } ${sysconfig.currency },可用${curModel.amount } ${sysconfig.currency }]
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label">选择API代付</label>
        <div class="layui-input-block">
            <select name="modelId" id="modelId" lay-verify="required" lay-search lay-filter="up_keyname">
                <c:forEach items="${modelList }" var="c">
                    <option value="${c.id}">${c.username}-${c.name}[余额${c.money} ${sysconfig.currency },冻结${c.frozenMoney } ${sysconfig.currency },可用${c.amount } ${sysconfig.currency }]</option>
                </c:forEach>
            </select>
        </div>
    </div>
    <div class="layui-form-item">
        <div class="layui-input-block">
            <button class="layui-btn" lay-submit lay-filter="edit">提交</button>
        </div>
    </div>
</form>
<script src="${context}/js/jquery.min.js"></script>
<script src="${context}/layui/layui.js"></script>
<script src="${context}/js/common.js?${s_vs}"></script>
<script>
    $(function () {

        layui.use(['form', 'layer'], function () {
            var form = layui.form;
            //按钮事件
            form.on('submit(edit)', function (data) {

                layer.confirm('确定保存信息？', {
                    icon: 3,//询问图标
                    btn: ['确定', '取消'] //按钮
                }, function (index) {
                    layer.close(index);//关闭弹层

                    lay_index = layer.load(1, {
                        shade: [0.1, '#fff'] //0.1透明度的白色背景
                    });
                    $.ajax({
                        type: 'post',
                        url: "autoConfigSubmit",
                        data: getFormJsonData("#editForm"),
                        dataType: "json",
                        cache: false,
                        success: function (data) {
                            layer.close(lay_index);
                            if (data.code == 200) {
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

                }, function (index) {
                    layer.close(index);
                });


                return false;
            });

            initSelectAll();
        });


    });
</script>
</body>
</html>
