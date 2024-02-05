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
            <label class="layui-form-label"><span class="span-red">*</span>登录名</label>
            <div class="layui-input-block">
                <input type="text" name="username" id="username" lay-verify="required" lay-verType="tips"
                       placeholder="登录名" autocomplete="off" class="layui-input" value="${result.username}"/>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">手机号码</label>
            <div class="layui-input-block">
                <input type="text" name="phone" id="phone" placeholder="手机号码" autocomplete="off" class="layui-input"
                       value="${result.phone}"/>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">用户名称</label>
            <div class="layui-input-block">
                <input type="text" name="name" id="name" placeholder="用户名称" autocomplete="off" class="layui-input"
                       value="${result.name}"/>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label"><span id="password_log" class="span-red">*</span>密码<span id="upd_no"
                                                                                                     style="color:red;display:none;">(不修改请留空)</span></label>
            <div class="layui-input-block">
                <input type="password" name="password" id="password" placeholder="密码"
                       <c:if test="${result==null}">lay-verify="required" lay-verType="tips"</c:if> autocomplete="off"
                       class="layui-input"/>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label"><span id="password2_log" class="span-red">*</span>确认密码</label>
            <div class="layui-input-block">
                <input type="password" name="password1" id="password1" placeholder="确认密码" lay-verify="rpwd"
                       lay-verType="tips" autocomplete="off" class="layui-input"/>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">用户状态</label>
            <div class="layui-input-block">
                <select name="status" lay-verify="required">
                    <option value="1" <c:if test='${result.status=="1"}'>selected</c:if>>启用</option>
                    <option value="0" <c:if test='${result.status=="0"}'>selected</c:if>>禁用</option>
                </select>
                <input type="hidden" name="type" id="type" value="0"/>
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
        layui.use(['form', 'layer'], function () {
            var form = layui.form;

            //自定义表单验证
            form.verify({
                rpwd: function (value, item) {//确认密码验证
                    var pwd = $('#password').val();
                    if (pwd != "") {//如果原密码不为空
                        if (value != pwd) {
                            return "两次输入的密码必须一样";
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
        if (!addFlag) {
            $('#username').attr("readonly", "readonly");//设置登录名为只读不可修改
            $("#password_log").hide();
            $("#password2_log").hide();
            $("#upd_no").show();
        }
    });
</script>
</body>
</html>
