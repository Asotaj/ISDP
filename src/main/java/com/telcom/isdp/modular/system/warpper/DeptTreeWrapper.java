package com.telcom.isdp.modular.system.warpper;

import com.telcom.isdp.core.common.node.TreeviewNode;

import java.util.List;

public class DeptTreeWrapper {

    public static void clearNull(List<TreeviewNode> list) {
        if (list == null) {
            return;
        } else {
            if (list.size() == 0) {
                return;
            } else {
                for (TreeviewNode node : list) {
                    if (node.getNodes() != null) {
                        if (node.getNodes().size() == 0) {
                            node.setNodes(null);
                        } else {
                            clearNull(node.getNodes());
                        }
                    }
                }
            }
        }
    }
}
