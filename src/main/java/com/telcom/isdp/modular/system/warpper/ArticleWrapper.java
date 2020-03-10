package com.telcom.isdp.modular.system.warpper;

import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Map;

public class ArticleWrapper extends BaseControllerWrapper {

    public ArticleWrapper(Map<String, Object> single) {
        super(single);
    }

    public ArticleWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    public ArticleWrapper(Page<Map<String, Object>> page) {
        super(page);
    }

    public ArticleWrapper(PageResult<Map<String, Object>> pageResult) {
        super(pageResult);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {

       // map.put("title", ConstantFactory.me().getDeptName(DecimalUtil.getLong(map.get("title"))));

    }
}
