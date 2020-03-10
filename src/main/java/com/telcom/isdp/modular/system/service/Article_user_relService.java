package com.telcom.isdp.modular.system.service;

import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.telcom.isdp.core.common.exception.BizExceptionEnum;
import com.telcom.isdp.core.common.page.LayuiPageFactory;
import com.telcom.isdp.modular.system.entity.Article_user_rel;
import com.telcom.isdp.modular.system.mapper.Article_user_relMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class Article_user_relService extends ServiceImpl<Article_user_relMapper, Article_user_rel> {
    @Resource
    private Article_user_relMapper article_user_relMapper;
    /**
     * 新增部门
     *
     */
    public List<Map<String, Object>> selectArticle_user_rel() {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectArticle_user_rel();
    }

    public List<Map<String, Object>> selectByUser(Long user_id) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByUser(user_id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void addArticle_user_rel(Article_user_rel article_user_rel) {

        if (ToolUtil.isOneEmpty(article_user_rel, article_user_rel.getId(), article_user_rel.getUser_id(), article_user_rel.getArticle_id(), article_user_rel.getUser_name())) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.save(article_user_rel);
    }

    @Transactional
    public void deleteArticle_user_rel(Long user_id,Long article_id) {
        this.baseMapper.deleteArticle_user_rel(user_id,article_id);
    }

}
