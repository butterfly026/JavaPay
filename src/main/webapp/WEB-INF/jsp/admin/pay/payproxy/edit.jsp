<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>编辑代理管理</title>
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
        <input type="hidden" name="id" value="${result.id}">
        <!--
		<div class="layui-form-item">
			<label class="layui-form-label"><span class="span-red">*</span>名称</label>
			<div class="layui-input-block">
				<input type="text" name="name" id="name" lay-verify="required" lay-verType="tips" placeholder="名称" autocomplete="off" class="layui-input"  value="${result.name}" />
			</div>
		</div>
	-->
        <div class="layui-form-item">
            <label class="layui-form-label"><span class="span-red">*</span>代理账号</label>
            <div class="layui-input-block">
                <input type="text" name="username" id="username" lay-verify="required" lay-verType="tips"
                       placeholder="代理账号" autocomplete="off" class="layui-input" value="${result.username}"/>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label"><span class="span-red">*</span>代理名</label>
            <div class="layui-input-block">
                <input type="text" name="name" id="name" lay-verify="required" lay-verType="tips" placeholder="代理名"
                       autocomplete="off" class="layui-input" value="${result.name}"/>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label"><span class="span-red">*</span>最低提现金额</label>
            <div class="layui-input-block">
                <input type="text" name="minCommission" id="minCommission" lay-verify="required|number"
                       lay-verType="tips" placeholder="最低提现金额" autocomplete="off" class="layui-input"
                       value="${result==null?5000:result.minCommission}"/>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label"><span class="span-red">*</span>提现手续费(银行卡)</label>
            <div class="layui-input-inline">
                <select name="cashMode" id="cashMode" lay-verify="required" lay-filter="cmode">
                    <option value="1" <c:if test='${result.cashMode==1}'>selected</c:if>>百分比(%)</option>
                    <option value="0" <c:if test='${result.cashMode==0}'>selected</c:if>>固定金额</option>
                </select>
            </div>
            <div class="layui-form-mid layui-word-aux" style="padding: 0px 0!important;">
                <input type="text" name="cashCommission" id="cashCommission" lay-verify="required|number"
                       lay-verType="tips" placeholder="提现手续费(银行卡)" autocomplete="off" class="layui-input"
                       value="${result==null?5:result.cashCommission}"/>
            </div>
            <div class="layui-form-mid layui-word-aux">
                <span style="color:red;" id="dw">%</span>
            </div>

        </div>


        <div class="layui-form-item">
            <label class="layui-form-label"><span class="span-red">*</span>提现手续费(UPI)</label>
            <div class="layui-input-inline">
                <select name="ptmcashMode" id="ptmcashMode" lay-verify="required" lay-filter="cmode_ptm">
                    <option value="1" <c:if test='${result.ptmcashMode==1}'>selected</c:if>>百分比(%)</option>
                    <option value="0" <c:if test='${result.ptmcashMode==0}'>selected</c:if>>固定金额</option>
                </select>
            </div>
            <div class="layui-form-mid layui-word-aux" style="padding: 0px 0!important;">
                <input type="text" name="ptmcashCommission" id="ptmcashCommission" lay-verify="required|number"
                       lay-verType="tips" placeholder="提现手续费(UPI)" autocomplete="off" class="layui-input"
                       value="${result==null?5:result.ptmcashCommission}"/>
            </div>
            <div class="layui-form-mid layui-word-aux">
                <span style="color:red;" id="ptmdw">%</span>
            </div>

        </div>


        <div class="layui-form-item">
            <label class="layui-form-label"><span class="span-red">*</span>提现手续费(USDT)</label>
            <div class="layui-input-inline">
                <select name="usdtcashMode" id="usdtcashMode" lay-verify="required" lay-filter="cmode_usdt">
                    <option value="1" <c:if test='${result.usdtcashMode==1}'>selected</c:if>>百分比(%)</option>
                    <option value="0" <c:if test='${result.usdtcashMode==0}'>selected</c:if>>固定金额</option>
                </select>
            </div>
            <div class="layui-form-mid layui-word-aux" style="padding: 0px 0!important;">
                <input type="text" name="usdtcashCommission" id="usdtcashCommission" lay-verify="required|number"
                       lay-verType="tips" placeholder="提现手续费(USDT)" autocomplete="off" class="layui-input"
                       value="${result==null?5:result.usdtcashCommission}"/>
            </div>
            <div class="layui-form-mid layui-word-aux">
                <span style="color:red;" id="usdtdw">%</span>
            </div>

        </div>


        <div class="layui-form-item">
            <div class="layui-input-block">
                <button class="layui-btn" lay-submit lay-filter="edit">提交</button>
                <button type="reset" class="layui-btn layui-btn-primary">重置</button>
            </div>
        </div>
    </form>
</div>
<script src="${context}/js/jquery.min.js"></script>
<script src="${context}/layui/layui.js?${s_vs}"></script>
<script src="${context}/js/common.js?${s_vs}"></script>
<script>
    var lay_index;
    var path = "addSave";
    var addFlag = true;//是否为新增
    <c:if test="${result!=null}">
    path = "editSave";
    addFlag = false;
    </c:if>
    <c:if test="${error!=null}">
    alert('${error}');
    window.parent.refreshPage();
    var index = parent.layer.getFrameIndex(window.name);
    parent.layer.close(index);
    </c:if>


    $(function () {
        if ($("#cashMode").val() == '0') {
            $("#dw").html("${sysconfig.currency }");
            //$("#mdw").html("${sysconfig.currency }");
        }
        if ($("#ptmcashMode").val() == '0') {
            $("#ptmdw").html("${sysconfig.currency }");
            //$("#mptmdw").html("${sysconfig.currency }");
        }
        if ($("#usdtcashMode").val() == '0') {
            $("#usdtdw").html("${sysconfig.currency }");
            //$("#musdtdw").html("${sysconfig.currency }");
        }

        layui.use(['form', 'layer'], function () {
            var form = layui.form;

            form.on('select(cmode)', function (data) {

                if (data.value == '0') {
                    $("#dw").html("${sysconfig.currency }");
                    //$("#mdw").html("${sysconfig.currency }");
                } else {
                    $("#dw").html("%");
                    //$("#mdw").html("%");
                }
            });
            form.on('select(cmode_ptm)', function (data) {

                if (data.value == '0') {
                    $("#ptmdw").html("${sysconfig.currency }");
                    //$("#mptmdw").html("${sysconfig.currency }");
                } else {
                    $("#ptmdw").html("%");
                    //$("#mptmdw").html("%");
                }
            });

            form.on('select(cmode_usdt)', function (data) {

                if (data.value == '0') {
                    $("#usdtdw").html("${sysconfig.currency }");
                    //$("#musdtdw").html("${sysconfig.currency }");
                } else {
                    $("#usdtdw").html("%");
                    //$("#musdtdw").html("%");
                }
            });
            //自定义表单验证
            /**
             form.verify({
			imgnull:function(value,item){//验证图标是否为空
				var name = $("#image").val();
				if(!name){
					return "必须选择一个图标";
				}
			}
		});
             **/
            //按钮事件
            form.on('submit(edit)', function (data) {
                lay_index = layer.load(1, {
                    shade: [0.1, '#fff'] //0.1透明度的白色背景
                });
                $.ajax({
                    type: 'post',
                    url: path,
                    data: getFormJsonData("#editForm"),
                    dataType: "json",
                    cache: false,
                    success: function (data) {
                        layer.close(lay_index);
                        if (data.code == 200) {
                            window.parent.refreshPage(JSON.stringify(data.result));
                        } else {
                            layer.msg(data.msg);
                        }
                    },
                    error: function (xhr, desc, err) {
                        layer.msg("数据请求失败:" + desc);
                        layer.close(lay_index);
                    }
                });
                return false;
            });
        });


    });
</script>
</body>
</html>
