/**
 * 角色详情对话框
 */
var DeptInfoDlg = {
    data: {
        pid: "",
        pName: ""
    }
};

layui.use(['layer', 'form', 'admin', 'ax'], function () {
    var $ = layui.jquery;
    var $ax = layui.ax;
    var form = layui.form;
    var admin = layui.admin;
    var layer = layui.layer;

    // 让当前iframe弹层高度适应
    admin.iframeAuto();
	//获取菜单信息
    var ajax = new $ax(Feng.ctxPath + "/regular/regular_update_Item?id=" + Feng.getUrlParam("id"));
        var result = ajax.start();
    form.val('regularForm', result.data);

    // 表单提交事件
    form.on('submit(btnSubmit)', function (data) {
        var data = {
            "id": $("#id").val(),
            "name": $("#name").val(),
            "regular": $("#regular").val()
        };
        $.ajax({
                url:Feng.ctxPath + "/regular/edit",
                async: false,
                type:"POST",
                contentType:"application/json",
                data: JSON.stringify(data),
                dataType: "json",
                success: function(data){
                    Feng.success("修改成功!");
                    admin.closeThisDialog();
                },
                error: function() {
                    Feng.error("修改失败!");
                }
         })
    });
});