function Graph() {
	this._nodes = [];
	this._edges = [];
	this._conf = {
		"kernel": "sklearn"
	};
	this._nodeMap = {};
}

Graph.prototype = (function() {
	return {
		info: function() {
			return {
				nodes: this._nodes,
				connections: this._edges,
				conf: this._conf
			};
		},
		isEmpty: function() {
			return this._nodes.length == 0;
		},
		getNodeById: function(id) {
			return this._nodeMap[id];
		},
		removeNode: function(id) {
			// 删除节点
			var node = this._nodeMap[id];
			var idx = -1;
			for (var i = 0, len = this._nodes.length; i < len; i++) {
				if(this._nodes[i]['id'] == node['id']) {
					idx = i;
					break;
				}
			}
			if (idx > -1) {
				this._nodes.splice(idx, 1);
			}
			delete this._nodeMap[id];
			// 删除连接线
			this._edges = this._edges.filter(function(edge) {
				return edge.from.nodeId != id && edge.to.nodeId != id;
			});
		},
		addNode: function(node) {
			this._nodes.push(node);
			this._nodeMap[node.id] = node;
		},
		updateNode: function(node){
			var idx = -1;
			for (var i = 0, len = this._nodes.length; i < len; i++) {
				if(this._nodes[i]['id'] == node['id']) {
					idx = i;
					break;
				}
			}
			this._nodeMap[node.id]=node;
			this._nodes[idx]=node;
		},
		getNodes: function() {
			return this._nodes;
		},
		getEdges: function() {
			return this._edges;
		},
		edgeExist: function(edge) {
			if(edge.from.nodeId == edge.to.nodeId) {
				return true;
			}
			var flag = this._edges.some(function(item) {
				//return item.from.nodeId == edge.from.nodeId && 
				//      item.from.portIndex == edge.from.portIndex &&
				//      item.to.nodeId == edge.to.nodeId &&
				//      item.to.portIndex == edge.to.portIndex;
				       
				 return item.to.nodeId == edge.to.nodeId &&
				        item.to.portIndex == edge.to.portIndex;
			})
			return flag;
		},
		getEdgeByEndPoint: function(from, to) {
			var selected = this._edges.filter(function(edge) {
				return edge.from.nodeId == from.nodeId &&
				       edge.from.portIndex == from.portIndex &&
				       edge.to.nodeId == to.nodeId &&
				       edge.to.portIndex == to.portIndex;
			});
			return selected.length == 1 ? selected[0] : null;
		},
		getActiveEdgesByActiveNodes: function(activeNodes){
			
			// 将所有activeNode的ID存在一个数据中方便进行包含查询
			var activeNodesId = [];
			activeNodes.each(function(){
				activeNodesId.push(this.id);
			})
			
			// 过滤this._edge中前后(from/to)node均为active状态的edge 
			var edges = this._edges.filter(function(edge){
				return $.inArray(edge.from.nodeId,activeNodesId)!=-1 && $.inArray(edge.to.nodeId,activeNodesId)!=-1;
			})
			return edges;
		},
		removeEdge: function(from, to) {
			this._edges = this._edges.filter(function(edge) {
				return edge.from.nodeId != from.nodeId ||
			           edge.from.portIndex != from.portIndex ||
			           edge.to.nodeId != to.nodeId ||
			           edge.to.portIndex != to.portIndex;
			});
		},
		addEdge: function(edge) {
			this._edges.push(edge);
		},
		setConf: function(key, value) {
			this._conf[key] = value;
		},
		getConf: function(key) {
			if(key) {
				return this._conf[key];
			} else {
				return this._conf;
			}
		},
		removeConf: function(key) {
			delete this._conf[key];
		},
		isValidate: function() {
			var nodes = []; 
		    this._edges.forEach(function(v) {
				var from = v.from.nodeId;
				var to = v.to.nodeId;
				if (nodes.indexOf(from) == -1) {
					nodes.push(from);
				}
				if (nodes.indexOf(to) == -1) {
					nodes.push(to);
				}
			});
		    
		    return this._nodes.length == nodes.length;
		},
		clipGraph: function(){
			var nodes = []; 
		    this._edges.forEach(function(v) {
				var from = v.from.nodeId;
				var to = v.to.nodeId;
				if (nodes.indexOf(from) == -1) {
					nodes.push(from);
				}
				if (nodes.indexOf(to) == -1) {
					nodes.push(to);
				}
			});
		    this._nodes=this._nodes.filter(function(v){
		    	return nodes.includes(v.nodeId);
		    });
		    
		},
		tsort: function() {
			if(this._edges.length == 0 && this._nodes.length == 1) {
				return [this._nodes[0].id];
			}
			var nodes = {},
			    sorted = [],
			    visited = {};
			var Node = function(id) {
				this.id = id;
				this.afters = [];
			}
			
			this._edges.forEach(function(v) {
				var from = v.from.nodeId;
				var to = v.to.nodeId;
				if(!nodes[from]) nodes[from] = new Node(from);
				if(!nodes[to]) nodes[to] = new Node(to);
				nodes[from].afters.push(to);
			});
			
			Object.keys(nodes).forEach(function visit(idstr, ancestors) {
				var node = nodes[idstr],
				    id = node.id;
				
				if(visited[idstr]) return;
				if (!Array.isArray(ancestors)) ancestors = [];
				ancestors.push(id);
				visited[idstr] = true;
				node.afters.forEach(function(afterID) {
					if (ancestors.indexOf(afterID) >= 0)  // if already in ancestors, a closed chain exists.
				        throw new Error('closed chain : ' +  afterID + ' is in ' + id);
				    visit(afterID.toString(), ancestors.map(function(v) { return v })); // recursive call
				});
				sorted.unshift(id);
			});
			return sorted;
		}
	}
})();