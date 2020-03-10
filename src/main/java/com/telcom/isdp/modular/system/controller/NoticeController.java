package com.telcom.isdp.modular.system.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.telcom.isdp.core.common.annotion.BussinessLog;
import com.telcom.isdp.core.common.constant.dictmap.DeleteDict;
import com.telcom.isdp.core.common.constant.dictmap.NoticeMap;
import com.telcom.isdp.core.common.constant.factory.ConstantFactory;
import com.telcom.isdp.core.common.exception.BizExceptionEnum;
import com.telcom.isdp.core.common.page.LayuiPageFactory;
import com.telcom.isdp.core.log.LogObjectHolder;
import com.telcom.isdp.core.shiro.ShiroKit;
import com.telcom.isdp.modular.system.entity.Notice;
import com.telcom.isdp.modular.system.service.NoticeService;
import com.telcom.isdp.modular.system.warpper.NoticeWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.Map;

@Controller
@RequestMapping("/notice")
public class NoticeController extends BaseController {

    private String PREFIX = "/modular/system/notice/";

    @Autowired
    private NoticeService noticeService;

    @RequestMapping("")
    public String index() {
        return PREFIX + "notice.html";
    }

    @RequestMapping("/notice_add")
    public String noticeAdd() {
        return PREFIX + "notice_add.html";
    }

    @RequestMapping("/notice_update/{noticeId}")
    public String noticeUpdate(@PathVariable Long noticeId, Model model) {
        Notice notice = this.noticeService.getById(noticeId);
        model.addAllAttributes(BeanUtil.beanToMap(notice));
        LogObjectHolder.me().set(notice);
        return PREFIX + "notice_edit.html";
    }

    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        Page<Map<String, Object>> list = this.noticeService.list(condition);
        Page<Map<String, Object>> wrap = new NoticeWrapper(list).wrap();
        return LayuiPageFactory.createPageInfo(wrap);
    }

    @RequestMapping(value = "/add")
    @ResponseBody
    @BussinessLog(value = "新增通知", key = "title", dict = NoticeMap.class)
    public Object add(Notice notice) {
        if (ToolUtil.isOneEmpty(notice, notice.getTitle(), notice.getContent())) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        notice.setCreateUser(ShiroKit.getUserNotNull().getId());
        notice.setCreateTime(new Date());
        this.noticeService.save(notice);
        return SUCCESS_TIP;
    }

    @RequestMapping(value = "/delete")
    @ResponseBody
    @BussinessLog(value = "删除通知", key = "noticeId", dict = DeleteDict.class)
    public Object delete(@RequestParam Long noticeId) {

        //缓存通知名称
        LogObjectHolder.me().set(ConstantFactory.me().getNoticeTitle(noticeId));

        this.noticeService.removeById(noticeId);

        return SUCCESS_TIP;
    }

    @RequestMapping(value = "/update")
    @ResponseBody
    @BussinessLog(value = "修改通知", key = "title", dict = NoticeMap.class)
    public Object update(Notice notice) {
        if (ToolUtil.isOneEmpty(notice, notice.getNoticeId(), notice.getTitle(), notice.getContent())) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        Notice old = this.noticeService.getById(notice.getNoticeId());
        old.setTitle(notice.getTitle());
        old.setContent(notice.getContent());
        this.noticeService.updateById(old);
        return SUCCESS_TIP;
    }

}
