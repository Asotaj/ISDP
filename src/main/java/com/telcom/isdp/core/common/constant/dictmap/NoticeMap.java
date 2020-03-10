package com.telcom.isdp.core.common.constant.dictmap;

import com.telcom.isdp.core.common.constant.dictmap.base.AbstractDictMap;

public class NoticeMap extends AbstractDictMap {

    @Override
    public void init() {
        put("noticeId", "标题id");
        put("title", "标题");
        put("content", "内容");
    }

    @Override
    protected void initBeWrapped() {
    }
}
