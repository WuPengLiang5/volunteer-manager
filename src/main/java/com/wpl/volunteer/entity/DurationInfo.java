package com.wpl.volunteer.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author WuPengLiang
 * @since 2022-05-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DurationInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer activityId;

    // 活动的标题
    @TableField(exist = false)
    private String title;

    private Integer userId;

    // 备注
    private String remarks;

    // 时长
    private BigDecimal duration;

    // 添加方式
    private String addMethod;

    // 状态
    private Integer state;

    // 创建时间
    private LocalDateTime createTime;
}
