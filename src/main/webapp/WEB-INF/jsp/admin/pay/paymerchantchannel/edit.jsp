<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>编辑商户通道</title>
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
            <label class="layui-form-label"><span class="span-red">*</span>商户名</label>
            <div class="layui-input-block">
                <select name="merchantId" id="merchantId" lay-verify="required" lay-search
                        <c:if test="${result!=null}">disabled="disabled"</c:if> >
                    <c:forEach items="${merchantList }" var="c">
                        <option value="${c.id}"
                                <c:if test="${result.merchantId==c.id}">selected</c:if> >${c.username}-${c.name}</option>
                    </c:forEach>
                </select>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label"><span class="span-red">*</span>支付平台</label>
            <div class="layui-input-block">
                <select name="channelTypeId" id="channelTypeId" lay-search lay-verify="required"
                        <c:if test="${result!=null}">disabled="disabled"</c:if> >
                    <c:forEach items="${channelTypeList }" var="c">
                        <option value="${c.id}"
                                <c:if test="${result.channelTypeId==c.id}">selected</c:if> >${c.name}</option>
                    </c:forEach>
                </select>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label"><span class="span-red">*</span>通道名</label>
            <div class="layui-input-inline">
                <input type="text" name="name" id="name" lay-verify="required" lay-verType="tips" placeholder="通道名"
                       autocomplete="off" class="layui-input" value="${result.name}"/>
            </div>
            <div class="layui-form-mid layui-word-aux">
                <button type="button" class="layui-btn layui-btn-xs" onclick="createName()">
                    自动生成
                </button>
            </div>
        </div>


        <div class="layui-form-item">
            <label class="layui-form-label"><span class="span-red">*</span>状态</label>
            <div class="layui-input-block">
                <select name="status" lay-verify="required">
                    <option value="1" <c:if test='${result.status=="1"}'>selected</c:if>>启用</option>
                    <option value="0" <c:if test='${result.status=="0"}'>selected</c:if>>禁用</option>
                    
                    
                </select>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label"><span class="span-red">*</span>落地页</label>
            <div class="layui-input-block">
                <select name="useChannelPayurl" lay-verify="required">
                    <option value="1" <c:if test='${result.useChannelPayurl=="1"}'>selected</c:if>>打开</option>
                    <option value="0" <c:if test='${result.useChannelPayurl=="0"}'>selected</c:if>>关闭</option>
                    
                    
                </select>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label"><span class="span-red">*</span>商户费率(%)</label>
            <div class="layui-input-block">
                <input type="text" name="merchantRatio" id="merchantRatio" lay-verify="required|number"
                       lay-verType="tips" placeholder="商户费率(%)" autocomplete="off" class="layui-input"
                       value="${result==null?0:result.merchantRatio}"/>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label"><span class="span-red">*</span>代理费率(%)</label>
            <div class="layui-input-block">
                <input type="text" name="proxyRatio" id="proxyRatio" lay-verify="required|number" lay-verType="tips"
                       placeholder="代理费率(%)" autocomplete="off" class="layui-input"
                       value="${result==null?0:result.proxyRatio}"/>
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

    function createName() {
        var merchantName = $("#merchantId").children("[value=" + $("#merchantId").val() + "]").text();
        var i1 = merchantName.indexOf("-");
        if (i1 != -1) {
            merchantName = merchantName.substring(i1 + 1);
        }
        var channelName = $("#channelTypeId").children("[value=" + $("#channelTypeId").val() + "]").text();
        $("#name").val(merchantName + "-" + channelName);
    }
</script>
</body>
</html>
