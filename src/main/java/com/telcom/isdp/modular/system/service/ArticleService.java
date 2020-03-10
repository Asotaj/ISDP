package com.telcom.isdp.modular.system.service;

import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.RequestEmptyException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.telcom.isdp.core.common.constant.cache.Cache;
import com.telcom.isdp.core.common.page.LayuiPageFactory;
import com.telcom.isdp.core.common.page.LayuiPageInfo;
import com.telcom.isdp.core.util.CacheUtil;
import com.telcom.isdp.modular.system.entity.Article;
import com.telcom.isdp.modular.system.mapper.ArticleMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class ArticleService extends ServiceImpl<ArticleMapper, Article> {
    @Resource
    private ArticleMapper articleMapper;

    /**
     *selectArticle_user_rel
     *
     * @return
     * @date 2017年2月12日 下午9:14:34
     */
    public Page<Map<String, Object>> selectArticle(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectArticle(page, condition);
    }

    public Page<Map<String, Object>> selectArticleByCon(String startTime, String endTime, String[] source,String[] category) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectArticleByCon(page, startTime,endTime,source,category);
    }

    public List selectArticleByList(String startTime, String endTime, String[]  source,String[]  category){
        return this.baseMapper.selectArticleByList(startTime,endTime,source,category);
    }
    public  Page<Map<String, Object>> selectArticleByOne(Long Id){
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectArticleByOne(page,Id);
    }

    public  Page<Map<String, Object>> selectArticleBySave(List saved){
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectArticleBySave(page,saved);
    }

    public Page<Map<String, Object>> selectArticleByfxSJ(String titleCondition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectArticleByfxSJ(page, titleCondition);
    }

    //假删除
    public void fakeDelete(Long Id) {
        Article article = articleMapper.selectById(Id);
        article.setIs_deleted(1);
        this.saveOrUpdate(article);
    }

    //真删除
    @Transactional
    public void deleteArticle(Long Id) {
        Article article = articleMapper.selectById(Id);
        this.removeById(article.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    public void editArticle(Article article) {

        if (ToolUtil.isOneEmpty(article, article.getTitle(), article.getSource(), article.getTime(),article.getAuthor(),article.getCategory(),article.getKeywords(),article.getLike_article_count())) {
            throw new RequestEmptyException();
        }

        this.updateById(article);

        //删除缓存
        CacheUtil.removeAll(Cache.CONSTANT);
    }


    public Page<Map<String, Object>> list(String condition, Long Id) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.list(page, condition, Id);
    }

    /**
     * 查询分页数据，Specification模式
     *

     */
    public List<Article> findPageBySpec(Article param) {
        QueryWrapper<Article> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("id", param.getId());

        /*if (ToolUtil.isNotEmpty(param.getCondition())) {
            objectQueryWrapper.and(i -> i.eq("code", param.getCondition()).or().eq("name", param.getCondition()));
        }*/

        objectQueryWrapper.orderByAsc("sort");

        List<Article> list = this.list(objectQueryWrapper);
        LayuiPageInfo result = new LayuiPageInfo();
        result.setData(list);

        return list;
    }

    public List<Article> selectArticleByKeyword(String startTime,String endTime,String keyword){
        return this.baseMapper.selectArticleByKeyword(startTime,endTime,keyword);
    }

}
