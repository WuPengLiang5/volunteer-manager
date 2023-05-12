package com.wpl.volunteer.vo;

import com.wpl.volunteer.entity.Volunteer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VolunteerVo extends Volunteer {
    private String username;
    private String phone;
    private String email;
    private LocalDateTime registerTime;
    private String avatar;
}
