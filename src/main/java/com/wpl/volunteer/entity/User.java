package com.wpl.volunteer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String username;
    private String password;
    private String salt;
    private String phone;
    private String email;
    private LocalDateTime registerTime;
    private LocalDateTime loginTime;
    private String loginIp;
    private String avatar;
    @TableLogic
    private Integer isDelete;
}
