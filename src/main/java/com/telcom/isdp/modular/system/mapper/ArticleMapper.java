package com.telcom.isdp.modular.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.telcom.isdp.modular.system.entity.Article;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
public interface ArticleMapper extends BaseMapper<Article> {
    /**
     * 查询列表
     * @param page
     * @param condition
     * @return
     */
    Page<Map<String, Object>> selectArticle(@Param("page") Page page, @Param("condition") String condition);

    Page<Map<String, Object>> selectArticleByCon(@Param("page") Page page, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("source") String[] source, @Param("category") String[] category);

    Page<Map<String, Object>> selectArticleBySave(@Param("page") Page page,@Param("saved") List saved);

    List selectArticleByList(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("source") String[] source, @Param("category") String[] category);

    Page<Map<String, Object>> selectArticleByfxSJ(@Param("page") Page page, @Param("titleCondition") String titleCondition);

    /**selectArticleByfxSJ
     * 删除
     *
     */
    int deleteArticleById(@Param("Id") Long Id);

    Page<Map<String, Object>> selectArticleByOne(@Param("page") Page page,@Param("Id") Long Id);
    /**
     * 获取列表
     */
    Page<Map<String, Object>> list(@Param("page") Page page, @Param("condition") String condition, @Param("id") Long id);

    List<Article> selectArticleByKeyword(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("keyword") String keyword);

}
