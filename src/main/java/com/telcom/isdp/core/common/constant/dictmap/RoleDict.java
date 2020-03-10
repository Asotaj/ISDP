package com.telcom.isdp.core.common.constant.dictmap;

import com.telcom.isdp.core.common.constant.dictmap.base.AbstractDictMap;

public class RoleDict extends AbstractDictMap {

    @Override
    public void init() {
        put("roleId", "角色名称");
        put("sort", "角色排序");
        put("pid", "角色的父级");
        put("name", "角色名称");
        put("description", "备注");
        put("ids", "资源名称");
    }

    @Override
    protected void initBeWrapped() {
        putFieldWrapperMethodName("pid", "getSingleRoleName");
        putFieldWrapperMethodName("deptId", "getDeptName");
        putFieldWrapperMethodName("roleId", "getSingleRoleName");
        putFieldWrapperMethodName("ids", "getMenuNames");
    }
}
