<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="adminPermission" uri="http://adminpermission.city.com" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>上游模块管理</title>
    <link rel="stylesheet" href="${context}/layui/css/layui.css">
    <link rel="stylesheet" href="${context}/css/style.css?${s_vs}">
    <!--[if lt IE 9]>
      <script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
      <script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<div class="layui-fluid admin-head-title">
    <div class="layui-row left">
        <div class="layui-col-xs11 layui-col-sm11 layui-col-md11">
            <h3><b>&nbsp;上游模块管理</b></h3>
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
        <legend class="admin-search-title">上游模块查询</legend>
        <form id="searchForm" class="layui-form">
            <div class="layui-row admin-search-row">

                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                    <input type="text" name="name" id="name" placeholder="模块名" class="layui-input"/>
                </div>

                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                    <input type="text" name="keyname" id="keyname" placeholder="键名" class="layui-input"/>
                </div>

                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                    <select name="status" id="status" lay-verify="required">
                        <option value="">状态</option>
                        <option value="1">启用</option>
                        <option value="0">禁用</option>
                    </select>
                </div>

                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2" style="text-align:center;">
                    <button class="layui-btn layui-btn-normal" type="button" onclick="searchData();">搜索</button>
                    <button class="layui-btn" type="button" onclick="resetForm();">重置</button>
                </div>
            </div>
        </form>
    </fieldset>

    <div class="admin-btns">
        <adminPermission:hasPermission permission="admin:payclientmodel:add">
            <button class="layui-btn layui-btn-sm layui-btn-normal" onclick="add();">
                添加
            </button>
        </adminPermission:hasPermission>

        <adminPermission:hasPermission permission="admin:payclientmodel:app">
            <button class="layui-btn layui-btn-sm layui-btn-danger" onclick="batchApp();">
                全部应用
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

    <script type="text/html" id="tpl_orderStatus">
        {{# if(d.orderStatus && d.orderStatus=="1"){ }}
        <span class="layui-badge layui-bg-blue">支持</span>
        {{# }else{ }}
        <span class="layui-badge">不支持</span>
        {{# } }}
    </script>

    <script type="text/html" id="tpl_payStatus">
        {{# if(d.payStatus && d.payStatus=="1"){ }}
        <span class="layui-badge layui-bg-blue">支持</span>
        {{# }else{ }}
        <span class="layui-badge">不支持</span>
        {{# } }}
    </script>

    <script type="text/html" id="tpl_testStatus">
        {{# if(d.testStatus && d.testStatus=="1"){ }}
        <span class="layui-badge layui-bg-blue">支持</span>
        {{# }else{ }}
        <span class="layui-badge">不支持</span>
        {{# } }}
    </script>

    <script type="text/html" id="tpl_cdStatus">
        {{# if(d.cdStatus && d.cdStatus=="1"){ }}
        <span class="layui-badge layui-bg-blue">支持</span>
        {{# }else{ }}
        <span class="layui-badge">不支持</span>
        {{# } }}
    </script>

    <script type="text/html" id="tpl_reqType">
        {{# if(d.reqType && d.reqType=="1"){ }}
        <span class="layui-badge">JSON</span>
        {{# }else{ }}
        <span class="layui-badge layui-bg-blue">普通表单</span>
        {{# } }}
    </script>

    <script type="text/html" id="tpl_opera">
        <adminPermission:hasPermission permission="admin:payclientmodel:edit">
            <a class="layui-btn layui-btn-xs layui-btn-normal" lay-event="edit">编辑</a>
        </adminPermission:hasPermission>
        <adminPermission:hasPermission permission="admin:payclientmodel:app">
            <a class="layui-btn layui-btn-xs layui-bg-green" lay-event="app">应用</a>
        </adminPermission:hasPermission>
    </script>

    <script type="text/html" id="tpl_index">
        {{ d.LAY_INDEX }}
    </script>

</div>
<script src="${context}/js/jquery.min.js"></script>
<script src="${context}/layui/layui.js?1"></script>
<script src="${context}/js/common.js?8"></script>
<script type="text/javascript">
    var tableIns;
    var table;
    var lay_index;
    $(function () {
        var $tableheight = $(document).height() - $("#datatable").offset().top - 29;
        layui.use(['table', 'layer'], function () {
            table = layui.table;
            tableIns = table.render({
                id: 'id',//用于获取被选中的项
                elem: '#datatable'
                , height: $tableheight
                , size: 'sm'
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
                    {field: 'id', title: '序号', width: 70, templet: '#tpl_index'}
                    , {field: 'name', title: '模块名', width: 130}
                    , {field: 'keyname', title: '键名', width: 130}
                    //,{field:'filepath', title: '文件路径', width:150}
                    , {field: 'status', title: '状态', width: 90, templet: '#tpl_status'}
                    , {field: 'orderStatus', title: '支持下单', width: 130, templet: '#tpl_orderStatus'}
                    , {field: 'payStatus', title: '支持代付', width: 130, templet: '#tpl_payStatus'}
                    , {field: 'testStatus', title: '支持测试', width: 130, templet: '#tpl_testStatus'}
                    , {field: 'cdStatus', title: '支持查单', width: 130, templet: '#tpl_cdStatus'}
                    , {field: 'reqType', title: '请求类型', width: 130, templet: '#tpl_reqType'}
                    , {field: 'version', title: '版本号', width: 130}

                    , {fixed: 'right', align: 'center', title: '操作', width: 120, toolbar: '#tpl_opera'}
                ]]
            });

            table.on('tool(table)', function (obj) {//tool event
                var data = obj.data; //获得当前行数据
                var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
                var tr = obj.tr; //获得当前行 tr 的DOM对象

                if (layEvent === 'edit') {
                    edit(data.id, data.name);
                } else if (layEvent === 'app') {
                    app(data.id);
                }
            });

            table.on('sort(table)', function (obj) {
                if (!obj.sorts) return;
                createSortView(obj.sorts);
                searchData();
            });
        });
    });

    function app(id) {
        layer.confirm('是否应用此模块？', {
            icon: 3,//询问图标
            btn: ['确定', '取消'] //按钮
        }, function (index) {
            layer.close(index);//关闭弹层

            commReq('app', {id: id}, function () {
                searchData();
            });
        }, function (index) {
            layer.close(index);
        });
    }

    function batchApp() {
        layer.confirm('是否批量应用模块？', {
            icon: 3,//询问图标
            btn: ['确定', '取消'] //按钮
        }, function (index) {
            layer.close(index);//关闭弹层

            commReq('batchApp', {}, function () {
                searchData();
            });
        }, function (index) {
            layer.close(index);
        });

    }

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
            title: '新增上游模块',
            skin: 'layui-layer-rim', //加上边框
            area: ['90%', '90%'], //宽高
            content: 'add'
        });
    }

    //编辑数据
    function edit(id, name) {
        if (typeof id == 'undefined') {
            layer.msg("请选择需要编辑的数据");
            return;
        }
        lay_index = layer.open({
            type: 2,
            title: '编辑上游模块-' + name,
            skin: 'layui-layer-rim', //加上边框
            area: ['90%', '90%'], //宽高
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

    function commReq(url, data, sucfunc) {
        lay_index = layer.load(1, {
            shade: [0.1, '#fff'] //0.1透明度的白色背景
        });
        $.ajax({
            type: 'post',
            url: url,
            data: data,
            dataType: "json",
            cache: false,
            success: function (data) {
                layer.close(lay_index);
                if (data.msg) layer.msg(data.msg);
                if (data.code == 200) {
                    if (sucfunc) sucfunc(data);
                }
            },
            error: function (xhr, desc, err) {
                layer.msg("数据请求失败:" + desc);
                layer.close(lay_index);
            }
        });
    }


</script>
</body>
</html>
