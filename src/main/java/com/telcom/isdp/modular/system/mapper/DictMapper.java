package com.telcom.isdp.modular.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.telcom.isdp.core.common.node.ZTreeNode;
import com.telcom.isdp.modular.system.entity.Dict;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface DictMapper extends BaseMapper<Dict> {

    /**
     * 获取ztree的节点列表
     */
    List<ZTreeNode> dictTree(@Param("dictTypeId") Long dictTypeId);

    /**
     * where parentIds like ''
     */
    List<Dict> likeParentIds(@Param("dictId") Long dictId);
}
