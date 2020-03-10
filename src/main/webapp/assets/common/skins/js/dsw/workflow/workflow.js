function WorkFlow() {
	this._queue = [];
	this.graph = new Graph();
	this._ICON_UNICODE = {
	  'fa-database': '\uf1c0',
	  'fa-diamond': '\uf219',
	  'fa-random': '\uf074',
	  'fa-crosshairs': '\uf05b',
	  'fa-circle-o': '\uf10c',
	  'fa-life-ring': '\uf1cd',
	  'fa-file-o': '\uf016',
	  'fa-crosshairs': '\uf05b',
	  'fa-table': '\uf0ce',
	  'fa-dot-circle-o': '\uf192',
	  'fa-cubes': '\uf1b3',
	  'fa-file': '\uf15b',
	  'fa-file-text':'\uf15c',
	  'fa-file-text-o': '\uf0f6',
      'icon-idsw-model':'\ue903',
      'icon-idsw-service':'\ue904',
      'icon-idsw-network':'\ue911',
      'icon-idsw-pythoncode':'\ue910',
      'icon-idsw-statistics':'\ue90f',
      'icon-idsw-timeseries':'\ue90e',
      'icon-idsw-graph':'\ue90b',
      'icon-idsw-dataprocess':'\ue905',
      'icon-idsw-dataset':'\ue909',
      'icon-idsw-feature':'\ue908',
      'icon-idsw-fileformat':'\ue907',
      'icon-idsw-ml':'\ue906',
      'icon-idsw-inout':'\ue90a',
      'icon-idsw-userdefined':'\ue901',
      'icon-idsw-images':'\ue900'
	};
	this._WIDTH = 180;
	this._HEIGHT = 36;
}

WorkFlow.prototype = (function() {
	var _WIDTH = 180;
	var _HEIGHT = 36;
	var activeLine = null;
	var points = [];
	var translate = null;
	var drawLine = false;
	
	function linestarted() {
		drawLine = false;
		// 当前选中的circle
		var anchor = d3.select(this);
		// 当前选中的节点
		var node = d3.select(this.parentNode);
		var dx = _WIDTH * (+anchor.attr("output"))/ (+node.attr("outputs") + 1);
		var dy = _HEIGHT;
		var transform = node.attr("transform");
	    translate = getTranslate(transform);
		points.push([dx + translate[0], dy + translate[1]]);
		activeLine = d3.select("#g-drag")
		  .append("path")
		  .attr("class", "cable")
		  .attr("from", node.attr("id"))
		  .attr("start", dx + ", " + dy)
		  .attr("output", d3.select(this).attr("output"))
		  .attr("output-id",d3.select(this).attr("port-id"))
		  .attr("output-type",d3.select(this).attr("data-type"))
		  .attr("marker-start", "url(#marker-circle)")
		  .attr("marker-end", "url(#marker-circle)");
	}
	
	function linedragged() {
		drawLine = true;
		points[1] = [d3.event.x + translate[0], d3.event.y + translate[1]];
		activeLine.attr("d", function() {
			return "M" + points[0][0] + "," + points[0][1]
	        + "C" + points[0][0] + "," + (points[0][1] + points[1][1]) / 2
	        + " " + points[1][0] + "," +  (points[0][1] + points[1][1]) / 2
	        + " " + points[1][0] + "," + points[1][1];
		});
	}

	function lineended(d) {
		drawLine = false;
		var anchor = d3.selectAll("g.end");
		var outputType=activeLine.attr("output-type");
		
		if(anchor.empty()) {
			activeLine.remove();
		} else{
			
			var inType=anchor.attr("data-type");
			if(outputType!==inType){
				activeLine.remove();
				anchor.classed("end", false);
			}else{
				var pNode = d3.select(anchor.node().parentNode);
				var offsetX = _WIDTH * (+anchor.attr("input")) / (+pNode.attr("inputs") + 1);
				anchor.classed("end", false);
				activeLine.attr("to", pNode.attr("id"));
				activeLine.attr("input", anchor.attr("input"));
				activeLine.attr("input-id", anchor.attr("port-id"));
				activeLine.attr("end", offsetX + ", 0");

				// 实现连接点吸附效果
				var t = getTranslate(pNode.attr("transform"));
				points[1] = [t[0] + offsetX, t[1]];
				activeLine.attr("d", function() {
					return "M" + points[0][0] + "," + points[0][1]
			        + "C" + points[0][0] + "," + (points[0][1] + points[1][1]) / 2
			        + " " + points[1][0] + "," +  (points[0][1] + points[1][1]) / 2
			        + " " + points[1][0] + "," + points[1][1];
				});
				
				// 添加connection
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

				// 判断是否已经存在连接线
				if(workflow.graph.edgeExist(conn)) {
					failed(L.getLocaleMessage('workflow.message.alreadyAddWire','已经添加连线'));
					activeLine.remove();
				} else {
					// 类型判断
			    	$.ajax({
						type: "POST",
						url: context + "/service/experiments/"+global.id+"?method=addDependency",
						contentType: "application/json",
						data: JSON.stringify(conn),
						async: false
					}).done(function(msg) {
						if(msg.success){
							
						}else{
							failed(L.getLocaleMessage('workflow.message.failToAddWire',"添加连接线失败"));
						}
					});
					workflow.graph.addEdge(conn);
					
					d3.select("#g-drag")
					  .append("path")
					  .attr("class", "cable-wrapper")
					  .attr("d", function() {
					return "M" + points[0][0] + "," + points[0][1]
			        + "C" + points[0][0] + "," + (points[0][1] + points[1][1]) / 2
			        + " " + points[1][0] + "," +  (points[0][1] + points[1][1]) / 2
			        + " " + points[1][0] + "," + points[1][1];
					  });
				}
				
				
			}
	
		}
		activeLine = null;
		points.length = 0;
		translate = null;
	}

	function getTranslate(transform) {
		// IE浏览器下transorflow使用空格分割,chrome使用逗号分割
		if(transform.indexOf(',') != -1) {
			var arr = transform.substring(transform.indexOf("(")+1, transform.indexOf(")")).split(",");
			return [+arr[0], +arr[1]];
		} else {
			var arr = transform.substring(transform.indexOf("(")+1, transform.indexOf(")")).split(" ");
			return [+arr[0], +arr[1]];
		}
	}

	// 存储当前可拖拽(active)的node信息
	var dx = {};
	var dy = {};
	var dragElements = {};
	
	function dragstarted() {
		var selected = d3.selectAll("g.node.active");
		// 因为单击node会清除其他node的active状态。
		// 因此判断拖动对象是否为已经框选(active)的节点
		if (this.classList.contains('active')){
			$('#'+global.nodetooltipid+'').remove();
			selected.each(function(){
				var transform = d3.select(this).attr("transform");
				var translate = getTranslate(transform);
				var nodeId = this.id;
				dx[nodeId] = d3.event.x - translate[0];
				dy[nodeId] = d3.event.y - translate[1];
				dragElements[nodeId] = d3.select(this);
			})
		}else{
			var transform = d3.select(this).attr("transform");
			var translate = getTranslate(transform);
			var nodeId = this.id;
			dx[nodeId] = d3.event.x - translate[0];
			dy[nodeId] = d3.event.y - translate[1];
			dragElements[nodeId] = d3.select(this);
			
			d3.selectAll("g.node").classed('active', false);
			dragElements[nodeId].classed('active', true);
			$('#'+global.nodetooltipid+'').remove();
			$("#" + dragElements[nodeId].attr("id")).click();
		}
		
	}

	function dragged() {
	  var selected = d3.selectAll("g.node.active");
	  selected.each(function(){
		  var nodeId = this.id;
		  dragElements[nodeId].attr("transform", "translate(" + (d3.event.x - dx[nodeId]) + ", " + (d3.event.y - dy[nodeId]) + ")");
		  updateCable(dragElements[nodeId]);
	  })
	}

	function updateCable(elem) {
		var width = this._WIDTH;
		var height = this._HIGHT;
		var id = elem.attr("id");
	    var transform = elem.attr("transform");
	    var t1 = getTranslate(transform);
		
		// 更新输出线的位置
		d3.selectAll('path[from="' + id + '"]')
		  .each(function() {
		  	var start = d3.select(this).attr("start").split(",");
		  	start[0] = +start[0] + t1[0];
		  	start[1] = +start[1] + t1[1];

		  	var path = d3.select(this).attr("d");
		  	if(path.indexOf(",") != -1) {
		  		var end = path.substring(path.lastIndexOf(" ") + 1).split(",");
		  	} else {
		  		var end = path.split(" ").slice(-2);
		  	}
		  	end[0] = +end[0];
		  	end[1] = +end[1];
		  	
		  	d3.select(this).attr("d", function() {
		  		return "M" + start[0] + "," + start[1]
	        	+ " C" + start[0] + "," + (start[1] + end[1]) / 2
	        	+ " " + end[0] + "," +  (start[1] + end[1]) / 2
	        	+ " " + end[0] + "," + end[1];
		  	});
		  	
		  	d3.select($(this).next()[0]).attr("d", function() {
		  		return "M" + start[0] + "," + start[1]
	        	+ " C" + start[0] + "," + (start[1] + end[1]) / 2
	        	+ " " + end[0] + "," +  (start[1] + end[1]) / 2
	        	+ " " + end[0] + "," + end[1];
		  	});
		  });

		// 更新输入线的位置
		d3.selectAll('path[to="' + id + '"]')
		  .each(function() {
		  	var path = d3.select(this).attr("d");
		  	// IE下使用空格分割,chrome使用逗号分割
		  	if(path.indexOf(",") != -1) {
		  		var start = path.substring(1, path.indexOf("C")).split(",");
		  	} else {
		  		var start = path.split(" ").slice(1, 3);
		  	}
		  	
		  	start[0] = +start[0];
		  	start[1] = +start[1];

		  	var end = d3.select(this).attr("end").split(",");
		  	end[0] = +end[0] + t1[0];
		  	end[1] = +end[1] + t1[1];
		  	d3.select(this).attr("d", function() {
		  		return "M" + start[0] + "," + start[1]
	        	+ " C" + start[0] + "," + (start[1] + end[1]) / 2
	        	+ " " + end[0] + "," +  (start[1] + end[1]) / 2
	        	+ " " + end[0] + "," + end[1];
		  	});
		  	
		  	d3.select($(this).next()[0]).attr("d", function() {
		  		return "M" + start[0] + "," + start[1]
	        	+ " C" + start[0] + "," + (start[1] + end[1]) / 2
	        	+ " " + end[0] + "," +  (start[1] + end[1]) / 2
	        	+ " " + end[0] + "," + end[1];
		  	});
		  });

	}

	function dragended() {
		var dx = {};
		var dy = {};
		var t = {};
		var flag = true;
		// 更新node坐标
		if(Object.keys(dragElements).length) {
		      var param={};
			  var arr=[];
			  var selected = d3.selectAll("g.node.active");
			  selected.each(function(){
				  var nodeId = this.id;
				  t[nodeId] = getTranslate(dragElements[nodeId].attr("transform"));
				  var node = workflow.graph.getNodeById(nodeId);
				  if(t[nodeId][0]==node.x && t[nodeId][1]==node.y){
					  flag = false;
				  }
				  node.x = t[nodeId][0];
				  node.y = t[nodeId][1];
				  arr.push(node);
				  param['newPos']=arr;
			  })
			  if (flag){
				  $.ajax({
					  type: "POST",
					  url: context + "/service/experiments/"+global.id+"?method=batchUpdateNodeInstancePosition",
					  contentType: "application/json",
					  async: false,
					  data: JSON.stringify(param)
				  }).done(function(msg) {
					  if(msg.success){
						  
					  }else{
						  failed(L.getLocaleMessage('workflow.message.failToUpdateLocation',"更新实验组件位置失败"));
					  }
				  });
			  }
		}
		dragElements = {};
		
	}

	function detectIE() {
	    var ua = window.navigator.userAgent;

	    var msie = ua.indexOf('MSIE ');
	    if (msie > 0) {
	        // IE 10 or older => return version number
	        return parseInt(ua.substring(msie + 5, ua.indexOf('.', msie)), 10);
	    }

	    var trident = ua.indexOf('Trident/');
	    if (trident > 0) {
	        // IE 11 => return version number
	        var rv = ua.indexOf('rv:');
	        return parseInt(ua.substring(rv + 3, ua.indexOf('.', rv)), 10);
	    }

	    var edge = ua.indexOf('Edge/');
	    if (edge > 0) {
	       // Edge (IE 12+) => return version number
	       return parseInt(ua.substring(edge + 5, ua.indexOf('.', edge)), 10);
	    }

	    // other browser
	    return false;
	}
	
	function updateNode(id, msg) {
		var current = d3.select("g.node[id='" + id + "']");
		if('RUNNING' == msg.status) {
			current.classed("running", true);
			current.select('.state').attr("fill", "#0275d8").text("\uf013");
			d3.selectAll("path.cable[from='" + id + "']").classed("running", true);
		} else {
			current.classed("running", false);
			d3.selectAll("path.cable").classed("running", false);
			if('OK' == msg.status) {
				current.select('.state').attr("fill", "#5cb85c").text("\uf058");
			} else if('ERROR' == msg.status) {
				current.select('.state').attr("fill", "#d9534f").text("\uf06a");
			} else{
				current.select('.state').attr("fill", "#333").text("\uf017");
			}
			workflow.graph.getNodeById(id)['log'] = msg.output;
		}
	}
	
	return {
		graph: this.graph,		
		removeNode: function(selected) {
			var that = this;
			selected.each(function(d, i) {
	          var id = d3.select(this).attr("id");
	          if(id) {
	            d3.selectAll('.bpmn path.cable[from="' + id + '"]').remove();
	            d3.selectAll('.bpmn path.cable[to="' + id + '"]').remove();
	           
	            that.graph.removeNode(id);
	          }
	        });
	        selected.remove();
		},
		addNode: function(svg, node) {
			this.graph.addNode(node);
			var g = d3.select("#g-drag").append("g")
	           .attr("class", "node")
	           .attr("data-id", node.dataId)
	           .attr("id", node.id)
	           .attr('codeName',node.codeName)
	           /*这是组件英文名称*/
	           .attr('name',node.name)
	           /*这是组件中文名称，比如拆分*/
	           .attr('text',node.text)
	           /*这是组件全名，比如说拆分-1*/
	           .attr("transform", 'translate(' + node.x + ', ' + node.y + ')');
			
			var rect = g.append("rect")
			.attr("class","rect")
			 .attr("rx", 5)
			 .attr("ry", 5)
			 .attr("stroke-width", 2)
			 .attr("stroke", "#333")
			 .attr("fill", "#fff")
			 .attr("width", this._WIDTH)
			 .attr("height", this._HEIGHT);
		
			// 如果node.text内容过多，则将其省略表示,记作nodeText
			var nodeText=node.text;
			if(nodeText.length>10){
				nodeText=nodeText.substring(0,10)+"...";
			}
			g.append("text")
			 .text(nodeText)
			 .attr("x", this._WIDTH / 2)
			 .attr("y", this._HEIGHT / 2)
			 .attr("text-anchor", "middle")
			 .style('pointer-events', 'none')
			 .attr('dy', '0.5ex')
			 .attr("clip-path", "url(#clip)");
			
					
			// left icon
			g.append('text')
			 .attr("x", 18)
			 .attr("y", this._HEIGHT / 2)
			 .attr("text-anchor", "middle")
			 .attr('font-family', 'FontAwesome')
			 .text(this._ICON_UNICODE[node.icon])
			 .style('pointer-events', 'none')
			 .attr('dy', '0.7ex');
		
			//right icon
			var g2 = g.append("g")
			          .attr("transform", 'translate(' + (this._WIDTH - 18) + ', ' + (this._HEIGHT / 2) + ')')
			          .style('pointer-events', 'none');
			
			g2.append('text')
			  .attr("class", "state")
			  .attr("text-anchor", "middle")
			  .attr('font-family', 'FontAwesome')
			  .text('\uf017')
			  .style('pointer-events', 'none')
			  .attr('dy', '0.7ex');
			
			if('OK' == node.status) {
				g2.select(".state").attr("fill", "#5cb85c").text("\uf058");
			} else if('ERROR' == node.status) {
				g2.select(".state").attr("fill", "#d9534f").text("\uf06a");
			} else {
				g2.select(".state").attr("fill", "#333").text("\uf017");
			}
		
			// input circle
			var inputs = node.inputs || 0;
			g.attr("inputs", inputs);
			for(var i = 0; i < inputs; i++) {
				var endpoint = g.append("g")
								.attr("class", "input classforport")
								.attr("input", (i + 1))
								.attr("data-type",node.inPorts[i]["dataType"])
								.attr("port-id",node.inPorts[i]["id"])
								.attr("transform", "translate(" + (this._WIDTH * (i + 1) / (inputs + 1)) + ", 0)")
								.attr("description",node.inPorts[i]["description"]);
				
				endpoint.append("circle")
						.attr("class", "outer")
						.attr("r", 8)
						.attr("fill", "none")
						.attr("stroke", 'none');
		
				endpoint.append("circle")
						.attr("class", "inner")
						.attr("r", 5)
						.attr("fill", "none")
						.attr("stroke", '#333');
			}
		
			// output circle
			var outputs = node.outputs || 0;
			g.attr("outputs", outputs);
			for(i = 0; i < outputs; i++) {
				var endpoint = g.append("g")
								.attr("class", "output classforport")								
								.attr("output", (i + 1))
								.attr("data-type",node.outPorts[i]["dataType"])
								.attr("port-id",node.outPorts[i]["id"])
								.attr("transform", "translate(" + (this._WIDTH * (i + 1) / (outputs + 1)) + ", " + this._HEIGHT + ")")
								.attr("description",node.outPorts[i]["description"]);
				
				endpoint.append("circle")
						.attr("class", "outer")
						.attr("r", 8)
						.attr("fill", "none")
						.attr("stroke", 'none');
				endpoint.append("circle")
						.attr("class", "inner")
						.attr("r", 5)
						.attr("fill", "none")
						.attr("stroke", '#333');
			}
			
			// remove icon
			var gr = g.append('g')
			          .attr("class", "remove")
			          .attr("node", node.id)
			          .attr("transform", "translate(" + (this._WIDTH + 8) + ", 10)");
			gr.append('text')
			  .attr("text-anchor", "middle")
			  .attr('font-family', 'FontAwesome')
			  .text('\uf00d');
			
			
			g.call(
				d3.drag()
			  	  .on("start", dragstarted)
			  	  .on("drag", dragged)
			  	  .on("end", dragended)
			);
			


			g.selectAll("g.output").call(
				d3.drag()
				  .on("start", linestarted)
				  .on("drag", linedragged)
				  .on("end", lineended)
			);

			g.selectAll("g.input").on("mouseover", function() {
			 	if(drawLine) {
			 		d3.selectAll("g.end").classed("end", false);
			 	    d3.select(this).classed("end", true);
			 	}
			});
			
			g.on()
			return g;
		},
		addEdge: function(conn) {
			var fromNode = this.graph.getNodeById(conn.from['nodeId']);
			var toNode = this.graph.getNodeById(conn.to['nodeId']);
			
			var points = [];
			var dx1 = this._WIDTH * (conn.from.portIndex) / (fromNode.outputs + 1);
			points.push([dx1 + fromNode.x, this._HEIGHT + fromNode.y]);
			var dx2 = this._WIDTH * (conn.to.portIndex) / (toNode.inputs + 1);
			points.push([dx2 + toNode.x, toNode.y]);
			var line = d3.select("#g-drag")
			  .append("path")
			  .attr("class", "cable")
			  .attr("from", conn.from.nodeId)
			  .attr("start", dx1 + ", " + this._HEIGHT)
			  .attr("output", conn.from.portIndex)
			  .attr("output-id",conn.from.portId)
			  .attr("to", conn.to.nodeId)
			  .attr("end", dx2 + ", 0")
			  .attr("input", conn.to.portIndex)
			  .attr("input-id",conn.to.portId)
			  .attr("marker-start", "url(#marker-circle)")
			  .attr("marker-end", "url(#marker-circle)")
			  .attr("d", function() {
					return "M" + points[0][0] + "," + points[0][1]
			        + "C" + points[0][0] + "," + (points[0][1] + points[1][1]) / 2
			        + " " + points[1][0] + "," +  (points[0][1] + points[1][1]) / 2
			        + " " + points[1][0] + "," + points[1][1];
			   });
			
			d3.select("#g-drag")
			  .append("path")
			  .attr("class", "cable-wrapper")
			  .attr("d", function() {
					return "M" + points[0][0] + "," + points[0][1]
			        + "C" + points[0][0] + "," + (points[0][1] + points[1][1]) / 2
			        + " " + points[1][0] + "," +  (points[0][1] + points[1][1]) / 2
			        + " " + points[1][0] + "," + points[1][1];
			   });
			this.graph.addEdge(conn);
		},
		getDataSetNode: function() {
		   var nodes = this.graph.getNodes();
		   for(var i = 0, len = nodes.length; i < len; i++) {
			   if(nodes[i].operation == 'mydataset') {
				   return nodes[i];
			   }
		   }
		},
		getTranslate: function(transform) {
			// IE浏览器下transorflow使用空格分割,chrome使用逗号分割
			if(transform.indexOf(',') != -1) {
				var arr = transform.substring(transform.indexOf("(")+1, transform.indexOf(")")).split(",");
				return [+arr[0], +arr[1]];
			} else {
				var arr = transform.substring(transform.indexOf("(")+1, transform.indexOf(")")).split(" ");
				return [+arr[0], +arr[1]];
			}
		},
		updateNode:function(id, msg) {
			var current = d3.select("g.node[id='" + id + "']");
			if('RUNNING' == msg.status || "READY"==msg.status) {
				current.classed("running", true);
				current.select('.state').attr("fill", "#0275d8").text("\uf013");
				d3.selectAll("path.cable[from='" + id + "']").classed("running", true);
			} else {
				current.classed("running", false);
				d3.selectAll("path.cable[from='" + id + "']").classed("running", false);
				if('OK' == msg.status) {
					current.select('.state').attr("fill", "#5cb85c").text("\uf058");
				} else if('ERROR' == msg.status) {
					current.select('.state').attr("fill", "#d9534f").text("\uf06a");
				} else {
					current.select('.state').attr("fill", "#333").text("\uf017");
				}
				//workflow.graph.getNodeById(id)['log'] = msg.output;
			}
		},
		selectPathBySelectedNode:function(activeNodes){
			// 将所有activeNode的ID存在一个数据中方便进行包含查询
			var activeNodesId = [];
			activeNodes.each(function(){
				activeNodesId.push(this.id);
			})
			
			var allPath = d3.selectAll("path.cable");
			allPath.classed("path-active", false);
			allPath.each(function(){
				var fromNodeId = d3.select(this).attr("from");
				var toNodeId = d3.select(this).attr("to");
				if($.inArray(fromNodeId,activeNodesId)!=-1 && $.inArray(toNodeId,activeNodesId)!=-1){
					d3.select(this).classed("path-active", true);
					d3.select($(this).next()[0]).classed("path-active",true);
				}
			})
		}
		
	}
})();
