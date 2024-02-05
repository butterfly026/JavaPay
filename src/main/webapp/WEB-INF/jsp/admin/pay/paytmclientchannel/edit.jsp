<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>编辑上游通道</title>
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
            <label class="layui-form-label"><span class="span-red">*</span>选择上游</label>
            <div class="layui-input-block">
                <select name="clientId" id="clientId" lay-verify="required"
                        <c:if test="${result!=null}">disabled="disabled"</c:if> lay-search>
                    <c:forEach items="${clientList }" var="c">
                        <option value="${c.id}"
                                <c:if test="${result.clientId==c.id}">selected</c:if> >${c.name}</option>
                    </c:forEach>
                </select>
            </div>
        </div>

        <input type="hidden" name="channelTypeId" id="channelTypeId" value="9"/>
        <input type="hidden" name="ctype" id="ctype" value="1"/>

        <div class="layui-form-item">
            <label class="layui-form-label"><span class="span-red">*</span>通道名</label>
            <div class="layui-input-block">
                <input type="text" name="name" id="name" lay-verify="required" lay-verType="tips" placeholder="通道名"
                       autocomplete="off" class="layui-input" value="${result.name}"/>

            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label"><span class="span-red">*</span>商户ID</label>
            <div class="layui-input-block">
                <input type="text" name="paytmid" id="paytmid" lay-verify="required" lay-verType="tips"
                       placeholder="商户ID" autocomplete="off" class="layui-input" value="${result.paytmid}"/>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label"><span class="span-red">*</span>商户秘钥</label>
            <div class="layui-input-block">
                <input type="text" name="paytmkey" id="paytmkey" lay-verify="required" lay-verType="tips"
                       placeholder="商户秘钥" autocomplete="off" class="layui-input" value="${result.paytmkey}"/>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label"><span class="span-red">*</span>开启时间</label>
            <div class="layui-input-block">
                <input type="text" name="startTime" id="startTime" lay-verify="required" lay-verType="tips"
                       placeholder="开启时间" autocomplete="off" class="layui-input"
                       value="${result==null?'00:00:00':result.startTime}"/>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label"><span class="span-red">*</span>关闭时间</label>
            <div class="layui-input-block">
                <input type="text" name="endTime" id="endTime" lay-verify="required|dateValid" lay-verType="tips"
                       placeholder="关闭时间" autocomplete="off" class="layui-input"
                       value="${result==null?'23:59:59':result.endTime}"/>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label"><span class="span-red">*</span>通道费率(%)</label>
            <div class="layui-input-block">
                <input type="text" name="ratio" id="ratio" lay-verify="required|number" lay-verType="tips"
                       placeholder="通道费率" autocomplete="off" class="layui-input"
                       value="${result==null?0:result.ratio}"/>
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
            <label class="layui-form-label"><span class="span-red">*</span>模式</label>
            <div class="layui-input-block">
                <select name="mtype" lay-verify="required">
                    <option value="0" <c:if test='${result.mtype==0}'>selected</c:if>>最大最小金额</option>
                    <option value="1" <c:if test='${result.mtype==1}'>selected</c:if>>固定金额</option>
                </select>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label"><span class="span-red">*</span>最小金额</label>
            <div class="layui-input-block">
                <input type="text" name="minMoney" id="minMoney" lay-verify="required|number" lay-verType="tips"
                       placeholder="最小金额" autocomplete="off" class="layui-input"
                       value="${result==null?0:result.minMoney}"/>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label"><span class="span-red">*</span>最大金额</label>
            <div class="layui-input-block">
                <input type="text" name="maxMoney" id="maxMoney" lay-verify="required|number" lay-verType="tips"
                       placeholder="最大金额" autocomplete="off" class="layui-input"
                       value="${result==null?10000:result.maxMoney}"/>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">固定金额</label>
            <div class="layui-input-block">
                <textarea name="money" id="money" placeholder="固定金额，金额以英文逗号隔开"
                          class="layui-textarea">${result.money}</textarea>
            </div>
        </div>

        <!--
		<div class="layui-form-item">
			<label class="layui-form-label"><span class="span-red">*</span>KEY</label>
			<div class="layui-input-block">
				<select name="keyname" lay-verify="required" >
					<c:forEach items="${modelList }" var="c">
						<option value="${c.keyname}" <c:if test="${result.keyname eq c.keyname}">selected</c:if> >${c.name} ${c.status==1?'(启用)':'(禁用)' }</option>
					</c:forEach>
				</select>
			</div>
		</div>


		<div class="layui-form-item">
			<label class="layui-form-label">通道参数</label>
			<div class="layui-input-block">
				<textarea name="params" id="params" placeholder="通道参数" class="layui-textarea">${result.params}</textarea>
			</div>
		</div>

		<div class="layui-form-item">
			<label class="layui-form-label">通道描述</label>
			<div class="layui-input-block">
				<textarea name="descript" id="descript" placeholder="通道描述" class="layui-textarea">${result.descript}</textarea>
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

        layui.use(['form', 'layer', 'laydate'], function () {
            var form = layui.form;
            var laydate = layui.laydate;

            laydate.render({
                elem: '#startTime' //指定元素
                , type: "time"
                , format: 'HH:mm:ss'
            });

            laydate.render({
                elem: '#endTime' //指定元素
                , type: "time"
                , format: 'HH:mm:ss'
            });
            //自定义表单验证
            form.verify({
                dateValid: function (value, item) {//验证图标是否为空
                    var stime = $("#startTime").val();
                    console.log(value + "==" + stime);
                    if (!compareTime(stime, value)) {
                        return "起始时间不能大于结束时间";
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

        function compareTime(startTime, endTime) {
            if (startTime.length > 0 && startTime.length > 0) {
                var l1 = parseInt(startTime.replace(/\:/g, ""));
                var l2 = parseInt(endTime.replace(/\:/g, ""));
                return l1 < l2;
            } else {
                return false;
            }
        }
    });

    function createName() {
        var merchantName = $("#clientId").children("[value=" + $("#clientId").val() + "]").text();
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
