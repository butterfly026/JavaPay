<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>分配收款/上游通道</title>
    <link rel="stylesheet" href="${context}/layui/css/layui.css">
    <link rel="stylesheet" href="${context}/css/style.css?${s_vs}">

    <script src="${context}/js/jquery.min.js"></script>
    <style>
        .admin-tree {
            padding-top: 5px;
            padding-left: 15px;
            text-align: left;
        }

        .admin-btn {
            position: fixed;
            left: 0px;
            right: 0;
            bottom: 0;
            height: 44px;
            line-height: 44px;
            padding: 0 15px;
            background-color: #eee;
            z-index: 999;
            text-align: center;
        }
    </style>
</head>
<body>
<div class="admin-main">
    <!--
        <div>
            模式：轮询，权重，优先级
        </div>
    -->
    <table class="layui-table" lay-size="sm">
        <colgroup>
            <col width="50">
            <col width="120">
            <col width="120">
            <col width="120">
            <col width="100">
            <col>
        </colgroup>
        <thead>
        <tr>
            <th><input type="checkbox" name="ids" id="ids"/></th>
            <th>上游账号</th>
            <th>通道名</th>
            <th>通道类型</th>
            <th>费率</th>
            <th>备注</th>
            <th>优先级</th>
        </tr>
        </thead>
        <tbody>

        <c:forEach items="${ccList}" var="role">
            <tr>
                <td>
                    <input type="checkbox" name="id" value="${role.id}"/>
                </td>
                <td>${role.clientNo}</td>
                <td>${role.name }</td>
                <td>${role.typeName}</td>
                <td>${role.ratio}</td>
                <td>${role.info}</td>
                <th>
                    <input type="text" name="pr_${role.id}" id="pr_${role.id}" value="${role.priority}"/>
                </th>
            </tr>
        </c:forEach>

        </tbody>
    </table>
    <div style="height:44px;"></div>

    <div class="admin-btn">
        <div class="layui-input-block">
            <button class="layui-btn" type="button" onclick="selectRole()">确定选择</button>
            <button type="button" class="layui-btn layui-btn-primary" onclick="closeView()">取消</button>
        </div>
    </div>
</div>
<script type="text/javascript">
    $(function () {
        var roleIds = ",${ccIds},";
        var ids = document.getElementsByName("id");
        for (var i = 0; i < ids.length; i++) {
            if (roleIds.indexOf("," + ids[i].value + ",") != -1) {
                ids[i].checked = true;
            }
        }

        $("#ids").click(function (e) {
            if ($(this).is(":checked")) {
                $("input[name='id']").prop('checked', true);
            } else {
                $("input[name='id']").prop('checked', false);
            }
        });
    });

    //获取选择的角色，返回父页面
    function selectRole() {
        //var checkIds=new Array();

        var data = new Array();

        $('input[name="id"]:checked').each(function () {
            //checkIds.push($(this).val());
            var val = $("#pr_" + $(this).val()).val();
            data[data.length] = {channelId: $(this).val(), priority: val};
        });
        //window.parent.selectClientChannel(checkIds);
        window.parent.selectClientChannel(JSON.stringify(data));
    }

    //关闭视图
    function closeView() {
        var index = parent.layer.getFrameIndex(window.name);
        parent.layer.close(index);
    }
</script>
</body>
</html>
