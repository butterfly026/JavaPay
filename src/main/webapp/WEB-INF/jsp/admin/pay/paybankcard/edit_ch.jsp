<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>编辑银行卡</title>
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
        <input type="hidden" name="btype" value="0">
        <div style="color:red;width:90%;padding-left:20px;margin-bottom:10px;">请尽可能多的完善您的银行卡信息</div>

        <div class="layui-form-item">
            <label class="layui-form-label"><span class="span-red">*</span>卡号</label>
            <div class="layui-input-block">
                <input type="text" name="cardNo" id="cardNo" lay-verify="required" lay-verType="tips" placeholder="卡号"
                       autocomplete="off" class="layui-input" value="${result.cardNo}"/>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label"><span class="span-red">*</span>持卡人姓名</label>
            <div class="layui-input-block">
                <input type="text" name="cardName" id="cardName" lay-verify="required" lay-verType="tips"
                       placeholder="持卡人姓名" autocomplete="off" class="layui-input" value="${result.cardName}"/>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label"><span class="span-red">*</span>银行</label>
            <div class="layui-input-block">
                <select name="bankCode" lay-verify="required" lay-search>
                    <c:forEach items="${bankCodeList }" var="c">
                        <option value="${c.code}"
                                <c:if test='${result.bankCode==c.code}'>selected</c:if> >${c.name}</option>
                    </c:forEach>
                </select>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">开户省份</label>
            <div class="layui-input-block">
                <input type="text" name="province" id="province" placeholder="开户省份" autocomplete="off"
                       class="layui-input" value="${result.province}"/>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">开户城市</label>
            <div class="layui-input-block">
                <input type="text" name="city" id="city" placeholder="开户城市" autocomplete="off" class="layui-input"
                       value="${result.city}"/>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">支行名</label>
            <div class="layui-input-block">
                <input type="text" name="bankSubname" id="bankSubname" placeholder="支行名" autocomplete="off"
                       class="layui-input" value="${result.bankSubname}"/>
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
</script>
</body>
</html>
