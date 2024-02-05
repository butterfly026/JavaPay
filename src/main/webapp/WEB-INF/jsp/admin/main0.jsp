<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>${sysconfig.cname} V2.0</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <link rel="stylesheet" href="${context}/layuiadmin/layui/css/layui.css" media="all">
    <link rel="stylesheet" href="${context}/layuiadmin/style/admin.css" media="all">
    <script>
        var host = window.location.host;
        console.log(host)
        var hrefLink= '';
        if (host.includes('quanqiupays.com')){
            hrefLink =`${context}/layuiadmin/layui/css/side.css`;
        }else if(host.includes('18.162.213.217')) {
            hrefLink =`${context}/layuiadmin/layui/css/side1.css`;
        }else {
            hrefLink =`${context}/layuiadmin/layui/css/side1.css`;
        }
        var head = document.getElementsByTagName('head')[0];
        var linkTag= document.createElement("link");
        linkTag.rel = "stylesheet";
        linkTag.href = hrefLink;
        head.appendChild(linkTag);
    </script>
    <style>

    </style>
</head>
<body class="layui-layout-body">

<div id="LAY_app">
    <div class="layui-layout layui-layout-admin">
        <div class="layui-header">
            <!-- 头部区域 -->
            <ul class="layui-nav layui-layout-left">
                <li class="layui-nav-item layadmin-flexible" lay-unselect>
                    <a href="javascript:;" layadmin-event="flexible" title="侧边伸缩">
                        <i class="layui-icon layui-icon-shrink-right" id="LAY_app_flexible"></i>
                    </a>
                </li>

                <li class="layui-nav-item" lay-unselect>
                    <a href="javascript:;" layadmin-event="refresh" title="刷新">
                        <i class="layui-icon layui-icon-refresh-3"></i>
                    </a>
                </li>

            </ul>
            <ul class="layui-nav layui-layout-right" lay-filter="layadmin-layout-right">

                <!--
                <li class="layui-nav-item layui-hide-xs" lay-unselect>
                  <a href="javascript:;" layadmin-event="theme">
                    <i class="layui-icon layui-icon-theme"></i>
                  </a>
                </li>
                <li class="layui-nav-item layui-hide-xs" lay-unselect>
                  <a href="javascript:;" layadmin-event="note">
                    <i class="layui-icon layui-icon-note"></i>
                  </a>
                </li>
                 -->
                <li class="layui-nav-item layui-hide-xs" lay-unselect>
                    <a href="javascript:;" layadmin-event="fullscreen">
                        <i class="layui-icon layui-icon-screen-full"></i>
                    </a>
                </li>
                <li class="layui-nav-item" lay-unselect>
                    <a href="javascript:;">
                        <cite>${name}</cite>
                    </a>
                    <dl class="layui-nav-child">
                        <dd><a href="javascript:;" onclick="refreshUserMenu();">刷新菜单</a></dd>
                        <hr>
                        <dd style="text-align: center;"><a href="${context}/login/logout">退出</a></dd>
                    </dl>
                </li>


            </ul>
        </div>

        <!-- 侧边菜单 -->
        <div class="layui-side layui-side-menu" >
            <div class="layui-side-scroll">
                <div class="layui-logo" lay-href="${url}">
                    <img src="${context}${sysconfig.logo}"
                         style="width:25px;height:25px;"/>&nbsp;<span>${sysconfig.cname} V2.0</span>
                </div>

                <ul class="layui-nav layui-nav-tree" lay-shrink="all" id="LAY-system-side-menu"
                    lay-filter="layadmin-system-side-menu">

                </ul>
            </div>
        </div>

        <!-- 页面标签 -->
        <div class="layadmin-pagetabs" id="LAY_app_tabs">
            <div class="layui-icon layadmin-tabs-control layui-icon-prev" layadmin-event="leftPage"></div>
            <div class="layui-icon layadmin-tabs-control layui-icon-next" layadmin-event="rightPage"></div>
            <div class="layui-icon layadmin-tabs-control layui-icon-down">
                <ul class="layui-nav layadmin-tabs-select" lay-filter="layadmin-pagetabs-nav">
                    <li class="layui-nav-item" lay-unselect>
                        <a href="javascript:;"></a>
                        <dl class="layui-nav-child layui-anim-fadein">
                            <dd layadmin-event="closeThisTabs"><a href="javascript:;">关闭当前标签页</a></dd>
                            <dd layadmin-event="closeOtherTabs"><a href="javascript:;">关闭其它标签页</a></dd>
                            <dd layadmin-event="closeAllTabs"><a href="javascript:;">关闭全部标签页</a></dd>
                        </dl>
                    </li>
                </ul>
            </div>
            <div class="layui-tab" lay-unauto lay-allowClose="true" lay-filter="layadmin-layout-tabs">
                <ul class="layui-tab-title" id="LAY_app_tabsheader">
                    <li lay-id="${url}" lay-attr="${url}" class="layui-this"><i class="layui-icon layui-icon-home"></i>
                    </li>
                </ul>
            </div>
        </div>


        <!-- 主体内容 -->
        <div class="layui-body" id="LAY_app_body">
            <div class="layadmin-tabsbody-item layui-show">
                <iframe src="${url}" frameborder="0" class="layadmin-iframe"></iframe>
            </div>
        </div>

        <!-- 辅助元素，一般用于移动设备下遮罩 -->
        <div class="layadmin-body-shade" layadmin-event="shade"></div>
    </div>
</div>

<script type="text/javascript" src="${context}/js/jquery.min.js"></script>
<script src="${context}/layuiadmin/layui/layui.js"></script>
<script>
    var element;
    layui.config({
        base: '${context}/layuiadmin/' //静态资源所在路径
        , initColorIndex: 10
    }).extend({
        index: 'lib/index' //主入口模块
    }).use(['index', 'element'], function () {
        element = layui.element;
        refreshUserMenu();
    });


    function menuTree(id, pid, name, icon, url) {
        this.id = id,
            this.pid = pid,
            this.name = name,
            this.url = url,
            this.icon = icon
    }

    var trees = new Array();


    function createTable(vid, f) {
        var str = "";
        for (var i = 0; i < trees.length; i++) {
            if (trees[i].pid == vid) {
                var str1 = createTable(trees[i].id, f + 1);
                if (str1 != "") {
                    /**
                     str+='<dl id="menu-'+trees[i].id+'"><dt>';
                     if(!trees[i].icon||trees[i].icon==""){
						str+='<i class="Hui-iconfont">&#xe626;</i>';
					}else{
						str+='<img src="/'+trees[i].icon+'" width="16" height="16"/>';
					}
                     str+=trees[i].name+'<i class="Hui-iconfont menu_dropdown-arrow">&#xe6d5;</i></dt><dd><ul>';
                     str+=str1;
                     str+='</ul></dd></dl>';
                     if(f!=0){
						str="<li>"+str+"</li>";
					}
                     **/
                    if (f == 0) {
                        str += '<li data-name="menu_' + trees[i].id + '" class="layui-nav-item"> ' +
                            '<a href="javascript:;" lay-tips="' + trees[i].name + '" lay-direction="2"> ' +
                            '<i class="layui-icon layui-icon-auz"></i> ' +
                            '<cite>' + trees[i].name + '</cite> ' +
                            '</a><dl class="layui-nav-child"> ' +
                            str1 +
                            '</dl></li>';
                    } else {
                        str += '<dl class="layui-nav-child"><dd data-name="menu_' + trees[i].id + '"><a href="javascript:;">' + trees[i].name + '</a><dl class="layui-nav-child">' + str1 + '</dl></dd></dl>';
                    }

                } else {
                    if (f == 0) {
                        str += '<li data-name="menu_' + trees[i].id + '" class="layui-nav-item"> ' +
                            '    <a href="javascript:;" lay-href="' + (trees[i].url.indexOf("http") == 0 ? trees[i].url : "${context}/" + trees[i].url) + '" lay-tips="' + trees[i].name + '" lay-direction="2"> ' +
                            '  <i class="layui-icon layui-icon-auz"></i> ' +
                            '   <cite>' + trees[i].name + '</cite> ' +
                            ' </a> ' +
                            '</li>';
                    } else {
                        str += '<dd data-name="menu_' + trees[i].id + '"><a lay-href="' + (trees[i].url.indexOf("http") == 0 ? trees[i].url : "${context}/" + trees[i].url) + '">' + trees[i].name + '</a></dd>';
                    }


                    /**
                     str+='<li><a data-href="'+(trees[i].url.indexOf("http")==0?trees[i].url:"${context}/"+trees[i].url)+'" data-title="'+trees[i].name+'" href="javascript:void(0)">';
                     if(!trees[i].icon||trees[i].icon==""){
						str+='<i class="Hui-iconfont">&#xe626;</i>';
					}else{
						str+='<img src="'+trees[i].icon+'" width="16" height="16"/>';
					}
                     str+=trees[i].name+'</a></li>';
                     **/
                }
            }
        }
        return str;
    }

    /**
     * 刷新用户菜单
     */
    function refreshUserMenu() {
        $.ajax({
            url: "${context}/system/sysUser/getUserMenu",
            type: "POST",
            dataType: "json",
            cache: false,
            success: function (data) {
                trees = new Array();
                if (data.code == 200 && data.result) {
                    data = data.result;
                    for (var i = 0; i < data.length; i++) {
                        trees[i] = new menuTree(data[i].id, data[i].fid, data[i].name, data[i].image, data[i].url);
                    }
                    $(".layui-nav-tree").html(createTable(null, 0));
                    element.init();
                }
            },
            error: function (xhr, desc, err) {
                layer.msg("刷新菜单失败:" + desc);
            }
        });
    }
</script>
</body>
</html>
