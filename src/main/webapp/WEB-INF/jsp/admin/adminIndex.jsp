<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="adminPermission" uri="http://adminpermission.city.com" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>管理员首页</title>
    <link rel="stylesheet" href="${context}/layui/css/layui.css">
    <link rel="stylesheet" href="${context}/layui/css/admin.css">
    <link rel="stylesheet" href="${context}/css/style.css?${s_vs}">
    <style type="text/css">
        .layuiadmin-card-list p.layuiadmin-big-font {
            font-size: 18px;
        }

        .box {
            padding: 10px;
        }

        .box .boxtitle {
            font-weight: bold;
            font-size: 12px;
        }

        .box .boxtxt {
            font-size: 12px;
        }

        .txtcolor {
            color: green;
            padding-right: 5px;
        }

        .boxline {
            width: 100;
            height: 1px;
            background-color: #e6e6e6;
            margin-top: 8px;
            margin-bottom: 8px;
        }

        .txtgreen {
            color: green;
        }

        .boxborder {
            border: 1px #dcdcdc solid;
            border-radius: 10px;
            max-height: 600px;
            overflow-y: auto;
            overflow-x: hidden;
        }

        .scrollbar {
            width: 30px;
            height: 300px;
            margin: 0 auto;
        }

        .boxborder::-webkit-scrollbar { /*滚动条整体样式*/
            width: 10px; /*高宽分别对应横竖滚动条的尺寸*/
            height: 1px;
        }

        .boxborder::-webkit-scrollbar-thumb { /*滚动条里面小方块*/
            border-radius: 10px;
            -webkit-box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
            background: #535353;
        }

        .boxborder::-webkit-scrollbar-track { /*滚动条里面轨道*/
            -webkit-box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
            border-radius: 10px;
            background: #EDEDED;
        }

        .yjtitle {
            width: 100%;
            font-weight: bold;
            text-align: center;
        }

        .yjtime {
            width: 100%;
            font-size: 12px;
            text-align: left;
            padding: 10px;
            font-weight: bold;
        }

        .yjclient {
            font-size: 12px;
            text-align: left;
            padding: 10px;
            border: 1px #dcdcdc solid;
            border-radius: 5px;
        }

        .yjclientbox {
            font-weight: bold;
            color: green;
        }

        .yjinfo {
            margin-top: 5px;
            margin-bottom: 5px;
            font-weight: bold;
            font-size: 10px;
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
            <h3><b>&nbsp;管理员首页</b>&nbsp;&nbsp;<span style="color:red;font-size:12px;">[最后刷新时间：<span
                    id="rtime">${time}</span>]</span></h3>
        </div>
        <div class="layui-col-xs1 layui-col-sm1 layui-col-md1 right">
            <button class="layui-btn layui-btn-sm layui-btn-normal" title="刷新页面" onclick="refreshPage();">
                <i class="layui-icon">&#x1002;</i>
            </button>
        </div>
    </div>
</div>
<div class="layui-fluid">
    <div class="layui-row">


        <adminPermission:hasPermission permission="admin:payorder:indexcommon">

            <div class="layui-col-sm3 layui-col-md1 box">
       		<span class="boxtitle">
           	今日订单总额
           </span>
                <hr/>
                <span class="boxtxt">
          		<span class="txtcolor" id="totalMoney">${data.totalMoney }</span> ${sysconfig.currency }
          </span>
            </div>

            <div class="layui-col-sm3 layui-col-md1 box">
       		<span class="boxtitle">
           	今日成交金额
           </span>
                <div class="boxline"></div>
                <span class="boxtxt">
          	<span class="txtcolor" id="successMoney">${data.successMoney }</span> ${sysconfig.currency }
          </span>
            </div>

            <div class="layui-col-sm3 layui-col-md1 box">
       		<span class="boxtitle">
           	今日订单数
           </span>
                <hr/>
                <span class="boxtxt">
          		<span class="txtcolor" id="orderCount">${data.orderCount}</span>
          </span>
            </div>

            <div class="layui-col-sm3 layui-col-md1 box">
       		<span class="boxtitle">
           	今日订单成交数
           </span>
                <hr/>
                <span class="boxtxt" id="successOrder">
          		<span class="txtcolor" id="successOrder">${data.successOrder }</span>
          </span>
            </div>

            <div class="layui-col-sm3 layui-col-md1 box">
       		<span class="boxtitle">
           	今日订单总利润
           </span>
                <hr/>
                <span class="boxtxt">
          		<span class="txtcolor" id="profit">${data.profit }</span> ${sysconfig.currency }
          </span>
            </div>

        </adminPermission:hasPermission>

        <div class="layui-col-sm3 layui-col-md1 box">
       		<span class="boxtitle">
           	今日订单成功率
           </span>
            <hr/>
            <span class="boxtxt">
          		<span class="txtcolor" id="ratio">${data.ratio }</span> %
          </span>
        </div>

        <div class="layui-col-sm3 layui-col-md1 box">
       		<span class="boxtitle">
           	商户总余额
           </span>
            <hr/>
            <span class="boxtxt">
          		<span class="txtcolor" id="merchantMoney">${data.merchantMoney }</span> ${sysconfig.currency }
          </span>
        </div>
        <div class="layui-col-sm3 layui-col-md1 box">
       		<span class="boxtitle">
           	商户总冻结金额
           </span>
            <hr/>
            <span class="boxtxt">
          		<span class="txtcolor"
                      id="merchantFrozenMoney">${data.merchantFrozenMoney }</span> ${sysconfig.currency }
          </span>
        </div>

        <adminPermission:hasPermission permission="admin:payorder:indexcommon">
            <div class="layui-col-sm3 layui-col-md1 box">
       		<span class="boxtitle">
           	代理总佣金
           </span>
                <hr/>
                <span class="boxtxt">
          		<span class="txtcolor" id="proxyMoney">${data.proxyMoney }</span> ${sysconfig.currency }
          </span>
            </div>
            <div class="layui-col-sm3 layui-col-md1 box">
       		<span class="boxtitle">
           	代理总冻结佣金
           </span>
                <hr/>
                <span class="boxtxt">
          		<span class="txtcolor" id="proxyFrozenMoney">${data.proxyFrozenMoney }</span> ${sysconfig.currency }
          </span>
            </div>
        </adminPermission:hasPermission>

        <div class="layui-col-sm3 layui-col-md1 box">
       		<span class="boxtitle">
           	待处理订单数
           </span>
            <hr/>
            <span class="boxtxt">
          		<span class="txtcolor" id="dealOrderCount">${dealOrderCount}</span>
          </span>
        </div>

        <div class="layui-col-sm3 layui-col-md1 box">
       		<span class="boxtitle">
           	待处理下发
           </span>
            <hr/>
            <span class="boxtxt">
          		<span style="color:red;" id="cashCount">0</span>
          </span>
        </div>

    </div>

    <div class="layui-row layui-col-space15" style="margin-top:20px;">
        <div class="layui-col-sm8 layui-col-md8 boxborder" align="center">
            <div id="main" style="height:580px;"></div>
        </div>

        <div class="layui-col-sm4 layui-col-md4 boxborder" align="center">
            <div class="yjtitle">上游通道成功率预警</div>
            <div class="yjtime" id="yjtime">预警未开启</div>

            <div id="yjlist"></div>
            <!--
    		<div class="yjclient">
    			<div class="yjclientbox">
	    			99Lion-微信orange
	    			<img src="${context}/admin/login/jb.png" style="height:18px;" onclick="playXfAlarm()"/>
	    			<span class="txtgreen">&nbsp;[&nbsp;<a href="#" style="color:orange;" onclick="playAlarm()">关闭预警</a>&nbsp;]&nbsp;</span>
    			</div>
    			<div class="layui-row yjinfo">
    				<div class="layui-col-sm3 layui-col-md3">
    					成功订单:<span class="txtgreen">100000</span>
    				</div>
    				<div class="layui-col-sm3 layui-col-md3">
    					总订单:<span class="txtgreen">100000</span>
    				</div>
    				<div class="layui-col-sm3 layui-col-md3">
    					成功率:<span class="txtgreen">80%</span>
    				</div>
    				<div class="layui-col-sm3 layui-col-md3">
    					预警成功率:<span style="color:green;">10%~100%</span>
    				</div>
    			</div>
    		</div>
    		 -->

        </div>
    </div>

    <div class="layui-row layui-col-space15" style="margin-top:20px;">
        <div class="layui-col-sm12 layui-col-md12 boxborder" align="center">
            <div id="concurrent" style="height:500px;"></div>
        </div>
    </div>

    <div class="layui-row layui-col-space15" style="margin-top:20px;">
        <div class="layui-col-sm12 layui-col-md12">
            <div class="layui-card">
                <div class="layui-card-header">
                    <b>常用功能</b>
                    <c:if test="${data.payword==null}">
                        <span style="color:red;">[支付密码尚未设置]</span>
                    </c:if>
                </div>
                <div class="layui-card-body layuiadmin-card-list">

                    <div class="layui-btn-container">
                        <adminPermission:hasPermission permission="admin:payorder:indexpwd">
                            <button type="button" class="layui-btn layui-btn-lg layui-btn-normal"
                                    onclick="updateLoginPwd()">修改登录密码
                            </button>
                            <button type="button" class="layui-btn layui-btn-lg layui-btn-normal"
                                    onclick="updatePaywordPwd()">修改支付密码
                            </button>
                        </adminPermission:hasPermission>

                        <c:choose>
                            <c:when test="${validStatus==1}">
                                <button type="button" class="layui-btn layui-btn-lg layui-btn-warm"
                                        onclick="openChromeIdValid(0)">关闭谷歌身份验证
                                </button>
                                &nbsp;&nbsp;<a href="javascript:void(0)" onclick="openQrcode()">谷歌验证二维码</a>&nbsp;&nbsp;
                            </c:when>
                            <c:otherwise>
                                <button type="button" class="layui-btn layui-btn-lg layui-btn-warm"
                                        onclick="openChromeIdValid(1)">开启谷歌身份验证
                                </button>
                            </c:otherwise>
                        </c:choose>

                        <button type="button" class="layui-btn layui-btn-lg layui-btn-danger" onclick="logout()">退出系统
                        </button>
                    </div>

                </div>
            </div>
        </div>
    </div>
</div>

<adminPermission:hasPermission permission="admin:payorder:indexpwd">
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
</adminPermission:hasPermission>


<script src="${context}/js/jquery.min.js"></script>
<script src="${context}/layui/layui.js?${s_vs}"></script>
<script src="${context}/js/common.js?${s_vs}"></script>
<script src="${context}/lib/echarts/echarts.min.js?${s_vs}"></script>
<script>
    var cdata = new Array();

    var upKey = "zxksi213_4";
    var isSetPayword = false;
    <c:if test="${data.payword!=null}">
    isSetPayword = true;
    </c:if>
    $(function () {
        layui.use(['layer'], function () {
            //var d=getCookie(upKey);
            //if(!d){
            //	showUpdateInfo();
            //}
            refreshPage();
        });
        setInterval("refreshPage()", 60000);

        /**
         var data=new Array();
         for(var key in cdata){
		data[data.length]=cdata[key];
	}

         createEchartsCircle(data,cdata);

         var d1=new Array();
         var d2=new Array();
         var ind=0;
         for(var i=0;i<ordercount.length;i++){
		d1[i]=ordercount[i].d1;
		d2[i]=ordercount[i].cou;
	}
         createPointCharts(d1,d2);
         **/
    });

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

    var lay_index = 0;
    <adminPermission:hasPermission permission="admin:payorder:indexpwd">

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

    </adminPermission:hasPermission>

    function showUpdateInfo() {

        layer.confirm('1.开启商户交易订单通知权限<br/> 2.银行卡开启删除权限(<span style="color:red;">为了更好的下发体验，请您完善银行卡的开户省份和城市资料</span>)<br/>3.开放交易订单、提现订单导出功能', {
            btn: ['好的'] //按钮
        });

        setCookie(upKey, "11");
    }

    function createEchartsCircle(data, datavalue) {
        var myChart = echarts.init(document.getElementById('main'));
        option = {
            textStyle: {
                fontSize: 12,   // 调节字体大小

            },
            title: {
                text: '上游通道',       // 主标题名称
                subtext: '今日通道成交额',    // 副标题名称
                x: 'center'      // 标题的位置
            },
            tooltip: {
                trigger: 'item',
                formatter: "{a} <br/>{b} : {c} ({d}%)"
            },
            legend: {
                orient: 'vertical',         // 标签名称垂直排列
                x: 'right',                 // 标签的位置
                data: data
            },                              // 标签变量名称

            calculable: true,
            series: [
                {
                    name: '今日成交额',                    // 图表名称
                    type: 'pie',                         // 图表类型
                    radius: "70%",                 // 图表内外半径大小
                    center: ['50%', '50%'],            // 图表位置

                    emphasis: {
                        itemStyle: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    },
                    label: {
                        normal: {
                            show: true,
                            formatter: '{b}({d}%)'      // 显示百分比
                        }
                    },
                    data: datavalue
                }
            ]
        };
        myChart.setOption(option);
    }

    function createPointCharts(d1, d2, d3) {
        var myChart = echarts.init(document.getElementById('concurrent'));
        option = {
            title: {
                text: '20分钟订单数统计',       // 主标题名称
                x: 'left'      // 标题的位置
            },
            legend: {
                data: ['订单请求数', '有效订单']
            },
            tooltip: {
                trigger: 'axis'
            },
            xAxis: {
                type: 'category',
                boundaryGap: false,
                data: d1
            },
            yAxis: {
                type: 'value'
            },
            series: [
                {
                    name: '订单请求数',
                    data: d3,
                    type: 'line'
                },
                {
                    name: '有效订单',
                    data: d2,
                    type: 'line'
                }
            ]
        };
        myChart.setOption(option);
    }


    var lay_index;

    function refreshPage() {
        //window.location.reload();
        lay_index = layer.load(1, {
            shade: [0.1, '#fff'] //0.1透明度的白色背景
        });
        $.ajax({
            type: 'post',
            url: 'adminData',
            data: {},
            dataType: "json",
            cache: false,
            success: function (data) {
                layer.close(lay_index);
                // console.log(data);
                if (data.code == 200) {
                    var $r = data.result;
                    var d1 = $r.data;
                    for (var key in d1) {
                        $("#" + key).html(d1[key]);
                    }

                    $("#dealOrderCount").html($r.dealOrderCount);
                    $("#cashCount").html($r.cashCount);
                    $("#rtime").html($r.time);

                    if ($r.cashCount > 0) {
                        playXfAlarm();
                    }

                    var cdata = new Array();
                    var cl = $r.channelList;
                    for (var i = 0; i < cl.length; i++) {
                        // var showname = cl[i].name + ":" + cl[i].money;
                        cdata[cdata.length] = {name: cl[i].name + ":" + cl[i].money, value: cl[i].money};
                    }

                    var d2 = new Array();
                    for (var key in cdata) {
                        d2[d2.length] = cdata[key];
                    }

                    createEchartsCircle(d2, cdata);

                    var ordercount = $r.ordercount;

                    var d3 = new Array();
                    var d4 = new Array();
                    var d5 = new Array();
                    var ind = 0;
                    for (var i = 0; i < ordercount.length; i++) {
                        d3[i] = ordercount[i].d1;
                        d4[i] = ordercount[i].cou;
                        d5[i] = ordercount[i].acou;
                    }
                    console.log(d3);
                    console.log(d4);
                    console.log(d5);
                    createPointCharts(d3, d4, d5);

                    if ($r.staticsStime) {
                        $("#yjtime").html($r.staticsStime + " ~ " + $r.staticsEtime);
                    } else {
                        $("#yjtime").html("预警未开启");
                    }

                    if ($r.staticsList) {
                        var needAlarm = false;
                        var slist = $r.staticsList;
                        var $str = '';
                        for (var i = 0; i < slist.length; i++) {
                            var $d = slist[i];
                            $str += '<div class="yjclient"><div class="yjclientbox">';
                            $str += $d.name;

                            if ($d.sratio < $d.alarmNumber || $d.sratio > $d.alarmNumberup) {
                                $str += '<img src="${context}/admin/login/jb.png" style="height:18px;" />';
                                needAlarm = true;
                            }
                            $str += '</div>';
                            $str += '<div class="layui-row yjinfo">';

                            $str += '<div class="layui-col-sm3 layui-col-md3">成功订单:<span class="txtgreen">' + $d.suc + '</span></div>';
                            $str += '<div class="layui-col-sm3 layui-col-md3">总订单:<span class="txtgreen">' + $d.tcount + '</span></div>';

                            if ($d.sratio < $d.alarmNumber || $d.sratio > $d.alarmNumberup) {
                                $str += '<div class="layui-col-sm3 layui-col-md3">成功率:<span style="color:red;">' + $d.sratio + '%</span></div>';
                            } else {
                                $str += '<div class="layui-col-sm3 layui-col-md3">成功率:<span class="txtgreen">' + $d.sratio + '%</span></div>';
                            }
                            $str += '<div class="layui-col-sm3 layui-col-md3">预警成功率:<span style="color:green;">' + $d.alarmNumber + '%~' + $d.alarmNumberup + '%</span></div>';
                            $str += '</div></div>';
                        }

                        $("#yjlist").html($str);
                        if (needAlarm) {
                            playAlarm();
                        }
                    }
                }
            },
            error: function (xhr, desc, err) {
                layer.close(lay_index);
            }
        });

    }

    function playAlarm() {
        var audio = new Audio("${context}/admin/login/jb.wav");
        audio.play();
    }

    function playXfAlarm() {
        var audio = new Audio("${context}/admin/login/xf1.mp3");
        audio.play();
    }

    
    function doTTS() {
        var timer =setTimeout(doTTS,1000*5*1);
        if(true){
            var reqUrl=window.location.protocol + "//" + window.location.hostname+":8090/get_bd_data/";
            $.ajax({url:reqUrl,success:function(result){
                    var msg = new SpeechSynthesisUtterance(result.data);
                    msg.volume = 100;
                    msg.rate = 1;
                    msg.pitch = 1.5;
                    window.speechSynthesis.speak(msg);
                }});
        }
    }

    window.onload=function(){
        doTTS();
    }
</script>
</body>
</html>
