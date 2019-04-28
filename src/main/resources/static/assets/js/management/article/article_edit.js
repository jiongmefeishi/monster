/** Created By Wuwenbin https://wuwenbin.me
 * mail to wuwenbinwork@163.com
 * 欢迎加入我们，QQ群：697053454
 * if you use the code,  please do not delete the comment
 * 如果您使用了此代码，请勿删除此头部注释
 * */
$(function () {
    var editor, formSelects;
    layui.use(['element', 'form', 'layer', 'upload', 'formSelects'], function () {
        var form = layui.form;
        var element = layui.element;
        var $ = layui.$;
        var upload = layui.upload;
        formSelects = layui.formSelects;
        element.render();
        form.render();
        window._laySelect = formSelects;


        formSelects.config('tags', {
            keyName: 'name'
            , keyVal: 'name'
        });

        formSelects.data('tags', 'server', {
            url: BMY.url.prefix + '/article/edit/tags?id=' + articleId
        });

        formSelects.maxTips('tags', function () {
            layer.msg("最多只能选择4个");
        });


        var post = function (data, draft, msg) {
            console.log(data.field);
            data.field.draft = draft;
            data.field.cate = data.field.cateId;
            data.field.tagNames = $("div.xm-input.xm-select").attr("title");
            // alert(data.field.tagNames);
            if (data.field.editor === 'html') {
                data.field.mdContent = "";
                data.field.content = editor.html();
            }
            if (data.field.editor === 'markdown') {
                data.field.mdContent = editorMd.getMarkdown();
                // data.field.content = editorMd.getHTML();
                data.field.content = editorMd.getPreviewedHTML();
            }
            data.field.cover = $("#coverImg").find("img").attr("src");

            BMY.ajaxManagement(
                "/article/update",
                data.field,
                function (json) {
                    BMY.msgHandle(json, function () {
                        if (json.code === BMY.status.ok) {
                            location.hash = vipspa.stringifyDefault("/article");
                        }
                    });
                });

        };

        //监听提交
        form.on('submit(postSubmit)', function (data) {
            post(data, false, "修改文章成功！");
            return false;
        });

        form.on('submit(draftSubmit)', function (data) {
            post(data, true, "修改成功，保存草稿！");
            return false;
        });

        form.on("switch(summary)", function (data) {
            if (data.elem.checked) {
                $("#article-summary").show();
            } else {
                $("#article-summary").hide();
            }
        });

        form.on("switch(customUrl)", function (data) {
            if (data.elem.checked) {
                $("#urlSequence").show();
            } else {
                $("#urlSequence").hide();
            }
        });

        form.on('radio(editor)', function (data) {
            if (data.value === "markdown") {
                editor.remove();
                $("#content-editor").append("<div id='editormd'></div>");
                $.getScript("/static/plugins/editormd/editormd.min.js", function () {
                    editorMd = editormd("editormd", {
                        height: 640,
                        watch: true,
                        codeFold: true,
                        toolbarIcons: function () {
                            return [
                                "undo", "redo", "|",
                                "bold", "del", "italic", "quote", "ucwords", "uppercase", "lowercase", "|",
                                "h1", "h2", "h3", "h4", "h5", "h6", "|",
                                "list-ul", "list-ol", "hr", "|",
                                "link", "reference-link", "image", "code", "preformatted-text", "code-block", "table", "datetime", "html-entities", "pagebreak", "|",
                                "goto-line", "watch", "preview", "fullscreen", "clear", "search", "|",
                                "help", "info"
                            ]
                        },
                        pluginPath: '/static/plugins/editormd/plugins/',
                        markdown: mdContents,
                        path: '/static/plugins/editormd/lib/',
                        placeholder: '请在此书写你的内容',
                        saveHTMLToTextarea: true,
                        searchReplace: true,
                        taskList: true,
                        tex: true,// 开启科学公式TeX语言支持，默认关闭
                        flowChart: true,//开启流程图支持，默认关闭
                        sequenceDiagram: true,//开启时序/序列图支持，默认关闭,
                        imageUpload: true,
                        imageFormats: ["jpg", "jpeg", "gif", "png", "bmp"],
                        imageUploadURL:
                            BMY.url.prefix + "/upload/editorMD?reqType=lay",
                        onfullscreen:
                            function () {
                                $(".layui-header").css("z-index", "-1");
                                $("#left-menu").css("z-index", "-1");
                                $(".layui-form-item>label,.layui-form-item>div:not(#content-editor)").css("z-index", -1);
                                $(".layui-card").css("z-index", "-1");
                            },
                        onfullscreenExit: function () {
                            $(".layui-header").css("z-index", "999");
                            $("#left-menu").css("z-index", "999");
                            $(".layui-form-item>label,.layui-form-item>div:not(#content-editor)").css("z-index", "");
                            $(".layui-card").css("z-index", "999");
                        }
                    });
                });
            }
            else if (data.value === "html") {
                editorMd.editor.remove();
                editor = KindEditor.create('#editor', {
                    cssData: 'body {font-family: "Helvetica Neue", Helvetica, "PingFang SC", 微软雅黑, Tahoma, Arial, sans-serif; font-size: 14px}',
                    width: "auto",
                    height: "600px",
                    items: [
                        'source', 'preview', 'undo', 'redo', 'code', 'cut', 'copy', 'paste',
                        'plainpaste', 'wordpaste', 'justifyleft', 'justifycenter', 'justifyright',
                        'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript',
                        'superscript', 'clearhtml', 'quickformat', 'selectall', 'fullscreen', '/',
                        'formatblock', 'fontname', 'fontsize', 'forecolor', 'hilitecolor', 'bold',
                        'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', 'image', 'graft',
                        'insertfile', 'table', 'hr', 'emoticons', 'pagebreak',
                        'link', 'unlink', 'about'
                    ],
                    uploadJson: BMY.url.prefix + '/upload?reqType=nk',
                    dialogOffset: 0, //对话框距离页面顶部的位置，默认为0居中，
                    allowImageUpload: true,
                    allowMediaUpload: true,
                    themeType: 'black',
                    fixToolBar: true,
                    autoHeightMode: true,
                    filePostName: 'file',//指定上传文件form名称，默认imgFile
                    resizeType: 1,//可以改变高度
                    afterCreate: function () {
                        var self = this;
                        KindEditor.ctrl(document, 13, function () {
                            self.sync();
                            K('form[name=example]')[0].submit();
                        });
                        KindEditor.ctrl(self.edit.doc, 13, function () {
                            self.sync();
                            KindEditor('form[name=example]')[0].submit();
                        });
                    },
                    //错误处理 handler
                    errorMsgHandler: function (message, type) {
                        try {
                            JDialog.msg({type: type, content: message, timer: 2000});
                        } catch (Error) {
                            alert(message);
                        }
                    }
                });
            }
        });

        upload.render({
            elem: '#coverImg' //绑定元素
            , url: BMY.url.prefix + '/upload?reqType=lay' //上传接口
            , done: function (res) {
                if (res.code === 0) {
                    $("#coverImg").html('<p><img style="width: 144px;height: 90px;" src="' + res.data.src + '"></p>');
                }
                layer.msg(res.msg || res.message);
            }
            , error: function () {
                layer.msg("上传失败！");
            }
        });


        if (!isMd) {
            try {
                editorMd.editor.remove();
            } catch (e) {
            }
            editor = KindEditor.create('#editor', {
                cssData: 'body {font-family: "Helvetica Neue", Helvetica, "PingFang SC", 微软雅黑, Tahoma, Arial, sans-serif; font-size: 14px}',
                width: "auto",
                height: "600px",
                items: [
                    'source', 'preview', 'undo', 'redo', 'code', 'cut', 'copy', 'paste',
                    'plainpaste', 'wordpaste', 'justifyleft', 'justifycenter', 'justifyright',
                    'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript',
                    'superscript', 'clearhtml', 'quickformat', 'selectall', 'fullscreen', '/',
                    'formatblock', 'fontname', 'fontsize', 'forecolor', 'hilitecolor', 'bold',
                    'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', 'image', 'graft',
                    'insertfile', 'table', 'hr', 'emoticons', 'pagebreak',
                    'link', 'unlink', 'about'
                ],
                uploadJson: BMY.url.prefix + '/upload?reqType=nk',
                dialogOffset: 0, //对话框距离页面顶部的位置，默认为0居中，
                allowImageUpload: true,
                allowMediaUpload: true,
                themeType: 'black',
                fixToolBar: true,
                autoHeightMode: true,
                filePostName: 'file',//指定上传文件form名称，默认imgFile
                resizeType: 1,//可以改变高度
                afterCreate: function () {
                    var self = this;
                    KindEditor.ctrl(document, 13, function () {
                        self.sync();
                        K('form[name=example]')[0].submit();
                    });
                    KindEditor.ctrl(self.edit.doc, 13, function () {
                        self.sync();
                        KindEditor('form[name=example]')[0].submit();
                    });
                },
                //错误处理 handler
                errorMsgHandler: function (message, type) {
                    try {
                        JDialog.msg({type: type, content: message, timer: 2000});
                    } catch (Error) {
                        alert(message);
                    }
                }
            });
        } else {
            $("#content-editor").append("<div id='editormd'></div>");
            $.getScript("/static/plugins/editormd/editormd.min.js", function () {
                editorMd = editormd("editormd", {
                    height: 640,
                    watch: true,
                    codeFold: true,
                    toolbarIcons: function () {
                        return [
                            "undo", "redo", "|",
                            "bold", "del", "italic", "quote", "ucwords", "uppercase", "lowercase", "|",
                            "h1", "h2", "h3", "h4", "h5", "h6", "|",
                            "list-ul", "list-ol", "hr", "|",
                            "link", "reference-link", "image", "code", "preformatted-text", "code-block", "table", "datetime", "html-entities", "pagebreak", "|",
                            "goto-line", "watch", "preview", "fullscreen", "clear", "search", "|",
                            "help", "info"
                        ]
                    },
                    pluginPath: '/static/plugins/editormd/plugins/',
                    markdown: mdContents,
                    path: '/static/plugins/editormd/lib/',
                    placeholder: '请在此书写你的内容',
                    saveHTMLToTextarea: true,
                    searchReplace: true,
                    taskList: true,
                    tex: true,// 开启科学公式TeX语言支持，默认关闭
                    flowChart: true,//开启流程图支持，默认关闭
                    sequenceDiagram: true,//开启时序/序列图支持，默认关闭,
                    imageUpload: true,
                    imageFormats: ["jpg", "jpeg", "gif", "png", "bmp"],
                    imageUploadURL: BMY.url.prefix + "/upload/editorMD?reqType=lay",
                    onfullscreen: function () {
                        $(".layui-header").css("z-index", "-1");
                        $("#left-menu").css("z-index", "-1");
                        $(".layui-form-item>label,.layui-form-item>div:not(#content-editor)").css("z-index", -1);
                        $(".layui-card").css("z-index", "-1");
                    },
                    onfullscreenExit: function () {
                        $(".layui-header").css("z-index", "999");
                        $("#left-menu").css("z-index", "999");
                        $(".layui-form-item>label,.layui-form-item>div:not(#content-editor)").css("z-index", "");
                        $(".layui-card").css("z-index", "999");
                    }
                });
            });
        }

    });

});





