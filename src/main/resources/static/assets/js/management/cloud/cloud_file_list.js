layui.use(['table', 'element'], function () {
    var table = layui.table
        , element = layui.element;

    element.render();
    var noteTable = table.render({
        elem: '#cloud-file-table'
        , height: 'full'
        , url: BMY.url.prefix + "/cloudFile/list"
        , cellMinWidth: 90
        , limit: 10
        , size: 'lg'
        , cols: [[
            {type: 'numbers'}
            , {field: 'name', title: '云文件名称', sort: true}
            , {
                field: "cloudFileCate", title: '分类', sort: true, templet: function (d) {
                    return d.cloudFileCate.cnName;
                }
            }
            , {field: "description", sort: true, title: "描述"}
            , {field: "url", sort: true, title: "项目主页"}
            , {title: '操作', width: 300, align: 'center', toolbar: '#cloudFileTableBar'}
        ]]
        , page: true
    });


//监听工具条
    table.on('tool(cloudFile)', function (obj) {
        var data = obj.data;
        if (obj.event === 'detail') {
            location.hash = vipspa.stringify("/cloudFile/edit", {id: data.id});
        } else if (obj.event === 'del') {
            layer.confirm('确认删除吗？', function (index) {
                obj.del();
                BMY.ajax(BMY.url.prefix + "/cloudFile/delete/" + data.id, {}, function (json) {
                    BMY.okMsgHandle(json);
                });
                layer.close(index);
            });
        }
    });

    table.on('sort(cloudFile)', function (obj) {
        noteTable.reload({
            initSort: obj
            , where: {
                sort: obj.field
                , order: obj.type
            }
        });
    });

});







