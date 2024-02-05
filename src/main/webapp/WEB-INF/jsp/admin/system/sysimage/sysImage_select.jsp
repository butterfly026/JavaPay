<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>查询页面</title>
    <link rel="stylesheet" href="${context}/layui/css/layui.css">
    <link rel="stylesheet" href="${context}/css/img_select.css?1">
    <!--[if lt IE 9]>
      <script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
      <script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<div class="admin-main">
    <form id="searchForm" class="layui-form">
        <div class="layui-row" style="padding-bottom:5px;">
            <div class="layui-col-xs4 layui-col-sm4 layui-col-md4">
                <select name="folderId" id="folderId">
                    <option value="">请选择目录</option>
                    <c:forEach var="list" items="${folderList}">
                        <option value="${list.id}">${list.name}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="layui-col-xs4 layui-col-sm4 layui-col-md4">
                <input type="text" name="name" id="name" placeholder="图片名" autocomplete="off" class="layui-input">
            </div>
            <div class="layui-col-xs4 layui-col-sm4 layui-col-md4">
                <button class="layui-btn layui-btn-normal" type="button" onclick="searchData();">搜索</button>
                <span class="imgdesc">图片多选支持跨页勾选</span>
            </div>
        </div>
    </form>
    <div style="border:1px solid #e6e6e6;width:100%;overflow-y:auto;" id="imgTable">
        <ul class="imglist">


        </ul>
    </div>

    <div id="pageDiv">

        <ul class="pagination">

        </ul>
        <button class="layui-btn layui-btn-normal layui-btn-sm" style="float:right;" type="button"
                onclick="callbackParent();">确定选择
        </button>
    </div>
</div>
<script src="${context}/js/jquery.min.js"></script>
<script src="${context}/layui/layui.js"></script>
<script src="${context}/js/xym_page.min.js"></script>
<script>
    var layer;
    var type = '${type}';
    var PAGE_SIZE = 20;

    //确定选择，回调父窗口
    function callbackParent() {
        <c:if test='${source=="uploader"}'>
        window.parent.${method}.addFileByBox(ckimgs);
        </c:if>
        <c:if test='${source!="uploader"}'>
        window.parent.${method}(ckimgs);
        </c:if>
    }

    var $p;
    var lay_index;
    var serverDatas;
    var ckimgs = new Array();//选中的图片对象数组

    //选中的图片对象
    function checkImg(id, name, thumbPath, path) {
        this.id = id,
            this.name = name,
            this.path = path,
            this.thumbPath = thumbPath
    }

    $(function () {
        $p = new xymPage({
            elem: '.pagination',
            page: 0,
            pageSize: 20,
            totalCount: 0,
            callFunc: function (page, pageSize) {

                searchData(page, pageSize);
            }
        });

        //加载数据
        layui.use(['layer', 'form'], function () {
            layer = layui.layer;


            searchData(1, PAGE_SIZE);
        });

        var $imgTable = $("#imgTable");
        var $tableheight = $(window).height() - $imgTable.offset().top - $("#pageDiv").height() - 10;
        $imgTable.css("height", $tableheight);
        $(window).resize(function () {
            $tableheight = $(window).height() - $imgTable.offset().top - $("#pageDiv").height() - 10;
            $imgTable.css("height", $tableheight);
        });
    });

    function searchData(pageNo, pageSize) {//加载图片数据
        lay_index = layer.load(1, {
            shade: [0.1, '#fff'] //0.1透明度的白色背景
        });
        $.ajax({
            type: 'post',
            url: 'list',
            data: {folderId: $("#folderId").val(), name: $("#name").val(), pageNo: pageNo, pageSize: pageSize},
            dataType: "json",
            cache: false,
            success: function (data) {
                layer.close(lay_index);
                if (data.code == 200) {
                    serverDatas = data.result.results;
                    createDataListView(serverDatas);
                    $.each($(".imglist li"), function () {//选中已经选中的图片
                        var index = $(this).attr("idx");
                        var id = serverDatas[index].id;
                        var $ck = $(this).find(".check-img");
                        for (var i = 0; i < ckimgs.length; i++) {
                            if (id == ckimgs[i].id) {
                                $ck.show();
                                return;
                            }
                        }
                    });

                    $(".imglist li").click(function () {
                        var index = $(this).attr("idx");
                        var id = serverDatas[index].id;
                        var $ck = $(this).find(".check-img");
                        if (type == "true") {//多选
                            for (var i = 0; i < ckimgs.length; i++) {
                                if (id == ckimgs[i].id) {//如果被选中，移除
                                    ckimgs.splice(i, 1);
                                    $ck.hide();
                                    updateSelinfo();
                                    return;
                                }
                            }
                            ckimgs[ckimgs.length] = new checkImg(serverDatas[index].id, serverDatas[index].name, serverDatas[index].thumbPath, serverDatas[index].path);
                            $ck.show();
                        } else {
                            $(".check-img").hide();
                            if (ckimgs.length > 0 && serverDatas[index].id == ckimgs[0].id) {//如果是选中项,移除
                                ckimgs.splice(0, 1);
                            } else {
                                ckimgs[0] = new checkImg(serverDatas[index].id, serverDatas[index].name, serverDatas[index].thumbPath, serverDatas[index].path);
                                $ck.show();
                            }
                        }
                        updateSelinfo();
                    });
                    $p.refreshPage(data.result.total, data.result.currentPage);
                } else {
                    if (data.msg) layer.msg(data.msg);
                }
            },
            error: function (xhr, desc, err) {
                layer.msg("数据请求失败:" + desc);
                layer.close(lay_index);
            }
        });
    }

    function createDataListView(datas) {
        var $str = '';
        for (var i = 0; i < datas.length; i++) {
            $str += '<li idx="' + i + '"><div class="img-container">';
            $str += '<span class="main-img"><img src="' + datas[i].thumbPath + '"/></span>';
            $str += '<img src="${context}/lib/webuploader/checked.png" class="check-img"/>';
            $str += '</div><div class="txt">' + datas[i].name + '</div></li>';
        }
        $(".imglist").html($str);
    }

    //更新图片选中条数
    function updateSelinfo() {
        $(".imgdesc").text("当前共选中" + ckimgs.length + "张图片");
    }
</script>
</body>
</html>
