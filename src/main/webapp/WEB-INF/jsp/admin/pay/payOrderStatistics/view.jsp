<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="adminPermission" uri="http://adminpermission.city.com" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>订单监控</title>
    <link rel="stylesheet" href="${context}/layui/css/layui.css">
    <link rel="stylesheet" href="${context}/css/style.css?${s_vs}">
    <!--[if lt IE 9]>
      <script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
      <script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<style>
    body {
        overflow-y: scroll;
    }

    /* 禁止刷新后出现横向滚动条 */
</style>
<body>

<div class="layui-fluid admin-head-title">
    <div class="layui-row left">
        <div class="layui-col-xs1 layui-col-sm1 layui-col-md1 left">
            <h3><b>&nbsp;订单监控</b></h3>
            
        </div>
        
        <div class="layui-col-xs10 layui-col-sm10 layui-col-md10 right">
            <button class="layui-btn layui-btn-sm layui-btn-normal" title="刷新页面" onclick="window.location.reload();">
                <i class="layui-icon">&#x1002;</i>
            </button>
        </div>
    </div>
</div>


<div class="admin-main">

    <fieldset class="layui-elem-field admin-search-form">
        <legend class="admin-search-title">5分钟自动刷新</legend>
        <form id="searchForm" class="layui-form">
            <div class="layui-row admin-search-row">

                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                    <select id="channelSel">
                        <option value="">选择渠道</option>
                    </select>
                </div>

                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                    <select id="merSel">
                        <option value="">选择商户</option>
                    </select>
                </div>

                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                    <select id="amountSel">
                        <option value="">选择金额</option>
                        <option value="30">30</option>
                        <option value="50">50</option>
                        <option value="100">100</option>
                        <option value="200">200</option>
                    </select>
                </div>

                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                    <select id="timeUnitSel">
                        <option value="120">2分钟</option>
                        <option value="300" selected="selected">5分钟</option>
                        <option value="600">10分钟</option>
                        <option value="1800">30分钟</option>
                    </select>
                </div>

                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2" style="text-align:center;">
                    <button class="layui-btn layui-btn-normal" type="button" id="orderBtn">精准查询</button>
                    <button class="layui-btn layui-btn-normal" type="button" id="todayBtn">刷新今日数据</button>
                    <%--	<button class="layui-btn layui-btn-normal" type="button" id="rateBtn">成功率定位</button>--%>
                </div>
            </div>
        </form>
    </fieldset>

    <%-- 5分钟一刷新 ？？？ 要有一个自动刷新的按钮  --%>

    <div class="layui-bg-gray" style="padding: 30px;">
        <div class="layui-row layui-col-space15">
            <div class="layui-col-md6">
                <div class="layui-card">
                    <div class="layui-card-header" style="font-size: large">今日进单</div>
                    <div class="layui-card-body">
                        <table id="todayTable"></table>
                    </div>

                    <div class="layui-card-header" style="font-size: large">入单监控....<span style="color: #0a6999;"
                                                                                          id="orderBeginStr">2021-05-13 10:24:00</span>-<span
                            style="color: #0a6999;" id="orderEndStr">2021-05-13 10:26:00</span></div>
                    <div class="layui-card-body">

                        <table id="orderChannelTable"></table>
                        <%--	<table id="orderChannelAmountTable" ></table>--%>
                        <table id="orderMerTable"></table>
                        <%--<table id="orderMerAmountTable" ></table>--%>
                        <%--<table id="orderMerChannelAmountTable" ></table>--%>

                    </div>
                </div>
            </div>

            <div class="layui-col-md6">
                <div class="layui-card">
                    <div class="layui-card-header" style="font-size: large">入单成功率监控....<span style="color: #0a6999;"
                                                                                             id="rateBeginStr">2021-05-13 10:24:00</span>-<span
                            style="color: #0a6999; " id="rateEndStr">2021-05-13 10:26:00</span></div>
                    <div class="layui-card-body">

                        <table id="rateChannelTable"></table>
                        <table id="rateChannelAmountTable"></table>
                        <%--<table id="rateMerTable" ></table>--%>
                        <table id="rateMerAmountTable"></table>
                        <%--<table id="rateMerChannelAmountTable" ></table>--%>

                    </div>
                </div>
            </div>
        </div>
    </div>

</div>

<script type="text/html" id="rateTpl">
    {{(d.count/(d.count+d.failCount)*100).toFixed(2)+'%'}}
</script>

<script src="${context}/js/jquery.min.js"></script>
<script src="${context}/layui/layui.js?${s_vs}"></script>
<script src="${context}/js/common.js?${s_vs}"></script>
<script type="text/javascript">
    var table;
    var form;

    $(function () {
        layui.use(['table', 'layer', 'form'], function () {
            table = layui.table;
            form = layui.form;

            var tableColsObj = {
                todayOrder: [[{field: 'count', title: '订单总数'}, {field: 'amount', title: '金额'}, {
                    field: 'count_s',
                    title: '成功'
                }, {field: 'count_f', title: '失败'}, {field: 'rate', title: '成功率'}]],
                orderChannelCols: [[{field: 'channelName', title: '渠道'}, {field: 'count', title: '入单'}]],
                //	orderChannelAmountCols:[[{field: 'channelName', title: '渠道'},{field: 'amount', title: '金额'},{field: 'count', title: '入单'}]],
                orderMerCols: [[{field: 'merchantName', title: '商户'}, {field: 'count', title: '入单'}]],
                //	orderMerAmountCols:[[{field: 'merchantName', title: '商户'},{field: 'amount', title: '金额'},{field: 'count', title: '入单'}]],
                //	orderMerChannelAmountCols:[[{field: 'merchantName', title: '商户'},{field: 'channelName', title: '上游渠道'},{field: 'amount', title: '金额'},{field: 'count', title: '入单'}]],

                rateChannelCols: [[{field: 'channelName', title: '渠道'}, {
                    field: 'count',
                    title: '成功'
                }, {field: 'failCount', title: '失败'}, {field: 'rate', title: '成功率', templet: '#rateTpl'}]],
                rateChannelAmountCols: [[{field: 'name', title: '渠道(成功/失败|成功率)'}, {
                    field: 'info1',
                    title: '30'
                }, {field: 'info2', title: '50'}, {field: 'info3', title: '100'}, {field: 'info4', title: '200'}]],
                //	rateMerCols:[[{field: 'merchantName', title: '商户'},{field: 'count', title: '成功'},{field: 'failCount', title: '失败'},{field: 'rate', title: '成功率', templet: '#rateTpl'}]],
                rateMerAmountCols: [[{field: 'name', title: '商户(成功/失败|成功率)'}, {
                    field: 'info1',
                    title: '30'
                }, {field: 'info2', title: '50'}, {field: 'info3', title: '100'}, {field: 'info4', title: '200'}]],
                //	rateMerChannelAmountCols:[[{field: 'merchantName', title: '商户'},{field: 'channelName', title: '渠道'},{field: 'amount', title: '金额'},{field: 'count', title: '成功'},{field: 'failCount', title: '失败'},{field: 'rate', title: '成功率', templet: '#rateTpl'}]]
            }

            var buildTable = function (tableId, tableCols, tableData) {
                table.render({
                    elem: '#' + tableId
                    , cols: tableCols
                    , data: tableData
                    , limit: 100
                });
            }

            var fillSelect = function (rateData) {
                // 填充select
                var channelSelect = {}
                var merSelect = {}
                //var amountSelect = {}

                for (var rData of rateData) {
                    channelSelect[rData.clientChannelId] = rData.channelName;
                    merSelect[rData.merchantId] = rData.merchantName;
                    //	amountSelect[rData.amount] = rData.amount;
                }

                $('#channelSel').empty();
                $('#merSel').empty();
                //	$('#amountSel').empty();

                var option = "<option value=''>选择渠道</option>";
                for (var x in channelSelect) {
                    option += "<option value='" + x + "'>" + channelSelect[x] + "</option>";
                }
                $('#channelSel').append(option);

                option = "<option value=''>选择商户</option>";
                for (var x in merSelect) {
                    option += "<option value='" + x + "'>" + merSelect[x] + "</option>";
                }
                $('#merSel').append(option);


                //console.log(amountSelect);
                /*option = "<option value=''>选择金额</option>";
                for (var x in amountSelect) {
                    option +="<option value='"+x+"'>"+x+"</option>";
                }*/
                //	$('#amountSel').append(option);

                form.render("select");
            }

            var hrefInComeOrder = function () {
                //canSelect();
                /*var channelValue = $('#channelSel').val();
                var merValue = $('#merSel').val();
                var amountValue = $('#amountSel').val();*/

                //var tableId = getTableId(channelValue,merValue,amountValue,"order");

                //	$('html, body').animate({scrollTop: $('#'+tableId).offset().top}, 1000)

                // 异步 查询

                refreshIncodeOrder();
                refreshRateStatistics();

            }

            /*var hrefRate = function (){
                canSelect();
                var channelValue = $('#channelSel').val();
                var merValue = $('#merSel').val();
                var amountValue = $('#amountSel').val();
                //var tableId = getTableId(channelValue,merValue,amountValue,"rate");

            //	$('html, body').animate({scrollTop: $('#'+tableId).offset().top}, 1000)



            }*/

            /*function getTableId(channelValue,merValue,amountValue,type){
                if (""!=channelValue&&""!=merValue&&""!=amountValue){
                    if (type='order') return 'orderMerChannelAmountTable';
                    else if (type='rate') return 'rateMerChannelAmountTable';
                    return ;
                }

                if (""!=channelValue&&""!=merValue&&""==amountValue){
                    layer.alert('暂无此表格，等待下一版本', {
                        icon: 2
                    });
                    return ;
                }

                if (""!=channelValue&&""==merValue&&""!=amountValue){
                    if (type='order') return 'orderChannelAmountTable';
                    else if (type='rate') return 'rateChannelAmountTable';
                    return ;
                }

                if (""!=channelValue&&""==merValue&&""==amountValue){
                    if (type='order') return 'orderChannelTable';
                    else if (type='rate') return 'rateChannelTable';
                    return ;
                }

                if (""==channelValue&&""!=merValue&&""==amountValue){
                    if (type='order') return 'orderMerTable';
                    else if (type='rate') return 'rateMerTable';
                    return ;
                }

                if (""==channelValue&&""!=merValue&&""!=amountValue){
                    if (type='order') return 'orderMerAmountTable';
                    else if (type='rate') return 'rateMerAmountTable';
                    return ;
                }

            }*/

            /*function canSelect(){
                var channelValue = $('#channelSel').val();
                var merValue = $('#merSel').val();
                var amountValue = $('#amountSel').val();
                if (""!=amountValue&&(""==merValue&&""==channelValue)){
                    layer.alert('渠道或商户必须选择', {
                        icon: 2
                    });
                    throw '渠道或商户必须选择';
                }
            }*/

            $('#orderBtn').click(hrefInComeOrder);
            //$('#rateBtn').click(hrefRate);

            var getSelectData = function () {
                return {
                    channelName: $('#channelSel').val(),
                    merName: $('#merSel').val(),
                    amount: $('#amountSel').val(),
                    timeUnit: $('#timeUnitSel').val()
                };
            }

            var refreshTodayOrder = function () {

                $.ajax({
                    type: 'post',
                    //data : getSelectData(),
                    url: "${context}/system/payOrderStatistics/todayOrderInfo",
                    cache: false,
                    success: function (data) {
                        if (data.code == 200) {
                            var body = data.result;
                            var todayData = makeTodayData(body);
                            // 刷新列表
                            buildTable('todayTable', tableColsObj.todayOrder, [todayData]);
                        } else {
                            layer.msg(data.msg);
                        }
                    },
                    error: function (xhr, desc, err) {
                        layer.msg("数据请求失败:" + desc);
                    }
                });

            }

            var makeTodayData = function (rData) {
                var todayObj = {
                    count: 0,
                    amount: 0,
                    count_s: 0,
                    count_f: 0,
                }
                for (var dataObj of rData) {
                    todayObj.count += dataObj.count;
                    if (1 == dataObj.orderStatus) {
                        todayObj.count_s += dataObj.count;
                        todayObj.amount += dataObj.amount;
                    }else{
                        todayObj.count_f += dataObj.count;
                    }
                }

                todayObj.rate = (todayObj.count_s / todayObj.count * 100).toFixed(2) + '%';

                return todayObj;
            }

            $('#todayBtn').click(refreshTodayOrder);

            // 初始化进单列表
            var refreshIncodeOrder = function () {
                $.ajax({
                    type: 'post',
                    data: getSelectData(),
                    url: "${context}/system/payOrderStatistics/inComeOrder",
                    cache: false,
                    success: function (data) {
                        if (data.code == 200) {
                            var body = data.result;
                            // 刷新列表
                            buildTable('orderChannelTable', tableColsObj.orderChannelCols, body.channel);
                            //	buildTable('orderChannelAmountTable',tableColsObj.orderChannelAmountCols,body.channelAmount);
                            buildTable('orderMerTable', tableColsObj.orderMerCols, body.mer);
                            //	buildTable('orderMerAmountTable',tableColsObj.orderMerAmountCols,body.merAmount);
                            //	buildTable('orderMerChannelAmountTable',tableColsObj.orderMerChannelAmountCols,body.merChannelAmount);

                            //  时间处理一下
                            $('#orderBeginStr').html(body.begin);
                            $('#orderEndStr').html(body.end);
                        } else {
                            layer.msg(data.msg);
                        }
                    },
                    error: function (xhr, desc, err) {
                        layer.msg("数据请求失败:" + desc);
                    }
                });
            }

            // 初始化成功率列表
            var refreshRateStatistics = function () {
                $.ajax({
                    type: 'post',
                    data: getSelectData(),
                    url: "${context}/system/payOrderStatistics/rateStatistics",
                    cache: false,
                    success: function (data) {
                        if (data.code == 200) {
                            var body = data.result;
                            // 刷新列表
                            buildTable('rateChannelTable', tableColsObj.rateChannelCols, body.channel);
                            //		buildTable('rateMerTable',tableColsObj.rateMerCols,body.mer);
                            //			buildTable('rateMerChannelAmountTable',tableColsObj.rateMerChannelAmountCols,body.merChannelAmount);

                            //  数据 分组 30 50 100 200
                            var rateChannelAmoutValue = makeData(body.channelAmount, "channelName");
                            buildTable('rateChannelAmountTable', tableColsObj.rateChannelAmountCols, rateChannelAmoutValue);
                            var rateMerAmoutValue = makeData(body.merAmount, "merchantName");
                            buildTable('rateMerAmountTable', tableColsObj.rateMerAmountCols, rateMerAmoutValue);

                            //  时间处理一下
                            $('#rateBeginStr').html(body.begin);
                            $('#rateEndStr').html(body.end);

                            // 重置select
                            fillSelect(body.merChannelAmount);

                        } else {
                            layer.msg(data.msg);
                        }
                    },
                    error: function (xhr, desc, err) {
                        layer.msg("数据请求失败:" + desc);
                    }
                });
            }

            var makeData = function (ajaxData, fildName) {

                var result = {};

                for (var channelData of ajaxData) {
                    if (null == result[channelData[fildName]]) {
                        var obj = {}
                        var amountObj = {};
                        amountObj.count = channelData.count;
                        amountObj.failCount = channelData.failCount;
                        obj["amount_" + channelData.amount] = amountObj;
                        result[channelData[fildName]] = obj;
                    } else {
                        var obj = result[channelData[[fildName]]];
                        if (null == obj["amount_" + channelData.amount]) {
                            var amountObj = {};
                            amountObj.count = channelData.count;
                            amountObj.failCount = channelData.failCount;
                            obj["amount_" + channelData.amount] = amountObj;
                        } else {
                            var amountObj = obj["amount_" + channelData.amount];
                            amountObj.count += channelData.count;
                            amountObj.failCount += channelData.failCount;
                        }
                    }
                }

                var resultArr = [];

                for (var resultFild in result) {
                    var obj = result[resultFild];
                    var arrObj = {}
                    arrObj.name = resultFild;
                    if (null != obj["amount_30"]) {
                        var amObj = obj["amount_30"];
                        arrObj.info1 = amObj.count + " / <span style='color: red'>" + amObj.failCount + "</span> | <span style='color: blue'>" + ((amObj.count / (amObj.count + amObj.failCount) * 100).toFixed(2) + '</span>%');
                    }

                    if (null != obj["amount_50"]) {
                        var amObj = obj["amount_50"];
                        arrObj.info2 = amObj.count + " / <span style='color: red'>" + amObj.failCount + "</span> | <span style='color: blue'>" + ((amObj.count / (amObj.count + amObj.failCount) * 100).toFixed(2) + '</span>%');
                    }

                    if (null != obj["amount_100"]) {
                        var amObj = obj["amount_100"];
                        arrObj.info3 = amObj.count + " / <span style='color: red'>" + amObj.failCount + "</span> | <span style='color: blue'>" + ((amObj.count / (amObj.count + amObj.failCount) * 100).toFixed(2) + '</span>%');
                    }

                    if (null != obj["amount_200"]) {
                        var amObj = obj["amount_200"];
                        arrObj.info4 = amObj.count + " / <span style='color: red'>" + amObj.failCount + "</span> | <span style='color: blue'>" + ((amObj.count / (amObj.count + amObj.failCount) * 100).toFixed(2) + '</span>%');
                    }
                    resultArr.push(arrObj);
                }


                //	console.log(resultArr);

                return resultArr;
            }

            refreshIncodeOrder();
            refreshRateStatistics();
            refreshTodayOrder();

            //5分钟刷新一次
            var timer = setInterval(function () {
                layer.msg("刷新");
                refreshIncodeOrder();
                refreshRateStatistics();
            }, 300000)


            /*table.on('sort(table)', function(obj){
                if(!obj.sorts) return;

            });*/

        });
    });


</script>
</body>
</html>
