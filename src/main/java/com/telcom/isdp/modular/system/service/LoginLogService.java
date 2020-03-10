package com.telcom.isdp.modular.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.telcom.isdp.modular.system.entity.LoginLog;
import com.telcom.isdp.modular.system.mapper.LoginLogMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class LoginLogService extends ServiceImpl<LoginLogMapper, LoginLog> {

    /**
     * 获取登录日志列表
     *
     * @author fengshuonan
     * @Date 2018/12/23 5:53 PM
     */
    public List<Map<String, Object>> getLoginLogs(Page page, String beginTime, String endTime, String logName) {
        return this.baseMapper.getLoginLogs(page, beginTime, endTime, logName);
    }
}
