<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="adminPermission" uri="http://adminpermission.city.com" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>USDT下发</title>
    <link rel="stylesheet" href="${context}/layui/css/layui.css">
    <link rel="stylesheet" href="${context}/css/style.css">
    <!--[if lt IE 9]>
    <script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
    <script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<div class="layui-fluid admin-head-title">
    <div class="layui-row left">
        <div class="layui-col-xs11 layui-col-sm11 layui-col-md11">
            <h3><b>&nbsp;USDT下发</b></h3>
        </div>
        <div class="layui-col-xs1 layui-col-sm1 layui-col-md1 right">
            <button class="layui-btn layui-btn-sm layui-btn-normal" title="刷新页面" onclick="window.location.reload();">
                <i class="layui-icon">&#x1002;</i>
            </button>
        </div>
    </div>
</div>

<div class="admin-main">

    <fieldset class="layui-elem-field admin-search-form"><%----%>

        <%--   这里写说明     --%>
        <blockquote class="layui-elem-quote">
            可申请下发金额:{}元 可申请账户金额:{}元<br>
            请确认钱包地址已加入白名单,若未加入白名单,请联系商务客服<br>
            目前仅支持{}钱包地址,手续费...<br>
            ......<br>
        </blockquote>

        <%-- 这里是提交usdt下发的表单 --%>
        <form id="usdtCashForm" class="layui-form">

            <div class="layui-form-item">
                <label class="layui-form-label">链类型</label>
                <div class="layui-input-block">
                    <select name="interest" lay-filter="aihao">
                        <option value="TRC-20">TRC-20</option>
                        <%-- <option value=""></option>--%>
                    </select>
                </div>
            </div>

            <div class="layui-form-item">
                <label class="layui-form-label">钱包地址</label>
                <div class="layui-input-block">
                    <input type="text" name="username" lay-verify="required" lay-reqtext="钱包地址没有填写" autocomplete="off"
                           class="layui-input">
                </div>
            </div>

            <div class="layui-form-item">
                <label class="layui-form-label">代付通道</label>
                <div class="layui-input-block">
                    <select name="interest" lay-filter="aihao">
                        <option value="USDT">USDT</option>
                        <%-- <option value=""></option>--%>
                    </select>
                </div>
            </div>

            <div class="layui-form-item">
                <label class="layui-form-label">代付金额</label>
                <div class="layui-input-block">
                    <input type="text" name="username" lay-verify="required" lay-reqtext="代付金额没有填写" autocomplete="off"
                           class="layui-input">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">USDT价格</label>
                <div class="layui-input-block">
                    <input type="text" name="username" lay-verify="required" lay-reqtext="用户名是必填项，岂能为空？"
                           placeholder="请输入" autocomplete="off" class="layui-input">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">USDT数量</label>
                <div class="layui-input-block">
                    <input type="text" name="username" lay-verify="required" lay-reqtext="用户名是必填项，岂能为空？"
                           placeholder="请输入" autocomplete="off" class="layui-input">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">支付密码</label>
                <div class="layui-input-block">
                    <input type="text" name="username" lay-verify="required" lay-reqtext="用户名是必填项，岂能为空？"
                           placeholder="请输入" autocomplete="off" class="layui-input">
                </div>
            </div>
            <%--<div class="layui-form-item">
                <label class="layui-form-label">开启状态</label>
                <div class="layui-input-block">
                    <input type="radio" name="sex" value="男" title="启动" checked="">
                    <input type="radio" name="sex" value="女" title="停止">
                </div>
            </div>
            <div class="layui-form-item">
                <div class="layui-inline">
                    <label class="layui-form-label">当前状态</label>
                </div>
                <div class="layui-inline">
                    <label class="layui-form-label">关闭</label>
                </div>
            </div>--%>


            <div class="layui-form-item">
                <div class="layui-input-block">
                    <%--权限--%>
                    <button type="submit" class="layui-btn" lay-submit="usdtCash()；" lay-filter="demo1">立即提交</button>
                    <%-- <button class="layui-btn layui-btn-sm layui-btn-normal" onclick="usdtCash();">   立即下发</button> --%>
                </div>
            </div>
        </form>

    </fieldset>

    <table id="datatable" lay-filter="table"></table>

    <%-- 表单的模板处理  --%>
    <script type="text/html" id="tpl_status">
        {{# if(d.status && d.status=="1"){ }}
        <span class="layui-badge layui-bg-blue">启用</span>
        {{# }else{ }}
        <span class="layui-badge">禁用</span>
        {{# } }}
    </script>

    <script type="text/html" id="tpl_opera">
        <adminPermission:hasPermission permission="admin:paychanneltype:edit">
            <a class="layui-btn layui-btn-xs layui-btn-normal" lay-event="edit">编辑</a>
        </adminPermission:hasPermission>
        <adminPermission:hasPermission permission="admin:paychanneltype:delete">
            <a class="layui-btn layui-btn-xs layui-btn-danger" lay-event="del">删除</a>
        </adminPermission:hasPermission>
    </script>

    <script type="text/html" id="tpl_index">
        {{ d.LAY_INDEX }}
    </script>

</div>
<script src="${context}/js/jquery.min.js"></script>
<script src="${context}/layui/layui.js?1"></script>
<script src="${context}/js/common.js?8"></script>
<script type="text/javascript">
    var tableIns;
    var table;
    var lay_index;
    $(function () {
        var $tableheight = $(document).height() - $("#datatable").offset().top - 29;
        layui.use(['table', 'layer'], function () {
            table = layui.table;
            tableIns = table.render({
                id: 'id',//用于获取被选中的项
                elem: '#datatable'
                , size: 'sm'
                , height: $tableheight
                , url: 'list' //数据接口
                , page: true //开启分页
                , limit: 20
                , mutilsort: true
                , method: 'POST'
                // ,where:getFormJsonData("#searchForm")
                , request: {
                    pageName: 'pageNo' //页码的参数名称，默认：page
                    , limitName: 'pageSize' //每页数据量的参数名，默认：limit
                    , sorts: 'orders' //排序参数名，默认:sorts
                }
                , cols: [[ //表头
                    {field: 'id', title: '序号', width: 70, type: 'checkbox'}
                    , {field: 'xxx', title: '钱包地址', width: 120}
                    , {field: 'xxx', title: '代付金额', width: 120}
                    , {field: 'xxx', title: '手续费', width: 120}
                    , {field: 'xxx', title: 'USDT价格', width: 120}
                    , {field: 'xxx', title: 'USDT数量', width: 120}
                    , {field: 'xxx', title: '时间', width: 120}



                    //     ,{fixed: 'right', align:'center',title:'操作', toolbar: '#tpl_opera'}
                ]]
            });

            table.on('tool(table)', function (obj) {//tool event
                var data = obj.data; //获得当前行数据
                var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
                var tr = obj.tr; //获得当前行 tr 的DOM对象

                if (layEvent === 'edit') {
                    edit(data.id);
                } else if (layEvent === 'del') {
                    del(data.id);
                }
            });

            table.on('sort(table)', function (obj) {
                if (!obj.sorts) return;
                createSortView(obj.sorts);
                searchData();
            });

            initTableSelect();
        });
    });

    //移除排序
    function removeSortField(field) {
        createSortView(table.removeSortField(field));
        searchData();
    }

    //搜索数据
    function searchData() {
        tableIns.reload({
            where: getFormJsonData("#searchForm")
        });
    }


    //真正的删除事件
    function delData(ids) {
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
                    searchData();
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
        searchData();
    }

    //重置表单
    function resetForm() {
        $('#usdtCashForm')[0].reset();
    }


    //生成排序相关视图
    function createSortView(sorts) {
        var $str = '';
        for (var i = 0; i < sorts.length; i++) {
            $str += '<button class="layui-btn layui-btn-xs layui-btn-radius layui-btn-normal" title="单击移除此项排序" onclick="removeSortField(\'' + sorts[i].field + '\')" type="button">' + sorts[i].fieldName + '<span class="layui-badge layui-bg-gray">';
            if (sorts[i].type == "desc") {
                $str += '降序';
            } else {
                $str += '升序';
            }
            $str += '</span></button>';
        }
        $("#order_tag").html($str);
    }

</script>
</body>
</html>
