package com.telcom.isdp.modular.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.telcom.isdp.modular.system.entity.OperationLog;
import com.telcom.isdp.modular.system.mapper.OperationLogMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OperationLogService extends ServiceImpl<OperationLogMapper, OperationLog> {

    public List<Map<String, Object>> getOperationLogs(Page page, String beginTime, String endTime, String logName, String s) {
        return this.baseMapper.getOperationLogs(page, beginTime, endTime, logName, s);
    }

}
