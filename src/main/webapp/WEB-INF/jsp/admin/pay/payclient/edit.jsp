<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>编辑上游管理</title>
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
            <label class="layui-form-label"><span class="span-red">*</span>账号</label>
            <div class="layui-input-block">
                <input type="text" name="username" id="username" lay-verify="required" lay-verType="tips"
                       placeholder="账号" autocomplete="off" class="layui-input" value="${result.username}"
                       <c:if test="${result!=null }">disabled</c:if> />
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label"><span class="span-red">*</span>上游名</label>
            <div class="layui-input-block">
                <input type="text" name="name" id="name" lay-verify="required" lay-verType="tips" placeholder="上游名"
                       autocomplete="off" class="layui-input" value="${result.name}"/>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">上游描述</label>
            <div class="layui-input-block">
                <input type="text" name="descript" id="descript" placeholder="上游描述" autocomplete="off"
                       class="layui-input" value="${result.descript}"/>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">网关</label>
            <div class="layui-input-block">
                <input type="text" name="url" id="url" placeholder="网关" autocomplete="off" class="layui-input"
                       value="${result.url}"/>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label"><span class="span-red">*</span>上游状态</label>
            <div class="layui-input-block">
                <select name="status" lay-verify="required">
                    <option value="1" <c:if test='${result.status=="1"}'>selected</c:if>>启用</option>
                    <option value="0" <c:if test='${result.status=="0"}'>selected</c:if>>禁用</option>
                </select>
            </div>
        </div>
        <!--
		<div class="layui-form-item">
			<label class="layui-form-label"><span class="span-red">*</span>API提现</label>
			<div class="layui-input-block">
				<select name="api" lay-verify="required">
					<option value="1"  <c:if test='${result.api==1}'>selected</c:if>>是</option>
					<option value="0"  <c:if test='${result.api==0}'>selected</c:if>>否</option>
				</select>
			</div>
		</div>
		 -->

        <div class="layui-form-item">
            <label class="layui-form-label"><span class="span-red">*</span>跳转方式</label>
            <div class="layui-input-block">
                <select name="gotype" lay-verify="required">
                    <option value="0" <c:if test='${result.gotype==0}'>selected</c:if>>API跳转</option>
                    <option value="1" <c:if test='${result.gotype==1}'>selected</c:if>>表单跳转</option>
                </select>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">上游商户号</label>
            <div class="layui-input-block">
                <input type="text" name="merchantNo" id="merchantNo" placeholder="上游商户号" autocomplete="off"
                       class="layui-input" value="${result.merchantNo}"/>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">上游商户秘钥</label>
            <div class="layui-input-block">
                <input type="text" name="merchantMy" id="merchantMy" placeholder="上游商户秘钥" autocomplete="off"
                       class="layui-input" value="${result.merchantMy}"/>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">下单地址</label>
            <div class="layui-input-block">
                <input type="text" name="urlpay" id="urlpay" placeholder="下单地址" autocomplete="off" class="layui-input"
                       value="${result.urlpay}"/>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">代付地址</label>
            <div class="layui-input-block">
                <input type="text" name="urlcash" id="urlcash" placeholder="代付地址" autocomplete="off" class="layui-input"
                       value="${result.urlcash}"/>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">查单地址</label>
            <div class="layui-input-block">
                <input type="text" name="urlquery" id="urlquery" placeholder="查单地址" autocomplete="off"
                       class="layui-input" value="${result.urlquery}"/>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">代付程序</label>
            <div class="layui-input-block">
                <select name="paykey" id="paykey" lay-search lay-filter="up_paykey">
                    <option value="">请选择</option>

                    <c:forEach items="${modelList }" var="c">
                        <option value="${c.keyname}"
                                <c:if test='${result.paykey eq c.keyname}'>selected</c:if>>${c.name}</option>
                    </c:forEach>

                </select>
                <input type="hidden" name="paykeyname" id="paykeyname" value="${result.paykeyname }"/>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">回调IP</label>
            <div class="layui-input-block">
                <input type="text" name="merchantIp" id="merchantIp" placeholder="回调IP,多个以英文逗号隔开" autocomplete="off"
                       class="layui-input" value="${result.merchantIp}"/>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label"><span class="span-red">*</span>上游押金</label>
            <div class="layui-input-block">
                <input type="text" name="yajin" id="yajin" lay-verify="required|number" lay-verType="tips"
                       placeholder="上游押金" autocomplete="off" class="layui-input"
                       value="${result==null?-1: null==result.yajin?-1:result.yajin}"/>
            </div>
        </div>

        <!--
		<div class="layui-form-item">
			<label class="layui-form-label">代付密码</label>
			<div class="layui-input-block">
				<input type="password" name="payword" id="payword" placeholder="代付密码，API代付需要用到。" autocomplete="off" class="layui-input"  value="${result.payword==null?'':'[payword]'}" />
			</div>
		</div>
		 -->
        <!--
		<div class="layui-form-item">
			<label class="layui-form-label"><span class="span-red">*</span>提现单笔费用</label>
			<div class="layui-input-block">
				<input type="text" name="cashCommission" id="cashCommission" lay-verify="required|number" lay-verType="tips" placeholder="提现手续费" autocomplete="off" class="layui-input"  value="${result==null?0:result.cashCommission}" />
			</div>
		</div>

		<div class="layui-form-item">
			<label class="layui-form-label"><span class="span-red">*</span>提现费率(%)</label>
			<div class="layui-input-block">
				<input type="text" name="cashRatio" id="cashRatio" lay-verify="required|number" lay-verType="tips" placeholder="最低提现金额" autocomplete="off" class="layui-input"  value="${result==null?0:result.cashRatio}" />
			</div>
		</div>
		 -->
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
            form.on('select(up_paykey)', function (data) {
                if (data.value != "") {
                    $("#paykeyname").val($("#paykey").find("option:selected").text());
                } else {
                    $("#paykeyname").val("");
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

            initSelectAll();
        });


    });
</script>
</body>
</html>
