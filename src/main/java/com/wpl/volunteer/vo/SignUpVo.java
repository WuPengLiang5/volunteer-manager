package com.wpl.volunteer.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class SignUpVo {
    private String id;
    private Integer activityId;
    private Integer userId;
    private LocalDateTime registrationTime;
    private Integer state;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal duration;


    //用户姓名
    private String name;

    private String title;
    private String contact;
    private String contactPhone;
}
