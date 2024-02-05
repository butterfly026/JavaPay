<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="adminPermission" uri="http://adminpermission.city.com" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>上游通道管理</title>
    <link rel="stylesheet" href="${context}/layui/css/layui.css">
    <link rel="stylesheet" href="${context}/layui/css/dropdown.css">
    <link rel="stylesheet" href="${context}/css/style.css?${s_vs}">
    <style>
        .layui-icon-triangle-d:before {
            content: "\e625"
        }
    </style>
    <!--[if lt IE 9]>
    <script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
    <script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

</head>
<body>
<div class="layui-fluid admin-head-title">
    <div class="layui-row left">
        <div class="layui-col-xs11 layui-col-sm11 layui-col-md11">
            <h3><b>&nbsp;上游通道管理</b></h3>
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
        <legend class="admin-search-title">上游通道查询</legend>
        <form id="searchForm" class="layui-form">
            <div class="layui-row admin-search-row">
                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                    <select name="clientId" lay-search>
                        <option value="">选择上游</option>

                        <c:forEach items="${clientList }" var="c">
                            <option value="${c.id}">${c.username}-${c.name}</option>
                        </c:forEach>

                    </select>
                </div>

                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                    <input type="text" name="name" id="name" placeholder="通道名" class="layui-input"/>
                </div>

                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                    <select name="channelTypeId" lay-search>
                        <option value="">支付方式</option>

                        <c:forEach items="${channelTypeList }" var="c">
                            <option value="${c.id}">${c.name}</option>
                        </c:forEach>

                    </select>
                </div>

                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                    <select name="mtype">
                        <option value="">模式</option>

                        <option value="0">最大最小金额</option>
                        <option value="1">固定金额</option>

                    </select>
                </div>

                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                    <select name="status">
                        <option value="">状态</option>

                        <option value="1">启用</option>
                        <option value="0">禁用</option>

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
        <adminPermission:hasPermission permission="admin:payclientchannel:add">
            <button class="layui-btn layui-btn-sm layui-btn-normal" onclick="add();">
                添加
            </button>
        </adminPermission:hasPermission>

        <adminPermission:hasPermission permission="admin:payclientchannel:status">
            <button class="layui-btn layui-btn-sm layui-btn-blue" onclick="batchStart();">
                批量启用
            </button>

            <button class="layui-btn layui-btn-sm layui-btn-danger" onclick="batchStop();">
                批量禁用
            </button>
        </adminPermission:hasPermission>

        <span class="order-tag" id="order_tag">

		</span>
    </div>
    <div style="clear: both;"></div>
    <table id="datatable" lay-filter="table"></table>

    <script type="text/html" id="tpl_primary_platform">
        {{# if(d.primaryPlatform=="0"){ }}
        通用
        {{# }else if(d.primaryPlatform=="1"){ }}
        苹果
        {{# }else if(d.primaryPlatform=="2"){ }}
        安卓
        {{# }else{ }}
        其它
        {{# } }}
    </script>

    <script type="text/html" id="tpl_alarm">
        {{# if(d.alarm && d.alarm=="1"){ }}
        <span class="layui-badge layui-bg-blue">开启</span>({{d.alarmNumber}}%~{{d.alarmNumberup}}%)
        {{# }else{ }}
        <span class="layui-badge">关闭</span>
        {{# } }}
    </script>

    <script type="text/html" id="tpl_mtype">
        {{# if(d.mtype==1){ }}
        固定金额
        {{# }else{ }}
        最大最小金额
        {{# } }}
    </script>

    <script type="text/html" id="tpl_gtype">
        {{# if(d.gtype==0){ }}
        API转发
        {{# }else if(d.gtype==1){ }}
        表单转发
        {{# } }}
    </script>

    <script type="text/html" id="tpl_opera">
        <adminPermission:hasPermission permission="admin:payclientchannel:status">
            {{# if(d.status && d.status==1){ }}
            <a class="layui-btn layui-btn-xs layui-btn-danger" onclick="upStatus({{d.id}},0);">禁用</a>
            {{# }else{ }}
            <a class="layui-btn layui-btn-xs layui-btn-normal" onclick="upStatus({{d.id}},1);">启用</a>
            {{# } }}
        </adminPermission:hasPermission>

        <button class="layui-btn layui-btn-xs" id="layuidropdown_{{d.id}}" lay-dropdown="{align:'right', menus:[

		<adminPermission:hasPermission permission="admin:payclientchannel:edit">
			{layIcon:'layui-icon layui-icon-edit',txt: '编辑',event:'edit'},
		</adminPermission:hasPermission>

		<adminPermission:hasPermission permission="admin:payclientchannel:edit">
		{{# if(d.alarm && d.alarm==1){ }}
			{layIcon:'layui-icon layui-icon-edit',txt: '关闭预警',event:'edit_gbyj'},
		{{# }else{ }}
			{layIcon:'layui-icon layui-icon-edit',txt: '开启预警',event:'edit_kqyj'},
		{{# } }}
		</adminPermission:hasPermission>
		<adminPermission:hasPermission permission="admin:payclientchannel:edit">
			{layIcon:'layui-icon layui-icon-edit',txt: '编辑预警设置',event:'edit_yjsz'},
		</adminPermission:hasPermission>

		<adminPermission:hasPermission permission="admin:payclientchannel:edit">
			{layIcon:'layui-icon layui-icon-edit',txt: '分配商户通道',event:'edit_merchant'},
		</adminPermission:hasPermission>

		<adminPermission:hasPermission permission="admin:payclientchannel:delete">
			{layIcon:'layui-icon layui-icon-delete',txt: '删除',event:'del'},
		</adminPermission:hasPermission>
	]}">
            <span>操作</span>
            <i class="layui-icon layui-icon-triangle-d"></i>
        </button>

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

    var dropdown;

    $(function () {
        layui.config({
            base: '${context}/layui/lay/modules/' //配置 layui-dropdown 组件基础目录
        }).extend({
            dropdown: 'dropdown'
        });
        var $tableheight = $(document).height() - $("#datatable").offset().top - 29;
        layui.use(['table', 'layer', 'dropdown'], function () {
            table = layui.table;
            dropdown = layui.dropdown;

            tableIns = table.render({
                id: 'id',//用于获取被选中的项
                elem: '#datatable'
                , size: 'sm'
                //,height: $tableheight
                , url: 'list' //数据接口
                , page: true //开启分页
                , limit: 90
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
                    , {field: 'name', title: '通道名', width: 120}
                    , {field: 'clientNo', title: '上游名', width: 120}
                    , {field: 'typeName', title: '支付平台', width: 120}
                    , {field: 'startTime', title: '开启时间', width: 100}
                    , {field: 'endTime', title: '关闭时间', width: 100}

                    //,{field:'status', title: '状态', width:80,templet:'#tpl_status'}

                    , {field: 'mtype', title: '模式', width: 120, templet: '#tpl_mtype'}
                    , {field: 'minMoney', title: '最小金额', width: 100, sort: true}
                    , {field: 'maxMoney', title: '最大金额', width: 100, sort: true}
                    , {field: 'money', title: '固定金额', width: 120}
                    , {field: 'primaryPlatform', title: '优先通道', width: 80, templet: '#tpl_primary_platform'}

                    <adminPermission:hasPermission permission="admin:payclientchannel:ratio">
                    , {field: 'ratio', title: '通道费率(%)', width: 120}
                    </adminPermission:hasPermission>

                    , {field: 'status', title: '预警', width: 80, sort: true, templet: '#tpl_alarm'}

                    , {field: 'jrcgl', title: '今日成功率', width: 120, sort: true}
                    , {field: 'dayMoney', title: '今日成交额', width: 120, sort: true}
                    , {field: 'jrcg', title: '今日成功', width: 120, sort: true}
                    , {field: 'jrdds', title: '今日订单数', width: 120, sort: true}


                    , {field: 'keyname', title: '上游模块', width: 120}

                    , {fixed: 'right', align: 'center', title: '操作', toolbar: '#tpl_opera', width: 140}
                ]]
                , done: function (res, curr, count) {
                    dropdown.suite();
                }
            });

            table.on('tool(table)', function (obj) {//tool event
                var data = obj.data; //获得当前行数据
                var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
                var tr = obj.tr; //获得当前行 tr 的DOM对象

                if (layEvent === 'edit') {
                    edit(data.id);
                } else if (layEvent === 'del') {
                    del(data.id);
                } else if (layEvent === 'disobj') {
                    upStatus(data.id, '0');
                } else if (layEvent === 'openobj') {
                    upStatus(data.id, '1');
                } else if (layEvent === 'edit_gbyj') {//关闭预警
                    gbyj(data.id);
                } else if (layEvent === 'edit_kqyj') {//开启预警
                    kqyj(data.id)
                } else if (layEvent === 'edit_yjsz') {//预警设置
                    yjsz(data.id);
                } else if (layEvent === 'edit_merchant') {//分配商户通道
                    allocateMerchantChannel(data.id, data.clientNo, data.name, data.ratio, data.typeName);
                }

            });

            table.on('sort(table)', function (obj) {
                if (!obj.sorts) return;
                createSortView(obj.sorts);
                searchData();
            });

            initSelectAll();
            initTableSelect();
        });
    });

    function kqyj(id) {
        commReq('yjStatus', {id: id, status: 1}, function () {
            searchData();
        });
    }

    function gbyj(id) {
        commReq('yjStatus', {id: id, status: 0}, function () {
            searchData();
        });
    }

    //预警设置
    function yjsz(id) {
        if (typeof id == 'undefined') {
            layer.msg("请选择需要编辑的数据");
            return;
        }
        lay_index = layer.open({
            type: 2,
            title: '预警设置',
            skin: 'layui-layer-rim', //加上边框
            area: ['60%', '60%'], //宽高
            content: 'editYj?id=' + id
        });
    }

    //更新状态
    function upStatus(id, status) {

        commReq('upStatus', {id: id, status: status}, function () {
            searchData();
        });
        /**
         var msg="禁用";
         if(status=='1'){
			msg="启用";
		}
         layer.confirm('是否'+msg+'此上游通道？', {
			icon: 3,//询问图标
			btn: ['确定','取消'] //按钮
		}, function(index){
			layer.close(index);//关闭弹层

			commReq('upStatus',{id:id,status:status},function(){
				searchData();
			});
		}, function(index){
			layer.close(index);
		});
         **/
    }


    function commReq(url, data, sucfunc) {
        lay_index = layer.load(1, {
            shade: [0.1, '#fff'] //0.1透明度的白色背景
        });
        $.ajax({
            type: 'post',
            url: url,
            data: data,
            dataType: "json",
            cache: false,
            success: function (data) {
                layer.close(lay_index);
                if (data.msg) layer.msg(data.msg);
                if (data.code == 200) {
                    if (sucfunc) sucfunc(data);
                }
            },
            error: function (xhr, desc, err) {
                layer.msg("数据请求失败:" + desc);
                layer.close(lay_index);
            }
        });
    }

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
    function add() {
        lay_index = layer.open({
            type: 2,
            title: '新增上游通道',
            skin: 'layui-layer-rim', //加上边框
            area: ['60%', '90%'], //宽高
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
            title: '编辑上游通道',
            skin: 'layui-layer-rim', //加上边框
            area: ['60%', '90%'], //宽高
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


    function batchStart() {
        var cs = table.checkStatus('id');
        if (cs.data.length > 0) {
            layer.confirm('您真的要启用选中的通道吗？', {
                icon: 3,//询问图标
                btn: ['确定', '取消'] //按钮
            }, function (index) {
                layer.close(index);//关闭弹层
                var ids = new Array();
                var data = cs.data;
                for (var i = 0; i < data.length; i++) {
                    ids.push(data[i].id);
                }

                updateChannelData(ids, 1);
            }, function (index) {
                layer.close(index);
            });

        } else {
            layer.msg("请选择要启用的通道！");
        }
    }

    function batchStop() {
        var cs = table.checkStatus('id');
        if (cs.data.length > 0) {
            layer.confirm('您真的要禁用选中的通道吗？', {
                icon: 3,//询问图标
                btn: ['确定', '取消'] //按钮
            }, function (index) {
                layer.close(index);//关闭弹层
                var ids = new Array();
                var data = cs.data;
                for (var i = 0; i < data.length; i++) {
                    ids.push(data[i].id);
                }

                updateChannelData(ids, 0);
            }, function (index) {
                layer.close(index);
            });

        } else {
            layer.msg("请选择要禁用的通道！");
        }
    }

    function updateChannelData(ids, sts) {
        lay_index = layer.load(1, {
            shade: [0.1, '#fff'] //0.1透明度的白色背景
        });
        $.ajax({
            type: 'post',
            url: 'updateChannelData',
            data: {ids: ids, status: sts},
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


    var clientId = -1;

    //分配上游通道
    function allocateMerchantChannel(id, clientNo, name, ratio, typeName) {
        if (typeof id == 'undefined') {
            layer.msg("请选择通道");
            return;
        }
        clientId = id;
        dlg_indx = layer.open({
            type: 2,
            title: clientNo + '分配收款/上游通道:' + name + "-" + typeName + " | 费率:" + ratio + "%",
            skin: 'layui-layer-rim', //加上边框
            area: ['80%', '60%'], //宽高
            content: 'merchantChannelList?id=' + id
        });
    }

    function selectMerchantChannel(role_ids) {
        lay_index = layer.load(1, {
            shade: [0.1, '#fff'] //0.1透明度的白色背景
        });

        $.ajax({
            type: 'post',
            url: 'addSaveMerchantChannel',
            //data:{id:merchantId,channelIds:role_ids},
            data: {id: clientId, data: role_ids},
            dataType: "json",
            cache: false,
            success: function (data) {
                layer.close(lay_index);
                if (data.msg) layer.msg(data.msg);
                if (data.code == 200) {
                    layer.close(dlg_indx);
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
