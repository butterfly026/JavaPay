<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="adminPermission" uri="http://adminpermission.city.com" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>提现管理</title>
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
            <h3><b>&nbsp;提现管理</b></h3>
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
        <legend class="admin-search-title">提现查询</legend>
        <form id="searchForm" class="layui-form">
            <div class="layui-row admin-search-row">

                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                    <input type="text" name="sn" id="sn" placeholder="系统订单号" class="layui-input"/>
                </div>
                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                    <input type="text" name="merchantSn" id="merchantSn" placeholder="商户订单号" class="layui-input"/>
                </div>
                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                    <input type="text" name="bankAccname" id="bankAccname" placeholder="持卡人姓名" class="layui-input"/>
                </div>


            </div>

            <div class="layui-row admin-search-row">
                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                    <input type="text" name="startTime" id="startTime" placeholder="开始时间" class="layui-input"
                           autocomplete="off"/>
                </div>
                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                    <input type="text" name="endTime" id="endTime" placeholder="结束时间" class="layui-input"
                           autocomplete="off"/>
                </div>

                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                    <input type="text" name="payStartTime" id="payStartTime" placeholder="完成时间-开始" class="layui-input"
                           autocomplete="off"/>
                </div>
                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                    <input type="text" name="payEndTime" id="payEndTime" placeholder="完成时间-结束" class="layui-input"
                           autocomplete="off"/>
                </div>

                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                    <select name="payStatus">
                        <option value="">订单状态</option>
                        <option value="0">等待审核</option>
                        <option value="1">失败</option>
                        <option value="2">已拒绝</option>
                        <option value="3">已通过</option>
                        <option value="4">处理中</option>
                        <option value="5">已支付</option>
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
        <adminPermission:hasPermission permission="admin:paycash:add">
            <button class="layui-btn layui-btn-sm layui-btn-normal" onclick="add();">
                申请提现
            </button>
        </adminPermission:hasPermission>

        <adminPermission:hasPermission permission="admin:paycash:addBatch">
            <button class="layui-btn layui-btn-sm" onclick="addBatch();">
                批量申请提现
            </button>
        </adminPermission:hasPermission>

        <adminPermission:hasPermission permission="admin:paycash:merchantExport">
            <button class="layui-btn layui-btn-sm layui-btn-normal" onclick="exportExcel();">
                导出Excel
            </button>

            <button class="layui-btn layui-btn-sm layui-btn-normal" onclick="showExportView();">
                历史导出数据
            </button>
        </adminPermission:hasPermission>

        &nbsp;
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

    <script type="text/html" id="tpl_tx">
        {{# if(d.cashType && d.cashType==1){ }}
        API发起
        {{# }else{ }}
        商户发起
        {{# } }}
    </script>

    <script type="text/html" id="tpl_ps">
        {{# if(d.payStatus==0){ }}
        等待审核
        {{# }else if(d.payStatus==1){ }}
        <span class="layui-badge">失败</span>
        {{# }else if(d.payStatus==2){ }}
        <span class="layui-badge">已拒绝</span>
        {{# }else if(d.payStatus==3){ }}
        <span class="layui-badge layui-bg-blue">审核通过</span>
        {{# }else if(d.payStatus==4){ }}
        <span class="layui-badge layui-bg-orange">处理中</span>
        {{# }else if(d.payStatus==5){ }}
        <span class="layui-badge layui-bg-green">已支付</span>
        {{# }else{ }}
        其他
        {{# } }}
    </script>

    <script type="text/html" id="tpl_ns">
        {{# if(d.dealStatus==1){ }}
        <span class="layui-badge layui-bg-blue">已通知</span>
        {{# } }}
    </script>

    <script type="text/html" id="tpl_opera">

	</script>

    <script type="text/html" id="tpl_index">
        {{ d.LAY_INDEX }}
    </script>

    <script type="text/html" id="tpl_btype">

        {{# if(d.btype!=null && d.btype=="0"){ }}
        <span class="layui-badge layui-bg-green">银行卡</span>
        {{# } }}

        {{# if(d.btype!=null && d.btype=="1"){ }}
        <span class="layui-badge layui-bg-blue">UPI账号</span>
        {{# } }}

        {{# if(d.btype!=null && d.btype=="2"){ }}
        <span class="layui-badge layui-bg-green">USDT</span>
        {{# } }}

    </script>

    <script type="text/html" id="tpl_ctype">
        {{# if(d.channelType && d.channelType=="1"){ }}
        <span class="layui-badge">代付</span>
        {{# }else{ }}
        <span class="layui-badge layui-bg-cyan">下发</span>
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
        layui.use(['table', 'layer', 'laydate'], function () {
            table = layui.table;

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

            laydate.render({
                elem: '#payStartTime' //指定元素
                , type: "datetime"
                , format: 'yyyy-MM-dd HH:mm:ss'
            });

            laydate.render({
                elem: '#payEndTime' //指定元素
                , type: "datetime"
                , format: 'yyyy-MM-dd HH:mm:ss'
            });

            tableIns = table.render({
                id: 'id',//用于获取被选中的项
                elem: '#datatable'
                , size: 'sm'
                , height: $tableheight
                , url: 'merchantList' //数据接口
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

                    {field: 'id', title: '序号', width: 70, type: 'checkbox'}
                    , {field: 'merchantNo', title: '商户号', width: 130}
                    , {field: 'sn', title: '订单号', width: 130}
                    , {field: 'createTime', title: '创建时间', width: 130}
                    , {field: 'payStatus', title: '状态', width: 100, templet: '#tpl_ps'}
                    , {field: 'merchantSn', title: '商户订单号', width: 130}
                    , {field: 'cashType', title: '提现来源', width: 100, templet: '#tpl_tx'}

                    , {field: 'realMoney', title: '提现金额(${sysconfig.currency })', width: 100}
                    , {field: 'commission', title: '手续费(${sysconfig.currency })', width: 100}
                    , {field: 'amount', title: '扣减余额(${sysconfig.currency })', width: 100}

                    , {field: 'channelType', title: '提现类型', width: 80, templet: '#tpl_ctype'}
                    , {field: 'btype', title: '账号类型', width: 100, templet: '#tpl_btype'}
                    , {field: 'bankAccno', title: '提现账号', width: 130}
                    , {field: 'bankAccname', title: '持卡人', width: 130}
                    , {field: 'bankName', title: '开户银行', width: 130}

                    <c:choose>
                    <c:when test="${sysconfig.cashMode==1}">
                    , {field: 'bankProvince', title: '开户行省份', width: 150}
                    , {field: 'bankCity', title: '开户行城市', width: 150}
                    , {field: 'bankSubname', title: '支行名', width: 120}
                    </c:when>
                    <c:otherwise>
                    , {field: 'bankIfsc', title: 'IFSC', width: 150}
                    , {field: 'bankNation', title: '国家', width: 150}
                    </c:otherwise>
                    </c:choose>

                    //,{field:'bankSubname', title: '支行名', width:130}


                    , {field: 'dealStatus', title: '通知状态', width: 100, templet: '#tpl_ns'}


                    , {field: 'payTime', title: '支付时间', width: 130}

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
                }
            });

            table.on('sort(table)', function (obj) {
                if (!obj.sorts) return;
                createSortView(obj.sorts);
                lay_sort = obj.sorts;
                searchData();
            });
            initSelectAll();
            initTableSelect();
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
            title: '新增商户提现',
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
            title: '编辑商户提现',
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

            {field: 'money', title: '提现金额(${sysconfig.currency })', width: 20, rowspan: 1, colspan: 1},
            {field: 'commission', title: '手续费(${sysconfig.currency })', width: 20, rowspan: 1, colspan: 1},
            {field: 'amount', title: '扣减余额(${sysconfig.currency })', width: 20, rowspan: 1, colspan: 1},

            {field: 'createTime', title: '发起时间', width: 30, rowspan: 1, colspan: 1},
            {field: 'payTime', title: '成交时间', width: 30, rowspan: 1, colspan: 1},

            {field: 'cashType', title: '提现来源', width: 30, rowspan: 1, colspan: 1},
            {field: 'channelType', title: '提现类型', width: 30, rowspan: 1, colspan: 1},

            {field: 'cardNo', title: '银行卡号', width: 30, rowspan: 1, colspan: 1},
            {field: 'bankName', title: '银行', width: 30, rowspan: 1, colspan: 1},
            {field: 'cardName', title: '持卡人', width: 30, rowspan: 1, colspan: 1},

            <c:choose>
            <c:when test="${sysconfig.cashMode==1}">
            {field: 'province', title: '开户行省份', width: 30, rowspan: 1, colspan: 1},
            {field: 'city', title: '开户行城市', width: 30, rowspan: 1, colspan: 1},
            {field: 'bankSubname', title: '支行名', width: 30, rowspan: 1, colspan: 1},
            </c:when>
            <c:otherwise>
            {field: 'ifsc', title: 'IFSC', width: 30, rowspan: 1, colspan: 1},
            {field: 'nation', title: '国家', width: 30, rowspan: 1, colspan: 1},
            </c:otherwise>
            </c:choose>


            {field: 'payStatus', title: '提现状态', width: 15, rowspan: 1, colspan: 1},
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
            url: 'exportExcelMerchant',
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
            content: '../payExcelexport/view?source=1'
        });
    }

    function addBatch() {
        lay_index = layer.open({
            type: 2,
            title: '商户批量提现申请',
            skin: 'layui-layer-rim', //加上边框
            area: ['80%', '80%'], //宽高
            content: 'addBatch'
        });
    }
</script>
</body>
</html>
