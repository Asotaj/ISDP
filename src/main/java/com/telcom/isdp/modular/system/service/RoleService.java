package com.telcom.isdp.modular.system.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.RequestEmptyException;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.telcom.isdp.core.common.constant.Const;
import com.telcom.isdp.core.common.constant.cache.Cache;
import com.telcom.isdp.core.common.constant.factory.ConstantFactory;
import com.telcom.isdp.core.common.exception.BizExceptionEnum;
import com.telcom.isdp.core.common.node.ZTreeNode;
import com.telcom.isdp.core.common.page.LayuiPageFactory;
import com.telcom.isdp.core.log.LogObjectHolder;
import com.telcom.isdp.core.util.CacheUtil;
import com.telcom.isdp.modular.system.entity.Relation;
import com.telcom.isdp.modular.system.entity.Role;
import com.telcom.isdp.modular.system.mapper.RelationMapper;
import com.telcom.isdp.modular.system.mapper.RoleMapper;
import com.telcom.isdp.modular.system.model.RoleDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


@Service
public class RoleService extends ServiceImpl<RoleMapper, Role> {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RelationMapper relationMapper;

    @Resource
    private UserService userService;

    @Transactional(rollbackFor = Exception.class)
    public void addRole(Role role) {

        if (ToolUtil.isOneEmpty(role, role.getName(), role.getPid(), role.getDescription())) {
            throw new RequestEmptyException();
        }

        role.setRoleId(null);

        this.save(role);
    }


    @Transactional(rollbackFor = Exception.class)
    public void editRole(RoleDto roleDto) {

        if (ToolUtil.isOneEmpty(roleDto, roleDto.getName(), roleDto.getPid(), roleDto.getDescription())) {
            throw new RequestEmptyException();
        }

        Role old = this.getById(roleDto.getRoleId());
        BeanUtil.copyProperties(roleDto, old);
        this.updateById(old);

        //删除缓存
        CacheUtil.removeAll(Cache.CONSTANT);
    }


    @Transactional(rollbackFor = Exception.class)
    public void setAuthority(Long roleId, String ids) {

        // 删除该角色所有的权限
        this.roleMapper.deleteRolesById(roleId);

        // 添加新的权限
        for (Long id : Convert.toLongArray(ids.split(","))) {
            Relation relation = new Relation();
            relation.setRoleId(roleId);
            relation.setMenuId(id);
            this.relationMapper.insert(relation);
        }

        // 刷新当前用户的权限
        userService.refreshCurrentUser();
    }


    @Transactional(rollbackFor = Exception.class)
    public void delRoleById(Long roleId) {

        if (ToolUtil.isEmpty(roleId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }

        //不能删除超级管理员角色
        if (roleId.equals(Const.ADMIN_ROLE_ID)) {
            throw new ServiceException(BizExceptionEnum.CANT_DELETE_ADMIN);
        }

        //缓存被删除的角色名称
        LogObjectHolder.me().set(ConstantFactory.me().getSingleRoleName(roleId));

        //删除角色
        this.roleMapper.deleteById(roleId);

        //删除该角色所有的权限
        this.roleMapper.deleteRolesById(roleId);

        //删除缓存
        CacheUtil.removeAll(Cache.CONSTANT);
    }

    public Page<Map<String, Object>> selectRoles(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectRoles(page, condition);
    }

    public int deleteRolesById(Long roleId) {
        return this.baseMapper.deleteRolesById(roleId);
    }

    public List<ZTreeNode> roleTreeList() {
        return this.baseMapper.roleTreeList();
    }

    public List<ZTreeNode> roleTreeListByRoleId(Long[] roleId) {
        return this.baseMapper.roleTreeListByRoleId(roleId);
    }

}
