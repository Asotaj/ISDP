package com.telcom.isdp.modular.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.telcom.isdp.core.common.page.LayuiPageFactory;
import com.telcom.isdp.modular.system.entity.Notice;
import com.telcom.isdp.modular.system.mapper.NoticeMapper;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class NoticeService extends ServiceImpl<NoticeMapper, Notice> {

    public Page<Map<String, Object>> list(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.list(page, condition);
    }
}
