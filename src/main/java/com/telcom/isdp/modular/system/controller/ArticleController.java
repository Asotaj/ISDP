package com.telcom.isdp.modular.system.controller;

import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.telcom.isdp.core.common.annotion.BussinessLog;
import com.telcom.isdp.core.common.exception.BizExceptionEnum;
import com.telcom.isdp.modular.system.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/article")
public class ArticleController extends BaseController {

    private String PREFIX = "/modular/system/article";

    @Autowired
    private ArticleService articleService;

    /**
     * 删除管理员（逻辑删除）
     *
     * @author fengshuonan
     * @Date 2018/12/24 22:44
     */

    @BussinessLog(value = "删除", key = "id")
    @ResponseBody
    public ResponseData delete(@RequestParam Long id) {
        if (ToolUtil.isEmpty(id)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.articleService.fakeDelete(id);
        return SUCCESS_TIP;
    }



}
