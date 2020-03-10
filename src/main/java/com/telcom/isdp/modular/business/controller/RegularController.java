package com.telcom.isdp.modular.business.controller;

import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.telcom.isdp.core.common.exception.BizExceptionEnum;
import com.telcom.isdp.core.common.page.LayuiPageFactory;
import com.telcom.isdp.core.common.page.LayuiPageInfo;
import com.telcom.isdp.core.log.LogObjectHolder;
import com.telcom.isdp.core.shiro.ShiroKit;
import com.telcom.isdp.core.shiro.ShiroUser;
import com.telcom.isdp.modular.system.entity.Regular;
import com.telcom.isdp.modular.system.service.RegularService;
import com.telcom.isdp.modular.system.warpper.RegularWrapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/regular")
public class RegularController  extends BaseController {

    private String PREFIX = "/modular/business/regular/";
    @Autowired
     private RegularService regularService;

    @RequestMapping("")
    public String index() {
        return PREFIX + "regular.html";
    }

    @RequestMapping("/regular_addItem")
    public String regular_addItem() {

        return PREFIX + "regular_addGz.html";
    }

    @RequestMapping(value = "/regular_add")
    public String regular_add(Regular regular, Model model) {
        Regular regu=new Regular();
        regu.setRegular(regular.getRegular());
        model.addAttribute("regular", regu);

        return PREFIX + "regular_add.html";
    }
    @RequestMapping("/regular_update")
    public String regular_update() {
        return PREFIX + "regular_edit.html";
    }

    @RequestMapping(value = "/add")
    @ResponseBody
    public ResponseData add(Regular regular) {
        Regular regu=new Regular();

        ShiroUser user = ShiroKit.getUserNotNull();
        Long id = user.getId();
        String name=user.getName();
        regu.setCreater_id(id);

        regu.setIs_deleted(0);
        regu.setCreater(name);

        regu.setName(regular.getName());
        regu.setRegular(regular.getRegular());
        System.out.println(regu);
        this.regularService.save(regu);
        return SUCCESS_TIP;
    }
    @RequestMapping(value = "/additem")
    @ResponseBody
    public ResponseData additem(Regular regular) {
        List<Map<String, Object>> roles= this.regularService.selectRegularByGZ();
        roles.get(0).get("");
        this.regularService.save(regular);
        return SUCCESS_TIP;
    }

    @RequestMapping(value = "/addItemData")
    @ResponseBody
    public ResponseData addItemData(Regular regular) {
       System.out.println(regular);
        ShiroUser user = ShiroKit.getUserNotNull();
        Long id = user.getId();
        String name=user.getName();
        Regular regulars=new Regular();
        regulars.setCreater_id(id);

        regulars.setRegular(regular.getRegular());
        regulars.setName(regular.getName());
        regulars.setIs_deleted(0);
        regulars.setCreater(name);
        this.regularService.save(regulars);
        return SUCCESS_TIP;
    }

    /**
     * 查找要展示的规则（最多8个）
     * @return
     */
    @RequestMapping(value = "/selectByShow")
    @ResponseBody
    public  List<Map<String, Object>> selectByShow() {
        List<Map<String, Object>> roles= this.regularService.selectRegularByShow();
        return roles;
    }

    /**
     * 展示规则（最大8个）
     * @param id
     * @return
     */
    @RequestMapping(value = "/showItem")
    @ResponseBody
    public String showItem(@RequestParam(value = "id") Long id){
        QueryWrapper countWrapper = new QueryWrapper(new Regular());
        countWrapper.eq("is_show",1);
        countWrapper.eq("is_deleted",0);
        int count = regularService.count(countWrapper);
        if(count<8){
            QueryWrapper wrapper = new QueryWrapper(new Regular());
            wrapper.eq("id",id);
            Regular regular = regularService.getOne(wrapper);
            regular.setIs_show(1);
            regularService.saveOrUpdate(regular);
            return "success";
        }else{
            return "error";
        }
    }

    @RequestMapping("/hideItem")
    @ResponseBody
    public ResponseData hideItem(@RequestParam(value = "id") Long id){
        QueryWrapper wrapper = new QueryWrapper(new Regular());
        wrapper.eq("id",id);
        Regular regular = regularService.getOne(wrapper);
        regular.setIs_show(0);
        regularService.saveOrUpdate(regular);
        return SUCCESS_TIP;
    }

    @RequestMapping("/list")
    @ResponseBody
    public LayuiPageInfo regularList(@RequestParam(value = "condition", required = false) String condition) {
        Page<Map<String, Object>> roles = regularService.selectRegular(condition);
        Page<Map<String, Object>> wrap = new RegularWrapper(roles).wrap();

        return LayuiPageFactory.createPageInfo(wrap);
    }

    @RequestMapping(value = "/edit")
    @ResponseBody
    public ResponseData edit(@RequestBody JSONObject obj) {
        Regular regulars= this.regularService.getById(obj.getString("id"));
        regulars.setName(obj.getString("name"));
        regulars.setRegular(obj.getString("regular"));
        this.regularService.updateById(regulars);
        System.out.println(obj.toString());
        return SUCCESS_TIP;
    }

    @RequestMapping("/regular_update_Item")
    @ResponseBody
    public ResponseData regularUpdateItem(@RequestParam Long id) {
        if (ToolUtil.isEmpty(id)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        Regular role = this.regularService.getById(id);
        LogObjectHolder.me().set(role);
        return ResponseData.success(role);
    }

    @RequestMapping("/delete")
    @ResponseBody
    public ResponseData delete(@RequestParam String id) {
        System.out.println(id+"---------");

        Regular regular= this.regularService.getById(id);
        System.out.println(regular.toString()+"---------");
        regular.setIs_deleted(1);
        this.regularService.updateById(regular);
        return SUCCESS_TIP;
    }



}
