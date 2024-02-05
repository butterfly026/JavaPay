var totalpage = 1;
var urlpar = "";
var objName = "";
var page = 0;
var prvstr = '<li><a href="javascript:void(0)" onclick="firstpage()" aria-label="Previous"><span aria-hidden="true">&laquo;</span></a></li>';
var nextstr = '<li><a href="javascript:void(0)" onclick="endpage()" aria-label="Next"><span aria-hidden="true">&raquo;</span></a></li>';
var psize = 0;
var tcount = 0;
var cfunc;

function cpage(elm, page, totalpage, func) {
    totalpage = totalpage;
    page = page;
    cfunc = func;
    showpageview(totalpage, elm, page);
}

function cpinit(p, s, tc) {
    totalpage = Math.ceil(tc / s);
    page = p;
    psize = s;
    tcount = tc;
}

function cat_getpage(val) {
    showpageview(totalpage, val, page);
}

function firstpage() {
    if (page == 1) return;
    pagequery(1);
}

function endpage() {
    if (page == totalpage) return;
    pagequery(totalpage);
}

function showpageview(totalpage, val, page) {
    var str = "";
    var startpage = page - 1;
    if (totalpage > 8) {
        if (totalpage - startpage < 8) startpage = totalpage - 7;
    } else {
        startpage = 1;
    }
    if (startpage < 1) startpage = 1;
    for (var i = startpage; i <= totalpage; i++) {
        if (i == page) {
            str += '<li class="active"><a href="#">' + i + '</a></li>';
        } else if (i > startpage + 3 && i < totalpage - 3) {
            str += "<li><span>...</span></li>";
            i = totalpage - 4;
        } else {
            str += "<li><a href='javascript:void(0)' onclick='pagequery(" + i + ")'>" + i + "</a></li>";
        }
    }
    var shtml = "";
    if (page == 1) {
        shtml = str + nextstr;
    } else if (page == totalpage) {
        shtml = prvstr + str;
    } else {
        shtml = prvstr + str + nextstr;
    }
    $(val).html(shtml);
}

function pagequery(page) {
    if (cfunc) cfunc(page);
}

function nextpage() {
    if (page < totalpage) page++;
    else return;
    pagequery(page);
}

function prvpage() {
    if (page > 1) page--;
    else return;
    pagequery(page);
}
