layui.use(['form', 'jquery', 'jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);

    form.on("submit(saveBtn)", function (data) {

        //获取表单元素
        let field = data.field;
        //发送ajax请求
        $.ajax({
            //请求方式
            type: "post",
            //请求地址
            url: ctx + "/user/updateSetting",
            //请求参数
            data: {
                userName: field.userName,
                userPwd: field.userPwd,
                trueName: field.trueName,
                phone: field.phone,
                email: field.email,
                id: field.id
            },
            dataType: "json",
            success: function (result) {
                if (result.code === 200) {
                    layer.msg("保存成功", function () {
                        //清空cookie
                        $.removeCookie("userIdStr", {domain: "localhost", path: "/crm"});
                        $.removeCookie("userName", {domain: "localhost", path: "/crm"});
                        $.removeCookie("trueName", {domain: "localhost", path: "/crm"});
                        //页面跳转
                        window.parent.location.href = ctx + "/index";
                    })
                } else {
                    layer.msg(result.msg);
                }
            }


        })

        return false;


    });


});
