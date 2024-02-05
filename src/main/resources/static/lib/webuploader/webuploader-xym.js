var isSupportBase64 = (function () {
        var data = new Image();
        var support = true;
        data.onload = data.onerror = function () {
            if (this.width != 1 || this.height != 1) {
                support = false;
            }
        }
        data.src = "data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///ywAAAAAAQABAAACAUwAOw==";
        return support;
    })(),
// 检测是否已经安装flash，检测flash的版本
    flashVersion = (function () {
        var version;

        try {
            version = navigator.plugins['Shockwave Flash'];
            version = version.description;
        } catch (ex) {
            try {
                version = new ActiveXObject('ShockwaveFlash.ShockwaveFlash')
                    .GetVariable('$version');
            } catch (ex2) {
                version = '0.0';
            }
        }
        version = version.match(/\d+/g);
        return parseFloat(version[0] + '.' + version[1], 10);
    })();
if (!WebUploader.Uploader.support('flash') && WebUploader.browser.ie) {

    // flash 安装了但是版本过低。
    if (flashVersion) {
        (function (container) {
            window['expressinstallcallback'] = function (state) {
                switch (state) {
                    case 'Download.Cancelled':
                        layer.alert('您取消了更新！')
                        break;

                    case 'Download.Failed':
                        layer.alert('安装失败')
                        break;

                    default:
                        layer.alert('安装已成功，请刷新！');
                        break;
                }
                delete window['expressinstallcallback'];
            };

            var swf = '/admin/lib/webuploader/expressInstall.swf';
            // insert flash object
            var html = '<object type="application/' +
                'x-shockwave-flash" data="' + swf + '" ';

            if (WebUploader.browser.ie) {
                html += 'classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" ';
            }

            html += 'width="100%" height="100%" style="outline:0">' +
                '<param name="movie" value="' + swf + '" />' +
                '<param name="wmode" value="transparent" />' +
                '<param name="allowscriptaccess" value="always" />' +
                '</object>';

            container.html(html);

        })($wrap);

        // 压根就没有安转。
    } else {
        $wrap.html('<a href="http://www.adobe.com/go/getflashplayer" target="_blank" border="0"><img alt="get flash player" src="http://www.adobe.com/macromedia/style_guide/images/160x41_Get_Flash_Player.jpg" /></a>');
    }

} else if (!WebUploader.Uploader.support()) {
    layer.alert('Web Uploader 不支持您的浏览器！');
}

function uploadFile(id, size, percent) {//进度条对象
    this.id = id,
        this.percent = percent,
        this.size = size
}

function successFile(id, fileId, name, thumbPath, path) {
    this.id = id;
    this.fileId = fileId,
        this.name = name;
    this.thumbPath = thumbPath;
    this.path = path;
}

function UploadFiles(options) {
    this.options = options;
    this.uploader;
    if (!this.options.elem || !this.options.uploaderElem) {//如果没有传入元素
        layer.msg("未传入元素，图片上传组件初始化失败");
        return;
    }
    if (!this.options.serverUrl) {//如果没传服务器地址
        this.options["serverUrl"] = "";
    }
    if (!this.options.formData) {
        this.options["formData"] = {};
    }
    if (!this.options.fileNumLimit) {
        this.options["fileNumLimit"] = 1;
    }
    if (!this.options.fileSizeLimit) {
        this.options["fileSizeLimit"] = 500 * 1024 * 1024;
    }
    if (!this.options.fileSingleSizeLimit) {
        this.options["fileSingleSizeLimit"] = 50 * 1024 * 1024;
    }
    if (!this.options.imgWidth) {//图片宽度
        this.options["imgWidth"] = 50;
    }
    if (!this.options.imgHeight) {//图片高度
        this.options["imgHeight"] = 50;
    }
    if (!this.options.dlgWidth) {//弹层宽度
        this.options["dlgWidth"] = "100%";
    }
    if (!this.options.dlgHeight) {//弹层高度
        this.options["dlgHeight"] = "80%";
    }
    if (!this.options.multiple) {
        this.options["multiple"] = false;
    }
    if (!this.options.objName) {
        this.options["objName"] = "callBack";
    }
    if (typeof imgChange != 'function') {
        this.options["imgChange"] = function (f) {
        };
    }
    if (typeof this.options.datas != 'object') {
        this.options["datas"] = [];
    }

    this.$dom = $(this.options.elem);
    this.uFiles = new Array();
    this.sucFiles = new Array();
    this.ratio = window.devicePixelRatio || 1;
    this.thumbnailWidth = this.options.imgWidth * this.ratio;
    this.thumbnailHeight = this.options.imgHeight * this.ratio;
    this.selectStr = "单选";
    this.$uploadInfo = this.$dom.find(".upload-all-info");
    this.$imgList = this.$dom.find(".imglist");
    this.$uploadBtn = this.$dom.find("#uploadImageBtn");//上传图片
    this.$imgsBtn = this.$dom.find("#imagesBtn");//从图库选择
    this.$uploaderBtn = $(this.options.uploaderElem);//uploader按钮
    this.uploadStatus = "pause";
    this.lay_index;
    this.box_index = 0;

    if (this.options.multiple) {
        this.selectStr = "多选";
    }
    this.$uploadBtn.hide();

    var that = this;
    this.uploader = WebUploader.create({
        pick: {
            id: this.options.uploaderElem,
            multiple: that.options.multiple
        },
        formData: that.options.formData,
        swf: that.options.serverUrl + '/lib/webuploader/Uploader.swf',
        server: that.options.uploaderUrl,
        accept: {
            title: 'Images',
            extensions: 'gif,jpg,jpeg,bmp,png',
            mimeTypes: 'image/*'
        },
        disableGlobalDnd: true,
        fileNumLimit: that.options.fileNumLimit,
        fileSizeLimit: that.options.fileSizeLimit,
        fileSingleSizeLimit: that.options.fileSingleSizeLimit
    });

    this.uploader.on('beforeFileQueued', function () {
        //检查文件是否超出了个数
        var fileNumLimit = that.uploader.option("fileNumLimit");
        var elemCount = that.$imgList.find("li").length;
        if (elemCount >= fileNumLimit) {//超出文件上传个数
            layer.msg("最多只允许使用" + fileNumLimit + "个图片");
            return false;
        }
    });

    this.uploader.onFileQueued = function (file) {//文件添加到队列中时触发
        that.addFile(file);
    };

    this.uploader.onFileDequeued = function (file) {//文件移除
        for (var i = 0; i < that.uFiles.length; i++) {
            if (that.uFiles[i].id == file.id) {
                that.uFiles.splice(i, 1);
                break;
            }
        }
        if (that.$imgList.find("li").length == 0) {
            that.$uploadInfo.hide();
            that.$uploadBtn.hide();
        }
    };

    this.uploader.onUploadProgress = function (file, percentage) {//更新进度条
        var $li = that.$imgList.find('.efile_' + file.id),
            $progressLength = $li.find('.progress-length');
        $progressLength.css('width', percentage * 100 + '%');

        for (var i = 0; i < that.uFiles.length; i++) {
            if (that.uFiles[i].id == file.id) {
                that.uFiles[i].percent = percentage;
                break;
            }
        }
        var tp = 0,
            tc = 0;
        for (var i = 0; i < that.uFiles.length; i++) {
            tp += that.uFiles[i].percent;
            tc += 1;
        }

        var p = tp * 100 / tc;
        that.$uploadInfo.html("上传总进度:" + parseInt(p) + "%");
    };
    //处理所有状态
    this.uploader.on('all', function (type) {
        //console.info(type);
        switch (type) {
            case 'uploadFinished':
                //setState( 'confirm' );

                that.uploadStatus = "pause";
                that.$uploadBtn.css("background-color", "#32AA9F");
                that.$uploadBtn.html("开始上传");
                var stats = that.uploader.getStats();
                that.$uploadInfo.html("上传成功" + stats.successNum + "张图片");
                if (stats.uploadFailNum) {
                    that.$uploadInfo.append(",失败" + stats.uploadFailNum + "张,您可以<a href='javascript:void(0)' class='retry'>重新上传</a>");
                    that.$uploadInfo.find(".retry").on("click", function () {
                        that.uploader.retry();
                    });
                }
                break;

            case 'startUpload':
                that.$uploadInfo.show();
                that.$uploadInfo.html("上传开始！");
                break;

            case 'stopUpload':
                that.$uploadInfo.html("暂停上传！");
                break;

        }
    });

    this.uploader.on("uploadSuccess", function (file, response) {//上传成功
        //console.info("上传成功"+file.id+",data:"+JSON.stringify(response));
        var $res = response.result;//服务器端返回数据
        //+that.options.serverUrl+'/'
        that.$imgList.find(".eimg_" + file.id).html('<img src="' + $res.thumbPath + '" style="max-height:' + that.options.imgHeight + 'px; max-width:' + that.options.imgWidth + 'px;width: expression(this.width > ' + that.options.imgWidth + ' && this.width > this.height ? ' + that.options.imgWidth + ' : auto);height: expression(this.height > ' + that.options.imgHeight + ' ? ' + that.options.imgHeight + ' : auto);"/>');
        that.sucFiles[that.sucFiles.length] = new successFile($res.id, 'efile_' + file.id, $res.name, $res.thumbPath, $res.path);
        that.options.imgChange(that.sucFiles);
    });


    this.$uploadBtn.on("click", function () {
        if (that.uploadStatus == "upload") {
            $(this).css("background-color", "#32AA9F");
            that.uploader.stop();//暂停
            that.uploadStatus = "pause";
            $(this).html("开始上传");
        } else {
            $(this).css("background-color", "#FC754B");
            that.uploader.upload();//上传
            that.uploadStatus = "upload";
            $(this).html("暂停上传");
        }
    });

    this.$imgsBtn.on("click", function () {
        that.lay_index = layer.open({
            type: 2,
            title: '选择图片-' + that.selectStr,
            area: [that.options.dlgWidth, that.options.dlgHeight], //宽高
            content: that.options.serverUrl + '/system/sysImage/select?type=' + that.options.multiple + '&method=' + that.options.objName + "&source=uploader"
        });
    });

    this.uploader.onError = function (code) {
        if (code == 'F_DUPLICATE') {
            layer.alert('无法添加重复的文件！', {icon: 2});
        } else if (code == 'Q_TYPE_DENIED') {
            layer.alert('请选择图片文件！', {icon: 2});
        } else if (code == 'Q_EXCEED_NUM_LIMIT') {
            layer.alert('超出最大文件上传数量限制！', {icon: 2});
        } else if (code == 'Q_EXCEED_SIZE_LIMIT') {
            layer.alert('超出文件上传总大小限制！', {icon: 2});
        } else if (code == 'F_EXCEED_SIZE') {
            layer.alert('单个文件最大只能上传50M！', {icon: 2});
        } else {
            layer.alert('Error: ' + code, {icon: 2});
        }
    };

    this.uploader.on('uploadAccept', function (obj, ret) {//服务器返回响应消息后的处理
        var data = ret._raw;
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
                return false;
            }
            return false;
        } else {
            data = eval("(" + data + ")");
            if (data.code == 200) {
                return true;
            } else {
                if (data.msg) layer.msg(data.msg);
                return false;
            }
        }
    });
    this.addFile = function (file) {
        var that = this;
        if (file.getStatus() === 'invalid') {
            this.$imgList.append("<li>无效图片</li>");
            return;
        }

        var $li = $('<li class="efile_' + file.id + '" title="' + file.name + '" style="width:' + that.options.imgWidth + 'px;"><div class="img-container" style="width:' + that.options.imgWidth + 'px;height:' + that.options.imgHeight + 'px;">' +
            '<span class="eimg_' + file.id + ' timg"></span><div class="delbtn-bg"><img src="' + that.options.serverUrl + '/lib/webuploader/delete.png" class="delbtn-img"/></div>' +
            '<div class="upload-progress"><div class="progress-length"></div></div><img src="' + that.options.serverUrl + '/lib/webuploader/success.png" class="upload-success"/>' +
            '<span class="info"></span></div><div class="txt">' + file.name + '</div></li>');
        var $thumbImage = $li.find(".thumb-image"),
            $imgContainer = $li.find(".img-container"),
            $progress = $li.find(".upload-progress"),
            $progressLength = $li.find(".progress-length"),
            $info = $li.find(".info"),
            $success = $li.find(".upload-success"),
            $delBtnBg = $li.find(".delbtn-bg"),
            $delBtn = $li.find(".delbtn-img"),
            $eimg = $li.find(".eimg_" + file.id);
        this.$imgList.append($li);
        this.uFiles[this.uFiles.length] = new uploadFile(file.id, file.size, 0);
        this.$uploadBtn.show();


        this.uploader.makeThumb(file, function (error, src) {//图片预览
            if (error) {
                $info.text('不能预览');
                return;
            }
            if (isSupportBase64) {
                $eimg.html('<img src="' + src + '"/>');
            } else {
                $info.text('不能预览');
            }
        }, that.thumbnailWidth, that.thumbnailHeight);

        file.on('statuschange', function (cur, prev) {//文件状态变化，参数：当前状态，上一状态
            $info.text('');
            if (cur === 'invalid') {//错误或者无效
                $progress.show();
                $progress.css("background-color", "#FF0000");
                $progressLength.css("background-color", "#FF0000");
                $info.text('文件错误');
            } else if (cur === 'interrupt') {
                $info.text('上传中断');
            } else if (cur === 'queued') {//队列中
                $progress.show();
                $progress.css("background-color", "F4F4F4");
                $progressLength.css("background-color", "#00B7EE");
                $progressLength.css("width", "0");
            } else if (cur === 'progress') {//上传中
                $progress.show();
            } else if (cur === 'complete') {//上传完成
                $progress.hide();
                $success.show();
            } else if (cur === 'error') {
                $progress.show();
                $progress.css("background-color", "#FF0000");
                $progressLength.css("background-color", "#FF0000");
                $info.text('上传失败');
            }
        });

        $li.on('mouseenter', function () {
            $delBtnBg.show();
        });

        $li.on('mouseleave', function () {
            $delBtnBg.hide();
        });

        $delBtn.on('mouseenter', function () {
            $(this).attr("src", that.options.serverUrl + "/lib/webuploader/delete1.png");
        });

        $delBtn.on('mouseleave', function () {
            $(this).attr("src", that.options.serverUrl + "/lib/webuploader/delete.png");
        });
        $delBtn.on("click", function () {
            $li.remove();
            that.removeSuccessFile('efile_' + file.id);
            that.uploader.removeFile(file, true);
        });
    };

    this.addFileByBox = function (ckimgs) {
        layer.close(this.lay_index);
        var fileNumLimit = that.uploader.option("fileNumLimit");
        //alert(JSON.stringify(ckimgs));
        //将图片添加到图片列表
        if (ckimgs && ckimgs.length > 0) {
            for (var i = 0; i < ckimgs.length; i++) {
                //检查文件是否超出了个数
                var elemCount = that.$imgList.find("li").length;
                if (elemCount >= fileNumLimit) {//超出文件上传个数
                    layer.msg("最多只允许使用" + fileNumLimit + "张图片");
                    return;
                }
                this.box_index--;
                //+that.options.serverUrl+"/"
                var $li = $('<li class="efile_' + this.box_index + '" title="' + ckimgs[i].name + '" style="width:' + that.options.imgWidth + 'px;"><div class="img-container" style="width:' + that.options.imgWidth + 'px;height:' + that.options.imgHeight + 'px;">' +
                    '<span class="eimg_' + this.box_index + ' timg"><img src="' + ckimgs[i].thumbPath + '" style="max-height:' + that.options.imgHeight + 'px; max-width:' + that.options.imgWidth + 'px;width: expression(this.width > ' + that.options.imgWidth + ' && this.width > this.height ? ' + that.options.imgWidth + ' : auto);height: expression(this.height > ' + that.options.imgHeight + ' ? ' + that.options.imgHeight + ' : auto);"/></span><div class="delbtn-bg"><img src="' + that.options.serverUrl + '/lib/webuploader/delete.png" class="delbtn-img"/></div>' +
                    '<div class="upload-progress"><div class="progress-length"></div></div><img src="' + that.options.serverUrl + '/lib/webuploader/success.png" class="upload-success"/>' +
                    '<span class="info"></span></div><div class="txt">' + ckimgs[i].name + '</div></li>');
                var $delBtnBg = $li.find(".delbtn-bg"),
                    $success = $li.find(".upload-success"),
                    $delBtn = $li.find(".delbtn-img");
                this.$imgList.append($li);
                $success.show();
                this.sucFiles[this.sucFiles.length] = new successFile(ckimgs[i].id, 'efile_' + this.box_index, ckimgs[i].name, ckimgs[i].thumbPath, ckimgs[i].path);
                this.options.imgChange(this.sucFiles);

                $li.on('mouseenter', function () {
                    $(this).find(".delbtn-bg").show();
                });

                $li.on('mouseleave', function () {
                    $(this).find(".delbtn-bg").hide();
                });

                $delBtn.on('mouseenter', function () {
                    $(this).attr("src", that.options.serverUrl + "/lib/webuploader/delete1.png");
                });

                $delBtn.on('mouseleave', function () {
                    $(this).attr("src", that.options.serverUrl + "/lib/webuploader/delete.png");
                });
                $delBtn.on("click", function () {
                    var $ll = $(this).parents("li");
                    $ll.remove();
                    //移除选中的图片列表中的图片
                    //console.info($ll.attr("class"));
                    that.removeSuccessFile($ll.attr("class"));
                });
            }
        }
    }
    //移除成功的文件
    this.removeSuccessFile = function (fileId) {
        for (var i = 0; i < this.sucFiles.length; i++) {
            if (this.sucFiles[i].fileId == fileId) {
                this.sucFiles.splice(i, 1);
                break;
            }
        }
        this.options.imgChange(this.sucFiles);
    }

    this.initImgList = function () {
        //初始化数据
        if (this.options.datas.length > 0) {
            var fileNumLimit = that.uploader.option("fileNumLimit");
            for (var i = 0; i < this.options.datas.length; i++) {
                //检查文件是否超出了个数
                var elemCount = this.$imgList.find("li").length;
                if (elemCount >= fileNumLimit) {//超出文件上传个数
                    that.$uploadInfo.html("初始化出错，最多只允许使用" + fileNumLimit + "张图片");
                    that.$uploadInfo.show();
                    return;
                }
                if (this.options.datas[i] == '') {//空字符串不处理
                    continue;
                }
                this.box_index--;
                var $li = $('<li class="efile_' + this.box_index + '" style="width:' + that.options.imgWidth + 'px;"><div class="img-container" style="width:' + that.options.imgWidth + 'px;height:' + that.options.imgHeight + 'px;">' +
                    '<span class="eimg_' + this.box_index + ' timg"><img src="' + this.options.datas[i] + '" style="max-height:' + that.options.imgHeight + 'px; max-width:' + that.options.imgWidth + 'px;width: expression(this.width > ' + that.options.imgWidth + ' && this.width > this.height ? ' + that.options.imgWidth + ' : auto);height: expression(this.height > ' + that.options.imgHeight + ' ? ' + that.options.imgHeight + ' : auto);"/></span><div class="delbtn-bg"><img src="' + that.options.serverUrl + '/lib/webuploader/delete.png" class="delbtn-img"/></div>' +
                    '<div class="upload-progress"><div class="progress-length"></div></div><img src="' + that.options.serverUrl + '/lib/webuploader/success.png" class="upload-success"/>' +
                    '<span class="info"></span></div><div class="txt">菜单图标</div></li>');
                var $delBtnBg = $li.find(".delbtn-bg"),
                    $success = $li.find(".upload-success"),
                    $delBtn = $li.find(".delbtn-img");
                this.$imgList.append($li);
                $success.show();
                this.sucFiles[this.sucFiles.length] = new successFile(-1, 'efile_' + this.box_index, "", "", this.options.datas[i]);

                $li.on('mouseenter', function () {
                    $(this).find(".delbtn-bg").show();
                });

                $li.on('mouseleave', function () {
                    $(this).find(".delbtn-bg").hide();
                });

                $delBtn.on('mouseenter', function () {
                    $(this).attr("src", that.options.serverUrl + "/lib/webuploader/delete1.png");
                });

                $delBtn.on('mouseleave', function () {
                    $(this).attr("src", that.options.serverUrl + "/lib/webuploader/delete.png");
                });
                $delBtn.on("click", function () {
                    var $ll = $(this).parents("li");
                    $ll.remove();
                    //移除选中的图片列表中的图片
                    //console.info($ll.attr("class"));
                    that.removeSuccessFile($ll.attr("class"));
                });
            }
        }
    }
    this.initImgList();
}
