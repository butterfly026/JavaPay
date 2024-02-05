<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
?{"code":"1001","msg":"很抱歉，您的身份已失效，请重新登录","url":"${context}/login/login"}<script>
var str='<!DOCTYPE html>';
str+='<html><head><meta charset="utf-8"><meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">';
str+='<title>身份失效</title><link rel="stylesheet" href="${context}/layui/css/layui.css"><style>.admin-main {margin: 15px;width:100%;text-align:center;}</style>';
str+='</head><body>';
str+='<div class="admin-main"><h2>很抱歉，您的身份已失效，请重新登录</h2><br/><a href="${context}/login/login">点击前往登录页面</a></div></body></html>';
document.body.innerHTML=str;</script>
