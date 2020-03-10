package com.telcom.isdp.modular.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.telcom.isdp.core.common.node.MenuNode;
import com.telcom.isdp.core.common.node.ZTreeNode;
import com.telcom.isdp.modular.system.entity.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


public interface MenuMapper extends BaseMapper<Menu> {


    Page<Map<String, Object>> selectMenus(@Param("page") Page page, @Param("condition") String condition, @Param("level") String level, @Param("menuId") Long menuId, @Param("code") String code);

    List<Long> getMenuIdsByRoleId(@Param("roleId") Long roleId);

    List<ZTreeNode> menuTreeList();

    List<ZTreeNode> menuTreeListByMenuIds(List<Long> menuIds);

    int deleteRelationByMenu(@Param("menuId") Long menuId);

    List<String> getResUrlsByRoleId(@Param("roleId") Long roleId);

    List<MenuNode> getMenusByRoleIds(List<Long> roleIds);

    List<Map<String, Object>> selectMenuTree(@Param("condition") String condition, @Param("level") String level);

    List<Menu> getMenusLikePcodes(@Param("code") String code);

}
