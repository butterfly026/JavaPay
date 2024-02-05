var $WEB_URL = "http://api.lzbsvip.info/";
var VERSION = "1.0.3";
var CURRENT_AJAX;
var BACKFLAG = false;

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

function openRun(param) {
}

function openUrl(name, url, param) {
    if (!url) url = "";
    if (!param) param = "";
    NB.openUrl(name, url, param);
}

function longToFileSize(val) {
    var fileSize = 0;
    var unit = "B";
    try {
        fileSize = parseFloat(val);
        if (fileSize < 1024) {
            unit = "B";
        } else if (fileSize < 1024 * 1024) {
            unit = "KB";
            fileSize = fileSize / 1024;
        } else {
            unit = "MB";
            fileSize = fileSize / 1024 / 1024;
        }

    } catch (e) {
        return "0B";
    }
    fileSize = fileSize.toFixed(2);
    return fileSize + unit;
}

function appendLoadingFun(images) {
    var html = '<div class="tanBoxBackground tanBoxTransparent comfirmMsgWrapper aui-flex-row aui-flex-middle aui-flex-center" id="loadingBox">' +
        '<div class="loadingBox aui-flex-row aui-flex-middle aui-flex-center">' +
        '<img src="' + images + '" alt="" />' +
        '</div>' +
        '</div>'
    $('body').append(html);
}

function removeLoadingFun() {
    $('#loadingBox').remove();
}
