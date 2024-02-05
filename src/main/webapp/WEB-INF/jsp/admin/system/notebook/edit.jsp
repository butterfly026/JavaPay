<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>编辑页面</title>
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
            <label class="layui-form-label"><span class="span-red">*</span>标题</label>
            <div class="layui-input-block">
                <input type="text" name="title" id="title" lay-verify="required" placeholder="标题" autocomplete="off"
                       class="layui-input" value="${result.title}"/>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label"><span class="span-red">*</span>内容</label>
            <div class="layui-input-block">
                <textarea name="content" id="content" lay-verify="required" placeholder="内容"
                          class="layui-textarea">${result.content}</textarea>
                <%--    <input type="text" name="content" id="content" lay-verify="required"  placeholder="内容" autocomplete="off" class="layui-input" value="${result.content}" />
               --%> </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label"><span class="span-red">*</span>关联内容</label>
            <div class="layui-input-block">
                <input type="text" name="linkContent" id="linkContent" lay-verify="required"
                       placeholder="关联内容|单号|商户号|代理" autocomplete="off" class="layui-input"
                       value="${result.linkContent}"/>
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
    path = "editSave";
    addFlag = false;

    $(function () {
        layui.use(['form', 'layer'], function () {
            var form = layui.form;

            //按钮事件
            form.on('submit(edit)', function (fromData) {
                lay_index = layer.load(1, {
                    shade: [0.1, '#fff'] //0.1透明度的白色背景
                });
                $.ajax({
                    type: 'post',
                    url: path,
                    data: fromData.field,      //getFormJsonData("#editForm"),
                    //   dataType: "json",
                    cache: false,
                    success: function (data) {
                        layer.close(lay_index);
                        //if(data.msg) layer.msg(data.msg);
                        if (data.code == 200) {
                            window.parent.refreshPage(data.msg);
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
