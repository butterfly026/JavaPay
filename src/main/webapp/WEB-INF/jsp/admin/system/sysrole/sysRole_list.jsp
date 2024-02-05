<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
            <h3><b>&nbsp;系统角色管理</b></h3>
        </div>
        <div class="layui-col-xs1 layui-col-sm1 layui-col-md1 right">
            <button class="layui-btn layui-btn-sm layui-btn-normal" title="刷新页面" onclick="window.location.reload()">
                <i class="layui-icon">&#x1002;</i>
            </button>
        </div>
    </div>
</div>
<div class="admin-main">
    <fieldset class="layui-elem-field admin-search-form">
        <legend class="admin-search-title">系统角色查询</legend>
        <form id="searchForm" class="layui-form">
            <div class="layui-row admin-search-row">
                <div class="layui-col-xs4 layui-col-sm4 layui-col-md4">
                    <label class="layui-form-label">角色名</label>
                    <div class="layui-input-block">
                        <input type="text" name="name" id="name" placeholder="角色名" class="layui-input"/>
                    </div>
                </div>
                <div class="layui-col-xs4 layui-col-sm4 layui-col-md4">
                    <label class="layui-form-label">用户状态</label>
                    <div class="layui-input-block">
                        <select name="status">
                            <option value="">请选择</option>
                            <option value="1">启用</option>
                            <option value="0">禁用</option>
                        </select>
                    </div>
                </div>
                <div class="layui-col-xs4 layui-col-sm4 layui-col-md4" style="text-align:center;">
                    <button class="layui-btn layui-btn-normal" type="button" style="width:35%;" onclick="searchData();">
                        搜索
                    </button>
                    <button class="layui-btn" style="width:35%;" type="button" onclick="resetForm();">重置</button>
                </div>
            </div>
        </form>
    </fieldset>

    <div class="admin-btns">
        <adminPermission:hasPermission permission="admin:sysrole:add">
            <button class="layui-btn layui-btn-sm layui-btn-normal" onclick="add();">
                添加
            </button>
        </adminPermission:hasPermission>
        <span class="order-tag" id="order_tag">

		</span>
    </div>
    <table id="datatable" lay-filter="table"></table>

    <script type="text/html" id="tpl_status">
        {{# if(d.status && d.status=="1"){ }}
        <span class="layui-badge layui-bg-blue">启用</span>
        {{# }else{ }}
        <span class="layui-badge">禁用</span>
        {{# } }}
    </script>

    <script type="text/html" id="tpl_opera">
        <adminPermission:hasPermission permission="admin:sysrole:edit">
            <a class="layui-btn layui-btn-xs layui-btn-normal" lay-event="edit">编辑</a>
        </adminPermission:hasPermission>
        <adminPermission:hasPermission permission="admin:sysrole:delete">
            <a class="layui-btn layui-btn-xs layui-btn-danger" lay-event="del">删除</a>
        </adminPermission:hasPermission>
        <adminPermission:hasPermission permission="admin:sysrole:rolemenu">
            <a class="layui-btn layui-btn-xs" lay-event="rolemenu">分配菜单</a>
        </adminPermission:hasPermission>
        <adminPermission:hasPermission permission="admin:sysrole:roleuser">
            <a class="layui-btn layui-btn-xs" lay-event="roleuser">分配用户</a>
        </adminPermission:hasPermission>
    </script>

    <script type="text/html" id="tpl_index">
        {{ d.LAY_INDEX }}
    </script>
</div>

<script src="${context}/js/jquery.min.js"></script>
<script src="${context}/layui/layui.js?2"></script>
<script src="${context}/js/common.js?8"></script>
<script>
    var tableIns;
    var table;
    var lay_index;
    $(function () {
        var $tableheight = $(document).height() - $("#datatable").offset().top - 29;
        layui.use(['table', 'layer'], function () {
            table = layui.table;
            tableIns = table.render({
                id: 'id',
                elem: '#datatable'
                , height: $tableheight
                , url: 'list' //数据接口
                , page: true //开启分页
                , limit: 20
                , mutilsort: true
                , method: 'POST'
                , where: getFormJsonData("#searchForm")
                , request: {
                    pageName: 'pageNo' //页码的参数名称，默认：page
                    , limitName: 'pageSize' //每页数据量的参数名，默认：limit
                    , sorts: 'orders' //排序参数名，默认:sorts
                }
                , cols: [[ //表头
                    {field: 'id', title: '序号', width: 70, templet: '#tpl_index', type: 'checkbox'}
                    , {field: 'name', title: '角色名', width: 130, sort: true}
                    , {field: 'status', title: '角色状态', width: 100, toolbar: '#tpl_status'}
                    , {field: 'desc', title: '角色描述', width: 230}
                    , {fixed: 'right', align: 'center', title: '操作', toolbar: '#tpl_opera'}
                ]]
            });

            table.on('tool(table)', function (obj) {//tool event
                var data = obj.data; //获得当前行数据
                var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
                var tr = obj.tr; //获得当前行 tr 的DOM对象

                if (layEvent === 'edit') {
                    edit(data.id);
                } else if (layEvent === 'del') {
                    del(data.id);
                } else if (layEvent === 'roleuser') {
                    allocateRole(data.id);
                } else if (layEvent === 'rolemenu') {
                    allocateMenu(data.id);
                }
                //alert(data.id);
            });

            table.on('sort(table)', function (obj) {
                if (!obj.sorts) return;
                createSortView(obj.sorts);
                searchData();
            });
        });
    });

    //移除排序
    function removeSortField(field) {
        createSortView(table.removeSortField(field));
        searchData();
    }

    //搜索数据
    function searchData() {
        tableIns.reload({
            where: getFormJsonData("#searchForm")
        });
    }

    //新增数据
    function add() {
        lay_index = layer.open({
            type: 2,
            title: '新增系统用户',
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
            title: '编辑系统用户',
            skin: 'layui-layer-rim', //加上边框
            area: ['550px', '450px'], //宽高
            content: 'edit?id=' + id
        });
    }

    function del(id) {
        if (typeof id == 'undefined') {
            layer.msg("请选择需要删除的数据");
            return;
        }
        layer.confirm('您真的要删除这条数据吗？', {
            icon: 3,//询问图标
            btn: ['确定', '取消'] //按钮
        }, function (index) {
            layer.close(index);//关闭弹层
            var ids = new Array();
            ids[0] = id;

            delData(ids)
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
                    searchData();
                }
            },
            error: function (xhr, desc, err) {
                layer.msg("数据请求失败:" + desc);
                layer.close(lay_index);
            }
        });
    }

    var dlg_indx;
    var roleId = -1;

    //打开角色分配菜单页面
    function allocateMenu(id) {
        if (typeof id == 'undefined') {
            layer.msg("请选择角色");
            return;
        }
        roleId = id;
        dlg_indx = layer.open({
            type: 2,
            title: '分配菜单',
            skin: 'layui-layer-rim', //加上边框
            area: ['550px', '450px'], //宽高
            content: 'roleMenuList?roleId=' + id
        });
    }

    //选择菜单后回调
    function selectMenu(menu_ids, menu_names) {
        lay_index = layer.load(1, {
            shade: [0.1, '#fff'] //0.1透明度的白色背景
        });

        $.ajax({
            type: 'post',
            url: 'addSaveRoleMenu',
            data: {roleId: roleId, menuIds: menu_ids},
            dataType: "json",
            cache: false,
            success: function (data) {
                layer.close(lay_index);
                if (data.msg) layer.msg(data.msg);
                if (data.code == 200) {
                    layer.close(dlg_indx);
                    window.parent.refreshUserMenu();
                }
            },
            error: function (xhr, desc, err) {
                layer.msg("数据请求失败:" + desc);
                layer.close(lay_index);
            }
        });
    }

    //给角色分配用户
    function allocateRole(id) {
        if (typeof id == 'undefined') {
            layer.msg("请选择角色");
            return;
        }
        roleId = id;
        dlg_indx = layer.open({
            type: 2,
            title: '分配用户',
            skin: 'layui-layer-rim', //加上边框
            area: ['550px', '450px'], //宽高
            content: 'roleUserList?roleId=' + id
        });
    }

    //选择菜单后回调
    function selectUser(user_ids) {
        lay_index = layer.load(1, {
            shade: [0.1, '#fff'] //0.1透明度的白色背景
        });

        $.ajax({
            type: 'post',
            url: 'addSaveRoleUser',
            data: {roleId: roleId, userIds: user_ids},
            dataType: "json",
            cache: false,
            success: function (data) {
                layer.close(lay_index);
                if (data.msg) layer.msg(data.msg);
                if (data.code == 200) {
                    layer.close(dlg_indx);
                    window.parent.refreshUserMenu();
                }
            },
            error: function (xhr, desc, err) {
                layer.msg("数据请求失败:" + desc);
                layer.close(lay_index);
            }
        });
    }

    //刷新页面
    function refreshPage(msg) {
        layer.close(lay_index);
        if (msg) layer.msg(msg);
        searchData();
    }

    //重置表单
    function resetForm() {
        $('#searchForm')[0].reset();
    }

    //生成排序相关视图
    function createSortView(sorts) {
        var $str = '';
        for (var i = 0; i < sorts.length; i++) {
            $str += '<button class="layui-btn layui-btn-xs layui-btn-radius layui-btn-normal" title="单击移除此项排序" onclick="removeSortField(\'' + sorts[i].field + '\')" type="button">' + sorts[i].fieldName + '<span class="layui-badge layui-bg-gray">';
            if (sorts[i].type == "desc") {
                $str += '降序';
            } else {
                $str += '升序';
            }
            $str += '</span></button>';
        }
        $("#order_tag").html($str);
    }

</script>
</body>
</html>
