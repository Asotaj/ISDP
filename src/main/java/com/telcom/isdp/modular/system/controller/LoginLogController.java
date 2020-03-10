package com.telcom.isdp.modular.system.controller;

import cn.stylefeng.roses.core.base.controller.BaseController;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SqlRunner;
import com.telcom.isdp.core.common.annotion.BussinessLog;
import com.telcom.isdp.core.common.annotion.Permission;
import com.telcom.isdp.core.common.constant.Const;
import com.telcom.isdp.core.common.page.LayuiPageFactory;
import com.telcom.isdp.modular.system.service.LoginLogService;
import com.telcom.isdp.modular.system.warpper.LogWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/loginLog")
public class LoginLogController extends BaseController {

    private static String PREFIX = "/modular/system/log/";

    @Autowired
    private LoginLogService loginLogService;

    @RequestMapping("")
    public String index() {
        return PREFIX + "login_log.html";
    }

    @RequestMapping("/list")
    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public Object list(@RequestParam(required = false) String beginTime,
                       @RequestParam(required = false) String endTime,
                       @RequestParam(required = false) String logName) {

        //获取分页参数
        Page page = LayuiPageFactory.defaultPage();

        //根据条件查询日志
        List<Map<String, Object>> result = loginLogService.getLoginLogs(page, beginTime, endTime, logName);
        page.setRecords(new LogWrapper(result).wrap());

        return LayuiPageFactory.createPageInfo(page);
    }

    @BussinessLog("清空登录日志")
    @RequestMapping("/delLoginLog")
    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public Object delLog() {
        SqlRunner.db().delete("delete from sys_login_log");
        return SUCCESS_TIP;
    }
}
