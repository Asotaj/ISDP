package com.telcom.isdp.modular.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.telcom.isdp.modular.business.entity.CaseMethodLib;
import com.telcom.isdp.modular.business.mapper.CaseMethodLibMapper;
import com.telcom.isdp.modular.business.service.ICaseMethodLibService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 服务实现类
 * @author zhangpengfei
 * @since 2019-12-10
 */
@Service
public class ICaseMethodLibServiceImpl extends ServiceImpl<CaseMethodLibMapper, CaseMethodLib> implements ICaseMethodLibService {

    @Autowired
    private CaseMethodLibMapper caseMethodLibMapper;

    public Map<String,String> selectBySql(String sql){
        return caseMethodLibMapper.selectBySql(sql);
    }
}
