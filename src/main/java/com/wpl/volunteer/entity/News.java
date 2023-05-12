package com.wpl.volunteer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class News{

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    // 标题
    private String title;
    // 创建者
    private String creator;
    // 发布时间
    private LocalDateTime createTime;
    // 内容
    private String content;
    // 来源
    private String source;
    // 类型
    private String newsType;
    // 活动封面
    private String cover;
    // 新闻状态（是否审核）
    private Integer state;

    @TableField(exist = false)
    private String typeName;

    @TableLogic
    private Integer isDelete;
}
