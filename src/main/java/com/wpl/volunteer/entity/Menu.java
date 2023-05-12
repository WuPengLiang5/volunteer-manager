package com.wpl.volunteer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Menu {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private Integer pid;
    private String path;
    private String component;
    private String redirect;
    private String name;
    private String title;
    private String icon;
    private Integer hidden;
    private Integer type;
    private String perms;

    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private List<Menu> children;
}
