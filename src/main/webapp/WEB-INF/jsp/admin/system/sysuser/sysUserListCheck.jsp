<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>选择用户</title>
    <link rel="stylesheet" href="${context}/layui/css/layui.css">
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
        }
    </style>
</head>
<body>
<div class="admin-tree">
    <c:forEach items="${userList}" var="user">
        <input type="checkbox" name="id" value="${user.id}"/>${user.username}(${user.name})<br/>
    </c:forEach>
    <div style="height:44px;"></div>
</div>
<div class="admin-btn">
    <div class="layui-input-block">
        <button class="layui-btn" type="button" onclick="selectUser()">确定选择</button>
        <button type="button" class="layui-btn layui-btn-primary" onclick="closeView()">取消</button>
    </div>
</div>
<script type="text/javascript">
    $(function () {
        var userIds = ",${userIds},";
        var ids = document.getElementsByName("id");
        for (var i = 0; i < ids.length; i++) {
            if (userIds.indexOf("," + ids[i].value + ",") != -1) {
                ids[i].checked = true;
            }
        }
    });

    //获取选择的用户，返回父页面
    function selectUser() {
        var checkIds = new Array();
        $('input[name="id"]:checked').each(function () {
            checkIds.push($(this).val());
        });
        window.parent.selectUser(checkIds);
    }
</script>
</body>
</html>
