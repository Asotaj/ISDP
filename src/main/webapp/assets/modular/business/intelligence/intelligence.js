var first = true;
var myChart;
layui.use(['form', 'layer', 'laydate', 'formSelects','table'], function() {
    //var $ = layui.$;
    var $ax = layui.ax;
    var layer = layui.layer;
    var table = layui.table;
    var laydate = layui.laydate;
    var admin = layui.admin;
    var form = layui.form;
    var formSelects = layui.formSelects;
    var start = new Date();

    start.setDate(start.getDate() - 7);
    var end = new Date();
    laydate.render({
        elem: '#date',
        value: formatDate(start)
    });
    laydate.render({
        elem: '#date1',
        value: formatDate(end)
    });
    //radio添加点击事件

    myChart = echarts.init(document.getElementById('main'));
    myChart.on("click",function (params) {
        console.log(params);
        window.open(Feng.ctxPath + "/intelligence/keywordMoMPage?startTime="+startTime+"&endTime="+endTime+"&keywords="+params.name)
    });

    //日期

    var Article = {
        tableId: "articleTable"  , //表格id
        condition: {
            startTime: "",
            endTime: "",
            website: "",
            category: ""
        }
    };

    /**
     * 初始化表格的列
     */
    Article.initColumn = function () {
        return [[
            {type: 'checkbox', fixed: 'left'},
            {field: 'Id', sort: true, title: 'ID',hide: true},
            {field: 'title', sort: true, title: '标题',minWidth:250,
                templet: function (d) {
                    var url = Feng.ctxPath + '/console/getArticle?id=' + d.Id;
                    var title = '<a style="color: #01AAED;" title="'+d.title+'" href="' + url + '">' + d.title + '</a>'+
                        "<br/>";
                    if(typeof d.abstract != "undefined"){
                        title += "<span>"+d.abstract+"</span>";
                    }
                    return title;
                }},
            {field: 'source', sort: true, title: '来源',width:120},
            {field: 'time', sort: true, title: '发布日期', width: 165},
            {field: 'author',  title: '作者',width: 90,minWidth:90},
            {field: 'category', title: '分类',width:130},
            {field: 'like_article_count',  title: '相似文章',width:100,
                templet: function (d) {
                    var url = Feng.ctxPath + '/intelligence/likeArticlePage?id='+d.Id;
                    return '<a style="color: #01AAED;" href="' + url + '">' + d.like_article_count + '</a>';
                }},
            {field: 'keywords', title: '关键词',width:125},
            {align: 'center', toolbar: '#tableBar',  templet: '#tableBar2',title: '操作', width: 175}
        ]];
    };
    var startTime = $("#date").val();
    var endTime = $("#date1").val();

    layer.ready(function(){
        getData(startTime,endTime);
    });
    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Article.search($("#date").val(),$("#date1").val());
        startTime = $("#date").val();
        endTime = $("#date1").val();
        $('input[type=radio][name=range]').get(0).checked=true;
    });
    //
    $('input[type=radio][name=range]').change(function() {
        if(this.value==0){
            startTime= $("#date").val();
            endTime=$("#date1").val();

            Article.search(startTime,endTime);
        }else if(this.value==1){

            var date = new Date($("#date").val().replace(/-/g,"/"));
            var date1 = new Date($("#date1").val().replace(/-/g,"/"));
            date.setDate((date.getDate()-15));
            date1.setDate((date1.getDate()-15));
            startTime=formatDate(date);
            endTime=formatDate(date1);
            Article.search(startTime,endTime);
        }else if(this.value==2){
            var date = new Date($("#date").val().replace(/-/g,"/"));
            var date1 = new Date($("#date1").val().replace(/-/g,"/"));

            date.setMonth(date.getMonth() - 1);//7天
            date1.setMonth(date1.getMonth() - 1);//7天
            startTime=formatDate(date);
            endTime=formatDate(date1);
            Article.search(startTime,endTime);
        }else if(this.value==3){
            var date = new Date($("#date").val().replace(/-/g,"/"));
            var date1 = new Date($("#date1").val().replace(/-/g,"/"));
            date.setMonth(date.getMonth() - 6);//7天
            date1.setMonth(date1.getMonth() - 6);//7天
            startTime=formatDate(date);
            endTime=formatDate(date1);
            Article.search(startTime,endTime);
        }else if(this.value==4){
            var date = new Date($("#date").val().replace(/-/g,"/"));
            var date1 = new Date($("#date1").val().replace(/-/g,"/"));
            date.setMonth(date.getMonth() - 12);//7天
            date1.setMonth(date1.getMonth() - 12);//7天
            startTime=formatDate(date);
            endTime=formatDate(date1);
            Article.search(startTime,endTime);
        }

    });
    var loadIndex;
     Article.search = function (startTime,endTime) {
        loadIndex= layer.load(2);
        var queryData = {};
        queryData['startTime'] =startTime;
        queryData['endTime'] = endTime;
        var reg = new RegExp(',',"g");
        var reg1 = new RegExp(',',"g");

        var source=formSelects.value('source', 'valStr');

        var category=formSelects.value('category', 'valStr');
        queryData['website'] = source;
        queryData['category'] = category;

        let tempOption = JSON.parse(JSON.stringify(tableOptions));
        tempOption.where = queryData;
        table.reload(Article.tableId, tempOption);

        Article.initChart(startTime,endTime);
    };

    Article.initTable = function (dictId, data) {
        let tempOption = JSON.parse(JSON.stringify(tableOptions));
        tempOption.url=Feng.ctxPath + 'intelligence/article/listByCon';
        return table.render(tempOption);
    };
    /**
     * 收藏
     */
    Article.collect = function (param) {
        $.ajax({
            url:Feng.ctxPath + "/intelligence/article_Save?article_id=" + param.Id+"&is_deleted=1",
            async: false,
            type:"get",
            dataType: "json",
            success: function(data){
                Feng.success("收藏成功!");
            },
            error: function() {
                Feng.error("收藏失败!");
            }
        })

    };

    Article.collect2 = function (param) {
        $.ajax({
            url:Feng.ctxPath + "/intelligence/article_delete?article_id=" + param.Id+"&is_deleted=1",
            async: false,
            type:"get",
            dataType: "json",
            success: function(data){
                Feng.success("取消收藏!");
            },
            error: function() {
                Feng.error("取消失败!");
            }
        })

    };

    Article.initChart = function (startTime,endTime) {
        var startTime =startTime;
        var endTime = endTime;
        var reg = new RegExp(',',"g");
        var reg1 = new RegExp('"',"g");
        var source=formSelects.value('source', 'valStr');
        var category=formSelects.value('category', 'valStr');
        var words = [];
        var count = [];
        getchar(words,count);
        $.ajax({
            url : Feng.ctxPath + "intelligence/article/listByList?startTime=" + startTime + "&endTime=" + endTime+ "&website=" + source+ "&category=" + category,
            type : "GET",
            contentType: "application/json",
            dataType: "json",
            success : function(response){
                words=response.list1;
                count=response.list2;
                getchar(words,count);
                layer.close(loadIndex);
            },
            error:function () {
                layer.close(loadIndex);
            }
        });
    };


    /**
     * 删除
     */
    Article.delete = function (data) {

        var operation = function () {
            $.ajax({
                url:Feng.ctxPath + "/intelligence/delete?id=" + data.Id,
                async: false,
                type:"get",
                dataType: "json",
                success: function(data){
                    Feng.success("删除成功!");
                    table.reload(Article.tableId);
                },
                error: function() {
                    Feng.error("删除失败!" + data.responseJSON.message + "!");
                }
            })

        };
        Feng.confirm("是否删除?", operation);
    };
    /**
     * 编辑
     */
    Article.edit = function (param) {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '编辑列表',
            content: Feng.ctxPath + 'intelligence/article_update?id=' + param.Id,
            end: function () {
                admin.getTempData('formOk') && table.reload(Article.tableId);
            }
        });
    };

    //数据表格参数
    var tableOptions = {
        elem: '#' + Article.tableId,
        url: Feng.ctxPath + 'intelligence/article/list',
        page: {curr:1},             //默认第一页
        height: "full-98",
        cellMinWidth: 100,
        cols: Article.initColumn(),
        toolbar: true,      //表头工具栏
        defaultToolbar: [
            { //自定义头部工具栏右侧图标。如无需自定义，去除该参数即可
                title: '收藏的文章'
                ,layEvent: 'savedArticle'
                ,icon: 'layui-icon-star'
            },
            {
                title: '导出'
                ,layEvent: 'articleExport'
                ,icon: 'layui-icon-download-circle'
            },
        ]
    };

    // 渲染表格
    var tableResult = table.render(tableOptions);

    // 工具条点击事件（导出按钮）
    table.on('toolbar('+Article.tableId+')', function(obj){
        var checkStatus = table.checkStatus(obj.config.id);
        switch(obj.event){
            case 'articleExport':       //导出
                var data = checkStatus.data;
                if(data.length>0){
                    var exportData=[];
                    for(let i in data){
                        exportData.push([
                            data[i]["title"],
                            data[i]["source"],
                            data[i]["time"] + "",
                            data[i]["author"],
                            data[i]["category"],
                            data[i]["keywords"],
                            data[i]["content"],
                        ])
                    }
                    table.exportFile(["标题","来源","发布日期","作者","分类","关键词","内容"], exportData); //data 为该实例中的任意数量的数据
                };
                break;
            case 'savedArticle':    //显示收藏的文章
                let tempOption = JSON.parse(JSON.stringify(tableOptions));
                tempOption.defaultToolbar = [
                    { //自定义头部工具栏右侧图标。如无需自定义，去除该参数即可
                        title: '全部文章'
                        ,layEvent: 'allArticle'
                        ,icon: 'layui-icon-home'
                    },
                    {
                        title: '导出'
                        ,layEvent: 'articleExport'
                        ,icon: 'layui-icon-download-circle'
                    },
                ];
                tempOption.url=Feng.ctxPath + 'intelligence/article/listBySave';
                tableResult.reload(tempOption);
                break;
            case 'allArticle':  //显示全部文章
                tableResult.reload(tableOptions);
                break;
        }
    });

    // 工具条点击事件
    table.on('tool(' + Article.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;

        if (layEvent === 'collect') {
            var btn = obj.tr.find("td:last a:first");
            if(btn.attr("class")=='layui-btn layui-btn-danger layui-btn-xs'){
                btn.attr("class", "layui-btn layui-btn-primary layui-btn-xs");
                Article.collect2(data);
            }else{
                btn.attr("class", "layui-btn layui-btn-danger layui-btn-xs");
                Article.collect(data);

            }
            //alert(obj.tr.find("td:last a:first").text())
        } else if (layEvent === 'delete') {
            Article.delete(data);
        } else if (layEvent === 'edit') {
            Article.edit(data);
        }
    });

    Article.initDict = function (typeId) {
        $.ajax({
            url : Feng.ctxPath + "/dict/list?dictTypeId=" + typeId,
            type : "GET",
            contentType: "application/json",
            dataType: "json",
            success : function(response){
                var options = [];
                $.each(response.data, function (i, value) {
                    options.push({"name": value.name, "value": value.code})
                });
                if(typeId == "1192261339146403841"){
                    formSelects.data('category', 'local', { arr: options});
                }else if(typeId == "1192262134399025153"){
                    formSelects.data('source', 'local', { arr: options});
                }
            }
        });
    }

    Article.initDict("1192261339146403841"); //类别
    Article.initDict("1192262134399025153"); //来源
});


function formatDate(date) {//获取当前时间
    var seperator1 = "-";
    var seperator2 = ":";
    var month = date.getMonth() + 1<10? "0"+(date.getMonth() + 1):date.getMonth() + 1;
    var strDate = date.getDate()<10? "0" + date.getDate():date.getDate();
    var currentdate = date.getFullYear() + seperator1  + month  + seperator1  + strDate;
    return currentdate;
}

function getData(startTime,endTime){
    var startTime =startTime;
    var endTime = endTime;

    var words = [];
    var count = [];
    getchar(words,count);
    $.ajax({
        url : Feng.ctxPath + "intelligence/article/listByList?startTime=" + startTime + "&endTime=" + endTime+ "&website=&category=",
        type : "GET",
        contentType: "application/json",
        dataType: "json",
        success : function(response){
            getchar(words,count);
        }
    });
}

function getchar(words,count){

    // 指定图表的配置项和数据
    var option = {
        color: ['#3398DB'],
        tooltip: {
            show: true,
            trigger: 'axis',
            axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
            }
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis: [
            {
                type: 'category',
                data: words,
                axisTick: {
                    alignWithLabel: true
                },
                axisLabel:{
                    interval: 0,  //控制坐标轴刻度标签的显示间隔.设置成 0 强制显示所有标签。设置为 1，隔一个标签显示一个标签。设置为2，间隔2个标签。以此类推
                    rotate:45,//倾斜度 -90 至 90 默认为0
                },
            }
        ],
        yAxis: [
            {
                type: 'value'
            }
        ],
        series: [
            {
                name: '数量',
                type: 'bar',
                barWidth: '60%',
                data: count,
                //标签：顶部显示柱状图数值
                label: {
                    normal: {
                        show: true,
                        position: 'top',
                        formatter: '{c}',
                        textStyle: {
                            fontSize: 12,
                            color: '#4f4f47'
                        }
                    }
                },
            }
        ]
    };

    myChart.setOption(option);
}
