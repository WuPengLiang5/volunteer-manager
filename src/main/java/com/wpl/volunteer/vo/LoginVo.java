package com.wpl.volunteer.vo;

import lombok.Data;

@Data
public class LoginVo {
    private Integer id;
    private String username;
    private String avatar;
    private String token;

    private Integer roleId;
}
