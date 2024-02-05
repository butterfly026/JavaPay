<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>编辑银行编码</title>
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
        <div style="color:red;width:90%;padding-left:20px;margin-bottom:10px;">为了您更好的提现体验，建议完善您银行卡的开户省份、开户城市</div>
         -->

        <div class="layui-form-item">
            <label class="layui-form-label"><span class="span-red">*</span>账号类型</label>
            <div class="layui-input-block">
                <select name="btype" id="btype" lay-verify="required" lay-search lay-filter="up_type">
                    <option value="0"
                            <c:if test="${result.btype==0 }">selected</c:if> >银行卡账号
                    </option>
                    <option value="1"
                            <c:if test="${result.btype==1 }">selected</c:if> >UPI账号
                    </option>
                    <option value="2"
                            <c:if test="${result.btype==2 }">selected</c:if> >USDT
                    </option>
                </select>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label"><span class="span-red">*</span>卡号/UPI账号</label>
            <div class="layui-input-block">
                <input type="text" name="cardNo" id="cardNo" lay-verify="required" lay-verType="tips"
                       placeholder="卡号/UPI账号" autocomplete="off" class="layui-input" value="${result.cardNo}"/>
            </div>
        </div>

        <div class="layui-form-item acc">
            <label class="layui-form-label"><span class="span-red">*</span>持卡人姓名</label>
            <div class="layui-input-block">
                <input type="text" name="cardName" id="cardName" lay-verify="" lay-verType="tips" placeholder="持卡人姓名"
                       autocomplete="off" class="layui-input" value="${result.cardName}"/>
            </div>
        </div>

        <div class="layui-form-item acc">
            <label class="layui-form-label"><span class="span-red">*</span>银行名</label>
            <div class="layui-input-block">
                <input type="text" name="bankName" id="bankName" lay-verify="" lay-verType="tips" placeholder="银行名"
                       autocomplete="off" class="layui-input" value="${result.bankName}"/>
            </div>
        </div>

        <div class="layui-form-item acc">
            <label class="layui-form-label"><span class="span-red">*</span>IFSC</label>
            <div class="layui-input-block">
                <input type="text" name="bankIfsc" id="bankIfsc" lay-verify="" lay-verType="tips" placeholder="IFSC"
                       autocomplete="off" class="layui-input" value="${result.bankIfsc}"/>
            </div>
        </div>

        <div class="layui-form-item acc">
            <label class="layui-form-label"><span class="span-red">*</span>国家</label>
            <div class="layui-input-block">
                <input type="text" name="bankNation" id="bankNation" lay-verify="" lay-verType="tips" placeholder="国家"
                       autocomplete="off" class="layui-input" value="${result.bankNation}"/>
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
<script src="${context}/layui/layui.js"></script>
<script src="${context}/js/common.js"></script>
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

        if ($("#btype").val() != '0') {
            $(".acc").hide();
        }

        layui.use(['form', 'layer'], function () {
            var form = layui.form;

            //自定义表单验证

            form.on('select(up_type)', function (data) {

                if (data.value == '0') {
                    $(".acc").show();
                } else {
                    $(".acc").hide();
                }
            });

            form.verify({
                bankName: function (value, item) {//验证图标是否为空
                    if ($("#btype").val == "0") {
                        var val = $("#bankName").val();
                        if (!val) {
                            return "银行名不能为空";
                        }
                    }
                },
                cardName: function (value, item) {//验证图标是否为空
                    if ($("#btype").val == "0") {
                        var val = $("#cardName").val();
                        if (!val) {
                            return "持卡人姓名不能为空";
                        }
                    }
                },
                bankIfsc: function (value, item) {//验证图标是否为空
                    if ($("#btype").val == "0") {
                        var val = $("#bankIfsc").val();
                        if (!val) {
                            return "IFSC不能为空";
                        }
                    }
                },
                bankIfsc: function (value, item) {//验证图标是否为空
                    if ($("#btype").val == "0") {
                        var val = $("#bankNation").val();
                        if (!val) {
                            return "国家不能为空";
                        }
                    }
                }
            });

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
                            window.parent.refreshPage(data.msg);
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
