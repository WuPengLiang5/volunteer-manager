package com.wpl.volunteer.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DurationInfoVo {
    private Integer id;
    private String remarks;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal duration;
    private String addMethod;
    private Integer state;
    private LocalDateTime createTime;

    private String vNumber;
    private String username;
    private String name;
    private String gender;
    private String phone;
    private String email;
    private String address;
}
