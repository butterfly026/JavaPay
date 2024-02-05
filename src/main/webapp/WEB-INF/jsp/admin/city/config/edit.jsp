<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>编辑栏目</title>
    <link rel="stylesheet" href="${context}/layui/css/layui.css">
    <link rel="stylesheet" href="${context}/lib/webuploader/webuploader-xym.css">
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
            <label class="layui-form-label"><span class="span-red">*</span>网站名</label>
            <div class="layui-input-block">
                <input type="text" name="cname" id="cname" lay-verify="required" lay-verType="tips" placeholder="网站名"
                       autocomplete="off" class="layui-input" value="${result.cname}"/>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">logo</label>
            <div class="layui-input-block">

                <div class="upload-image-container">
                    <div>
                        <span class="webuploader-pick-selectimage" id="imagesBtn">从图库选择</span>

                        <span id="filePicker">添加图片</span>
                        <span class="webuploader-pick-uploadimage" id="uploadImageBtn">上传图片</span>


                    </div>

                    <ul class="imglist">

                    </ul>
                    <div style="clear:both;"></div>
                    <div class="upload-all-info"></div>
                </div>
                <input type="hidden" name="logo" id="logo" value="${result.logo}"/>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">主域名</label>
            <div class="layui-input-block">
                <input type="text" name="domain" id="domain" placeholder="主域名" autocomplete="off" class="layui-input"
                       value="${result.domain}"/>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">API域名</label>
            <div class="layui-input-block">
                <input type="text" name="apidomain" id="apidomain" placeholder="API域名" autocomplete="off"
                       class="layui-input" value="${result.apidomain}"/>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">跳转地址列表</label>
            <div class="layui-input-block">
                <input type="text" name="payClientUrl" id="payClientUrl" placeholder="跳转地址列表" autocomplete="off"
                       class="layui-input" value="${result.payClientUrl}"/>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">高速队列地址</label>
            <div class="layui-input-block">
                <input type="text" name="queueUrl" id="queueUrl" placeholder="高速队列服务器地址" autocomplete="off" class="layui-input"
                       value="${result.queueUrl}"/>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">订单通知模式</label>
            <div class="layui-input-block">
                <input type="number" name="servermodel" id="servermodel" placeholder="通知模式：0-NotifyServ,1-本地,2-高速队列" autocomplete="off" class="layui-input"
                       value="${result.servermodel}"/>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">订单发起代理</label>
            <div class="layui-input-block">
                <input type="text" name="proxyInfoBlock" id="proxyInfoBlock" placeholder="HOST:PORT:USER:PWD 多个用'|'分割" autocomplete="off" class="layui-input"
                       value="${result.proxyInfoBlock}"/>
            </div>
        </div>


        <div class="layui-form-item">
            <label class="layui-form-label">回调IP</label>
            <div class="layui-input-block">
                <input type="text" name="nip" id="nip" placeholder="回调IP" autocomplete="off" class="layui-input"
                       value="${result.nip}"/>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">币种单位</label>
            <div class="layui-input-block">
                <input type="text" name="currency" id="currency" placeholder="币种单位" autocomplete="off"
                       class="layui-input" value="${result.currency}"/>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">提现模式</label>
            <div class="layui-input-block">
                <select name="cashMode" lay-verify="required">
                    <option value="0"
                            <c:if test='${result.cashMode==null||result.cashMode=="0"}'>selected</c:if> >India
                    </option>
                    <option value="1"
                            <c:if test='${result.cashMode=="1"}'>selected</c:if> >China
                    </option>
                </select>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label"><span class="span-red">*</span>开启平台优先</label>
            <div class="layui-input-inline">
                <select name="platformvalid" id="platformvalid" lay-verify="required">
                    <option value="0"
                            <c:if test='${result.platformvalid==0}'>selected</c:if> >关闭
                    </option>
                    <option value="1"
                            <c:if test='${result.platformvalid==1}'>selected</c:if> >开启
                    </option>
                </select>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label"><span class="span-red">*</span>回调通知强验证</label>
            <div class="layui-input-inline">
                <select name="ordervalid" id="ordervalid" lay-verify="required">
                    <option value="0"
                            <c:if test='${result.ordervalid==0}'>selected</c:if> >关闭
                    </option>
                    <option value="1"
                            <c:if test='${result.ordervalid==1}'>selected</c:if> >开启
                    </option>
                </select>
            </div>
            <div class="layui-form-mid layui-word-aux">
                <span style="color:red;">开启后会在通知验证成功后发起查单,以查单结果做为订单结果</span>
            </div>
        </div>

        <!--
		<div class="layui-form-item">
			<label class="layui-form-label"><span class="span-red">*</span>内存优化</label>
			<div class="layui-input-inline">
				<select name="proxyopen" id="proxyopen" lay-verify="required">
					<option value="0" <c:if test='${result.proxyopen==0}'>selected</c:if> >关闭</option>
					<option value="1" <c:if test='${result.proxyopen==1}'>selected</c:if> >开启</option>
				</select>
			</div>
			<div class="layui-form-mid layui-word-aux">
				<span style="color:red;">尽可能的优化内存,会消耗CPU</span>
			</div>
		</div>

		<div class="layui-form-item">
			<label class="layui-form-label"><span class="span-red">*</span>优化比例(%)</label>
			<div class="layui-input-inline">
				<input type="text" name="proxyratio" id="proxyratio" lay-verify="required|number" lay-verType="tips" placeholder="优化比例(%)" autocomplete="off" class="layui-input"  value="${result==null?0:result.proxyratio}" />
			</div>
			<div class="layui-form-mid layui-word-aux">
				<span style="color:red;">越高消耗CPU越大</span>
			</div>
		</div>
		 -->
        <div class="layui-form-item">
            <label class="layui-form-label"><span class="span-red">*</span>统计上游通道</label>
            <div class="layui-input-inline">
                <input type="text" name="beforetime" id="beforetime" lay-verify="required|number" lay-verType="tips"
                       autocomplete="off" class="layui-input" value="${result==null?0:result.beforetime}"/>
            </div>
            <div class="layui-form-mid layui-word-aux">
                <span style="color:red;">秒之前</span>
            </div>
            <div class="layui-input-inline">
                <input type="text" name="staticstime" id="staticstime" lay-verify="required|number" lay-verType="tips"
                       autocomplete="off" class="layui-input" value="${result==null?0:result.staticstime}"/>
            </div>
            <div class="layui-form-mid layui-word-aux">
                <span style="color:red;">秒之内的成功率</span>
            </div>

        </div>

        <!--
		<div class="layui-form-item">
			<label class="layui-form-label">订单掉单间隔笔数</label>
			<div class="layui-input-block">
				<input type="text" name="ordercount" id="ordercount" lay-verify="required|number" lay-verType="tips" placeholder="订单掉单间隔笔数" autocomplete="off" class="layui-input"  value="${result.ordercount==null?0:result.ordercount}" />
			</div>
		</div>

		<div class="layui-form-item">
			<label class="layui-form-label">上游/商户名</label>
			<div class="layui-input-block">
				<input type="text" name="channelkeyword" id="channelkeyword" placeholder="上游/商户名，多个以逗号隔开" autocomplete="off" class="layui-input" value="${result.channelkeyword}"/>
			</div>
		</div>

		<div class="layui-form-item">
			<label class="layui-form-label">掉单金额</label>
			<div class="layui-input-block">
				<input type="text" name="ordermoney" id="ordermoney" placeholder="掉单金额，多个用逗号隔开" autocomplete="off" class="layui-input" value="${result.ordermoney}"/>
			</div>
		</div>
		 -->

        <div class="layui-form-item">
            <label class="layui-form-label">请求规则</label>
            <div class="layui-input-block">
                <textarea name="creq" id="creq" placeholder="请输入请求规则" class="layui-textarea">${result.creq}</textarea>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">后台域名设置</label>
            <div class="layui-input-block">
                <textarea name="cadmin" id="cadmin" placeholder="请输入后台域名设置"
                          class="layui-textarea">${result.cadmin}</textarea>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">回调域名/IP(给上游)</label>
            <div class="layui-input-block">
                <input type="text" name="notifyurl" id="notifyurl" placeholder="回调域名/IP(给上游)" autocomplete="off"
                       class="layui-input" value="${result.notifyurl}"/>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">回调域名2/IP</label>
            <div class="layui-input-block">
                <input type="text" name="notifyUrl2" id="notifyUrl2" placeholder="回调域名2/IP" autocomplete="off"
                       class="layui-input" value="${result.notifyUrl2}"/>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">系统回调服务器的域名/IP</label>
            <div class="layui-input-block">
                <input type="text" name="paynotifyurl" id="paynotifyurl" placeholder="系统回调服务器的域名/IP" autocomplete="off"
                       class="layui-input" value="${result.paynotifyurl}"/>
            </div>
        </div>
        <!--
		<div class="layui-form-item">
			<label class="layui-form-label">商户首页说明文字</label>
			<div class="layui-input-block">
				<textarea name="cfoot" id="cfoot" placeholder="请输入内容" class="layui-textarea">${result.cfoot}</textarea>
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
<script src="${context}/layui/layui.js"></script>
<script type="text/javascript" src="${context}/lib/webuploader/webuploader.js"></script>
<script type="text/javascript" src="${context}/lib/webuploader/webuploader-xym.js?5"></script>
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

    var upFile;
    $(function () {
        var image_arr = new Array();
        <c:if test='${result.logo!=null&&result.logo!=""}'>
        image_arr[0] = '${result.logo}';
        </c:if>

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
        });

        upFile = new UploadFiles({
            //元素名
            elem: '.upload-image-container',
            //是否多选
            multiple: false,
            //webuploader使用的元素
            uploaderElem: '.upload-image-container #filePicker',
            //服务器域名
            serverUrl: '${context}',
            //文件上传路径
            uploaderUrl: '${context}/system/sysImage/addSave',
            //表单参数
            formData: {},
            //文件最大个数
            fileNumLimit: 0,
            //文件总大小
            fileSizeLimit: 500 * 1024 * 1024,
            //单个文件大小
            fileSingleSizeLimit: 50 * 1024 * 1024,
            //预览图宽度
            imgWidth: 60,
            //预览图高度
            imgHeight: 60,
            //弹层宽度
            dlgWidth: '100%',
            //弹层高度
            dlgHeight: '80%',
            //当前对象的变量名，从图库选择图片时需要用到
            objName: 'upFile',
            //图片添加成功或移除时调用
            imgChange: imgChange,
            //初始化的图片数据，图片url数组
            datas: image_arr
        });
    });

    //图片控件的回调事件
    function imgChange(files) {
        //alert(JSON.stringify(files));
        if (files && files.length > 0) {
            $("#logo").val(files[0].path);
        } else {
            $("#logo").val("");
        }
    }
</script>
</body>
</html>
