<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="adminPermission" uri="http://adminpermission.city.com" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>商户首页</title>
    <link rel="stylesheet" href="${context}/layuiadmin/layui/css/layui.css">
    <link rel="stylesheet" href="${context}/layuiadmin/style/admin.css">
    <!--[if lt IE 9]>
      <script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
      <script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

</head>
<body>

<div class="layui-fluid admin-head-title">
    <div class="layui-row left">
        <div class="layui-col-xs11 layui-col-sm11 layui-col-md11">
            <h3><b>&nbsp;商户首页</b></h3>
        </div>
        <div class="layui-col-xs1 layui-col-sm1 layui-col-md1" style="text-align:right;">
            <button class="layui-btn layui-btn-sm layui-btn-normal" title="刷新页面" onclick="window.location.reload();">
                <i class="layui-icon">&#x1002;</i>
            </button>
        </div>
    </div>
</div>

<div class="layui-fluid">
    <div class="layui-row layui-col-space15">

        <div class="layui-col-md6">
            <div class="layui-card">
                <div class="layui-card-header">数据统计</div>
                <div class="layui-card-body">

                    <div class="layui-carousel layadmin-carousel layadmin-backlog" lay-anim="" lay-indicator="inside"
                         style="width: 100%; height: 280px;">
                        <div carousel-item="">
                            <ul class="layui-row layui-col-space10">
                                <li class="layui-col-xs6">
                                    <a href="javascript:;" class="layadmin-backlog-body">
                                        <h3>余额</h3>
                                        <p><cite>${data.money }</cite> ${sysconfig.currency }</p>
                                    </a>
                                </li>
                                <li class="layui-col-xs6">
                                    <a href="javascript:;" class="layadmin-backlog-body">
                                        <h3>冻结金额</h3>
                                        <p><cite>${data.frozenMoney }</cite> ${sysconfig.currency }</p>
                                    </a>
                                </li>
                                <li class="layui-col-xs6">
                                    <a href="javascript:;" class="layadmin-backlog-body">
                                        <h3>可用金额</h3>
                                        <p><cite>${data.amount}</cite> ${sysconfig.currency }</p>
                                    </a>
                                </li>
                                <li class="layui-col-xs6">
                                    <a href="javascript:;" class="layadmin-backlog-body">
                                        <h3>今日成交额</h3>
                                        <p><cite>${data.orderMoney }</cite> ${sysconfig.currency }</p>
                                    </a>
                                </li>
                            </ul>

                        </div>


                    </div>

                </div>
            </div>

            <div class="layui-card">
                <div class="layui-card-header">
                    常用功能
                    <c:if test="${data.payword==null}">
                        <span style="color:red;">[支付密码尚未设置]</span>
                    </c:if>
                </div>
                <div class="layui-card-body layui-text layadmin-text">
                    <div class="layui-btn-group">
                        <button type="button" class="layui-btn layui-btn-sm layui-btn-normal"
                                onclick="updateLoginPwd()">修改登录密码
                        </button>
                        <button type="button" class="layui-btn layui-btn-sm layui-btn-normal"
                                onclick="updatePaywordPwd()">修改支付密码
                        </button>

                        <button type="button" class="layui-btn layui-btn-sm layui-btn-normal"
                                onclick="merchantAdminIp()">管理后台白名单
                        </button>
                        <button type="button" class="layui-btn layui-btn-sm layui-btn-normal" onclick="merchantApiIp()">
                            API白名单
                        </button>

                        <c:choose>
                            <c:when test="${validStatus==1}">
                                <button type="button" class="layui-btn layui-btn-sm layui-btn-warm"
                                        onclick="openChromeIdValid(0)">关闭谷歌身份验证
                                </button>
                            </c:when>
                            <c:otherwise>
                                <button type="button" class="layui-btn layui-btn-sm layui-btn-warm"
                                        onclick="openChromeIdValid(1)">开启谷歌身份验证
                                </button>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>

        </div>

        <div class="layui-col-md6">
            <div class="layui-card">
                <div class="layui-card-header">
                    关于
                    <i class="layui-icon layui-icon-tips" lay-tips="系统说明" lay-offset="5"></i>
                </div>
                <div class="layui-card-body layui-text layadmin-text">
                    <p>1. 管理后台登录地址： http://${sysconfig.domain}/login/login</p>
                    <p>2. 下单域名为： http://${sysconfig.apidomain}，即完整的下单地址为：http://${sysconfig.apidomain}/api/pay/order</p>
                    <p>3. 我方回调IP: ${sysconfig.nip}</p>
                    <p><span style="color:red;">4. 如需获取商户秘钥,您可以咨询管理员,也可以点击下方的"商户私密数据"获取.支付密码默认为空,请设置支付密码。</span></p>
                    <p><span style="color:red;">商户私密数据：<a href="javascript:void(0)" onclick="viewMerchantData()"
                                                          style="color:green;">点击查看</a></span></p>
                    <!--
                    <p>If you need help,<a href="#" onclick="$(this).html('Sorry,I won\'t help you.')" style="color:green;">Please click here!</a></p>
                     -->
                </div>
            </div>
            <div class="layui-card">
                <div class="layui-card-header">
                    版本记录
                </div>
                <div class="layui-card-body layui-text layadmin-text">
                    <p style="color:red;">更新了若干个Bug</p>
                </div>
            </div>
        </div>

    </div>

    <div id="password_dlg" style="display:none;">
        <form class="layui-form" style="width:90%;margin-top:10px;">
            <div class="layui-row" style="padding-bottom:5px;">
                <div class="layui-col-xs12 layui-col-sm12 layui-col-md12">
                    <label class="layui-form-label">原密码</label>
                    <div class="layui-input-block">
                        <input type="password" name="password" id="password" placeholder="密码" autocomplete="off"
                               class="layui-input"/>
                    </div>
                </div>
            </div>
            <div class="layui-row" style="padding-bottom:5px;">
                <div class="layui-col-xs12 layui-col-sm12 layui-col-md12">
                    <label class="layui-form-label">新密码</label>
                    <div class="layui-input-block">
                        <input type="password" name="password1" id="password1" placeholder="新密码" autocomplete="off"
                               class="layui-input"/>
                    </div>
                </div>
            </div>
            <div class="layui-row" style="padding-bottom:5px;">
                <div class="layui-col-xs12 layui-col-sm12 layui-col-md12">
                    <label class="layui-form-label">确认密码</label>
                    <div class="layui-input-block">
                        <input type="password" name="password2" id="password2" placeholder="确认密码" autocomplete="off"
                               class="layui-input"/>
                    </div>
                </div>
            </div>
        </form>
    </div>

    <div id="payword_dlg" style="display:none;">
        <form class="layui-form" style="width:90%;margin-top:10px;">
            <div class="layui-row" style="padding-bottom:5px;" id="payword_div">
                <div class="layui-col-xs12 layui-col-sm12 layui-col-md12">
                    <label class="layui-form-label">原密码</label>
                    <div class="layui-input-block">
                        <input type="password" name="payword" id="payword" placeholder="支付密码" autocomplete="off"
                               class="layui-input"/>
                    </div>
                </div>
            </div>
            <div class="layui-row" style="padding-bottom:5px;">
                <div class="layui-col-xs12 layui-col-sm12 layui-col-md12">
                    <label class="layui-form-label">新密码</label>
                    <div class="layui-input-block">
                        <input type="password" name="payword1" id="payword1" placeholder="新密码" autocomplete="off"
                               class="layui-input"/>
                    </div>
                </div>
            </div>
            <div class="layui-row" style="padding-bottom:5px;">
                <div class="layui-col-xs12 layui-col-sm12 layui-col-md12">
                    <label class="layui-form-label">确认密码</label>
                    <div class="layui-input-block">
                        <input type="password" name="payword2" id="payword2" placeholder="确认密码" autocomplete="off"
                               class="layui-input"/>
                    </div>
                </div>
            </div>
        </form>
    </div>

    <div id="adminip_dlg" style="display:none;">
        <form class="layui-form" style="width:90%;margin-top:10px;">
            <div style="color:red;width:90%;padding-left:20px;">修改即时生效。请仔细确认IP是否准确，错误的IP会导致管理功能无法使用</div>

            <div class="layui-row" style="padding-bottom:5px;">
                <div class="layui-col-xs12 layui-col-sm12 layui-col-md12">
                    <label class="layui-form-label">管理后台IP白名单</label>
                    <div class="layui-input-block">
                        <textarea name="adminip" id="adminip" placeholder="ip地址，多个用英文逗号隔开"
                                  class="layui-textarea"></textarea>
                    </div>
                </div>
            </div>

            <div class="layui-row" style="padding-bottom:5px;">
                <div class="layui-col-xs12 layui-col-sm12 layui-col-md12">
                    <label class="layui-form-label">支付密码</label>
                    <div class="layui-input-block">
                        <input type="password" name="paywordAdmin" id="paywordAdmin" placeholder="若未设置支付密码，请先设置支付密码"
                               autocomplete="off" class="layui-input"/>
                    </div>
                </div>
            </div>

        </form>
    </div>

    <div id="apiip_dlg" style="display:none;">
        <form class="layui-form" style="width:90%;margin-top:10px;">
            <div style="color:red;width:90%;padding-left:20px;">修改即时生效。请仔细确认IP是否准确，错误的IP会导致接口无法调用</div>

            <div class="layui-row" style="padding-bottom:5px;">
                <div class="layui-col-xs12 layui-col-sm12 layui-col-md12">
                    <label class="layui-form-label">API接口IP白名单</label>
                    <div class="layui-input-block">
                        <textarea name="apiip" id="apiip" placeholder="ip地址，多个用英文逗号隔开"
                                  class="layui-textarea"></textarea>
                    </div>
                </div>
            </div>

            <div class="layui-row" style="padding-bottom:5px;">
                <div class="layui-col-xs12 layui-col-sm12 layui-col-md12">
                    <label class="layui-form-label">支付密码</label>
                    <div class="layui-input-block">
                        <input type="password" name="paywordApi" id="paywordApi" placeholder="若未设置支付密码，请先设置支付密码"
                               autocomplete="off" class="layui-input"/>
                    </div>
                </div>
            </div>

        </form>
    </div>

    <div id="viewdata_dlg" style="display:none;">
        <form class="layui-form" style="width:90%;margin-top:10px;">
            <div style="color:red;width:90%;padding-left:20px;">请输入支付密码查看私有数据</div>

            <div class="layui-row" style="padding-bottom:5px;">
                <div class="layui-col-xs12 layui-col-sm12 layui-col-md12">
                    <label class="layui-form-label">支付密码</label>
                    <div class="layui-input-block">
                        <input type="password" name="paywordView" id="paywordView" placeholder="支付密码" autocomplete="off"
                               class="layui-input"/>
                    </div>
                </div>
            </div>
        </form>
    </div>

    <script src="${context}/js/jquery.min.js"></script>
    <script src="${context}/layuiadmin/layui/layui.js?2"></script>
    <script src="${context}/js/common.js?9"></script>
    <script>
        var upKey = "zxksi213_5";
        var isSetPayword = false;
        <c:if test="${data.payword!=null}">
        isSetPayword = true;
        </c:if>
        $(function () {
            /**
             layui.use(['layer'], function(){
	});
             **/
            layui.config({
                base: '${context}/layuiadmin/' //静态资源所在路径
            }).extend({
                index: 'lib/index' //主入口模块
            }).use(['index', 'console', 'layer']);
        });

        function openQrcode() {
            layer.open({
                type: 1,
                title: '二维码',
                skin: 'layui-layer-rim', //加上边框
                area: ['550px', '300px'], //宽高
                content: $("#qrcode_dlg")
            });
        }

        //开启chrome
        function openChromeIdValid(val) {
            var msg = '启用';
            if (val == 0) {
                msg = '关闭';
            }

            layer.confirm('您真的要' + msg + '谷歌验证吗？', {
                icon: 3,//询问图标
                btn: ['确定', '取消'] //按钮
            }, function (index) {
                layer.close(index);//关闭弹层

                lay_index = layer.load(1, {
                    shade: [0.1, '#fff'] //0.1透明度的白色背景
                });
                $.ajax({
                    type: 'post',
                    url: "${context}/system/sysUser/openChromeIdValid",
                    data: {val: val},
                    dataType: "json",
                    cache: false,
                    success: function (data) {
                        layer.close(lay_index);

                        if (data.msg) layer.msg(data.msg);
                        if (data.code == 200) {
                            layer.close(index);
                            if (val == 1) {
                                var $r = data.result;
                                layer.confirm('开启成功<br/>用户名：' + $r.name + "<br/>秘钥：" + $r.key + "<br/><img src='" + $r.img + "'/>", {
                                    icon: 3,//询问图标
                                    btn: ['确定'] //按钮
                                }, function (index) {
                                    window.location.reload();
                                });
                            } else {
                                window.location.reload();
                            }

                        }
                    },
                    error: function (xhr, desc, err) {
                        layer.msg("数据请求失败:" + desc);
                        layer.close(lay_index);
                    }
                });

            }, function (index) {
                layer.close(index);
            });
        }

        function logout() {
            layer.confirm('您真的要退出吗？', {
                icon: 3,//询问图标
                btn: ['确定', '取消'] //按钮
            }, function (index) {
                layer.close(index);//关闭弹层
                window.parent.location.href = "${context}/login/logout";
            }, function (index) {
                layer.close(index);
            });

        }

        var lay_index = 0;

        function updateLoginPwd() {
            $("#password").val("");
            $("#password1").val("");
            $("#password2").val("");

            layer.open({
                type: 1,
                title: '修改登录密码',
                skin: 'layui-layer-rim', //加上边框
                area: ['550px', '300px'], //宽高
                btn: ['确定', '取消'],
                content: $("#password_dlg")
                , yes: function (index) {
                    var password = $("#password").val();
                    var password1 = $("#password1").val();
                    var password2 = $("#password2").val();

                    if (password == "") {
                        layer.msg("原密码不能为空！");
                        return;
                    }
                    if (password1 == "") {
                        layer.msg("新密码不能为空！");
                        return;
                    }
                    if (password2 == "") {
                        layer.msg("确认密码不能为空！");
                        return;
                    }

                    if (password1 != password2) {
                        layer.msg("新密码与确认密码不一致！");
                        return;
                    }

                    lay_index = layer.load(1, {
                        shade: [0.1, '#fff'] //0.1透明度的白色背景
                    });
                    $.ajax({
                        type: 'post',
                        url: "updateLoginPwd",
                        data: {password: password, password1: password1, password2: password2},
                        dataType: "json",
                        cache: false,
                        success: function (data) {
                            layer.close(lay_index);

                            if (data.msg) layer.msg(data.msg);
                            if (data.code == 200) {
                                layer.close(index);
                            }
                        },
                        error: function (xhr, desc, err) {
                            layer.msg("数据请求失败:" + desc);
                            layer.close(lay_index);
                        }
                    });

                }, cancel: function (index) {
                    layer.close(index);
                }
            });
        }

        function updatePaywordPwd() {
            if (isSetPayword) {
                $("#payword_div").show();
            } else {
                $("#payword_div").hide();
            }
            $("#payword").val("");
            $("#payword1").val("");
            $("#payword2").val("");

            layer.open({
                type: 1,
                title: '修改支付密码',
                skin: 'layui-layer-rim', //加上边框
                area: ['550px', '300px'], //宽高
                btn: ['确定', '取消'],
                content: $("#payword_dlg")
                , yes: function (index) {
                    var payword = $("#payword").val();
                    var payword1 = $("#payword1").val();
                    var payword2 = $("#payword2").val();


                    if (payword1 == "") {
                        layer.msg("新密码不能为空！");
                        return;
                    }
                    if (payword2 == "") {
                        layer.msg("确认密码不能为空！");
                        return;
                    }

                    if (payword1 != payword2) {
                        layer.msg("新密码与确认密码不一致！");
                        return;
                    }

                    lay_index = layer.load(1, {
                        shade: [0.1, '#fff'] //0.1透明度的白色背景
                    });
                    $.ajax({
                        type: 'post',
                        url: "updatePaywordPwd",
                        data: {payword: payword, payword1: payword1, payword2: payword2},
                        dataType: "json",
                        cache: false,
                        success: function (data) {
                            layer.close(lay_index);

                            if (data.msg) layer.msg(data.msg);
                            if (data.code == 200) {
                                isSetPayword = true;
                                layer.close(index);
                            }
                        },
                        error: function (xhr, desc, err) {
                            layer.msg("数据请求失败:" + desc);
                            layer.close(lay_index);
                        }
                    });

                }, cancel: function (index) {
                    layer.close(index);
                }
            });
        }

        function showUpdateInfo() {

        }

        function merchantAdminIp() {
            $("#adminip").val("");
            $("#paywordAdmin").val("");

            layer.open({
                type: 1,
                title: '设置商户管理后台IP白名单',
                skin: 'layui-layer-rim', //加上边框
                area: ['550px', '300px'], //宽高
                btn: ['确定', '取消'],
                content: $("#adminip_dlg")
                , yes: function (index) {
                    var adminip = $("#adminip").val();
                    var paywordAdmin = $("#paywordAdmin").val();

                    if (adminip == "") {
                        layer.msg("IP不能为空！");
                        return;
                    }
                    if (paywordAdmin == "") {
                        layer.msg("支付密码不能为空！");
                        return;
                    }

                    lay_index = layer.load(1, {
                        shade: [0.1, '#fff'] //0.1透明度的白色背景
                    });
                    $.ajax({
                        type: 'post',
                        url: "${context}/system/payMerchant/merchantAdminIp",
                        data: {adminip: adminip, paywordAdmin: paywordAdmin},
                        dataType: "json",
                        cache: false,
                        success: function (data) {
                            layer.close(lay_index);

                            if (data.msg) layer.msg(data.msg);
                            if (data.code == 200) {
                                layer.close(index);
                            }
                        },
                        error: function (xhr, desc, err) {
                            layer.msg("数据请求失败:" + desc);
                            layer.close(lay_index);
                        }
                    });

                }, cancel: function (index) {
                    layer.close(index);
                }
            });
        }

        function merchantApiIp() {
            $("#apiip").val("");
            $("#paywordApi").val("");

            layer.open({
                type: 1,
                title: '设置API接口IP白名单',
                skin: 'layui-layer-rim', //加上边框
                area: ['550px', '300px'], //宽高
                btn: ['确定', '取消'],
                content: $("#apiip_dlg")
                , yes: function (index) {
                    var apiip = $("#apiip").val();
                    var paywordApi = $("#paywordApi").val();

                    if (apiip == "") {
                        layer.msg("IP不能为空！");
                        return;
                    }
                    if (paywordApi == "") {
                        layer.msg("支付密码不能为空！");
                        return;
                    }

                    lay_index = layer.load(1, {
                        shade: [0.1, '#fff'] //0.1透明度的白色背景
                    });
                    $.ajax({
                        type: 'post',
                        url: "${context}/system/payMerchant/merchantApiIp",
                        data: {apiip: apiip, paywordApi: paywordApi},
                        dataType: "json",
                        cache: false,
                        success: function (data) {
                            layer.close(lay_index);

                            if (data.msg) layer.msg(data.msg);
                            if (data.code == 200) {
                                layer.close(index);
                            }
                        },
                        error: function (xhr, desc, err) {
                            layer.msg("数据请求失败:" + desc);
                            layer.close(lay_index);
                        }
                    });

                }, cancel: function (index) {
                    layer.close(index);
                }
            });
        }

        function viewMerchantData() {
            $("#paywordView").val("");

            layer.open({
                type: 1,
                title: '设置API接口IP白名单',
                skin: 'layui-layer-rim', //加上边框
                area: ['550px', '300px'], //宽高
                btn: ['确定', '取消'],
                content: $("#viewdata_dlg")
                , yes: function (index) {
                    var paywordView = $("#paywordView").val();

                    if (paywordView == "") {
                        layer.msg("支付密码不能为空！");
                        return;
                    }

                    lay_index = layer.load(1, {
                        shade: [0.1, '#fff'] //0.1透明度的白色背景
                    });
                    $.ajax({
                        type: 'post',
                        url: "${context}/system/payMerchant/viewMerchantData",
                        data: {paywordView: paywordView},
                        dataType: "json",
                        cache: false,
                        success: function (data) {
                            layer.close(lay_index);

                            if (data.msg) layer.msg(data.msg);
                            if (data.code == 200) {
                                layer.close(index);
                                var $r = data.result;
                                var $msg = '';
                                if ($r.validCode) {
                                    $msg = '<br/>用户名：' + $r.un + "<br/>秘钥：" + $r.validCode + '<br/><img src="' + $r.img + '" />';
                                }
                                layer.confirm('您的账号信息如下<br/>商户秘钥：' + $r.my + '<br/>管理后台IP白名单：' + ($r.adminip == null ? "" : $r.adminip) + '<br/>API接口IP白名单：' + ($r.apiip == null ? "" : $r.apiip) + $msg, {
                                    btn: ['好的'] //按钮
                                });

                            }
                        },
                        error: function (xhr, desc, err) {
                            layer.msg("数据请求失败:" + desc);
                            layer.close(lay_index);
                        }
                    });

                }, cancel: function (index) {
                    layer.close(index);
                }
            });
        }
    </script>
</body>
</html>
