package com.telcom.isdp.modular.system.controller;

import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.roses.kernel.model.exception.RequestEmptyException;
import com.telcom.isdp.core.common.node.ZTreeNode;
import com.telcom.isdp.core.common.page.LayuiPageInfo;
import com.telcom.isdp.modular.system.entity.Dict;
import com.telcom.isdp.modular.system.entity.DictType;
import com.telcom.isdp.modular.system.model.params.DictParam;
import com.telcom.isdp.modular.system.model.result.DictResult;
import com.telcom.isdp.modular.system.service.DictService;
import com.telcom.isdp.modular.system.service.DictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
@RequestMapping("/dict")
public class DictController extends BaseController {

    private String PREFIX = "/modular/system/dict";

    @Autowired
    private DictService dictService;

    @Autowired
    private DictTypeService dictTypeService;

    @RequestMapping("")
    public String index(@RequestParam("dictTypeId") Long dictTypeId, Model model) {
        model.addAttribute("dictTypeId", dictTypeId);

        //获取type的名称
        DictType dictType = dictTypeService.getById(dictTypeId);
        if (dictType == null) {
            throw new RequestEmptyException();
        }
        model.addAttribute("dictTypeName", dictType.getName());

        return PREFIX + "/dict.html";
    }

    @RequestMapping("/add")
    public String add(@RequestParam("dictTypeId") Long dictTypeId, Model model) {
        model.addAttribute("dictTypeId", dictTypeId);

        //获取type的名称
        DictType dictType = dictTypeService.getById(dictTypeId);
        if (dictType == null) {
            throw new RequestEmptyException();
        }

        model.addAttribute("dictTypeName", dictType.getName());
        return PREFIX + "/dict_add.html";
    }


    @RequestMapping("/edit")
    public String edit(@RequestParam("dictId") Long dictId, Model model) {

        //获取type的id
        Dict dict = dictService.getById(dictId);
        if (dict == null) {
            throw new RequestEmptyException();
        }

        //获取type的名称
        DictType dictType = dictTypeService.getById(dict.getDictTypeId());
        if (dictType == null) {
            throw new RequestEmptyException();
        }

        model.addAttribute("dictTypeId", dict.getDictTypeId());
        model.addAttribute("dictTypeName", dictType.getName());

        return PREFIX + "/dict_edit.html";
    }

    @RequestMapping("/addItem")
    @ResponseBody
    public ResponseData addItem(DictParam dictParam) {
        this.dictService.add(dictParam);
        return ResponseData.success();
    }

    @RequestMapping("/editItem")
    @ResponseBody
    public ResponseData editItem(DictParam dictParam) {
        this.dictService.update(dictParam);
        return ResponseData.success();
    }

    @RequestMapping("/delete")
    @ResponseBody
    public ResponseData delete(DictParam dictParam) {
        this.dictService.delete(dictParam);
        return ResponseData.success();
    }

    @RequestMapping("/detail")
    @ResponseBody
    public ResponseData detail(DictParam dictParam) {
        DictResult dictResult = this.dictService.dictDetail(dictParam.getDictId());
        return ResponseData.success(dictResult);
    }

    @ResponseBody
    @RequestMapping("/list")
    public LayuiPageInfo list(DictParam dictParam) {
        return this.dictService.findPageBySpec(dictParam);
    }

    @RequestMapping(value = "/ztree")
    @ResponseBody
    public List<ZTreeNode> ztree(@RequestParam("dictTypeId") Long dictTypeId, @RequestParam(value = "dictId", required = false) Long dictId) {
        return this.dictService.dictTreeList(dictTypeId, dictId);
    }

}


