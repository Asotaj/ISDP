package com.telcom.isdp.modular.system.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.telcom.isdp.modular.system.entity.Regular;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Map;

public interface RegularMapper extends BaseMapper<Regular> {

    /**
     * 查询列表
     * @param page
     * @param condition
     * @return
     */
    Page<Map<String, Object>> selectRegular(@Param("page") Page page, @Param("condition") String condition);

    Page<Map<String, Object>> selectRegularByCon(@Param("page") Page page, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("website") String website, @Param("category") String category);
    List selectRegularByShow();
    List selectRegularByGZ();
//    int showRegularCount();
    /**
     * 删除
     *
     */
    int deleteRegularById(@Param("id") Long id);



}
