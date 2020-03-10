package com.telcom.isdp.core.common.constant.dictmap;

import com.telcom.isdp.core.common.constant.dictmap.base.AbstractDictMap;

public class DeptDict extends AbstractDictMap {

    @Override
    public void init() {
        put("deptId", "部门名称");
        put("sort", "部门排序");
        put("pid", "上级名称");
        put("simpleName", "部门简称");
        put("fullName", "部门全称");
        put("description", "备注");
    }

    @Override
    protected void initBeWrapped() {
        putFieldWrapperMethodName("deptId", "getDeptName");
        putFieldWrapperMethodName("pid", "getDeptName");
    }
}
