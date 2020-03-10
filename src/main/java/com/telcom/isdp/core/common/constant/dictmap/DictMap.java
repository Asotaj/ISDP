package com.telcom.isdp.core.common.constant.dictmap;

import com.telcom.isdp.core.common.constant.dictmap.base.AbstractDictMap;

public class DictMap extends AbstractDictMap {

    @Override
    public void init() {
        put("dictId", "字典名称");
        put("name", "字典名称");
        put("code", "字典编码");
        put("description", "字典描述");
        put("sort", "排序");
    }

    @Override
    protected void initBeWrapped() {

    }
}
