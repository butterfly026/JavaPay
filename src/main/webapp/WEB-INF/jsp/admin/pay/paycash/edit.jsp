<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>编辑商户提现</title>
    <link rel="stylesheet" href="${context}/layui/css/layui.css">
    <style>
        .admin-main {
            margin: 15px;
        }

        .span-red {
            color: red;
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
        <input type="hidden" name="id" value="${result.id}">

        <div style="color:red;width:90%;padding-left:20px;">余额：<span
                style="color:green;">${money} ${sysconfig.currency }</span>，冻结金额：<span
                style="color:green;">${frozenMoney} ${sysconfig.currency }</span>，预估可提现金额：<span
                style="color:green;">${amount} ${sysconfig.currency }</span>，最低提现额度：<span style="color:green;"
                                                                                          id="zdtx">${minCommission}</span> ${sysconfig.currency }
        </div>
        <div style="color:red;width:90%;padding-left:20px;">手续费扣减模式：<span style="color:green;">外扣</span>。如果尚未添加提现账号，请去提现账号管理添加银行卡/UPI账号！如果未设置支付密码，请去首页设置支付密码！
        </div>
        <div style="color:red;width:90%;padding-left:20px;margin-bottom:10px;">当前选择模式为：<span style="color:green;"
                                                                                             id="modelName">下发</span>。银行卡提现费率：<span
                id="bankMoney"></span><c:if test="${sysconfig.cashMode==0}">，UPI提现费率：<span id="upiMoney"></span></c:if>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label"><span class="span-red">*</span>提现类型</label>
            <div class="layui-input-block">
                <select name="channelType" id="channelType" lay-verify="required" lay-search lay-filter="ct_type">
                    <option value="0">下发</option>
                    <option value="1">代付</option>
                </select>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label"><span class="span-red">*</span>账号类型</label>
            <div class="layui-input-block">
                <select name="btype" id="btype" lay-verify="required" lay-search lay-filter="up_type">
                    <option value="0"
                            <c:if test="${result.btype==0 }">selected</c:if> >银行卡账号
                    </option>
                    <!--
                    <option value="1"
                            <c:if test="${result.btype==1 }">selected</c:if> >UPI账号
                    </option>
                    -->
                    <option value="2"
                            <c:if test="${result.btype==2 }">selected</c:if> >USDT
                    </option>
                </select>
            </div>
        </div>

        <div class="layui-form-item" id="ownernamediv">
            <label class="layui-form-label"><span class="span-red">*</span>姓名</label>
            <div class="layui-input-block">
                <input type="text" name="ownername" id="ownername"  lay-vertype="tips" placeholder="姓名" autocomplete="off" class="layui-input" onchange="">
            </div>
        </div>

        <div class="layui-form-item" id="carnoddiv">
            <label class="layui-form-label" style="margin-top:-10px"><span class="span-red">*</span>卡号/<p>TRC20</p></label>
            <div class="layui-input-block">
                <input type="text" name="cardNo" id="cardNo" lay-verify="required" lay-vertype="tips" placeholder="卡号/TRC20" autocomplete="off" class="layui-input" onchange="">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label"><span class="span-red">*</span>开户行</label>
            <div class="layui-input-block">
                <select id="bankCode" name="bankCode" lay-verify="required" lay-search>
                    <c:forEach items="${bankCodeList }" var="c">
                        <option value="${c.code}"
                                <c:if test='${result.bankCode==c.code}'>selected</c:if> >${c.name}</option>
                    </c:forEach>
                </select>
            </div>
        </div>        

        <!--
        <div class="layui-form-item" id="ptmdiv" style="display:none;">
            <label class="layui-form-label"><span class="span-red">*</span>UPI账号</label>
            <div class="layui-input-block">
                <select name="cardId1" lay-verify="required" lay-search>
                    <c:forEach items="${ptmList }" var="c">
                        <option value="${c.id}">${c.cardNo}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
        -->
        <div class="layui-form-item">
            <label class="layui-form-label"><span class="span-red">*</span>提现金额</label>
            <div class="layui-input-block">
                <input type="number" name="money" id="money" lay-verify="required|number" lay-verType="tips"
                       placeholder="提现金额" autocomplete="off" class="layui-input" onchange="changeMoney();"/>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label"><span class="span-red">*</span>支付密码</label>
            <div class="layui-input-block">
                <input type="password" name="payPwd" id="payPwd" lay-verify="required" lay-verType="tips"
                       placeholder="支付密码" autocomplete="off" class="layui-input"/>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">实际扣减</label>
            <div class="layui-input-block">
                <span style="color:red;" id="realMoney"></span>
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
<script src="${context}/layui/layui.js?${s_vs}"></script>
<script src="${context}/js/common.js?${s_vs}"></script>
<script>
    var lay_index;
    var path = "addSave";
    var addFlag = true;//是否为新增
    <c:if test="${result!=null}">
    path = "editSave";
    addFlag = false;
    </c:if>
    <c:if test="${error!=null}">
    alert('${error}');
    window.parent.refreshPage();
    var index = parent.layer.getFrameIndex(window.name);
    parent.layer.close(index);
    </c:if>


    var cc =${cashConfig};
    var mc = "${minCommission==null?0:minCommission}";
    var dfmc = "${dfminCommission==null?0:dfminCommission}";

    /**
     var cmode=${cmode};
     var pcmode=${pcmode};
     var ccommission=${ccommission};
     var pccommission=${pccommission};
     **/
    $(function () {
        if ($("#btype").val() != '0') {
            $("#bankdiv").hide();
            $("#ptmdiv").show();
        }

        updateInfo();

        layui.use(['form', 'layer'], function () {
            var form = layui.form;

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

            form.on('select(up_type)', function (data) {
                if (data.value == '0') {
                    $("#bankdiv").show();
                    $("#ptmdiv").hide();
                    
                } else {
                    $('#bankCode').siblings("div.layui-form-select").find('dl').find('[lay-value="USDT"]').click();
                    $("#bankdiv").hide();
                    $("#ptmdiv").show();
                }
                updateInfo();
                changeMoney();
            });

            form.on('select(ct_type)', function (data) {
                updateInfo();
                changeMoney();
            });

            //按钮事件
            form.on('submit(edit)', function (data) {
                lay_index = layer.load(1, {
                    shade: [0.1, '#fff'] //0.1透明度的白色背景
                });
                $.ajax({
                    type: 'post',
                    url: path,
                    data: getFormJsonData("#editForm"),
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

            initSelectAll();
        });


    });

    function updateInfo() {
        var btype = $("#btype").val();
        var dvalue = $("#channelType").val();
        var bname = "下发";
        var bankData = cc.bankRatio;
        var upiData = cc.upiRatio;
        if (dvalue == '0') {
            bname = "下发";
            $("#modelName").html("下发");

            bankData = cc.bankRatio;
            upiData = cc.upiRatio;
            $("#zdtx").html(mc);
        } else {
            bname = "代付";
            $("#modelName").html("代付");

            bankData = cc.dfbankRatio;
            upiData = cc.dfupiRatio;

            $("#zdtx").html(dfmc);
        }

        $("#bankMoney").html("<span style=\"color:green;\">" + bname + "金额 x " + bankData.ratioCommission + "% + " + bankData.commission + "</span>");
        $("#upiMoney").html("<span style=\"color:green;\">" + bname + "金额 x " + upiData.ratioCommission + "% + " + upiData.commission + "</span>");
    }


    function changeMoney() {
        var money = $("#money").val();
        if (money == "") money = 0;
        money = parseFloat(money);

        var btype = $("#btype").val();
        var dvalue = $("#channelType").val();
        var bname = "下发";

        var data = cc.bankRatio;
        if (dvalue == '0') {
            bname = "下发";

            if (btype == "0") {
                data = cc.bankRatio;
            } else {
                data = cc.upiRatio;
            }
        } else {
            bname = "代付";

            if (btype == "0") {
                data = cc.dfbankRatio;
            } else {
                data = cc.dfupiRatio;
            }
        }

        var rmoney = 0;
        var mtxt = money + " [ " + bname + "金额 ] + (" + money + " [ " + bname + "金额 ] x " + data.ratioCommission + "% + " + data.commission + ") [ 手续费 ] = " + (money + (money * data.ratioCommission / 100 + data.commission)).toFixed(2);

        $("#realMoney").html(mtxt);
    }

</script>
</body>
</html>
