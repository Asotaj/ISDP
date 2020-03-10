package com.telcom.isdp.modular.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

@TableName("regular")
public class Regular implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long Id;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    /**
     *
     */
    @TableField("regular")
    private String regular;

    /**
     *
     */
    @TableField("creater_id")
    private Long creater_id;

    /**
     *
     */
    @TableField("creater")
    private String creater;


    @TableField("is_deleted")
    private Integer is_deleted;

    @TableField("is_show")
    private Integer is_show;



    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegular() {
        return regular;
    }

    public void setRegular(String regular) {
        this.regular = regular;
    }

    public Long getCreater_id() {
        return creater_id;
    }

    public void setCreater_id(Long creater_id) {
        this.creater_id = creater_id;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public Integer getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(Integer is_deleted) {
        this.is_deleted = is_deleted;
    }

    public Integer getIs_show() {
        return is_show;
    }

    public void setIs_show(Integer is_show) {
        this.is_show = is_show;
    }
}
