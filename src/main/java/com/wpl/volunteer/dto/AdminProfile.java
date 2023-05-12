package com.wpl.volunteer.dto;

import lombok.Data;

@Data
public class AdminProfile {
    private Integer id;
    private String username;
    private String name;
    private String phone;
    private String email;
    private String introduction;
}
