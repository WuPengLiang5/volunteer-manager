package com.wpl.volunteer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Admin {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String username;
    private String password;
    private String salt;
    private String name;
    private String phone;
    private String email;
    private String introduction;
    private Integer roleId;

    @TableLogic
    private Integer isDelete;
}
