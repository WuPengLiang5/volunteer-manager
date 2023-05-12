package com.wpl.volunteer.vo;

import com.wpl.volunteer.entity.Role;
import lombok.Data;

@Data
public class AdminInfoVo {
    private Integer id;
    private String username;
    private String password;
    private String name;
    private String phone;
    private String email;
    private String introduction;
    private Integer roleId;
    private Integer isDelete;
}
