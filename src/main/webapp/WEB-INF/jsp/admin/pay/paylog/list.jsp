<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="adminPermission" uri="http://adminpermission.city.com" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>系统日志管理</title>
    <link rel="stylesheet" href="${context}/layui/css/layui.css">
    <link rel="stylesheet" href="${context}/css/style.css?${s_vs}">
    <!--[if lt IE 9]>
      <script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
      <script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<div class="layui-fluid admin-head-title">
    <div class="layui-row left">
        <div class="layui-col-xs11 layui-col-sm11 layui-col-md11">
            <h3><b>&nbsp;系统日志管理</b></h3>
        </div>
        <div class="layui-col-xs1 layui-col-sm1 layui-col-md1 right">
            <button class="layui-btn layui-btn-sm layui-btn-normal" title="刷新页面" onclick="window.location.reload();">
                <i class="layui-icon">&#x1002;</i>
            </button>
        </div>
    </div>
</div>
<div class="admin-main">
    <fieldset class="layui-elem-field admin-search-form">
        <legend class="admin-search-title">系统日志查询</legend>
        <form id="searchForm" class="layui-form">
            <div class="layui-row admin-search-row">

                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                    <select name="type">
                        <option value="">日志类型</option>
                        <option value="1">支付订单请求</option>
                        <option value="2">支付订单上游回调</option>
                        <option value="3">支付订单通知下游</option>
                        <option value="4">上游API提现请求</option>
                        <option value="5">API提现上游回调</option>
                        <option value="6">提现通知下游</option>
                        <option value="7">商户API提现请求</option>
                        <option value="8">登录</option>
                        <option value="9">系统操作</option>
                        <option value="10">登录失败</option>
                    </select>
                </div>

                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                    <select name="snType">
                        <option value="">订单类型</option>
                        <option value="0">支付订单</option>
                        <option value="1">提现</option>
                        <option value="2">系统操作</option>
                    </select>
                </div>

                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                    <input type="text" name="sn" id="sn" placeholder="订单号" class="layui-input"/>
                </div>


                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2" style="text-align:center;">
                    <button class="layui-btn layui-btn-normal" type="button" onclick="searchData();">搜索</button>
                    <button class="layui-btn" type="button" onclick="resetForm();">重置</button>
                </div>

            </div>
        </form>
    </fieldset>

    <div class="admin-btns">

        <adminPermission:hasPermission permission="admin:paylog:delete">
            <button class="layui-btn layui-btn-sm layui-btn-danger" onclick="del();">
                清空日志
            </button>
        </adminPermission:hasPermission>

        <span class="order-tag" id="order_tag">

		</span>
    </div>
    <div style="clear: both;"></div>
    <table id="datatable" lay-filter="table"></table>

    <script type="text/html" id="tpl_status">
        {{# if(d.status && d.status=="1"){ }}
        <span class="layui-badge layui-bg-blue">启用</span>
        {{# }else{ }}
        <span class="layui-badge">禁用</span>
        {{# } }}
    </script>

    <script type="text/html" id="tpl_type">
        {{# if(d.type==1){ }}
        支付订单请求
        {{# }else if(d.type==2){ }}
        支付订单上游回调
        {{# }else if(d.type==3){ }}
        支付订单通知下游
        {{# }else if(d.type==4){ }}
        上游API提现请求
        {{# }else if(d.type==5){ }}
        API提现上游回调
        {{# }else if(d.type==6){ }}
        提现通知下游
        {{# }else if(d.type==7){ }}
        商户API提现请求
        {{# }else if(d.type==8){ }}
        登录
        {{# }else if(d.type==9){ }}
        系统操作
        {{# }else if(d.type==10){ }}
        登录失败
        {{# } }}
    </script>

    <script type="text/html" id="tpl_sntype">
        {{# if(d.snType==0){ }}
        支付订单
        {{# }else if(d.snType==1){ }}
        提现
        {{# }else if(d.snType==2){ }}
        系统操作
        {{# } }}
    </script>

    <script type="text/html" id="tpl_opera">
        <adminPermission:hasPermission permission="admin:paylog:list">
            <a class="layui-btn layui-btn-xs layui-btn-normal" lay-event="detail">详情</a>
        </adminPermission:hasPermission>
    </script>

    <script type="text/html" id="tpl_index">
        {{ d.LAY_INDEX }}
    </script>

</div>
<script src="${context}/js/jquery.min.js"></script>
<script src="${context}/layui/layui.js?${s_vs}"></script>
<script src="${context}/js/common.js?${s_vs}"></script>
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
                , where: getFormJsonData("#searchForm")
                , request: {
                    pageName: 'pageNo' //页码的参数名称，默认：page
                    , limitName: 'pageSize' //每页数据量的参数名，默认：limit
                    , sorts: 'orders' //排序参数名，默认:sorts
                }
                , cols: [[ //表头
                    {field: 'id', title: '序号', width: 70, templet: '#tpl_index'}
                    , {field: 'createTime', title: '创建时间', width: 150, sort: true}
                    , {field: 'type', title: '日志类型', width: 120, sort: true, templet: '#tpl_type'}
                    , {field: 'descript', title: '描述', width: 200}
                    , {field: 'userName', title: '操作人', width: 100}
                    , {field: 'sn', title: '关联订单', width: 180}
                    , {field: 'snType', title: '订单类型', width: 100, templet: '#tpl_sntype'}

                    , {fixed: 'right', align: 'center', title: '操作', toolbar: '#tpl_opera', width: 100}
                ]]
            });

            table.on('tool(table)', function (obj) {//tool event
                var data = obj.data; //获得当前行数据
                var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
                var tr = obj.tr; //获得当前行 tr 的DOM对象

                if (layEvent === 'detail') {
                    detail(data.id);
                }
            });

            table.on('sort(table)', function (obj) {
                if (!obj.sorts) return;
                createSortView(obj.sorts);
                searchData();
            });

            initSelectAll();
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

    function detail(id) {
        lay_index = layer.open({
            type: 2,
            title: '日志详情',
            skin: 'layui-layer-rim', //加上边框
            area: ['80%', '80%'], //宽高
            content: 'detail?id=' + id
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
        $('#searchForm')[0].reset();
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

    function del() {
        layer.confirm('您真的要清空日志数据吗？', {
            icon: 3,//询问图标
            btn: ['确定', '取消'] //按钮
        }, function (index) {
            layer.close(index);//关闭弹层

            delData();
        }, function (index) {
            layer.close(index);
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
            data: {},
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
</script>
</body>
</html>
