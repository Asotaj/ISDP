package com.telcom.isdp.modular.business.controller;

import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.telcom.isdp.core.common.exception.BizExceptionEnum;
import com.telcom.isdp.core.common.page.LayuiPageFactory;
import com.telcom.isdp.core.common.page.LayuiPageInfo;
import com.telcom.isdp.core.log.LogObjectHolder;
import com.telcom.isdp.core.shiro.ShiroKit;
import com.telcom.isdp.core.shiro.ShiroUser;
import com.telcom.isdp.modular.system.entity.Article;
import com.telcom.isdp.modular.system.entity.Article_user_rel;
import com.telcom.isdp.modular.system.entity.Regular;
import com.telcom.isdp.modular.system.service.ArticleService;
import com.telcom.isdp.modular.system.service.Article_user_relService;
import com.telcom.isdp.modular.system.service.UserService;
import com.telcom.isdp.modular.system.warpper.ArticleWrapper;
import jdk.nashorn.internal.ir.ReturnNode;
import org.apache.commons.collections.ArrayStack;
import org.apache.commons.collections.list.AbstractLinkedList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

import java.util.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;



/**
 * @author jiayq
 * @date 2019/11/7 10:33
 */
@Controller
@RequestMapping("/intelligence")
public class IntelligenceController extends BaseController {
    private String PREFIX = "/modular/business/intelligence/";
    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    @Autowired
    private Article_user_relService article_user_relService;


    /**
     * 璺宠浆鍒版儏鎶ラ椤�
     *
     * @author jiayq
     * @date 2019/11/7 10:32
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "intelligence.html";
    }

    /**
     * 鑾峰彇鎵�湁閮ㄩ棬鍒楄〃
     *
     * @author fengshuonan
     * @Date 2018/12/23 4:57 PM
     */
    @RequestMapping("article/list")
    @ResponseBody
    public LayuiPageInfo articleList(
            @RequestParam(value = "startTime",required = false) String startTime,
            @RequestParam(value = "endTime",required = false) String endTime,
            @RequestParam(value = "website",required = false) String website,
            @RequestParam(value = "category",required = false) String category) {
        ShiroUser user = ShiroKit.getUserNotNull();
        Long user_id = user.getId();
        String[] source = null;
        String[] categorys = null;
        if(website!=null && website!=""){
            source = website.split(",");
        }
        if(category!=null && category!=""){
            categorys = category.split(",");
        }

        Page<Map<String, Object>> roles = articleService.selectArticleByCon(startTime,endTime,source,categorys);
        List<Map<String, Object>> list_Article_user_rel=article_user_relService.selectArticle_user_rel();
        List<Map<String, Object>> list= roles.getRecords();
        for(int i=0;i<list.size();i++){
            Map<String, Object> obj= list.get(i);
            Long id=Long.parseLong(obj.get("Id")+"");
            obj.put("is_save","未收藏");
            for (Map<String, Object> strs : list_Article_user_rel) {
                if(id.equals(strs.get("article_id"))&&user_id.equals(strs.get("user_id"))){
                    obj.put("is_save","已收藏");
                    break;
                }
            }
        }
        Page<Map<String, Object>> wrap = new ArticleWrapper(roles).wrap();
        return LayuiPageFactory.createPageInfo(wrap);
    }

    @RequestMapping("/article/listByCon")
    @ResponseBody
    public LayuiPageInfo articleListByCon(
            @RequestParam(value = "startTime",required = false) String startTime,
            @RequestParam(value = "endTime",required = false) String endTime,
            @RequestParam(value = "website",required = false) String website,
            @RequestParam(value = "category",required = false) String category) {
        System.out.println(startTime+"---"+endTime+"-----"+website+"-----"+category);

        String[] source = null;
        String[] categorys = null;
        if(website!=null && website!=""){
            source = website.split(",");
        }
        if(category!=null && category!=""){
            categorys = category.split(",");
        }

        Page<Map<String, Object>> roles = articleService.selectArticleByCon(startTime,endTime,source,categorys);
        Page<Map<String, Object>> wrap = new ArticleWrapper(roles).wrap();

        return LayuiPageFactory.createPageInfo(wrap);
    }

    /**
     * 列出收藏的文章
     * @return
     */
    @RequestMapping("article/listBySave")
    @ResponseBody
    public LayuiPageInfo listBySave(){
        ShiroUser user = ShiroKit.getUserNotNull();
        Long user_id = user.getId();

        //该用户收藏的文章
        List<Map<String, Object>> list_Article_user_rel=article_user_relService.selectByUser(user_id);

        //记录该用户收藏的文章的id
        List<Object> saved = new ArrayList<>();
        for(Map<String, Object> rel : list_Article_user_rel){
            saved.add(rel.get("article_id"));
        }

        //查出该用户收藏的文章内容
        Page<Map<String, Object>> articles = articleService.selectArticleBySave(saved);
        for (Map<String, Object> obj : articles.getRecords()){
            obj.put("is_save","已收藏");
        }

        Page<Map<String, Object>> wrap = new ArticleWrapper(articles).wrap();

        return LayuiPageFactory.createPageInfo(wrap);
    }


    /**
     * 情报的关键词柱状图
     * @param page
     * @param limit
     * @param startTime
     * @param endTime
     * @param website
     * @param category
     * @return
     */
    @RequestMapping("/article/listByList")
    @ResponseBody
    public Map  selectArticleByList(
            @RequestParam(value = "page",required = false) String page,
            @RequestParam(value = "limit",required = false) String limit,
            @RequestParam(value = "startTime",required = false) String startTime,
            @RequestParam(value = "endTime",required = false) String endTime,
            @RequestParam(value = "website",required = false) String website,
            @RequestParam(value = "category",required = false) String category) {
        String[] source = null;
        String[] categorys = null;
        if(website!=null && website!=""){
            source = website.split(",");
        }
        if(category!=null && category!=""){
            categorys = category.split(",");
        }
        List<Map<String, Object>> roles = articleService.selectArticleByList(startTime,endTime,source,categorys);
        Map maparray =null;
        if(roles.size()>0){
            StringBuffer sbuff=new StringBuffer();
            for(int i=0;i<roles.size();i++){
                sbuff.append(roles.get(i).get("keywords")+",");
            }
            String[] sbtritem=sbuff.toString().split(",");
            Map<String, Integer> map = new HashMap<>();

            for (String str : sbtritem) {
                Integer num = map.get(str);
                map.put(str, num == null ? 1 : num + 1);
            }

            Iterator it01 = map.keySet().iterator();
            Map mapitem=new HashMap();
            while (it01.hasNext()) {
                Object key = it01.next();

                mapitem.put(key,map.get(key));

            }
            List <String>linval= revsort(mapitem,20);
            List <Integer>linvalInteger=new ArrayList<Integer>();
            String[] array = linval.toArray(new String[0]);
            for (String strs : array) {
                Integer nums = map.get(strs);
                linvalInteger.add(nums);
            }
            maparray = new HashMap<>();
            maparray.put("list1",linval);
            maparray.put("list2",linvalInteger);
        }

        return maparray;
    }

    public static <K,V extends Comparable<? super V>> List<K> revsort(Map<K, V> map,int num){
        List<Map.Entry<K, V>> list = new ArrayList<Map.Entry<K,V>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {

            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return (o2.getValue()).compareTo( o1.getValue() );
            }
        });
        List<K> result = new ArrayList<K>();
        if(num<0){
            result.add(list.get(num).getKey());
            for(Map.Entry<K, V> entry : list){
                result.add(entry.getKey());
            }
        }else{
            for(int i=0;i<num;i++){
                result.add(list.get(i).getKey());
            }
        }

        return result;
    }
    /**
     *

     * @Date 2018/12/24 22:44
     */
    @RequestMapping("/delete")
    @ResponseBody
    public ResponseData delete(@RequestParam Long id) {
        if (ToolUtil.isEmpty(id)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.articleService.fakeDelete(id);
        return SUCCESS_TIP;
    }

    /**
     * 璺宠浆鍒颁慨鏀归儴闂�
     *

     */
    @RequestMapping("/article_update")
    public String articleUpdate(@RequestParam Long id) {
        if (ToolUtil.isEmpty(id)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        Article role = this.articleService.getById(id);
        LogObjectHolder.me().set(role);
        return PREFIX + "intelligence_edit.html";
    }


    @RequestMapping(value = "/regular_fx")
    @ResponseBody
    public LayuiPageInfo regular_fx(Regular entity) {
        ShiroUser user = ShiroKit.getUserNotNull();
        Long user_id = user.getId();
        String regular = (entity.getRegular() + "+").replace(" ", "");
        StringBuffer result = new StringBuffer();
        char[] os = new char[100];
        int j = 0;
        StringBuffer temp = new StringBuffer();
        for (int i = 0; i < regular.replace(" ", "").length(); i++) {
            char c = regular.charAt(i); // 鑾峰彇涓�釜瀛楃

            if(c != '+' && c != '-' && c != '|' &&c != '(' &&c != ')'){
                temp.append(c); // 鍔犲叆鍒版暟瀛楃紦瀛樹腑
            } else { // 鎿嶄綔绗�
                os[j++] = c;
                String tempStr = temp.toString();
                if(tempStr.isEmpty()){
                    result.append(" " + c);
                    os[j++] = c;
                }else{
                    if(j -2 >= 0 && os[j-2] == '-'){
                        result.append("title NOT LIKE '%" + tempStr + "%'" + c);
                    }else{
                        result.append("title LIKE '%" + tempStr + "%'" + c);
                    }
                }
                temp = new StringBuffer(); // 閲嶇疆鏁板瓧缂撳瓨
            }
        }
        String s = result.toString();

        Page<Map<String, Object>> roles= articleService.selectArticleByfxSJ(s.substring(0, s.length() -1 ).replace("+", " AND ").replace("-", " AND ").replace("|", " OR "));
        List<Map<String, Object>> list_Article_user_rel=article_user_relService.selectArticle_user_rel();
        List<Map<String, Object>> list= roles.getRecords();

        for(int i=0;i < list.size();i++){
            Map<String, Object> obj= list.get(i);
            Long id=Long.parseLong(obj.get("Id")+"");
            obj.put("is_save","未收藏");
            for (Map<String, Object> strs : list_Article_user_rel) {
                if(id.equals(strs.get("article_id"))&&user_id.equals(strs.get("user_id"))){
                    obj.put("is_save","已收藏");
                }
            }
        }
        Page<Map<String, Object>> wrap = new ArticleWrapper(roles).wrap();

        return LayuiPageFactory.createPageInfo(wrap);

    }

    @RequestMapping("/article_update_Item")
    @ResponseBody
    public ResponseData articleUpdateItem(@RequestParam Long id) {

       // Page<Map<String, Object>> roles = this.articleService.selectArticleByOne(id);
        Article article =this.articleService.getById(id+"");
        //article.setId(Long.parseLong(roles.getRecords().get(0).get("id")+""));
        //System.out.println(roles.toString()+"---"+Long.parseLong(roles.getRecords().get(0).get("id")+""));
        return ResponseData.success(article);
    }


    @RequestMapping("/article_Save")
    @ResponseBody
    public ResponseData article_Save(@RequestParam Long article_id,@RequestParam Long is_deleted) {
        if (ToolUtil.isEmpty(article_id)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        ShiroUser user = ShiroKit.getUserNotNull();
        Long id = user.getId();
        String name=user.getName();
        Article_user_rel article_user_rel=new Article_user_rel();
        article_user_rel.setArticle_id(article_id);
        article_user_rel.setIs_deleted(0+"");
        article_user_rel.setUser_id(id);
        article_user_rel.setUser_name(name);
        article_user_relService.save(article_user_rel);

        return SUCCESS_TIP;
    }


    @RequestMapping("/article_delete")
    @ResponseBody
    public ResponseData article_delete(@RequestParam Long article_id,@RequestParam Long is_deleted) {
        if (ToolUtil.isEmpty(article_id)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        ShiroUser user = ShiroKit.getUserNotNull();
        Long id = user.getId();
        String name=user.getName();
        Article_user_rel article_user_rel=new Article_user_rel();
        article_user_rel.setArticle_id(article_id);
        article_user_rel.setIs_deleted(1+"");
        article_user_rel.setUser_id(id);
        article_user_rel.setUser_name(name);
        article_user_relService.deleteArticle_user_rel(id,article_id);

        return SUCCESS_TIP;
    }

    /**
     * 瑙掕壊淇敼
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:31 PM
     */

    @RequestMapping(value = "/edit")
    @ResponseBody
    public ResponseData edit(Article article) {
        this.articleService.editArticle(article);
        return SUCCESS_TIP;
    }

    /**
     * 鍏抽敭璇嶅浘琛�
     *
     * @author jiayq
     * @date 2019/11/7 10:32
     */
    @RequestMapping("/keyword/chart")
    public String edit(@RequestParam("keyword") String keyWord, Model model) {

       /* model.addAttribute("dictTypeId", dict.getDictTypeId());
        model.addAttribute("dictTypeName", dictType.getName());*/

        return PREFIX + "/key_word.html";
    }

    /**
     * 关键词环比界面(month-on-month)
     * @return
     */
    @RequestMapping("/keywordMoMPage")
    public String keywordMoMPage() {
        return PREFIX + "/keyword_mom.html";
    }

    @RequestMapping("/getKeywordMoM")
    @ResponseBody
    public Map getKeywordMoM(@RequestParam(value = "startTime") String startTime,
                             @RequestParam(value = "endTime") String endTime,
                             @RequestParam(value = "keywords") String keywords){
        Map<String,Object> result = new HashMap<>();
        List<String> dateKey = new ArrayList<>();
        Map<String,Object> dateValue = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date start = sdf.parse(startTime);
            Date end = sdf.parse(endTime);
            Calendar cal = Calendar.getInstance();
            cal.setTime(start);
            Date thisDate = cal.getTime();
            while(thisDate.before(end)){
                dateKey.add(sdf.format(thisDate));
                cal.setTime(thisDate);
                cal.add(Calendar.DAY_OF_MONTH,1);
                thisDate = cal.getTime();
            }
            dateKey.add(sdf.format(end));

            result.put("dateKey",dateKey);

            String [] keywordArray = keywords.split(",");
            for(int i=0;i<keywordArray.length;i++){
                Map<String,List> thisKeyMap = new HashMap<>();
                List<Integer> thisKeyData = new ArrayList<>();
                List<Double> thisKeyMom = new ArrayList<>();
                //初始化
                for(int j=0;j<dateKey.size();j++){
                    thisKeyData.add(0);
                    thisKeyMom.add(0.0);
                }
                List<Article> keywordMom = articleService.selectArticleByKeyword(startTime,endTime,keywordArray[i]);
                //该关键词每天的数量统计
                for(Article article :keywordMom){
                    String thisTime = sdf.format(article.getTime());
                    int index = dateKey.indexOf(thisTime);
                    Integer thisTimeNum = thisKeyData.get(index);
                    if(thisTimeNum!=null){
                        thisKeyData.set(index,thisTimeNum+1);
                    }
                }
                //环比计算
                for(int j=0;j<dateKey.size();j++){
                    int now = thisKeyData.get(j);
                    if(j==0){//首次，设为0
                        thisKeyMom.set(0,0.0);
                    }else{
                        int before = thisKeyData.get(j-1);
                        double mom;
                        if(before!=0){
                            mom = (now-before)/(double)before*1000;
                            mom = Math.round(mom)/10.0;   //保留一位小数
                        }else if(now==0){   //before和now都为0
                            mom=0.0;
                        }else{
                            mom=now*100;
                        }
                        thisKeyMom.set(j,mom);
                    }
                }
                thisKeyMap.put("data",thisKeyData);
                thisKeyMap.put("mom",thisKeyMom);
                dateValue.put(keywordArray[i],thisKeyMap);
            }
            result.put("dateValue",dateValue);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 相似文章界面
     * @param id
     * @return
     */
    @RequestMapping("/likeArticlePage")
    public String likeArticlePage(@RequestParam(value = "id") Long id){
        return PREFIX+"/likeArticles.html";
    }

    @RequestMapping("/getLikeArticles")
    @ResponseBody
    public Map getLikeArticles(@RequestParam(value = "id") Long id){
        Map<String,Object> result = new HashMap<>();
        QueryWrapper queryWrapper = new QueryWrapper(new Article());
        queryWrapper.eq("id",id);
        Article article = articleService.getOne(queryWrapper);
        String likeArticles = article.getLikeArticles();
        if(likeArticles!=null && !"".equals(likeArticles)){
            Map<String,Object> likeArticlesMap = JSON.parseObject(likeArticles);
            return likeArticlesMap;
//            int i =1;
//            for(String key:likeArticlesMap.keySet()){
//                Map<String,Object> temp = new HashMap<>();
//                temp.put("content",likeArticlesMap.get(key).toString());
//                temp.put("id",i);
//                likeArticlesMap.put(key,temp);
//                i++;
//            }
//            result.put("count",article.getLike_article_count());
//            result.put("data",likeArticlesMap);
        }else{
            return null;
//            result.put("count",0);
//            result.put("data",null);
        }
//        result.put("code",0);
//        result.put("msg","请求成功");
//        return result;
    }
}
