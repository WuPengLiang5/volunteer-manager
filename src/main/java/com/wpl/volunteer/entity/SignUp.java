package com.wpl.volunteer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class SignUp {
    @TableId(value = "id",type = IdType.UUID)
    private String id;
    private Integer activityId;
    private Integer userId;
    private LocalDateTime registrationTime;
    private Integer state;
    private String auditReason;
    private BigDecimal duration;

    @TableLogic
    private Integer isDelete;
}
