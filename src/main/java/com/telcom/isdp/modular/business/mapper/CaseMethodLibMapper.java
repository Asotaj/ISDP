package com.telcom.isdp.modular.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.telcom.isdp.modular.business.entity.CaseMethodLib;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

/**
 * Mapper接口
 * @author zhangpengfei
 * @since 2019-12-10
 */
@Mapper
public interface CaseMethodLibMapper extends BaseMapper<CaseMethodLib> {

    @Select("${sql}")
    public Map<String,String> selectBySql(@Param("sql") String sql);

}
