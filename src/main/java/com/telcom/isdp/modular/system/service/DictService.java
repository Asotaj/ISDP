package com.telcom.isdp.modular.system.service;

import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.RequestEmptyException;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.telcom.isdp.core.common.constant.state.CommonStatus;
import com.telcom.isdp.core.common.exception.BizExceptionEnum;
import com.telcom.isdp.core.common.node.ZTreeNode;
import com.telcom.isdp.core.common.page.LayuiPageFactory;
import com.telcom.isdp.core.common.page.LayuiPageInfo;
import com.telcom.isdp.modular.system.entity.Dict;
import com.telcom.isdp.modular.system.mapper.DictMapper;
import com.telcom.isdp.modular.system.model.params.DictParam;
import com.telcom.isdp.modular.system.model.result.DictResult;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Service
public class DictService extends ServiceImpl<DictMapper, Dict> {

    public void add(DictParam param) {

        //判断是否已经存在同编码或同名称字典
        QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
        dictQueryWrapper
                .and(i -> i.eq("code", param.getCode()).or().eq("name", param.getName()))
                .and(i -> i.eq("dict_type_id", param.getDictTypeId()));
        List<Dict> list = this.list(dictQueryWrapper);
        if (list != null && list.size() > 0) {
            throw new ServiceException(BizExceptionEnum.DICT_EXISTED);
        }

        Dict entity = getEntity(param);

        //设置pids
        dictSetPids(entity);

        //设置状态
        entity.setStatus(CommonStatus.ENABLE.getCode());

        this.save(entity);
    }


    public void delete(DictParam param) {

        //删除字典的所有子级
        List<Long> subIds = getSubIds(param.getDictId());
        if (subIds.size() > 0) {
            this.removeByIds(subIds);
        }

        this.removeById(getKey(param));
    }


    public void update(DictParam param) {
        Dict oldEntity = getOldEntity(param);
        Dict newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);

        //判断编码是否重复
        QueryWrapper<Dict> wrapper = new QueryWrapper<Dict>()
                .and(i -> i.eq("code", newEntity.getCode()).or().eq("name", newEntity.getName()))
                .and(i -> i.ne("dict_id", newEntity.getDictId()))
                .and(i -> i.eq("dict_type_id", param.getDictTypeId()));
        int dicts = this.count(wrapper);
        if (dicts > 0) {
            throw new ServiceException(BizExceptionEnum.DICT_EXISTED);
        }

        //设置pids
        dictSetPids(newEntity);

        this.updateById(newEntity);
    }

    public DictResult findBySpec(DictParam param) {
        return null;
    }

    public List<DictResult> findListBySpec(DictParam param) {
        return null;
    }

    public LayuiPageInfo findPageBySpec(DictParam param) {
        QueryWrapper<Dict> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("dict_type_id", param.getDictTypeId());

        if (ToolUtil.isNotEmpty(param.getCondition())) {
            objectQueryWrapper.and(i -> i.eq("code", param.getCondition()).or().eq("name", param.getCondition()));
        }

        objectQueryWrapper.orderByAsc("sort");

        List<Dict> list = this.list(objectQueryWrapper);

        //创建根节点
        Dict dict = new Dict();
        dict.setName("根节点");
        dict.setDictId(0L);
        dict.setParentId(-999L);
        list.add(dict);

        LayuiPageInfo result = new LayuiPageInfo();
        result.setData(list);

        return result;
    }

    public List<ZTreeNode> dictTreeList(Long dictTypeId, Long dictId) {
        if (dictTypeId == null) {
            throw new RequestEmptyException();
        }

        List<ZTreeNode> tree = this.baseMapper.dictTree(dictTypeId);

        //获取dict的所有子节点
        List<Long> subIds = getSubIds(dictId);

        //如果传了dictId，则在返回结果里去掉
        List<ZTreeNode> resultTree = new ArrayList<>();
        for (ZTreeNode zTreeNode : tree) {

            //如果dictId等于树节点的某个id则去除
            if (ToolUtil.isNotEmpty(dictId) && dictId.equals(zTreeNode.getId())) {
                continue;
            }
            if (subIds.contains(zTreeNode.getId())) {
                continue;
            }
            resultTree.add(zTreeNode);
        }

        resultTree.add(ZTreeNode.createParent());

        return resultTree;
    }

    public DictResult dictDetail(Long dictId) {
        if (ToolUtil.isEmpty(dictId)) {
            throw new RequestEmptyException();
        }

        DictResult dictResult = new DictResult();

        //查询字典
        Dict detail = this.getById(dictId);
        if (detail == null) {
            throw new RequestEmptyException();
        }

        //查询父级字典
        if (ToolUtil.isNotEmpty(detail.getParentId())) {
            Long parentId = detail.getParentId();
            Dict dictType = this.getById(parentId);
            if (dictType != null) {
                dictResult.setParentName(dictType.getName());
            } else {
                dictResult.setParentName("无父级");
            }
        }

        ToolUtil.copyProperties(detail, dictResult);

        return dictResult;
    }

    private Serializable getKey(DictParam param) {
        return param.getDictId();
    }

    private Page getPageContext() {
        return LayuiPageFactory.defaultPage();
    }

    private Dict getOldEntity(DictParam param) {
        return this.getById(getKey(param));
    }

    private Dict getEntity(DictParam param) {
        Dict entity = new Dict();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    private List<Long> getSubIds(Long dictId) {

        ArrayList<Long> longs = new ArrayList<>();

        if(ToolUtil.isEmpty(dictId)){
            return longs;
        }else{
            List<Dict> list = this.baseMapper.likeParentIds(dictId);
            for (Dict dict : list) {
                longs.add(dict.getDictId());
            }
            return longs;
        }
    }

    private void dictSetPids(Dict param) {
        if (param.getParentId().equals(0L)) {
            param.setParentIds("[0]");
        } else {
            //获取父级的pids
            Long parentId = param.getParentId();
            Dict parent = this.getById(parentId);
            if (parent == null) {
                param.setParentIds("[0]");
            } else {
                param.setParentIds(parent.getParentIds() + "," + "[" + parentId + "]");
            }
        }
    }
}
