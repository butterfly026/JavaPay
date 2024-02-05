<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>批量查单</title>
    <link rel="stylesheet" href="${context}/layui/css/layui.css">
    <style>
        .admin-main {
            margin: 15px;
        }

        .span-red {
            color: red;
        }
    </style>
    <!--[if lt IE 9]>
      <script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
      <script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<div class="admin-main">
    <form id="editForm" class="layui-form">

        <div class="layui-form-item">
            <label class="layui-form-label"><span class="span-red">*</span>开始时间</label>
            <div class="layui-input-block">
                <input type="text" name="startTime" id="startTime" lay-verify="required" lay-verType="tips"
                       placeholder="开始时间" autocomplete="off" class="layui-input"/>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label"><span class="span-red">*</span>结束时间</label>
            <div class="layui-input-block">
                <input type="text" name="endTime" id="endTime" lay-verify="required|dateValid" lay-verType="tips"
                       placeholder="结束时间" autocomplete="off" class="layui-input"/>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label"><span class="span-red">*</span>上游渠道</label>
            <div class="layui-input-block">
                <select name="clientId" id="clientId" lay-verify="required" lay-search>
                    <c:forEach items="${modelList }" var="c">
                        <option value="${c.clientId}">${c.clientName} [ ${c.clientUserName} ]</option>
                    </c:forEach>
                </select>
            </div>
        </div>

        <div class="layui-form-item">
            <div class="layui-input-block">

                <button class="layui-btn layui-btn-danger" onclick="stopQueryOrder()" id="stopbtn" type="button"
                        style="display:none;">停止批量查单
                </button>
                <button class="layui-btn" onclick="startQueryOrder()" id="startbtn" type="button">批量查单</button>
                <button type="reset" class="layui-btn layui-btn-primary">重置</button>
            </div>
        </div>

        <div style="text-align: left;font-weight: bold;font-size:12px;padding-left:25px;">
            【&nbsp;查单日志&nbsp;】
        </div>
        <div style="padding:5px 25px 5px 25px;font-size:12px;">
            共有&nbsp;<span style="color:green;" id="total">0</span>&nbsp;条订单需要查询,已完成&nbsp;<span style="color:green;"
                                                                                               id="complete">0</span>&nbsp;条,剩余&nbsp;<span
                style="color:green;" id="surplus">0</span>&nbsp;条,&nbsp;<span style="color:green;" id="query">0</span>&nbsp;条正在处理,支付成功&nbsp;<span
                style="color:green;" id="success">0</span>&nbsp;条,支付失败&nbsp;<span style="color:green;"
                                                                                  id="fail">0</span>&nbsp;条
        </div>
        <div style="color:red;font-size:12px;padding:5px 25px 25px 25px;height:160px;overflow-y:auto; " id="queryLog">

        </div>
    </form>


</div>
<script src="${context}/js/jquery.min.js"></script>
<script src="${context}/layui/layui.js"></script>
<script src="${context}/js/common.js"></script>
<script>
    var lay_index;

    $(function () {

        layui.use(['form', 'layer', 'laydate'], function () {
            var form = layui.form;

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

            initSelectAll();
        });

        <c:if test="${run}">queryOrderInfo();
        </c:if>
    });

    function queryOrderInfo() {
        $.ajax({
            type: 'post',
            url: "queryOrderInfo",
            data: {},
            dataType: "json",
            cache: false,
            success: function (data) {
                layer.close(lay_index);
                if (data.code == 200) {
                    var $r = data.result;
                    $("#total").html($r.total);
                    $("#surplus").html($r.surplus);
                    $("#complete").html($r.complete);
                    $("#query").html($r.query);

                    $("#success").html($r.success);
                    $("#fail").html($r.fail);
                    if ($r.run) {
                        setTimeout("queryOrderInfo()", 2000);
                        $("#startbtn").hide();
                        $("#stopbtn").show();
                    } else {
                        $("#startbtn").show();
                        $("#stopbtn").hide();
                    }
                    var d1 = $r.errorLogs;
                    var $str = '';
                    for (var i = 0; i < d1.length; i++) {
                        $str += '<p>' + d1[i] + '</p>';
                    }
                    $("#queryLog").html($str);
                }
            },
            error: function (xhr, desc, err) {
                layer.msg("数据请求失败:" + desc);
                layer.close(lay_index);
            }
        });
    }

    function startQueryOrder() {
        lay_index = layer.load(1, {
            shade: [0.1, '#fff'] //0.1透明度的白色背景
        });
        $.ajax({
            type: 'post',
            url: "startQueryOrderInfo",
            data: getFormJsonData("#editForm"),
            dataType: "json",
            cache: false,
            success: function (data) {
                layer.close(lay_index);
                layer.msg(data.msg);
                if (data.code == 200) {
                    $("#startbtn").hide();
                    $("#stopbtn").show();

                    queryOrderInfo();
                }
            },
            error: function (xhr, desc, err) {
                layer.close(lay_index);
                layer.msg("数据请求失败:" + desc);
            }
        });
    }

    function stopQueryOrder() {
        lay_index = layer.load(1, {
            shade: [0.1, '#fff'] //0.1透明度的白色背景
        });
        $.ajax({
            type: 'post',
            url: "stopQueryOrderInfo",
            data: {},
            dataType: "json",
            cache: false,
            success: function (data) {
                layer.close(lay_index);
                layer.msg(data.msg);
                if (data.code == 200) {
                    $("#startbtn").show();
                    $("#stopbtn").hide();
                    queryOrderInfo();
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
