<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="adminPermission" uri="http://adminpermission.city.com" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>订单管理</title>
    <link rel="stylesheet" href="${context}/layui/css/layui.css">
    <link rel="stylesheet" href="${context}/layui/css/dropdown.css">
    <link rel="stylesheet" href="${context}/css/style.css?${s_vs}">
    <style>
        .layui-icon-triangle-d:before {
            content: "\e625"
        }

        .layui-icon-form:before {
            content: "\e63c"
        }

        .layui-icon-speaker:before {
            content: "\e645"
        }
    </style>
    <!--[if lt IE 9]>
      <script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
      <script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<div class="layui-fluid admin-head-title">
    <div class="layui-row left">
        <div class="layui-col-xs11 layui-col-sm11 layui-col-md11">
            <h3><b>&nbsp;订单管理</b></h3>
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
        <legend class="admin-search-title">订单查询</legend>
        <form id="searchForm" class="layui-form">
            <div class="layui-row admin-search-row">
                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                    <input type="text" name="sn" id="sn" placeholder="系统订单号" class="layui-input"/>
                </div>

                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                    <input type="text" name="merchantSn" id="merchantSn" placeholder="商户订单号" class="layui-input"/>
                </div>

                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                    <select name="merchantId" id="merchantId" lay-search>
                        <option value="">选择商户</option>

                        <c:forEach items="${merchantList }" var="c">
                            <option value="${c.id}">${c.username}-${c.name}</option>
                        </c:forEach>

                    </select>
                </div>

                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                    <select name="typeId" lay-search>
                        <option value="">支付方式</option>

                        <c:forEach items="${channelTypeList }" var="c">
                            <option value="${c.id}">${c.name}</option>
                        </c:forEach>

                    </select>
                </div>

            </div>

            <div class="layui-row admin-search-row">

                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                    <select name="orderStatus" lay-search>
                        <option value="">订单状态</option>
                        <option value="0">未支付</option>
                        <option value="1">已支付</option>
                    </select>
                </div>

                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                    <input type="text" name="startTime" id="startTime" placeholder="开始时间" class="layui-input"
                           autocomplete="off"/>
                </div>

                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                    <input type="text" name="endTime" id="endTime" placeholder="结束时间" class="layui-input"
                           autocomplete="off"/>
                </div>

                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2" style="text-align:center;">
                    <button class="layui-btn layui-btn-normal" type="button" onclick="searchData();">搜索</button>
                    <button class="layui-btn" type="button" onclick="resetForm();">重置</button>
                </div>
            </div>
        </form>
    </fieldset>

    <div style="width:100%;font-size:12px;padding:10px">
        <input type="checkbox" name="needTotal" id="needTotal" value="1"/>显示统计信息
        <span style="padding-left:20px;" id="totalInfo">请在需要的时候查看统计信息以减轻系统压力</span>
    </div>

    <div class="admin-btns">
        <adminPermission:hasPermission permission="admin:payorder:proxyExport">
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

    <script type="text/html" id="tpl_dd">
        {{# if(d.order_status && d.order_status=="1"){ }}
        <span class="layui-badge layui-bg-blue">已支付</span>
        {{# }else{ }}
        <span class="layui-badge">待支付</span>
        {{# } }}

        {{# if(d.mend==1){ }}
        <span class="layui-badge layui-bg-blue">补单</span>
        {{# } }}
    </script>

    <script type="text/html" id="tpl_tz">
        {{# if(d.notifyStatus && d.notifyStatus=="1"){ }}
        <span class="layui-badge layui-bg-blue">成功</span>
        {{# } }}
    </script>

    <script type="text/html" id="tpl_url">
        <a href="{{d.pay_url}}" target="_blank" style="color:#1E9FFF;">点击打开</a>
    </script>

    <script type="text/html" id="tpl_opera">

	</script>

    <script type="text/html" id="tpl_index">
        {{ d.LAY_INDEX }}
    </script>

</div>
<script src="${context}/js/jquery.min.js"></script>
<script src="${context}/layui/layui.js?${s_vs}"></script>
<script src="${context}/js/common.js?${s_vs}"></script>
<script type="text/javascript">
    var tableIns;
    var table;
    var lay_index;

    var dropdown;

    var lay_sort;

    $(function () {
        layui.config({
            base: '${context}/layui/lay/modules/' //配置 layui-dropdown 组件基础目录
        }).extend({
            dropdown: 'dropdown'
        });

        var $tableheight = $(document).height() - $("#datatable").offset().top - 29;
        layui.use(['table', 'layer', 'laydate', 'dropdown'], function () {

            dropdown = layui.dropdown;

            var laydate = layui.laydate;

            laydate.render({
                elem: '#startTime' //指定元素
                , type: "datetime"
                , format: 'yyyy-MM-dd HH:mm:ss'
            });

            laydate.render({
                elem: '#endTime' //指定元素
                , type: "datetime"
                , format: 'yyyy-MM-dd HH:mm:ss'
            });

            table = layui.table;
            tableIns = table.render({
                id: 'id',//用于获取被选中的项
                elem: '#datatable'
                , size: 'sm'
                //,height: $tableheight
                , url: 'proxyList' //数据接口
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
                    , {field: 'sn', title: '系统订单号', width: 150}
                    //,{field:'merchantSn', title: '商户订单号', width:150}
                    , {field: 'merchant_sn', title: '商户订单号', width: 150}

                    , {field: 'tname', title: '支付方式', width: 110}
                    , {field: 'money', title: '金额(元)', width: 90, sort: true}

                    //,{field:'merchantNo', title: '商户号', width:110}
                    , {field: 'merchant_no', title: '商户号', width: 110}

                    //,{field:'orderStatus', title: '订单状态', width:120,templet:'#tpl_dd',sort:true}
                    , {field: 'order_status', title: '订单状态', width: 120, templet: '#tpl_dd', sort: true}
                    , {field: 'notifyStatus', title: '通知状态', width: 90, templet: '#tpl_tz'}

                    //,{field:'createTime', title: '创建时间', width:150,sort:true}
                    //,{field:'payTime', title: '支付时间', width:150,sort:true}
                    , {field: 'create_time', title: '创建时间', width: 150, sort: true}
                    , {field: 'pay_time', title: '支付时间', width: 150, sort: true}

                    //,{fixed: 'right', align:'center',title:'操作', width:90, toolbar: '#tpl_opera'}
                ]]
                , done: function (res, curr, count) {
                    dropdown.suite();
                }
            });

            table.on('tool(table)', function (obj) {//tool event
                var data = obj.data; //获得当前行数据
                var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
                var tr = obj.tr; //获得当前行 tr 的DOM对象

                if (layEvent === 'edit') {
                    edit(data.id);
                } else if (layEvent === 'detail') {
                    detail(data.id, data.sn);
                } else if (layEvent === 'xy') {
                    xy(data.id);
                }
            });

            table.on('sort(table)', function (obj) {
                if (!obj.sorts) return;
                createSortView(obj.sorts);
                lay_sort = obj.sorts;
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

        if ($('#needTotal').is(":checked")) {
            totalInfoData();
        } else {
            $("#totalInfo").html("请在需要的时候查看统计信息以减轻系统压力");
        }
    }

    function detail(id, sn) {
        lay_index = layer.open({
            type: 2,
            title: '订单详情-' + sn,
            skin: 'layui-layer-rim', //加上边框
            area: ['80%', '80%'], //宽高
            content: 'merchantDetail?id=' + id
        });
    }

    //更新状态
    function xy(id) {
        layer.confirm('是否向商户后台发送通知？', {
            icon: 3,//询问图标
            btn: ['确定', '取消'] //按钮
        }, function (index) {
            layer.close(index);//关闭弹层

            commReq('merchantXy', {id: id}, function () {
                searchData();
            });
        }, function (index) {
            layer.close(index);
        });
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

    //新增数据
    function add() {
        lay_index = layer.open({
            type: 2,
            title: '新增支付订单',
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
            title: '编辑支付订单',
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
            {field: 'sn', title: '系统订单号', width: 30, rowspan: 1, colspan: 1},
            {field: 'merchantSn', title: '商户订单号', width: 30, rowspan: 1, colspan: 1},
            {field: 'money', title: '成交金额(元)', width: 20, rowspan: 1, colspan: 1},
            {field: 'typeName', title: '通道类型', width: 15, rowspan: 1, colspan: 1},
            {field: 'createTime', title: '创建时间', width: 30, rowspan: 1, colspan: 1},
            {field: 'payTime', title: '成交时间', width: 30, rowspan: 1, colspan: 1},
            {field: 'orderStatus', title: '订单状态', width: 15, rowspan: 1, colspan: 1},
            {field: 'merchantNo', title: '商户账号', width: 15, rowspan: 1, colspan: 1}
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
            url: 'exportExcelProxy',
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
            content: '../payExcelexport/view?source=0'
        });
    }

    function totalInfoData() {
        $("#totalInfo").html("正在统计...");
        $.ajax({
            type: 'post',
            url: 'queryTotalProxy',
            data: getFormJsonData("#searchForm"),
            dataType: "json",
            cache: false,
            success: function (data) {
                if (data.code == 200) {
                    var $r = data.result;
                    if ($r != null) {
                        $("#totalInfo").html("当前数据中,交易成功金额<span style='color:green'>" + ($r.sucMoney ? $r.sucMoney : '0') + " ${sysconfig.currency }</span>");
                    } else {
                        $("#totalInfo").html("无统计数据");
                    }

                } else {
                    layer.msg(data.msg);
                }
            },
            error: function (xhr, desc, err) {
                layer.msg("数据请求失败:" + desc);
            }
        });
    }
</script>
</body>
</html>
