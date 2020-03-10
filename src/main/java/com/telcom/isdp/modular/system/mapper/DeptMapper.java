package com.telcom.isdp.modular.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.telcom.isdp.core.common.node.TreeviewNode;
import com.telcom.isdp.core.common.node.ZTreeNode;
import com.telcom.isdp.modular.system.entity.Dept;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface DeptMapper extends BaseMapper<Dept> {

    /**
     * 获取ztree的节点列表
     */
    List<ZTreeNode> tree();

    /**
     * 获取所有部门列表
     */
    Page<Map<String, Object>> list(@Param("page") Page page, @Param("condition") String condition, @Param("deptId") Long deptId);

    /**
     * 获取所有部门树列表
     */
    List<TreeviewNode> treeviewNodes();

    /**
     * where pids like ''
     */
    List<Dept> likePids(@Param("deptId") Long deptId);
}
