<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="adminPermission" uri="http://adminpermission.city.com" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>商户管理管理</title>
    <link rel="stylesheet" href="${context}/layui/css/layui.css">
    <link rel="stylesheet" href="${context}/layui/css/dropdown.css">
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
            <h3><b>&nbsp;商户管理</b></h3>
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
        <legend class="admin-search-title">商户查询</legend>
        <form id="searchForm" class="layui-form">
            <div class="layui-row admin-search-row">
                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                    <select name="proxyId" lay-search>
                        <option value="">选择代理</option>

                        <c:forEach items="${proxyList }" var="c">
                            <option value="${c.id}">${c.username}-${c.name}</option>
                        </c:forEach>

                    </select>
                </div>

                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                    <select name="merchantId" lay-verify="required" lay-search>
                        <option value="">选择商户</option>
                        <c:forEach items="${merchantList }" var="c">
                            <option value="${c.id}"
                                    <c:if test="${result.merchantId==c.id}">selected</c:if> >${c.username}-${c.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                    <input type="text" name="name" id="name" placeholder="商户名" class="layui-input"/>
                </div>
                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                    <select name="status">
                        <option value="">商户状态</option>
                        <option value="1">启用</option>
                        <option value="0">禁用</option>
                    </select>
                </div>

                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                    <select name="jyl">
                        <option value="">交易量</option>
                        <option value="1">今日有量</option>
                        <option value="0">今日无量</option>
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
        <adminPermission:hasPermission permission="admin:paymerchant:add">
            <button class="layui-btn layui-btn-sm layui-btn-normal" onclick="add();">
                添加
            </button>

            <adminPermission:hasPermission permission="admin:paymerchant:export">
                <button class="layui-btn layui-btn-sm layui-btn-normal" onclick="exportExcel();">
                    导出Excel
                </button>

                <button class="layui-btn layui-btn-sm layui-btn-normal" onclick="showExportView();">
                    历史导出数据
                </button>
            </adminPermission:hasPermission>
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

    <script type="text/html" id="tpl_opera">

        <adminPermission:hasPermission permission="admin:paymerchant:status">
            {{# if(d.status && d.status=="1"){ }}
            <a class="layui-btn layui-btn-xs layui-btn-danger" onclick="upStatus({{d.id}},'0');">禁用</a>
            {{# }else{ }}
            <a class="layui-btn layui-btn-xs layui-btn-normal" onclick="upStatus({{d.id}},'1');">启用</a>
            {{# } }}

        </adminPermission:hasPermission>


        <button class="layui-btn layui-btn-xs" id="layuidropdown_{{d.id}}" lay-dropdown="{align:'right', menus:[

		<adminPermission:hasPermission permission="admin:paymerchant:edit">
			{layIcon:'layui-icon layui-icon-edit',txt: '编辑',event:'edit'},
		</adminPermission:hasPermission>

		<adminPermission:hasPermission permission="admin:paymerchant:dlmm">
			{layIcon:'layui-icon layui-icon-password',txt: '重置登录密码',event:'dlmm'},
		</adminPermission:hasPermission>
		<adminPermission:hasPermission permission="admin:paymerchant:zfmm">
			{layIcon:'layui-icon layui-icon-key',txt: '重置支付密码',event:'zfmm'},
		</adminPermission:hasPermission>
		<adminPermission:hasPermission permission="admin:paymerchant:zfmm">
			{layIcon:'layui-icon layui-icon-auz',txt: '重置后台IP白名单',event:'adminip'},
		</adminPermission:hasPermission>
		<adminPermission:hasPermission permission="admin:paymerchant:zfmm">
			{layIcon:'layui-icon layui-icon-auz',txt: '重置Api IP白名单',event:'apiip'},
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

    var lay_sort;

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

                    {field: 'id', title: '序号', width: 70, sort: true}
                    , {field: 'username', title: '账号', width: 120}
                    , {field: 'name', title: '商户名', width: 100}
                    , {field: 'proxyNo', title: '代理账号', width: 100}

                    //,{field:'status', title: '状态', width:80,sort:true,templet:'#tpl_status'}

                    , {field: 'money', title: '余额', width: 100, sort: true}
                    , {field: 'frozenMoney', title: '冻结金额', width: 100, sort: true}

                    , {field: 'dayMoney', title: '今日成交额', width: 120, sort: true}
                    , {field: 'jrcg', title: '今日成功', width: 120, sort: true}
                    , {field: 'jrdds', title: '今日订单数', width: 120, sort: true}
                    , {field: 'jrcgl', title: '今日成功率', width: 120, sort: true}
                    //,{field:'lscgl', title: '历史成功率', width:110,sort:true}
                    //,{field:'xdl', title: '下单率', width:100,sort:true}

                    , {field: 'cashConfigstr', title: '提现手续费', width: 130}
                    //,{field:'mcashCommission', title: '代理费率', width:130}

                    //,{field:'cashRatio', title: '提现费率(%)', width:130}

                    , {field: 'createTime', title: '创建时间', width: 180, sort: true}

                    <adminPermission:hasPermission permission="admin:paymerchant:my">
                    , {field: 'merchantMy', title: '商户秘钥', width: 150}
                    </adminPermission:hasPermission>

                    , {fixed: 'right', align: 'center', title: '操作', toolbar: '#tpl_opera', width: 150}
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
                } else if (layEvent === 'dlmm') {
                    dlmm(data.id, data.name, data.username);
                } else if (layEvent === 'zfmm') {
                    zfmm(data.id, data.name, data.username);
                } else if (layEvent === 'adminip') {
                    adminip(data.id);
                } else if (layEvent === 'apiip') {
                    apiip(data.id);
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

    function adminip(id) {
        layer.confirm('是否重置此商户管理后台IP白名单？', {
            icon: 3,//询问图标
            btn: ['确定', '取消'] //按钮
        }, function (index) {
            layer.close(index);//关闭弹层

            commReq('restoreAdminip', {id: id}, function () {
            });
        }, function (index) {
            layer.close(index);
        });

    }

    function apiip(id) {
        layer.confirm('是否重置此商户API接口IP白名单？', {
            icon: 3,//询问图标
            btn: ['确定', '取消'] //按钮
        }, function (index) {
            layer.close(index);//关闭弹层

            commReq('restoreApiip', {id: id}, function () {

            });
        }, function (index) {
            layer.close(index);
        });

    }

    //更新状态
    function upStatus(id, status) {
        /**
         var msg="禁用";
         if(status=='1'){
			msg="启用";
		}
         layer.confirm('是否'+msg+'此商户？', {
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
        commReq('upStatus', {id: id, status: status}, function () {
            searchData();
        });
    }

    function dlmm(id, name, username) {
        layer.confirm('是否重置商户[ ' + name + ' ]的登录密码？', {
            icon: 3,//询问图标
            btn: ['确定', '取消'] //按钮
        }, function (index) {
            layer.close(index);//关闭弹层

            commReq('dlmm', {id: id}, function (data) {
                layer.confirm('成功重置登录密码<br/>账号：' + username + '<br/>登录密码：' + data.result.pwd + '<br/>密码仅显示一次，建议登录后修改密码！', {
                    btn: ['好的'] //按钮
                });
            });
        }, function (index) {
            layer.close(index);
        });
    }

    function zfmm(id, name, username) {
        layer.confirm('是否重置商户[ ' + name + ' ]的支付密码？', {
            icon: 3,//询问图标
            btn: ['确定', '取消'] //按钮
        }, function (index) {
            layer.close(index);//关闭弹层

            commReq('zfmm', {id: id}, function (data) {
                layer.confirm('成功重置支付密码<br/>账号：' + username + '<br/>支付密码：' + data.result.pwd + '<br/>密码仅显示一次，建议登录后修改密码！', {
                    btn: ['好的'] //按钮
                });
            });
        }, function (index) {
            layer.close(index);
        });
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
            title: '新增商户',
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
            title: '编辑商户',
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
        searchData();

        var data = JSON.parse(msg);
        //if(data.msg) layer.msg(msg);


        layer.confirm('成功添加商户<br/>账号：' + data.username + '<br/>密码：' + data.pwd + '<br/>后台登录地址：http://${sysconfig.domain}/login/login<br/>API网关：http://${sysconfig.apidomain}<br/>下单地址：http://${sysconfig.apidomain}/api/pay/order<br/>回调IP：${sysconfig.nip}<br/>支付密码默认为空,请先登录商户后台首页设置支付密码,再点击‘商户私密数据’获取商户秘钥<br/>密码仅显示一次，建议登录后修改密码！', {
            btn: ['好的'] //按钮
        });
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

    var exportObj = [
        [ //表头
            {field: 'username', title: '商户号', width: 30, rowspan: 1, colspan: 1},
            {field: 'name', title: '商户名', width: 30, rowspan: 1, colspan: 1},
            {field: 'money', title: '余额(元)', width: 20, rowspan: 1, colspan: 1},
            {field: 'frozenMoney', title: '冻结金额(元)', width: 20, rowspan: 1, colspan: 1},
            {field: 'amount', title: '有效金额(元)', width: 20, rowspan: 1, colspan: 1}
        ]
    ];

    //导出excel
    function exportExcel() {
        //$("#searchForm").attr("action","exportExcel");
        //$("#searchForm").submit();
        lay_index = layer.load(1, {
            shade: [0.1, '#fff'] //0.1透明度的白色背景
        });
        var orders = new Array();
        if (lay_sort) {
            for (var i = 0; i < lay_sort.length; i++) {
                orders.push(lay_sort[i].field + " " + lay_sort[i].type);
            }
        }
        var param = getFormJsonData("#searchForm");
        param["excelCols"] = JSON.stringify(exportObj);
        param["orders"] = orders;

        //请求后台生成要下载的excel，再下载到本地
        $.ajax({
            type: 'post',
            url: 'exportExcel',
            data: param,
            dataType: "json",
            cache: false,
            success: function (data) {
                //console.log(data);
                layer.close(lay_index);
                if (data.msg) layer.msg(data.msg);
                if (data.code == 200) {
                    //var url=data.result;
                    //后续优化为按指定名称导出
                    //window.open(url);
                    showExportView();
                }
            },
            error: function (xhr, desc, err) {
                layer.msg("数据请求失败:" + desc);
                layer.close(lay_index);
            }
        });

    }

    function showExportView() {
        lay_index = layer.open({
            type: 2,
            title: '导出列表',
            skin: 'layui-layer-rim', //加上边框
            area: ['90%', '90%'], //宽高
            content: '../payExcelexport/view?source=2'
        });
    }
</script>
</body>
</html>
