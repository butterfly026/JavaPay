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
    <title>记事本</title>
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
            <h3><b>&nbsp;记事本</b></h3>
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

        <legend class="admin-search-title">记事本内容查询</legend>

        <form id="searchForm" class="layui-form">
            <div class="layui-row admin-search-row">

                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                    <label class="layui-form-label">标题</label>
                    <div class="layui-input-block">
                        <input type="text" name="title" id="title" placeholder="标题" class="layui-input"/>
                    </div>
                </div>

                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                    <label class="layui-form-label">内容</label>
                    <div class="layui-input-block">
                        <input type="text" name="content" id="content" placeholder="内容" class="layui-input"/>
                    </div>
                </div>


                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                    <label class="layui-form-label">关联内容</label>
                    <div class="layui-input-block">
                        <input type="text" name="linkContent" id="linkContent" placeholder="关联内容" class="layui-input"/>
                    </div>
                </div>


                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2" style="text-align:center;">
                    <button class="layui-btn layui-btn-normal" type="button" style="width:35%;" onclick="searchData();">
                        搜索
                    </button>
                    <button class="layui-btn" style="width:35%;" type="button" onclick="resetForm();">重置</button>
                </div>
            </div>
        </form>
    </fieldset>

    <div class="admin-btns">
        <adminPermission:hasPermission permission="admin:notebook:add">
            <button class="layui-btn layui-btn-sm layui-btn-normal" onclick="add();">
                添加
            </button>
        </adminPermission:hasPermission>
    </div>

    <table id="datatable" lay-filter="table"></table>


    <script type="text/html" id="tpl_opera">
        <adminPermission:hasPermission permission="admin:notebook:edit">
            <a class="layui-btn layui-btn-xs layui-btn-normal" lay-event="edit">编辑</a>
        </adminPermission:hasPermission>
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
                    {field: 'title', title: '标题'}
                    , {field: 'content', title: '内容', width: '40%'}
                    , {field: 'linkContent', title: '关联'}
                    , {field: 'username', title: '创建人'}
                    , {field: 'createTime', title: '创建时间'}
                    , {field: 'upUsername', title: '更新人'}
                    , {field: 'updateTime', title: '更新时间'}
                    , {fixed: 'right', align: 'center', title: '操作', toolbar: '#tpl_opera'}
                ]]
            });

            table.on('tool(table)', function (obj) {//tool event
                var data = obj.data; //获得当前行数据
                var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
                var tr = obj.tr; //获得当前行 tr 的DOM对象

                if (layEvent === 'edit') {
                    edit(data.id);
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
