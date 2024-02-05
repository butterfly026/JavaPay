var $VERSION = "1.0";

//获取指定长度的字符串
function getVallen(val, len) {
    return val.length > len ? val.substring(0, len) : val;
}

//获取指定长度的字符串，末尾加上"..."
function getVallens(val, len) {
    return val.length > len ? val.substring(0, len) + "..." : val;
}

//获取url中参数的值
function getUrlPar(par) {
    var url = location.href;
    var ind = url.indexOf("?");
    if (ind == -1) {
        return "";
    }
    url = url.substring(ind + 1);
    if (url == "") {
        return "";
    }
    var urls = url.split("&");
    for (var i = 0; i < urls.length; i++) {
        if (urls[i].indexOf(par + "=") == 0) {
            return urls[i].substring(par.length + 1);
        }
    }
    return "";
}

//获取url中参数字符串
function getUrlPars() {
    var url = location.href;
    var ind = url.indexOf("?");
    if (ind == -1) {
        return "";
    }
    return url.substring(ind + 1);
}

function nullVal(val) {
    return val == null ? "" : val;
}

//身份过期
var GOLOGIN = "1001";
//无权限
var NOPERMISSION = "1002";
//异地登录
var LOGINERROR = "1003";
$(function () {
    /**ajax data filter*/
    $.ajaxSetup({
        dataFilter: function (data, type) {
            if (data.indexOf("?") == 0) {//没有权限
                var i1 = data.indexOf("<script>");
                if (i1 != -1) {
                    //get json string and analysis to object
                    var obj = eval("(" + data.substring(1, i1) + ")");
                    if (GOLOGIN == obj.code || LOGINERROR == obj.code) {
                        layer.alert(obj.msg, {icon: 2}, function (index) {
                            top.location.href = obj.url;
                            layer.close(index);
                        });
                    } else if (NOPERMISSION == obj.code) {
                        layer.alert(obj.msg, {icon: 2});
                    } else {
                        layer.alert("what are your doing?", {icon: 2});
                    }
                    return '{"code":' + obj.code + '}';
                }
                return '{"code":666}';
            } else {
                return data;
            }
        }
    });
});

//表格数据转json
function getFormJsonData(formName) {
    var o = {};
    $.each($(formName).serializeArray(), function () {
        if (o[this.name]) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
}

/**
 * cookie中存值
 * */
function setCookie(name, value) {
    if (value) {
        var days = 365;
        var exp = new Date();
        exp.setTime(exp.getTime() + days * 24 * 60 * 60 * 1000);
// 写入Cookie, toGMTString将时间转换成字符串
        document.cookie = name + "=" + escape(value) + ";expires=" + exp.toGMTString;
    }
};

/**
 * cookie中取值
 * */
function getCookie(name) {
    var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");	//匹配字段
    if (arr = document.cookie.match(reg)) {
        return unescape(arr[2]);
    } else {
        return null;
    }
};

function initSelect(keynames) {
    for (var i = 0; i < keynames.length; i++) {
        $("#" + keynames[i]).next().find('.layui-select-title input[type="text"]').on('click', function () {
            this.select();
        });
    }
}

function initSelectAll() {
    $("select").next().find('.layui-select-title input[type="text"]').on('click', function () {
        this.select();
    });
}

function initTableSelect() {
    $("body").on('click', '.layui-table-body tr ', function () {
        //console.log(this);
        var data_index = $(this).attr('data-index');//得到当前的tr的index
        //console.log($(this).attr('data-index'));
        $(".layui-table-body tr").attr({"style": "background:#FFFFFF"});//其他tr恢复颜色
        $(".layui-table-body tr[data-index=" + data_index + "]").attr({"style": "background:#e6e6e6;"});//改变当前tr颜色

    });
}
