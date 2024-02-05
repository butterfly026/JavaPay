<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="adminPermission" uri="http://adminpermission.city.com" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>上游管理</title>
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
            <h3><b>&nbsp;上游管理</b></h3>
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
        <legend class="admin-search-title">上游查询</legend>
        <form id="searchForm" class="layui-form">
            <div class="layui-row admin-search-row">

                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                    <input type="text" name="username" id="username" placeholder="上游帐号" class="layui-input"/>
                </div>
                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                    <input type="text" name="name" id="name" placeholder="上游名" class="layui-input"/>
                </div>
                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                    <select name="status">
                        <option value="">上游状态</option>
                        <option value="1">启用</option>
                        <option value="0">禁用</option>
                    </select>
                </div>

                <div class="layui-col-xs2 layui-col-sm2 layui-col-md2">
                    <select name="gotype">
                        <option value="">跳转方式</option>
                        <option value="0">API跳转</option>
                        <option value="1">表单跳转</option>
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
        <adminPermission:hasPermission permission="admin:payclient:add">
            <button class="layui-btn layui-btn-sm layui-btn-normal" onclick="add();">
                添加
            </button>
        </adminPermission:hasPermission>

        <span class="order-tag" id="order_tag">

		</span>
    </div>
    <div style="clear: both;"></div>
    <table id="datatable" lay-filter="table"></table>

    <!-- 设置代付密码 -->
    <div id="payword_dlg" style="display:none;">
        <form class="layui-form" style="width:90%;margin-top:10px;">
            <div class="layui-row" style="padding-bottom:5px;width:100%;text-align:center;color:red;" id="info_div">
            </div>
            <div class="layui-row" style="padding-bottom:5px;">
                <div class="layui-col-xs12 layui-col-sm12 layui-col-md12">
                    <label class="layui-form-label">代付密码</label>
                    <div class="layui-input-block">
                        <input type="text" name="payword" id="payword" placeholder="代付密码" autocomplete="off"
                               class="layui-input"/>
                    </div>
                </div>
            </div>
        </form>
    </div>

    <script type="text/html" id="tpl_status">
        {{# if(d.status && d.status=="1"){ }}
        <span class="layui-badge layui-bg-blue">启用</span>
        {{# }else{ }}
        <span class="layui-badge">禁用</span>
        {{# } }}
    </script>

    <script type="text/html" id="tpl_gotype">
        {{# if(d.gotype==0){ }}
        API跳转
        {{# }else if(d.gotype==1){ }}
        表单跳转
        {{# } }}
    </script>

    <script type="text/html" id="tpl_url">
        {{# if(d.url){ }}
        <a href="{{d.url}}" target="_blank" style="color:#1E9FFF;">{{d.url}}</a>
        {{# } }}
    </script>

    <script type="text/html" id="tpl_opera">
        <adminPermission:hasPermission permission="admin:payclient:status">
            {{# if(d.status && d.status=="1"){ }}
            <a class="layui-btn layui-btn-xs layui-btn-danger" onclick="upStatus({{d.id}},'0');">禁用</a>
            {{# }else{ }}
            <a class="layui-btn layui-btn-xs layui-btn-normal" onclick="upStatus({{d.id}},'1');">启用</a>
            {{# } }}

        </adminPermission:hasPermission>


        <button class="layui-btn layui-btn-xs" id="layuidropdown_{{d.id}}" lay-dropdown="{align:'right', menus:[

			<adminPermission:hasPermission permission="admin:payclientchannel:edit">
				{layIcon:'layui-icon layui-icon-edit',txt: '编辑',event:'edit'},
			</adminPermission:hasPermission>

			<adminPermission:hasPermission permission="admin:payclientchannel:edit">
				{layIcon:'layui-icon layui-icon-password',txt: '设置代付密码',event:'uppayword'},
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
                    {field: 'id', title: '序号', width: 70, sort: true}
                    , {field: 'username', title: '账号', width: 120}
                    , {field: 'name', title: '上游名', width: 120}

                    //,{field:'status', title: '状态', width:80,sort:true,templet:'#tpl_status'}

                    , {field: 'money', title: '余额', width: 80, sort: true}
                    , {field: 'frozenMoney', title: '冻结金额', width: 100, sort: true}

                    , {field: 'url', title: '网关', width: 150, templet: '#tpl_url'}
                    , {field: 'descript', title: '上游描述', width: 120}

                    , {field: 'dayMoney', title: '今日成交额', width: 120, sort: true}
                    , {field: 'jrcg', title: '今日成功', width: 120, sort: true}
                    , {field: 'jrdds', title: '今日订单数', width: 120, sort: true}
                    , {field: 'jrcgl', title: '今日成功率', width: 120, sort: true}


                    , {field: 'gotype', title: '跳转方式', width: 120, templet: '#tpl_gotype'}
                    , {field: 'merchantIp', title: '回调IP', width: 120}

                    , {field: 'createTime', title: '创建时间', width: 150, sort: true}

                    , {field: 'paykeyname', title: '代付程序', width: 120}

                    , {field: 'payword', title: '代付密码', width: 100}

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
                } else if (layEvent === 'uppayword') {
                    uppayword(data.id);
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

    //更新代付密码
    function uppayword(id) {
        $("#info_div").html("请设置代付密码");
        $("#payword").val("");
        layer.open({
            type: 1,
            title: '设置代付密码',
            skin: 'layui-layer-rim', //加上边框
            area: ['550px', '300px'], //宽高
            btn: ['确定', '取消'],
            content: $("#payword_dlg")
            , yes: function (index) {
                var payword = $("#payword").val();
                lay_index = layer.load(1, {
                    shade: [0.1, '#fff'] //0.1透明度的白色背景
                });

                commReq('uppayword', {id: id, payword: payword}, function () {
                    layer.close(index);
                    searchData();
                });

            }, cancel: function (index) {
                layer.close(index);
            }
        });
    }

    //更新状态
    function upStatus(id, status) {

        //TODO
        if ('1' == status) {
            $.ajax({
                type: 'post',
                url: 'yajinStatus',
                data: {id: id},
                dataType: "json",
                cache: false,
                success: function (data) {
                    if (data.result == 1) {
                        commReq('upStatus', {id: id, status: status}, function () {
                            searchData();
                        });
                    } else {

                        layer.confirm('当前上游充值金额已经超过押金，开启渠道会重置押金。确认开启吗？', {
                            icon: 3,//询问图标
                            btn: ['确定', '取消'] //按钮
                        }, function (index) {
                            layer.close(index);//关闭弹层

                            commReq('resetYajin', {id: id}, function () {

                                commReq('upStatus', {id: id, status: status}, function () {
                                    searchData();
                                });

                            });

                        }, function (index) {
                            layer.close(index);
                        });

                    }
                },
                error: function (xhr, desc, err) {
                    layer.msg("数据请求失败:" + desc);
                }
            });

            //TODO 判断充值的金额是否大于押金  大于则提示需要重置才能开启    小于则直接开始

        } else {
            commReq('upStatus', {id: id, status: status}, function () {
                searchData();
            });
        }

        /**
         var msg="禁用";
         if(status=='1'){
			msg="启用";
		}
         layer.confirm('是否'+msg+'此上游？', {
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
            title: '新增上游管理',
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
            title: '编辑上游管理',
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
        searchData();

        var data = JSON.parse(msg);

        layer.confirm('成功添加上游<br/>账号：' + data.username + '<br/>密码：' + data.pwd + '<br/>密码仅显示一次，建议登录后修改密码！', {
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
