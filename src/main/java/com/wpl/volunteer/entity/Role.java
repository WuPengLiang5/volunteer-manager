package com.wpl.volunteer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Role implements Serializable{

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String name;
    private String nameZh;
    private String description;
    private String isSys;
    private Integer state;

    @TableField(exist = false)
    List<Menu> menus;
}
