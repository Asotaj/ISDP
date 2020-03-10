package com.telcom.isdp.modular.system.controller;

import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import com.telcom.isdp.core.common.page.LayuiPageInfo;
import com.telcom.isdp.modular.system.entity.DictType;
import com.telcom.isdp.modular.system.model.params.DictTypeParam;
import com.telcom.isdp.modular.system.service.DictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/dictType")
public class DictTypeController extends BaseController {

    private String PREFIX = "/modular/system/dictType";

    @Autowired
    private DictTypeService dictTypeService;

    @RequestMapping("")
    public String index() {
        return PREFIX + "/dictType.html";
    }

    @RequestMapping("/add")
    public String add() {
        return PREFIX + "/dictType_add.html";
    }

    @RequestMapping("/edit")
    public String edit() {
        return PREFIX + "/dictType_edit.html";
    }

    @RequestMapping("/addItem")
    @ResponseBody
    public ResponseData addItem(DictTypeParam dictTypeParam) {
        this.dictTypeService.add(dictTypeParam);
        return ResponseData.success();
    }

    @RequestMapping("/editItem")
    @ResponseBody
    public ResponseData editItem(DictTypeParam dictTypeParam) {
        this.dictTypeService.update(dictTypeParam);
        return ResponseData.success();
    }

    @RequestMapping("/delete")
    @ResponseBody
    public ResponseData delete(DictTypeParam dictTypeParam) {
        this.dictTypeService.delete(dictTypeParam);
        return ResponseData.success();
    }

    @RequestMapping("/detail")
    @ResponseBody
    public ResponseData detail(DictTypeParam dictTypeParam) {
        DictType detail = this.dictTypeService.getById(dictTypeParam.getDictTypeId());
        return ResponseData.success(detail);
    }

    @ResponseBody
    @RequestMapping("/list")
    public LayuiPageInfo list(DictTypeParam dictTypeParam) {
        return this.dictTypeService.findPageBySpec(dictTypeParam);
    }

}


