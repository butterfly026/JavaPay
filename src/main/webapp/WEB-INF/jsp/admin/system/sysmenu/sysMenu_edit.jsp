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
            <label class="layui-form-label"><span class="span-red">*</span>菜单名</label>
            <div class="layui-input-block">
                <input type="text" name="name" id="name" lay-verify="required" lay-verType="tips" placeholder="菜单名"
                       autocomplete="off" class="layui-input" value="${result.name}"/>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">上级菜单</label>
            <div class="layui-input-block">
                <input type="hidden" name="fid" id="fid" value="${result.fid}"/>
                <input type="text" name="fname" id="fname" placeholder="点击选择上级菜单" title="点击选择上级菜单" autocomplete="off"
                       class="layui-input" value="${result.fname}" onclick="openMenuChoose();" readonly
                       style="cursor:pointer;"/>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">菜单地址</label>
            <div class="layui-input-block">
                <input type="text" name="url" id="url" placeholder="菜单地址" autocomplete="off" class="layui-input"
                       value="${result.url}"/>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">菜单序号</label>
            <div class="layui-input-block">
                <input type="text" name="order" id="order" placeholder="菜单序号" autocomplete="off" class="layui-input"
                       value="${result.order}"/>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">菜单状态</label>
            <div class="layui-input-block">
                <select name="status" lay-verify="required">
                    <option value="1" <c:if test='${result.status==null||result.status=="1"}'>selected</c:if>>启用
                    </option>
                    <option value="0" <c:if test='${result.status=="0"}'>selected</c:if>>禁用</option>
                </select>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">菜单权限</label>
            <div class="layui-input-block">
                <input type="text" name="permission" id="permission" placeholder="菜单权限" autocomplete="off"
                       class="layui-input" value="${result.permission}"/>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">菜单类型</label>
            <div class="layui-input-block">
                <select name="type" lay-verify="required">
                    <option value="0" <c:if test='${result.type==null||result.type=="0"}'>selected</c:if>>菜单</option>
                    <option value="1" <c:if test='${result.type=="1"}'>selected</c:if>>按钮</option>
                </select>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">菜单图标</label>
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
                <input type="hidden" name="image" id="image" value="${result.image}"/>
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
        <c:if test='${result.image!=null&&result.image!=""}'>
        image_arr[0] = '${result.image}';
        </c:if>

        layui.use(['form', 'layer'], function () {
            var form = layui.form;
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
            $("#image").val(files[0].path);
        } else {
            $("#image").val("");
        }
    }

    //获取选择的菜单
    function selectMenu(selid, selname) {
        if (selid == 0) {
            $("#fid").val("");
            $("#fname").val("");
        } else {
            $("#fid").val(selid);
            $("#fname").val(selname);
        }

        layer.close(lay_index);
    }

    //打开菜单选择
    function openMenuChoose() {
        lay_index = layer.open({
            type: 2,
            title: '选择上级菜单',
            skin: 'layui-layer-rim', //加上边框
            area: ['350px', '350px'], //宽高
            content: 'menuSelectSingle'
        });
    }
</script>
</body>
</html>
