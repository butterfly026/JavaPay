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
        <div class="layui-col-xs11 layui-col-sm11 layui-col-md11">
            <h3><b>&nbsp;订单监控</b></h3>
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
        <legend class="admin-search-title">订单监控</legend>
        <form id="searchForm" class="layui-form">
            <div class="layui-row admin-search-row">
                <div class="layui-col-xs1 layui-col-sm1 layui-col-md1">
                    <select id="merSel" name="merSel">
                        <option value="">选择商户</option>
                    </select>
                </div>

                <div class="layui-col-xs1 layui-col-sm1 layui-col-md1">
                    <select id="channelSel" name="channelSel">
                        <option value="">商户通道</option>
                    </select>
                </div>
               
                <div class="layui-col-xs2 layui-col-sm2 layui-col-md1" >
                    <select id="amountSel" name="amountSel">
                        <option value="">选择金额</option>
                        <option value="30">30</option>
                        <option value="50">50</option>
                        <option value="100">100</option>
                        <option value="200">200</option>
                        <option value="other">其他金额</option>
                    </select>
                </div>

                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                    <input type="text" name="startTime" id="startTime" placeholder="开始时间" class="layui-input" autocomplete="off"/>
                </div>

                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                    <input type="text" name="endTime" id="endTime" placeholder="结束时间" class="layui-input" autocomplete="off"/>
                </div>

                <div class="layui-col-xs4 layui-col-sm4 layui-col-md4" style="text-align:left;">
                    <button class="layui-btn layui-btn-normal" type="button" id="clearBtn" style="margin-left:5px">重置</button>
                    <button class="layui-btn layui-btn-normal" type="button" id="orderBtn" style="margin-left:3px">查询</button>
<%--                    <button class="layui-btn layui-btn-normal" type="button" id="order5Btn" style="margin-left:30px">5分钟</button>--%>
                    <button class="layui-btn layui-btn-normal" type="button" id="order10Btn" style="margin-left:30px">10分钟</button>
                    <button class="layui-btn layui-btn-normal" type="button" id="order30Btn" style="margin-left:3px">30分钟</button>
                    <button class="layui-btn layui-btn-normal" type="button" id="order1hBtn" style="margin-left:3px">1小时</button>
                    <button class="layui-btn layui-btn-normal" type="button" id="order2hBtn" style="margin-left:3px">2小时</button>
                    <button class="layui-btn layui-btn-normal" type="button" id="order4hBtn" style="margin-left:3px">4小时</button>
                    <button class="layui-btn layui-btn-normal" type="button" id="ordertodayBtn" style="margin-left:3px" >今日数据</button>
                    
                    <%--	<button class="layui-btn layui-btn-normal" type="button" id="todayBtn">刷新今日数据</button>--%>
                    <%--	<button class="layui-btn layui-btn-normal" type="button" id="rateBtn">成功率定位</button>--%>                    
                </div>  


            </div>
        </form>
    </fieldset>

    <%-- 5分钟一刷新 ？？？ 要有一个自动刷新的按钮  --%>
    <div class="layui-col-md12">

            <div class="layui-col-xs2 layui-col-sm2 layui-col-md2" >
                <input type="checkbox" name="check_filterEmpty" id="check_filterEmpty" value="1" checked="true">隐藏合计为0的数据
            </div>
            <div class="layui-col-xs3 layui-col-sm3 layui-col-md3" >
                <div class="layui-col-xs3 layui-col-sm3 layui-col-md3" >
                    <input type="checkbox" name="check_autoRef" id="check_autoRef" value="0" checked="false">启用自动刷新
                </div>
                <div class="layui-col-xs4 layui-col-sm4 layui-col-md4" >
                    <input type="text" id="text_Time" name="text_Time" class="layui-input"  style="height:22px" placeholder="0" value="60" text="60">
                </div>
                <div class="layui-col-xs5 layui-col-sm5 layui-col-md5" style="padding-left:10px">
                    秒
                </div>
                
            </div>


        <div class="layui-col-xs7 layui-col-sm7 layui-col-md7" id="searchTitle" name="searchTitle" style="text-align:right;">
            <span id="searchMem" name="searchMem" style='color: red'>搜索类型：</span>
        </div>
    </div>

    <div class="layui-col-md12">
        <div class="layui-card-header" style="font-size: large">今日进单</div>
            <div class="layui-card-body">
                <table id="todayTable"></table>
            </div>
    </div>

        <div class="layui-col-md12">

            <div class="layui-card">
                    <table id="rateMerAmountTable"></table>
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
        layui.use(['table', 'layer', 'form','laydate'], function () {
            table = layui.table;
            form = layui.form;
            var laydate = layui.laydate;

            laydate.render({
                elem: '#startTime' //指定元素
                , type: "datetime"
                , format: 'yyyy-MM-dd HH:mm:ss'
            });
            //                , value: new Date(new Date().setHours(0, 0, 0, 0))
            laydate.render({
                elem: '#endTime' //指定元素
                , type: "datetime"
                , format: 'yyyy-MM-dd HH:mm:ss'
            });

            var tableColsObj = {
                todayOrder: [[{field: 'count', title: '订单总数'}, {field: 'amount', title: '金额'}, 
                            {field: 'count_s',title: '成功'}, {field: 'count_f', title: '失败'}, {field: 'rate', title: '成功率'}]],
                orderChannelCols: [[{field: 'channelName', title: '渠道'}, {field: 'count', title: '入单'}]],
                //	orderChannelAmountCols:[[{field: 'channelName', title: '渠道'},{field: 'amount', title: '金额'},{field: 'count', title: '入单'}]],
                orderMerCols: [[{field: 'merchantName', title: '商户'}, {field: 'count', title: '入单'}]],
                //	orderMerAmountCols:[[{field: 'merchantName', title: '商户'},{field: 'amount', title: '金额'},{field: 'count', title: '入单'}]],
                //	orderMerChannelAmountCols:[[{field: 'merchantName', title: '商户'},{field: 'channelName', title: '上游渠道'},{field: 'amount', title: '金额'},{field: 'count', title: '入单'}]],

                rateChannelCols: [[{field: 'channelName', title: '渠道'},{field: 'count',title: '成功'}, 
                                {field: 'failCount', title: '失败'}, {field: 'rate', title: '成功率', templet: '#rateTpl'}]],
                rateChannelAmountCols: [[{field: 'name', title: '渠道(成功/失败|成功率)'}, 
                                        {field: 'info1',title: '30'}, {field: 'info2', title: '50'}, 
                                        {field: 'info3', title: '100'}, {field: 'info4', title: '200'}]],
                //	rateMerCols:[[{field: 'merchantName', title: '商户'},{field: 'count', title: '成功'},{field: 'failCount', title: '失败'},{field: 'rate', title: '成功率', templet: '#rateTpl'}]],
                rateMerAmountCols: [[
                    {field: 'merchantName', title: '商户名'},
                    {field: 'merchantNickname', title: '商户昵称'},
                    {field: 'tname', title: '商户通道'},
                    {field: 'amount_30', title: '30'},
                    {field: 'amount_50', title: '50'},
                    {field: 'amount_100', title: '100'},
                    {field: 'amount_200', title: '200'},
                    {field: 'other', title: '其他'},
                    {field: 'total', title: '合计'}]],
                //	rateMerChannelAmountCols:[[{field: 'merchantName', title: '商户'},{field: 'channelName', title: '渠道'},{field: 'amount', title: '金额'},{field: 'count', title: '成功'},{field: 'failCount', title: '失败'},{field: 'rate', title: '成功率', templet: '#rateTpl'}]]
            }

            var buildTable = function (tableId, tableCols, tableData) {
                table.render({
                    elem: '#' + tableId
                    , cols: tableCols
                    , data: tableData
                    , limit: 100
                    /*
                     , done : function(res, curr, count) {
                         merge(res);
                     },
                     */
                });
            }

            $('#check_autoRef').click(function(){
                refPageProc();
            });

            $('#check_filterEmpty').click(function(){
                showZeroTotal(!$('#check_filterEmpty').is(":checked"));
            });
            function showZeroTotal(b){
                //var alltr=$(".layui-table-body>.layui-table");
                
                var alltr=$('.layui-card').find('table tbody [data-index]');
                    var allTotal=alltr.find('[data-field="total"] div');
                    for(var cnt=0;cnt<allTotal.length;cnt++){
                        if(allTotal[cnt].innerText=="0")
                        {
                            if(b){
                                alltr[cnt].removeAttribute("style");
                            }else{
                                alltr[cnt].setAttribute("style","display: none");
                            }
                        }
                    }
            }

            var fillSelect = function (rateData) {
                // 填充select
                var channelSelect = {};
                //var SYchannelSelect={};
                var merSelect = {};
                //var amountSelect = {}

                for (var rData of rateData) {
                    //if(rData.channelName==null || rData.channelName=="")continue;
                    //if(null==rData.amount || rData.amount<=0 || rData.amount=="")continue;
                    //if(rData.orderStatus==0)continue;
                    if(rData.tname=="" || rData.tname==null)continue;
                    if(rData.channelName=="" || rData.channelName==null)continue;
                    channelSelect[rData.tname] = rData.tname;
                    //SYchannelSelect[rData.channelName] = rData.channelName;
                    //channelSelect[rData.clientChannelId] = rData.channelName;

                    merSelect[rData.merchantId] = rData.merchantName;

                    //	amountSelect[rData.amount] = rData.amount;
                }

                $('#channelSel').empty();
                $('#merSel').empty();
                //$('#SYchannelSel').empty();
                
                //	$('#amountSel').empty();
                /*
                var option = "<option value=''>上游通道</option>";
                for (var x in SYchannelSelect) {
                    option += "<option value='" + x + "'>" + SYchannelSelect[x] + "</option>";
                }
                $('#SYchannelSel').append(option);
                */
                
                
                var option = "<option value=''>商户通道</option>";
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

            var hrefInComeOrder = function (tu) {
                //canSelect();
                /*var channelValue = $('#channelSel').val();
                var merValue = $('#merSel').val();
                var amountValue = $('#amountSel').val();*/

                //var tableId = getTableId(channelValue,merValue,amountValue,"order");

                //	$('html, body').animate({scrollTop: $('#'+tableId).offset().top}, 1000)

                // 异步 查询

                //refreshIncodeOrder();
                refreshRateStatistics(tu);

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
            $('#orderBtn').on("click", function () {
                if($('#startTime').val()!="" &&  $('#endTime').val()!=""){
                    $("#searchMem").text("自定义搜索:" + $('#startTime').val() +" - "+ $('#endTime').val());
                    hrefInComeOrder(0);
                }
                
                updateTable()

                
            });
            $('#order5Btn').on("click", function () {
                hrefInComeOrder(300);//5分钟
                $("#searchMem").text("5分钟结果:");
            });
            $('#order10Btn').on("click", function () {
                hrefInComeOrder(60*10);//10分钟
                $("#searchMem").text("10分钟结果:");
            });
            $('#order30Btn').on("click", function () {
                hrefInComeOrder(60*30);//30分钟
                $("#searchMem").text("30分钟结果:");
            });
            $('#order1hBtn').on("click", function () {
                hrefInComeOrder(60*60);//1小时
                $("#searchMem").text("1小时结果:");
            });
            $('#order2hBtn').on("click", function () {
                hrefInComeOrder(60*60*2);//2小时
                $("#searchMem").text("2小时结果:");
            });
            $('#order4hBtn').on("click", function () {
                hrefInComeOrder(60*60*4);//4小时
                $("#searchMem").text("4小时结果:");
            });
            $('#ordertodayBtn').on("click",function () {
                refreshTodayOrder();
            });
            
            function updateTable(){
                var stype=["10分钟","30分钟","1小时","2小时","4小时","自定义"];
                var tmi=[60*10,60*30,60*60,60*60*2,60*60*4,0];
                for(var cnt=0;cnt<stype.length;cnt++){
                    if($("#searchMem").text().indexOf(stype[cnt])!=-1){
                        hrefInComeOrder(tmi[cnt]);
                        break;
                    }
                }
            }
            /*
            $('#ordertodayBtn').on("click", function () {
                hrefInComeOrder(-1);//今天
                $("#searchMem").text("今日数据:");

            });
            */
            $('#clearBtn').on("click", function () {
                $('#startTime').val("");
                $('#endTime').val("");
                $('#channelSel').val("");
                $('#merSel').val("");
                $('#amountSel').val("");
                form.render();
            });  

            var getSelectData = function () {
                return {
                    startTime:$('#startTime').val(),
                    endTime:$('#endTime').val(),
                    tname:$('#channelSel').val(),
                    //cname: $('#SYchannelSel').val(),
                    merName: $('#merSel').val(),
                    amount: $('#amountSel').val(),
                    //mcid:$('#SYchannelSel').val(),
                    timeUnit: ""
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
            var refreshRateStatistics = function (tu) {
                var formData = getSelectData();
                if(tu==-1)
                {
                    formData.startTime="";
                    formData.endTime="";
                }
                formData.timeUnit=tu;
                $.ajax({
                    type: 'post',
                    data: formData,
                    url: "${context}/system/payOrderStatistics/rateStatisticsNew",
                    cache: false,
                    success: function (data) {
                        if (data.code == 200) {
                            var body = data.result;
                            // 刷新列表
                            //buildTable('rateChannelTable', tableColsObj.rateChannelCols, body.channel);
                            //		buildTable('rateMerTable',tableColsObj.rateMerCols,body.mer);
                            //			buildTable('rateMerChannelAmountTable',tableColsObj.rateMerChannelAmountCols,body.merChannelAmount);

                            //  数据 分组 30 50 100 200
                           // var rateChannelAmoutValue = makeData(body.channelAmount, "channelName");
                            //buildTable('rateChannelAmountTable', tableColsObj.rateChannelAmountCols, rateChannelAmoutValue);
                            var rateMerAmoutValue = makeData(body, "merStatistics");
                            buildTable('rateMerAmountTable', tableColsObj.rateMerAmountCols, rateMerAmoutValue);

                            //  时间处理一下
                            $('#rateBeginStr').html(body.begin);
                            $('#rateEndStr').html(body.end);

                            // 重置select
                            fillSelect(body.merStatistics);
                            //默认隐藏统计为0的数据
                            showZeroTotal(!$('#check_filterEmpty').is(":checked"));
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
/*
merStatistics:
                amount: 30
                begin: null
                channelName: "畅付CF-微信慢充"
                clientChannelId: 87
                count: 127
                end: null
                failCount: 119
                merchantId: 327
                merchantName: "S087"
                merchantNickname: "TNZF"
                orderStatus: 0



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
*/
                var result = {};
                console.log(ajaxData[fildName]);
                for (var item of ajaxData[fildName])
                {
                    //if(null==item.amount || item.amount<=0 || item.amount=="")continue;

                    var amountObj={};
                    var itemkey=item.merchantId+"_"+item.tname;
                    var ikey="amount_"+item.amount;
                    if(item.channelName==null || item.channelName=="")continue;

                    if(!result[itemkey]){
                        result[itemkey]={};
                    }
                    if(!result[itemkey][ikey]){
                        result[itemkey][ikey]={};
                        amountObj =result[itemkey][ikey];
                        amountObj.count = 0;
                        amountObj.failCount = 0;
                        amountObj.total=0;
                    }
                    amountObj=result[itemkey][ikey];
                    if(item.orderStatus==1){
                        amountObj.count += item.count;
                        amountObj.total += item.count * item.amount;
                    }else{
                        amountObj.failCount += item.count;
                    }
                    amountObj.merchantName = item.merchantName;
                    //amountObj.mcid = item.mcid;
                    //amountObj.mcname = item.mcname;
                    amountObj.merchantNickname = item.merchantNickname;
                    amountObj.tname = item.tname;
                    //amountObj.stname = item.channelName;
                   // amountObj.channelName = item.mcname;

                }
                var resultArr = [];
                for (var resultFild in result) {
                    var ikey=["amount_30","amount_50","amount_100","amount_200","amount_10","amount_5"];
                    var obj = result[resultFild];
                    //console.log(JSON.stringify(obj));
                    var arrObj = {}
                    var total=0;
                    for(var k of ikey){
                        if (obj[k]) {

                            var amObj = obj[k];
                            var otherKey=ikey.indexOf(k);
                            if(otherKey>3){
                                arrObj.other = amObj.count + " / <span style='color: red'>" + amObj.failCount + "</span> | <span style='color: blue'>" + ((amObj.count / (amObj.count + amObj.failCount) * 100).toFixed(2) + '</span>%');
                            }else{
                                arrObj[k] = amObj.count + " / <span style='color: red'>" + amObj.failCount + "</span> | <span style='color: blue'>" + ((amObj.count / (amObj.count + amObj.failCount) * 100).toFixed(2) + '</span>%');
                            }
                            
                            arrObj.name = resultFild;
                            //arrobj.mcname=amObj.mcname;
                            //arrobj.mcid=amObj.mcid;
                            arrObj.merchantName = amObj.merchantName;
                            arrObj.merchantNickname = amObj.merchantNickname;
                            //arrObj.channelName = amObj.mcname;
                            arrObj.tname = amObj.tname;
                            //arrObj.stname = amObj.stname;                            
                            total += amObj.total;
                        }
                    }

                    arrObj.total=total;
                    resultArr.push(arrObj); 
                    
                }
                

                //	console.log(resultArr);

                return resultArr;
            }


            var merge =  function (res) {

                var data = res.data;
                var mergeIndex = 0;//定位需要添加合并属性的行数
                var mark = 1; //这里涉及到简单的运算，mark是计算每次需要合并的格子数
                var columsName = ['merchantName','merchantNickname'];//需要合并的列名称
                var columsIndex = [0,1];//需要合并的列索引值

                for (var k = 0; k < columsName.length; k++) { //这里循环所有要合并的列
                    var trArr = $(".layui-table-body>.layui-table").find("tr");//所有行
                    for (var i = 1; i < res.data.length; i++) { //这里循环表格当前的数据
                        var tdCurArr = trArr.eq(i).find("td").eq(columsIndex[k]);//获取当前行的当前列
                        var tdPreArr = trArr.eq(mergeIndex).find("td").eq(columsIndex[k]);//获取相同列的第一列

                        if (data[i][columsName[k]] === data[i-1][columsName[k]]) { //后一行的值与前一行的值做比较，相同就需要合并
                            mark += 1;
                            tdPreArr.each(function () {//相同列的第一列增加rowspan属性
                                $(this).attr("rowspan", mark);
                            });
                            tdCurArr.each(function () {//当前行隐藏
                                $(this).css("display", "none");
                            });
                        }else {
                            mergeIndex = i;
                            mark = 1;//一旦前后两行的值不一样了，那么需要合并的格子数mark就需要重新计算
                        }
                    }
                    mergeIndex = 0;
                    mark = 1;
                }
            }

            $("#searchMem").text("5分钟结果:");
            updateTable();
            var timer;
            function refPageProc(){
                
                if(!$('#check_autoRef').is(":checked"))
                {
                    clearInterval(timer);
                    return;
                }
                
                if($("#text_Time").val()=="" || $("#text_Time").val()==0){
                    clearInterval(timer);
                    return;
                }
                timer = setInterval(function(){
                    layer.msg("刷新");
                    updateTable();
                }, $("#text_Time").val()*1000);

            }
            refPageProc();
            

            /*table.on('sort(table)', function(obj){
                if(!obj.sorts) return;

            });*/

        });
    });


</script>
</body>
</html>
