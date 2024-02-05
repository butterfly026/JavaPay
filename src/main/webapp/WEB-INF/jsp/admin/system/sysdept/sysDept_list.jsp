<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="adminPermission" uri="http://adminpermission.city.com" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>查询页面</title>
    <link rel="stylesheet" href="${context}/layui/css/layui.css">
    <link rel="stylesheet" href="${context}/css/style.css">
    <!--[if lt IE 9]>
      <script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
      <script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<div class="layui-fluid admin-head-title">
    <div class="layui-row left">
        <div class="layui-col-xs11 layui-col-sm11 layui-col-md11">
            <h3><b>&nbsp;系统部门管理</b></h3>
        </div>
        <div class="layui-col-xs1 layui-col-sm1 layui-col-md1 right">
            <button class="layui-btn layui-btn-sm layui-btn-normal" title="刷新页面" onclick="window.location.reload();">
                <i class="layui-icon">&#x1002;</i>
            </button>
        </div>
    </div>
</div>
<div class="admin-main">

    <div class="admin-btns">
        <adminPermission:hasPermission permission="admin:sysdept:add">
            <button class="layui-btn layui-btn-sm layui-btn-normal" onclick="add();">
                添加
            </button>
        </adminPermission:hasPermission>
    </div>
    <table id="datatable" lay-filter="table" class="layui-table" lay-size="sm">
        <colgroup>
            <col width="300">
            <col width="80">
            <col>
            <col width="250">
        </colgroup>
        <thead>
        <tr>
            <th>部门名</th>
            <th>序号</th>
            <th>描述</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody id="table-list">

        </tbody>
    </table>
</div>

<script src="${context}/js/jquery.min.js"></script>
<script src="${context}/layui/layui.js?1"></script>
<script src="${context}/js/common.js?8"></script>
<script>
    var menuTreeData;
    var lay_index;
    var dlg_index;

    //菜单树对象
    function menuTree(data, childs) {//数据，子对象
        this.data = data,
            this.childs = childs
    }

    $(function () {
        layui.use(['layer'], function () {
            loadData();
        });
    });

    //新增数据
    function add() {
        lay_index = layer.open({
            type: 2,
            title: '新增系统部门',
            skin: 'layui-layer-rim', //加上边框
            area: ['550px', '450px'], //宽高
            content: 'add'
        });
    }

    //编辑数据
    function edit(id) {
        if (typeof id == 'undefined') {
            layer.msg("请选择需要编辑的数据");
            return;
        }
        lay_index = layer.open({
            type: 2,
            title: '编辑系统部门',
            skin: 'layui-layer-rim', //加上边框
            area: ['550px', '450px'], //宽高
            content: 'edit?id=' + id
        });
    }

    //删除数据
    function del(id) {
        if (typeof id == 'undefined') {
            layer.msg("请选择需要删除的数据");
            return;
        }
        layer.confirm('您真的要删除这个部门吗？如果包含子部门，子部门会一起删除。', {
            icon: 3,//询问图标
            btn: ['确定', '取消'] //按钮
        }, function (index) {
            layer.close(index);//关闭弹层
            var ids = new Array();
            ids = menuTreeChildsId(menuTreeData, id);
            ids[ids.length] = id;

            delData(ids);
        }, function (index) {
            layer.close(index);
        });
    }

    //真正的删除事件
    function delData(ids) {
        lay_index = layer.load(1, {
            shade: [0.1, '#fff'] //0.1透明度的白色背景
        });
        $.ajax({
            type: 'post',
            url: 'delete',
            data: {ids: ids},
            dataType: "json",
            cache: false,
            success: function (data) {
                layer.close(lay_index);
                if (data.msg) layer.msg(data.msg);
                if (data.code == 200) {
                    //移除id关联的元素
                    for (var i = 0; i < ids.length; i++) {
                        $("#" + ids[i]).remove();
                    }
                }
            },
            error: function (xhr, desc, err) {
                layer.msg("数据请求失败:" + desc);
                layer.close(lay_index);
            }
        });
    }

    var deptId = -1;

    //分配用户
    function allocateUser(id) {
        if (typeof id == 'undefined') {
            layer.msg("请选择部门");
            return;
        }
        deptId = id;
        dlg_index = layer.open({
            type: 2,
            title: '分配用户',
            skin: 'layui-layer-rim', //加上边框
            area: ['550px', '450px'], //宽高
            content: 'deptUserList?deptId=' + id
        });
    }

    //分配用户子页面回调，获取选择的用户
    function selectUser(userIds) {
        lay_index = layer.load(1, {
            shade: [0.1, '#fff'] //0.1透明度的白色背景
        });

        $.ajax({
            type: 'post',
            url: 'addSaveDeptUser.action',
            data: {deptId: deptId, userIds: userIds},
            dataType: "json",
            cache: false,
            success: function (data) {
                layer.close(lay_index);
                if (data.msg) layer.msg(data.msg);
                if (data.code == 200) {
                    layer.close(lay_index);
                }
            },
            error: function (xhr, desc, err) {
                layer.msg("数据请求失败:" + desc);
                layer.close(lay_index);
            }
        });
    }

    //重新载入这个页面
    function refreshPage(msg) {
        layer.close(lay_index);
        if (msg) layer.msg(msg);
        loadData();
    }

    //加载数据
    function loadData() {
        lay_index = layer.load(1, {
            shade: [0.1, '#fff'] //0.1透明度的白色背景
        });
        //从后台获取所有菜单数据
        $.ajax({
            type: 'post',
            url: 'list',
            data: {},
            dataType: "json",
            cache: false,
            success: function (data) {
                layer.close(lay_index);
                if (data.code == 200) {
                    //处理result里的数据，将其生成菜单树
                    var $result = data.result;
                    menuTreeData = createMenuTrees($result, null);
                    //alert(JSON.stringify(mts));
                    //创建表格
                    $("#table-list").html(createTable(menuTreeData, ""));

                    $("tr.parent").css("cursor", "pointer")//添加一个样式
                        .bind("click", function () {//得到所在的那行表格并添加单击事件
                            //获取当前的子元素状态，如果是隐藏，显示该元素。如果是显示，递归隐藏所有子元素
                            var $childs = $(this).siblings(".child_" + this.id);
                            $childs.each(function () {
                                var flag = $(this).is(":hidden");
                                if (flag) {
                                    $(this).show();
                                } else {
                                    $(this).hide();
                                    childElementHide($(this), this.id);
                                }
                            });
                        });
                    //阻止元素事件冒泡
                    $("tr").find("a").click(function (event) {
                        event.stopPropagation();
                    });
                } else {
                    if (data.msg) layer.msg(data.msg);
                }
            },
            error: function (xhr, desc, err) {
                layer.close(lay_index);
                layer.msg("数据请求失败:" + desc);
            }
        });
    }

    //获取当前菜单树下的所有子菜单ID
    function menuTreeChildsId(mtrees, fid) {
        if (!mtrees || mtrees.length == 0) {
            return new Array();
        }
        var mdata = new Array();
        var ls_data = new Array();
        for (var i = 0; i < mtrees.length; i++) {
            if (mtrees[i].data.fid == fid) {
                mdata[mdata.length] = mtrees[i].data.id;
                ls_data = menuTreeChildsId(mtrees[i].childs, mtrees[i].data.id);
                for (var j = 0; j < ls_data.length; j++) {
                    mdata.push(ls_data[j]);
                }
            } else {
                ls_data = menuTreeChildsId(mtrees[i].childs, fid);
                for (var j = 0; j < ls_data.length; j++) {
                    mdata.push(ls_data[j]);
                }
            }
        }
        return mdata;
    }

    //根据父id获取菜单树
    function createMenuTrees(arrs, fid) {
        var mtrees = new Array();
        var len = 0;
        if (arrs) {
            for (var i = 0; i < arrs.length; i++) {
                if (arrs[i].fid == fid) {
                    mtrees[len] = new menuTree(arrs[i], createMenuTrees(arrs, arrs[i].id));
                    len++;
                }
            }
        }
        return mtrees;
    }

    //创建表格
    function createTable(mtrees, nprev) {
        var $str = '';
        for (var i = 0; i < mtrees.length; i++) {
            $str += '<tr class="parent child_' + nullStr(mtrees[i].data.fid) + '" id="' + mtrees[i].data.id + '">';
            $str += '<td>' + nprev;
            if (mtrees[i].childs && mtrees[i].childs.length > 0) {
                $str += '<i class="layui-icon" style="font-size:10px;">&#xe623;</i>';
            }
            $str += '&nbsp;<span style="color:#6A6AFF;">' + nullStr(mtrees[i].data.name) + '</span></td>';
            $str += '<td>' + nullStr(mtrees[i].data.order) + '</td>';
            $str += '<td>' + nullStr(mtrees[i].data.desc) + '</td>';

            $str += '<td>';
            <adminPermission:hasPermission permission="admin:sysdept:edit">
            $str += '<a class="layui-btn layui-btn-xs layui-btn-normal" href="javascript:void(0);" onclick="edit(\'' + mtrees[i].data.id + '\')">编辑</a>';
            </adminPermission:hasPermission>
            <adminPermission:hasPermission permission="admin:sysdept:delete">
            $str += '&nbsp;&nbsp;<a class="layui-btn layui-btn-xs layui-btn-danger" href="javascript:void(0);" onclick="del(\'' + mtrees[i].data.id + '\')">删除</a>';
            </adminPermission:hasPermission>
            <adminPermission:hasPermission permission="admin:sysdept:deptuser">
            $str += '&nbsp;&nbsp;<a class="layui-btn layui-btn-xs">用户</a>';
            </adminPermission:hasPermission>
            $str += '</td>';
            $str += '</tr>';
            if (mtrees[i].childs && mtrees[i].childs.length > 0) {//如果包含子选项
                $str += createTable(mtrees[i].childs, nprev + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
            }
        }
        return $str;
    }

    //将空对象转换成空字符串
    function nullStr(obj) {
        if (obj) return obj;
        return "";
    }

    //隐藏子元素
    function childElementHide(elem, id) {
        var $elems = elem.siblings(".child_" + id);
        $elems.each(function () {
            $(this).hide();
            childElementHide($(this), this.id);
        });
    }
</script>
</body>
</html>
