/**
 * 详情对话框
 */
var MenuInfoDlg = {
    data: {
        pid: "",
        pcodeName: ""
    }
};

layui.use(['layer', 'form', 'admin', 'laydate', 'ax'], function () {
    var $ = layui.jquery;
    var $ax = layui.ax;
    var form = layui.form;
    var admin = layui.admin;
    var laydate = layui.laydate;
    var layer = layui.layer;

    // 让当前iframe弹层高度适应
    admin.iframeAuto();

    //获取菜单信息
    var ajax = new $ax(Feng.ctxPath + "/intelligence/article_update_Item?id=" + Feng.getUrlParam("id"));
        var result = ajax.start();
        console.log(result.data);

        form.val('articleForm', result.data);

    // 表单提交事件b
    form.on('submit(btnSubmit)', function (data) {
        var ajax = new $ax(Feng.ctxPath + "/intelligence/edit", function (data) {
            Feng.success("修改成功！");

            //关掉对话框
            admin.closeThisDialog();
        }, function (data) {
            Feng.error("修改失败！" + data.responseJSON.message)
        });
        ajax.set(data.field);
        ajax.start();
    });

    // 渲染时间选择框
    laydate.render({
        elem: '#time'
        ,type: 'datetime'
    });
});