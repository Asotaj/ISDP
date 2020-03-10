package com.telcom.isdp.modular.system.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

public class ArticleDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long Id;
    private String title;
    private String source;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;
    private String author;
    private String category;
    private Integer like_article_count;
    private String keywords;
}
