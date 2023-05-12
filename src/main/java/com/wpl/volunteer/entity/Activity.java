package com.wpl.volunteer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Activity {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    // 活动标题
    private String title;
    // 活动介绍
    private String introduction;
    // 开始时间
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime beginTime;
    // 结束时间
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
    // 开始报名时间
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime registrationBegin;
    // 结束报名时间
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime registrationEnd;
    // 活动地点
    private String location;
    // 报名人数上限
    private String registerMax;
    // 招募人数
    private String recruitNumber;
    // 活动封面
    private String cover;
    // 活动状态
    private Integer state;

    @TableLogic
    private Integer isDelete;

    private String contact;
    private String contactPhone;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
