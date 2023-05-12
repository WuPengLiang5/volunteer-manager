package com.wpl.volunteer.vo;

import lombok.Data;

@Data
public class VolunteerInfoVo {
    private Integer id;
    private String name;
    private String gender;
    private String idNumber;
    private String politic;
    private String occupation;
    private String location;
    private String address;

    private String username;
    private String phone;
    private String email;
    private String avatar;
}
