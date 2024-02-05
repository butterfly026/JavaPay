<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>选择菜单</title>
    <link rel="stylesheet" href="${context}/layui/css/layui.css">
    <link rel="stylesheet" type="text/css" href="${context}/lib/zTree/v3/css/zTreeStyle/zTreeStyle.css"/>
    <script src="${context}/js/jquery.min.js"></script>
    <script type="text/javascript" src="${context}/lib/zTree/v3/js/jquery.ztree.core.min.js"></script>
    <script type="text/javascript" src="${context}/lib/zTree/v3/js/jquery.ztree.excheck.min.js"></script>
    <style>
        .admin-tree {
            padding: 5px;
            text-align: center;
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
    <ul id="menus_div" class="ztree"></ul>
    <div style="height:44px;"></div>
</div>
<div class="admin-btn">
    <div class="layui-input-block">
        <button class="layui-btn" type="button" onclick="selectMenu()">确定选择</button>
        <button type="button" class="layui-btn layui-btn-primary" onclick="closeView()">取消</button>
    </div>
</div>

<script type="text/javascript">
    var select_ids = "${menuIds}";//初始选中的菜单
    var menu_ids = new Array();//勾选的菜单ID
    var menu_names = new Array();//勾选的菜单名
    $(function () {
        var zTree;
        var demoIframe;
        var setting = {
            check: {
                enable: true
            },
            data: {
                simpleData: {
                    enable: true
                }
            }, callback: {
                onCheck: function (e, treeId, treeNode) {
                    var treeObj = $.fn.zTree.getZTreeObj("menus_div");
                    var nodes = treeObj.getCheckedNodes(true);
                    menu_ids = new Array();
                    for (var i = 0; i < nodes.length; i++) {
                        menu_ids[i] = nodes[i].id;
                        menu_names[i] = nodes[i].name;
                    }
                }
            }
        };
        var zNodes = [
            <c:forEach items="${menuList}" var="menu">
            {
                id:${menu.id},
                pId: '${menu.fid==null?0:menu.fid}',
                name: "${menu.name}",
                open: true,
                icon: "${menu.image==null?'':menu.image}"
            },
            </c:forEach>
            {id: 0, pId: -1, name: "系统主目录", open: true, icon: ""}
        ];
        $.fn.zTree.init($("#menus_div"), setting, zNodes);

        if (select_ids != "") {
            var treeObj = $.fn.zTree.getZTreeObj("menus_div");
            var str = select_ids.split(",");
            for (var i = 0; i < str.length; i++) {
                var node = treeObj.getNodeByParam("id", str[i]);
                treeObj.checkNode(node, true, false);
            }
        }

    });

    //选中菜单
    function selectMenu() {
        window.parent.selectMenu(menu_ids, menu_names);
    }

    //关闭视图
    function closeView() {
        var index = parent.layer.getFrameIndex(window.name);
        parent.layer.close(index);
    }
</script>
</body>
</html>
