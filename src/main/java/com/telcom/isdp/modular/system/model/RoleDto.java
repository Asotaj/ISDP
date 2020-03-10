package com.telcom.isdp.modular.system.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class RoleDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long roleId;
    /**
     * 父角色id
     */
    private Long pid;
    /**
     * 角色名称
     */
    private String name;
    /**
     * 提示
     */
    private String description;
    /**
     * 序号
     */
    private Integer sort;
}
