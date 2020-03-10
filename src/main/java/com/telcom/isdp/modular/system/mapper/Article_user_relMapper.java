package com.telcom.isdp.modular.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.telcom.isdp.modular.system.entity.Article_user_rel;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface Article_user_relMapper extends BaseMapper<Article_user_rel> {
    List<Map<String, Object>> selectArticle_user_rel();

    List<Map<String, Object>> selectByUser(@Param("user_id") Long user_id);

    int deleteArticle_user_rel(@Param("user_id") Long user_id,@Param("article_id") Long article_id);
}
