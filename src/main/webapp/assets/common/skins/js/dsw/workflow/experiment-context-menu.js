/**
 * 右键菜单
 */
function ExperimentContextMenu(){

}

ExperimentContextMenu.prototype=(function(){
    /**
     * 删除节点
     */
    function removeNode(key,opt){
        var selected=d3.selectAll("div#idsw-bpmn>svg.edit g.node.active");
        var arr=[];
        selected.each(function(){
            var id = d3.select(this).attr("id");
            arr.push(id);
        });
        var param={};
        param['nodeInstanceIds']=arr.join(",");

        $.ajax({
            type: "POST",
            url: context + "/service/experiments/"+global.id+"?method=deleteNodeInstances",
            contentType: "application/json",
            data: JSON.stringify(param),
            async: false
        }).done(function(msg) {
            if(msg.success){
                workflow.removeNode(selected);
            }else{
                failed(L.getLocaleMessage('workflow.message.component.removeFailed',"删除组件失败"));
            }
        });
    }

    /**
     * 保存模型
     */
    function saveModel(key,opt){
        var selected=d3.select("div#idsw-bpmn>svg.edit g.node.active");
        var nodeId=selected.attr("id");
        $.dialog({
            title: L.getLocaleMessage('workflow.label.saveModel',"保存模型"),
            type: "iframe",
            url: context + "/v2/experiment/save-model/"+global.id+"/"+nodeId,
            width: 520,
            height: 300,
            onclose: function() {
                if(this.returnValue) {
                    var val = this.returnValue;
                    success(L.getLocaleMessage('message.success.save',"保存模型成功"));
                    updateModel();
                }
            }
        });
    }

    /**
     * 查看模型
     */
    function viewModel(key,opt){
        var selected=d3.select("div#idsw-bpmn>svg.edit g.node.active");
        var nodeId=selected.attr("id");
        var nodeText=selected.attr("text");
        $.dialog({
            title: L.getLocaleMessage('experiment.model.viewModel',"查看模型"),
            type: "iframe",
            url: context + "/v2/experiment/view-model/"+global.id+"/"+nodeId+"/"+nodeText,
            width: 800,
            height: 400,
            onclose: function() {

            }
        });
    }

    /**
    * 查看执行结果
    */
    function viewData(key,opt){
        var selected=d3.select("div#idsw-bpmn>svg.edit g.node.active");
        var nodeId=selected.attr("id");
        $.dialog({
            title: L.getLocaleMessage('label.viewData',"查看数据"),
            type: "iframe",
            url: context + "/v2/experiment/view-data/"+global.id+"/"+nodeId,
            width: 800,
            height: 400,
            onclose: function() {
            }
        });
    }

    /**
     * 查看依赖节点
     * @param {*} key 
     * @param {*} opt 
     */
    function viewDependencyGraph(key,opt){
        var selected=d3.select("div#idsw-bpmn>svg.edit g.node.active");
        var nodeId=selected.attr("id");
        var param={};
        param["nodeId"]=nodeId;
        param['graph']=JSON.stringify(workflow.graph.info());
		$.ajax({
			type: "POST",
			url: context + "/service/experiments/"+global.id+"?method=viewDependencyGraph",
			contentType: "application/json",
			data: JSON.stringify(param),
			async: false
		}).done(function(msg){
			if(msg.success){
				$.each(msg.info,function(key,value){
                    d3.select("div#idsw-bpmn>svg.edit g.node[id='"+key+"']").classed('dependency',true);
                });
			}else{
				failed(L.getLocaleMessage('workflow.message.failToViewDepGraph','查看依赖图失败!'));
			}
		});
    }

    
    /**
     * 查看下游图节点
     * @param {*} key 
     * @param {*} opt 
     */
    function viewDownStreamGraph(key,opt){
        var selected=d3.select("div#idsw-bpmn>svg.edit g.node.active");
        var nodeId=selected.attr("id");
        var param={};
        param["nodeId"]=nodeId;
        param['graph']=JSON.stringify(workflow.graph.info());
		$.ajax({
			type: "POST",
			url: context + "/service/experiments/"+global.id+"?method=viewDownStreamGraph",
			contentType: "application/json",
			data: JSON.stringify(param),
			async: false
		}).done(function(msg){
			if(msg.success){
				$.each(msg.info,function(key,value){
                    d3.select("div#idsw-bpmn>svg.edit g.node[id='"+key+"']").classed('dependency',true);
                });
			}else{
				failed(L.getLocaleMessage('workflow.message.failToViewDepGraph','查看依赖图失败!'));
			}
		});
    }

    /**
     * 查看评估报告
     * @param {*} key 
     * @param {*} opt 
     */
    function viewBinaryEval(key,opt){
        var selected=d3.select("div#idsw-bpmn>svg.edit g.node.active");
        var nodeId=selected.attr("id");
        $.dialog({
            title: L.getLocaleMessage('workflow.evaluationReport.binary',"二分类模型评估报告"),
            type: "iframe",
            url: context + "/v2/experiment/view-binary-eval/"+global.id+"/"+nodeId,
            width: 900,
            height: 450,
            onclose: function() {
            }
        });
    }

    /**
     * 查看多分类模型报告
     * @param {*} key 
     * @param {*} opt 
     */
    function viewMultiEval(key,opt){
        var selected=d3.select("div#idsw-bpmn>svg.edit g.node.active");
        var nodeId=selected.attr("id");
        $.dialog({
            title: L.getLocaleMessage('workflow.evaluationReport.multi',"多分类模型评估报告"),
            type: "iframe",
            url: context + "/v2/experiment/view-multi-eval/"+global.id+"/"+nodeId,
            width: 900,
            height: 450,
            onclose: function() {
            }
        });
    }

    /**
     * 查看回归模型评估报告
     * @param {*} key 
     * @param {*} opt 
     */
    function viewRegressorEvaluation(key,opt){
        var selected=d3.select("div#idsw-bpmn>svg.edit g.node.active");
        var nodeId=selected.attr("id");
        $.dialog({
            title: L.getLocaleMessage('workflow.evaluationReport.regression',"回归模型评估报告"),
            type: "iframe",
            url: context + "/v2/experiment/view-reg-eval/"+global.id+"/"+nodeId,
            width: 900,
            height: 450,
            onclose: function() {
            }
        });
    }

    function basicContext01(){

        // 其它右键菜单
        $.contextMenu({
            selector: "div#idsw-bpmn>svg.edit g:not([codeName^='Train']):not([codeName^='Evaluate']):not([codeName$='Plot']):not([codeName='FastTextTrain']):not([codeName='ShiftReduceTrain']):not([codeName='LexParserTrain']):not([codeName='NNDepTrain']):not([codeName$='Classification']):not([codeName$='Regression']):not([codeName$='Cluster']):not([codeName='ALS']):not([codeName='BayesTrainOperator']):not([codeName='Word2VecTrainOperator']):not([codeName='LoadModel']).node.active",
            items: {
                "remove": {
                    name: L.getLocaleMessage('label.remove',"删除"), 
                    icon:"",
                    callback: removeNode
                },
                "viewDependencyGraph":{
                    name: L.getLocaleMessage('workflow.view.dependentNode',"查看依赖节点"),
                    icon: "",
                    callback: viewDependencyGraph
                },
                "viewDownStreamGraph":{
                    name: L.getLocaleMessage('workflow.view.downStreamNode', '查看下游节点'),
                    icon:'',
                    callback: viewDownStreamGraph
                },
                "sep1": "---------",
                "viewData":{
                    name:L.getLocaleMessage('label.viewData',"查看数据"),
                    icon:"",
                    callback: viewData
                }
            }
        });
    }

    /**
     * 初始化实验上下文菜单
     */
    function initExperimentContextMenu(){

		basicContext01();

        // 连接线右键菜单
        $.contextMenu({
            selector: "div#idsw-bpmn>svg.edit path.cable-wrapper.path-active",
            items: {
                remove: {
                    name: L.getLocaleMessage('label.remove',"删除"),
                    icon:"",
                    callback: function(key, opt){
                        var edges=workflow.graph.getEdges();
                        var from={};
                        var to={};
                        var activeLine=d3.select('div#idsw-bpmn>svg.edit path.cable.path-active');
                        var conn = {
                            "from": {
                                "nodeId": activeLine.attr("from"),
                                "portIndex": activeLine.attr("output"),
                                "portId": activeLine.attr("output-id")
                            },
                            "to": {
                                "nodeId": activeLine.attr("to"),
                                "portIndex": activeLine.attr("input"),
                                "portId": activeLine.attr("input-id")
                            }
                        };
                        $.ajax({
                            type: "POST",
                            url: context + "/service/experiments/"+global.id+"?method=removeDependency",
                            contentType: "application/json",
                            data: JSON.stringify(conn),
                            async: false
                        }).done(function(msg) {
                            if(msg.success){
                                workflow.graph.removeEdge(conn.from,conn.to);
                                d3.selectAll("div#idsw-bpmn>svg.edit path.path-active").remove();
                            }else{
                                failed(L.getLocaleMessage('workflow.message.failToAddWire',"添加连接线失败"));
                            }
                        });

                    }
                }
            }
        });

        // fasttext训练
        $.contextMenu({
            selector: "div#idsw-bpmn>svg.edit g[codeName='FastTextTrain'].node.active",
            items: {
                "remove": {
                    name: L.getLocaleMessage('label.remove',"删除"),
                    icon:"fas fa-trash-alt",
                    callback: removeNode
                },
                "sep1": "---------",
                "saveModel":{
                    name:L.getLocaleMessage('workflow.label.saveModel',"保存模型"),
                    icon:"",
                    callback: saveModel
                }
            }
        });

        // 统计分词模型训练
        $.contextMenu({
            selector: "div#idsw-bpmn>svg.edit g[codeName='NatureDictionaryMaker'].node.active",
            items: {
                "remove": {
                    name: L.getLocaleMessage('label.remove',"删除"), 
                    icon:"",
                    callback: removeNode
                },
                "sep1": "---------",
                "saveModel":{
                    name:L.getLocaleMessage('workflow.label.saveModel',"保存模型"),
                    icon:"",
                    callback: saveModel
                }
            }
        });
    
        //LDA话题识别模型训练
        $.contextMenu({
            selector: "div#idsw-bpmn>svg.edit g[codeName='LDATrain'].node.active",
            items: {
                "remove": {
                    name: L.getLocaleMessage('label.remove',"删除"),
                    icon:"", 
                    callback: removeNode
                },
                "sep1": "---------",
                "saveModel":{
                    name:L.getLocaleMessage('workflow.label.saveModel',"保存模型"),
                    icon:"",
                    callback: saveModel
                }
            }
        });
        
        /*// 基于ShiftReduce模型训练右键菜单
        $.contextMenu({
            selector: "div#idsw-bpmn>svg.edit g[codeName='ShiftReduceTrain'].node.active",
            items: {
                "remove": {
                    name: "删除", 
                    icon:"",
                    callback: removeNode
                },
                "sep1": "---------",
                "saveModel":{
                    name:"保存模型",
                    icon:"",
                    callback: saveModel
                },
                "viewModel":{
                    name:"查看模型报告",
                    icon:"",
                    callback: viewModel
                }
            }
        });*/
        
        /*// 基于LexParser模型训练右键菜单
        $.contextMenu({
            selector: "div#idsw-bpmn>svg.edit g[codeName='LexParserTrain'].node.active",
            items: {
                "remove": {
                    name: "删除",
                    icon:"",
                     callback: removeNode
                },
                "sep1": "---------",
                "saveModel":{
                    name:"保存模型",
                    icon:"",
                    callback: saveModel
                }
            }
        });*/
        
        //序列标注模型训练
        $.contextMenu({
            selector: "div#idsw-bpmn>svg.edit g[codeName='CRFTrain'].node.active",
            items: {
                "remove": {
                    name: L.getLocaleMessage('label.remove',"删除"), 
                    icon:"",
                    callback: removeNode
                },
                "sep1": "---------",
                "saveModel":{
                    name:L.getLocaleMessage('workflow.label.saveModel',"保存模型"),
                    icon:"",
                    callback: saveModel
                }
            }
        });
    
        // 二分类模型评估右键菜单
        $.contextMenu({
            selector: "div#idsw-bpmn>svg.edit g:not([codeName^='Train'])[codeName=EvaluateBinaryClassifier].node.active",
            items: {
                "remove": {
                    name: L.getLocaleMessage('label.remove',"删除"),
                    icon:"",
                    callback: removeNode
                },
                "sep1": "---------",
                "viewBinaryEvaluation":{
                    name:L.getLocaleMessage('workflow.view.evaluationReport',"查看评估报告"),
                    icon:"",
                    callback: viewBinaryEval
                },
                "viewData":{
                    name:L.getLocaleMessage('label.viewData',"查看数据"),
                    icon:"",
                    callback: viewData
                }
            }
        });
    
        // 多分类模型评估右键菜单
        $.contextMenu({
            selector: "div#idsw-bpmn>svg.edit g:not([codeName^='Train'])[codeName=EvaluateMultiClassClassifier].node.active",
            items: {
                "remove": {
                    name: L.getLocaleMessage('label.remove',"删除"), 
                    icon:"",
                    callback: removeNode
                },
                "sep1": "---------",
                "viewMultiEvaluation":{
                    name:L.getLocaleMessage('workflow.view.evaluationReport',"查看评估报告"),
                    icon:"",
                    callback: viewMultiEval
                },
                "viewData":{
                    name:L.getLocaleMessage('label.viewData',"查看数据"),
                    icon:"",
                    callback: viewData
                }
            }
        });
    
        // 回归模型评估右键菜单
        $.contextMenu({
            selector: "div#idsw-bpmn>svg.edit g:not([codeName^='Train'])[codeName=EvaluateRegressor].node.active",
            items: {
                "remove": {
                    name: L.getLocaleMessage('label.remove',"删除"),
                    icon:"",
                    callback: removeNode
                },
                "sep1": "---------",
                "viewRegressorEvaluation":{
                    name:L.getLocaleMessage('workflow.view.evaluationReport',"查看评估报告"),
                    icon:"",
                    callback: viewRegressorEvaluation
                },
                "viewData":{
                    name:L.getLocaleMessage('label.viewData',"查看数据"),
                    icon:"",
                    callback: viewData
                }
            }
        });
    
        // 聚类模型评估右键菜单
        $.contextMenu({
            selector: "div#idsw-bpmn>svg.edit g:not([codeName^='Train'])[codeName=EvaluateClustering].node.active",
            items: {
                "remove": {
                    name: L.getLocaleMessage('label.remove',"删除"),
                    icon:"",
                    callback: removeNode
                },
                "sep1": "---------",
                "viewClusteringEvaluation":{
                    name:L.getLocaleMessage('workflow.view.evaluationReport',"查看评估报告"),
                    icon:"",
                    callback: function(key,opt){
                        var selected=d3.select("div#idsw-bpmn>svg.edit g.node.active");
                        var nodeId=selected.attr("id");
                        $.dialog({
                            title: L.getLocaleMessage('workflow.evaluationReport.cluster',"聚类模型评估报告"),
                            type: "iframe",
                            url: context + "/v2/experiment/view-cluster-eval/"+global.id+"/"+nodeId,
                            width: 900,
                            height: 450,
                            onclose: function() {
                            }
                        });
                    }
                },
                "viewData":{
                    name:L.getLocaleMessage('label.viewData',"查看数据"),
                    icon:"",
                    callback: viewData
                }
            }
        });
    
        // 自动分类模型评估右键菜单
        $.contextMenu({
            selector: "div#idsw-bpmn>svg.edit g:not([codeName^='Train'])[codeName=EvaluateAutoClassifier].node.active",
            items: {
                "remove": {
                    name: L.getLocaleMessage('label.remove',"删除"),
                    icon:"",
                    callback: removeNode
                },
                "sep1": "---------",
                "viewAutoClassificationEval":{
                    name:L.getLocaleMessage('workflow.view.evaluationReport',"查看评估报告"),
                    icon:"",
                    callback: function(key,opt){
                        var selected=d3.select("div#idsw-bpmn>svg.edit g.node.active");
                        var nodeId=selected.attr("id");
                        $.dialog({
                            title: L.getLocaleMessage('workflow.view.autoClass.evaluationReport',"查看自动分类评估报告"),
                            type: "iframe",
                            url: context + "/v2/experiment/view-auto-classify-eval/"+global.id+"/"+nodeId,
                            width: 900,
                            height: 450,
                            onclose: function() {
    
                            }
                        });
                    }
                },
                "viewData":{
                    name:L.getLocaleMessage('label.viewData',"查看数据"),
                    icon:"",
                    callback: viewData
                }
            }
        });
    
        // 自动回归模型评估右键菜单
        $.contextMenu({
            selector: "div#idsw-bpmn>svg.edit g:not([codeName^='Train'])[codeName=EvaluateAutoRegressor].node.active",
            items: {
                "remove": {
                    name: L.getLocaleMessage('label.remove',"删除"),
                    icon:"", 
                    callback: removeNode
                },
                "sep1": "---------",
                "viewAutoRegEval":{
                    name:L.getLocaleMessage('workflow.view.evaluationReport',"查看评估报告"),
                    icon:"",
                    callback: function(key,opt){
                        var selected=d3.select("div#idsw-bpmn>svg.edit g.node.active");
                        var nodeId=selected.attr("id");
                        $.dialog({
                            title: L.getLocaleMessage('workflow.view.autoRegress.evaluationReport',"查看自动回归评估报告"),
                            type: "iframe",
                            url: context + "/v2/experiment/view-reg-eval/"+global.id+"/"+nodeId,
                            width: 900,
                            height: 450,
                            onclose: function() {
                            }
                        });
                    }
                },
                "viewData":{
                    name:L.getLocaleMessage('label.viewData',"查看数据"),
                    icon:"",
                    callback: viewData
                }
            }
        });
    
        // 画图菜单
        $.contextMenu({
            selector: "div#idsw-bpmn>svg.edit g[codeName$='Plot'].node.active",
            items: {
                "remove": {
                    name: L.getLocaleMessage('label.remove',"删除"),
                    icon:"",
                    callback: removeNode
                },
                "sep1": "---------",
                "viewPlot":{
                    name:L.getLocaleMessage('workflow.view.graphic',"查看图形"),
                    icon:"",
                    callback: function(key,opt){
                        var selected=d3.select("div#idsw-bpmn>svg.edit g.node.active");
                        var nodeId=selected.attr("id");
                        $.dialog({
                            title: L.getLocaleMessage('workflow.view.graphic',"查看图形"),
                            type: "iframe",
                            url: context + "/v2/experiment/view-plot/"+global.id+"/"+nodeId,
                            width: 900,
                            height: 450,
                            onclose: function() {
                            }
                        });
                    }},
                "viewData":{
                    name:L.getLocaleMessage('label.viewData',"查看数据"),
                    icon:"",
                    callback: viewData
                }
            }
        });
    
        // 选择算法
        $.contextMenu({
            selector: "div#idsw-bpmn>svg.edit g[codeName$='Classification'].node.active, div#idsw-bpmn>svg.edit g[codeName$='Regression'].node.active, div#idsw-bpmn>svg.edit g[codeName='ShiftReduceTrain'].node.active, div#idsw-bpmn>svg.edit g[codeName='NNDepTrain'].node.active, div#idsw-bpmn>svg.edit g[codeName='LexParserTrain'].node.active,div#idsw-bpmn>svg.edit g[codeName='ALS'].node.active, div#idsw-bpmn>svg.edit g[codeName='BayesTrainOperator'].node.active, div#idsw-bpmn>svg.edit g[codeName='Word2VecTrainOperator'].node.active, div#idsw-bpmn>svg.edit g[codeName=LoadModel].node.active",
            items: {
                "remove": {
                    name: L.getLocaleMessage('label.remove',"删除"),
                    icon:"",
                    callback:  removeNode
                },
                "sep1": "---------",
                "viewModel":{
                    name:L.getLocaleMessage('experiment.model.viewModel',"查看模型"),
                    icon:"",
                    callback: viewModel
                },
                "saveModel":{
                    name:L.getLocaleMessage('workflow.label.saveModel',"保存模型"),
                    icon:"",
                    callback: saveModel
                }
            }
        });

        // 聚类算法
        $.contextMenu({
            selector: "div#idsw-bpmn>svg.edit g[codeName$='Cluster'].node.active",
            items: {
                "remove": {
                    name: L.getLocaleMessage('label.remove',"删除"),
                    icon:"",
                    callback:  removeNode
                },
                "sep1": "---------",
                "viewModel":{
                    name:L.getLocaleMessage('experiment.model.viewModel',"查看模型"),
                    icon:"",
                    callback: viewModel
                },
                "saveModel":{
                    name:L.getLocaleMessage('workflow.label.saveModel',"保存模型"),
                    icon:"",
                    callback: saveModel
                },
                "viewData":{
                    name:L.getLocaleMessage('label.viewData',"查看数据"),
                    icon:"",
                    callback: viewData
                }
            }
        });

    }
    

	return {
        version: '1.0',
        init: initExperimentContextMenu
	}
})();

function updateModel() {
    $li = $("#folder li[data-id='mymodels']");
    // 加载存在的模型文件
    $.ajax({
        url: context + "/service/models",
        // data: {
        // 	"kernel": workflow.graph.getConf('kernel')
        // }
    }).done(function (json) {
        $li.find("ul").remove();
        var arr = ['<ul>'];
        for (var i = 0, len = json.length; i < len; i++) {
            arr.push('<li class="node" data-id="mymodels"');
            arr.push(' data-model="' + json[i].id + '"');
            arr.push(' data-model-url="' + json[i].path + '"');
            arr.push(' data-input-count="0" data-output-count="1">');
            arr.push('  <a title="');
            arr.push(json[i].name);
            arr.push('">');
            arr.push('    <i class="fa fa-diamond"></i>');
            arr.push('    <span>' + json[i].name + '</span>');
            arr.push('  </a>');
            arr.push('</li>');
        }
        if (arr.length == 1) {
            arr.push('<li class="text-center"><spring:message code="label.status.noModel" text="无模型文件"/></li>');
        }
        arr.push('</ul>');
        $li.append(arr.join(''));
        $li.addClass('open');
        $li.find(">a>i:first").removeClass('fa-caret-right').addClass('fa-caret-down');
        $li.find(">a>i:last").removeClass('fa-folder-o').addClass('fa-folder-open-o');
        $li.find(">ul").show();
    });
}


