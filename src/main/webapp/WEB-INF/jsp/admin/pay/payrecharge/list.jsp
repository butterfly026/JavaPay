<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="adminPermission" uri="http://adminpermission.city.com" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>商户充值管理</title>
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
            <h3><b>&nbsp;商户充值管理</b></h3>
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
        <legend class="admin-search-title">商户充值查询</legend>
        <form id="searchForm" class="layui-form">
            <div class="layui-row admin-search-row">
                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                    <input type="text" name="sn" id="sn" placeholder="系统订单号" class="layui-input"/>
                </div>

                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                    <input type="text" name="cardNo" id="cardNo" placeholder="帐号/卡号" class="layui-input"/>
                </div>

                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                    <select name="merchantId" id="merchantId" lay-search>
                        <option value="">选择商户</option>

                        <c:forEach items="${merchantList }" var="c">
                            <option value="${c.id}">${c.username}-${c.name}</option>
                        </c:forEach>

                    </select>
                </div>

            </div>

            <div class="layui-row admin-search-row">
                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                    <select name="accType" id="accType" lay-search>
                        <option value="">账号类型</option>

                        <option value="0">银行卡</option>
                        <option value="1">UPI</option>

                    </select>
                </div>

                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                    <select name="status" id="status" lay-search>
                        <option value="">订单状态</option>

                        <option value="0">发起充值</option>
                        <option value="1">拒绝充值</option>
                        <option value="2">待充值</option>
                        <option value="3">已充值</option>
                        <option value="4">成功</option>
                        <option value="5">失败</option>

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

    <!-- 订单审核 -->
    <div id="audit_dlg" style="display:none;">
        <form class="layui-form" style="width:90%;margin-top:10px;">
            <div class="layui-row" style="padding-bottom:5px;width:100%;text-align:center;color:red;" id="info_div">
            </div>
            <div class="layui-row" style="padding-bottom:5px;">
                <div class="layui-col-xs12 layui-col-sm12 layui-col-md12">
                    <label class="layui-form-label">备注</label>
                    <div class="layui-input-block">
                        <textarea name="remark" id="remark" placeholder="审核备注" class="layui-textarea"></textarea>
                    </div>
                </div>
            </div>
        </form>
    </div>

    <script type="text/html" id="tpl_status">
        {{# if(d.status==0){ }}
        发起充值
        {{# }else if(d.status==1){ }}
        <span class="layui-badge">已拒绝</span>
        {{# }else if(d.status==2){ }}
        <span class="layui-badge layui-bg-blue">待充值</span>
        {{# }else if(d.status==3){ }}
        <span class="layui-badge layui-bg-orange">已充值</span>
        {{# }else if(d.status==4){ }}
        <span class="layui-badge layui-bg-green">成功</span>
        {{# }else{ }}
        <span class="layui-badge">失败</span>
        {{# } }}
    </script>

    <script type="text/html" id="tpl_acctype">
        {{# if(d.accType==1){ }}
        <span class="layui-badge layui-bg-blue">UPI账号</span>
        {{# }else{ }}
        <span class="layui-badge layui-bg-green">银行卡</span>
        {{# } }}
    </script>

    <script type="text/html" id="tpl_opera">

        {{# if(d.status==0){ }}
        <adminPermission:hasPermission permission="admin:payrecharge:fpzh">
            <a class="layui-btn layui-btn-xs layui-btn-normal" lay-event="shtg">通过</a>
        </adminPermission:hasPermission>
        <adminPermission:hasPermission permission="admin:payrecharge:jj">
            <a class="layui-btn layui-btn-xs layui-btn-danger" lay-event="shjj">拒绝</a>
        </adminPermission:hasPermission>
        {{# } }}

        {{# if(d.status==2){ }}
        <adminPermission:hasPermission permission="admin:payrecharge:fpzh">
            <a class="layui-btn layui-btn-xs layui-btn-green" lay-event="shtg">修改账号</a>
        </adminPermission:hasPermission>
        {{# } }}

        {{# if(d.status==3){ }}
        <adminPermission:hasPermission permission="admin:payrecharge:cg">
            <a class="layui-btn layui-btn-xs layui-btn-green" lay-event="suc">成功</a>
        </adminPermission:hasPermission>
        {{# } }}

        {{# if(d.status==3 || d.status==2){ }}
        <adminPermission:hasPermission permission="admin:payrecharge:sb">
            <a class="layui-btn layui-btn-xs layui-btn-danger" lay-event="fail">失败</a>
        </adminPermission:hasPermission>
        {{# } }}

        <adminPermission:hasPermission permission="admin:payrecharge:detail">
            <a class="layui-btn layui-btn-xs layui-btn-normal" lay-event="detail">详情</a>
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
                , where: getFormJsonData("#searchForm")
                , request: {
                    pageName: 'pageNo' //页码的参数名称，默认：page
                    , limitName: 'pageSize' //每页数据量的参数名，默认：limit
                    , sorts: 'orders' //排序参数名，默认:sorts
                }
                , cols: [[ //表头
                    {field: 'id', title: '序号', width: 70, templet: '#tpl_index'}
                    , {field: 'sn', title: '订单号', width: 130}
                    , {field: 'merchantNo', title: '商户号', width: 130}
                    , {field: 'money', title: '金额', width: 100}
                    , {field: 'createTime', title: '创建时间', width: 150, sort: true}
                    , {field: 'accType', title: '账号类型', width: 100, templet: '#tpl_acctype'}

                    , {field: 'status', title: '状态', width: 100, templet: '#tpl_status'}

                    , {field: 'cardNo', title: '账号', width: 130}
                    , {field: 'cardName', title: '持卡人姓名', width: 130}
                    , {field: 'bankName', title: '银行名', width: 130}
                    , {field: 'bankIfsc', title: 'IFSC', width: 130}
                    , {field: 'bankNation', title: '国家', width: 130}

                    , {fixed: 'right', align: 'center', title: '操作', width: 200, toolbar: '#tpl_opera'}
                ]]
            });

            table.on('tool(table)', function (obj) {//tool event
                var data = obj.data; //获得当前行数据
                var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
                var tr = obj.tr; //获得当前行 tr 的DOM对象

                if (layEvent === 'shtg') {
                    shtg(data.id);
                } else if (layEvent === 'shjj') {
                    shjj(data.id);
                } else if (layEvent === 'suc') {
                    suc(data.id);
                } else if (layEvent === 'fail') {
                    fail(data.id);
                } else if (layEvent === 'detail') {
                    detail(data.id);
                }
            });

            table.on('sort(table)', function (obj) {
                if (!obj.sorts) return;
                createSortView(obj.sorts);
                searchData();
            });
        });
    });

    function shtg(id) {
        if (typeof id == 'undefined') {
            layer.msg("请选择需要处理的数据");
            return;
        }

    }

    function shjj(id) {
        if (typeof id == 'undefined') {
            layer.msg("请选择需要处理的数据");
            return;
        }
        $("#info_div").html("确定要将拒绝此充值申请？");
        $("#remark").val("");
        layer.open({
            type: 1,
            title: '拒绝充值',
            skin: 'layui-layer-rim', //加上边框
            area: ['550px', '300px'], //宽高
            btn: ['确定', '取消'],
            content: $("#audit_dlg")
            , yes: function (index) {
                var remark = $("#remark").val();
                lay_index = layer.load(1, {
                    shade: [0.1, '#fff'] //0.1透明度的白色背景
                });

                commReq('auditJj', {id: id, remark: remark}, function () {
                    layer.close(index);
                    searchData();
                });

            }, cancel: function (index) {
                layer.close(index);
            }
        });
    }

    function shtg(id) {
        if (typeof id == 'undefined') {
            layer.msg("请选择需要处理的数据");
            return;
        }
        lay_index = layer.open({
            type: 2,
            title: '审核通过',
            skin: 'layui-layer-rim', //加上边框
            area: ['80%', '80%'], //宽高
            content: 'edit?id=' + id
        });
    }

    function suc(id) {
        if (typeof id == 'undefined') {
            layer.msg("请选择需要处理的数据");
            return;
        }
        $("#info_div").html("执行此商户请确认款项是否到账，执行成功操作后金额将添加到商户余额？");
        $("#remark").val("");
        layer.open({
            type: 1,
            title: '充值成功',
            skin: 'layui-layer-rim', //加上边框
            area: ['550px', '300px'], //宽高
            btn: ['确定', '取消'],
            content: $("#audit_dlg")
            , yes: function (index) {
                var remark = $("#remark").val();
                lay_index = layer.load(1, {
                    shade: [0.1, '#fff'] //0.1透明度的白色背景
                });

                commReq('rechargeSuc', {id: id, remark: remark}, function () {
                    layer.close(index);
                    searchData();
                });

            }, cancel: function (index) {
                layer.close(index);
            }
        });
    }

    function fail(id) {
        if (typeof id == 'undefined') {
            layer.msg("请选择需要处理的数据");
            return;
        }
        $("#info_div").html("确认将此充值操作置为失败？");
        $("#remark").val("");
        layer.open({
            type: 1,
            title: '充值失败',
            skin: 'layui-layer-rim', //加上边框
            area: ['550px', '300px'], //宽高
            btn: ['确定', '取消'],
            content: $("#audit_dlg")
            , yes: function (index) {
                var remark = $("#remark").val();
                lay_index = layer.load(1, {
                    shade: [0.1, '#fff'] //0.1透明度的白色背景
                });

                commReq('rechargeFail', {id: id, remark: remark}, function () {
                    layer.close(index);
                    searchData();
                });

            }, cancel: function (index) {
                layer.close(index);
            }
        });

    }

    function detail(id) {
        if (typeof id == 'undefined') {
            layer.msg("请选择需要处理的数据");
            return;
        }

        lay_index = layer.open({
            type: 2,
            title: '订单详情',
            skin: 'layui-layer-rim', //加上边框
            area: ['80%', '80%'], //宽高
            content: 'detail?id=' + id
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
            title: '新增商户充值',
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
            title: '编辑商户充值',
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
