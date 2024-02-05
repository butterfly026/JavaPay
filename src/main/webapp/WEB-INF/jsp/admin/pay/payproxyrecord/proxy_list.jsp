<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="adminPermission" uri="http://adminpermission.city.com" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>流水明细-代理</title>
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
            <h3><b>&nbsp;流水明细-代理</b></h3>
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
        <legend class="admin-search-title">流水查询</legend>
        <form id="searchForm" class="layui-form">
            <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                <input type="text" name="orderSn" id="orderSn" placeholder="订单号" class="layui-input"/>
            </div>
            <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                <input type="text" name="startTime" id="startTime" placeholder="开始时间" class="layui-input"
                       autocomplete="off"/>
            </div>
            <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                <input type="text" name="endTime" id="endTime" placeholder="结束时间" class="layui-input"
                       autocomplete="off"/>
            </div>
            <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                <select name="status">
                    <option value="">记录类型</option>
                    <option value="0">提现手续费</option>
                    <option value="1">完成提现</option>
                    <option value="2">完成订单</option>
                    <option value="3">订单手续费</option>
                    <option value="4">完成充值</option>
                    <option value="5">充值手续费</option>
                    <option value="6">人工调账</option>
                </select>
            </div>

            <div class="layui-col-xs2 layui-col-sm2 layui-col-md2" style="text-align:center;">
                <button class="layui-btn layui-btn-normal" type="button" onclick="searchData();">搜索</button>
                <button class="layui-btn" type="button" onclick="resetForm();">重置</button>
            </div>
</div>
</form>
</fieldset>

<div class="admin-btns">


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

<script type="text/html" id="tpl_opera">
    <adminPermission:hasPermission permission="admin:payproxyrecord:edit">
        <a class="layui-btn layui-btn-xs layui-btn-normal" lay-event="edit">编辑</a>
    </adminPermission:hasPermission>
    <adminPermission:hasPermission permission="admin:payproxyrecord:delete">
        <a class="layui-btn layui-btn-xs layui-btn-danger" lay-event="del">删除</a>
    </adminPermission:hasPermission>
</script>

<script type="text/html" id="tpl_index">
    {{ d.LAY_INDEX }}
</script>

<script type="text/html" id="tpl_sn">

    <a style="color:#1E9FFF;">{{d.orderSn}}</a>

</script>

<script type="text/html" id="tpl_type">
    {{# if(d.type==0){ }}
    提现手续费
    {{# }else if(d.type==1){ }}
    完成提现
    {{# }else if(d.type==2){ }}
    完成订单
    {{# }else if(d.type==3){ }}
    订单手续费
    {{# }else if(d.type==4){ }}
    完成充值
    {{# }else if(d.type==5){ }}
    充值手续费
    {{# }else if(d.type==6){ }}
    人工调账
    {{# }else{ }}
    其他
    {{# } }}
</script>

<script type="text/html" id="tpl_reason">
    {{# if(d.reason==0){ }}
    后台提现
    {{# }else if(d.reason==1){ }}
    线下结算
    {{# }else if(d.reason==2){ }}
    错误补单
    {{# }else{ }}
    其他原因
    {{# } }}
</script>

<script type="text/html" id="tpl_money">
    {{# if(d.money && d.money>=0){ }}
    <span style="color:green;">{{d.money}}</span>
    {{# }else{ }}
    <span style="color:red;">{{d.money}}</span>
    {{# } }}
</script>

<script type="text/html" id="tpl_balance">
    {{# if(d.money && d.money>=0){ }}
    <span style="color:green;">{{d.balance}}</span>
    {{# }else{ }}
    <span style="color:red;">{{d.balance}}</span>
    {{# } }}
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
        layui.use(['table', 'layer', 'laydate'], function () {
            table = layui.table;

            var laydate = layui.laydate;

            laydate.render({
                elem: '#startTime' //指定元素
                , type: "datetime"
                , format: 'yyyy-MM-dd HH:mm:ss'
            });

            laydate.render({
                elem: '#endTime' //指定元素
                , type: "datetime"
                , format: 'yyyy-MM-dd HH:mm:ss'
            });

            tableIns = table.render({
                id: 'id',//用于获取被选中的项
                elem: '#datatable'
                , size: 'sm'
                , height: $tableheight
                , url: 'proxyList' //数据接口
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
                    {field: 'id', title: '序号', width: 70, type: 'checkbox'}
                    , {field: 'no', title: '流水号', width: 150}
                    , {field: 'createTime', title: '流水时间', width: 180}
                    , {field: 'proxyNo', title: '代理号', width: 120}
                    , {field: 'orderSn', title: '关联订单', width: 180, templet: '#tpl_sn'}
                    , {field: 'type', title: '记录类型', width: 120, templet: '#tpl_type'}
                    , {field: 'money', title: '变动金额', width: 120, templet: '#tpl_money'}
                    , {field: 'balance', title: '余额', width: 120, templet: '#tpl_balance'}
                    , {field: 'remark', title: '备注', width: 120}
                    , {field: 'reason', title: '调账原因', width: 120, templet: '#tpl_reason'}

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

    //新增数据
    function add_add() {
        lay_index = layer.open({
            type: 2,
            title: '增加代理余额',
            skin: 'layui-layer-rim', //加上边框
            area: ['80%', '80%'], //宽高
            content: 'add_add'
        });
    }

    //新增数据
    function add() {
        lay_index = layer.open({
            type: 2,
            title: '扣减代理余额',
            skin: 'layui-layer-rim', //加上边框
            area: ['80%', '80%'], //宽高
            content: 'add'
        });
    }

    //编辑数据
    function edit(id) {
        if (typeof id == 'undefined') {
            layer.msg("请选择需要编辑的数据");
            return;
        }
        lay_index = layer.open({
            type: 2,
            title: '编辑代理流水',
            skin: 'layui-layer-rim', //加上边框
            area: ['80%', '80%'], //宽高
            content: 'edit?id=' + id
        });
    }

    function del(id) {
        if (typeof id == 'undefined') {
            layer.msg("请选择需要删除的数据");
            return;
        }
        layer.confirm('您真的要删除这条数据吗？', {
            icon: 3,//询问图标
            btn: ['确定', '取消'] //按钮
        }, function (index) {
            layer.close(index);//关闭弹层
            var ids = new Array();
            ids[0] = id;

            delData(ids)
        }, function (index) {
            layer.close(index);
        });
    }

    //批量删除
    function batchDelete() {
        var cs = table.checkStatus('id');
        if (cs.data.length > 0) {
            layer.confirm('您真的要删除选中的数据吗？', {
                icon: 3,//询问图标
                btn: ['确定', '取消'] //按钮
            }, function (index) {
                layer.close(index);//关闭弹层
                var ids = new Array();
                var data = cs.data;
                for (var i = 0; i < data.length; i++) {
                    ids.push(data[i].id);
                }

                delData(ids);
            }, function (index) {
                layer.close(index);
            });

        } else {
            layer.msg("请选择要删除的数据！");
        }

        //console.log(cs.data); //获取选中行的数据
        //console.log(cs.data.length); //获取选中行数量，可作为是否有选中行的条件
        //console.log(cs.isAll); //表格是否全选
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
</script>
</body>
</html>
