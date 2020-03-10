package com.telcom.isdp.modular.system.service;

import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.RequestEmptyException;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.telcom.isdp.core.common.constant.cache.Cache;
import com.telcom.isdp.core.common.exception.BizExceptionEnum;
import com.telcom.isdp.core.common.page.LayuiPageFactory;
import com.telcom.isdp.core.util.CacheUtil;
import com.telcom.isdp.modular.system.entity.Regular;
import com.telcom.isdp.modular.system.mapper.RegularMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
@Service
public class RegularService extends ServiceImpl<RegularMapper, Regular> {

    @Resource
    private RegularMapper regularMapper;

    public Page<Map<String, Object>> selectRegular(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectRegular(page, condition);
    }


    @Transactional
    public void deleteRegular(Long id) {
        Regular regular = regularMapper.selectById(id);


        this.removeById(regular.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    public void editRegular(Regular regular) {

        if (ToolUtil.isOneEmpty(regular, regular.getId(), regular.getName(), regular.getCreater_id(),regular.getCreater(),regular.getIs_deleted())) {
            throw new RequestEmptyException();
        }

        this.updateById(regular);

        //删除缓存
        CacheUtil.removeAll(Cache.CONSTANT);
    }
    public List selectRegularByShow(){
        return this.baseMapper.selectRegularByShow();
    }

    public List selectRegularByGZ(){
        return this.baseMapper.selectRegularByGZ();
    }
    @Transactional(rollbackFor = Exception.class)
    public void addRegular(Regular regular) {

        if (ToolUtil.isOneEmpty(regular, regular.getId(), regular.getName(), regular.getCreater_id(),regular.getCreater(),regular.getIs_deleted())) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.save(regular);
    }
}










