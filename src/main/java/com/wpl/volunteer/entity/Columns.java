package com.wpl.volunteer.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author WuPengLiang
 * @since 2022-05-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Columns implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String pid;

    private String title;

    private String name;

    private String sort;

    private String newsSort;
}
