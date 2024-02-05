<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>分配充值账号</title>
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

        <div class="layui-form-item">
            <label class="layui-form-label"><span class="span-red">*</span>商户号</label>
            <div class="layui-input-block">
                <span style="color:green;">${result.merchantNo}</span>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label"><span class="span-red">*</span>充值金额</label>
            <div class="layui-input-block">
                <span style="color:green;">${result.money} ${sysconfig.currency }</span>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label"><span class="span-red">*</span>账号录入模式</label>
            <div class="layui-input-block">
                <select name="smodel" id="smodel" lay-verify="required" lay-search lay-filter="up_smodel">
                    <option value="0" <c:if test="${result.smodel==0 }">selected</c:if>>已录入账号</option>
                    <option value="1" <c:if test="${result.smodel==1 }">selected</c:if>>手动录入</option>
                </select>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label"><span class="span-red">*</span>账号类型</label>
            <div class="layui-input-block">
                <select name="accType" id="accType" lay-verify="required" lay-search lay-filter="up_type">
                    <option value="0" <c:if test="${result.accType==0 }">selected</c:if>>银行卡账号</option>
                    <option value="1" <c:if test="${result.accType==1 }">selected</c:if>>UPI账号</option>
                </select>
            </div>
        </div>

        <div class="layui-form-item" id="bankdiv">
            <label class="layui-form-label"><span class="span-red">*</span>银行卡账号</label>
            <div class="layui-input-block">
                <select name="cardId" lay-verify="required" lay-search>
                    <c:forEach items="${cardList }" var="c">
                        <option value="${c.id}">${c.cardName}-${c.cardNo} [ 银行名:${c.bankName} | IFSC:${c.bankIfsc} |
                            国家:${c.bankNation} ]
                        </option>
                    </c:forEach>
                </select>
            </div>
        </div>

        <div class="layui-form-item" id="ptmdiv" style="display:none;">
            <label class="layui-form-label"><span class="span-red">*</span>UPI账号</label>
            <div class="layui-input-block">
                <select name="cardId1" lay-verify="required" lay-search>
                    <c:forEach items="${ptmList }" var="c">
                        <option value="${c.id}">${c.cardNo}</option>
                    </c:forEach>
                </select>
            </div>
        </div>

        <div class="layui-form-item acount">
            <label class="layui-form-label"><span class="span-red">*</span>卡号/UPI账号</label>
            <div class="layui-input-block">
                <input type="text" name="cardNo" id="cardNo" lay-verify="cardNo" lay-verType="tips"
                       placeholder="卡号/UPI账号" autocomplete="off" class="layui-input" value="${result.cardNo}"/>
            </div>
        </div>

        <div class="layui-form-item acc">
            <label class="layui-form-label"><span class="span-red accdiv">*</span>持卡人姓名</label>
            <div class="layui-input-block">
                <input type="text" name="cardName" id="cardName" lay-verify="cardName" lay-verType="tips"
                       placeholder="持卡人姓名" autocomplete="off" class="layui-input" value="${result.cardName}"/>
            </div>
        </div>

        <div class="layui-form-item acc">
            <label class="layui-form-label"><span class="span-red accdiv">*</span>银行名</label>
            <div class="layui-input-block">
                <input type="text" name="bankName" id="bankName" lay-verify="bankName" lay-verType="tips"
                       placeholder="银行名" autocomplete="off" class="layui-input" value="${result.bankName}"/>
            </div>
        </div>

        <div class="layui-form-item acc">
            <label class="layui-form-label"><span class="span-red accdiv">*</span>IFSC</label>
            <div class="layui-input-block">
                <input type="text" name="bankIfsc" id="bankIfsc" lay-verify="bankIfsc" lay-verType="tips"
                       placeholder="IFSC" autocomplete="off" class="layui-input" value="${result.bankIfsc}"/>
            </div>
        </div>

        <div class="layui-form-item acc">
            <label class="layui-form-label"><span class="span-red accdiv">*</span>国家</label>
            <div class="layui-input-block">
                <input type="text" name="bankNation" id="bankNation" lay-verify="bankNation" lay-verType="tips"
                       placeholder="国家" autocomplete="off" class="layui-input" value="${result.bankNation}"/>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">备注</label>
            <div class="layui-input-block">
                <textarea name="remark" id="remark" placeholder="审核备注" class="layui-textarea"></textarea>
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
    var status = "${result.status}";
    var merchantNo = "${result.merchantNo}";

    $(function () {
        var accType = $("#accType").val();

        if ($("#smodel").val() == 0) {
            $(".acount").hide();
            $(".acc").hide();

            if (accType == '0') {
                $("#bankdiv").show();
                $("#ptmdiv").hide();
            } else {
                $("#bankdiv").hide();
                $("#ptmdiv").show();
            }
        } else {
            $("#bankdiv").hide();
            $("#ptmdiv").hide();

            if (accType == '0') {
                $(".accdiv").show();
            } else {
                $(".accdiv").hide();
            }
        }


        layui.use(['form', 'layer'], function () {
            var form = layui.form;

            form.on('select(up_smodel)', function (data) {
                var accType = $("#accType").val();
                if (data.value == '0') {
                    $(".acount").hide();
                    $(".acc").hide();

                    if (accType == '0') {
                        $("#bankdiv").show();
                        $("#ptmdiv").hide();
                    } else {
                        $("#bankdiv").hide();
                        $("#ptmdiv").show();
                    }

                } else {
                    $(".acount").show();
                    $(".acc").show();
                    $("#bankdiv").hide();
                    $("#ptmdiv").hide();

                    if (accType == '0') {
                        $(".accdiv").show();
                    } else {
                        $(".accdiv").hide();
                    }
                }
            });

            form.on('select(up_type)', function (data) {
                var smodel = $("#smodel").val();
                if (smodel == 0) {
                    if (data.value == '0') {
                        $("#bankdiv").show();
                        $("#ptmdiv").hide();
                    } else {
                        $("#bankdiv").hide();
                        $("#ptmdiv").show();
                    }
                } else {
                    if (data.value == '0') {
                        $(".accdiv").show();
                    } else {
                        $(".accdiv").hide();
                    }
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
            form.verify({
                bankName: function (value, item) {//验证图标是否为空
                    if ($("#accType").val() == "0" && $("#smodel").val() == "1") {
                        var val = $("#bankName").val();
                        if (!val) {
                            return "银行名不能为空";
                        }
                    }
                },
                cardName: function (value, item) {//验证图标是否为空
                    if ($("#accType").val() == "0" && $("#smodel").val() == "1") {
                        var val = $("#cardName").val();
                        if (!val) {
                            return "持卡人姓名不能为空";
                        }
                    }
                },
                bankIfsc: function (value, item) {//验证图标是否为空
                    if ($("#accType").val() == "0" && $("#smodel").val() == "1") {
                        var val = $("#bankIfsc").val();
                        if (!val) {
                            return "IFSC不能为空";
                        }
                    }
                },
                bankIfsc: function (value, item) {//验证图标是否为空
                    if ($("#accType").val() == "0" && $("#smodel").val() == "1") {
                        var val = $("#bankNation").val();
                        if (!val) {
                            return "国家不能为空";
                        }
                    }
                },
                cardNo: function (value, item) {//验证图标是否为空
                    if ($("#accType").val() == "0" && $("#smodel").val() == "1") {
                        var val = $("#cardNo").val();
                        if (!val) {
                            return "账号/卡号不能为空";
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
