<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>编辑支付订单</title>
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
            <label class="layui-form-label"><span class="span-red">*</span>商户通道</label>
            <div class="layui-input-block">
                <select name="merchantChannelId" id="merchantChannelId" lay-verify="required" lay-search
                        <c:if test="${result!=null}">disabled="disabled"</c:if> >
                    <c:forEach items="${merchantChannelList }" var="c">
                        <option value="${c.id}">${c.username} [ ${c.name} ]</option>
                    </c:forEach>
                </select>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label"><span class="span-red">*</span>上游通道</label>
            <div class="layui-input-block">
                <select name="clientChannelId" id="clientChannelId" lay-verify="required" lay-search
                        <c:if test="${result!=null}">disabled="disabled"</c:if> >
                    <c:forEach items="${clientChannelList }" var="c">
                        <option value="${c.id}">${c.username} [ ${c.name} ]</option>
                    </c:forEach>
                </select>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label"><span class="span-red">*</span>订单号</label>
            <div class="layui-input-inline">
                <input type="text" name="sn" id="sn" lay-verify="required" lay-verType="tips" placeholder="订单号"
                       autocomplete="off" class="layui-input"/>
            </div>
            <div class="layui-form-mid layui-word-aux">
                <button type="button" class="layui-btn layui-btn-xs" onclick="createSn()">
                    随机生成单号
                </button>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label"><span class="span-red">*</span>订单金额</label>
            <div class="layui-input-block">
                <input type="text" name="money" id="money" lay-verify="required|number" lay-verType="tips"
                       placeholder="订单金额" autocomplete="off" class="layui-input" value=""/>
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
    var path = "createNullOrder";


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

        createSn();

    });

    function createSn() {
        $("#sn").val("BD" + guid2());
    }

    function guid2() {
        function S4() {
            return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
        }

        return (S4() + S4() + S4() + S4() + S4());
    }
</script>
</body>
</html>
