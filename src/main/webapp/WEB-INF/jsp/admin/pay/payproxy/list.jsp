<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="adminPermission" uri="http://adminpermission.city.com" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>代理管理</title>
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
            <h3><b>&nbsp;代理管理</b></h3>
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
        <legend class="admin-search-title">代理查询</legend>
        <form id="searchForm" class="layui-form">
            <div class="layui-row admin-search-row">
                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                    <input type="text" name="username" id="username" placeholder="代理帐号" class="layui-input"/>
                </div>
                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                    <input type="text" name="name" id="name" placeholder="代理名" class="layui-input"/>
                </div>
                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                    <select name="status">
                        <option value="">代理状态</option>
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
        <adminPermission:hasPermission permission="admin:payproxy:add">
            <button class="layui-btn layui-btn-sm layui-btn-normal" onclick="add();">
                添加
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

    <script type="text/html" id="tpl_opera">
        <adminPermission:hasPermission permission="admin:payproxy:status">
            {{# if(d.status && d.status=="1"){ }}
            <a class="layui-btn layui-btn-xs layui-btn-danger" onclick="upStatus({{d.id}},'0');">禁用</a>
            {{# }else{ }}
            <a class="layui-btn layui-btn-xs layui-btn-normal" onclick="upStatus({{d.id}},'1');">启用</a>
            {{# } }}

        </adminPermission:hasPermission>

        <adminPermission:hasPermission permission="admin:payproxy:edit">
            <a class="layui-btn layui-btn-xs layui-btn-normal" lay-event="edit">编辑</a>
        </adminPermission:hasPermission>

        <adminPermission:hasPermission permission="admin:payproxy:dlmm">
            <a class="layui-btn layui-btn-xs layui-btn-warm" lay-event="dlmm">登密</a>
        </adminPermission:hasPermission>
        <adminPermission:hasPermission permission="admin:payproxy:zfmm">
            <a class="layui-btn layui-btn-xs layui-btn-warm" lay-event="zfmm">支密</a>
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
                    {field: 'id', title: '序号', width: 70, type: 'checkbox'}
                    , {field: 'username', title: '账号', width: 150}
                    , {field: 'name', title: '代理名', width: 150}
                    , {field: 'money', title: '余额', width: 130, sort: true}
                    , {field: 'frozenMoney', title: '冻结金额', width: 130, sort: true}
                    , {field: 'cashCommission', title: '提现手续费', width: 130}
                    , {field: 'createTime', title: '创建时间', width: 180, sort: true}
                    , {field: 'status', title: '状态', width: 80, sort: true, templet: '#tpl_status'}

                    , {fixed: 'right', align: 'center', title: '操作', width: 220, toolbar: '#tpl_opera'}
                ]]
            });

            table.on('tool(table)', function (obj) {//tool event
                var data = obj.data; //获得当前行数据
                var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
                var tr = obj.tr; //获得当前行 tr 的DOM对象

                if (layEvent === 'dlmm') {
                    dlmm(data.id, data.name, data.username);
                } else if (layEvent === 'zfmm') {
                    zfmm(data.id, data.name, data.username);
                } else if (layEvent === 'edit') {
                    edit(data.id);
                }
            });

            table.on('sort(table)', function (obj) {
                if (!obj.sorts) return;
                createSortView(obj.sorts);
                searchData();
            });
        });
    });

    //更新状态
    function upStatus(id, status) {
        var msg = "禁用";
        if (status == '1') {
            msg = "启用";
        }
        layer.confirm('是否' + msg + '此代理？', {
            icon: 3,//询问图标
            btn: ['确定', '取消'] //按钮
        }, function (index) {
            layer.close(index);//关闭弹层

            commReq('upStatus', {id: id, status: status}, function () {
                searchData();
            });
        }, function (index) {
            layer.close(index);
        });
    }

    function dlmm(id, name, username) {
        layer.confirm('是否重置代理[ ' + name + ' ]的登录密码？', {
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
        layer.confirm('是否重置代理[ ' + name + ' ]的登录密码？', {
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
            title: '新增代理',
            skin: 'layui-layer-rim', //加上边框
            area: ['60%', '80%'], //宽高
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
            title: '编辑代理管理',
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

    //刷新页面
    function refreshPage(msg) {
        layer.close(lay_index);
        searchData();

        var data = JSON.parse(msg);
        //if(data.msg) layer.msg(msg);


        layer.confirm('成功添加代理<br/>账号：' + data.username + '<br/>密码：' + data.pwd + '<br/>密码仅显示一次，建议登录后修改密码！', {
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
</script>
</body>
</html>
