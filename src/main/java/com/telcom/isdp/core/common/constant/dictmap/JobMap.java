package com.telcom.isdp.core.common.constant.dictmap;

import com.telcom.isdp.core.common.constant.dictmap.base.AbstractDictMap;

public class JobMap extends AbstractDictMap {
    @Override
    public void init() {
        put("jobId", "ID");
        put("jobName", "作业名称");
    }

    @Override
    protected void initBeWrapped() {

    }
}
