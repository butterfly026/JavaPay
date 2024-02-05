<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>余额冻结</title>
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
            <label class="layui-form-label"><span class="span-red">*</span>用户类型</label>
            <div class="layui-input-block">
                <select name="type" lay-verify="required" lay-search lay-filter="up_type">
                    <option value="1">商户</option>
                    <option value="0">代理</option>
                </select>
            </div>
        </div>

        <div class="layui-form-item" id="proxyDiv" style="display:none;">
            <label class="layui-form-label"><span class="span-red">*</span>代理账号</label>
            <div class="layui-input-block">
                <select name="proxyId" lay-verify="required"
                        <c:if test="${result!=null}">disabled="disabled"</c:if> lay-search>
                    <c:forEach items="${proxyList }" var="c">
                        <option value="${c.id}">${c.username}-${c.name}[余额${c.money} ${sysconfig.currency },冻结${c.frozenMoney } ${sysconfig.currency },可用${c.amount } ${sysconfig.currency }]</option>
                    </c:forEach>
                </select>
            </div>
        </div>

        <div class="layui-form-item" id="merchantDiv">
            <label class="layui-form-label"><span class="span-red">*</span>商户账号</label>
            <div class="layui-input-block">
                <select name="merchantId" lay-verify="required"
                        <c:if test="${result!=null}">disabled="disabled"</c:if> lay-search>
                    <c:forEach items="${merchantList }" var="c">
                        <option value="${c.id}">${c.username}-${c.name}[余额${c.money} ${sysconfig.currency },冻结${c.frozenMoney } ${sysconfig.currency },可用${c.amount } ${sysconfig.currency }]</option>
                    </c:forEach>
                </select>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label"><span class="span-red">*</span>冻结金额</label>
            <div class="layui-input-block">
                <input type="text" name="money" id="money" lay-verify="required|number" lay-verType="tips"
                       placeholder="冻结金额" autocomplete="off" class="layui-input"
                       value="${result==null?0:result.money}"/>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">备注</label>
            <div class="layui-input-block">
                <textarea name="remark" id="remark" placeholder="备注" class="layui-textarea">${result.remark}</textarea>
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

        layui.use(['form', 'layer'], function () {
            var form = layui.form;

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
            form.on('select(up_type)', function (data) {
                if (data.value == '0') {
                    $("#proxyDiv").show();
                    $("#merchantDiv").hide();
                } else {
                    $("#merchantDiv").show();
                    $("#proxyDiv").hide();
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
            initSelectAll();
        });


    });
</script>
</body>
</html>
