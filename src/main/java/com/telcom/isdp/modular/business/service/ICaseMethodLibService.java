package com.telcom.isdp.modular.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.telcom.isdp.modular.business.entity.CaseMethodLib;

import java.util.Map;

/**
 * 服务类
 * @author zhangpengfei
 * @since 2019-12-19
 */

public interface ICaseMethodLibService extends IService<CaseMethodLib> {

    public Map<String,String> selectBySql(String sql);
}
