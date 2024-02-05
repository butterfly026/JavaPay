<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="adminPermission" uri="http://adminpermission.city.com" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>上游通道调试</title>
    <link rel="stylesheet" href="${context}/layui/css/layui.css">
    <link rel="stylesheet" href="${context}/layui/css/admin.css">
    <link rel="stylesheet" href="${context}/css/style.css">
    <style type="text/css">
        .layuiadmin-card-list p.layuiadmin-big-font {
            font-size: 24px;
        }

        .span-red {
            color: red;
        }

        td {
            word-break: break-all;
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
            <h3><b>&nbsp;上游通道调试</b></h3>
        </div>
        <div class="layui-col-xs1 layui-col-sm1 layui-col-md1 right">
            <button class="layui-btn layui-btn-sm layui-btn-normal" title="刷新页面" onclick="window.location.reload();">
                <i class="layui-icon">&#x1002;</i>
            </button>
        </div>
    </div>
</div>
<div class="layui-fluid">
    <div class="layui-row layui-col-space15">

        <div class="layui-col-sm12 layui-col-md6">
            <fieldset class="layui-elem-field admin-search-form">
                <legend class="admin-search-title">收款商数据</legend>
                <form id="editForm" class="layui-form">

                    <div class="layui-form-item">
                        <label class="layui-form-label"><span class="span-red">*</span>收款商</label>
                        <div class="layui-input-block">
                            <select name="channelId" id="channelId" lay-verify="required" lay-search
                                    lay-filter="up_client">
                                <c:forEach items="${channelList }" var="c">
                                    <option value="${c.id}">${c.username}-${c.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <label class="layui-form-label"><span class="span-red">*</span>订单号</label>
                        <div class="layui-input-inline">
                            <input type="text" name="sn" id="sn" lay-verify="required" lay-verType="tips"
                                   placeholder="订单号" autocomplete="off" class="layui-input"/>
                        </div>
                        <div class="layui-form-mid layui-word-aux">
                            <button type="button" class="layui-btn layui-btn-xs" onclick="createSn()">
                                随机生成单号
                            </button>
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <label class="layui-form-label"><span class="span-red">*</span>测试金额</label>
                        <div class="layui-input-block">
                            <input type="text" name="money" id="money" lay-verify="required|number" lay-verType="tips"
                                   placeholder="测试金额" autocomplete="off" class="layui-input" value="100"/>
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <div class="layui-input-block">
                            <button type="button" class="layui-btn" onclick="testApi()">提交测试</button>
                        </div>
                    </div>
                </form>

            </fieldset>
            <fieldset class="layui-elem-field admin-search-form">
                <legend class="admin-search-title">通道详情</legend>

                <table class="layui-table" lay-size="sm">
                    <colgroup>
                        <col width="200">
                        <col>
                    </colgroup>
                    <tbody id="channelInfo">


                    </tbody>
                </table>

            </fieldset>
        </div>
        <div class="layui-col-sm12 layui-col-md6">
            <fieldset class="layui-elem-field admin-search-form">
                <legend class="admin-search-title">测试结果</legend>

                <div style="color:red;width:100%;font-size:12pt;" id="errorInfo"></div>
                <table class="layui-table" lay-size="sm">
                    <colgroup>
                        <col width="200">
                        <col>
                    </colgroup>
                    <tbody id="apiInfo">


                    </tbody>
                </table>

            </fieldset>
        </div>
    </div>
</div>


<script src="${context}/js/jquery.min.js"></script>
<script src="${context}/layui/layui.js?2"></script>
<script src="${context}/js/common.js?12"></script>
<script>

    var lay_index = 0;
    $(function () {
        layui.use(['form', 'layer'], function () {
            var form = layui.form;

            form.on('select(up_client)', function (data) {
                detail();
            });
            createSn();
            detail();

            initSelectAll();
        });
    });

    function createSn() {
        $("#sn").val("KK" + guid2());
    }

    function guid2() {
        function S4() {
            return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
        }

        return (S4() + S4() + S4() + S4() + S4());
    }

    function testApi() {
        var channelId = $("#channelId").val();

        var money = $("#money").val();
        var sn = $("#sn").val();

        lay_index = layer.load(1, {
            shade: [0.1, '#fff'] //0.1透明度的白色背景
        });
        $.ajax({
            type: 'post',
            url: 'testApiPaytm',
            data: {id: channelId, amount: money, merchantSn: sn},
            dataType: "json",
            cache: false,
            success: function (data) {
                layer.close(lay_index);

                if (data.code == 200) {
                    $("#errorInfo").html("");
                    var $r = data.result;
                    var $str = '';

                    var $typestr = "API转发";
                    if ($r.gotype == 1) {
                        $typestr = "表单转发<span style='color:red;'>(请点击链接测试接口)</span>";
                    }
                    $str += '<tr><td width="20%"><b>上游接口地址</b></td><td width="80%">' + $r.orderUrl + '</td></tr>';

                    $str += '<tr><td width="20%"><b>访问模式</b></td><td width="80%">' + $typestr + '</td></tr>';

                    $str += '<tr><td width="20%"><b>下单链接</b></td><td width="80%"><a href="' + $r.payUrl + '" target="_blank" style="color:#1E9FFF;">' + $r.payUrl + '</a></td></tr>';

                    $str += '<tr><td width="20%"><b>接口返回数据</b></td><td width="80%">' + $r.result + '</td></tr>';

                    $str += '<tr><td width="20%"><b>上游响应数据</b></td><td width="80%">' + $r.responseData + '</td></tr>';

                    $str += '<tr><td width="20%"><b>处理结果</b></td><td width="80%">' + $r.dealResult + '</td></tr>';

                    $str += '<tr><td width="20%"><b>上游请求数据</b></td><td width="80%">' + $r.requstData + '</td></tr>';

                    $str += '<tr><td width="20%"><b>接口返回数据</b></td><td width="80%" style="color:green;">' + $r.result + '</td></tr>';

                    $str += '<tr><td width="20%"><b>参与签名数据</b></td><td width="80%">' + $r.signData + '</td></tr>';
                    $str += '<tr><td width="20%"><b>签名字符串</b></td><td width="80%">' + $r.signStr + '</td></tr>';
                    $str += '<tr><td width="20%"><b>签名</b></td><td width="80%">' + $r.sign + '</td></tr>';


                    $str += '<tr><td width="20%"><b>响应异常信息</b></td><td width="80%">' + $r.responseError + '</td></tr>';

                    $str += '<tr><td width="20%"><b>附带信息</b></td><td width="80%">' + $r.extraStr + '</td></tr>';

                    $("#apiInfo").html($str);
                } else {
                    layer.msg(data.msg);
                    $("#apiInfo").html("");
                    $("#errorInfo").html(data.msg);
                }
            },
            error: function (xhr, desc, err) {
                layer.close(lay_index);
                $("#errorInfo").html("调试失败:" + desc + "");
                $("#apiInfo").html("");
            }
        });
    }


    function detail() {
        var id = $("#channelId").val();

        lay_index = layer.load(1, {
            shade: [0.1, '#fff'] //0.1透明度的白色背景
        });
        $.ajax({
            type: 'post',
            url: 'detail',
            data: {id: id},
            dataType: "json",
            cache: false,
            success: function (data) {
                layer.close(lay_index);

                if (data.code == 200) {
                    //console.log(data.result);
                    var $r = data.result;
                    var $str = '';
                    $str += '<tr><td width="20%"><b>上游名称</b></td><td width="30%">' + $r.clientNo + '-' + $r.clientName + '</td>';
                    $str += '<td width="20%"><b>通道名(mid)</b></td><td width="30%">' + $r.name + '(' + $r.paytmid + ')</td></tr>';
                    $str += '<tr><td width="20%"><b>通道类型</b></td><td width="30%">' + $r.typeName + '</td>';
                    <adminPermission:hasPermission permission="admin:payclientchannel:ratio">
                    $str += '<td width="20%"><b>通道费率</b></td><td width="30%">' + $r.ratio + '%</td>';
                    </adminPermission:hasPermission>
                    $str += '</tr>';

                    $str += '<tr><td width="20%"><b>开启时间</b></td><td width="30%">' + $r.startTime + '</td>';
                    $str += '<td width="20%"><b>关闭时间</b></td><td width="30%">' + $r.endTime + '</td></tr>';

                    var $mname = '最大最小金额';
                    var $moneystr = $r.minMoney + "~" + $r.maxMoney;
                    if ($r.mtype == 1) {
                        $mname = '固定金额';
                        $moneystr = $r.money;
                    }

                    $str += '<tr><td width="20%"><b>金额模式</b></td><td width="30%">' + $mname + '</td>';
                    $str += '<td width="20%"><b>金额范围</b></td><td width="30%">' + $moneystr + '</td></tr>';


                    var $statusstr = "启用";
                    if ($r.status == 0) {
                        $statusstr = "<span style='color:red;'>禁用</span>";
                    }
                    $str += '<tr><td width="20%"><b>通道状态</b></td><td width="30%">' + $statusstr + '</td>';

                    var $typestr = "支付链接";
                    //if($r.gotype==1){
                    //	$typestr="表单转发";
                    //}
                    $str += '<td width="20%"><b>跳转类型</b></td><td width="30%">' + $typestr + '</td></tr>';

                    //$str+='<tr><td width="20%"><b>附加参数</b></td><td width="80%" colspan="3">'+($r.params?$r.params:'')+'</td></tr>';

                    //$str+='<tr><td width="20%"><b>关联程序</b></td><td width="80%" colspan="3">'+$r.keyname+'</td></tr>';


                    $("#channelInfo").html($str);
                } else {
                    $("#channelInfo").html("<tr><td class='span-red'>" + data.msg + "</td></tr>");
                }
            },
            error: function (xhr, desc, err) {
                layer.close(lay_index);
                $("#channelInfo").html("<tr><td class='span-red'>获取通道数据失败:" + desc + "</td></tr>");
            }
        });
    }


</script>
</body>
</html>
