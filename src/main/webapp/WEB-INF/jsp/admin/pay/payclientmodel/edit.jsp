<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>上游模块</title>
    <link rel="stylesheet" href="${context}/layui/css/layui.css">

    <link href="${context}/lib/codemirror/lib/codemirror.css" rel="stylesheet" type="text/css">
    <link href="${context}/lib/codemirror/theme/monokai.css" rel="stylesheet" type="text/css">
    <link href="${context}/lib/codemirror/addon/display/fullscreen.css" rel="stylesheet" type="text/css">
    <link href="${context}/lib/codemirror/addon/hint/show-hint.css" rel="stylesheet" type="text/css">

    <style>
        .admin-main {
            margin: 15px;
        }

        .span-red {
            color: red;
        }

        .admin-search-title {
            font-size: 10pt !important;
            font-weight: bold !important;
        }
    </style>
    <!--[if lt IE 9]>
      <script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
      <script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<div class="admin-main">
    <form id="editForm" class="layui-form">


        <div class="layui-tab" lay-filter="mytabs">
            <ul class="layui-tab-title">
                <li class="layui-this" lay-id="0">基础配置信息</li>
                <li lay-id="6">通用导入包</li>
                <li lay-id="7">创建交易订单</li>

                <li lay-id="1">处理订单接口数据</li>
                <li lay-id="2">订单通知回调</li>
                <li lay-id="3">测试下单</li>
                <li lay-id="4">创建代付订单</li>
                <li lay-id="5">处理代付接口数据</li>
                <li lay-id="8">查单参数</li>
                <li lay-id="9">查单结果处理</li>
            </ul>
            <div class="layui-tab-content">

                <div class="layui-tab-item layui-show">

                    <input type="hidden" name="id" value="${result.id}">

                    <div class="layui-form-item">
                        <label class="layui-form-label"><span class="span-red">*</span>名称</label>
                        <div class="layui-input-block">
                            <input type="text" name="name" id="name" lay-verify="required" lay-verType="tips"
                                   placeholder="名称" autocomplete="off" class="layui-input" value="${result.name}"/>
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <label class="layui-form-label"><span class="span-red">*</span>键名</label>
                        <div class="layui-input-block">
                            <input type="text" name="keyname" id="keyname" lay-verify="required" lay-verType="tips"
                                   placeholder="键名" autocomplete="off" class="layui-input" value="${result.keyname}"/>
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <label class="layui-form-label"><span class="span-red">*</span>处理成功返回的字符串</label>
                        <div class="layui-input-block">
                            <input type="text" name="notifystr" id="notifystr" lay-verify="required" lay-verType="tips"
                                   placeholder="处理成功返回的字符串" autocomplete="off" class="layui-input"
                                   value="${result.notifystr}"/>
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <label class="layui-form-label"><span class="span-red">*</span>状态</label>
                        <div class="layui-input-block">
                            <select name="status" id="status" lay-verify="required">
                                <option value="1">启用</option>
                                <option value="0">禁用</option>
                            </select>
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <label class="layui-form-label"><span class="span-red">*</span>请求类型</label>
                        <div class="layui-input-block">
                            <select name="reqType" id="reqType" lay-verify="required">
                                <option value="0">普通表单</option>
                                <option value="1">JSON</option>
                            </select>
                        </div>
                    </div>

                </div>


                <div class="layui-tab-item">

                    <div class="layui-form-item">
                        <label class="layui-form-label">导入的包</label>
                        <div class="layui-input-block">
                            <div style="color:red;">此处导入的包用于所有处理程序</div>
                            <textarea name="reqpage" style="width:100%;height:100px;" id="reqpage"></textarea>
                        </div>
                    </div>

                </div>


                <div class="layui-tab-item">
                    <!--
                        <div class="layui-form-item">
                            <label class="layui-form-label">请求程序导入的包</label>
                            <div class="layui-input-block">
                                <div style="color:red;">此处导入的包用于生成请求参数的程序</div>
                                <textarea name="reqpage" style="width:100%;height:100px;" id="reqpage"></textarea>
                            </div>
                        </div>
                     -->
                    <div class="layui-form-item">
                        <label class="layui-form-label">请求参数生成程序</label>
                        <div class="layui-input-block">
                            <div style="color:red;">请在此处完成签名等操作,返回OrderPreInfo,可使用的参数
                                id：通道ID,merchantNo:商户号(String),merchantMy:商户秘钥(String),channelParams:通道参数(Map),sn:订单号(String),amount:金额(BigDecimal),domain:请求使用的域名(String)。
                                <br/><br/></div>
                            <div style="color:green;">OrderPreInfo
                                createReqParams(id,merchantNo,merchantMy,channelParams,sn,amount,domain){
                            </div>
                            <textarea name="reqprogram" style="width:100%;height:200px;" id="reqprogram"></textarea>
                            <div style="color:red;">}</div>
                        </div>
                    </div>

                </div>

                <div class="layui-tab-item">
                    <!--
                    <div class="layui-form-item">
                        <label class="layui-form-label">下单返回数据处理程序导入的包</label>
                        <div class="layui-input-block">
                            <div style="color:red;">此处导入的包用于下单返回数据处理程序</div>
                            <textarea name="rsppage" style="width:100%;height:100px;" id="rsppage">import com.city.city_collector.channel.bean.OrderInfo;</textarea>
                        </div>
                    </div>
                     -->
                    <div class="layui-form-item">
                        <label class="layui-form-label">下单返回数据处理程序</label>
                        <div class="layui-input-block">
                            <div style="color:red;">请在此处完成下单处理程序，需返回一个OrderInfo对象或者null,null表示下单失败
                                <br/><br/></div>
                            <div style="color:green;">OrderInfo
                                dealOrderData(data,clientId,clientNo,clientName,clientChannelId){
                            </div>
                            <textarea name="rspprogram" style="width:100%;height:200px;" id="rspprogram"></textarea>
                            <div style="color:red;">}</div>
                        </div>
                    </div>

                </div>

                <div class="layui-tab-item">
                    <!--
                    <div class="layui-form-item">
                        <label class="layui-form-label">回调处理程序导入的包</label>
                        <div class="layui-input-block">
                            <div style="color:red;">此处导入的包用于回调处理程序</div>
                            <textarea name="ntpage" style="width:100%;height:100px;" id="ntpage">import com.city.city_collector.channel.bean.NotifyInfo;</textarea>
                        </div>
                    </div>
                     -->
                    <div class="layui-form-item">
                        <label class="layui-form-label">回调处理程序</label>
                        <div class="layui-input-block">
                            <div style="color:red;">请在此处完成回调处理程序，需返回一个NotifyInfo对象或者null,null表示回调处理失败
                                <br/><br/></div>
                            <div style="color:green;">NotifyInfo notifyData(params,merchantNo,merchantMy,channelParams,body){
                            </div>
                            <textarea name="ntprogram" style="width:100%;height:200px;" id="ntprogram"></textarea>
                            <div style="color:red;">}</div>
                        </div>
                    </div>

                </div>
                <div class="layui-tab-item">
                    <!--
                    <div class="layui-form-item">
                        <label class="layui-form-label">请求程序导入的包</label>
                        <div class="layui-input-block">
                            <div style="color:red;">此处导入的包用于生成请求参数的程序</div>
                            <textarea name="treqpage" style="width:100%;height:100px;" id="treqpage">import com.city.city_collector.channel.bean.ApiInfo;</textarea>
                        </div>
                    </div>
                    -->
                    <div class="layui-form-item">
                        <label class="layui-form-label">请求参数生成程序</label>
                        <div class="layui-input-block">
                            <div style="color:red;">请在此处完成签名等操作,可使用的参数
                                id：通道ID,merchantNo:商户号(String),merchantMy:商户秘钥(String),channelParams:通道参数(Map),sn:订单号(String),amount:金额(BigDecimal),domain:请求使用的域名(String)。需返回一个ApiInfo对象
                                <br/><br/></div>
                            <div style="color:green;">ApiInfo
                                createReqParams(id,merchantNo,merchantMy,channelParams,sn,amount,domain){
                            </div>
                            <textarea name="treqprogram" style="width:100%;height:200px;" id="treqprogram"></textarea>
                            <div style="color:red;">}</div>
                        </div>
                    </div>

                </div>
                <div class="layui-tab-item">
                    <!--
                    <div class="layui-form-item">
                        <label class="layui-form-label">请求程序导入的包</label>
                        <div class="layui-input-block">
                            <div style="color:red;">此处导入的包用于生成请求参数的程序</div>
                            <textarea name="payreqpage" style="width:100%;height:100px;" id="payreqpage"></textarea>
                        </div>
                    </div>
                    -->
                    <div class="layui-form-item">
                        <label class="layui-form-label">请求参数生成程序</label>
                        <div class="layui-input-block">
                            <div style="color:red;">请在此处完成签名等操作,返回Map,可使用的参数 PayCash cash,SysUser client,String
                                domain,String urlcash,String keyname(键名)。
                                <br/><br/></div>
                            <div style="color:green;">Map createReqParams(cash,client,domain,urlcash,keyname){
                            </div>
                            <textarea name="payreqprogram" style="width:100%;height:200px;"
                                      id="payreqprogram"></textarea>
                            <div style="color:red;">}</div>
                        </div>
                    </div>

                </div>
                <div class="layui-tab-item">
                    <!--
                    <div class="layui-form-item">
                        <label class="layui-form-label">代付返回数据处理程序导入的包</label>
                        <div class="layui-input-block">
                            <div style="color:red;">此处导入的包用于代付返回数据处理程序</div>
                            <textarea name="payrsppage" style="width:100%;height:100px;" id="payrsppage">import com.city.city_collector.channel.bean.CashInfo;</textarea>
                        </div>
                    </div>
                     -->
                    <div class="layui-form-item">
                        <label class="layui-form-label">代付返回数据处理程序</label>
                        <div class="layui-input-block">
                            <div style="color:red;">请在此处完成代付处理程序，需返回一个CashInfo对象
                                <br/><br/></div>
                            <div style="color:green;">CashInfo dealOrderData(data){
                            </div>
                            <textarea name="payrspprogram" style="width:100%;height:200px;"
                                      id="payrspprogram"></textarea>
                            <div style="color:red;">}</div>
                        </div>
                    </div>

                </div>

                <div class="layui-tab-item">
                    <div class="layui-form-item">
                        <label class="layui-form-label">查单参数生成程序</label>
                        <div class="layui-input-block">
                            <div style="color:red;">请在此处完成参数生成程序，需返回一个QueryOrderInfo对象
                                <br/><br/></div>
                            <div style="color:green;">QueryOrderInfo
                                dealOrderData(order,clientChannel,client,channelParams){
                            </div>
                            <textarea name="queryreqprogram" style="width:100%;height:200px;"
                                      id="queryreqprogram"></textarea>
                            <div style="color:red;">}</div>
                        </div>
                    </div>

                </div>

                <div class="layui-tab-item">
                    <div class="layui-form-item">
                        <label class="layui-form-label">查单结果处理程序</label>
                        <div class="layui-input-block">
                            <div style="color:red;">请在此处完成查单结果处理程序，需返回一个QueryOrderResult对象
                                <br/><br/></div>
                            <div style="color:green;">QueryOrderResult
                                dealOrderData(data,order,clientChannel,client,channelParams){
                            </div>
                            <textarea name="queryrspprogram" style="width:100%;height:200px;"
                                      id="queryrspprogram"></textarea>
                            <div style="color:red;">}</div>
                        </div>
                    </div>

                </div>

            </div>

            <div class="layui-form-item">
                <label class="layui-form-label"><span class="span-red">*</span>签名</label>
                <div class="layui-input-block">
                    <input type="password" name="sign" id="sign" lay-verify="required" lay-verType="tips"
                           placeholder="签名" autocomplete="off" class="layui-input"/>
                </div>
            </div>
        </div>


        <div class="layui-form-item">
            <div class="layui-input-block">
                <button class="layui-btn" lay-submit lay-filter="edit">提交</button>
                <button type="reset" class="layui-btn layui-btn-primary">重置</button>
            </div>
        </div>
    </form>
</div>
<script src="${context}/js/jquery.min.js"></script>
<script src="${context}/layui/layui.js"></script>
<script src="${context}/js/common.js"></script>

<!-- 引入CodeMirror核心文件 -->
<script type="text/javascript" src="${context}/lib/codemirror/lib/codemirror.js"></script>

<!-- CodeMirror支持不同语言，根据需要引入JS文件 -->
<script type="text/javascript" src="${context}/lib/codemirror/mode/groovy/groovy.js"></script>

<!-- 下面分别为显示行数、括号匹配和全屏插件 -->
<script type="text/javascript" src="${context}/lib/codemirror/addon/selection/active-line.js"></script>
<script type="text/javascript" src="${context}/lib/codemirror/addon/edit/matchbrackets.js"></script>
<script type="text/javascript" src="${context}/lib/codemirror/addon/display/fullscreen.js"></script>
<script type="text/javascript" src="${context}/lib/codemirror/addon/hint/show-hint.js"></script>

<script>
    var lay_index;
    var path = "addSave";
    var addFlag = true;//是否为新增
    <c:if test="${error!=null}">
    alert('${error}');
    window.parent.refreshPage();
    var index = parent.layer.getFrameIndex(window.name);
    parent.layer.close(index);
    </c:if>


    $(function () {

        layui.use(['form', 'layer', 'element'], function () {
            var form = layui.form;
            var element = layui.element;

            element.on('tab(mytabs)', function () {
                var id = this.getAttribute('lay-id');
                if (id == 1) {
                    //rsppageEditor.refresh();
                    rspprogramEditor.refresh();
                } else if (id == 2) {
                    //ntpageEditor.refresh();
                    ntprogramEditor.refresh();
                } else if (id == 3) {
                    //treqpageEditor.refresh();
                    treqprogramEditor.refresh();
                } else if (id == 4) {
                    //payreqpageEditor.refresh();
                    payreqprogramEditor.refresh();
                } else if (id == 5) {
                    //payrsppageEditor.refresh();
                    payrspprogramEditor.refresh();
                } else if (id == 6) {
                    reqpageEditor.refresh();
                } else if (id == 7) {
                    reqprogramEditor.refresh();
                } else if (id == 8) {
                    queryreqprogramEditor.refresh();
                } else if (id == 9) {
                    queryrspprogramEditor.refresh();
                }
            });
            //自定义表单验证
            /**
             form.verify({
			imgnull:function(value,item){//验证图标是否为空
				var name = $("#image").val();
				if(!name){
					return "必须选择一个图标";
				}
			}
		});
             **/
            //按钮事件
            form.on('submit(edit)', function (data) {

                var reqpage = reqpageEditor.getValue();
                var reqprogram = reqprogramEditor.getValue();
                //var rsppage=rsppageEditor.getValue();
                var rspprogram = rspprogramEditor.getValue();
                //var ntpage=ntpageEditor.getValue();
                var ntprogram = ntprogramEditor.getValue();
                //var treqpage=treqpageEditor.getValue();
                var treqprogram = treqprogramEditor.getValue();
                //var payreqpage=payreqpageEditor.getValue();
                var payreqprogram = payreqprogramEditor.getValue();
                //var payrsppage=payrsppageEditor.getValue();
                var payrspprogram = payrspprogramEditor.getValue();

                var queryreqprogram = queryreqprogramEditor.getValue();

                var queryrspprogram = queryrspprogramEditor.getValue();

                var data = {
                    name: $("#name").val(),
                    keyname: $("#keyname").val(),
                    status: $("#status").val(),
                    reqType: $("#reqType").val(),
                    notifystr: $("#notifystr").val(),
                    sign: $("#sign").val(),
                    reqpage: reqpage,
                    reqprogram: reqprogram,
                    rsppage: '',
                    rspprogram: rspprogram,
                    ntpage: '',
                    ntprogram: ntprogram,
                    treqpage: '',
                    treqprogram: treqprogram,
                    payreqpage: '',
                    payreqprogram: payreqprogram,
                    payrsppage: '',
                    payrspprogram: payrspprogram,
                    queryreqprogram: queryreqprogram,
                    queryrspprogram: queryrspprogram
                };

                lay_index = layer.load(1, {
                    shade: [0.1, '#fff'] //0.1透明度的白色背景
                });
                $.ajax({
                    type: 'post',
                    url: path,
                    data: data,
                    dataType: "json",
                    cache: false,
                    success: function (data) {
                        layer.close(lay_index);
                        if (data.code == 200) {
                            window.parent.refreshPage(data.msg);
                        } else {
                            layer.msg(data.msg);
                        }
                    },
                    error: function (xhr, desc, err) {
                        layer.msg("数据请求失败:" + desc);
                        layer.close(lay_index);
                    }
                });
                return false;
            });
        });

        var reqpageEditor = CodeMirror.fromTextArea(document.getElementById("reqpage"), {
            lineNumbers: true,     // 显示行数
            indentUnit: 2,         // 缩进单位为2
            styleActiveLine: true, // 当前行背景高亮
            matchBrackets: true,   // 括号匹配
            mode: 'groovy',     // groovy
            lineWrapping: true,    // 自动换行
            theme: 'monokai'      // 使用monokai模版
        });
        reqpageEditor.setSize('100%', '300px');

        var reqprogramEditor = CodeMirror.fromTextArea(document.getElementById("reqprogram"), {
            lineNumbers: true,     // 显示行数
            indentUnit: 2,         // 缩进单位为2
            styleActiveLine: true, // 当前行背景高亮
            matchBrackets: true,   // 括号匹配
            mode: 'groovy',     // groovy
            lineWrapping: true,    // 自动换行
            theme: 'monokai'      // 使用monokai模版
        });
        reqprogramEditor.setSize('100%', '300px');

        /**
         var rsppageEditor = CodeMirror.fromTextArea(document.getElementById("rsppage"), {
		lineNumbers: true,     // 显示行数
		indentUnit: 2,         // 缩进单位为2
		styleActiveLine: true, // 当前行背景高亮
		matchBrackets: true,   // 括号匹配
		mode: 'groovy',     // groovy
		lineWrapping: true,    // 自动换行
		theme: 'monokai'      // 使用monokai模版
	});
         rsppageEditor.setSize('100%','100px');
         **/

        var rspprogramEditor = CodeMirror.fromTextArea(document.getElementById("rspprogram"), {
            lineNumbers: true,     // 显示行数
            indentUnit: 2,         // 缩进单位为2
            styleActiveLine: true, // 当前行背景高亮
            matchBrackets: true,   // 括号匹配
            mode: 'groovy',     // groovy
            lineWrapping: true,    // 自动换行
            theme: 'monokai'      // 使用monokai模版
        });
        rspprogramEditor.setSize('100%', '300px');

        /**
         var ntpageEditor = CodeMirror.fromTextArea(document.getElementById("ntpage"), {
		lineNumbers: true,     // 显示行数
		indentUnit: 2,         // 缩进单位为2
		styleActiveLine: true, // 当前行背景高亮
		matchBrackets: true,   // 括号匹配
		mode: 'groovy',     // groovy
		lineWrapping: true,    // 自动换行
		theme: 'monokai'      // 使用monokai模版
	});
         ntpageEditor.setSize('100%','100px');
         **/
        var ntprogramEditor = CodeMirror.fromTextArea(document.getElementById("ntprogram"), {
            lineNumbers: true,     // 显示行数
            indentUnit: 2,         // 缩进单位为2
            styleActiveLine: true, // 当前行背景高亮
            matchBrackets: true,   // 括号匹配
            mode: 'groovy',     // groovy
            lineWrapping: true,    // 自动换行
            theme: 'monokai'      // 使用monokai模版
        });
        ntprogramEditor.setSize('100%', '300px');

        /**
         var treqpageEditor = CodeMirror.fromTextArea(document.getElementById("treqpage"), {
		lineNumbers: true,     // 显示行数
		indentUnit: 2,         // 缩进单位为2
		styleActiveLine: true, // 当前行背景高亮
		matchBrackets: true,   // 括号匹配
		mode: 'groovy',     // groovy
		lineWrapping: true,    // 自动换行
		theme: 'monokai'      // 使用monokai模版
	});
         treqpageEditor.setSize('100%','100px');
         **/
        var treqprogramEditor = CodeMirror.fromTextArea(document.getElementById("treqprogram"), {
            lineNumbers: true,     // 显示行数
            indentUnit: 2,         // 缩进单位为2
            styleActiveLine: true, // 当前行背景高亮
            matchBrackets: true,   // 括号匹配
            mode: 'groovy',     // groovy
            lineWrapping: true,    // 自动换行
            theme: 'monokai'      // 使用monokai模版
        });
        treqprogramEditor.setSize('100%', '300px');

        /**
         var payreqpageEditor = CodeMirror.fromTextArea(document.getElementById("payreqpage"), {
		lineNumbers: true,     // 显示行数
		indentUnit: 2,         // 缩进单位为2
		styleActiveLine: true, // 当前行背景高亮
		matchBrackets: true,   // 括号匹配
		mode: 'groovy',     // groovy
		lineWrapping: true,    // 自动换行
		theme: 'monokai'      // 使用monokai模版
	});
         payreqpageEditor.setSize('100%','100px');
         **/

        var payreqprogramEditor = CodeMirror.fromTextArea(document.getElementById("payreqprogram"), {
            lineNumbers: true,     // 显示行数
            indentUnit: 2,         // 缩进单位为2
            styleActiveLine: true, // 当前行背景高亮
            matchBrackets: true,   // 括号匹配
            mode: 'groovy',     // groovy
            lineWrapping: true,    // 自动换行
            theme: 'monokai'      // 使用monokai模版
        });
        payreqprogramEditor.setSize('100%', '300px');

        /**
         var payrsppageEditor = CodeMirror.fromTextArea(document.getElementById("payrsppage"), {
		lineNumbers: true,     // 显示行数
		indentUnit: 2,         // 缩进单位为2
		styleActiveLine: true, // 当前行背景高亮
		matchBrackets: true,   // 括号匹配
		mode: 'groovy',     // groovy
		lineWrapping: true,    // 自动换行
		theme: 'monokai'      // 使用monokai模版
	});
         payrsppageEditor.setSize('100%','100px');
         **/

        var payrspprogramEditor = CodeMirror.fromTextArea(document.getElementById("payrspprogram"), {
            lineNumbers: true,     // 显示行数
            indentUnit: 2,         // 缩进单位为2
            styleActiveLine: true, // 当前行背景高亮
            matchBrackets: true,   // 括号匹配
            mode: 'groovy',     // groovy
            lineWrapping: true,    // 自动换行
            theme: 'monokai'      // 使用monokai模版
        });
        payrspprogramEditor.setSize('100%', '300px');

        var queryreqprogramEditor = CodeMirror.fromTextArea(document.getElementById("queryreqprogram"), {
            lineNumbers: true,     // 显示行数
            indentUnit: 2,         // 缩进单位为2
            styleActiveLine: true, // 当前行背景高亮
            matchBrackets: true,   // 括号匹配
            mode: 'groovy',     // groovy
            lineWrapping: true,    // 自动换行
            theme: 'monokai'      // 使用monokai模版
        });
        queryreqprogramEditor.setSize('100%', '300px');

        var queryrspprogramEditor = CodeMirror.fromTextArea(document.getElementById("queryrspprogram"), {
            lineNumbers: true,     // 显示行数
            indentUnit: 2,         // 缩进单位为2
            styleActiveLine: true, // 当前行背景高亮
            matchBrackets: true,   // 括号匹配
            mode: 'groovy',     // groovy
            lineWrapping: true,    // 自动换行
            theme: 'monokai'      // 使用monokai模版
        });
        queryrspprogramEditor.setSize('100%', '300px');
    });
</script>
</body>
</html>
