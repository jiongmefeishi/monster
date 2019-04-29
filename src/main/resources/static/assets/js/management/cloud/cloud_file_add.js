layui.use(['element', 'form', 'layer', 'upload'], function () {
    var form = layui.form;
    var element = layui.element;
    var $ = layui.$;
    element.render();
    form.render();


    var post = function (data) {
        data.field.cloudFileCate = data.field.cateId;
        $.ajax({
            type: "post"
            , url: BMY.url.prefix + "/cloudFile/create"
            , dataType: "json"
            , data: data.field
            , success: function (json) {
                BMY.msgHandle(json, function () {
                    if (json.code === 200) {
                        location.hash = vipspa.stringifyDefault("/cloudFile");
                    }
                })
            }
        });
    };


    //监听提交
    form.on('submit(cloudFileSubmit)', function (data) {
        if (data.field.description.length > 500) {
            layer.msg("项目描述长度不能超过500字符");
        } else {
            post(data);
        }
        return false;
    });

});





