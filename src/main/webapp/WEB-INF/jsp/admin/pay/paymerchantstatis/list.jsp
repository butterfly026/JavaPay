<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="adminPermission" uri="http://adminpermission.city.com" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>商户统计管理</title>
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
            <h3><b>&nbsp;商户统计管理</b></h3>
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
        <legend class="admin-search-title">商户统计查询</legend>
        <form id="searchForm" class="layui-form">
            <div class="layui-row admin-search-row">
                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                    <select name="merchantId" lay-search>
                        <option value="">选择商户</option>

                        <c:forEach items="${merchantList }" var="c">
                            <option value="${c.id}">${c.username}-${c.name}</option>
                        </c:forEach>

                    </select>
                </div>

                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                    <input type="text" name="startTime" id="startTime" placeholder="开始时间" class="layui-input"
                           value="${startTime}" autocomplete="off"/>
                </div>

                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                    <input type="text" name="endTime" id="endTime" placeholder="结束时间" class="layui-input"
                           autocomplete="off"/>
                </div>

                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                    <select name="type" lay-search>
                        <option value="0">按天统计</option>
                        <option value="1">按月统计</option>
                    </select>
                </div>

                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2" style="text-align:center;">
                    <button class="layui-btn layui-btn-normal" type="button" onclick="searchData();" id="searchBtn">搜索</button>
                    <button class="layui-btn" type="button" onclick="resetForm();">重置</button>
                </div>
            </div>
        </form>
    </fieldset>

    <div class="admin-btns">

        <adminPermission:hasPermission permission="admin:paymerchantstatis:export">
            <button class="layui-btn layui-btn-sm layui-btn-normal" onclick="exportExcel();">
                导出Excel
            </button>

            <button class="layui-btn layui-btn-sm layui-btn-normal" onclick="showExportView();">
                历史导出数据
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

    <script type="text/html" id="tpl_opera">
        <adminPermission:hasPermission permission="admin:paymerchantstatis:edit">
            <a class="layui-btn layui-btn-xs layui-btn-normal" lay-event="edit">编辑</a>
        </adminPermission:hasPermission>
        <adminPermission:hasPermission permission="admin:paymerchantstatis:delete">
            <a class="layui-btn layui-btn-xs layui-btn-danger" lay-event="del">删除</a>
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

    var lay_sort;

    $(function () {
        var $tableheight = $(document).height() - $("#datatable").offset().top - 29;
        layui.use(['table', 'layer', 'laydate'], function () {
            table = layui.table;

            var laydate = layui.laydate;

            laydate.render({
                elem: '#startTime' //指定元素
                , type: "date"
                , format: 'yyyy-MM-dd'
            });

            laydate.render({
                elem: '#endTime' //指定元素
                , type: "date"
                , format: 'yyyy-MM-dd'
            });

            tableIns = table.render({
                id: 'id',//用于获取被选中的项
                elem: '#datatable'
                //,height: $tableheight
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
                    , {field: 'merchantno', title: '商户', width: 180, sort: true}
                    , {field: 'paytime', title: '日期', width: 180, sort: true}
                    , {field: 'succount', title: '订单成功数', width: 150, sort: true}
                    , {field: 'total', title: '订单总数', width: 150, sort: true}
                    , {field: 'ratio', title: '成功率', width: 150, sort: true}
                    , {field: 'money', title: '成交额', width: 150, sort: true}
                    , {field: 'profit', title: '订单利润(${sysconfig.currency })', width: 150, sort: true}
                    , {field: 'cashprofit', title: '提现利润(${sysconfig.currency })', width: 150, sort: true}
                    , {field: 'totalprofit', title: '总利润(${sysconfig.currency })', width: 150, sort: true}
                ]]
                , done: function (res, curr, count) {
                    if(res){
                        $('#searchBtn').removeAttr("disabled");
                    }
                    layer.close(lay_index);
                }
            });

            table.on('tool(table)', function (obj) {//tool event
                var data = obj.data; //获得当前行数据
                var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
                var tr = obj.tr; //获得当前行 tr 的DOM对象

                if (layEvent === 'edit') {
                    edit(data.id);
                } else if (layEvent === 'del') {
                    del(data.id);
                }
            });

            table.on('sort(table)', function (obj) {
                if (!obj.sorts) return;
                createSortView(obj.sorts);
                lay_sort = obj.sorts;
                searchData();
            });
        });
        initSelectAll();
    });

    //移除排序
    function removeSortField(field) {
        createSortView(table.removeSortField(field));
        searchData();
    }

    //搜索数据
    function searchData() {
        $('#searchBtn').attr({'disabled':'disabled'});
        lay_index = layer.load(1, {
            shade: [0.1, '#fff'] //0.1透明度的白色背景
        });
        tableIns.reload({
            where: getFormJsonData("#searchForm")
        });
        setTimeout(function () {
            $('#searchBtn').removeAttr("disabled");
        },3000)
    }

    //新增数据
    function add() {
        lay_index = layer.open({
            type: 2,
            title: '新增商户统计',
            skin: 'layui-layer-rim', //加上边框
            area: ['80%', '80%'], //宽高
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
            title: '编辑商户统计',
            skin: 'layui-layer-rim', //加上边框
            area: ['80%', '80%'], //宽高
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

    var exportObj = [
        [ //表头
            {field: 'merchantno', title: '商户号', width: 30, rowspan: 1, colspan: 1},
            {field: 'paytime', title: '日期', width: 30, rowspan: 1, colspan: 1},
            {field: 'succount', title: '订单成功数', width: 30, rowspan: 1, colspan: 1},
            {field: 'total', title: '订单总数', width: 15, rowspan: 1, colspan: 1},
            {field: 'ratio', title: '成功率', width: 20, rowspan: 1, colspan: 1},
            {field: 'money', title: '成交额(${sysconfig.currency })', width: 20, rowspan: 1, colspan: 1},
            {field: 'profit', title: '订单利润(${sysconfig.currency })', width: 15, rowspan: 1, colspan: 1},
            {field: 'cashprofit', title: '提现利润(${sysconfig.currency })', width: 30, rowspan: 1, colspan: 1},
            {field: 'totalprofit', title: '总利润(${sysconfig.currency })', width: 30, rowspan: 1, colspan: 1}
        ]
    ];

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
        param["excelCols"] = JSON.stringify(exportObj);
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
                    //var url=data.result;
                    //后续优化为按指定名称导出
                    //window.open(url);
                    showExportView();
                }
            },
            error: function (xhr, desc, err) {
                layer.msg("数据请求失败:" + desc);
                layer.close(lay_index);
            }
        });

    }

    function showExportView() {
        lay_index = layer.open({
            type: 2,
            title: '导出列表',
            skin: 'layui-layer-rim', //加上边框
            area: ['90%', '90%'], //宽高
            content: '../payExcelexport/view?source=3'
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
</script>
</body>
</html>
