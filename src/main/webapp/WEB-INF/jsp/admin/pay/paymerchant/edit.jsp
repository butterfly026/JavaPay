<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>编辑商户管理</title>
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
        <!--
		<div class="layui-form-item">
			<label class="layui-form-label"><span class="span-red">*</span>名称</label>
			<div class="layui-input-block">
				<input type="text" name="name" id="name" lay-verify="required" lay-verType="tips" placeholder="名称" autocomplete="off" class="layui-input"  value="${result.name}" />
			</div>
		</div>
	-->
        <div class="layui-form-item">
            <label class="layui-form-label">代理</label>
            <div class="layui-input-block">
                <select name="proxyId" lay-search>
                    <option value="">无</option>
                    <c:forEach items="${proxyList }" var="c">
                        <option value="${c.id}" <c:if test='${result.proxyId==c.id}'>selected</c:if>>${c.name}</option>
                    </c:forEach>
                </select>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label"><span class="span-red">*</span>账号</label>
            <div class="layui-input-block">
                <input type="text" name="username" id="username" lay-verify="required" lay-verType="tips"
                       placeholder="账号" autocomplete="off" class="layui-input" value="${result.username}"
                       <c:if test="${result!=null }">disabled</c:if> />
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label"><span class="span-red">*</span>商户名</label>
            <div class="layui-input-inline">
                <input type="text" name="name" id="name" lay-verify="required" lay-verType="tips" placeholder="商户名"
                       autocomplete="off" class="layui-input" value="${result.name}"/>
            </div>
            <div class="layui-form-mid layui-word-aux">
                <button type="button" class="layui-btn layui-btn-xs" onclick="createName()">
                    自动生成
                </button>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label"><span class="span-red">*</span>商户状态</label>
            <div class="layui-input-block">
                <select name="status" lay-verify="required">
                    <option value="1" <c:if test='${result.status=="1"}'>selected</c:if>>启用</option>
                    <option value="0" <c:if test='${result.status=="0"}'>selected</c:if>>禁用</option>
                </select>
            </div>
        </div>

        <!--
		<div class="layui-form-item">
			<label class="layui-form-label"><span class="span-red">*</span>API提现</label>
			<div class="layui-input-block">
				<select name="api" lay-verify="required">
					<option value="1"  <c:if test='${result.api==1}'>selected</c:if>>启用</option>
					<option value="0"  <c:if test='${result.api==0}'>selected</c:if>>禁用</option>

				</select>
			</div>
		</div>
		-->

        <div class="layui-tab" lay-filter="mytabs">
            <ul class="layui-tab-title">
                <li class="layui-this" lay-id="0">下发手续费</li>
                <li lay-id="1">代付手续费</li>

            </ul>
            <div class="layui-tab-content">
                <div class="layui-tab-item layui-show">

                    <div class="layui-form-item">
                        <label class="layui-form-label"><span class="span-red">*</span>最低提现金额(下发)</label>
                        <div class="layui-input-block">
                            <input type="text" name="minCommission" id="minCommission" lay-verify="required|number"
                                   lay-verType="tips" placeholder="最低提现金额" autocomplete="off" class="layui-input"
                                   value="${result==null?10000:result.minCommission}"/>
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <label class="layui-form-label"><span class="span-red">*</span>商户手续费(银行卡)</label>
                        <div class="layui-input-inline">
                            <input type="text" name="ratioCommission" id="ratioCommission" lay-verify="required|number"
                                   lay-verType="tips" placeholder="下发手续费(%)" autocomplete="off" class="layui-input"
                                   value="${bankRatio==null?0:bankRatio.ratioCommission}"/>
                        </div>
                        <div class="layui-form-mid layui-word-aux">
                            <span style="color:red;">% x 下发金额 + </span>
                        </div>
                        <div class="layui-input-inline">
                            <input type="text" name="commission" id="commission" lay-verify="required|number"
                                   lay-verType="tips" placeholder="下发手续费(固定金额)" autocomplete="off" class="layui-input"
                                   value="${bankRatio==null?5:bankRatio.commission}"/>
                        </div>
                        <div class="layui-form-mid layui-word-aux">
                            <span style="color:red;">${sysconfig.currency }</span>
                        </div>

                    </div>

                    <div class="layui-form-item">
                        <label class="layui-form-label"><span class="span-red">*</span>代理佣金(银行卡)</label>
                        <div class="layui-input-inline">
                            <input type="text" name="proxyRatioCommission" id="proxyRatioCommission"
                                   lay-verify="required|number" lay-verType="tips" placeholder="下发手续费(%)"
                                   autocomplete="off" class="layui-input"
                                   value="${bankRatio==null?0:bankRatio.proxyRatioCommission}"/>
                        </div>
                        <div class="layui-form-mid layui-word-aux">
                            <span style="color:red;">% x 下发金额 + </span>
                        </div>
                        <div class="layui-input-inline">
                            <input type="text" name="proxyCommission" id="proxyCommission" lay-verify="required|number"
                                   lay-verType="tips" placeholder="下发手续费(固定金额)" autocomplete="off" class="layui-input"
                                   value="${bankRatio==null?0:bankRatio.proxyCommission}"/>
                        </div>
                        <div class="layui-form-mid layui-word-aux">
                            <span style="color:red;">${sysconfig.currency }</span>
                        </div>

                    </div>

                    <div class="layui-form-item">
                        <label class="layui-form-label"><span class="span-red">*</span>商户手续费(UPI)</label>
                        <div class="layui-input-inline">
                            <input type="text" name="upiratioCommission" id="upiratioCommission"
                                   lay-verify="required|number" lay-verType="tips" placeholder="下发手续费(%)"
                                   autocomplete="off" class="layui-input"
                                   value="${upiRatio==null?0:upiRatio.ratioCommission}"/>
                        </div>
                        <div class="layui-form-mid layui-word-aux">
                            <span style="color:red;">% x 下发金额 + </span>
                        </div>
                        <div class="layui-input-inline">
                            <input type="text" name="upicommission" id="upicommission" lay-verify="required|number"
                                   lay-verType="tips" placeholder="下发手续费(固定金额)" autocomplete="off" class="layui-input"
                                   value="${upiRatio==null?0:upiRatio.commission}"/>
                        </div>
                        <div class="layui-form-mid layui-word-aux">
                            <span style="color:red;">${sysconfig.currency }</span>
                        </div>

                    </div>

                    <div class="layui-form-item">
                        <label class="layui-form-label"><span class="span-red">*</span>代理佣金(UPI)</label>
                        <div class="layui-input-inline">
                            <input type="text" name="upiproxyRatioCommission" id="upiproxyRatioCommission"
                                   lay-verify="required|number" lay-verType="tips" placeholder="下发手续费(%)"
                                   autocomplete="off" class="layui-input"
                                   value="${upiRatio==null?0:upiRatio.proxyRatioCommission}"/>
                        </div>
                        <div class="layui-form-mid layui-word-aux">
                            <span style="color:red;">% x 下发金额 + </span>
                        </div>
                        <div class="layui-input-inline">
                            <input type="text" name="upiproxyCommission" id="upiproxyCommission"
                                   lay-verify="required|number" lay-verType="tips" placeholder="下发手续费(固定金额)"
                                   autocomplete="off" class="layui-input"
                                   value="${upiRatio==null?0:upiRatio.proxyCommission}"/>
                        </div>
                        <div class="layui-form-mid layui-word-aux">
                            <span style="color:red;">${sysconfig.currency }</span>
                        </div>

                    </div>

                    <div class="layui-form-item">
                        <label class="layui-form-label"><span class="span-red">*</span>商户手续费(USDT)</label>
                        <div class="layui-input-inline">
                            <input type="text" name="usdtratioCommission" id="usdtratioCommission"
                                   lay-verify="required|number" lay-verType="tips" placeholder="下发手续费(%)"
                                   autocomplete="off" class="layui-input"
                                   value="${usdtRatio==null?0:usdtRatio.ratioCommission}"/>
                        </div>
                        <div class="layui-form-mid layui-word-aux">
                            <span style="color:red;">% x 下发金额 + </span>
                        </div>
                        <div class="layui-input-inline">
                            <input type="text" name="usdtcommission" id="usdtcommission" lay-verify="required|number"
                                   lay-verType="tips" placeholder="下发手续费(固定金额)" autocomplete="off" class="layui-input"
                                   value="${usdtRatio==null?0:usdtRatio.commission}"/>
                        </div>
                        <div class="layui-form-mid layui-word-aux">
                            <span style="color:red;">${sysconfig.currency }</span>
                        </div>

                    </div>

                    <div class="layui-form-item">
                        <label class="layui-form-label"><span class="span-red">*</span>代理佣金(USDT)</label>
                        <div class="layui-input-inline">
                            <input type="text" name="usdtproxyRatioCommission" id="usdtproxyRatioCommission"
                                   lay-verify="required|number" lay-verType="tips" placeholder="下发手续费(%)"
                                   autocomplete="off" class="layui-input"
                                   value="${usdtRatio==null?0:usdtRatio.proxyRatioCommission}"/>
                        </div>
                        <div class="layui-form-mid layui-word-aux">
                            <span style="color:red;">% x 下发金额 + </span>
                        </div>
                        <div class="layui-input-inline">
                            <input type="text" name="usdtproxyCommission" id="usdtproxyCommission"
                                   lay-verify="required|number" lay-verType="tips" placeholder="下发手续费(固定金额)"
                                   autocomplete="off" class="layui-input"
                                   value="${usdtRatio==null?0:usdtRatio.proxyCommission}"/>
                        </div>
                        <div class="layui-form-mid layui-word-aux">
                            <span style="color:red;">${sysconfig.currency }</span>
                        </div>

                    </div>


                </div>
                <div class="layui-tab-item">

                    <div class="layui-form-item">
                        <label class="layui-form-label"><span class="span-red">*</span>最低提现金额(代付)</label>
                        <div class="layui-input-block">
                            <input type="text" name="dfminCommission" id="dfminCommission" lay-verify="required|number"
                                   lay-verType="tips" placeholder="最低提现金额(代付)" autocomplete="off" class="layui-input"
                                   value="${result==null?500:result.dfminCommission}"/>
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <label class="layui-form-label"><span class="span-red">*</span>商户手续费(银行卡)</label>
                        <div class="layui-input-inline">
                            <input type="text" name="dfratioCommission" id="dfratioCommission"
                                   lay-verify="required|number" lay-verType="tips" placeholder="代付手续费(%)"
                                   autocomplete="off" class="layui-input"
                                   value="${dfbankRatio==null?0.8:dfbankRatio.ratioCommission}"/>
                        </div>
                        <div class="layui-form-mid layui-word-aux">
                            <span style="color:red;">% x 代付金额 + </span>
                        </div>
                        <div class="layui-input-inline">
                            <input type="text" name="dfcommission" id="dfcommission" lay-verify="required|number"
                                   lay-verType="tips" placeholder="代付手续费(固定金额)" autocomplete="off" class="layui-input"
                                   value="${dfbankRatio==null?2:dfbankRatio.commission}"/>
                        </div>
                        <div class="layui-form-mid layui-word-aux">
                            <span style="color:red;">${sysconfig.currency }</span>
                        </div>

                    </div>

                    <div class="layui-form-item">
                        <label class="layui-form-label"><span class="span-red">*</span>代理佣金(银行卡)</label>
                        <div class="layui-input-inline">
                            <input type="text" name="dfproxyRatioCommission" id="dfproxyRatioCommission"
                                   lay-verify="required|number" lay-verType="tips" placeholder="代付手续费(%)"
                                   autocomplete="off" class="layui-input"
                                   value="${dfbankRatio==null?0:dfbankRatio.proxyRatioCommission}"/>
                        </div>
                        <div class="layui-form-mid layui-word-aux">
                            <span style="color:red;">% x 代付金额 + </span>
                        </div>
                        <div class="layui-input-inline">
                            <input type="text" name="dfproxyCommission" id="dfproxyCommission"
                                   lay-verify="required|number" lay-verType="tips" placeholder="代付手续费(固定金额)"
                                   autocomplete="off" class="layui-input"
                                   value="${dfbankRatio==null?0:dfbankRatio.proxyCommission}"/>
                        </div>
                        <div class="layui-form-mid layui-word-aux">
                            <span style="color:red;">${sysconfig.currency }</span>
                        </div>

                    </div>

                    <div class="layui-form-item">
                        <label class="layui-form-label"><span class="span-red">*</span>商户手续费(UPI)</label>
                        <div class="layui-input-inline">
                            <input type="text" name="dfupiratioCommission" id="dfupiratioCommission"
                                   lay-verify="required|number" lay-verType="tips" placeholder="代付手续费(%)"
                                   autocomplete="off" class="layui-input"
                                   value="${dfupiRatio==null?0:dfupiRatio.ratioCommission}"/>
                        </div>
                        <div class="layui-form-mid layui-word-aux">
                            <span style="color:red;">% x 代付金额 + </span>
                        </div>
                        <div class="layui-input-inline">
                            <input type="text" name="dfupicommission" id="dfupicommission" lay-verify="required|number"
                                   lay-verType="tips" placeholder="代付手续费(固定金额)" autocomplete="off" class="layui-input"
                                   value="${dfupiRatio==null?0:dfupiRatio.commission}"/>
                        </div>
                        <div class="layui-form-mid layui-word-aux">
                            <span style="color:red;">${sysconfig.currency }</span>
                        </div>

                    </div>

                    <div class="layui-form-item">
                        <label class="layui-form-label"><span class="span-red">*</span>代理佣金(UPI)</label>
                        <div class="layui-input-inline">
                            <input type="text" name="dfupiproxyRatioCommission" id="dfupiproxyRatioCommission"
                                   lay-verify="required|number" lay-verType="tips" placeholder="代付手续费(%)"
                                   autocomplete="off" class="layui-input"
                                   value="${dfupiRatio==null?0:dfupiRatio.proxyRatioCommission}"/>
                        </div>
                        <div class="layui-form-mid layui-word-aux">
                            <span style="color:red;">% x 代付金额 + </span>
                        </div>
                        <div class="layui-input-inline">
                            <input type="text" name="dfupiproxyCommission" id="dfupiproxyCommission"
                                   lay-verify="required|number" lay-verType="tips" placeholder="代付手续费(固定金额)"
                                   autocomplete="off" class="layui-input"
                                   value="${dfupiRatio==null?0:dfupiRatio.proxyCommission}"/>
                        </div>
                        <div class="layui-form-mid layui-word-aux">
                            <span style="color:red;">${sysconfig.currency }</span>
                        </div>

                    </div>

                    <div class="layui-form-item">
                        <label class="layui-form-label"><span class="span-red">*</span>商户手续费(USDT)</label>
                        <div class="layui-input-inline">
                            <input type="text" name="dfusdtratioCommission" id="dfusdtratioCommission"
                                   lay-verify="required|number" lay-verType="tips" placeholder="代付手续费(%)"
                                   autocomplete="off" class="layui-input"
                                   value="${dfusdtRatio==null?0:dfusdtRatio.ratioCommission}"/>
                        </div>
                        <div class="layui-form-mid layui-word-aux">
                            <span style="color:red;">% x 代付金额 + </span>
                        </div>
                        <div class="layui-input-inline">
                            <input type="text" name="dfusdtcommission" id="dfusdtcommission"
                                   lay-verify="required|number" lay-verType="tips" placeholder="代付手续费(固定金额)"
                                   autocomplete="off" class="layui-input"
                                   value="${dfusdtRatio==null?0:dfusdtRatio.commission}"/>
                        </div>
                        <div class="layui-form-mid layui-word-aux">
                            <span style="color:red;">${sysconfig.currency }</span>
                        </div>

                    </div>

                    <div class="layui-form-item">
                        <label class="layui-form-label"><span class="span-red">*</span>代理佣金(USDT)</label>
                        <div class="layui-input-inline">
                            <input type="text" name="dfusdtproxyRatioCommission" id="dfusdtproxyRatioCommission"
                                   lay-verify="required|number" lay-verType="tips" placeholder="代付手续费(%)"
                                   autocomplete="off" class="layui-input"
                                   value="${dfusdtRatio==null?0:dfusdtRatio.proxyRatioCommission}"/>
                        </div>
                        <div class="layui-form-mid layui-word-aux">
                            <span style="color:red;">% x 代付金额 + </span>
                        </div>
                        <div class="layui-input-inline">
                            <input type="text" name="dfusdtproxyCommission" id="dfusdtproxyCommission"
                                   lay-verify="required|number" lay-verType="tips" placeholder="代付手续费(固定金额)"
                                   autocomplete="off" class="layui-input"
                                   value="${dfusdtRatio==null?0:dfusdtRatio.proxyCommission}"/>
                        </div>
                        <div class="layui-form-mid layui-word-aux">
                            <span style="color:red;">${sysconfig.currency }</span>
                        </div>

                    </div>

                </div>
            </div>
        </div>

        <!--
		<div class="layui-form-item">
			<label class="layui-form-label"><span class="span-red">*</span>提现手续费(银行卡)</label>
			<div class="layui-input-inline">
				<select name="cashMode" id="cashMode" lay-verify="required" lay-filter="cmode">
					<option value="1"  <c:if test='${result.cashMode==1}'>selected</c:if>>百分比(%)</option>
					<option value="0"  <c:if test='${result.cashMode==0}'>selected</c:if>>固定金额</option>
				</select>
			</div>
			<div class="layui-form-mid layui-word-aux" style="padding: 0px 0!important;">
				<input type="text" name="cashCommission" id="cashCommission" lay-verify="required|number" lay-verType="tips" placeholder="提现手续费(银行卡)" autocomplete="off" class="layui-input"  value="${result==null?5:result.cashCommission}" />
			</div>
			<div class="layui-form-mid layui-word-aux">
				<span style="color:red;" id="dw">%</span>
			</div>

		</div>

		<div class="layui-form-item">
			<label class="layui-form-label"><span class="span-red">*</span>代理佣金(银行卡)</label>
			<div class="layui-input-inline">
				<input type="text" name="mcashCommission" id="mcashCommission" lay-verify="required|number" lay-verType="tips" placeholder="代理收取手续费百分比(银行卡)" autocomplete="off" class="layui-input"  value="${result==null?0:result.mcashCommission}" />
			</div>
			<div class="layui-form-mid layui-word-aux">
				<span style="color:red;" id="mdw">%</span>
			</div>
		</div>

		<div class="layui-form-item">
			<label class="layui-form-label"><span class="span-red">*</span>提现手续费(UPI)</label>
			<div class="layui-input-inline">
				<select name="ptmcashMode" id="ptmcashMode" lay-verify="required" lay-filter="cmode_ptm">
					<option value="1"  <c:if test='${result.ptmcashMode==1}'>selected</c:if>>百分比(%)</option>
					<option value="0"  <c:if test='${result.ptmcashMode==0}'>selected</c:if>>固定金额</option>
				</select>
			</div>
			<div class="layui-form-mid layui-word-aux" style="padding: 0px 0!important;">
				<input type="text" name="ptmcashCommission" id="ptmcashCommission" lay-verify="required|number" lay-verType="tips" placeholder="提现手续费(UPI)" autocomplete="off" class="layui-input"  value="${result==null?5:result.ptmcashCommission}" />
			</div>
			<div class="layui-form-mid layui-word-aux">
				<span style="color:red;" id="ptmdw">%</span>
			</div>

		</div>

		<div class="layui-form-item">
			<label class="layui-form-label"><span class="span-red">*</span>代理佣金(UPI)</label>
			<div class="layui-input-inline">
				<input type="text" name="mptmcashCommission" id="mptmcashCommission" lay-verify="required|number" lay-verType="tips" placeholder="代理收取手续费百分比(UPI)" autocomplete="off" class="layui-input"  value="${result==null?0:result.mptmcashCommission}" />
			</div>
			<div class="layui-form-mid layui-word-aux">
				<span style="color:red;" id="mptmdw">%</span>
			</div>
		</div>

		<div class="layui-form-item">
			<label class="layui-form-label"><span class="span-red">*</span>提现手续费(USDT)</label>
			<div class="layui-input-inline">
				<select name="usdtcashMode" id="usdtcashMode" lay-verify="required" lay-filter="cmode_usdt">
					<option value="1"  <c:if test='${result.usdtcashMode==1}'>selected</c:if>>百分比(%)</option>
					<option value="0"  <c:if test='${result.usdtcashMode==0}'>selected</c:if>>固定金额</option>
				</select>
			</div>
			<div class="layui-form-mid layui-word-aux" style="padding: 0px 0!important;">
				<input type="text" name="usdtcashCommission" id="usdtcashCommission" lay-verify="required|number" lay-verType="tips" placeholder="提现手续费(USDT)" autocomplete="off" class="layui-input"  value="${result==null?5:result.usdtcashCommission}" />
			</div>
			<div class="layui-form-mid layui-word-aux">
				<span style="color:red;" id="usdtdw">%</span>
			</div>

		</div>

		<div class="layui-form-item">
			<label class="layui-form-label"><span class="span-red">*</span>代理佣金(USDT)</label>
			<div class="layui-input-inline">
				<input type="text" name="musdtcashCommission" id="musdtcashCommission" lay-verify="required|number" lay-verType="tips" placeholder="代理收取手续费(USDT)" autocomplete="off" class="layui-input"  value="${result==null?0:result.musdtcashCommission}" />
			</div>
			<div class="layui-form-mid layui-word-aux">
				<span style="color:red;" id="musdtdw">%</span>
			</div>
		</div>
		 -->
        <!--
		<div class="layui-form-item">
			<label class="layui-form-label"><span class="span-red">*</span>提现手续费</label>
			<div class="layui-input-block">
				<input type="text" name="cashCommission" id="cashCommission" lay-verify="required|number" lay-verType="tips" placeholder="提现手续费" autocomplete="off" class="layui-input"  value="${result==null?3:result.cashCommission}" />
			</div>
		</div>
		 -->


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


    $(function () {
        /**
         if($("#cashMode").val()=='0'){
		$("#dw").html("₹");
		$("#mdw").html("₹");
	}
         if($("#ptmcashMode").val()=='0'){
		$("#ptmdw").html("₹");
		$("#mptmdw").html("₹");
	}
         if($("#usdtcashMode").val()=='0'){
		$("#usdtdw").html("₹");
		$("#musdtdw").html("₹");
	}
         **/
        layui.use(['form', 'layer', 'element'], function () {
            var form = layui.form;
            var element = layui.element;

            /**
             form.on('select(cmode)', function(data){

			if(data.value=='0'){
				$("#dw").html("₹");
				$("#mdw").html("₹");
			}else{
				$("#dw").html("%");
				$("#mdw").html("%");
			}
		});
             form.on('select(cmode_ptm)', function(data){

			if(data.value=='0'){
				$("#ptmdw").html("₹");
				$("#mptmdw").html("₹");
			}else{
				$("#ptmdw").html("%");
				$("#mptmdw").html("%");
			}
		});

             form.on('select(cmode_usdt)', function(data){

			if(data.value=='0'){
				$("#usdtdw").html("₹");
				$("#musdtdw").html("₹");
			}else{
				$("#usdtdw").html("%");
				$("#musdtdw").html("%");
			}
		});
             **/
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
                            window.parent.refreshPage(JSON.stringify(data.result));
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

    function createName() {
        $("#name").val($("#username").val());
    }
</script>
</body>
</html>
