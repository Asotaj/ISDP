layui.use(['table', 'ax'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;

    /**
     * 字典类型表管理
     */
    var RegularType = {
        tableId: "regularTable"
    };

    /**
     * 初始化表格的列
     */
    RegularType.initColumn = function () {
        return [[
          
            {field: 'id', hide: true, title: '字典类型id'},
            {field: 'name', sort: true, title: '规则名称'},
            {field: 'regular', sort: true, title: '规则表达式'},
           /* {field: 'creater_id', sort: true, title: '创建人id'},*/
			{field: 'creater', sort: true, title: '创建人'},
			/*{field: 'is_deleted', sort: true, title: '删除标记'},*/
            {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 100}
        ]];
    };

    /**
     * 点击查询按钮
     */
    RegularType.search = function () {
        var queryData = {};
        queryData['condition'] = $("#condition").val();
        table.reload(RegularType.tableId, {where: queryData});
    };

    /**
     * 弹出添加对话框
     */
    RegularType.openAddDlg = function () {
        top.layui.admin.open({
            type: 2,
            title: '添加规则',
            content: Feng.ctxPath + '/regular/regular_addItem',
            end: function () {
                table.reload(RegularType.tableId);
            }
        });
    };

    /**
     * 点击编辑
     *
     * @param data 点击按钮时候的行数据
     */
    RegularType.openEditDlg = function (data) {
        top.layui.admin.open({
            type: 2,
            title: '编辑列表',
            content: Feng.ctxPath + '/regular/regular_update?id=' + data.id,
            end: function () {
                 table.reload(RegularType.tableId);
            }
        });
    };

    /**
     * 点击删除
     *
     * @param data 点击按钮时候的行数据
     */
    RegularType.onDeleteItem = function (data) {
        var operation = function () {
            
			 $.ajax({
                url:Feng.ctxPath + "/regular/delete?id=" + data.id,
                async: false,
                type:"get",
                dataType: "json",
                success: function(data){
                    Feng.success("删除成功!");
                    table.reload(RegularType.tableId);
                },
                error: function() {
                    Feng.error("删除失败!" + data.responseJSON.message + "!");
                }
            })
			
        };
        Feng.confirm("是否删除?", operation);
    };


    /**
     * 点击展示
     */
    RegularType.onShowItem = function (data,btn) {
        $.ajax({
            url:Feng.ctxPath + "/regular/showItem?id=" + data.id,
            type:"get",
            success: function(data){
                if(data=="success"){
                    btn.attr("class", "layui-btn layui-btn-danger layui-btn-xs");
                    Feng.success("展示成功!");
                    // table.reload(RegularType.tableId);
                }else{
                    Feng.error("最多展示8个规则，已达上限！");
                    // table.reload(RegularType.tableId);
                }
            },
            error: function() {
                Feng.error("展示失败!");
            }
        })
    };

    /**
     * 点击展示
     */
    RegularType.onHideItem = function (data,btn) {
        $.ajax({
            url:Feng.ctxPath + "/regular/hideItem?id=" + data.id,
            type:"get",
            success: function(data){
                btn.attr("class", "layui-btn layui-btn-primary layui-btn-xs");
                Feng.success("取消展示!");
                // table.reload(RegularType.tableId);
            },
            error: function() {
                Feng.error("取消展示失败!");
            }
        })
    };

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + RegularType.tableId,
        url: Feng.ctxPath + '/regular/list',
        page: true,
        height: "full-98",
        cellMinWidth: 100,
        cols: RegularType.initColumn()
    });

    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        RegularType.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        RegularType.openAddDlg();
    });

    // 工具条点击事件
    table.on('tool(' + RegularType.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;

        if (layEvent === 'edit') {
            RegularType.openEditDlg(data);
        } else if (layEvent === 'delete') {
            RegularType.onDeleteItem(data);
        }else if (layEvent === 'add') {
           // RegularType.openAddDlg(data);
        }else if(layEvent === 'show'){
            var btn = obj.tr.find("td:last a:first");
            if(btn.attr("class")=='layui-btn layui-btn-danger layui-btn-xs'){
                RegularType.onHideItem(data,btn);
            }else{
                RegularType.onShowItem(data,btn);
            }
        }
    });
});
