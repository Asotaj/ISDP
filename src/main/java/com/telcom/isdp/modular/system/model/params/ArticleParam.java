package com.telcom.isdp.modular.system.model.params;

import cn.stylefeng.roses.kernel.model.validator.BaseValidatingParam;

import java.io.Serializable;
import java.util.Date;

public class ArticleParam implements Serializable, BaseValidatingParam {
    private static final long serialVersionUID = 1L;

    private Long Id;

    private String title;

    private String url;

    private String source;

    private Date time;

    private String content;

    private String website;

    private String author;

    private String keywords;

    private String abstracts;

    private String category;

    private Integer like_article_count;

    private Integer is_deleted;

    @Override
    public String checkParam() {
        return null;
    }
}
