package com.wpl.volunteer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "sys_dict")
public class Dict {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String type;
    private String typeName;
    private String code;
    private String name;
    private String parentType;
    private String parentCode;
    private Integer sort;
    private Integer editable;
    private Integer enable;
    private String remark;
    private Integer createBy;
    private LocalDateTime createTime;
    private Integer updateBy;
    private LocalDateTime updateTime;
    @TableLogic
    private Integer isDelete;
}
