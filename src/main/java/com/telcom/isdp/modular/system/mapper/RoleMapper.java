package com.telcom.isdp.modular.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.telcom.isdp.core.common.node.ZTreeNode;
import com.telcom.isdp.modular.system.entity.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface RoleMapper extends BaseMapper<Role> {

    Page<Map<String, Object>> selectRoles(@Param("page") Page page, @Param("condition") String condition);

    int deleteRolesById(@Param("roleId") Long roleId);

    List<ZTreeNode> roleTreeList();

    List<ZTreeNode> roleTreeListByRoleId(Long[] roleId);

}
