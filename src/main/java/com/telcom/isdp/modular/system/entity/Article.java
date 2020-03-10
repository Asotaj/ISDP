package com.telcom.isdp.modular.system.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *  * 管理员表
 *  * </p>
 */
@TableName("article")
public class Article  implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long Id;
    /**
     * 标题
     */
    @TableField("title")
    private String title;
    /**
     * 来源
     */
    @TableField("url")
    private String url;
    /**
     *
     */
    @TableField("source")
    private String source;

    /**
     * 添加时间, fill = FieldFill.INSERT
     */
    @TableField(value = "time")
    private Date time;

    /**
     * 文本
     */
    @TableField("content")
    private String content;

    @TableField("website")
    private String website;

    @TableField("author")
    private String author;

    @TableField("keywords")
    private String keywords;

    @TableField("abstract")
    private String abstracts;

    @TableField("category")
    private String category;

    @TableField("like_article_count")
    private Integer like_article_count;

    @TableField("is_deleted")
    private Integer is_deleted;

    @TableField("visits")
    private Integer visits;

    @TableField("like_articles")
    private String likeArticles;



    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getAbstracts() {
        return abstracts;
    }

    public void setAbstracts(String abstracts) {
        this.abstracts = abstracts;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getLike_article_count() {
        return like_article_count;
    }

    public void setLike_article_count(Integer like_article_count) {
        this.like_article_count = like_article_count;
    }

    public Integer getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(Integer is_deleted) {
        this.is_deleted = is_deleted;
    }

    public Integer getVisits() {
        return visits;
    }

    public void setVisits(Integer visits) {
        this.visits = visits;
    }

    public String getLikeArticles() {
        return likeArticles;
    }

    public void setLikeArticles(String likeArticles) {
        this.likeArticles = likeArticles;
    }


    @Override
    public String toString() {
        return "Article{" +
                "Id=" + Id +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", source='" + source + '\'' +
                ", time=" + time +
                ", content='" + content + '\'' +
                ", website='" + website + '\'' +
                ", author='" + author + '\'' +
                ", keywords='" + keywords + '\'' +
                ", abstracts='" + abstracts + '\'' +
                ", category='" + category + '\'' +
                ", like_article_count=" + like_article_count +
                ", is_deleted=" + is_deleted +
                '}';
    }
}
