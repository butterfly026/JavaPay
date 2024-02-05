<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="adminPermission" uri="http://adminpermission.city.com" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>商户提现管理</title>
    <link rel="stylesheet" href="${context}/layui/css/layui.css">
    <link rel="stylesheet" href="${context}/layui/css/dropdown.css">
    <link rel="stylesheet" href="${context}/css/style.css?${s_vs}">
    <!--[if lt IE 9]>
      <script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
      <script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    <style>
        .qqfpay{
            background-color: #fff888 !important;
            color: #666666 !important;
        }
        #qrcodeTable{
            margin-left: 175px;
            margin-top: 18px;
        }
    </style>
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
                    <select name="merchantId" lay-search>
                        <option value="">选择商户</option>

                        <c:forEach items="${merchantList }" var="c">
                            <option value="${c.id}">${c.username}-${c.name}</option>
                        </c:forEach>

                    </select>
                </div>

                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                    <select name="clientId" lay-search>
                        <option value="">选择上游</option>

                        <c:forEach items="${clientList }" var="c">
                            <option value="${c.id}">${c.username}-${c.name}</option>
                        </c:forEach>

                    </select>
                </div>

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
                    <input type="text" name="startTime" id="startTime" placeholder="下单时间-开始" class="layui-input"
                           autocomplete="off"/>
                </div>
                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                    <input type="text" name="endTime" id="endTime" placeholder="下单时间-结束" class="layui-input"
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
                        <option value="2">驳回</option>
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

        <adminPermission:hasPermission permission="admin:paycash:api">
            <!--
            <button class="layui-btn layui-btn-sm layui-btn-normal" onclick="add();">
            发起代付
            </button>
            -->
        </adminPermission:hasPermission>

        <div class="layui-col-xs8 layui-col-sm8 layui-col-md8">

            <adminPermission:hasPermission permission="admin:paycash:export">
                <button class="layui-btn layui-btn-sm layui-btn-normal" onclick="exportExcel();">
                    导出Excel
                </button>

                <button class="layui-btn layui-btn-sm layui-btn-normal" onclick="showExportView();">
                    历史导出数据
                </button>
            </adminPermission:hasPermission>

            <adminPermission:hasPermission permission="admin:paycash:add">

                <button class="layui-btn layui-btn-sm layui-btn-normal" onclick="configAuto();">
                    配置自动代付
                </button>

                <button class="layui-btn layui-btn-sm layui-btn-normal" onclick="add();">
                    指定商户申请提现
                </button>
            </adminPermission:hasPermission>
        </div>


        <div class="layui-col-xs2 layui-col-sm2 layui-col-md2" style="text-align:center;">
            &nbsp;
            <adminPermission:hasPermission permission="admin:paycash:suc">
                <%--	<button class="layui-btn layui-btn-sm" onclick="batchSuccess();">
                    批量成功
                    </button>--%>
            </adminPermission:hasPermission>
            <adminPermission:hasPermission permission="admin:paycash:fail">
                <%--<button class="layui-btn layui-btn-sm" onclick="batchFail();">
                    批量失败
                </button>--%>
            </adminPermission:hasPermission>
        </div>


        <span class="order-tag" id="order_tag">
		</span>
    </div>
    <div style="clear: both;"></div>
    <table id="datatable" lay-filter="table"></table>

    <!-- 订单审核 -->
    <div id="audit_dlg" style="display:none;">
        <form class="layui-form" style="width:90%;margin-top:10px;">
            <div class="layui-row" style="padding-bottom:5px;width:100%;text-align:center;color:red;" id="info_div">
            </div>
            <div class="layui-row" style="padding-bottom:5px;">
                <div class="layui-col-xs12 layui-col-sm12 layui-col-md12">
                    <label class="layui-form-label">备注</label>
                    <div class="layui-input-block">
                        <textarea name="remark" id="remark" placeholder="审核备注" class="layui-textarea"></textarea>
                    </div>
                </div>
            </div>
        </form>
    </div>
<!--2022.6.17 kahn 提现驳回-->
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
        <span class="layui-badge">提现驳回</span>
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

        {{# if(d.payStatus==0){ }}
        <adminPermission:hasPermission permission="admin:paycash:shtg">
            <a class="layui-btn layui-btn-xs layui-btn-normal" lay-event="shtg">通过</a>
        </adminPermission:hasPermission>
        <adminPermission:hasPermission permission="admin:paycash:shjj">
            <a class="layui-btn layui-btn-xs layui-btn-danger" lay-event="shjj">拒绝</a>
        </adminPermission:hasPermission>
        {{# } }}

        {{# if(d.payStatus==3 ){ }}
        <adminPermission:hasPermission permission="admin:paycash:api">
            <a class="layui-btn layui-btn-xs layui-btn-normal" lay-event="api">发起代付</a>
        </adminPermission:hasPermission>
        <adminPermission:hasPermission permission="admin:paycash:shjj">
            <a class="layui-btn layui-btn-xs layui-btn-danger" lay-event="bh">驳回</a>
        </adminPermission:hasPermission>
        {{# } }}

        {{# if(d.payStatus==4){ }}
        <adminPermission:hasPermission permission="admin:paycash:suc">
            <a class="layui-btn layui-btn-xs layui-btn-green" lay-event="suc">成功</a>
        </adminPermission:hasPermission>
        <adminPermission:hasPermission permission="admin:paycash:fail">
            <a class="layui-btn layui-btn-xs layui-btn-danger" lay-event="fail">失败</a>
        </adminPermission:hasPermission>


        {{# } }}

        {{# if(d.payStatus==1 || d.payStatus==2 || d.payStatus==5){ }}
        <adminPermission:hasPermission permission="admin:paycash:xy">
            <a class="layui-btn layui-btn-xs layui-btn-normal" lay-event="xy">下游</a>
        </adminPermission:hasPermission>
        {{# } }}


        <button class="layui-btn layui-btn-xs" id="layuidropdown_{{d.id}}" lay-dropdown="{align:'right', menus:[
		<adminPermission:hasPermission permission="admin:paycash:list">
			{layIcon:'layui-icon layui-icon-edit',txt: '详情',event:'detail'},
		</adminPermission:hasPermission>

		<adminPermission:hasPermission permission="admin:paycash:list">
			{layIcon:'layui-icon layui-icon-edit',txt: '添加备注',event:'edremark'},
		</adminPermission:hasPermission>

		<adminPermission:hasPermission permission="admin:paycash:list">
			{layIcon:'layui-icon layui-icon-edit',txt: '二维码',event:'qrcode'},
		</adminPermission:hasPermission>

		{{# if(d.payStatus==4){ }}
		<adminPermission:hasPermission permission="admin:paycash:api">
			{layIcon:'layui-icon layui-icon-edit',txt: '更换代付上游',event:'changeClient'},
		</adminPermission:hasPermission>

		{{# } }}

		]}">
            <span>操作</span>
            <i class="layui-icon layui-icon-triangle-d"></i>
        </button>

    </script>

    <script type="text/html" id="tpl_index">
        {{ d.LAY_INDEX }}
    </script>

    <script type="text/html" id="tpl_btype">
        {{# if(d.btype && d.btype=="1"){ }}
        <span class="layui-badge layui-bg-blue">UPI账号</span>
        {{# }else{ }}
        <span class="layui-badge layui-bg-green">银行卡</span>
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
<script src="${context}/js/jquery.qrcode.min.js"></script>
<script src="${context}/layui/layui.js?${s_vs}"></script>
<script src="${context}/js/common.js?${s_vs}"></script>
<script type="text/javascript">


    var tableIns;
    var table;
    var lay_index;

    var lay_sort;
    var dropdown;

    $(function () {
        layui.config({
            base: '${context}/layui/lay/modules/' //配置 layui-dropdown 组件基础目录
        }).extend({
            dropdown: 'dropdown'
        });

        // var $tableheight=$(document).height()-$("#datatable").offset().top-29;
        layui.use(['table', 'layer', 'dropdown', 'laydate'], function () {
            table = layui.table;
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
                // ,height: $tableheight
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

                    {field: 'id', title: '序号', width: 60, templet: '#tpl_index'}
                    , {field: 'merchantNo', title: '商户号', width: 140}
                    , {field: 'sn', title: '订单号', width: 130}
                    , {field: 'createTime', title: '创建时间', width: 170}
                    , {field: 'payStatus', title: '状态', width: 100, templet: '#tpl_ps'}

                    , {field: 'merchantSn', title: '商户订单号', width: 130}

                    , {field: 'realMoney', title: '提现(${sysconfig.currency })', width: 100}

                    //,{field:'proxyNo', title: '代理帐号', width:130}
                    , {field: 'clientNo', title: '上游帐号', width: 130}

                    , {field: 'channelType', title: '提现类型', width: 80, templet: '#tpl_ctype'}
                    , {field: 'btype', title: '账号类型', width: 100, templet: '#tpl_btype'}
                    , {field: 'bankAccno', title: '提现账号', width: 200}
                    , {field: 'bankAccname', title: '持卡人', width: 130}
                    //	,{type:'checkbox'}
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

                    , {field: 'payTime', title: '支付时间', width: 170}

                    , {field: 'dealStatus', title: '通知状态', width: 100, templet: '#tpl_ns'}

                    , {field: 'remark', title: '备注', width: 130}
                    , {field: 'cashType', title: '提现来源', width: 130, templet: '#tpl_tx'}
                    //,{field:'amount', title: '金额(元)', width:100}
                    //,{field:'commission', title: '手续费(元)', width:100}

                    , {fixed: 'right', align: 'center', title: '操作', toolbar: '#tpl_opera', width: 220}
                ]]
                , done: function (res, curr, count) {
                    for(var i =0 ;i<res.data.length+1;i++){
                        if(i<res.data.length && res.data[i].merchantNo.includes('全球付')){
                            var ieq = $('.layui-table tbody tr').eq(i);
                            if(ieq!=null){
                                ieq.addClass('qqfpay');
                            }

                        }
                        //if(res.data[i].merchantSn==""){

                            
                            //res.data[i].merchantSn="平台处理";
                            //console.log(res.data[i].merchantSn);
                        //}
                        var merchantSn = document.querySelector("body > div.admin-main > div.layui-form.layui-border-box.layui-table-view > div.layui-table-box > div.layui-table-body.layui-table-main > table > tbody > tr:nth-child("+i+") > td:nth-child(6) > div");
                        //document.querySelector("body > div.admin-main > div.layui-form.layui-border-box.layui-table-view > div.layui-table-box > div.layui-table-body.layui-table-main > table > tbody > tr:nth-child(6) > td:nth-child(6) > div")
                        if(merchantSn!=null && merchantSn.innerHTML.length<=0){
                            merchantSn.innerHTML="平台处理";
                        }
                    
                        /*
                        var merchantSns = $('.laytable-cell-1-merchantSn');
                        for(let i in merchantSns) {
                            //console.log(merchantSns[i].html);
                            if(merchantSns[i].html.length<=0){
                                merchantSns[i].html="平台处理";
                                console.log("111");
                            }
                        }
                        */
                    }

                    dropdown.suite();
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
                } else if (layEvent === 'shtg') {
                    shtg(data.id);
                } else if (layEvent === 'shjj') {
                    shjj(data.id);
                } else if (layEvent === 'api') {
                    api(data.id);
                } else if (layEvent === 'suc') {
                    suc(data.id);
                } else if (layEvent === 'fail') {
                    fail(data.id);
                } else if (layEvent === 'xy') {
                    xy(data.id);
                } else if (layEvent === 'detail') {
                    detail(data.id, data.sn);
                } else if (layEvent === 'bh') {//驳回
                    bh(data.id);
                } else if (layEvent === 'changeClient') {//更换上游
                    changeClient(data.id);
                } else if (layEvent === 'edremark') {
                    edremark(data.id);
                } else if (layEvent === 'qrcode') {
                    qrcode(data);
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

    function showExportView() {
        lay_index = layer.open({
            type: 2,
            title: '导出列表',
            skin: 'layui-layer-rim', //加上边框
            area: ['90%', '90%'], //宽高
            content: '../payExcelexport/view?source=1'
        });
    }

    function configAuto() {
        lay_index = layer.open({
            type: 2,
            title: '自动代付',
            skin: 'layui-layer-rim', //加上边框
            area: ['90%', '90%'], //宽高
            content: 'autoConfig'
        });
    }

    function changeClient(id) {
        lay_index = layer.open({
            type: 2,
            title: '发起代付',
            skin: 'layui-layer-rim', //加上边框
            area: ['80%', '80%'], //宽高
            content: 'merchantChangeClientCash?id=' + id
        });
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

    function xy(id) {
        layer.confirm('确定向商户发送通知？', {
            icon: 3,//询问图标
            btn: ['确定', '取消'] //按钮
        }, function (index) {
            layer.close(index);//关闭弹层

            commReq('sendNotify', {id: id}, function () {
                searchData();
            });
        }, function (index) {
            layer.close(index);
        });
    }

    function suc(id) {
        $("#info_div").html("确定要将此提现申请的状态设置为成功吗，此操作将扣除相应用户的余额？(API发起的下发会自动通知商户)");
        $("#remark").val("");
        layer.open({
            type: 1,
            title: '提现成功',
            skin: 'layui-layer-rim', //加上边框
            area: ['550px', '300px'], //宽高
            btn: ['确定', '取消'],
            content: $("#audit_dlg")
            , yes: function (index) {
                var remark = $("#remark").val();
                lay_index = layer.load(1, {
                    shade: [0.1, '#fff'] //0.1透明度的白色背景
                });

                commReq('cashSuc', {id: id, remark: remark}, function () {
                    layer.close(index);
                    searchData();
                });

            }, cancel: function (index) {
                layer.close(index);
            }
        });
        /**
         layer.confirm('确定要将此提现申请的状态设置为成功吗，此操作将扣除相应用户的余额？(API发起的下发会自动通知商户)', {
			icon: 3,//询问图标
			btn: ['确定','取消'] //按钮
		}, function(index){
			layer.close(index);//关闭弹层

			commReq('cashSuc',{id:id},function(){
				searchData();
			});
		}, function(index){
			layer.close(index);
		});
         **/
    }

    function fail(id) {

        $("#info_div").html("确定要将此提现申请的状态设置为失败吗，此操作将解冻相应用户的冻结金额？");
        $("#remark").val("");
        layer.open({
            type: 1,
            title: '提现失败',
            skin: 'layui-layer-rim', //加上边框
            area: ['550px', '300px'], //宽高
            btn: ['确定', '取消'],
            content: $("#audit_dlg")
            , yes: function (index) {
                var remark = $("#remark").val();
                lay_index = layer.load(1, {
                    shade: [0.1, '#fff'] //0.1透明度的白色背景
                });

                commReq('cashFail', {id: id, remark: remark}, function () {
                    layer.close(index);
                    searchData();
                });

            }, cancel: function (index) {
                layer.close(index);
            }
        });

        /**
         layer.confirm('确定要将此提现申请的状态设置为失败吗，此操作将解冻相应用户的冻结金额？', {
			icon: 3,//询问图标
			btn: ['确定','取消'] //按钮
		}, function(index){
			layer.close(index);//关闭弹层

			commReq('cashFail',{id:id},function(){
				searchData();
			});
		}, function(index){
			layer.close(index);
		});
         **/
    }

    function shtg(id) {
        $("#info_div").html("确定要通过此提现申请？");
        $("#remark").val("");
        layer.open({
            type: 1,
            title: '审核通过',
            skin: 'layui-layer-rim', //加上边框
            area: ['550px', '300px'], //宽高
            btn: ['确定', '取消'],
            content: $("#audit_dlg")
            , yes: function (index) {
                var remark = $("#remark").val();
                lay_index = layer.load(1, {
                    shade: [0.1, '#fff'] //0.1透明度的白色背景
                });

                commReq('shtg', {id: id, remark: remark}, function () {
                    layer.close(index);
                    searchData();
                });

            }, cancel: function (index) {
                layer.close(index);
            }
        });
        /**
         layer.confirm('确定要通过此提现申请？', {
			icon: 3,//询问图标
			btn: ['确定','取消'] //按钮
		}, function(index){
			layer.close(index);//关闭弹层

			commReq('shtg',{id:id},function(){
				searchData();
			});
		}, function(index){
			layer.close(index);
		});
         **/
    }

    //更新状态
    function shjj(id) {
        $("#info_div").html("确定要拒绝此提现申请？");
        $("#remark").val("");
        layer.open({
            type: 1,
            title: '审核拒绝',
            skin: 'layui-layer-rim', //加上边框
            area: ['550px', '300px'], //宽高
            btn: ['确定', '取消'],
            content: $("#audit_dlg")
            , yes: function (index) {
                var remark = $("#remark").val();
                lay_index = layer.load(1, {
                    shade: [0.1, '#fff'] //0.1透明度的白色背景
                });

                commReq('shjj', {id: id, remark: remark}, function () {
                    layer.close(index);
                    searchData();
                });

            }, cancel: function (index) {
                layer.close(index);
            }
        });
        /**
         layer.confirm('确定要拒绝此提现申请？', {
			icon: 3,//询问图标
			btn: ['确定','取消'] //按钮
		}, function(index){
			layer.close(index);//关闭弹层

			commReq('shjj',{id:id},function(){
				searchData();
			});
		}, function(index){
			layer.close(index);
		});
         **/
    }

    //驳回
    function bh(id) {
        $("#info_div").html("确定要驳回此提现申请？");
        $("#remark").val("");
        layer.open({
            type: 1,
            title: '提现驳回',
            skin: 'layui-layer-rim', //加上边框
            area: ['550px', '300px'], //宽高
            btn: ['确定', '取消'],
            content: $("#audit_dlg")
            , yes: function (index) {
                var remark = $("#remark").val();
                lay_index = layer.load(1, {
                    shade: [0.1, '#fff'] //0.1透明度的白色背景
                });

                commReq('bh', {id: id, remark: remark}, function () {
                    layer.close(index);
                    searchData();
                });

            }, cancel: function (index) {
                layer.close(index);
            }
        });
    }

    //编辑备注
    function edremark(id) {
        $("#info_div").html("请填写备注");
        $("#remark").val("");
        layer.open({
            type: 1,
            title: '添加备注',
            skin: 'layui-layer-rim', //加上边框
            area: ['550px', '300px'], //宽高
            btn: ['确定', '取消'],
            content: $("#audit_dlg")
            , yes: function (index) {
                var remark = $("#remark").val();
                lay_index = layer.load(1, {
                    shade: [0.1, '#fff'] //0.1透明度的白色背景
                });

                commReq('edremark', {id: id, remark: remark}, function () {
                    layer.close(index);
                    searchData();
                });

            }, cancel: function (index) {
                layer.close(index);
            }
        });
    }

    function utf16to8(str) {
        var out, i, len, c;
        out = "";
        len = str.length;
        for (i = 0; i < len; i++) {
            c = str.charCodeAt(i);
            if ((c >= 0x0001) && (c <= 0x007F)) {
                out += str.charAt(i);
            } else if (c > 0x07FF) {
                out += String.fromCharCode(0xE0 | ((c >> 12) & 0x0F));
                out += String.fromCharCode(0x80 | ((c >> 6) & 0x3F));
                out += String.fromCharCode(0x80 | ((c >> 0) & 0x3F));
            } else {
                out += String.fromCharCode(0xC0 | ((c >> 6) & 0x1F));
                out += String.fromCharCode(0x80 | ((c >> 0) & 0x3F));
            }
        }
        return out;
    }

    //生成二维码
    function qrcode(data) {
        var textContent = '姓名：'+data.bankAccname+'\n银行：' +data.bankName+'\n卡号：' +data.bankAccno+'\n金额：' +data.amount
        textContent = utf16to8(textContent)
        layer.open({
            type: 1,
            title: '二维码信息',
            skin: 'layui-layer-rim', //加上边框
            area: ['550px', '320px'], //宽高
            btn: ['确定'],
            content:'<div id="qrcodeTable"></div>'
            , yes: function (index) {
                layer.close(index);
            }
        });
        jQuery('#qrcodeTable').qrcode({
            render: "table",
            text: textContent,
            width: "200",
            height: "200",
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
            async: false,
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

    function api(id) {
        lay_index = layer.open({
            type: 2,
            title: '发起代付',
            skin: 'layui-layer-rim', //加上边框
            area: ['80%', '80%'], //宽高
            content: 'merchantClientCash?id=' + id
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
         lay_index=layer.open({
		  type: 2,
		  title:'新增商户提现',
		  skin: 'layui-layer-rim', //加上边框
		  area: ['70%', '70%'], //宽高
		  content:'addSelf'
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
            {field: 'merchantNo', title: '商户号', width: 15, rowspan: 1, colspan: 1},
            {field: 'sn', title: '系统订单号', width: 30, rowspan: 1, colspan: 1},
            {field: 'merchantSn', title: '商户订单号', width: 30, rowspan: 1, colspan: 1},

            {field: 'clientNo', title: '代付上游', width: 30, rowspan: 1, colspan: 1},

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


            {field: 'payStatus', title: '提现状态', width: 15, rowspan: 1, colspan: 1}

        ]
    ];

    /*function  batchApiCash(){

        // 判断其中是否有状态不合法

        var checkData = table.checkStatus('id');
        if (!checkData.data||checkData.data.length<1) return alert("请选择需要api下发记录");

        var errorStr  = "";

        for (let i = 0; i < checkData.data.length; i++) {
            if (checkData.data[i].payStatus!=3) errorStr+=(" 第"+(i+1)+"条状态不正确");
        }

        if (errorStr!="") return layer.msg(errorStr);

        // 弹出代付页面

        lay_index=layer.open({
            type: 2,
            title:'发起批量api代付',
            skin: 'layui-layer-rim', //加上边框
            area: ['80%', '80%'], //宽高
            content:'batchShtgView?ids='+checkData.data.map(ele=>ele.id).join(",")
        });


    }*/


    function batchSuccess() {

        // 判断其中是否有状态不合法

        var checkData = table.checkStatus('id');
        if (!checkData.data || checkData.data.length < 1) return alert("请选择需要通过下发记录");

        var errorStr = "";
        for (let i = 0; i < checkData.data.length; i++) {
            if (checkData.data[i].payStatus != 4) errorStr += (" 第" + (i + 1) + "条状态不正确 需要处理中 ");
        }

        if (errorStr != "") return layer.msg(errorStr);


        $("#info_div").html("确定要将此提现申请的状态设置为成功吗，此操作将扣除相应用户的余额？(API发起的下发会自动通知商户)");
        $("#remark").val("");
        layer.open({
            type: 1,
            title: '提现成功',
            skin: 'layui-layer-rim', //加上边框
            area: ['550px', '300px'], //宽高
            btn: ['确定', '取消'],
            content: $("#audit_dlg")
            , yes: function (index) {
                var remark = $("#remark").val();
                lay_index = layer.load(1, {
                    shade: [0.1, '#fff'] //0.1透明度的白色背景
                });

                for (let dataObj of checkData.data) {
                    commReq('cashSuc', {id: dataObj.id, remark: remark}, function () {
                        layer.msg(dataObj.sn + " 处理成功");
                    });
                }
                layer.close(index);
                searchData();


            }, cancel: function (index) {
                layer.close(index);
            }
        });
    }


    function batchFail() {

        // 判断其中是否有状态不合法

        var checkData = table.checkStatus('id');
        if (!checkData.data || checkData.data.length < 1) return alert("请选择需要通过下发记录");

        var errorStr = "";
        for (let i = 0; i < checkData.data.length; i++) {
            if (checkData.data[i].payStatus != 4) errorStr += (" 第" + (i + 1) + "条状态不正确 需要处理中 ");
        }

        if (errorStr != "") return layer.msg(errorStr);


        $("#info_div").html("确定要将此提现申请的状态设置为失败吗，此操作将解冻相应用户的冻结金额？");
        $("#remark").val("");
        layer.open({
            type: 1,
            title: '提现失败',
            skin: 'layui-layer-rim', //加上边框
            area: ['550px', '300px'], //宽高
            btn: ['确定', '取消'],
            content: $("#audit_dlg")
            , yes: function (index) {
                var remark = $("#remark").val();
                lay_index = layer.load(1, {
                    shade: [0.1, '#fff'] //0.1透明度的白色背景
                });

                for (let dataObj of checkData.data) {
                    commReq('cashFail', {id: dataObj.id, remark: remark}, function () {
                        layer.msg(dataObj.sn + " 标记失败完成");
                    });
                }

                layer.close(index);
                searchData();


            }, cancel: function (index) {
                layer.close(index);
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
</script>
</body>
</html>
