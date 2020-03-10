package com.telcom.isdp.modular.business.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sun.org.apache.regexp.internal.RE;
import com.telcom.isdp.modular.business.entity.CaseMethodLib;
import com.telcom.isdp.modular.business.service.ICaseMethodLibService;
import com.telcom.isdp.modular.system.entity.Article;
import com.telcom.isdp.modular.system.entity.Dict;
import com.telcom.isdp.modular.system.service.ArticleService;
import com.telcom.isdp.modular.system.service.DictService;
import org.aspectj.apache.bcel.generic.RET;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * 首页
 * @author zhangpengfei
 * @since 2019-12-11
 */
@Controller
@RequestMapping("/console")
public class ConsoleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ICaseMethodLibService caseMethodLibService;

    @Autowired
    private DictService dictService;

    private String PREFIX = "/modular/frame/";

    @ResponseBody
    @RequestMapping("/init")
    public Map<String,Map> init(){
        QueryWrapper dictWrapper = new QueryWrapper(new Dict());
        dictWrapper.eq("dict_type_id","1192261339146403841");
        List<Dict> dicts = dictService.list(dictWrapper);
        List<String> categorys = new ArrayList<>();
        for(Dict dict:dicts){
            if(dict.getCode()!=""){
                categorys.add(dict.getCode());
            }
        }

        QueryWrapper wrapper = new QueryWrapper(new Article());
        wrapper.le("time",LocalDate.now().minusDays(160L));
        wrapper.ge("time",LocalDate.now().minusDays(175L));
        wrapper.orderByDesc("visits");
        List<Article> articles = articleService.list(wrapper);

        System.out.println("**************************"+articles.size());

        Map<String,Map> results = new HashMap<>();                  //最终整合的返回结果

        Map<String,List> result1 = new HashMap<>();                 //各分类15天文章数
        Map<String,Map<String,Integer>> result2 = new HashMap<>();  //各分类热门关键词统计
        Map<String,List<Map>> result3 = new HashMap<>();            //各分类热点文章

        //初始化
        for(int i = 0;i<categorys.size();i++){
            result1.put(categorys.get(i),new ArrayList<Integer>());
            result2.put(categorys.get(i),new HashMap<>());
            result3.put(categorys.get(i),new ArrayList<>());
            for(int j=0;j<15;j++){
                result1.get(categorys.get(i)).add(0);
            }
        }
        result3.put("hot",new ArrayList<>());

        //筛选、统计
        for(int i = 0;i<articles.size();i++){
            String[] categoryList = articles.get(i).getCategory().split(",");
            for(String category:categoryList){
                if (category!=null && result1.get(category)!=null){
                    //15天每天不同分类的文章数
                    LocalDate now = LocalDate.now();
//                    LocalDate before =  articles.get(i).getTime().toLocalDate();
                    java.util.Date date = articles.get(i).getTime();
                    Instant instant = date.toInstant();
                    ZoneId zone = ZoneId.systemDefault();
                    LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
                    LocalDate before = localDateTime.toLocalDate();

                    int days = (int)(now.toEpochDay()-before.toEpochDay());  //日期差值（正数）
                    int num = (int)result1.get(category).get(14-(days-161));    //翻转 list元素，使下标 0-14 代表 过去->现在
                    num+=1;
                    result1.get(category).set(14-(days-161),num);

                    //不同分类关键词
                    String keywords = articles.get(i).getKeywords();
                    if(keywords!=null && !"".equals(keywords)){
                        String[] temp = keywords.split(",");
                        for(int j=0;j<temp.length;j++){
                            if(result2.get(category).get(temp[j])==null){
                                result2.get(category).put(temp[j],1);
                            }else{
                                result2.get(category).put(temp[j],result2.get(category).get(temp[j])+1);
                            }
                        }
                    }

                    //各分类热点文章
                    if(result3.get(category).size()<6){  //各类型也只要visit最高的6个
                        Map<String,String> temp = new HashMap<>();
                        temp.put("id",articles.get(i).getId().toString());
                        temp.put("title",articles.get(i).getTitle());
                        temp.put("visits",articles.get(i).getVisits().toString());
                        result3.get(category).add(temp);
                    }
                }
            }
            //整体热点文章
            if(i<6){    //hot只要visit最高的6个
                Map<String,String> temp = new HashMap<>();
                temp.put("id",articles.get(i).getId().toString());
                temp.put("title",articles.get(i).getTitle());
                temp.put("visits",articles.get(i).getVisits().toString());
                result3.get("hot").add(temp);
            }
        }
        results.put("line",result1);
        results.put("cloud",result2);
        results.put("hot",result3);

        //方法库、案例库
        QueryWrapper wrapper2 = new QueryWrapper(new CaseMethodLib());
        wrapper2.le("upload_time",LocalDate.now().plusDays(1L));
        wrapper2.ge("upload_time",LocalDate.now().minusDays(15L));
        wrapper2.orderByDesc("read_count");
        wrapper2.eq("type","case");
        wrapper2.last(" limit 6 ");

        QueryWrapper wrapper3 = new QueryWrapper(new CaseMethodLib());
        wrapper3.le("upload_time",LocalDate.now().plusDays(1L));
        wrapper3.ge("upload_time",LocalDate.now().minusDays(15L));
        wrapper3.orderByDesc("read_count");
        wrapper3.eq("type","method");
        wrapper3.last(" limit 6 ");

        Map<String,List<CaseMethodLib>> result4 = new HashMap<>();
        List<CaseMethodLib> cases = caseMethodLibService.list(wrapper2);
        List<CaseMethodLib> methods = caseMethodLibService.list(wrapper3);

        result4.put("cases",cases);
        result4.put("methods",methods);

        results.put("tools",result4);

        return results;
    }

    @RequestMapping(value = "/getArticle",method = {RequestMethod.GET})
    public String getArticle(){
        return PREFIX+"article.html";
    }

    @ResponseBody
    @RequestMapping(value = "/initArticleDetail/{id}",method = {RequestMethod.GET})
    public Article initArticleDetail(@PathVariable("id") Object id){
        QueryWrapper queryWrapper = new QueryWrapper(new Article());
        queryWrapper.eq("id",id);
        Article article = articleService.getOne(queryWrapper);
        Integer visits = article.getVisits();
        article.setVisits(visits+1);
        articleService.saveOrUpdate(article);
        return article;
    }

    @RequestMapping("/getAllArticles")
    public String getAllArticles(){
        return PREFIX+"content.html";
    }

    @ResponseBody
    @RequestMapping("/initAllArticles")
    public List<Map> initAllArticles( @RequestParam("current") int current, @RequestParam("limit") int limit){
        QueryWrapper wrapper = new QueryWrapper(new Article());
        wrapper.le("time",LocalDate.now().minusDays(160L));
        wrapper.ge("time",LocalDate.now().minusDays(175L));
        wrapper.last(" limit "+limit+" offset " + (current-1)*limit);
        wrapper.orderByDesc("visits");

        List<Map> result = new ArrayList<>();
        List<Article> articles = articleService.list(wrapper);

        for(Article article : articles){
            Map<String,String> temp = new HashMap<>();
            temp.put("title",article.getTitle());
            SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" );
            temp.put("time",sdf.format(article.getTime()));
            temp.put("id",article.getId().toString());
            temp.put("category",article.getCategory());
            result.add(temp);
        }

        return result;
    }

    @ResponseBody
    @RequestMapping("/getArticleCount")
    public int getArticleCount(){
        QueryWrapper wrapper = new QueryWrapper(new Article());
        wrapper.le("time",LocalDate.now().minusDays(160L));
        wrapper.ge("time",LocalDate.now().minusDays(175L));
        return articleService.count(wrapper);
    }

    @ResponseBody
    @RequestMapping("/changeArticleType")
    public void changeArticleType(@RequestParam("id") Long id,
                                    @RequestParam("category") String category){
        Article article = articleService.getById(id);
        article.setCategory(category);
        articleService.saveOrUpdate(article);
    }

    @ResponseBody
    @RequestMapping("/test")
    public Object test(){
        try {
            //DataNtpUtil dataNtpUtil = new DataNtpUtil();
            //dataNtpUtil.dataNtp();
//            String temp = dataNtpUtil.getLikeArticle("");
//            return JSON.parse(temp);
//            return  dataNtpUtil.rtPostObject("虚商的未来市场机遇或将是万亿级物联网市场\",\"流量不清零、取消长途漫游费、取消流量漫游费等举措让运营商在资费套餐上越来越像虚商。这也意味着，虚商前期依靠的套餐资费灵活创新优势不再，虚商需要寻找全新的发展蓝海。快速增长的物联网市场为虚商提供全新机遇,物联网市场无疑将给虚商提供这一机遇。据分析机构数据显示，2020年全球物联网市场规模将突破1万亿美元。而中国市场，据工信部数据显示，物联网产业规模在2020年将突破1.5万亿元。不同于传统电信市场已见增长天花板的市场局面");
        }catch (Exception e){
            e.printStackTrace();
        }
        return "nothing";
    }
}
