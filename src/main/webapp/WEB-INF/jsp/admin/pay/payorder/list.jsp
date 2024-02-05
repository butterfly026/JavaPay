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
                    <input type="text" name="clientSn" id="clientSn" placeholder="上游订单号" class="layui-input"/>
                </div>

                <div class="layui-col-xs2 layui-col-sm2 layui-col-md1">
                    <select name="clientId" id="clientId" lay-search>
                        <option value="">选择上游</option>

                        <c:forEach items="${clientList }" var="c">
                            <option value="${c.id}">${c.username}-${c.name}</option>
                        </c:forEach>

                    </select>
                </div>
                <div class="layui-col-xs2 layui-col-sm2 layui-col-md1">
                    <select name="client_channel_id" id="client_channel_id" lay-search>
                        <option value="">上游渠道名</option>

                        <c:forEach items="${channelNameList }" var="c">
                            <option value="${c.id}">${c.name}</option>
                        </c:forEach>

                    </select>
                </div>

                <div class="layui-col-xs2 layui-col-sm2 layui-col-md1">
                    <select name="proxyId" id="proxyId" lay-search>
                        <option value="">选择代理</option>

                        <c:forEach items="${proxyList }" var="c">
                            <option value="${c.id}">${c.username}-${c.name}</option>
                        </c:forEach>

                    </select>
                </div>

                <div class="layui-col-xs2 layui-col-sm2 layui-col-md1">
                    <select name="merchantId" id="merchantId" lay-search>
                        <option value="">选择商户</option>

                        <c:forEach items="${merchantList }" var="c">
                            <option value="${c.id}">${c.username}-${c.name}</option>
                        </c:forEach>

                    </select>
                </div>

                <div class="layui-col-xs2 layui-col-sm2 layui-col-md1">
                    <select name="typeId" lay-search>
                        <option value="">支付方式</option>

                        <c:forEach items="${channelTypeList }" var="c">
                            <option value="${c.id}">${c.name}</option>
                        </c:forEach>

                    </select>
                </div>

            </div>

            <div class="layui-row admin-search-row">


                <div class="layui-col-xs2 layui-col-sm2 layui-col-md1">
                    <select name="orderStatus" lay-search id="orderStatus" lay-filter="orderTpye">
                        <option value="">订单状态</option>
                        <option value="-2">未跳转</option><!--未支付 未获取链接-->
                        <option value="-1">上游失败</option><!--获取链接失败-->
                        <option value="0">未支付</option>
                        <option value="1">已支付</option>
                    </select>
                </div>

                <div class="layui-col-xs2 layui-col-sm2 layui-col-md1">
                    <select name="amountEq" id="amountEq" lay-search>
                        <option value="">选择金额</option>
                        <option value="30">30</option>
                        <option value="50">50</option>
                        <option value="100">100</option>
                        <option value="200">200</option>
                    </select>
                </div>

                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                    <input type="text" name="startTime" id="startTime" placeholder="创建开始时间" class="layui-input"
                           autocomplete="off"/>
                </div>

                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                    <input type="text" name="endTime" id="endTime" placeholder="创建结束时间" class="layui-input"
                           autocomplete="off"/>
                </div>

                <div id="payTime" style="display: none">
                    <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                        <input type="text" name="paystartTime" id="paystartTime" placeholder="支付开始时间" class="layui-input"
                               autocomplete="off"/>
                    </div>

                    <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                        <input type="text" name="payendTime" id="payendTime" placeholder="支付结束时间" class="layui-input"
                               autocomplete="off"/>
                    </div>
                </div>

                <div class="layui-col-xs2 layui-col-sm2 layui-col-md1">
                    <select name="mend_status" id="mend_status" lay-search>
                        <option value="">筛选补单</option>
                        <option value="0">所有订单</option>
                        <option value="1">补单订单</option>
                    </select>
                </div>

                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2" style="text-align:center;">
                    <button class="layui-btn layui-btn-normal" type="button" id="searchBtn" onclick="searchData();">搜索</button>
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
        <adminPermission:hasPermission permission="admin:payorder:nullorder">
            <button class="layui-btn layui-btn-sm layui-btn-danger" onclick="createNullOrder();">
                创建空单
            </button>
        </adminPermission:hasPermission>

        <adminPermission:hasPermission permission="admin:payorder:export">
            <button class="layui-btn layui-btn-sm layui-btn-normal" onclick="exportExcel();">
                导出Excel
            </button>

            <button class="layui-btn layui-btn-sm layui-btn-normal" onclick="showExportView();">
                历史导出数据
            </button>
        </adminPermission:hasPermission>

        <adminPermission:hasPermission permission="admin:payorder:queryorder">
            <button class="layui-btn layui-btn-sm layui-btn-warm" onclick="batchQueryOrder();">
                批量查单
            </button>
        </adminPermission:hasPermission>

        <adminPermission:hasPermission permission="admin:payorder:notifyorder">
            <button class="layui-btn layui-btn-sm layui-btn-warm" onclick="batchNotifyMerchant();">
                批量通知商户
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
        {{# }else if(d.order_status && d.order_status=="-2"){ }}
        <span class="layui-badge">未获取链接</span>
        {{# }else if(d.order_status && d.order_status=="-1"){ }}
        <span class="layui-badge">获取链接失败</span>
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

        <adminPermission:hasPermission permission="admin:payorder:detail">
            <a class="layui-btn layui-btn-xs layui-btn" lay-event="detail">详情</a>
        </adminPermission:hasPermission>

        {{# if(d.order_status==0){ }}
        <adminPermission:hasPermission permission="admin:payorder:bd">
            <a class="layui-btn layui-btn-xs layui-btn-normal" lay-event="bd">补单</a>
        </adminPermission:hasPermission>

        <adminPermission:hasPermission permission="admin:payorder:bd">
            <a class="layui-btn layui-btn-xs layui-btn-warm" lay-event="cd">查单</a>
        </adminPermission:hasPermission>
        {{# } }}

        {{# if(d.order_status==1){ }}
        <adminPermission:hasPermission permission="admin:payorder:xy">
            <a class="layui-btn layui-btn-xs layui-btn-normal" lay-event="xy">下游</a>
        </adminPermission:hasPermission>
        {{# } }}
    </script>

    <script type="text/html" id="tpl_index">
        {{ d.LAY_INDEX }}
    </script>

    <script type="text/html" id="tpl_platform">
        {{# if(d.platform=="1"){ }}
        <span class="layui-btn layui-btn-xs layui-btn-warm">IOS</span>
        {{# }else if(d.platform=="2"){ }}
        <span class="layui-btn layui-btn-xs layui-btn-warm">Android</span>
        {{# }else if(d.platform=="3"){ }}
        <span class="layui-btn layui-btn-xs layui-btn-warm">通用</span>
        {{# } }}
    </script>

</div>
<script src="${context}/js/jquery.min.js"></script>
<script src="${context}/layui/layui.js?${s_vs}"></script>
<script src="${context}/js/common.js?${s_vs}"></script>
<script type="text/javascript">
    var tableIns;
    var table;
    var lay_index;

    var lay_sort;

    $(function () {
        var $tableheight = $(document).height() - $("#datatable").offset().top - 29;
        layui.use(['table', 'layer', 'laydate','form'], function () {
            table = layui.table;

            var laydate = layui.laydate;
            var form = layui.form;

            laydate.render({
                elem: '#startTime' //指定元素
                , type: "datetime"
                , format: 'yyyy-MM-dd HH:mm:ss'
                , value: new Date(new Date().setHours(0, 0, 0, 0))
            });

            laydate.render({
                elem: '#endTime' //指定元素
                , type: "datetime"
                , format: 'yyyy-MM-dd HH:mm:ss'
            });

            laydate.render({
                elem: '#paystartTime' //指定元素
                , type: "datetime"
                , format: 'yyyy-MM-dd HH:mm:ss'
            });

            laydate.render({
                elem: '#payendTime' //指定元素
                , type: "datetime"
                , format: 'yyyy-MM-dd HH:mm:ss'
            });

            tableIns = table.render({
                id: 'id',//用于获取被选中的项
                elem: '#datatable'
                , size: 'sm'
                //,height: $tableheight
                , url: 'list' //数据接口
                , page: true //开启分页
                , limit: 90
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
                    //,{field:'clientSn', title: '上游订单号', width:150}
                    , {field: 'merchant_sn', title: '商户订单号', width: 150}
                    , {field: 'client_sn', title: '上游订单号', width: 150}
                    //,{field:'clientNo', title: '上游名称', width:110}
                    , {field: 'client_no', title: '上游名称', width: 110}
                    , {field: 'client_channel_no', title: '上游通道名称', width: 110}
                    //,{field:'merchantNo', title: '商户名称', width:110}
                    , {field: 'merchant_no', title: '商户名称', width: 110}
		    , {field: 'tname', title: '支付方式', width: 110}
                    , {field: 'money', title: '金额(元)', width: 90, sort: true}


                    //,{field:'orderStatus', title: '订单状态', width:120,templet:'#tpl_dd',sort:true}
                    , {field: 'order_status', title: '订单状态', width: 120, templet: '#tpl_dd', sort: true}
                    , {field: 'notifyStatus', title: '通知状态', width: 90, templet: '#tpl_tz'}

                    //,{field:'createTime', title: '创建时间', width:170,sort:true}
                    , {field: 'create_time', title: '创建时间', width: 170, sort: true}
                    //,{field:'payTime', title: '支付时间', width:170,sort:true}
                    , {field: 'pay_time', title: '支付时间', width: 170, sort: true}
                    , {field: 'platform', title: '设备类型', width: 90, templet: '#tpl_platform'}

//mod by astro

                    //,{field:'amount', title: '应付金额', width:130}
                    <adminPermission:hasPermission permission="admin:payorder:profit">
                    , {field: 'profit', title: '平台利润', width: 90}
                    </adminPermission:hasPermission>
                    //,{field:'payUrl', title: '支付链接', width:100,templet:'#tpl_url'}
                    , {field: 'pay_url', title: '支付链接', width: 100, templet: '#tpl_url'}

                    , {fixed: 'right', align: 'center', title: '操作', width: 200, toolbar: '#tpl_opera'}
                ]]
            });

            table.on('tool(table)', function (obj) {//tool event
                var data = obj.data; //获得当前行数据
                var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
                var tr = obj.tr; //获得当前行 tr 的DOM对象

                if (layEvent === 'edit') {
                    edit(data.id);
                } else if (layEvent === 'detail') {
                    detail(data.id, data.sn);
                } else if (layEvent === 'bd') {
                    bd(data.id);
                } else if (layEvent === 'xy') {
                    xy(data.id);
                } else if (layEvent == 'cd') {
                    cd(data.id);
                }
            });

            table.on('sort(table)', function (obj) {
                if (!obj.sorts) return;
                createSortView(obj.sorts);
                lay_sort = obj.sorts;
                searchData();
            });
            form.on('select(orderTpye)', function (data) {
                var orderStatus = $("#orderStatus").val()
                if (orderStatus==1){
                    $('#payTime').css('display','block')
                }else {
                    $('#payTime').css('display','none')
                }
            });


            initSelectAll();


        });
    });

    function cd(id) {
        commReq('cd', {id: id}, function () {
            searchData();
        });
    }

    function bd(id) {
        layer.open({
            content: '<label class="layui-form-label"><span style="color: red">*</span>验证码</label><div class="layui-input-block"><input type="number" name="key" id="key" lay-verify="required|number" lay-verType="tips" placeholder="请输入验证码" autocomplete="off" class="layui-input"/></div>',
            btn: ['确定', '取消'], //按钮
            yes:function (index,layero) {
                layer.close(index);//关闭弹层
                commReq('bd', {id: id,key:key.value}, function () {
                    searchData();
                });
            },
            btn2:function (index) {
                layer.close(index);
            }
        });
    }

    //更新状态
    function xy(id) {
        layer.confirm('是否通知商户？', {
            icon: 3,//询问图标
            btn: ['确定', '取消'] //按钮
        }, function (index) {
            layer.close(index);//关闭弹层

            commReq('xy', {id: id}, function () {
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

    //移除排序
    function removeSortField(field) {
        createSortView(table.removeSortField(field));
        searchData();
    }

    //搜索数据
    function searchData() {
        $('#searchBtn').attr({'disabled':'disabled'});
        tableIns.reload({
            where: getFormJsonData("#searchForm")
        });

        if ($('#needTotal').is(":checked")) {
            totalInfoData();
        } else {
            $("#totalInfo").html("请在需要的时候查看统计信息以减轻系统压力");
        }
        setTimeout(function () {
            $('#searchBtn').removeAttr("disabled");
        },3000)
    }

    function detail(id, sn) {
        lay_index = layer.open({
            type: 2,
            title: '订单详情-' + sn,
            skin: 'layui-layer-rim', //加上边框
            area: ['80%', '80%'], //宽高
            content: 'detail?id=' + id
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
            {field: 'clientSn', title: '上游订单号', width: 30, rowspan: 1, colspan: 1},
            {field: 'clientNo', title: '上游账号', width: 15, rowspan: 1, colspan: 1},
            {field: 'client_channel_no', title: '上游通道名', width: 20, rowspan: 1, colspan: 1},
            //{field:'clientName',title:'上游名',width:15,rowspan:1,colspan:1},
            {field: 'money', title: '成交金额(元)', width: 20, rowspan: 1, colspan: 1},
            {field: 'typeName', title: '通道类型', width: 15, rowspan: 1, colspan: 1},
            {field: 'createTime', title: '创建时间', width: 30, rowspan: 1, colspan: 1},
            {field: 'payTime', title: '成交时间', width: 30, rowspan: 1, colspan: 1},
            {field: 'clientRatio', title: '上游费率', width: 15, rowspan: 1, colspan: 1},
            {field: 'merchantRatio', title: '商户费率', width: 15, rowspan: 1, colspan: 1},
            {field: 'proxyRatio', title: '代理费率', width: 15, rowspan: 1, colspan: 1},
            {field: 'orderStatus', title: '订单状态', width: 15, rowspan: 1, colspan: 1}
            //{field:'merchantSn',title:'商户订单号',width:30,rowspan:1,colspan:1},
            , {field: 'merchantNo', title: '商户账号', width: 15, rowspan: 1, colspan: 1}
            , {field: 'merchant_name', title: '商户名', width: 15, rowspan: 1, colspan: 1}
            , {field: 'platform', title: '设备类型', width: 15, rowspan: 1, colspan: 1}
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
                // console.log(data);
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

    function totalInfoData() {
        $("#totalInfo").html("正在统计...");
        $.ajax({
            type: 'post',
            url: 'queryTotalAdmin',
            data: getFormJsonData("#searchForm"),
            dataType: "json",
            cache: false,
            success: function (data) {
                if (data.code == 200) {
                    var $r = data.result;
                    if ($r != null) {
                        var $percentage = 0;
                        if ($r.suc_count != 0){
                            $percentage = $r.suc_count * 100 / $r.all_count;
                            $percentage = $percentage.toFixed(2);
                        }
                        $("#totalInfo").html("当前数据中, 订单总数<span style='color:green'>"
                            + ($r.all_count ? $r.all_count : '0')
                            + "</span> 订单成交数<span style='color:blue'>"
                            + ($r.suc_count ? $r.suc_count : '0')
                            + "</span>元,  订单成功率<span style='color:red'>"
                            + ($percentage ? $percentage : '0.00')
                            + "</span>%, 订单总金额<span style='color:green'>"
                            + ($r.money ? $r.money : '0')
                            + "</span>元, 订单成交额<span style='color:blue'>"
                            + ($r.sucMoney ? $r.sucMoney : '0')
                            + "</span>元"
                        );
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

    //创建空单
    function createNullOrder() {
        lay_index = layer.open({
            type: 2,
            title: '创建空单',
            skin: 'layui-layer-rim', //加上边框
            area: ['80%', '80%'], //宽高
            content: 'nullOrder'
        });
    }

    function batchQueryOrder() {
        lay_index = layer.open({
            type: 2,
            title: '批量查单',
            skin: 'layui-layer-rim', //加上边框
            area: ['80%', '80%'], //宽高
            content: 'batchQueryOrder'
        });
    }

    function batchNotifyMerchant() {
        lay_index = layer.open({
            type: 2,
            title: '批量通知商户',
            skin: 'layui-layer-rim', //加上边框
            area: ['80%', '80%'], //宽高
            content: 'batchNotifyMerchant'
        });
    }
</script>
</body>
</html>
