<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="adminPermission" uri="http://adminpermission.city.com" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>图片管理</title>
    <link rel="stylesheet" href="${context}/layui/css/layui.css">
    <link rel="stylesheet" href="${context}/css/style.css">
    <style>
        .admin-main {
            margin: 5px;
        }

        .span-red {
            color: red;
        }

        .imglist {
            font: 12px verdana, arial, sans-serif; /* 设置文字大小和字体样式 */
        }

        .imglist, .imglist li {
            list-style: none; /* 将默认的列表符号去掉 */
            padding: 0; /* 将默认的内边距去掉 */
            margin: 0; /* 将默认的外边距去掉 */
        }

        .imglist li {
            margin-top: 5px;
            margin-left: 40px; /* 设置内边距 */
            float: left; /* 往左浮动 */
            width: 100px;
            height: 150px;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
            word-break: keep-all; /* 不换行 */
            white-space: nowrap; /* 不换行 */
            background-color: #FFFFFF;
            border: 1px solid #FFFFFF;
        }

        .imglist li img {
            max-height: 95px;
            max-width: 95px;
            width: expression(this.width > 95 && this.width > this.height ? 95 : auto);
            height: expression(this.height > 95 ? 95 : auto);
        }

        .imglist li:hover {
            background-color: #F4F4F4;
            cursor: pointer;
        }

        .imglist li .img-select {
            border: 1px solid #FF0000;
        }

        .imglist li .txt {
            width: 100%;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
            text-align: left;
            padding-left: 5px;
        }

        .imglist li .btns {
            width: 100%;
            white-space: nowrap;
        }

        .pageloc {
            position: fixed;
            left: 185px;
            right: 5px;
            bottom: 5px;
            height: 50px;
            line-height: 50px;
            z-index: 1;
        }
    </style>
    <!--[if lt IE 9]>
      <script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
      <script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    <link rel="stylesheet" type="text/css" href="${context}/lib/zTree/v3/css/zTreeStyle/zTreeStyle.css"/>
</head>
<body>
<div class="layui-fluid admin-head-title">
    <div class="layui-row left">
        <div class="layui-col-xs11 layui-col-sm11 layui-col-md11">
            <h3 class="layui-nav-item"><b>&nbsp;图片管理</b></h3>
        </div>
        <div class="layui-col-xs1 layui-col-sm1 layui-col-md1 right">
            <button class="layui-btn layui-btn-sm layui-btn-normal" onclick="window.location.reload();" title="刷新页面">
                <i class="layui-icon">&#x1002;</i>
            </button>
        </div>
    </div>
</div>
<div class="admin-main">
    <div style="border:1px solid #e6e6e6;" id="imgtable">
        <div style="border-right:1px solid #e6e6e6;width:180px;float:left;" id="imgtable-left">
            <ul id="tree_div" class="ztree"></ul>
        </div>
        <div style="overflow:hidden;text-align:center;" id="imgtable-right">
            <div style="overflow-y:auto;" id="img-contant">
                <ul class="imglist" id="img-listview">

                </ul>
            </div>
            <div id="pageDiv" class="pageloc"></div>
        </div>
    </div>
</div>


<div class="contextMenu" id="allMenu">
    <ul>
        <li id="createFolder">创建目录</li>
        <li id="createFile">上传图片</li>
        <li id="refresh">刷新</li>
        <li id="help">使用帮助</li>
    </ul>
</div>

<div class="contextMenu" id="folderMenu">
    <ul>
        <li id="rename">重命名</li>
        <li id="delete">删除</li>
    </ul>
</div>

<div class="contextMenu" id="fileMenu">
    <ul>
        <li id="rename">重命名</li>
        <li id="geturl">获取链接</li>
        <li id="details">图片属性</li>
        <li id="download">下载图片</li>
        <li id="img-delete">删除</li>
        <li id="imgs-delete">删除选中图片</li>
    </ul>
</div>

<div id="view_urls" style="display:none;">
    <table class="layui-table" lay-size="sm">
        <tbody>
        <tr>
            <td colspan="2"><b>如需在当前网站的其他页面引用图片：可以使用站内链接，如需在其他网站使用图片，请用站外链接</b></td>
        </tr>
        <tr>
            <td>站内链接</td>
            <td><span class="url-path"></span></td>
        </tr>
        <tr>
            <td>站外引用链接</td>
            <td>${context}<span class="url-path"></span></td>
        </tr>
        </tbody>
    </table>
</div>

<div id="view_images" style="display:none;">
    <table class="layui-table" lay-size="sm">
        <colgroup>
            <col width="200">
            <col>
        </colgroup>
        <thead>
        <tr>
            <td>属性</td>
            <td>内容</td>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td>图片名</td>
            <td><span id="image-name"></span></td>
        </tr>
        <tr>
            <td>图片目录</td>
            <td><span id="image-folder"></span></td>
        </tr>
        <tr>
            <td>创建时间</td>
            <td><span id="image-time"></span></td>
        </tr>
        <tr>
            <td>图片大小</td>
            <td><span id="image-size"></span></td>
        </tr>
        <tr>
            <td>图片类型</td>
            <td><span id="image-type"></span></td>
        </tr>
        <tr>
            <td>上传用户</td>
            <td><span id="image-user"></span></td>
        </tr>
        </tbody>
    </table>
</div>

<script src="${context}/js/jquery.min.js"></script>
<script src="${context}/layui/layui.js"></script>
<script src="${context}/admin/main/js/jquery.contextmenu.r2.min.js"></script>
<script type="text/javascript" src="${context}/lib/zTree/v3/js/jquery.ztree.all.min.js"></script>
<script src="${context}/js/common.js"></script>
<script>
    var selImgs = new Array();//所有被选中的孩子
    var lay_index;//弹出层索引
    var folderId = 0;//目录id
    function selImg(id, name, url) {
        this.id = id,
            this.name = name,
            this.url = url
    }

    var serverDatas;//服务器返回的数据

    var PAGE_SIZE = 20;

    var laypage;
    var layer;
    var zTree;

    $(function () {

        var tbheight = $(document).height() - $("#imgtable").offset().top - 10;
        $("#imgtable").css("height", tbheight);
        $("#imgtable-left").css("height", tbheight);
        $("#imgtable-right").css("height", tbheight);
        $("#img-contant").css("height", tbheight - 52);

        var demoIframe;
        var setting = {
            view: {
                addHoverDom: function (treeId, treeNode) {//鼠标移入事件
                },
                removeHoverDom: function (treeId, treeNode) {//鼠标移出事件
                }
            },
            data: {
                simpleData: {
                    enable: true
                }
            }, callback: {
                beforeClick: function (treeId, treeNode) {
                    $(".folder_btn").remove();
                    var $tView = $("#" + treeNode.tId + "_span");
                    var $btns = '<span class="folder_btn">';
                    if (treeNode.id == 0) {
                        $btns += '<a href="javascript:void(0);" onclick="createFolder();" title="创建目录"><i class="layui-icon" style="color:#1E9FFF;">&#xe654;</i></a>';
                    } else {
                        $btns += '<a href="javascript:void(0);" onclick="updateFolder(\'' + treeNode.id + '\');" title="编辑目录"><i class="layui-icon" style="color:#009688;">&#xe642;</i></a>';
                        $btns += '<a href="javascript:void(0);" onclick="deleteFolder(\'' + treeNode.id + '\');" title="删除目录"><i class="layui-icon" style="color:#FF5722;">&#xe640;</i></a>';
                        $btns += '<a href="javascript:void(0);" onclick="uploadFile(\'' + treeNode.id + '\');" title="上传文件"><i class="layui-icon" style="color:#009688;">&#xe681;</i></a>';
                    }
                    $btns += '</span>';
                    $tView.after($btns);

                    folderId = treeNode.id;
                    //加载图片文件
                    loadData(folderId, 1, PAGE_SIZE);
                }
            }
        };
        var zNodes = [
            <c:forEach items="${folderList}" var="folder">
            {id:${folder.id}, pId: '${folder.fid==null?0:folder.fid}', name: "${folder.name}", open: true, icon: ""},
            </c:forEach>
            {id: 0, pId: -1, name: "系统图片", open: true, icon: ""}
        ];
        zTree = $.fn.zTree.init($("#tree_div"), setting, zNodes);

        //选中第一项
        var node = zTree.getNodeByParam("id", 0);
        zTree.selectNode(node);

        $(document).contextMenu('allMenu', {
            bindings: {
                'createFolder': function (t) {
                    createFolder();
                },
                'createFile': function (t) {
                    uploadFile(folderId);
                },
                'refresh': function (t) {
                    refreshImageList();
                },
                'help': function (t) {
                    sysHelp();
                }
            }
        });

        $("#tree_div li").contextMenu('folderMenu', {
            bindings: {
                'rename': function (t) {
                    //获取首个span的id属性
                    var idStr = $(t).find("span").first().attr("id");
                    var id_tree = idStr.match(/tree_div_\d/);//获取ID字符串
                    var obj = zTree.getNodeByTId(id_tree + "");
                    var node = zTree.getNodeByParam("id", obj.id);
                    zTree.selectNode(node);
                    updateFolder(obj.id);
                },
                'delete': function (t) {
                    //获取首个span的id属性
                    var idStr = $(t).find("span").first().attr("id");
                    var id_tree = idStr.match(/tree_div_\d/);//获取ID字符串
                    var obj = zTree.getNodeByTId(id_tree + "");
                    deleteFolder(obj.id);
                }
            }
        });

        layui.use(['laypage', 'layer', 'element'], function () {
            laypage = layui.laypage;
            layer = layui.layer;


            loadData(0, 1, PAGE_SIZE);//加载初始数据
        });


    });

    //加载数据
    function loadData(folderId, pageNo, pageSize) {
        if (folderId == 0) folderId = "";
        lay_index = layer.load(1, {
            shade: [0.1, '#fff'] //0.1透明度的白色背景
        });
        $.ajax({
            type: 'post',
            url: 'list',
            data: {folderId: folderId, pageNo: pageNo, pageSize: pageSize},
            dataType: "json",
            cache: false,
            success: function (data) {
                layer.close(lay_index);
                var node = zTree.getNodeByParam("id", parseInt(folderId));
                zTree.selectNode(node);

                if (data.code == 200) {
                    serverDatas = data.result.results;
                    selImgs = new Array();
                    createDataListView(serverDatas);
                    //单击事件
                    $(".imglist li").click(function () {
                        var index = $(this).attr("img-index");
                        for (var i = 0; i < selImgs.length; i++) {
                            if (selImgs[i].id == serverDatas[index].id) {//如果被选中，移除选中状态
                                $(this).find("img").removeClass("img-select");
                                selImgs.splice(i, 1);
                                return;
                            }
                        }
                        $(this).find("img").addClass("img-select");
                        selImgs[selImgs.length] = new selImg(serverDatas[index].id, serverDatas[index].name, serverDatas[index].path);
                    });
                    //双击事件
                    $(".imglist li").dblclick(function () {
                        var index = $(this).attr("img-index");
                        layer.open({
                            type: 1,
                            skin: 'layui-layer-rim', //加上边框
                            area: ['80%', '100%'], //宽高
                            content: '<img src="' + serverDatas[index].path + '"/>'
                        });
                    });

                    laypage.render({
                        elem: 'pageDiv'
                        , count: data.result.total //数据总数，从服务端得到
                        , curr: data.result.currentPage
                        , limit: PAGE_SIZE
                        , jump: function (obj, first) {
                            //首次不执行
                            if (!first) {
                                loadData(folderId, obj.curr, PAGE_SIZE);
                            }
                        }
                    });

                    $(".imglist li").contextMenu('fileMenu', {
                        bindings: {
                            'rename': function (t) {
                                var index = $(t).attr("img-index");
                                updateImage(serverDatas[index].id);
                            },
                            'geturl': function (t) {
                                var index = $(t).attr("img-index");
                                getUrls(index, serverDatas[index].path);
                            },
                            'details': function (t) {
                                var index = $(t).attr("img-index");
                                img_details(index);
                            },
                            'download': function (t) {
                                var index = $(t).attr("img-index");
                                downImage(index);
                            },
                            'img-delete': function (t) {
                                var index = $(t).attr("img-index");
                                deleteImage(serverDatas[index].id);
                            },
                            'imgs-delete': function (t) {
                                deleteImages();
                            }
                        }
                    });
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

    //创建数据视图
    function createDataListView(datas) {
        $str = "";
        for (var i = 0; i < datas.length; i++) {
            $str += '<li title="' + datas[i].name + '(双击查看大图)" img-index="' + i + '">';
            $str += '<div style="width:95px;height:95px;"><img src="' + datas[i].thumbPath + '"></div>';
            $str += '<div class="txt">' + datas[i].name + '</div>';
            $str += '<div class="txt">' + datas[i].size + '</div>';
            $str += '<div class="btns">';
            $str += '<a href="javascript:void(0);" onclick="updateImage(' + datas[i].id + ');" title="重命名"><i class="layui-icon" style="color:#009688;">&#xe642;</i></a>';
            $str += '<a href="javascript:void(0);" onclick="getUrls(' + i + ');" title="获取链接"><i class="layui-icon" style="color:#1E9FFF;">&#xe64c;</i></a>';
            $str += '<a href="javascript:void(0);" onclick="img_details(' + i + ');" title="图片属性"><i class="layui-icon" style="color:#1E9FFF;">&#xe60a;</i></a>';
            $str += '<a href="javascript:void(0);" onclick="downImage(' + i + ');" title="下载图片"><i class="layui-icon" style="color:#1E9FFF;">&#xe61e;</i></a>';
            $str += '<a href="javascript:void(0);" onclick="deleteImage(' + datas[i].id + ');" title="删除"><i class="layui-icon" style="color:#FF5722;">&#xe640;</i></a>';
            $str += '</div></li>';
        }
        $("#img-listview").html($str);
    }

    //创建目录，打开目录添加页面
    function createFolder() {
        lay_index = layer.open({
            type: 2,
            title: '新增目录',
            skin: 'layui-layer-rim', //加上边框
            area: ['550px', '450px'], //宽高
            content: '../sysFolder/add'
        });
    }

    //修改目录
    function updateFolder(id) {
        if (id == 0) {
            layer.alert("此目录不能重命名!", {icon: 2});
            return;
        }
        lay_index = layer.open({
            type: 2,
            title: '编辑目录',
            skin: 'layui-layer-rim', //加上边框
            area: ['550px', '450px'], //宽高
            content: '../sysFolder/edit?id=' + id
        });
    }

    //删除目录
    function deleteFolder(id) {
        if (id == 0) {
            layer.alert("此目录不能删除!", {icon: 2});
            return;
        }
        layer.confirm('您真的要删除这个目录吗？如果包含图片，图片会一起删除。', {
            icon: 3,//询问图标
            btn: ['确定', '取消'] //按钮
        }, function (index) {
            layer.close(index);//关闭弹层
            var ids = new Array();
            ids[ids.length] = id;

            deleteFolderData(ids);
        }, function (index) {
            layer.close(index);
        });
    }

    //真正的删除目录操作
    function deleteFolderData(ids) {
        lay_index = layer.load(1, {
            shade: [0.1, '#fff'] //0.1透明度的白色背景
        });
        $.ajax({
            type: 'post',
            url: '../sysFolder/delete',
            data: {ids: ids},
            dataType: "json",
            cache: false,
            success: function (data) {
                layer.close(lay_index);
                if (data.msg) layer.msg(data.msg);
                if (data.code == 200) {
                    window.location.reload();
                }
            },
            error: function (xhr, desc, err) {
                layer.msg("数据请求失败:" + desc);
                layer.close(lay_index);
            }
        });
    }

    //刷新图片列表
    function refreshImageList() {
        loadData(folderId, 1, PAGE_SIZE);
    }

    //使用帮助
    function sysHelp() {
        layer.msg("孤立无援！");
    }

    //上传文件
    function uploadFile(folderId) {
        lay_index = layer.open({
            type: 2,
            title: '上传文件',
            skin: 'layui-layer-rim', //加上边框
            area: ['100%', '100%'], //宽高
            cancel: function () {
                refreshImageList();
                $(".folder_btn").remove();
                return true;
            }
            , content: 'add?folderId=' + folderId
        });
    }

    //更新folderID
    function updateFolderId(fId) {
        folderId = fId;
    }

    //修改文件名
    function updateImage(id) {
        lay_index = layer.open({
            type: 2,
            title: '编辑文件',
            skin: 'layui-layer-rim', //加上边框
            area: ['550px', '450px'], //宽高
            content: 'edit?id=' + id
        });
    }

    //显示链接
    function getUrls(index, path) {
        if (path) {
            $(".url-path").html(path);
        } else {
            $(".url-path").html(serverDatas[index].path);
        }

        layer.open({
            type: 1,
            title: '获取链接',
            skin: 'layui-layer-rim', //加上边框
            area: ['550px', '450px'], //宽高
            content: $("#view_urls").html()
        });
    }

    //选中图片的详情页面
    function img_details(index) {
        $("#image-name").html(serverDatas[index].name);
        $("#image-size").html(serverDatas[index].size);
        $("#image-type").html(serverDatas[index].type);
        $("#image-folder").html(serverDatas[index].folderName);
        $("#image-user").html(serverDatas[index].userName + "(" + serverDatas[index].userUserName + ")");
        $("#image-time").html(serverDatas[index].createTime);

        layer.open({
            type: 1,
            title: '图片详情',
            skin: 'layui-layer-rim', //加上边框
            area: ['550px', '450px'], //宽高
            content: $("#view_images").html()
        });

    }

    //下载文件
    function downImage(index) {
        window.open("download?id=" + serverDatas[index].id);
    }

    //删除选中图片
    function deleteImages() {
        if (!selImgs) {
            layer.alert("请选择要删除的图片!", {icon: 2});
            return;
        }
        var $str = '';
        for (var i = 0; i < selImgs.length; i++) {
            $str += selImgs[i].name + " ,";
        }
        if ($str != '') {
            $str = $str.substring(0, $str.length - 1);
        }
        layer.confirm('您真的要删除这些图片吗？图片列表：【' + $str + '】', {
            icon: 3,//询问图标
            btn: ['确定', '取消'] //按钮
        }, function (index) {
            layer.close(index);//关闭弹层
            var ids = new Array();
            for (var i = 0; i < selImgs.length; i++) {
                ids[ids.length] = selImgs[i].id;
            }

            deleteImageData(ids);
        }, function (index) {
            layer.close(index);
        });
    }

    //删除图片
    function deleteImage(id) {
        if (!id) {
            layer.alert("请选择要删除的图片!", {icon: 2});
            return;
        }
        layer.confirm('您真的要删除这张图片吗？', {
            icon: 3,//询问图标
            btn: ['确定', '取消'] //按钮
        }, function (index) {
            layer.close(index);//关闭弹层
            var ids = new Array();
            ids[ids.length] = id;

            deleteImageData(ids);
        }, function (index) {
            layer.close(index);
        });
    }

    //真正的删除目录操作
    function deleteImageData(ids) {
        lay_index = layer.load(1, {
            shade: [0.1, '#fff'] //0.1透明度的白色背景
        });
        $.ajax({
            type: 'post',
            url: 'delete',
            data: {ids: ids},
            dataType: "json",
            cache: false,
            success: function (data) {
                layer.close(lay_index);
                if (data.msg) layer.msg(data.msg);
                if (data.code == 200) {
                    refreshImageList();
                }
            },
            error: function (xhr, desc, err) {
                layer.msg("数据请求失败:" + desc);
                layer.close(lay_index);
            }
        });
    }

    //刷新页面
    function refreshPage(msg) {
        layer.close(lay_index);
        if (msg) layer.msg(msg);
        window.location.reload();
    }

    //刷新页面-文件
    function refreshPage_image(msg) {
        layer.close(lay_index);
        if (msg) layer.msg(msg);
        refreshImageList();
    }
</script>
</body>
</html>
