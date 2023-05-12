package com.wpl.volunteer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Volunteer {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    // 用户id
    private Integer uid;
    private String vNumber;
    //用户姓名
    private String name;
    private String gender;
    private String idNumber;
    //政治面貌
    private String politic;
    //职业
    private String occupation;
    //所在地省份
    private String location;
    //详细地址
    private String address;
    //服务时长
    private BigDecimal durations;

    @TableLogic
    private Integer isDelete;
}
