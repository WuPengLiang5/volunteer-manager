package com.wpl.volunteer.dto;

import lombok.Data;

@Data
public class Registrants {
    //账号信息
    private String username;
    private String password;
    private String phone;
    private String verifyCode;
    private String avatar;

    //基本信息
    private String name;
    private String idNumber;
    private String gender;
    private String politic;
    private String occupation;
    private String location;
    private String address;
}
