function uelayer(index, layindex, editor) {
    this.index = index;
    this.layindex = layindex;
    this.editor = editor;
}

var $UELAYERS = new Array();

var lay_ue_index;
UE.registerUI('selectImage', function (editor, uiName) {

    //参考addCustomizeButton.js
    var btn = new UE.ui.Button({
        name: uiName,
        title: '选择文件/图片',
        //需要添加的额外样式，指定icon图标，这里默认使用一个重复的icon
        cssRules: 'background-position: -726px -77px;',
        onclick: function () {
            var index = $UELAYERS.length;
            //弹出文件/图片选择框
            lay_ue_index = layer.open({
                type: 2,
                title: '选择文件/图片',
                area: ['95%', '95%'], //宽高
                content: '/admin/system/sysImage/select?type=true&method=ueInsertImage&source=&index=' + index
            });
            $UELAYERS[index] = new uelayer(index, lay_ue_index, editor);
        }
    });

    return btn;
});

/***
 * 图片选择框回调方法
 * @param imgs
 * @returns
 */
function ueInsertImage(imgs) {
    layer.close(lay_ue_index);
    if (imgs) {
        var $str = '';
        for (var i = 0; i < imgs.length; i++) {
            $str += '<img src="/admin' + imgs[i].thumbPath + '"  title="' + imgs[i].name + '" />';
        }
        ue.execCommand("inserthtml", $str);
    }
}
