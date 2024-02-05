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
            <h3><b>&nbsp;系统用户管理</b></h3>
        </div>
        <div class="layui-col-xs1 layui-col-sm1 layui-col-md1 right">
            <button class="layui-btn layui-btn-sm layui-btn-normal" title="刷新页面" onclick="window.location.reload();">
                <i class="layui-icon">&#x1002;</i>
            </button>
        </div>
    </div>
</div>
<div class="admin-main">
    <fieldset class="layui-elem-field admin-search-form">
        <legend class="admin-search-title">系统用户查询</legend>
        <form id="searchForm" class="layui-form">
            <div class="layui-row admin-search-row">
                <div class="layui-col-xs4 layui-col-sm4 layui-col-md4">
                    <label class="layui-form-label">登录名</label>
                    <div class="layui-input-block">
                        <input type="text" name="username" id="username" placeholder="登录名" class="layui-input"/>
                    </div>
                </div>
                <div class="layui-col-xs4 layui-col-sm4 layui-col-md4">
                    <label class="layui-form-label">手机号码</label>
                    <div class="layui-input-block">
                        <input type="text" name="phone" id="phone" placeholder="手机号码" class="layui-input"/>
                    </div>
                </div>
                <div class="layui-col-xs4 layui-col-sm4 layui-col-md4">
                    <label class="layui-form-label">用户名称</label>
                    <div class="layui-input-block">
                        <input type="text" name="name" id="name" placeholder="用户名称" class="layui-input"/>
                    </div>
                </div>
            </div>
            <div class="layui-row" style="padding-bottom:5px;">
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
        <adminPermission:hasPermission permission="admin:sysuser:add">
            <button class="layui-btn layui-btn-sm layui-btn-normal" onclick="add();">
                添加
            </button>
        </adminPermission:hasPermission>

        <button class="layui-btn layui-btn-sm layui-btn-normal" onclick="exportExcel();">
            导出Excel
        </button>

        <adminPermission:hasPermission permission="admin:sysuser:delete">
            <button class="layui-btn layui-btn-sm layui-btn-danger" onclick="batchDelete();">
                批量删除
            </button>
        </adminPermission:hasPermission>


        <span class="order-tag" id="order_tag">

		</span>
    </div>
    <div style="clear: both;"></div>
    <table id="datatable" lay-filter="table"></table>

    <script type="text/html" id="tpl_status">
        {{# if(d.status && d.status=="1"){ }}
        <span class="layui-badge layui-bg-blue">启用</span>
        {{# }else{ }}
        <span class="layui-badge">禁用</span>
        {{# } }}
    </script>

    <script type="text/html" id="tpl_role">
        {{#    if(d.role){ }}
        {{#        if(d.role.indexOf(",")==0){ }}
        {{    d.role.substring(1) }}
        {{#        }else if(d.role.lastIndexOf(",")==d.role.length-1){ }}
        {{    d.role.substring(0,d.role.length-1) }}
        {{#        }else{ }}
        {{    d.role }}
        {{#        } }}
        {{#    } }}
    </script>

    <script type="text/html" id="tpl_dept">
        {{#    if(d.dept){ }}
        {{#        if(d.dept.indexOf(",")==0){ }}
        {{    d.dept.substring(1) }}
        {{#        }else if(d.dept.lastIndexOf(",")==d.dept.length-1){ }}
        {{    d.dept.substring(0,d.dept.length-1) }}
        {{#        }else{ }}
        {{    d.dept }}
        {{#        } }}
        {{#    } }}
    </script>

    <script type="text/html" id="tpl_opera">
        <adminPermission:hasPermission permission="admin:sysuser:edit">
            <a class="layui-btn layui-btn-xs layui-btn-normal" lay-event="edit">编辑</a>
        </adminPermission:hasPermission>
        <adminPermission:hasPermission permission="admin:sysuser:delete">
            <a class="layui-btn layui-btn-xs layui-btn-danger" lay-event="del">删除</a>
        </adminPermission:hasPermission>
        <adminPermission:hasPermission permission="admin:sysuser:userrole">
            <a class="layui-btn layui-btn-xs" lay-event="role">角色</a>
        </adminPermission:hasPermission>
        <adminPermission:hasPermission permission="admin:sysuser:usermenu">
            <a class="layui-btn layui-btn-xs" lay-event="mymenu">私有菜单</a>
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
    var lay_sort;

    var cols = [[ //表头
        {field: 'id', title: '序号', width: 70, type: 'checkbox'}
        , {field: 'username', title: '登录名', width: 100, sort: true, export: true}
        , {field: 'name', title: '用户称呼', width: 100, export: true}
        , {field: 'phone', title: '手机号码', width: 130, export: true}
        , {field: 'last_login_date', title: '上次登录时间', width: 170, sort: true, export: true}
        , {field: 'status', title: '状态', width: 70, templet: '#tpl_status', export: true}
        , {field: 'role', title: '持有角色', width: 180, templet: '#tpl_role', export: true}
        , {field: 'dept', title: '所属部门', templet: '#tpl_dept', export: true}
        , {fixed: 'right', width: 220, align: 'center', title: '操作', toolbar: '#tpl_opera'}
    ]];

    $(function () {
        var $tableheight = $(document).height() - $("#datatable").offset().top - 29;
        layui.use(['table', 'layer'], function () {
            table = layui.table;
            tableIns = table.render({
                id: 'id',//用于获取被选中的项
                elem: '#datatable'
                , height: $tableheight
                , url: 'list' //数据接口
                , page: true //开启分页
                , limit: 20
                , mutilsort: false
                , method: 'POST'
                , where: getFormJsonData("#searchForm")
                , request: {
                    pageName: 'pageNo' //页码的参数名称，默认：page
                    , limitName: 'pageSize' //每页数据量的参数名，默认：limit
                    , sorts: 'orders' //排序参数名，默认:sorts
                }
                , cols: cols
            });

            table.on('tool(table)', function (obj) {//tool event
                var data = obj.data; //获得当前行数据
                var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
                var tr = obj.tr; //获得当前行 tr 的DOM对象

                if (layEvent === 'edit') {
                    edit(data.id);
                } else if (layEvent === 'del') {
                    del(data.id);
                } else if (layEvent === 'role') {
                    allocateRole(data.id);
                } else if (layEvent === 'mymenu') {
                    allocateMenu(data.id);
                }
                //alert(data.id);
            });

            table.on('sort(table)', function (obj) {
                if (!obj.sorts) return;
                createSortView(obj.sorts);
                lay_sort = obj.sorts;
                console.log(lay_sort);
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

    //删除数据
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

            delData(ids);
        }, function (index) {
            layer.close(index);
        });
    }

    //批量删除
    function batchDelete() {
        var cs = table.checkStatus('id');
        if (cs.data.length > 0) {
            layer.confirm('您真的要删除选中的数据吗？', {
                icon: 3,//询问图标
                btn: ['确定', '取消'] //按钮
            }, function (index) {
                layer.close(index);//关闭弹层
                var ids = new Array();
                var data = cs.data;
                for (var i = 0; i < data.length; i++) {
                    ids.push(data[i].id);
                }

                delData(ids);
            }, function (index) {
                layer.close(index);
            });

        } else {
            layer.msg("请选择要删除的数据！");
        }

        //console.log(cs.data); //获取选中行的数据
        //console.log(cs.data.length); //获取选中行数量，可作为是否有选中行的条件
        //console.log(cs.isAll); //表格是否全选
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

    //导出excel
    function exportExcel() {
        //$("#searchForm").attr("action","exportExcel");
        //$("#searchForm").submit();
        lay_index = layer.load(1, {
            shade: [0.1, '#fff'] //0.1透明度的白色背景
        });
        var orders = new Array();
        if (lay_sort) {
            for (var i = 0; i < lay_sort.length; i++) {
                orders.push(lay_sort[i].field + " " + lay_sort[i].type);
            }
        }
        var param = getFormJsonData("#searchForm");
        param["excelCols"] = JSON.stringify(colsToExportCols(cols));
        param["orders"] = orders;

        //请求后台生成要下载的excel，再下载到本地
        $.ajax({
            type: 'post',
            url: 'exportExcel',
            data: param,
            dataType: "json",
            cache: false,
            success: function (data) {
                //console.log(data);
                layer.close(lay_index);
                if (data.msg) layer.msg(data.msg);
                if (data.code == 200) {
                    var url = data.result;
                    //后续优化为按指定名称导出
                    window.open(url);
                }
            },
            error: function (xhr, desc, err) {
                layer.msg("数据请求失败:" + desc);
                layer.close(lay_index);
            }
        });

    }

    /**
     * 将列表对象转换为excel导出工具使用的数据格式
     * @param {} cols
     * @return {}
     */
    function colsToExportCols(cols) {
        var ncols = new Array();
        for (var i = 0; i < cols.length; i++) {
            var d = cols[i];
            var d1 = new Array();
            for (var j = 0; j < d.length; j++) {
                if (!d[j].export) {
                    continue;
                }
                var o = new Object();
                if (d[j].field) {
                    o["field"] = d[j].field;
                }
                if (d[j].exportField) {
                    o["field"] = d[j].exportField;
                }
                if (d[j].title) {
                    o["title"] = d[j].title;
                    o["width"] = o.title.length * 2;
                }
                if (d[j].rowspan) {
                    o["rowspan"] = d[j].rowspan;
                }
                if (d[j].colspan) {
                    o["colspan"] = d[j].colspan;
                }
                if (d[j].width) {
                    var n1 = d[j].width / 10;
                    if (o["width"] && o["width"] < n1) {
                        o["width"] = n1;
                    } else if (!o["width"]) {
                        o["width"] = n1;
                    }
                }
                d1[d1.length] = o;
            }
            ncols[ncols.length] = d1;
        }
        return ncols;
    }

    var dlg_indx;
    var userId = -1;

    //给用户分配特殊菜单
    function allocateMenu(id) {
        if (typeof id == 'undefined') {
            layer.msg("请选择用户");
            return;
        }
        userId = id;
        dlg_indx = layer.open({
            type: 2,
            title: '分配菜单',
            skin: 'layui-layer-rim', //加上边框
            area: ['550px', '450px'], //宽高
            content: 'userMenuList?userId=' + id
        });
    }

    //选择菜单后回调
    function selectMenu(menu_ids, menu_names) {
        lay_index = layer.load(1, {
            shade: [0.1, '#fff'] //0.1透明度的白色背景
        });

        $.ajax({
            type: 'post',
            url: 'addSaveUserMenu',
            data: {userId: userId, menuIds: menu_ids},
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

    //给用户分配角色
    function allocateRole(id) {
        if (typeof id == 'undefined') {
            layer.msg("请选择用户");
            return;
        }
        userId = id;
        dlg_indx = layer.open({
            type: 2,
            title: '分配角色',
            skin: 'layui-layer-rim', //加上边框
            area: ['550px', '450px'], //宽高
            content: 'userRoleList?userId=' + id
        });
    }

    //选择菜单后回调
    function selectRole(role_ids) {
        lay_index = layer.load(1, {
            shade: [0.1, '#fff'] //0.1透明度的白色背景
        });

        $.ajax({
            type: 'post',
            url: 'addSaveUserRole',
            data: {userId: userId, roleIds: role_ids},
            dataType: "json",
            cache: false,
            success: function (data) {
                layer.close(lay_index);
                if (data.msg) layer.msg(data.msg);
                if (data.code == 200) {
                    layer.close(dlg_indx);
                    searchData();
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
